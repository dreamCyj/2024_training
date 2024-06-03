package cn.cyj.structure

import scala.collection.mutable.ListBuffer
object ListDemo {

    def main(args: Array[String]): Unit = {
        val list1 = List("cyj1", "cyj2", "cyj3")
        val list2 = Nil
        val list3 = "cyj1" :: 1 :: Nil
        println(list1)
        println(list2)
        println(list3)

        val list = ListBuffer(1, 2, 3, 4)
        //isEmpty判断列表是否为空
        println(list.isEmpty)
        val list4 = ListBuffer(4, 5, 6)
        //++拼接两个列表
        val list5 = list ++ list4
        println(list5)
        //head 取列表首个元素
        println(list5.head)
        //tail 取除去首个的剩下元素
        println(list5.tail)
        //reverse 反转
        println(list5.reverse)
        //take 获取列表前缀元素
        println(list5.take(3))
        //drop 获取列表后缀元素
        println(list5.drop(3))

        val list6 = List(List(1,2), List(2,3), List(4,5,6))
        val list7 = list6.flatten
        println(list7)

        val names = List("cyj1", "cyj2", "cyj3")
        val ages = List(23, 24, 25)
        val list8 = names.zip(ages)
        println(list8)
        val list9 = list8.unzip
        println(list9)

        println(names)
        println(names.mkString("-"))

        println(list.union(list4))

        println(list.intersect(list4))

        println(list.diff(list4))



    }



}
