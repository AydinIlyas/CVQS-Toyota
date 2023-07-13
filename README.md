# Vehicle Error Tracking System Project

This is a sample microservices project built with Spring Boot for tracking vehicle errors. It consists of multiple
microservices that work together to provide a complete system. Each microservice is responsible for a specific
functionality and communicates with others using RESTful APIs.

## Microservices

The project includes the following microservices:

1. **Discovery-Server**: Provides service registration and discovery capabilities.

2. **API-Gateway**: Acts as a centralized entry point, routing requests and managing authentication.

3. **ErrorLogin-Service**: handles the operations related to vehicles, defects, and locations, providing functionality
   for adding, updating, and deleting records in the system.
    - Required Role: OPERATOR
    - Endpoints for vehicles:
        - Add Vehicle: `POST /vehicle/add`
        - Update Vehicle: `PUT /vehicle/update{id}`
        - Delete Vehicle: `PUT /vehicle/delete`

    - Endpoints for defects:
        - Add Defect:`POST /defect/add/{vehicleId}`
        - Update Defect: `PUT /defect/update/{defectId}`
        - Delete Defect: `PUT /defect/delete`
        - Add Defect Image: `POST defect/add/image/{defectId}`
        - Delete Defect Image: `PUT defect/delete/image`

    - Endpoints for locations:
        - Add Location: `POST /location/add/{defectId}`
        - Update Location: `PUT /location/update/{locationId}`
        - Delete Location: `PUT /location/delete`
        -
4. **ErrorListing-Service**: Lists vehicles defects with paging, filtering and sorting. Processes image with marker.
    - Required Role: LEADER
    - Endpoints:
        - Get Vehicles: `GET /list/vehicles`
        - Get Defects: `GET /list/defects`
        - Get Defect-Image: `GET /list/defect-image/{defectId}`

5. **Terminal-Service**: Lists terminals. Handles operations for adding, activating, disabling, deleting terminals.
    - Required Role: None
    - Endpoints:
        - Get Terminals: `GET /terminal/getAll`
        - Add Terminal: `POST /terminal/add`
        - Activate Terminal: `PUT /terminal/activate`
        - Disable Terminal: `PUT /terminal/disable`
        - Delete Terminal: `PUT /terminal/delete`

6. **User-Management-Service**: The User Service manages user-related operations, including adding, updating, and
   deleting users, as well as adding and removing user roles.
    - Required Role: ADMIN
    - Endpoints:
        - Get Users: `GET /user-management/users`
        - Add User: `POST /user-management/create`
        - Update User: `PUT /user-management/update/{user_id}`
        - Delete User: `PUT /user-management/delete`
        - Add Role: `PUT /user-management/role/add/{user_id}`
        - Remove Role: `PUT /user-management/role/remove/{user_id}`

7. **Verification-Authorization-Service**: The Verification Authorization Service is responsible for authentication and
   authorization logic, including tasks such as token validation, token invalidation, and ensuring the security of the
   system.
    - Required Role: None
    - Endpoints:
        - Login: `POST /auth/login`
        - Logout: `POST /auth/logout`
        - Change Password: `PUT /auth/changePassword`

## Technologies Used

- Java 17
- Spring Boot 3.1.0
- Maven
- Spring Security
- JWT (JSON Web Tokens)
- Docker 23.0.5
- PostgreSQL
- Log4j2 2.20.0
- Spring Webflux
- ModelMapper

## Getting Started

To get started with this project without docker, follow these steps:

1. Clone the repository: `git clone https://github.com/AydinIlyas/SpringBoot-Microservices`
2. Build the project using Maven: `mvn clean install`
3. Start each microservice individually by navigating to its directory and running `mvn spring-boot:run`
4. Access the microservices through their respective endpoints

To get started with this project with docker, follow these steps:

1. Clone the repository: `git clone https://github.com/AydinIlyas/SpringBoot-Microservices`
2. Build the project using Maven: `mvn clean install`
3. Start the microservices using Docker Compose: Navigate to the root directory of the project (the parent module) and
   run `docker-compose -f docker-compose.dev.yml up --build`.
4. Access the microservices through their respective endpoints

## Usage
- There is by default a user with the username: 'admin' and password: 'admin'
- You can use this account to create new users with roles etc. and go on.

## Contributing

Contributions are welcome! If you find any issues or want to add new features, please submit a pull request. Make sure
to follow the existing coding style and provide clear commit messages.
