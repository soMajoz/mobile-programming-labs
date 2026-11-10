/**
 * Логика PWA «Список задач».
 * Сохраняет выданную в методичке модель MVC (TaskModel, TaskView, TaskController).
 * @author Быханов Михаил Сергеевич
 */

const STORAGE_KEY = "tasks_pwa";

/** Модель задач: чтение, изменение и сохранение списка в localStorage. */
class TaskModel {
  constructor() {
    this.tasks = this.loadTasks();
    this.nextId = this.tasks.reduce((next, task) => Math.max(next, task.id + 1), 1);
  }

  loadTasks() {
    try {
      const saved = JSON.parse(localStorage.getItem(STORAGE_KEY) || "[]");
      if (!Array.isArray(saved)) return [];
      return saved.filter((task) =>
        task &&
        Number.isSafeInteger(task.id) &&
        typeof task.title === "string" &&
        typeof task.completed === "boolean"
      );
    } catch (error) {
      console.warn("Сохранённый список задач повреждён и будет создан заново.", error);
      return [];
    }
  }

  saveTasks() {
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(this.tasks));
      return true;
    } catch (error) {
      console.error("Не удалось сохранить задачи в localStorage.", error);
      return false;
    }
  }

  getAll() {
    return this.tasks.map((task) => ({ ...task }));
  }

  add(title) {
    const task = {
      id: this.nextId++,
      title: title.trim(),
      completed: false,
      createdAt: Date.now()
    };
    this.tasks.push(task);
    this.saveTasks();
    return task;
  }

  toggle(id) {
    const task = this.tasks.find((item) => item.id === id);
    if (!task) return null;
    task.completed = !task.completed;
    this.saveTasks();
    return task;
  }

  delete(id) {
    const previousLength = this.tasks.length;
    this.tasks = this.tasks.filter((task) => task.id !== id);
    if (this.tasks.length !== previousLength) this.saveTasks();
  }

  getFiltered(filter) {
    if (filter === "active") return this.tasks.filter((task) => !task.completed);
    if (filter === "completed") return this.tasks.filter((task) => task.completed);
    return this.getAll();
  }

  getActiveCount() {
    return this.tasks.filter((task) => !task.completed).length;
  }
}

/** Представление списка и обработчиков пользовательского интерфейса. */
class TaskView {
  constructor() {
    this.taskList = document.getElementById("taskList");
    this.taskInput = document.getElementById("taskInput");
    this.taskForm = document.getElementById("taskForm");
    this.activeCount = document.getElementById("activeCount");
    this.totalCount = document.getElementById("totalCount");
    this.filterButtons = document.querySelectorAll(".filter-btn");
    this.offlineStatus = document.getElementById("offlineStatus");
    this.toast = document.getElementById("toast");
    this.toastTimer = null;
    this._onToggle = () => {};
    this._onDelete = () => {};
  }

  render(tasks, filter) {
    this.taskList.replaceChildren();
    if (tasks.length === 0) {
      const item = document.createElement("li");
      item.className = "empty-state";
      const icon = document.createElement("span");
      icon.className = "empty-icon";
      icon.setAttribute("aria-hidden", "true");
      icon.textContent = filter === "completed" ? "✓" : "＋";
      const title = document.createElement("p");
      title.textContent = filter === "active"
        ? "Все задачи выполнены"
        : filter === "completed"
          ? "Пока нет выполненных задач"
          : "Список пока пуст";
      const detail = document.createElement("p");
      detail.className = "empty-detail";
      detail.textContent = filter === "all" ? "Добавьте первое дело выше." : "Измените фильтр, чтобы увидеть другие задачи.";
      item.append(icon, title, detail);
      this.taskList.append(item);
      return;
    }

    tasks.forEach((task) => {
      const item = document.createElement("li");
      item.className = "task-item" + (task.completed ? " completed" : "");
      item.dataset.id = String(task.id);

      const checkbox = document.createElement("button");
      checkbox.className = "task-check";
      checkbox.type = "button";
      checkbox.textContent = "✓";
      checkbox.setAttribute("aria-label", task.completed ? "Вернуть задачу в активные" : "Отметить задачу выполненной");
      checkbox.setAttribute("aria-pressed", String(task.completed));
      checkbox.addEventListener("click", () => this._onToggle(task.id));

      const title = document.createElement("span");
      title.className = "task-title";
      title.textContent = task.title;

      const remove = document.createElement("button");
      remove.className = "task-delete";
      remove.type = "button";
      remove.textContent = "×";
      remove.setAttribute("aria-label", "Удалить задачу: " + task.title);
      remove.addEventListener("click", () => this._onDelete(task.id));

      item.append(checkbox, title, remove);
      this.taskList.append(item);
    });
  }

  updateCounter(active, total) {
    this.activeCount.textContent = String(active);
    this.totalCount.textContent = this.formatTaskCount(total);
  }

  formatTaskCount(count) {
    const remainder100 = count % 100;
    const remainder10 = count % 10;
    const word = remainder100 >= 11 && remainder100 <= 14
      ? "задач"
      : remainder10 === 1
        ? "задача"
        : remainder10 >= 2 && remainder10 <= 4
          ? "задачи"
          : "задач";
    return count + " " + word;
  }

  setActiveFilter(filter) {
    this.filterButtons.forEach((button) => {
      const isActive = button.dataset.filter === filter;
      button.classList.toggle("active", isActive);
      button.setAttribute("aria-pressed", String(isActive));
    });
  }

  setOfflineStatus(isAvailable) {
    this.offlineStatus.classList.toggle("ready", isAvailable);
    this.offlineStatus.classList.toggle("unavailable", !isAvailable);
    this.offlineStatus.querySelector(".connection-label").textContent =
      isAvailable ? "Доступно офлайн" : "Офлайн-кеш недоступен";
  }

  onAdd(callback) {
    this.taskForm.addEventListener("submit", (event) => {
      event.preventDefault();
      callback();
    });
  }

  onToggle(callback) {
    this._onToggle = callback;
  }

  onDelete(callback) {
    this._onDelete = callback;
  }

  onFilter(callback) {
    this.filterButtons.forEach((button) => {
      button.addEventListener("click", () => callback(button.dataset.filter));
    });
  }

  getTaskInput() {
    const value = this.taskInput.value.trim();
    this.taskInput.value = "";
    this.taskInput.focus();
    return value;
  }

  showToast(message) {
    this.toast.textContent = message;
    this.toast.classList.add("visible");
    window.clearTimeout(this.toastTimer);
    this.toastTimer = window.setTimeout(() => this.toast.classList.remove("visible"), 2600);
  }
}

/** Связывает модель и представление, обновляет сеть и регистрирует Service Worker. */
class TaskController {
  constructor() {
    this.model = new TaskModel();
    this.view = new TaskView();
    this.currentFilter = "all";
    this.installPrompt = null;
    this.init();
  }

  init() {
    this.view.onAdd(() => this.addTask());
    this.view.onToggle((id) => this.toggleTask(id));
    this.view.onDelete((id) => this.deleteTask(id));
    this.view.onFilter((filter) => this.changeFilter(filter));

    this.configureInstallation();
    this.registerServiceWorker();
    this.render();
  }

  addTask() {
    const title = this.view.getTaskInput();
    if (!title) {
      this.view.showToast("Введите название задачи");
      return;
    }
    this.model.add(title);
    this.currentFilter = "all";
    this.view.setActiveFilter("all");
    this.render();
    this.view.showToast("Задача добавлена");
  }

  toggleTask(id) {
    const task = this.model.toggle(id);
    this.render();
    if (task) this.view.showToast(task.completed ? "Задача выполнена" : "Задача снова активна");
  }

  deleteTask(id) {
    const task = this.model.tasks.find((item) => item.id === id);
    if (!task) return;
    this.model.delete(id);
    this.render();
    this.view.showToast("Задача удалена");
  }

  changeFilter(filter) {
    this.currentFilter = filter;
    this.view.setActiveFilter(filter);
    this.render();
  }

  render() {
    this.view.render(this.model.getFiltered(this.currentFilter), this.currentFilter);
    this.view.updateCounter(this.model.getActiveCount(), this.model.tasks.length);
  }

  configureInstallation() {
    const installButton = document.getElementById("installButton");
    window.addEventListener("beforeinstallprompt", (event) => {
      event.preventDefault();
      this.installPrompt = event;
      installButton.hidden = false;
    });
    installButton.addEventListener("click", async () => {
      if (!this.installPrompt) return;
      this.installPrompt.prompt();
      await this.installPrompt.userChoice;
      this.installPrompt = null;
      installButton.hidden = true;
    });
    window.addEventListener("appinstalled", () => {
      installButton.hidden = true;
      this.view.showToast("Приложение установлено");
    });
  }

  async registerServiceWorker() {
    if (!("serviceWorker" in navigator)) {
      console.info("Service Worker не поддерживается в этом браузере.");
      this.view.setOfflineStatus(false);
      return;
    }
    try {
      const registration = await navigator.serviceWorker.register("./sw.js", { scope: "./" });
      await navigator.serviceWorker.ready;
      this.view.setOfflineStatus(true);
      console.info("Service Worker зарегистрирован:", registration.scope);
      registration.addEventListener("updatefound", () => {
        const worker = registration.installing;
        if (!worker) return;
        worker.addEventListener("statechange", () => {
          if (worker.state === "installed" && navigator.serviceWorker.controller) {
            this.view.showToast("Доступна новая версия приложения");
          }
        });
      });
      registration.update().catch((error) => console.info("Проверка обновлений отложена.", error));
    } catch (error) {
      this.view.setOfflineStatus(false);
      console.warn("Не удалось зарегистрировать Service Worker.", error);
    }
  }
}

document.addEventListener("DOMContentLoaded", () => {
  const app = new TaskController();
  window.app = app;
});
