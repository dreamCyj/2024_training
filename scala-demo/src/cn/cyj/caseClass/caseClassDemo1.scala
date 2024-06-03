package cn.cyj.caseClass

object caseClassDemo1 {

    case class Person(name: String, var age: Int) {}

    def main(args: Array[String]): Unit = {
        //apply
        val p = Person("cyj", 23)
        //toString
        println(p)
        //equals
        val p1 = Person("cyj", 23)
        val p2 = Person("cyj", 24)
        println(p == p1)
        println(p == p2)
        //hashCode
        println(p.hashCode())
        //copy()
        val p3 = p.copy()
        println(p3)
        val p4 = p.copy(name = "cyj2")
        println(p4)
    }
}
