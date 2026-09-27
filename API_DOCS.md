# Campus Services API Documentation

Base URL (via API Gateway): `http://localhost:8080`

## 1. Auth & Users (`/api/auth`, `/api/users`) - User Service

### `POST /api/auth/register`
*   **Description**: Registers a new user.
*   **Body**: `{ "name": "...", "email": "...", "password": "...", "role": "STUDENT|ADMIN" }`
*   **Auth**: Public

### `POST /api/auth/login`
*   **Description**: Authenticates a user and returns a JWT.
*   **Body**: `{ "email": "...", "password": "..." }`
*   **Auth**: Public
*   **Response**: `{ "token": "ey...", "user": { ... } }`

### `GET /api/users/me`
*   **Description**: Returns the currently authenticated user's details.
*   **Auth**: Any authenticated user.

## 2. Facilities (`/api/facilities`) - Facility Service

### `GET /api/facilities`
*   **Description**: Returns a paginated list of facilities. Supports search.
*   **Query Params**: `search` (optional), `page` (default: 0), `size` (default: 10)
*   **Auth**: Any authenticated user.

### `GET /api/facilities/{id}`
*   **Description**: Returns details of a specific facility.
*   **Auth**: Any authenticated user.

### `POST /api/facilities`
*   **Description**: Creates a new facility.
*   **Body**: `{ "name": "...", "description": "...", "location": "...", "capacity": 50, "available": true }`
*   **Auth**: `ADMIN` only.

### `PUT /api/facilities/{id}` / `DELETE /api/facilities/{id}`
*   **Description**: Updates or deletes a facility.
*   **Auth**: `ADMIN` only.

## 3. Bookings (`/api/bookings`) - Booking Service

### `POST /api/bookings`
*   **Description**: Books a facility.
*   **Body**: `{ "facilityId": 1, "bookingDate": "YYYY-MM-DD", "startTime": "HH:MM:SS", "endTime": "HH:MM:SS" }`
*   **Auth**: Any authenticated user.

### `GET /api/bookings/my`
*   **Description**: Returns all bookings made by the current user.
*   **Auth**: Any authenticated user.

### `PUT /api/bookings/{id}/cancel`
*   **Description**: Cancels a pending or approved booking.
*   **Auth**: The booking owner or an `ADMIN`.

### `PUT /api/bookings/{id}/approve` | `/reject`
*   **Description**: Approves or rejects a booking.
*   **Auth**: `ADMIN` only.

## 4. Notifications (`/api/notifications`) - Notification Service

### `GET /api/notifications/my`
*   **Description**: Retrieves unread notifications for the current user.
*   **Auth**: Any authenticated user.

### `PUT /api/notifications/{id}/read`
*   **Description**: Marks a specific notification as read.
*   **Auth**: The notification owner.
