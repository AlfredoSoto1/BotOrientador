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
### What's new?
The current version for this software is: v2023.2-Beta
* Has most of the commands ported from prior version in python to java
* Uses Discord modals to enter student data
* Assigns roles to each corresponding user
* Has button functionality

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
push and/or pull from cloud's repository
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
## How to use bot?
<details>
<summary>Here are the bot commands that can be used in any chat inside the pre-defined server</summary>
  
| Method                              | Description                                                                                                                         |
| ----------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| `/help command: provide`            | Provides a list of all the commands that the bot knows about and can be run by _any_ user.                                          |
| `/curriculo program: choose`        | Provides a PDF containing the department's curriculum. It accepts the following options: __(INEL/ICOM/INSO/CIIC)__.                 |
| `/map`                              | Provides a link to an official UPRM site that contains the map of the campus.                                                       |
| `/links`                            | Provides a list of links with important general information about the campus and the department.                                    |
| `/salon code: provide`              | Provides a link to an official UPRM site that contains information about a specific classroom.                                      |
| `/calendario`                       | Provides a link containing the academic calendar of UPRM.                                                                           |
| `/telephone_guide`                  | Contains a list of contact numbers to choose.                                                                                       |
| `/ls_projects`                      | Provides a list of projects and research done in relation to the __INEL/ICOM/INSO/CIIC__ departments.                               |
| `/estudiante-orientador choose: department` | Provides a list of all the EO's in the server with the department provided as a parameter in the command.                   |
| `/ls_student_orgs`                  | Provides a list of student organizations.                                                                                           |
| `/rules`                            | Provides a list of rules for the server.                                                                                            |
| `/guia-prepistica`                  | Provides a PDF containing the guide on how-to for prepas.                                                                           |

</details>

### API Usage (for developers)
#### Where to start debugging?
To start running the software in Eclipse IDE, you need to run the java file named
`ApplicationEntry.java`, located at `application.client` package

### Configuration
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
