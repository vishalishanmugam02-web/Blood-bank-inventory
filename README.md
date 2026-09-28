# BloodBank — Blood Bank Inventory and Donor Eligibility Tracker

Spring Boot + JPA + MySQL project based on the supplied Question 58 use case.

## Included
- Separate Admin Login and User Login pages
- Session-based role checks
- Donor CRUD and donor eligibility check
- Donation logging
- Automatic creation of individual blood-unit inventory records
- 90-day minimum donor gap enforced in the service layer
- Blood units expiring within 7 days are excluded from issue requests
- Expired/expiring status refresh
- Stock levels by blood group
- Issue units against a request
- Validation and global error handling
- Simple browser UI and REST APIs

## Demo logins
- Admin: `admin` / `admin123`
- User: `user` / `user123`

These are demo credentials only. Change them for a real deployment.

## Run in VS Code
1. Install Java 17+ and MySQL.
2. Create the database by running `src/main/resources/db/create_database.sql` or create a database named `bloodbank` manually.
3. Open `src/main/resources/application.properties` and change the MySQL username/password if needed.
4. Open this folder in VS Code.
5. Run:
   - Windows: `mvnw.cmd spring-boot:run`
   - macOS/Linux: `./mvnw spring-boot:run`
6. Open `http://localhost:8080/`

## Main pages
- `/` — landing/login selection
- `/admin-login.html` — Admin Login
- `/user-login.html` — User Login
- `/admin-dashboard.html` — Admin dashboard
- `/user-dashboard.html` — User dashboard

## Main APIs
- POST `/api/auth/login`
- POST `/api/auth/logout`
- GET `/api/auth/me`
- POST/GET/PUT/DELETE `/api/donors`
- GET `/api/donors/{id}/eligibility`
- POST `/api/inventory/donations`
- GET `/api/inventory/units`
- GET `/api/inventory/stock`
- POST `/api/inventory/refresh`
- POST `/api/issues`
- GET `/api/issues` (admin)

## Business rules
- Donor must have completed 90 days since last donation.
- Collection date cannot be in the future.
- Expiry date must be after collection date.
- Donation blood group must match donor blood group.
- Units that are expired or within 7 days of expiry cannot be issued.
- A request is rejected when eligible inventory is insufficient.
