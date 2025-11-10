// src/pages/BrowseChefsPage.jsx
// Gọi thẳng BE qua endpoint /api/chefs.
// Dev: proxy Vite sẽ chuyển tiếp sang http://localhost:8080 -> tránh CORS.
// Prod: bỏ proxy, deploy FE/BE thật; khi đó giữ nguyên đường dẫn /api/...,
//       server/nginx sẽ route sang BE.
//
// YÊU CẦU BE: cung cấp GET /api/chefs trả JSON dạng mảng:
//   [{ "id": 1, "name": "Chef A", "specialty": "..." }, ...]
//
// Nếu BE trả schema khác, bạn sửa phần render cho khớp.

import { useEffect, useState } from 'react'
import { useLocation } from 'react-router-dom'

export default function BrowseChefsPage() {
  const { search } = useLocation()           // VD: ?event=BBQ&loc=DaNang&date=2025-11-10
  const [chefs, setChefs] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const url = `/api/chefs${search || ''}`  // proxy Vite sẽ chuyển sang BE:8080
    fetch(url, { credentials: 'include' })
      .then(r => { if (!r.ok) throw new Error(`HTTP ${r.status}`); return r.json() })
      .then(data => setChefs(Array.isArray(data) ? data : []))
      .catch(e => setError(e.message))
      .finally(() => setLoading(false))
  }, [search])

  if (loading) return <div className="p-4">Đang tải...</div>
  if (error) return <div className="p-4 text-danger">Lỗi: {error}</div>

  return (
    <div className="container py-4">
      <h2>Browse Chefs</h2>
      {chefs.length === 0 ? <div>Không có dữ liệu.</div> :
        <ul style={{ lineHeight: 1.8 }}>
          {chefs.map(c => <li key={c.id}><strong>{c.name}</strong> — {c.specialty}</li>)}
        </ul>}
    </div>
  )
}
