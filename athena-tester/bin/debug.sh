export SPARK_SUBMIT_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,address=5005,suspend=n
spark-submit --class "athena.user.application.Main" --master spark://127.0.0.1:7077 target/athena-spark-tester-1.6.0.jar --num-executors 1 --executor-cores 1
export SPARK_SUBMIT_OPTS=