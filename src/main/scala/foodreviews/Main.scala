package foodreviews

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.Source

/**
  * Created by serg on 1/26/17.
  */
object Main  extends App {

  import Review._

  def reviewStream() = Source.fromFile(args(0)).getLines().toStream.tail.map(parseEntry(_))

  //1) Finding 1000 most active users (profile names)
  val usersFuture = mostActiveUsers(reviewStream)

  //2) Finding 1000 most commented food items (item ids).
  val productsFuture = mostCommentedItems(reviewStream)

  //3) Finding 1000 most used words in the reviews
  val wordsFuture = mostUsedWords(reviewStream)

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
