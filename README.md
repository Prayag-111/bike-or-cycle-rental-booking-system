# Bike / Cycle Rental Booking System — Phase 4

**Class:** I MCA A
**Phase:** 4 — Server-Side Integration and Execution
**CLOs Addressed:** CLO1, CLO2, CLO3

This phase joins the Java code (Phases 1 and 2) with the web pages (Phase 3) and runs them on a web server (Apache Tomcat 9).

## What this phase covers

| Requirement | Where it is implemented |
|---|---|
| Process registration and login using Servlets (GET/POST) | `RegisterServlet` (`/register`), `LoginServlet` (`/login`) |
| Validate user credentials on the server side | `UserOperationsImpl.registerUser()` and `authenticateUser()` |
| Session tracking using HttpSession | `LoginServlet` creates the session and stores the `User` in it |
| JSP page for the user dashboard | `webapp/dashboard.jsp` |
| Restrict dashboard access without login | Guard at the top of `dashboard.jsp` redirects to `login.jsp` |
| Logout and session invalidation | `LogoutServlet` calls `session.invalidate()` |
| Deploy and execute on a web server | `build.bat` + Tomcat 9, opened at `http://localhost:8080/BikeRental/` |

## Folder structure

```
BikeRentalSystem_Phase4/
├── build.bat                  (compiles and deploys to Tomcat)
├── src/com/bikerental/
│   ├── model/                 User, Cycle, Booking (Phase 1), Activity
│   ├── exception/             InvalidUserDataException, AuthenticationException, BookingException
│   ├── service/               UserOperations + Impl, BookingOperations + Impl
│   ├── servlet/               AppListener, Register, Login, Logout, Api (JSON for the dashboard)
│   └── util/                  Html, Auth, Json, StateJson (JSON for the live dashboard)
└── webapp/                    (this folder becomes the deployed web app)
    ├── login.jsp, register.jsp, dashboard.jsp
    ├── css/style.css
    ├── js/validation.js, register.js, login.js, dashboard-view.js, dashboard.js
    └── WEB-INF/web.xml
```

## How it works

1. The browser checks the form with JavaScript, then POSTs it to a servlet.
2. The servlet validates the data again on the server (browser checks can be bypassed) using `UserOperations`.
3. On a successful login, `LoginServlet` starts an `HttpSession` and stores the logged-in `User` in it.
4. `dashboard.jsp` shows only if the session holds a user. Otherwise it redirects to the login page.
5. `LogoutServlet` ends the session.

## Pages

| Page | What it shows |
|---|---|
| `login.jsp`, `register.jsp` | Sign in and create an account |
| `dashboard.jsp` (Cycles) | Banner, filter chips and a picture card for every cycle, with live booking |
| `bookings.jsp` (My bookings) | Your active bookings with a countdown, and the live activity feed |
| `profile.jsp` (Profile) | Your personal information and session details |

The pages share their header, navigation, scripts and cycle pictures through the files in `webapp/WEB-INF/jspf/`.
The cycle pictures are drawn as SVG in `sprites.jspf`, so there are no image files to manage.
To use real photos instead, put them in a `webapp/images` folder and show them with an `<img>` tag in `cyclesHtml()` in `dashboard-view.js`.

## Live dashboard

After logging in, the dashboard is a live booking page. Nothing reloads: the page talks to the server in the background with `fetch()`.

- **Live updates:** the page asks the server for the latest state every 4 seconds. If another user books a cycle, it shows as Booked on your screen by itself, and the row flashes.
- **Instant booking:** choose the hours (the cost updates as you change them) and click Book. The cycle, the stats and your bookings table update at once, and a message pops up in the corner.
- **Countdown:** each booking shows the time left, ticking every second, with a progress bar. When the time is up the server frees the cycle automatically.
- **Two-step cancel:** click Cancel, then click again within 3 seconds to confirm.
- **Filter:** All, Mountain, Hybrid, Road or Electric.
- **Live activity feed:** the latest bookings, cancellations and finished rides of all users. Other users appear as "Another rider", so usernames are not shown.
- **Status light:** the green dot beside Log out shows the page is connected. It turns amber and says "Reconnecting..." if the server cannot be reached, and goes back to green by itself.

How it works:
- `ApiServlet` serves `GET /api/state`, `POST /api/book` and `POST /api/cancel`, all as JSON.
- `dashboard-view.js` turns that JSON into HTML (all text is escaped), and `dashboard.js` handles the clicks, the refresh and the countdown.
- The server checks every booking (valid hours, cycle exists and is free, you own the booking), so the page cannot cheat.
- Booking changes are synchronized, so two users clicking the same cycle at the same moment cannot both get it.

To see the live updates, log in as one user in a normal window and as another user in a private/incognito window. Book a cycle in one window and watch the other.

Bookings are kept in memory, so they are cleared when Tomcat restarts.

## Setup (Windows, VS Code)

You need a JDK (you already have one for VS Code) and Tomcat 9.

1. **Check Java:** open a terminal and run `java -version` and `javac -version`.
2. **Set JAVA_HOME** (Tomcat needs it): search Windows for "Edit the system environment variables" → Environment Variables → New (user variable) → name `JAVA_HOME`, value = your JDK folder, for example `C:\Program Files\Java\jdk-21`.
3. **Download Tomcat 9:** go to tomcat.apache.org → Download → **Tomcat 9** → Binary Distributions → Core → **64-bit Windows zip**. Extract it to a short path such as `C:\apache-tomcat-9.0.121`.
4. **Open this folder in VS Code:** File → Open Folder → `BikeRentalSystem_Phase4`.
5. **Edit `build.bat`:** change the `TOMCAT_HOME` line to your Tomcat folder.
6. **Run `build.bat`:** in the VS Code terminal type `.\build.bat` (or double-click it in File Explorer). It compiles the Java code and copies the app to Tomcat's `webapps\BikeRental` folder.
7. **Start Tomcat:** run `bin\startup.bat` inside the Tomcat folder. A console window opens. Leave it open.
8. **Open the site:** go to `http://localhost:8080/BikeRental/` in your browser.
9. **Stop Tomcat** when finished: run `bin\shutdown.bat`.

## What to try

1. Click **Create an account**, fill the form and submit. You return to the login page with a success message.
2. Log in with the same username and password. The dashboard opens.
3. Open `http://localhost:8080/BikeRental/dashboard.jsp` in a private/incognito window. You are sent back to the login page.
4. Click **Log out**, then press the browser's Back button. The dashboard does not show again.
5. Log in with a wrong password. An error message appears.

## Troubleshooting

| Problem | Fix |
|---|---|
| Tomcat window closes immediately | `JAVA_HOME` is not set or is wrong (step 2). Open a new terminal after setting it. |
| `build.bat` says servlet-api.jar not found | `TOMCAT_HOME` in `build.bat` is wrong. |
| `error: release version 17 not supported` | Your JDK is older than 17. In `build.bat`, change `--release 17` to your JDK number (for example `11`), or delete `--release 17 `. |
| Browser shows 404 | The app is not deployed. Check that `webapps\BikeRental` exists in the Tomcat folder, and look at the Tomcat window for errors. |
| Port 8080 already in use | Close the other program, or change the port in Tomcat's `conf\server.xml`. |
| You use Tomcat 10 or 11 | This project targets Tomcat 9. Tomcat 10+ needs `javax.servlet` replaced by `jakarta.servlet` in the servlet files and a different servlet-api jar. Use Tomcat 9 to avoid this. |

## Notes

- Registered users are kept in memory, so they are cleared each time Tomcat restarts. Register again after a restart.
- Passwords are stored as plain text in memory to keep the project simple. A real application would store a salted hash and use a database.
