package demo.test

class Reflect_demo {
  def rt(){}
  def ty(){}
}

object Reflect_demo{
  
  def main(args: Array[String]): Unit = {
    val f = Class.forName("Reflect_demo").getMethods
    
    println(f)
    
  }
}