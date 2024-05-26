package cn.cyj.matchOption

object OptionDemo {
    def divide(a: Int, b:Int): Option[Int] = {
        if(b == 0)
            None
        else
            Some(a/b)
    }

    def main(args: Array[String]): Unit = {
        //法1
        val res = divide(1, 0)
        println(res)

        //法2 模式匹配
        res match {
            case Some(x) => println(s"商为${x}")
            case None => println("除数不能为0")
        }
        //法3 getOrElse
        println(res.getOrElse("除数不能为0"))
    }
}
