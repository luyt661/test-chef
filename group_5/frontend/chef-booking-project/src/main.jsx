// src/main.jsx
// Khai báo routing cơ bản để điều hướng giữa các trang,
// KHÔNG tải lại trang (SPA).
//
// /               -> HomePage (có nút Browse, Login)
// /browse-chefs   -> BrowseChefsPage (gọi API GET /api/chefs)
// /login          -> LoginPage (gọi API POST /api/login)

import React from 'react'
import ReactDOM from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'

import App from './App'
import HomePage from './pages/HomePage'
import BrowseChefsPage from './pages/BrowseChefsPage'
import LoginPage from './pages/LoginPage'

import 'bootstrap/dist/css/bootstrap.min.css'
import './index.css'

// Nếu app chạy dưới nhánh con (hiếm khi trên Vite dev), dùng:
// const router = createBrowserRouter(routes, { basename: '/chef-booking-project' });

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,                  
    children: [
      { index: true, element: <HomePage /> },
      { path: 'browse-chefs', element: <BrowseChefsPage /> },
      { path: 'login', element: <LoginPage /> },
     
      { path: 'reviews', element: <div className="p-4">Reviews</div> },
      { path: 'apply-as-chef', element: <div className="p-4">Apply as Chef</div> },
      { path: 'profile', element: <div className="p-4">Profile</div> },

      { path: '*', element: <div className="p-4">Not found</div> } // fallback
    ],
  },
])

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
)
