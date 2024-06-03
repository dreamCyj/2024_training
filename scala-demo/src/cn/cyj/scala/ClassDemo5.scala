package cn.cyj.scala

object ClassDemo5 {

    class Person {
      val name = "cyj"
      var age = 23
    }

    class Student extends Person{
      override val name = "jyc"
      def say() = println("hello, cyj")
    }
    def main(args: Array[String]): Unit = {
        val s = new Student
        //println(s.isInstanceOf[Student])
/*        val p: Person = new Student
        if(p.isInstanceOf[Student]){
            val ss = p.asInstanceOf[Student]
            ss.say()
        }*/
        val p: Person = new Student
        println(p.isInstanceOf[Person])
        println(p.isInstanceOf[Student])
        println(p.getClass)
        println(p.getClass == classOf[Person])
    }
}
