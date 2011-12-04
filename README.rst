================
SalSSuite README
================

This project is discontinued and will not receive any updates for the time being.
---------------------------------------------------------------------------------

This file merely provides an overview for developers about the SalSSuite
and its Git repository at github.com. User documentation is not available
at the moment. The javadoc gives fairly detailed explanations of all
aspects of the SalSSuite.

Introduction
------------

The SalSSuite is a suite of programmes written entirely in Java and
designed for organisers of the "Schule
als Staat" school project, a school activity more or less common in the
south of Germany. The project transforms an entire school into a fictive state for
a short period of time (several days), featuring for example a political
system, an own currency, an economical system (that is people having
to work and gain money) etc.

The programme is developed to help organisers of this definitely not
undercomplex project to establish a functioning and as-good-as-possible
realistic state. The SalSSuite is set up as a server-client-programme
where the server is divided up into various modules, each being responsible
for a certain area of the project.

Current State of Development
----------------------------

- established a server framework

- added the following modules: duty, accounting, magazine, company management

- added additional functionality to the server for citizen management

- established an account management system for the whole programme

- wrote various generic code snippets to make further development easier

- wrote a primitive installer/uninstaller

- wrote an ant buildfile to automatise distribution

Next Major Steps
----------------

- add support for localisation

Repository Contents
-------------------

- **./src** contains all the source files

- **./lib** contains necessary libraries (currently Derby and MarkdownJ)

- **./build.xml** NetBeans-generated ant buildfile

- **./ant/build.xml** custom buildfile to do the 'jar-ing' of clients and servers (as NetBeans does not support projects with multiple main() methods)

- **./input** files for testing the server (see below)

Steps Necessary to Start a Server
---------------------------------

- Compile all the files in ./src (using the libraries from ./lib).

- Customise the buildfile ./ant/build.xml. Set the properties in the beginning to point to folders on your system.

- Call "ant install" on said buildfile.

- Install the SalSSuite to any folder you like.

- Start the Server (Server.jar) and set up a new project.

 - In the project setup dialog give ./input as the "Eingabepfad".

 - The other values can be set at will.

- All modules of the server can be accessed as user "admin" with the password you have chosen when installing the SalSSuite.

- Start any client (*Client.jar) and connect to the server using the same authentication.

Prerequisites for Testing the Server
------------------------------------

A text file named "install" (no filename extension) with a standard MD5 hash of some password of your choice
must be located in the working directory when testing the server. You can then use
this password to access the Server modules as user "admin" as well as the clients.

Active Developers
-----------------

Jannis Limperg (Germany)

Any help is thoroughly appreciated.
