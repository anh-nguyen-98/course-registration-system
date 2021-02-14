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

### View/ Add/ Update User 
1. View User: 
- Returns a list of all users in the system iff the requester is an Admin user. 
- The requester is required to send authentication info: email & password. 
- In Postman, set: 
  - GET method type
  - Request URL: 
```
localhost:8080/user
```
  - Request body: email and password of the requester 
  
  Example: 
  
 ```json
 {
    "email": "anh.nguyen.190005@student.fulbright.edu.vn",
    "password": "1234" 
}
 ```
 
2. Add User: 
- Returns "Success" and adds a new user to the system iff the requester has Admin role, else returns "Access denied"
- The requester is required to send 2 info: 
  - requester authentication info: email & password 
  - user info: 
    - email (required)
    - password (required)
    - name (optional)
    - role (optional)
3. Update User: 

### View/ Add/ Update Course 

### Register/ Cancel Course 

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
