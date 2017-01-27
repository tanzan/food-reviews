package foodreviews

import scala.concurrent.{Await, Future}
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by serg on 1/26/17.
  */
object Main  extends App {

  import Review._


  def reviewStream() = Source.fromFile(args(0)).getLines().toStream.tail.map(parseEntry(_))

  //1) Finding 1000 most active users (profile names)
  val usersFuture = Future {
    reviewStream().map(_.profileName)
      .groupBy(user => user).mapValues(_.size)
      .toIndexedSeq.sortWith(_._2 > _._2).take(1000)
  }

  //2) Finding 1000 most commented food items (item ids).
  val productsFuture = Future {
    reviewStream().map(_.productId)
      .groupBy(id => id).mapValues(_.size)
      .toIndexedSeq.sortWith(_._2 > _._2).take(1000)
  }

  //3) Finding 1000 most used words in the reviews
  val wordsFuture = Future {
    reviewStream().flatMap(r => splitWords(r.text).map(w => (w, 1)))
      .foldLeft(Map[String, Int]())((words, w) => countWords(words, w)).toIndexedSeq.sortWith(_._2 > _._2).take(1000)
  }

  println("--------- Most Active Users --------")
  Await.result(usersFuture, 30 minutes).foreach(println)

  println()
  println("--------- Most Commended Items -----")
  Await.result(productsFuture, 30 minutes).foreach(println)

  println()
  println("--------- Most Used Words ---------")
  Await.result(wordsFuture, 30 minutes).foreach(println)

  //4) We want to translate all the reviews using Google Translate API.
  // Not implemented because lack of time.

  //See README.md for additional comments.

}
