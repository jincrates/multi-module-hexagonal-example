# Kafka 실행 명령어

```
cd infra/docker-compose/

# 주키퍼 실행(종료는 down)
docker-compose -f common.yml -f zookeeper.yml up   

# 주키퍼 실행 확인
echo ruok | nc localhost 2181   

# 클러스터 실행(종료는 down)
docker-compose -f common.yml -f kafka_cluster.yml up

# 카프카 초기 실행(종료는 down)
docker-compose -f common.yml -f init_kafka.yml up


# 접속
http:localhost:{port}

# Add Cluster
- Cluster Name: food-ordering-system-cluster
- Zookeeper Hosts: zookeeper:2181
```