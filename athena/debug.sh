mvn clean install -DskipTests
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=5005,suspend=n -jar target/athena-application-1.6.0.jar