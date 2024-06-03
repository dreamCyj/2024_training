package cn.cyj.structure

import scala.collection.mutable
import scala.collection.mutable.Map
object MapDemo {
    def main(args: Array[String]): Unit = {
        val map1 = Map("cyj1" -> 23, "cyj2" -> 24, "cyj2" -> 25)
        val map2 = mutable.Map(("cyj1", 23), ("cyj2", 24), ("cyj2", 25))

        println(map1("cyj1"))
        map1("cyj1") = 99
        println(map1("cyj1"))

        println(map1.keys)
        println(map2.values)
        for((k, v) <- map1) println(k, v)

        println(map1.getOrElse("陈月佳", -1))

    }



}
