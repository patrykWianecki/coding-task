Application works with Java 14, Spring Boot 2.3.1, Maven 3.6.3

By default, application starts on port `8888`.

To run application in `application.yml` set your own database name, username and password.

Application can be run via IntelliJ from `CodingTaskApplication.java` or from command line.

To run application via command line, go to project folder and type:

`mvn clean install`
after that, go to target directory

`cd target`
and type

`java -jar coding-task.jar`

Example endpoints are located in

`coding-task/requests`

Example files used to add data are located in 

`coding-task\src\test\resources`