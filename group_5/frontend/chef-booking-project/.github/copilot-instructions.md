## Repo context for AI coding agents

This repository is a small React + Vite single-page app (no backend included). The guidance below highlights concrete, discoverable patterns and workflows so an AI coding agent can be productive immediately.

Key facts
- Project root: contains `package.json`, `vite.config.js`, ESLint config (`eslint.config.js`) and `README.md`.
- Frontend source: `src/` with entry `src/main.jsx`, layout `src/App.jsx`, and pages under `src/pages/` (e.g. `HomePage.jsx`).
- Routing: React Router v7 is used via `createBrowserRouter` in `src/main.jsx`.
- Build/dev: Uses Vite. Scripts in `package.json`: `npm run dev` (vite), `npm run build` (vite build), `npm run preview` (vite preview), `npm run lint` (eslint).

Architecture & important patterns
- Entrypoint: `src/main.jsx` creates a `createBrowserRouter` routes tree. The top-level `element` is `App` which acts as the layout.
- Layout pattern: `src/App.jsx` uses `<Outlet />` to render page children. Add shared header/footer in `App.jsx` so it appears on every page.
- Pages: Place route-level components in `src/pages/`. Example: `src/pages/HomePage.jsx` uses `Link` from `react-router-dom` (preferred over `<a>` for client-side navigation).
- Component conventions: There are no special frameworks or global state libraries. Keep components small and colocate route components under `src/pages/`.
- Static assets: Use `src/assets/` and `public/` for static files; import images from `src/assets` in components when needed.

Code examples to follow when editing
- To add a new page and route:
  - Create `src/pages/YourPage.jsx` that exports a default React component.
  - Register it in `src/main.jsx` inside the `children` array of the router with a `path` and `element: <YourPage />`.
- To add layout UI visible on all pages, edit `src/App.jsx` and place components around `<Outlet />`.
- Use `Link to="/path"` from `react-router-dom` for navigation (see `HomePage.jsx`).

Developer workflows (how to run & lint)
- Start dev server with: `npm run dev` (uses Vite with HMR).
- Create production build with: `npm run build` and preview it with `npm run preview`.
- Lint the project with: `npm run lint` (ESLint configured in repo root).

Dependencies & versions (important)
- React 19.x and ReactDOM 19.x — code uses `ReactDOM.createRoot(...)` in `src/main.jsx`.
- react-router-dom 7.x — the project uses the newer router API (`createBrowserRouter`, `RouterProvider`, route `children`).

Integration points & missing pieces
- No backend or API client found in this repository. If adding API integration, follow the existing pattern of small components and place client code in `src/lib/` or `src/services/` and keep it decoupled from components.

Quick guardrails for the agent (project-specific)
- Do not change routing pattern to older `BrowserRouter` unless also updating route creation and all examples.
- Keep pages under `src/pages/` and layout logic in `src/App.jsx` to preserve current structure.
- Prefer minimal edits: new pages + route registrations are low-risk; global changes (build config, major dependency bumps) require a brief explanation and tests.

Where to look for examples
- `src/main.jsx` (routing & entry)
- `src/App.jsx` (layout + Outlet pattern)
- `src/pages/HomePage.jsx` (typical page component using `Link` and semantic sections)
- `package.json` (scripts & dependencies)

If something is unclear or you need me to expand examples (tests, API client, or component patterns), tell me what to add or modify.
