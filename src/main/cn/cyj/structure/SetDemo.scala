package cn.cyj.structure
import scala.collection.mutable.Set
object SetDemo {
    def main(args: Array[String]): Unit = {
        val set1 = Set(1, 1, 3, 5, 8, 2)
        println(set1)
        println(set1.size)
        val set2 = set1 - 3
        println(set2)
        val set3 = set2 ++ Set(7, 3, 8)
        println(set3)
        val set4 = set3 ++ List(9, 10)
        println(set4)
        set4.remove(9)
        println(set4)
    }

}
