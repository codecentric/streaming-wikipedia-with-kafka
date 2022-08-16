# Streaming Wikipedia with Apache Kafka
This repository contains a few samples how Kafka can be used. All samples are implemented using Kotlin and Gradle. 

- kafka-playground-producer: Receives *server sent events* from Wikimedia via a Webflux client and sends the events to a Kafka topic.
- kafka-playground-streams: Uses the Kafka Stream API to make aggregations on received events. Sends the aggregations to other topics.
- kafka-playground-consumer: Consumes events from Kafka topics. Consuming means in this particular case: logging.
- kafka-playground-connect: Implements a Kafka Connector to replace the producer.

## Installation and Setup
Prerequisites: you need docker and java on your machine. 

The following steps are necessary to get the apps up and running:
1. Build the connector. Therefore execute `cd kafka-playground-connect && ./gradlew clean shadowJar && cd ../`
2. Start the Kafka environment. Therefore execute `docker-compose -f docker/docker-compose.yaml up -d zookeeper kafka kafka-ui connect`
3. Wait until all 4 containers are running (check by using `docker ps -a`)
4. When opening the browser at `localhost:9080` you should see a web UI
5. To activate our custom connector you have to execute `curl -X POST -H "Content-Type:application/json" -d @./kafka-playground-connect/http/sse-source-connect.json http://localhost:8083/connectors`. In this JSON file our custom configuration parameters are set.
6. In the web UI you should now see a topic *wikipedia-via-connect* with an increasing number of messages in it. We are now successfully receiving events from wikimedia. Yay!
7. You can now start the *streams* and *consumer* applications by running `docker-compose -f docker/docker-compose.yaml up -d consumer streams-consumer`. Both apps have a multistage docker build, so you don't have to build the applications on your own machine first.
8. After both containers have started you can check the logs by running `docker logs consumer` or `docker logs streams-consumer`. You should find information about the aggregation made in the streams application in both log files (every 30 seconds).
9. Furthermore, you can see new topics in the web UI. The UI is awful at printing numbers, so you can't really see the aggregated results there.
10. Stopping the application is possible via `docker-compose -f docker/docker-compose.yaml down`.
