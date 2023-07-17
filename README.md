# Bot-Orientador Official Software Documentation
This is a new re-built version from the original Discord bot for Team-Made's Developer team.
This software is a fully autonomous Discord bot for managing incomming students main Discord server.
Assigning roles, replying, deleting and running commands as its main feature holding its main data
in a database using Microsoft Access files with secure password and encryption.

## Table of Contents
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
  - [How to use bot](#how-to-use-bot)
  - [API Usage](#api-usage-for-developers)
  - [Configuration](#configuration)
- [Contributing](#contributing)
- [License](#license)


## Features
### Working on these features:
The current version for this software is: v2023.3-Beta
* Implement all organizations
* Leader board and points
* voting polls
* test software with real users
* debug software with all test users
* do final touches and deploy
* present to team

## Getting Started
### Prerequisites
To contribute code for this software you will need to have knowledge but not limited to:
  * Knowing Java and SQL
  * A bit of Python (To understand the prior version of this new implementation. Not required but useful)
  * Adapt to new APIs used throught this software refered as 'drivers'. You can check these in the `drivers` package
    
To run this software you will need for this version (at the moment) is Eclipse IDE.
You will also need to create a custom Discord token to feed the bot with, in this way when
the software starts it knows to where connect and start running the bot in the corresponding server.
For this software you will also need Git installed in your preferred Operating system. This will be
important for cloning the repository, working with branches and git staging commits to later
push and/or pull from/to remote repository
### Installation
To install this software first clone the repository to a folder of your choice in your device. After cloning,
you can open Eclipse IDE and import with git (smart import) the already cloned repository.

#### How to clone from github using terminal
Open the folder where you would like to clone the repository and open a terminal
that is located at the folder where you want to clone the repository.
Then use the following git command:
```git
git clone `GitHub's repository link here`
```
## Usage
### How to use bot?
<details>
<summary>Here are the bot commands that can be used in any chat inside the pre-defined server</summary>
  
| Slash Command                       | Description                                                                                                                         |
| ----------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| `/help`                             | Provides a list of all the commands that the bot knows about and can be run by _any_ user.                                          |
| `/curriculo <departamento>`         | Provides a PDF containing the department's curriculum. It accepts the following options: __(INEL/ICOM/INSO/CIIC)__.                 |
| `/map`                              | Provides a link to an official UPRM site that contains the map of the campus.                                                       |
| `/links`                            | Provides a list of links with important general information about the campus and the department.                                    |
| `/salon <letra>`                    | Provides a link to an official UPRM site that contains information about a specific classroom.                                      |
| `/calendario`                       | Provides a link containing the academic calendar of UPRM.                                                                           |
| `/contact <who?>`                   | Contains a list of contact numbers to choose.                                                                                       |
| `/ls_projects <select>`             | Provides a list of projects and research done in relation to the __INEL/ICOM/INSO/CIIC__ departments.                               |
| `/estudiante-orientador <dept>`     | Provides a list of all the EO's in the server with the department provided as a parameter in the command.                           |
| `/ls_student_orgs <select>`         | Provides a list of student organizations.                                                                                           |
| `/reglas`                           | Provides a list of rules for the server.                                                                                            |
| `/guia-prepistica`                  | Provides a PDF containing the guide on how-to for prepas.                                                                           |

### Program flow

insert image of how the program executes and it's levels of abstraction

</details>

### API Usage (for developers)
#### Prepare software before debugging!!!
1) Setting up the database
* You first need to create a `database/` folder in the main project's directory.
  You will need to reach to one of the managers of TeamMade to give you access to our
  database. Once you have the database as a Microsoft Access file, you can paste it
  inside the created `database/` directory. It should be named as `TeamMadeDB.accdb`

2) Setting Bot-token
* You are going to need a bot token before launching the software.
  You can either create one for your own testing purposes or ask to
  one of the official managers of TeamMade to test the bot lively in
  the server (not recommended) only if the bot is not in a pre-release state

* You will need to create a file inside the `assets/bot-token/` directory named
  `bot-token.tkn`. There you will paste the bot token and you should be ready to go.

#### Where to start debugging?
To start running the software in Eclipse IDE, you need to run the java file named
`ApplicationEntry.java`, located at `application.client` package.

Note that once the program starts running you will need to wait a couple of minutes
before start testing all commands. This is because the JDA is loading all the data
and it might take a few minutes or seconds. If you want to reduce this time you can
do so by commenting the `listenerAdapterManager.loadComponentAdapter(...@Component)` methods inside the
bot entry class. 

### Configuration
(Currently Depricated)
This software will start running in debug mode by default for developers. If you want to deploy the bot as an official
build version, you will need to turn off the debug flag located at the `ApplicationEntry.java` file by switching the flag to false

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
 Copyright © 2023 Team-Made and Developer team. All rights reserved.

 Prior version link: https://github.com/CarolinaZRM/bot-discord
