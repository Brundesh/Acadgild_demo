package demo.test


import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.TypeDescription
import scala.io.Source
import java.io.FileInputStream
import java.io.File
import scala.beans.BeanProperty


class Rules(){
  
  @BeanProperty val rules = new java.util.ArrayList[String]()
}









object Rules {
  def main(args: Array[String]): Unit = {
    val path = "C:/Users/brunr/Documents/HRS/rule.yaml"
   val input = new FileInputStream(new File(path))
 val yaml = new Yaml(new Constructor(classOf[Rules]))
        val e = yaml.load(input).asInstanceOf[Rules]
        println(e.rules)

  }
}