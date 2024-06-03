package cn.cyj.structure

object IteratorDemo {

    def main(args: Array[String]): Unit = {
        val list = List(1, 2, 3, 4, 5)
        val it  = list.iterator
        while(it.hasNext){  //判断迭代器中是否有下一元素
            println(it.next())  //返回下一元素
        }
    }
}
