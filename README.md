# Vehicle Error Tracking System Project

This is a sample microservices project built with Spring Boot for tracking vehicle errors. It consists of multiple
microservices that work together to provide a complete system. Each microservice is responsible for a specific
functionality and communicates with others using RESTful APIs.

## Microservices

The project includes the following microservices:

1. **Discovery-Server**: Provides service registration and discovery capabilities.

2. **API-Gateway**: Acts as a centralized entry point, routing requests and managing authentication.

3. **ErrorLogin-Service**: Handles the operations related to vehicles, defects, and locations, providing functionality
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

4. **ErrorListing-Service**: Lists vehicles defects with paging, filtering and sorting. Processes image with marker.
   This service is fetches the required data from error login service.
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

- There is by default a user with the username: 'admin' and password: 'admin'
- You should use this account to create new accounts for other users.
- You should use the generated bearer token obtained during login for all requests to the services.

## API Documentation

### 1. Error Login Service

### Endpoints for vehicles

#### Add Vehicle

Endpoint: `POST /vehicle/add`     
Description: Adds new vehicle to database. VIN must be unique for each car.

Request Body:

```json
{
  "model": "Supra",
  "vin": "011",
  "yearOfProduction": "2003-02-20",
  "engineType": "GASOLINE",
  "transmissionType": "MANUAL",
  "color": "red"
}
```

Response: `Status: 201 Created`

```json
{
  "id": 1,
  "model": "Supra",
  "vin": "011",
  "yearOfProduction": "2003-02-20",
  "engineType": "GASOLINE",
  "transmissionType": "MANUAL",
  "color": "red",
  "defect": null
}
```

#### Update Vehicle

Endpoint: `PUT /vehicle/update{id}`    
Description: Updates existing vehicle.

Request Body:

```json
{
  "model": "R8",
  "vin": "000122",
  "yearOfProduction": "2023-03-31",
  "engineType": "DIESEL",
  "transmissionType": "MANUAL",
  "color": "blue"
}
```

Response: `Status: 200 OK`

```json
{
  "id": 1,
  "model": "R8",
  "vin": "000122",
  "yearOfProduction": "2023-03-31",
  "engineType": "DIESEL",
  "transmissionType": "MANUAL",
  "color": "blue",
  "defect": []
}
```

#### Delete Vehicle

Endpoint: `PUT /vehicle/delete`  
Description: Soft deletes existing vehicle.

Request Body:

```json
1
```

Response: `Status: 200 OK`

```json
{
  "id": 1,
  "model": "R8",
  "vin": "000122",
  "yearOfProduction": "2023-03-31",
  "engineType": "DIESEL",
  "transmissionType": "MANUAL",
  "color": "blue",
  "defect": []
}
```

### Endpoints for defects

#### Add Defect

Endpoint: `POST /defect/add/{vehicleId}`   
Description: Adds new defect to vehicle.

Request Body:

```json
{
  "type": "Broken Window",
  "description": "Broken Window",
  "state": "MINOR"
}
```

Response: `Status: 201 Created`

```json
{
  "id": 1,
  "type": "Broken Window",
  "description": "Broken Window",
  "state": "MINOR",
  "reportTime": "2023-07-16T21:04:34.6367218",
  "reportedBy": "admin",
  "location": null
}
```

#### Update Defect

Endpoint: `PUT /defect/update/{defectId}`  
Description: Updates existing defect.

Request Body:

```json
{
  "type": "Tire",
  "description": "Example",
  "state": "MAJOR"
}
```

Response: `Status: 200 OK`

```json
{
  "id": 1,
  "type": "Tire",
  "description": "Ornek2",
  "state": "MAJOR",
  "reportTime": "2023-07-16T21:04:34.636722",
  "reportedBy": "admin",
  "location": []
}
```

#### Delete Defect

Endpoint: `PUT /defect/delete`  
Description: Soft deletes defect.

Request Body:

```json
1
```

Response: `Status: 200 OK`

```json
{
  "id": 1,
  "type": "Tire",
  "description": "Ornek2",
  "state": "MAJOR",
  "reportTime": "2023-07-16T21:04:34.636722",
  "reportedBy": "admin",
  "location": []
}
```

#### Add Image

Endpoint: `POST defect/add/image/{defectId}`  
Description: Adds image to defect.

Request Parameters:

| Parameter | Type   | Description               |
|-----------|--------|---------------------------|
| `Image`   | `File` | The image to be uploaded. |

Response: `Status: 200 OK`

```json
{
  "id": 1,
  "type": "Tire",
  "description": "Ornek2",
  "state": "MAJOR",
  "reportTime": "2023-07-16T21:04:34.636722",
  "reportedBy": "admin",
  "location": []
}
```

#### Delete Image

Endpoint: `PUT defect/delete/image`  
Description: Soft deletes image.

Request Body:

```json
2
```

Response: `Status: 200 OK`

### Endpoints for locations

#### Add Location

Endpoint: `POST /location/add/{defectId}`  
Description: Adds location to defect.

Request Body:

```json
{
  "x_Axis": 100,
  "y_Axis": 250,
  "height": 30,
  "width": 30,
  "colorHex": "#FFFF00"
}
```

Response: `Status: 201 Created`

#### Update Location

Endpoint:`PUT /location/update/{locationId}`  
Description: Updates location.

Request Body:

```json
{
  "x_Axis": 200,
  "y_Axis": 200,
  "height": 60,
  "width": 50,
  "colorHex": "#00AAAA"
}
```

Response: `Status: 200 OK`

#### Delete Location

Endpoint: `PUT /location/delete`  
Description: Soft deletes location.

Request Body:

```
1
```

Response: `Status: 200 OK`

### 2. Error Listing Service

#### List Vehicles

Endpoint: `GET /list/vehicles`  
Description: Lists vehicles. Fetches vehicles from error login service and lists them with paging, filtering and
sorting.

Request Param:

| Parameter                | Type           | Description                   |
|--------------------------|----------------|-------------------------------|
| `filterModel`            | `String`       | Filter for vehicle model      |
| `filterVin`              | `String`       | Filter for vehicle VIN        |
| `filterYearOfProduction` | `Date`         | Filter for year of production |
| `filterEngineType`       | `String`       | Filter for engine type        |
| `filterTransmissionType` | `String`       | Filter for transmission type  |
| `filterColor`            | `String`       | Filter for color              |
| `page`                   | `Integer`      | Page number                   |
| `size`                   | `Integer`      | Page size                     |
| `sortOrder`              | `String`       | Sort direction (ASC/DESC)     |
| `sortBy`                 | `List<String>` | Sort by field                 |

Response: `Status: 200 OK`

```json
{
  "content": [
    {
      "id": 3,
      "model": "Example",
      "vin": "012",
      "yearOfProduction": "2005-02-20",
      "engineType": "DIESEL",
      "transmissionType": "AUTOMATIC",
      "color": "black",
      "defect": []
    },
    {
      "id": 2,
      "model": "Supra",
      "vin": "011",
      "yearOfProduction": "2003-02-20",
      "engineType": "GASOLINE",
      "transmissionType": "MANUAL",
      "color": "red",
      "defect": [
        {
          "id": 2,
          "type": "Broken Window",
          "description": "Broken Window",
          "state": "MINOR",
          "reportTime": "2023-07-16T21:28:28.788884",
          "reportedBy": "admin",
          "location": []
        }
      ]
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 2,
    "totalElements": 2,
    "totalPages": 1
  }
}
```

#### List Defects

Endpoint: `GET /list/defects`  
Description: Lists defects. Fetches defect from error login service and lists them with paging, filtering and sorting.

Request Params:

| Parameter    | Type      | Description               |
|--------------|-----------|---------------------------|
| `type`       | `String`  | Filter for defect type    |
| `state`      | `String`  | Filter for defect state   |
| `reportTime` | `Date`    | Filter for report time    |
| `reportedBy` | `String`  | Filter for reporter       |
| `vin`        | `String`  | Filter for VIN            |
| `page`       | `Integer` | Page number               |
| `size`       | `Integer` | Page size                 |
| `sortOrder`  | `String`  | Sort direction (ASC/DESC) |
| `sortBy`     | `String`  | Sort by field             |

Response: `Status: 200 OK`

```json
{
  "content": [
    {
      "id": 2,
      "type": "Broken Window",
      "description": "Broken Window",
      "state": "MINOR",
      "reportTime": "2023-07-16T21:28:28.788884",
      "reportedBy": "admin",
      "location": []
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 1,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

### Shows Image

Endpoint: `GET /list/defect-image/{defectId}`  
Description: Processes image with markers. Fetches image and locations from error login service. The type must match the
image type.

Request Params:

| Parameter   | Type      | Description              |
|-------------|-----------|--------------------------|
| `format`    | `String`  | Type of image (png/jpeg) |
| `processed` | `boolean` | Filter for defect state  |

Response: `Status: 200 OK`

- Type png or jpeg

### 3. Terminal Service

#### Lists Terminals

Endpoint: `GET /terminal/getAll`  
Description: Lists terminals with paging, filtering and sorting.

Request Params:

| Parameter   | Type      | Description                |
|-------------|-----------|----------------------------|
| `depName`   | `String`  | Filter for department name |
| `depCode`   | `String`  | Filter for department code |
| `shopCode`  | `String`  | Filter for shop code       |
| `isActive`  | `boolean` | Filter for state           |
| `page`      | `Integer` | Page number                |
| `size`      | `Integer` | Page size                  |
| `sortOrder` | `String`  | Sort direction (ASC/DESC)  |
| `sortBy`    | `String`  | Sort by field              |

Response: `Status: 200 OK`

```json
{
  "content": [
    {
      "depName": "A",
      "depCode": "A",
      "shopCode": "A"
    },
    {
      "depName": "depName",
      "depCode": "depCode",
      "shopCode": "shopCode"
    }
  ],
  "pageable": {
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "pageSize": 5,
    "pageNumber": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 2,
  "size": 5,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "first": true,
  "numberOfElements": 2,
  "empty": false
}
```

#### Add Terminal

Endpoint: `POST /terminal/add`    
Description:Adds new terminal.

Request Body:

```
{
  "depName":"depName",
  "depCode":"depCode",
  "shopCode":"shopCode"
}
```

Response: `Status: 200 OK`

#### Activate Terminal

Endpoint: `PUT /terminal/activate`  
Description: Activates terminal.

Request Body:

```
depCode
```

Response: `Status: 200 OK`

##### Disable Terminal

Endpoint: `PUT /terminal/disable`  
Description: Disables terminal.

Request Body:

```
depCode
```

Response: `Status: 200 OK`

#### Delete Terminal

Endpoint: `PUT /terminal/delete`   
Description: Deletes terminal.

Request Body:

```
depCode
```

Response: `Status: 200 OK`

### 4.User Management Service

#### Lists Users

Endpoint: `GET /user-management/users`  
Description: Lists user with paging, filtering and sorting.

Request Params:

| Parameter   | Type      | Description               |
|-------------|-----------|---------------------------|
| `firstname` | `String`  | Filter for user firstname |
| `lastname`  | `String`  | Filter for user lastname  |
| `email`     | `String`  | Filter for user email     |
| `username`  | `boolean` | Filter for user username  |
| `page`      | `Integer` | Page number               |
| `size`      | `Integer` | Page size                 |
| `sortOrder` | `String`  | Sort direction (ASC/DESC) |
| `sortList`  | `String`  | Sort by fields            |

Response: `Status: 200 OK`

```json
{
  "content": [
    {
      "id": 2,
      "firstname": "A",
      "lastname": "A",
      "username": "testA",
      "email": "testA@gmail.com",
      "role": [
        "LEADER"
      ],
      "gender": "MALE"
    },
    {
      "id": 3,
      "firstname": "B",
      "lastname": "B",
      "username": "testB",
      "email": "testB@gmail.com",
      "role": [
        "OPERATOR"
      ],
      "gender": "MALE"
    },
    {
      "id": 4,
      "firstname": "C",
      "lastname": "C",
      "username": "testC",
      "email": "testC@gmail.com",
      "role": [
        "OPERATOR",
        "ADMIN"
      ],
      "gender": "MALE"
    }
  ],
  "pageable": {
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 5,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalElements": 3,
  "totalPages": 1,
  "size": 5,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 3,
  "empty": false
}
```

#### Create User

Endpoint: `POST /user-management/create`  
Description: Creates new user.

Request Body:

```json
{
  "firstname": "A",
  "lastname": "A",
  "username": "testA",
  "email": "testA@gmail.com",
  "password": "test1234",
  "role": [
    "LEADER"
  ],
  "gender": "MALE"
}
```

Response:
`Status: 201 Created`

```json
{
  "id": 5,
  "firstname": "A",
  "lastname": "A",
  "username": "testA",
  "email": "testA@gmail.com",
  "role": [
    "LEADER"
  ],
  "gender": "MALE"
}
```

#### Update User

Endpoint: `PUT /user-management/update/{user_id}`  
Description: Updates existing user.  
Request Body:

```json
{
  "firstname": "firstname",
  "lastname": "lastname",
  "username": "username",
  "email": "email@gmail.com",
  "gender": "FEMALE"
}
```

Response:
`Status: 200 OK`

```json
{
  "id": 3,
  "firstname": "firstname",
  "lastname": "lastname",
  "username": "username",
  "email": "email@gmail.com",
  "role": [
    "OPERATOR"
  ],
  "gender": "FEMALE"
}
```

#### Delete User

Endpoint: `PUT /user-management/delete`  
Description: Soft Deletes user.  
Request Body:

```json
3
```

Response: `Status: 200 OK`

```json
{
  "id": 3,
  "firstname": "firstname",
  "lastname": "lastname",
  "username": "username",
  "email": "email@gmail.com",
  "role": [
    "OPERATOR"
  ],
  "gender": "FEMALE"
}
```

#### Add User Role

Endpoint: `PUT /user-management/role/add/{user_id}`  
Description: Adds role to user. Roles: ADMIN, OPERATOR, LEADER

Request Body:

```json
"OPERATOR"
```

Response:
`Status: 200 OK`

```json
{
  "id": 5,
  "firstname": "A",
  "lastname": "A",
  "username": "testA",
  "email": "testA@gmail.com",
  "role": [
    "OPERATOR",
    "LEADER"
  ],
  "gender": "MALE"
}
```

#### Remove User Role

Endpoint: `PUT /user-management/role/remove/{user_id}`
Description: Removes role from user. Roles: ADMIN, OPERATOR, LEADER  
Request Body:

```json
"LEADER"
```

Response:
`Status: 200 OK`

```json
{
  "id": 5,
  "firstname": "A",
  "lastname": "A",
  "username": "testA",
  "email": "testA@gmail.com",
  "role": [
    "OPERATOR"
  ],
  "gender": "MALE"
}
```

## 5.Verification-Authorization Service

#### Login

Endpoint: `POST /auth/login`  
Description: Login for users.  
Request Body:

```json
{
  "username": "admin",
  "password": "admin"
}
```

Response:`Status 200 OK`

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhNjc1ZGEzYi0yOWEzLTRlMjAtYTFhYy0xODQxZjU2Y2FmZjkiLCJzdWIiOiJhZG1pbiIsImlhdCI6MTY4OTUzNzMxMSwiZXhwIjoxNjg5NTQwOTExfQ.FotzY4QeIIJls8DRFcbZCP5uJEeUS4idzgLUU2SCGjU"
}
```

#### Logout

Endpoint: `POST /auth/logout`  
Description: Logout for users.

Request: Uses token for logout.

Response: `Status 200 OK`

#### Change Password

Endpoint: `PUT /auth/changePassword`  
Description: Changes password.  
Request Body:

```json
{
  "oldPassword": "admin",
  "newPassword": "newPassword"
}
```

Response:
`Status: 200 OK`

## Contributing

Contributions are welcome! If you find any issues or want to add new features, please submit a pull request. Make sure
to follow the existing coding style and provide clear commit messages.
