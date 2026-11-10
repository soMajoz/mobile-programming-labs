/**
 * Service Worker PWA.
 * Кеширует оболочку приложения, обновляет статические файлы и открывает список офлайн.
 * @author Быханов Михаил Сергеевич
 */

const CACHE_PREFIX = "pwa-todolist-";
const CACHE_NAME = CACHE_PREFIX + "v1.0.1";
const BASE_URL = new URL("./", self.location.href);
const APP_ENTRY = new URL("index.html", BASE_URL).href;
const ASSETS = [
  "",
  "index.html",
  "css/style.css",
  "js/app.js",
  "manifest.json",
  "icons/icon-192.png",
  "icons/icon-512.png"
].map((path) => new URL(path, BASE_URL).href);

self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then((cache) => cache.addAll(ASSETS))
      .then(() => self.skipWaiting())
  );
});

self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys()
      .then((keys) => Promise.all(
        keys
          .filter((key) => key.startsWith(CACHE_PREFIX) && key !== CACHE_NAME)
          .map((key) => caches.delete(key))
      ))
      .then(() => self.clients.claim())
  );
});

self.addEventListener("fetch", (event) => {
  const request = event.request;
  const requestUrl = new URL(request.url);
  if (request.method !== "GET" || requestUrl.origin !== BASE_URL.origin) return;
  if (!requestUrl.href.startsWith(BASE_URL.href)) return;

  if (request.mode === "navigate") {
    event.respondWith(
      fetch(request)
        .then((response) => {
          if (response.ok) {
            const copy = response.clone();
            event.waitUntil(caches.open(CACHE_NAME).then((cache) => cache.put(request, copy)));
          }
          return response;
        })
        .catch(async () => (await caches.match(APP_ENTRY)) || new Response(
          "Приложение временно недоступно. Откройте его после подключения к сети.",
          { status: 503, headers: { "Content-Type": "text/plain; charset=utf-8" } }
        ))
    );
    return;
  }

  event.respondWith((async () => {
    const cache = await caches.open(CACHE_NAME);
    const cached = await cache.match(request);
    const refresh = fetch(request).then((response) => {
      if (response.ok && response.type === "basic") {
        return cache.put(request, response.clone()).then(() => response);
      }
      return response;
    });
    event.waitUntil(refresh.catch(() => {}));
    if (cached) return cached;
    try {
      return await refresh;
    } catch {
      return new Response("Офлайн: ресурс ещё не сохранён.", {
        status: 503,
        headers: { "Content-Type": "text/plain; charset=utf-8" }
      });
    }
  })());
});
