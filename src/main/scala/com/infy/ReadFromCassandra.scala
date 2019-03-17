package com.infy
import org.apache.spark.sql.SparkSession

object ReadFromCassandra {
    def main(args: Array[String]) {
      
   import com.datastax.spark.connector._
    val spark = SparkSession.builder().appName("Demo").master("local")
      .config("spark.cassandra.connection.host", "172.17.0.2")
      .config("spark.cassandra.connection.port", 9042)
      .getOrCreate()
    import spark.implicits._
    /*        import org.apache.spark.sql.cassandra._

        var df = spark
  .read
  .cassandraFormat("emp", "test")
  .load()
 df.show*/

    //DF from Hive Table
    val targetDf = Seq(("rajesh", "raj", "raj", "raj", "raj")).toDF("1", "2", "10", "9", "12")
    //DF from Cassandra
    var sourceDf = Seq(("rajesh", "raj", "RJ", "RJ")).toDF("1", "9", "3", "2")

    import org.apache.spark.sql.functions._
    val targetColumns = targetDf.columns.toSet
    val sourceColumns = sourceDf.columns.toSet
    var extraColumnsTobeDropped = sourceColumns.diff(targetColumns)

    //Filtering columns of Struct DataType
    val newTargetColumn = targetColumns.filter(p => !(p.equals("1") || p.equals("10")))

    val finalColumns = newTargetColumn.diff(sourceColumns)

    //extra Columns to be Dropped
    for (i <- extraColumnsTobeDropped)
      sourceDf = sourceDf.drop(i)

    //Other Columns populated to null
    for (i <- finalColumns)
      sourceDf = sourceDf.withColumn(i, lit(null).cast(StringType))

    //Column names which has StructDataType
    val structArray = Array("CID", "name")

    val arrayOfStructFields = structArray.map(f => StructField(f, StringType))
    val df1 = sourceDf.withColumn("10", lit(null).cast(ArrayType((StructType(
      arrayOfStructFields)))))

    df1.printSchema
    }
}
