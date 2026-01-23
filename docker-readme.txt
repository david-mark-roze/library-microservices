# Start the Docker containers in detached mode
docker compose up -d

# List the running Docker containers
docker ps

# Stop the Docker containers
docker compose down

# Create a Kafka topic named "loan-event"
docker exec -it kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --if-not-exists --topic loan-event --partitions 1 --replication-factor 1

# List all Kafka topics
docker exec -it kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

# Start a Kafka consumer for the "loan-event" topic with headers and keys
# Remember to run in the docker exec -it kafka bash shell first.
/opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic loan-event \
  --partition 0 \
  --offset earliest \
  --max-messages 50 \
  --property print.headers=true \
  --property print.key=true \
  --property key.separator=" | "



