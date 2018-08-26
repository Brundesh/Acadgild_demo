package com.twitter

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import twitter4j.conf.ConfigurationBuilder
import twitter4j.auth.OAuthAuthorization
import org.apache.spark.streaming.twitter.TwitterUtils
import edu.stanford.nlp.pipeline._

import java.util.Properties
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations

object Twitter {
  def main(args: Array[String]): Unit = {
    val ssc = getStreamContext()
    
     val Array(consumerKey, consumerSecret, accessToken, accessTokenSecret) = args.take(4)
    val filters = args.takeRight(args.length - 4)
    val cb = new ConfigurationBuilder
    cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
      .setOAuthConsumerSecret(consumerSecret)
      .setOAuthAccessToken(accessToken)
      .setOAuthAccessTokenSecret(accessTokenSecret)
    val auth = new OAuthAuthorization(cb.build)
    val tweets_raw = TwitterUtils.createStream(ssc, Some(auth))
    
    //
     val tags = tweets_raw.flatMap { status =>    status.getHashtagEntities.map(_.getText)  }
    
  val cnt =  tags.countByValue().foreachRDD { rdd =>
        
        val now = org.joda.time.DateTime.now()
        rdd .sortBy(_._2).map(x => (x, now))
      }
    
   tweets_raw.saveAsTextFiles("tweets", "json")
  
  
  //
  
  val tweets = tweets_raw.filter {t =>
      val tags = t.getText.split(" ").filter(_.startsWith("#")).map(_.toLowerCase)
      tags.contains("#bigdata") && tags.contains("#food")
    }
  

  
    ssc.start()
    ssc.awaitTermination()
  
  }

  
  
  
  
  
  
  
  
  
  def getStreamContext():StreamingContext={
    
val config = new SparkConf().setMaster("local[3]").setAppName("twitter-stream-sentiment")
val sc = new SparkContext(config)
sc.setLogLevel("WARN")
 
val ssc = new StreamingContext(sc, Seconds(5))

ssc
  }
  
  
  def getSentiment(tweet:String):String ={
     val props = new Properties()
    props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
    val pipeline = new StanfordCoreNLP(props)
    
    var mainSentiment = 0
      if (tweet != null && tweet.length() > 0) {
        var longest = 0
        val annotation = pipeline.process(tweet)
        val list = annotation.get(classOf[CoreAnnotations.SentencesAnnotation])
        val it = list.iterator()
        while (it.hasNext())
        {
          val sentence = it.next()
          val tree = sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree])
          val sentiment = RNNCoreAnnotations.getPredictedClass(tree)
          val partText = sentence.toString()
          if (partText.length() > longest) {
            mainSentiment = sentiment
            longest = partText.length()
          }
        }
  }
    
    if (mainSentiment < 2)
       "Negative"
      else if (mainSentiment == 2)
        "Neutral"
      else
       " Positive"
    
  }
}