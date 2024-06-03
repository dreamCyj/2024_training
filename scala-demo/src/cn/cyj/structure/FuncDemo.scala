package cn.cyj.structure

object FuncDemo {

    def main(args: Array[String]): Unit = {
/*        //foreach
        val list1 = List("cyj1", "cyj2", "cyj3")
        list1.foreach((x: String) => {println(x)})
        println("-" * 10)
        list1.foreach(x => println(x))
        println("-" * 10)
        list1.foreach(println(_))
        //map
        val list2 = List(1, 2, 3, 4)
        //val list3 = list2.map((x: Int) => { "*" * x})
        val list3 = list2.map("*" * _)
        println(list3)*/
        //flatMap
/*        val list3 = List("xiaomi huawei oppo", "car phone pc")
        //1.先map再flatten
        val list4 = list3.map(x => x.split(" "))
        val list5 = list4.flatten
        println(list5)
        //2.flatMap
        val list6 = list3.flatMap(_.split(" "))
        println(list6)*/
        //filter
        val list7 = (1 to 9).toList
        val list8 = list7.filter(_ % 2 == 0)
        println(list8)
        //sort
        //sorted
        val list9 = List(3, 1, 2, 9, 7)
        val list10 = list9.sorted
        println(list10)
        val list11 = list10.reverse
        println(list11)
        //sortBy
        val list12 = List("01 xiaomi", "02 huawei", "03 oppo")
        val list13 = list12.sortBy(x => x.split(" ")(1))
        println(list13)
        //sortWith
        //val list14 = list9.sortWith((x, y) => x > y)
        val list14 = list9.sortWith(_ > _)
        println(list14)

        //groupBy
        val list15 = List("刘德华" -> "男", "刘亦菲" -> "女", "彭于晏" -> "男")
        //按照性别分组
        //val map1 = list15.groupBy(x => x._2)
        val map1 = list15.groupBy(_._2)
        println(map1)
        //统计每个性别的人数
        val map2 = map1.map(x => x._1 -> x._2.length)
        println(map2)

        //val list16 = list7.reduce((x, y) => (x + y))
        val list16 = list7.reduce(_ + _)
        println(list16)

        val list17 = list7.fold(100)(_ + _)
        println(list17)
    }
}
