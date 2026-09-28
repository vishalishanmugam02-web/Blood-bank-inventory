# Detailed Explanation — BloodBank

## Real-World Scenario
A local blood bank tracks unit inventory by blood group on paper ledgers, making it hard to know real-time stock or which donors are currently eligible to donate again.

## What You Need to Build
Build a backend and simple web interface that tracks blood unit inventory by group and expiry, checks donor eligibility before accepting a donation, and supports controlled issue requests.

## Core Features to Implement
1. Log a donation, adding units to inventory with collection and expiry date.
2. Check donor eligibility based on last donation date with a minimum 90-day gap.
3. Issue units against a request, reducing inventory by blood group.
4. Auto-flag units nearing expiry within 7 days.
5. View current stock levels per blood group.
6. Admin Login and User Login with role-based access.

## Admin Features
- Login as administrator.
- Add, view, edit and delete donors.
- Check donor eligibility.
- Log donations.
- View individual blood units and statuses.
- View stock by blood group.
- View issue records.
- Refresh inventory status.

## User Features
- Login as user.
- View current stock by blood group.
- Submit an issue/request for required blood units.
- The backend validates availability and excludes expired or near-expiry units.

## Suggested Database Design
Entities: AppUser, Donor, Donation, BloodUnit, IssueRecord.

- AppUser stores username, password and role.
- Donor stores donor identity, contact, blood group and last donation date.
- Donation belongs to one Donor and records collection, expiry, blood group and unit count.
- BloodUnit belongs to one Donation and represents one inventory unit.
- IssueRecord stores recipient, blood group, units issued, date and requesting username.

## Business Rules You Must Enforce
- A donation cannot be accepted if the donor has not completed the 90-day minimum gap.
- A donor must be active.
- Donation blood group must match donor blood group.
- Collection date cannot be in the future.
- Expiry date must be after collection date.
- Units expired or expiring within 7 days are not eligible for issue.
- A request is rejected if eligible inventory is insufficient.
- Business rules are enforced in service classes before database changes.

## How to Approach the Build
1. Spring Boot project with Spring Web, Spring Data JPA, Validation and MySQL driver.
2. Create entity classes and relationships.
3. Create JpaRepository interfaces.
4. Create service classes containing business rules.
5. Build REST controllers.
6. Add validation and global exception handling.
7. Add Admin/User login and session role checks.
8. Test APIs and browser pages.
