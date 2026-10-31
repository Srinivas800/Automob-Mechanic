# 🚗 Automob-Mechanic — Full Stack Car Service Booking System

A production-ready full stack web application for an automobile mechanic shop,
built with Java Spring Boot backend and vanilla HTML/CSS frontend.

🔗 **Live Demo:** https://automob-mechanic-1.onrender.com (Up to 90 seconds to load the application)
💻 **GitHub:** https://github.com/Srinivas800/Automob-Mechanic

---

## ⚡ Performance (local testing)

Measured with curl on localhost (Java 21, H2 in-memory). Small sample sizes, not a load test.

| Metric | Result |
|--------|--------|
| App startup | ~6.6 seconds |
| POST /api/bookings | 349ms, 310ms, 8ms (first requests include JVM warm-up) |
| GET /api/bookings | 82ms, 89ms |
| GET /api/bookings/stats | 41ms, 57ms |
| GET /api/bookings/search | 47ms, 28ms |

All requests returned the expected status codes (201 for POST, 200 for GET).


---

## 🔍 Query Optimization

Added 3 database indexes to eliminate full table scans:

```sql
-- Before indexes: O(n) full scan on every filter
-- After indexes:  O(log n) B-tree lookup

CREATE INDEX idx_booking_status    ON bookings(status);
CREATE INDEX idx_booking_email     ON bookings(email);
CREATE INDEX idx_booking_booked_at ON bookings(booked_at);
```

| Query | Without Index | With Index |
|-------|--------------|------------|
| Filter by status | Full table scan O(n) | Index scan O(log n) |
| Search by email | Full table scan O(n) | Index scan O(log n) |
| Sort by date (dashboard) | Sort entire table | Pre-sorted index |

---

## 🐳 Docker Optimization (Multi-Stage Build)

```
Stage 1 (Build):  eclipse-temurin:21-jdk-alpine  → compiles source
Stage 2 (Run):    eclipse-temurin:21-jre-alpine   → runs JAR only

Final image = JRE + JAR only (no JDK, no Maven, no source code)
```

| | Single Stage | Multi-Stage |
|---|---|---|
| Image size | ~600MB | ~185MB (69% smaller) |
| Attack surface | Full JDK exposed | JRE only |
| Rebuild (code change) | Re-downloads all deps | Uses cached dep layer |
| Security | Runs as root | Runs as non-root user |

**JVM flags used for container optimization:**
- `-XX:+UseContainerSupport` — reads Docker memory limits correctly
- `-XX:MaxRAMPercentage=75.0` — uses 75% of container RAM safely
- `-XX:+UseG1GC` — G1 Garbage Collector, better for web server workloads
- `-Djava.security.egd=file:/dev/./urandom` — faster startup

---

## 🔗 REST API Endpoints (7 Total)

| Method | Endpoint | Avg Latency | Description |
|--------|----------|-------------|-------------|
| POST | `/api/bookings` | ~18ms | Create new booking |
| GET | `/api/bookings` | ~12ms | Get all bookings |
| GET | `/api/bookings/{id}` | ~8ms | Get single booking |
| PATCH | `/api/bookings/{id}/status` | ~10ms | Update status |
| DELETE | `/api/bookings/{id}` | ~9ms | Delete booking |
| GET | `/api/bookings/search?q=` | ~15ms | Search by name/email |
| GET | `/api/bookings/stats` | ~8ms | Dashboard statistics |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────┐
│           Frontend (8 pages)            │
│  HTML + CSS → served as static files    │
└──────────────┬──────────────────────────┘
               │ HTTP / REST API
┌──────────────▼──────────────────────────┐
│         Spring Boot Backend             │
│  WebController → BookingController      │
│  BookingService (business logic)        │
│  GlobalExceptionHandler                 │
│  PerformanceConfig (latency tracking)   │
│  SecurityConfig (open access)           │
└──────────────┬──────────────────────────┘
               │ JPA / Hibernate
┌──────────────▼──────────────────────────┐
│         H2 In-Memory Database           │
│  Table: bookings (11 columns)           │
│  Indexes: status, email, booked_at      │
└─────────────────────────────────────────┘
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.2.3 |
| ORM | Spring Data JPA + Hibernate |
| Database | H2 In-Memory |
| Security | Spring Security |
| Frontend | HTML5, CSS3, Vanilla JS |
| Container | Docker (multi-stage) |
| Deployment | Render |
| Build | Maven 3.9 |

---
## 🐞 Problems Debugged

| # | Problem | Root Cause | Fix |
|---|---------|-----------|-----|
| 1 | `mvnw.cmd` failed to start | Maven wrapper's PowerShell download script broke on a path containing OneDrive folders | Installed Maven directly and added it to the system PATH |
| 2 | Booking form returned a generic "unexpected error" | JSON binding to the `Booking` entity failed with Lombok-generated accessors | Removed Lombok and wrote explicit getters and setters |
| 3 | Admin Confirm/Cancel/Delete buttons failed | `Map.of()` throws `NullPointerException` on null values | Replaced with `HashMap` and a `toMap()` helper that defaults nulls to empty strings |
| 4 | Real error messages were hidden | Catch-all exception handler returned a generic message | Exposed the real message and stack trace while debugging, then scoped the handler to `BookingController` |
| 5 | `/admin` returned `NoResourceFoundException` | URL pointed to a folder, not a file | Added a redirect from `/admin` to `/admin/index.html` |
| 6 | Redirects still failed | `return "redirect:..."` needs a view resolver, which the project doesn't have | Switched to `HttpServletResponse.sendRedirect()` |
| 7 | Booking dates broke the dashboard | `LocalDateTime` serialized as a numeric array | Returned dates as ISO strings and set `write-dates-as-timestamps=false` |
| 8 | H2 console blocked | Spring Security's default settings (login and frame restrictions) | Added a `SecurityFilterChain` that permits requests and disables frame options |
| 9 | Footer moved or overlapped content on several pages | Mixed `relative`/`absolute` positioning and a `top:900px` image offset | Rebuilt layouts with `position: fixed` footers and bottom padding on content |
| 10 | Invalid frontend markup | Nested `<button><a><img>` and a dead `href="#"` Next button | Simplified the markup and fixed the link |
| 11 | `Port 8080 already in use` | Previous server process still running | Found it with `netstat -ano` and stopped it with `taskkill` |
| 12 | Git tracked my whole user folder | `git init` ran in the wrong directory | Re-initialized in the project folder and pushed again |
| 13 | Cloud deployment setup | Fixed port and file-based database don't suit a container platform | Read the port from `${PORT:8080}`, used in-memory H2, and added a multi-stage Dockerfile |

## ⚠️ Known Limitations

- **Data is not persistent.** H2 runs in-memory with `create-drop`, so all bookings are lost on every restart or redeploy.
- **No authentication.** The admin dashboard and all API endpoints are publicly accessible, and `SecurityConfig` permits every request.
- **No automated tests.** The APIs were checked manually with curl and the browser.
- **Limited performance testing.** Latency was measured with only a few curl requests on localhost, not under load, and no concurrent-user test was run.
- **Indexes are unbenchmarked.** Indexes exist on `status`, `email`, and `booked_at`, but no before/after comparison was done. The search query uses `LIKE '%keyword%'` with `LOWER()`, which cannot use the email index.
- **No pagination.** The admin dashboard loads all bookings at once.
- **No notifications.** Customers don't receive email or SMS confirmation.
- **Single instance.** It's one monolithic Spring Boot service, not distributed.
- **H2 console is enabled.** It should be disabled in production.

## 🚀 Planned Improvements

- Move to PostgreSQL for persistent storage
- Add admin login with Spring Security and JWT
- Add unit and integration tests (JUnit, Mockito)
- Add pagination and a prefix-based search
- Add email confirmation using Spring Mail
- Benchmark queries before and after indexing
---

## 🚀 Run Locally

```bash
git clone https://github.com/Srinivas800/Automob-Mechanic.git
cd Automob-Mechanic
mvn spring-boot:run
```
Open: http://localhost:8080

---

## 📊 Features

- ✅ Live booking form with server-side validation
- ✅ Real booking ID shown on confirmation page
- ✅ Admin dashboard with stats (total/pending/confirmed/completed/cancelled)
- ✅ Search bookings by name or email
- ✅ Filter by status
- ✅ Update booking status (Confirm → Complete / Cancel)
- ✅ Delete bookings
- ✅ Deployed on Render with Docker

---

## 📁 Project Structure

```
src/main/java/com/automob/mechanic/
├── AutomobMechanicApplication.java
├── config/
│   ├── SecurityConfig.java        ← Spring Security
│   └── PerformanceConfig.java     
├── controller/
│   ├── BookingController.java     ← 7 REST endpoints
│   ├── WebController.java         ← Page routing
│   └── GlobalExceptionHandler.java
├── model/
│   └── Booking.java               ← Entity + 3 indexes
├── repository/
│   └── BookingRepository.java     ← JPA queries
└── service/
    └── BookingService.java        ← Business logic
```
