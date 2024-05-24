package cn.cyj.traitdemo

object Trait1 {
    trait logger1 {
        def log1(msg: String)
    }
    trait logger2 {
        def log2(msg: String)
    }
    //类继承单特质
    class ConsoleLogger1 extends logger1 {
        override def log1(msg: String): Unit = println(msg)
    }

    //类继承多特质
    class ConsoleLogger2 extends logger1 with logger2 {
        override def log1(msg: String): Unit = println(msg)

        override def log2(msg: String): Unit = println(msg)
    }
    def main(args: Array[String]): Unit = {
        val consoleLogger1 = new ConsoleLogger1
        consoleLogger1.log1("cyj1")
        val consoleLogger2 = new ConsoleLogger2
        consoleLogger2.log1("cyj2")
        consoleLogger2.log2("cyj2")
    }
}
