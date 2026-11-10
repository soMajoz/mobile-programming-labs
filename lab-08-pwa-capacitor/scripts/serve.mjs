/**
 * Простая локальная раздача webDir для проверки PWA на localhost.
 * Использует только встроенные модули Node.js, без стороннего сервера.
 */

import { createReadStream, statSync } from "node:fs";
import { createServer } from "node:http";
import path from "node:path";
import { fileURLToPath } from "node:url";

const scriptDirectory = path.dirname(fileURLToPath(import.meta.url));
const webRoot = path.resolve(scriptDirectory, "../www");
const port = Number(process.env.PORT || 4173);
const mimeTypes = new Map([
  [".html", "text/html; charset=utf-8"],
  [".css", "text/css; charset=utf-8"],
  [".js", "text/javascript; charset=utf-8"],
  [".json", "application/json; charset=utf-8"],
  [".png", "image/png"],
  [".svg", "image/svg+xml"]
]);

createServer((request, response) => {
  if (request.method !== "GET" && request.method !== "HEAD") {
    response.writeHead(405, { Allow: "GET, HEAD" });
    response.end();
    return;
  }

  let relativePath;
  try {
    relativePath = decodeURIComponent(new URL(request.url || "/", "http://localhost").pathname);
  } catch {
    response.writeHead(400);
    response.end("Некорректный адрес");
    return;
  }

  const requestedFile = relativePath === "/" ? "index.html" : relativePath.replace(/^\/+/, "");
  const filePath = path.resolve(webRoot, requestedFile);
  if (filePath !== webRoot && !filePath.startsWith(webRoot + path.sep)) {
    response.writeHead(403);
    response.end("Доступ запрещён");
    return;
  }

  try {
    if (!statSync(filePath).isFile()) throw new Error("Not a file");
    response.writeHead(200, {
      "Content-Type": mimeTypes.get(path.extname(filePath).toLowerCase()) || "application/octet-stream",
      "Cache-Control": "no-store",
      "X-Content-Type-Options": "nosniff"
    });
    if (request.method === "HEAD") response.end();
    else createReadStream(filePath).pipe(response);
  } catch {
    response.writeHead(404, { "Content-Type": "text/plain; charset=utf-8" });
    response.end("Файл не найден");
  }
}).listen(port, "0.0.0.0", () => {
  console.log("PWA доступно по адресу http://localhost:" + port);
});
