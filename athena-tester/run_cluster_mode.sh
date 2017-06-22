# specify your master address
#mvn clean install -DskipTests
#SRI
spark-submit --class "athena.user.application.Main" --driver-memory 15G --executor-memory 15G --master spark://192.168.4.21:7077 target/athena-tester-1.6.0.jar
#US HOME
#spark-submit --class "athena.user.application.Main" --master spark://192.168.229.129:7077 target/athena-tester-1.6.0.jar
