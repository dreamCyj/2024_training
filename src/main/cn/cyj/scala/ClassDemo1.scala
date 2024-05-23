package cn.cyj.scala

object ClassDemo1 {

  //定义Customer类，在主构造器中定义姓名和地址
  class Customer(val name:String, val address: String){
    //定义辅助构造器 接收数组类型的参数化
    def this(arr: Array[String]) { //这个this为辅助构造器名称
      //第一行代码必须访问主构造器或其他辅助构造器
      this(arr(0), arr(1))  //这个this为Customer
    }
  }

  object Human {
    val hand_num = 2
    val leg_num = 2
    def sayHello() = println("hello world")
  }

  def main(args: Array[String]): Unit = {
    //通过辅助构造器，创建Customer类的对象
/*    val c = new Customer(Array("cyj", "wuhan"))
    println(c.name, c.address)*/

    println(Human.leg_num)
    Human.sayHello()
  }

}
