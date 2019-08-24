# Kafka-SparkStreaming-Elasticsearch
kafka consumer - sparkstreaming- elasticsearch
spark streaming 으로 kafka-0.10 consumer api를 사용하여 elasticsearch에 저장하는 프로젝트

토픽은 search_word 로 고정되어있음
실행 순서 : 

1. kafka 를 위한 zookeeper 실행 
2. Kafka 서버 실행 - 토픽 (search_word) 추가 
3. elasticsearch 실행 
4. Build 한 jar 파일 만들기 
   ProjectRoot에서 실행 
       $./gradlew fatJar
5. (테스트) kafka console producer를 통해서 테스트 실행 함 
   kafkaspark-all-1.0-SNAPSHOT.jar 는 프로젝트 루트의 아래 /build/lib에 위치해있다.
       $java -jar kafkaspark-all-1.0-SNAPSHOT.jar 127.0.0.1 9200 localhost:9092
       
파라미터 : 
첫 번째 - elasticsearch 주소, 
두 번째 - elasticsearch 포트,
세 번째 - kafkaip:kafkaport,
   




