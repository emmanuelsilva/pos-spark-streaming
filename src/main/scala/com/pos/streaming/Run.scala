package com.pos.streaming

import scala.util.Try

object Run extends App {
  val inputDirectory = Try(args(0)).getOrElse("/Users/emmanuel.silva/temp-dir/input")
  val checkpointDirectory = Try(args(1)).getOrElse("/Users/emmanuel.silva/temp-dir/checkpoint")
  val outputDirectory = Try(args(2)).getOrElse("/Users/emmanuel.silva/temp-dir/output")

  new AggregateSalesStreamingPerDay(inputDirectory, checkpointDirectory, outputDirectory).start()
}
