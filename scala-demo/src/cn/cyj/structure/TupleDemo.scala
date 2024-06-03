package cn.cyj.structure

object TupleDemo {

    def main(args: Array[String]): Unit = {
        val tuple = ("cyj1", "cyj2", "cyj3")
        println(tuple._1)

        val it = tuple.productIterator
        for(i <- it) println(i)
    }
}
