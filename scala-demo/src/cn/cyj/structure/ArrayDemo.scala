package cn.cyj.structure

import scala.collection.mutable.ArrayBuffer
object ArrayDemo {
    //定长数组
    val array = new Array[String](5)
    val array2 = Array("cyj1", "cyj2")

    //变长数组
    val array3 = new ArrayBuffer[String]()
    val array4 = ArrayBuffer("cyj3", "cyj4")

    def main(args: Array[String]): Unit = {
/*        array(0) = "scala"
        println(array(0))
        println(array2.length)
        println(array3.length)
        println(array4(0))*/
/*        array3 += "scala"
        println(array3)
        array3 += "xiaomi"
        println(array3)
        array3 -= "xiaomi"
        println(array3)
        array3 ++= Array("cyj1", "cyj2")
        println(array3)
        array3 --= Array("cyj1", "cyj2")
        println(array3)*/
        //遍历
        array3 ++= Array("cyj1", "cyj2", "cyj3")
        for(i <- 0 until array3.length) println(array3(i))
        for(i <- array3) println(i)

    }
}
