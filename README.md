# Smart-Assistant Discord Application
This repository hosts a Java-based Discord bot tailored to support new incoming students in various 
engineering disciplines, including Electrical, Computer, Software, and Computer Science Engineering. 
The bot provides a comprehensive set of features aimed at facilitating orientation and information 
dissemination within Discord servers. This version represents an evolution from the previous Discord
bot developed by Team-Made's Developer Team.

## Table of Contents
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
  - [Application Commands](#application-commands)
- [Contributing](#contributing)
- [License](#license)


## Features
- **Comprehensive Information Commands**:
  - Delivers detailed insights into curriculums, campus facilities, and orientation week schedules.
- **Interactive Functionality**:
  - Engages users with role assignment, game selection, and FAQ responses through intuitive commands.
- **Seamless Integration**:
  - Interfaces with external APIs and databases to ensure real-time updates and efficient information retrieval.
- **Flexible Customization**:
  - Easily adaptable with customizable commands tailored to meet specific server requirements.

## Getting Started
### Prerequisites
To contribute to this software, you'll need knowledge of the following:
- Java: Proficiency in Java programming language.
- Python: Basic understanding (primarily for referencing prior versions).
- PostgreSQL: Familiarity with setting up and managing PostgreSQL databases.
- Docker: Understanding Docker containerization for setting up PostgreSQL locally.

### Installation
1. **Set Up Docker with PostgreSQL**:
   - Install Docker on your machine if not already installed.
   - Pull the PostgreSQL Docker image and create a container only for testing locally.
   You need to setup yout custom container name, database username and password as well.
   ```bash
   docker run --name <container_name_here>
   -e POSTGRES_USER=<your_username>
   -e POSTGRES_PASSWORD=<your_password>
   -e POSTGRES_DB=<your_database>
   -p 5432:5432
   -d postgres
   ```
2. **Prepare the database**:
   You have to prepare the database, this means to create and fill all tables if you want to
   test the commands that require the use of data from the database. If you are running a local
   database using postgres with your container, you can use dunmmy data. If you are using the production
   database, make sure to not destroy any existing data. Please contact the manager to assist on this
   area.
3. **Setup Discord App token**:
   You have to create a new Discord app token if you want to test the application. Once you
   create the token, you have to join the bot to a test server **NOT THE DISTRIBUTION SERVER**.
   If you want to run the bot on the distribution server, ask the manager of the development team
   for the token and permision to run it.
4. **Prepare config file**:
   You have to create a `resources.config` package inside `/src`. Inside the resources.config package,
   you have to create a file named `application.properties`. Inside that file you will set the following
   data:
    ```properties
    # REST Spring Settings
    spring.application.name=AssistantREST
    #server.port=8080
    
    # Application information
    app.name=DiscordAssistant
    app.version=v2024.2.SO4
    
    # Assistant API-REST controller token
    app.rest.controller.token=REST_CONTROLLER_TOKEN_HERE
    
    # Discord bot token
    app.discord.bot.token=YOUR_TOKEN_HERE
    
    # Database credentials for postgres container in docker
    app.database.url=jdbc:postgresql://YOUR_MACHINE_IP:5432/DATABASE_NAME_HERE
    app.database.username=DATABASE_USERNAME_HERE
    app.database.password=DATABASE_PASSWORD_HERE
    app.database.driver=org.postgresql.Driver
    ```
   
5. **Compile the project with maven**:
   Open your IDE of preference (I used Eclipse for this project) and compile the software to a runnable jar file.
   Once the application is compiled and set, you have to put the `/assistant` folder in the root folder where the
   jar is located. You can then start running the app and the bot should go online.

#### Where to start debugging the application?
  - Inside the `assistant.app.entry` package, there ypu will find the entry file that will kick off the app. 

## Usage
### Application Commands
| Command                         | Description                                                                    |
| ------------------------------- | ------------------------------------------------------------------------------ |
| `/calendario`                   | Links to the academic calendar of UPRM.                                        |
| `/estudiante-orientador`        | Lists all EO's in the server by department.                                    |
| `/rules`                        | Lists server rules.                                                            |
| `/guia-prepistica`              | Provides a PDF guide on prepas.                                                |
| `/faculty`                      | Displays detailed information about faculty members.                           |
| `/ls_projects`                  | Provides information on ongoing projects and research.                         |
| `/ls_student_orgs`              | Lists student organizations and their activities.                              |
| `/salon`                        | Finds a building on campus based on its code.                                  |
| `/lab`                          | Finds a lab on campus based on its code.                                       |
| `/links`                        | Provides useful links related to studies and resources.                        |
| `/made-web`                     | Links to the MADE (Media Arts and Digital Entertainment) website.              |
| `/contact-dcsp`                 | Provides information about the Department of Computer Science and Engineering. |
| `/contact-department`           | Displays information about specific departments at UPRM.                       |
| `/contact-decanato-estudiantes` | Provides contact information for the Dean of Students.                         |
| `/contact-guardia-univ`         | Provides contact information for campus security.                              |
| `/contact-asesoria-academica`   | Provides guidance on academic matters and advisories.                          |
| `/contact-asistencia-economica` | Provides information about financial aid and economic assistance.              |
| `/curriculo`                    | Provides general information about academic curricula.                         |
| `/guia-prepistica`              | Provides a guide for incoming freshmen.                                        |
| `/faq`                          | Answers frequently asked questions about the Discord server.                   |
| `/help`                         | Displays a comprehensive help menu for navigating the bot's commands.          |
| `/map`                          | Provides a link to the UPRM campus map.                                        |
| `/rules`                        | Displays the rules and guidelines for the Discord server.                      | 

## Contributing
### How can you help?
* First remember that in order to push  to GitHub any finished and tested code you will need to
  create a branch. This is where __*your*__ new version/implementation will be. All branches must be done localy
  on your machine unless you want to back up your code in the cloud repository (which should be your way to go).

* Always do a Pull Request on GitHub before merging to master. This pull request will let us know when your _build_ is
  done and needs to be revised before merging.

* Be always aware of the Pull Request done, there comments will be done by others
  that checked and approved your code contribution.

* Do **NOT** push to Master.

* Test your code before doing a Pull Request. Write useful and clear information about your new code to facilitate
  others for testing and approving.

## License
This software has a license please read it and make notes on what you can do with this software.

Software based on the previous version: https://github.com/CarolinaZRM/bot-discord
