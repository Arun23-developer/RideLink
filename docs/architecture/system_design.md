# RideLink System Architecture and Design

## Authentication Service (Port 8081)
**Responsibilities:**
- Passenger Registration
- Driver Registration
- User Login
- JWT Token Generation
- JWT Token Validation
- User Profile Management
- Role Management
- Account Status Management

**Flow:**
1. **User Register**: User Register -> AuthController -> AuthService -> UserRepository -> Database
2. **User Login**: User Login -> AuthController -> Validate Credentials -> Generate JWT Token -> Return Token

**APIs:**
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/users`
- `GET /api/users/{id}`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

**Roles:** `ROLE_PASSENGER`, `ROLE_DRIVER`, `ROLE_ADMIN`
**Database Tables:** `users`, `roles`

---

## Driver Service (Port 8082)
**Responsibilities:**
- Driver Management
- Vehicle Management
- Driver Availability Management
- Driver Location Tracking
- Service Area Management
- Available Driver Search

**Flow:**
- **Driver Login**: Create Driver Profile -> Add Vehicle -> Set Availability -> Update Current Location -> Ready For Ride Assignment
- **Ride Service Request**: Ride Service -> Request Available Drivers -> Driver Service -> Return Available Drivers

**APIs:**
- `POST /api/drivers`
- `GET /api/drivers`
- `GET /api/drivers/{id}`
- `PUT /api/drivers/{id}`
- `DELETE /api/drivers/{id}`
- `POST /api/vehicles`
- `GET /api/vehicles/{id}`
- `PUT /api/vehicles/{id}`
- `DELETE /api/vehicles/{id}`
- `PUT /api/drivers/status`
- `PUT /api/drivers/location`
- `GET /api/drivers/available`

**Driver Status:** `ONLINE`, `OFFLINE`, `BUSY`
**Database Tables:** `drivers`, `vehicles`

---

## Ride Service (Port 8083)
**Responsibilities:**
- Ride Request Management
- Driver Assignment
- Ride Lifecycle Management
- Ride Tracking
- Ride Search
- Ride History

**Flow:**
- **Passenger Login**: Create Ride Request -> Ride Service -> Call Driver Service -> Get Available Driver -> Assign Driver -> Driver Accept Ride -> Start Ride -> Complete Ride -> Call Payment Service

**Ride Lifecycle:**
`REQUESTED` -> `ASSIGNED` -> `ACCEPTED` -> `IN_PROGRESS` -> `COMPLETED`
(Alternative: `REQUESTED` -> `CANCELLED`)

**APIs:**
- `POST /api/rides`
- `GET /api/rides`
- `GET /api/rides/{id}`
- `PUT /api/rides/assign`
- `PUT /api/rides/accept`
- `PUT /api/rides/start`
- `PUT /api/rides/complete`
- `PUT /api/rides/cancel`
- `GET /api/rides/history`
- `GET /api/rides/passenger/{passengerId}`
- `GET /api/rides/driver/{driverId}`

**Ride Status Enum:** `REQUESTED`, `ASSIGNED`, `ACCEPTED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`
**Database Tables:** `rides`

---

## Payment Service (Port 8084)
**Responsibilities:**
- Fare Estimation
- Final Fare Calculation
- Payment Processing
- Payment Status Tracking
- Receipt Generation
- Receipt Retrieval

**Flow:**
- **Ride Completed**: Ride Service -> Payment Service -> Calculate Final Fare -> Create Payment -> Payment Success -> Generate Receipt -> Return Receipt

**Fare Formula:** `Base Fare + (Distance × Rate Per KM)`
*Example:* Base Fare = 100, Distance = 5 KM, Rate = 50 Per KM -> Fare = 350 LKR

**APIs:**
- `POST /api/fare/estimate`
- `POST /api/fare/calculate`
- `POST /api/payments`
- `GET /api/payments`
- `GET /api/payments/{id}`
- `GET /api/receipts`
- `GET /api/receipts/{rideId}`

**Payment Status:** `PENDING`, `SUCCESS`, `FAILED`
**Database Tables:** `fares`, `payments`, `receipts`

---

## Inter-Service Communication
1. **Ride Service -> Authentication Service**: Validate JWT, Validate Passenger, Validate User Role
2. **Driver Service -> Authentication Service**: Validate Driver, Validate Role, Validate Token
3. **Ride Service -> Driver Service**: Find Available Drivers, Assign Driver, Update Driver Status, Update Driver Availability
4. **Ride Service -> Payment Service**: Fare Estimation, Final Fare Calculation, Create Payment, Generate Receipt

---

## End-to-End Project Flow
Passenger Registration -> Passenger Login -> JWT Token Generated -> Create Ride Request -> Ride Service -> Find Available Driver -> Driver Service -> Driver Assigned -> Driver Accepts Ride -> Ride Status: ACCEPTED -> Start Ride -> Ride Status: IN_PROGRESS -> Complete Ride -> Ride Status: COMPLETED -> Payment Service -> Calculate Final Fare -> Process Payment -> Generate Receipt -> Ride Completed Successfully

---

## Negative Scenarios
1. **No Available Driver**: Passenger Creates Ride -> No Driver Found -> Error: `{"message": "No drivers available"}`
2. **Invalid JWT Token**: User Calls Protected API -> JWT Validation Failed -> Error: `{"message": "Unauthorized access"}`
3. **Invalid Ride Status Transition**: COMPLETED -> START RIDE -> Error: `{"message": "Invalid ride status transition"}`
4. **Payment Failure**: Payment Processing -> Payment Failed -> Error: `{"message": "Payment unsuccessful"}`

---

## Database Structure
- **Auth DB**: `users`, `roles`
- **Driver DB**: `drivers`, `vehicles`
- **Ride DB**: `rides`
- **Payment DB**: `fares`, `payments`, `receipts`

---

## Final System Flow
Authentication Service (8081) -> Ride Service (8083) -> Driver Service (8082) -> Payment Service (8084)
(Authentication -> Ride Booking -> Driver Assignment -> Ride Completion -> Payment -> Receipt Generation)
