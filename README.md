# CS5031-P2 RESTful API for a Library Lending System

# Purpose
This project implements a library lending system using a RESTful API and JavaFX client user interface.

Users can manage members in the library, including adding, deleting, and updating members.

Users can also manage the books of the library, including adding and deleting books.

Members of the library can borrow and return books, and the check-out status of books is presented in the UI, as is a summary of the books checked-out by each user.

# Compilation and Execution Instructions

To install the dependencies, build the project, and run the unit tests:

`cd <path_directory_cloned_to>/cs5031-p2/`

`mvn clean install`

To run the target '.jar' file which runs the backend and launches the client UI:

`cd <path_directory_cloned_to>/cs5031-p2/`

`java -jar target/CS5031-P2-0.0.1-SNAPSHOT.jar`

# Test Instructions

`cd <path_directory_cloned_to>/cs5031-p2/`

`mvn test install`

Note: Some of the team experienced a JavaFX exception when running the front-end tests using TestFX.

This appeared to be dependent on the user.

# Code Coverage
A Jacoco code coverage file is produced at the following directory after running the unit tests:

`<path_directory_cloned_to>/cs5031-p2/target/site/jacoco/index.html`

Open this file in a web browser to view the code coverage for each file.

# Javadocs
The Maven Javadoc plugin can be used to generate a webpage from the Javadocs of the project.

Run the following commands:

`cd <path_directory_cloned_to>/cs5031-p2/`

`mvn javadoc:javadoc`

Open the following page in a web browser:

`<path_directory_cloned_to>/cs5031-p2/target/site/apidocs/index.html`

# Dependencies
The project was developed using Java 17.

The following dependencies are included in the pom.xml file:

SpringBoot - simplifies the process of deploying an application with Spring.

JavaFX - for front-end user interface development.

Surefire - allows unit tests to be run during the maven build process.

JUnit - for unit testing using JUnit5.

Mockito - for mocking objects to make unit tests more atomic.

TestFX - for testing front-end JavaFX user interfaces.

Awaitility - adds the ability to await for asynchronous operations. Useful for front-end testing.

Jacoco - shows the code coverage of each file covered by the JUnit tests.

Json - for reading in Json files to populate the model with data.

Maven Javadoc Plugin - for generating an html page from the Javadocs


