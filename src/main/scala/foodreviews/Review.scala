package foodreviews

import java.util.regex.Pattern

import scala.concurrent.Future

/**
  * Created by serg on 1/27/17.
  */
case class Review(id:Int, productId:String, userId:String, profileName:String,
                  helpfulnessNumerator:Int, helpfulnessDenominator:Int,
                  score:Int, time:Long, summary:String, text:String)

object Review {

  import scala.concurrent.ExecutionContext.Implicits.global

  private final val EntryPattern = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")

  def parseEntry(entry:String):Review = {

    val fields = EntryPattern.split(entry)
      .map(_.trim.replaceAll("\"\"", "\"").replace("\"", ""))

    Review(
      id = fields(0).toInt,
      productId = fields(1),
      userId = fields(2),
      profileName = fields(3),
      helpfulnessNumerator = fields(4).toInt,
      helpfulnessDenominator = fields(5).toInt,
      score = fields(6).toInt,
      time = fields(7).toLong,
      summary = fields(8),
      text = fields(9)
    )

  }

  def mostActiveUsers(reviewStream:() => Stream[Review]):Future[Seq[(String, Int)]] = Future {
    reviewStream().map(_.profileName)
      .groupBy(user => user).mapValues(_.size)
      .toSeq.sortWith(_._2 > _._2).take(1000)
  }

  def mostCommentedItems(reviewStream:() => Stream[Review]):Future[Seq[(String, Int)]] = Future {
    reviewStream().map(_.productId)
      .groupBy(id => id).mapValues(_.size)
      .toSeq.sortWith(_._2 > _._2).take(1000)
  }

  def mostUsedWords(reviewStream:() => Stream[Review]):Future[Seq[(String, Int)]] = {

    def splitWords(s: String): Stream[String] = s.split("\\W+").map(_.toLowerCase).toStream

    def countWords(words: Map[String, Int], word: (String, Int)): Map[String, Int] =
      words + (word._1 -> (words.getOrElse(word._1, 0) + word._2))

    Future {
      reviewStream().flatMap(r => splitWords(r.text).map(w => (w, 1)))
        .foldLeft(Map[String, Int]())((words, w) => countWords(words, w)).toSeq.sortWith(_._2 > _._2).take(1000)
    }
  }
}
