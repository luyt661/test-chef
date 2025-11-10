// src/api/client.js
// Lớp "API client" tập trung mọi lời gọi HTTP.
// Ưu điểm:
//  - FE chỉ dùng apiGet/apiPost/... -> sau này cần thêm token, interceptors, retry...
//    ta chỉnh đúng 1 chỗ.
//  - URL backend đọc từ .env (không hardcode).

const BASE_URL = import.meta.env.VITE_API_BASE_URL

// GET: đọc dữ liệu danh sách/chi tiết
export async function apiGet(path) {
  // Nếu dùng proxy Vite (vite.config.js), bạn có thể gọi trực tiếp '/api/...'
  // bằng fetch(path). Ở đây ghép BASE_URL để không phụ thuộc proxy.
  const url = path.startsWith('http') ? path : `${BASE_URL}${path}`
  const res = await fetch(url, {
    credentials: 'include', // cho case BE dùng cookie/session
  })
  if (!res.ok) {
    throw new Error(`GET ${path} failed with status ${res.status}`)
  }
  return res.json()
}

// POST: tạo mới / login ...
export async function apiPost(path, body) {
  const url = path.startsWith('http') ? path : `${BASE_URL}${path}`
  const res = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(body),
  })
  if (!res.ok) {
    throw new Error(`POST ${path} failed with status ${res.status}`)
  }
  return res.json()
}

// PUT: cập nhật
export async function apiPut(path, body) {
  const url = path.startsWith('http') ? path : `${BASE_URL}${path}`
  const res = await fetch(url, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(body),
  })
  if (!res.ok) throw new Error(`PUT ${path} failed with status ${res.status}`)
  return res.json()
}

// DELETE: xoá
export async function apiDelete(path) {
  const url = path.startsWith('http') ? path : `${BASE_URL}${path}`
  const res = await fetch(url, {
    method: 'DELETE',
    credentials: 'include',
  })
  if (!res.ok) throw new Error(`DELETE ${path} failed with status ${res.status}`)
  try { return await res.json() } catch { return true } // 204 No Content
}
