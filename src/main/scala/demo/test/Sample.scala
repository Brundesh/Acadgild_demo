package demo.test

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import scala.collection.mutable.ListBuffer

import java.io.{File, FileInputStream}
import scala.beans.BeanProperty
import scala.io.Source

object Sample {
    def main(args: Array[String]) {
      
     val i =  getClass.getResourceAsStream("/sample.yml")
      val lines = Source.fromInputStream(i).getLines
    lines.foreach(line => println(line))
     println(i)
        val filename = "/src/main/resources/sample.yml"
        val input = new FileInputStream(new File(filename))
        val yaml = new Yaml(new Constructor(classOf[EmailAccount]))
        val e = yaml.load(input).asInstanceOf[EmailAccount]
        println(e.usersOfInterest)
    }
}

/**
* With the Snakeyaml Constructor approach shown in the main method,
* this class must have a no-args constructor.
*/
class EmailAccount {
    @BeanProperty var accountName = ""
    @BeanProperty var username = ""
    @BeanProperty var password = ""
    @BeanProperty var mailbox = ""
    @BeanProperty var imapServerUrl = ""
    @BeanProperty var protocol = ""
    @BeanProperty var minutesBetweenChecks = 0
    @BeanProperty var usersOfInterest = new java.util.ArrayList[String]()
    override def toString: String = s"acct: $accountName, user: $username, url: $imapServerUrl"
}

class R{
   @BeanProperty var rules = new java.util.ArrayList[String]()
   override def toString: String = s"rules: $rules"
  
}

class Rules1{
  @BeanProperty var accountName = ""
  @BeanProperty var usersOfInterest = new java.util.ArrayList[String]()
  override def toString: String = s"acct: $accountName,  usersOfInterest: $usersOfInterest"
}