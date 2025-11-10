// src/pages/LoginPage.jsx
// Form POST tới /api/login để BE xử lý xác thực ngay.
//
// YÊU CẦU BE: nhận body JSON { username, password } và trả về JSON (ví dụ):
//   { "status":"OK", "fullName":"Nguyen Van A" }
// FE hiển thị thông báo từ response. Nếu BE trả schema khác, sửa phần setMsg.
//
// Nếu BE dùng session/cookie: Spring Security nên set cookie; proxy đã bật
// changeOrigin nên cookie sẽ về FE bình thường trong DEV.

import { useState } from 'react'
export default function LoginPage() {
  const [username, setU] = useState(''), [password, setP] = useState(''), [msg, setMsg] = useState('')
  const submit = async e => {
    e.preventDefault(); setMsg('Đang đăng nhập...')
    try {
      const r = await fetch('/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ username, password })
      })
      if (!r.ok) throw new Error(`HTTP ${r.status}`)
      const data = await r.json()
      setMsg(data.fullName ? `Xin chào ${data.fullName}` : 'Đăng nhập thành công')
    } catch (err) { setMsg(`Đăng nhập thất bại: ${err.message}`) }
  }
  return (
    <div className="container py-4" style={{ maxWidth: 420 }}>
      <h2>Login</h2>
      <form onSubmit={submit}>
        <input className="form-control mb-2" placeholder="Username" value={username} onChange={e=>setU(e.target.value)} required />
        <input type="password" className="form-control mb-2" placeholder="Password" value={password} onChange={e=>setP(e.target.value)} required />
        <button className="btn btn-primary">Login</button>
      </form>
      {msg && <p className="mt-2">{msg}</p>}
    </div>
  )
}
