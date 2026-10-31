# âœˆï¸ TripNest: Travel Planning & Trip Management Platform

A full-stack travel platform built with **React.js + Vite** and **Spring Boot 3 + MySQL**.

---

## ðŸš€ Quick Start (1-Click Run)

Simply double-click the launcher script in this folder:
- **`start-tripnest.bat`** âž” Starts MySQL, starts the Backend (port 8081), starts the Frontend (port 5173), and automatically opens `http://localhost:5173` in your browser!
- **`stop-tripnest.bat`** âž” Gracefully stops both servers when you are finished.

---

## ðŸ› ï¸ Manual Run Instructions

If you prefer running via separate Command Prompt / PowerShell terminals:

### 1. Ensure MySQL is Running
Make sure your MySQL service (`MySQL80`) is running. You can check or start it via Command Prompt (as Admin):
```cmd
net start MySQL80
```
Database configured: `tripnest` (Username: `root`, Password: `Saki@123`).

### 2. Start the Spring Boot Backend (Port 8081)
Open a terminal in `tripnest-backend`:
```cmd
cd "c:\Users\Sakanasri S\OneDrive\Desktop\tripnest final\tripnest-backend"
.\mvnw.cmd spring-boot:run
```
*Wait until you see `Started TripnestApplication in ... seconds`.*

### 3. Start the React Frontend (Port 5173)
Open a second terminal in `tripnest-frontend`:
```cmd
cd "c:\Users\Sakanasri S\OneDrive\Desktop\tripnest final\tripnest-frontend"
npm.cmd run dev
```
*Vite will start at `http://localhost:5173`.*

### 4. Open in Browser
Visit **[http://localhost:5173](http://localhost:5173)** in Chrome, Edge, or Firefox.

---

## ðŸ”‘ Login Accounts

| Role | Email | Password | Access Highlights |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin@tripnest.com` | `Admin@123` | Full access to ðŸ›¡ï¸ **Admin Center**, platform metrics, user role management, destination catalog controls |
| **Traveler (Google)** | 1-Click Button | *None required* | Instant one-click Google OAuth demo traveler |
| **Traveler (User)** | `sakanasri07@gmail.com` | *Your password* | Full access to trips, day-wise itineraries, expense splitting, and chat |

---

## ðŸ“‹ Features & Milestone Checklist

- **Milestone 1**: JWT & OAuth2 login, Password recovery, Role-Based Access (`ROLE_TRAVELER`, `ROLE_GROUP_ADMIN`, `ROLE_ADMIN`), User Profile & Travel Preferences.
- **Milestone 2**: Trip creation, Day-wise itinerary planning, Interactive Leaflet GPS route map, Activity scheduling (Sightseeing, Transport, Hotel, Dining, Adventure, Shopping), Live Weather forecast widget & Travel guides.
- **Milestone 3**: Budget utilization tracking, Over-budget warning alerts, Group collaboration (Invite companions by email), In-app real-time group chat, Group expense splitting & "Mark as Settled" toggle, Centralized travel documents vault (tickets, hotel vouchers, passport/ID, photos).
- **Milestone 4**: Traveler Analytics Dashboard with Chart.js charts (Budget vs Actual spend, Category doughnut distribution), System Admin Dashboard with platform KPIs, user role assignment, and destination catalog management.

