package com.kafkastream.word

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.elasticsearch.spark.streaming._

object KafkaStreaming{
  def main(args: Array[String]): Unit = {
    val esIp=args(0)
    val esPort = args(1)
    val kafkaIpPort = args(2)
    println("Start Project")
    println("ElasticSearch IP address: "+args(0) +":"+args(1))
    println("Kafka Server IP,Port : "+ args(2))
    val conf = new SparkConf().setAppName("KafkaSpark").setMaster("local")
    conf.set("es.index.auto.create","true")
    conf.set("es.nodes",esIp)
//    conf.set("es.nodes","127.0.0.1")
    conf.set("es.port",esPort)
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(10))//stream context



    val kafkaParams = Map(
      "bootstrap.servers" -> kafkaIpPort,
//      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "testGroup",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean))
    val topics = Set("search_word")
    val kafkaStream :InputDStream[ConsumerRecord[String, String]]= KafkaUtils.createDirectStream[String,String](ssc,PreferConsistent,Subscribe[String,String](topics,kafkaParams))

    val streamData  =  kafkaStream.map(raw=>Map("word"->raw.value()))
    println("SparkContext refresh")
    streamData.print()

    streamData.saveToEs("realtime_word/_doc")

    ssc.start()
    ssc.awaitTermination()
  }
}
