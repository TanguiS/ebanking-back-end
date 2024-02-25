# ENSICAEN - Industrial Project - BackEnd2023

***

## Description
The purpose of this project is the realisation of a back office (issuer et acquirer) for students and professors of the ENSICAEN.

## Badges

### DevOps

<img src="https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white" />
<img src="https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=black" />
<img src="	https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white" />
<img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white" />

<img src="https://img.shields.io/badge/GitLab-330F63?style=for-the-badge&logo=gitlab&logoColor=white" />
<img src="https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white" />


### Technologies

<img src="https://img.shields.io/badge/npm-CB3837?style=for-the-badge&logo=npm&logoColor=white" />

<img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=whit" />
<img src="https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot" />
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" />
<img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white" />
<img src="	https://img.shields.io/badge/Node%20js-339933?style=for-the-badge&logo=nodedotjs&logoColor=white" />
<img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" />
<img src="https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white" />


### IDE

<img src="https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white" />
<img src="https://img.shields.io/badge/WebStorm-000000?style=for-the-badge&logo=WebStorm&logoColor=white" />


### Laptop brand used for the development of the application

<img src="https://img.shields.io/badge/dell%20laptop-007DB8?style=for-the-badge&logo=dell&logoColor=white" />
<img src="https://img.shields.io/badge/MSI%20laptop-FF0000?style=for-the-badge&logo=msi&logoColor=white" />
<img src="https://img.shields.io/badge/lenovo%20laptop-E2231A?style=for-the-badge&logo=lenovo&logoColor=white" />


## Prerequisites

- [ ] Optional : [Docker Desktop](https://docs.docker.com/desktop/release-notes/#4220) (v4.22.0) 
- [ ] Optional : [Postman](https://www.postman.com/downloads/)
- [ ] [Docker Engine](https://docs.docker.com/engine/install/) (version 24.0.5)
- [ ] [Maven](https://maven.apache.org/download.cgi) (v3.8.x)
- [ ] [Node.js](https://nodejs.org/en/blog/release/v21.1.0) (v20.0.9)
- [ ] [npm](https://www.npmjs.com/package/npm/v/10.2.1) (v10.2.4)
- [ ] [WSL2](https://learn.microsoft.com/fr-fr/windows/wsl/install)
- [ ] [JDK21](https://www.oracle.com/fr/java/technologies/downloads/)

## Installation
- [ ] Import the [postman collection](./Back-End/controler-api-test.postman_collection.json)

### Back-End
Go to the root folder of the project
- [ ] Import the [postman collection](./collection)
- [ ] [Optional] Open Docker Desktop
- [ ] In a terminal, enter the following commands :
```bash
cd ./Back-End
mvn clean install
cp ./target/BackEndApplication.jar ./docker/api/
cd ..
docker compose up
```

#### Resetting project
- Stop all the containers
- Delete all the containers
- [ ] In a terminal, at the root folder, put the following command :
```bash
docker compose
```

### Front-End
Go to the root folder of the project
- [ ] In a terminal, go to the ./Front-End folder with the following command :
```bash
cd ./Front-End
npm install -g @angular/cli
npm install
npm run start
```

## Endpoints
You can see the end points [here](http://localhost:8080/swagger-ui.html).

## Contribution

If you'd like to contribute to a project and make it better, we'd love to hear from you. Just ask Mr Ndiaga Faye for permission.

## Authors and acknowledgment

- David Guo, intern at PwC Luxembourg
- Tangui Steimetz, intern at Mobile Process
- Kylian Picque, intern at Sopra Steria Toulouse
- Thomas Seng, intern at Sopra Steria Toulouse

## License

MIT License
Copyright (c)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

## Project status
The project is scheduled for completion on 8/02/2024. It will probably be taken over by other third year students from ENSICAEN's Computer Science department.
