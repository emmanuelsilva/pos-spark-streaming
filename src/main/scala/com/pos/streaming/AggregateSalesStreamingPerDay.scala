package com.pos.streaming

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.{col, count, date_trunc, sum}
import org.apache.spark.sql.types._

class AggregateSalesStreamingPerDay(salesInputDirectory: String, outputDirectory: String) {

  lazy val spark = SparkSession
    .builder()
    .config("spark.master", "local[*]")
    .appName("POS data streaming aggregator")
    .getOrCreate()

  lazy val input_sale_schema = StructType(Seq(
    StructField("time",          TimestampType),
    StructField("transactionId", LongType),
    StructField("customer",      StringType),
    StructField("product",       StringType),
    StructField("price",         FloatType)
  ))

  spark.sparkContext.setLogLevel("ERROR")

  def getSalesStreaming() = {
    spark
      .readStream
      .schema(input_sale_schema)
      .format("csv")
      .option("delimiter", ",")
      .load(salesInputDirectory)
      .withColumn("hour", date_trunc("hour", col("time")))
  }

  def aggregateSalesPerHour(salesStreaming: DataFrame) = {
    salesStreaming
      .groupBy("hour")
      .agg(
        count("transactionId").as("qtd"),
        sum("price")
      )
  }

  def start() = {
    val salesStreaming = getSalesStreaming()
    val aggregatedSales = aggregateSalesPerHour(salesStreaming)

    aggregatedSales
      .writeStream
      .outputMode("complete")
      .outputMode("complete")
      .format("console")
      .start()
      .awaitTermination()

      //save into CSV file
      //.format("csv")
      //.option("path", outputDirectory)
      //.start()
      //.awaitTermination()



  }

}
