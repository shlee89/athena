mvn clean install -DskipTests
spark-submit --class "athena.user.application.Main" --driver-memory 48G --executor-memory 48G --master spark://127.0.0.1:7077 target/athena-tester-1.6.0.jar