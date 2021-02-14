# Course Registration RESTful APIs

Course Registration RESTful APIs are role-based APIs for mananaging user accounts and course content, dealing with course registration and course cancellation in a university.

## Installation

1. Clone the repo
```bash
git clone https://github.com/anh-nguyen-98/course-registration-system.git
```

The repo includes the [database](https://github.com/anh-nguyen-98/course-registration-system/blob/main/testdb.mv.db) for the system. 

2. [Install Postman](https://www.postman.com/downloads/) to send the http requests and use the APIs 

In each Postman request, configure Header section: 

![header](https://github.com/anh-nguyen-98/course-registration-system/blob/main/images/header%20postman.jpg)


## Usage

### Role-based access
There are 3 roles in system: admin, student, guest. 

![role](https://github.com/anh-nguyen-98/course-registration-system/blob/main/images/role.jpg)

Each role has different sets of permisions: 
1. admin: 
- view, edit users 
- view, edit courses 
- register, cancel courses for any students 

2. student: 
- view courses 
- register courses only for him/her
- cancel courses only for him/her

3. guest: 
- view courses 

![Access](https://github.com/anh-nguyen-98/course-registration-system/blob/main/images/access.jpg)

### View/ Add/ Update/ Delete User 
**1. View User**: 
- Returns a list of all users or a specfic user info in the system iff the requester is an Admin user, else returns an empty list/ empty user. 
- The requester is required to send authentication info: email & password. 
- In Postman, set: 
  - GET method type
  - Request URL: 
  
  for viewing all users: 
```
localhost:8080/user
```
 
 for viewing a specific user: 
 ```
 localhost:8080/user/{id}
 ```
 - Request body: email and password of the requester 
  
  Example: 
  
 ```json
 {
    "email": "anh.nguyen.190005@student.fulbright.edu.vn",
    "password": "1234" 
}
 ```
 
**2. Add User**: 
- Returns "Success" and adds a new user to the system iff the requester has Admin role, else returns "Access denied"
- The requester is required to send 2 info: 
  - requester authentication info: email & password 
  - user info: 
    - email (required)
    - password (required)
    - name (optional)
    - role (optional)
 - In Postman, set: 
   - POST method type
   - Request URL: 
   ```
   localhost:8080/user
   ```
   - Request body: 
   
   Example: 
   ```json
   {
    "user1": {
        "email": "anh.nguyen.190005@student.fulbright.edu.vn",
        "password": "1234"
    },
    "user2": {
        "name": "Phan Lan Khanh",
        "email": "khanh.phan.190034@student.fulbright.edu.vn",
        "password": "1234",
        "role": "student"
    }
    }
    ```
  
**3. Update User**: 
- Updates info (name, email, password or role) of specific user iff the requester has an Admin role.
- The requester is required to send 3 info: 
  - requester authentication info: email & password 
  - user id (that needs update)
  - user update info: 
    - email (required)
    - password (required)
    - name (optional)
    - role (optional)
 - In Postman, set: 
   - PUT method type
   - Request URL: 
   ```
   localhost:8080/user/{id}
   ```
   - Request body: 
   
Example 
   ```
   {
        "name": "Phan Lan Khanh",
        "email": "khanh.phan.190034@student.fulbright.edu.vn",
        "password": "1234",
        "role": "student"
    }
   ```

**4. Delete User**: 
- Deletes a user from the system iff the requester has Admin role. 
- The requester is required to send 2 info: 
  - requester authentication info: email & password 
  - user id (that needs deletion)
- In Postman, set: 
  - DELETE method type
  - Request URL: 
   ```
   localhost:8080/user/{id}
   ```

### View/ Add/ Update/ Delete Course 
**1. View Course**: 
- Returns a list of all courses or a specfic course info in the system 
- In Postman, set: 
  - GET method type
  - Request URL: 
  
  for viewing all courses: 
```
localhost:8080/course
```
 
 for viewing a specific course: 
 ```
 localhost:8080/course/{id}
```
  
 
**2. Add Course**: 
- Returns "Success" and adds a new course to the system iff the requester has Admin role
- The requester is required to send 2 info: 
  - requester authentication info: email & password 
  - course info: 
    - code (required)
    - name, capacity, startDate, endDate, currentStudents, pastStudents, prerequisites (optional)
 - In Postman, set: 
   - POST method type
   - Request URL: 
   ```
   localhost:8080/course
   ```
   - Request body: 
   
   Example: 
   ```json
{
    "auth": 
    {
       "email": "anh.nguyen.190005@student.fulbright.edu.vn",
       "password": "1234"
    },
    "course":
    {
        "name": "Human Computer Interaction",
        "code": "CS303",
        "capacity": 5,
        "startDate": "2020-09-05",
        "endDate": "2020-12-23"
    }

}
    ```
  
**3. Update Course**: 
- Updates info (name, code, capacity, startDate, endDate, currentStudents, pastStudents, prerequisites) of specific course iff the requester has an Admin role.
- The requester is required to send 3 info: 
  - requester authentication info: email & password 
  - course id (that needs update)
  - course update info: 
    - code (required)
    - name, capacity, startDate, endDate, currentStudents, pastStudents, prerequisites (optional)

 - In Postman, set: 
   - PUT method type
   - Request URL: 
   ```
   localhost:8080/course/{id}
   ```
   - Request body: 
   
Example 
   ```
    {
        "name": "Human Computer Interaction",
        "code": "CS303",
        "capacity": 5,
        "startDate": "2020-09-05",
        "endDate": "2020-12-23"
    }
   ```

**4. Delete Course**: 
- Deletes a course from the system iff the requester has Admin role. 
- The requester is required to send 2 info: 
  - requester authentication info: email & password 
  - course id (that needs deletion)
- In Postman, set: 
  - DELETE method type
  - Request URL: 
   ```
   localhost:8080/course/{id}
   ```

### Register/ Cancel Course 

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
