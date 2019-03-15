package com.infy
import org.apache.spark.sql.SparkSession

object ReadFromCassandra {
    def main(args: Array[String]) {
      
      import com.datastax.spark.connector._
       val spark = SparkSession.builder().appName("Demo").master("local")
       .config("spark.cassandra.connection.host","172.17.0.2")
       .config("spark.cassandra.connection.port",9042)
.getOrCreate()
import org.apache.spark.sql.cassandra._

val df = spark
  .read
  .cassandraFormat("test", "emp")
  .load()
 df.show
    }
}