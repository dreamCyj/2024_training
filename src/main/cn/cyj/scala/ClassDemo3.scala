package cn.cyj.scala

object ClassDemo3 {

/*  //定义伴生类
  class Generals { //非静态
    def beat(): Unit = println(s"我手里拿着${Generals.weapon}")
  }
  //定义伴生对象
  object Generals { //静态
    private val weapon = "流星弯月刀"
  }

  def main(args: Array[String]): Unit = {
    //创建伴生类对象
    val g = new Generals
    g.beat()
  }*/

  //1.定义Person类
  class Person(var name: String = "", var age: Int = 0){

  }

  //2.定义类的伴生对象
  object Person{
    //3.在其中定义apply()方法，用于创建对象时免new
    def apply(name: String, age: Int) = new Person(name, age)
  }

  def main(args: Array[String]): Unit = {
    val p = Person("cyj", 23)
    println(p.name, p.age)
  }
}
