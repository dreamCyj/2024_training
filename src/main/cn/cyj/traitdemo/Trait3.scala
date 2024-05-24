package cn.cyj.traitdemo

object Trait3 {
    abstract class Template {
        //代码
        def code()
        //计算代码运行时间
        def getRuntime() = {
            val start = System.currentTimeMillis()
            code()
            val end = System.currentTimeMillis()
            end - start
        }
    }

    class Cal extends Template {
        override def code(): Unit = for (i <- 1 to 10000) println("cyj")
    }

    def main(args: Array[String]): Unit = {
        val c = new Cal
        println(c.getRuntime())
    }
}
