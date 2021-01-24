package com.pos.streaming

import scala.util.Try

object Run extends App {
  val inputDirectory = Try(args(0)).getOrElse("src/main/resources/input")
  val outputDirectory = Try(args(1)).getOrElse("src/main/resources/output")

  new AggregateSalesStreamingPerDay(inputDirectory, outputDirectory).start()
}
