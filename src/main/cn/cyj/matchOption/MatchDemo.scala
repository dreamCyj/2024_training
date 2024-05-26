package cn.cyj.matchOption
import scala.io.StdIn
object MatchDemo {

    def main(args: Array[String]): Unit = {
        //简单匹配
        println("请输入要翻译的单词")
        val word = StdIn.readLine()
        word match {
            case "student" => println("学生")
            case "teacher" => println("老师")
            case "man" => println("男人")
            case "woman" => println("男人")
            case _ => println("出错了")
        }
        //类型匹配
        val t:Any = 5.20
        t match {
            case x: String => println(s"${x}是String类型的")
            case x: Int => println(s"${x}是Int类型的")
            case _: Double => println("是Double类型的")
            case _ => println("未匹配")
        }
        //守卫
        println("请输入一个数")
        val num = StdIn.readInt()
        num match {
            case x if x < 0 => println("负数")
            case x if x > 0 => println("正数")
        }
        //样例类匹配
        case class Customer(name: String, age: Int)
        case class Order(id: Int)
        val customer: Any = Customer("cyj", 23)
        //val c: Any = Order(1)
        customer match {
            case Customer(a, b) => println(s"Customer类型对象, name = ${a}, age = ${b}")
            case Order(a) => println(s"Order类型对象, id = ${a}")
            case _ => println("未匹配")
        }
        //集合类匹配
        val arr1 = Array(1, 2, 4)
        val arr2 = Array(0)
        val arr3 = Array(0, 1, 3, 8)

        arr3 match {
            case Array(1, x, y) => println("共三个元素 1开头 剩下两个任意")
            case Array(0) => println("只有元素0")
            case Array(0, _*) => println("0开头 任意多个任意元素")
            case _ => println("未匹配")
        }
        //获取数据
        val arr4 = (1 to 10).toArray
        val Array(_, x, y, z, _*) = arr4
        println(x, y, z)

        val list1 = (1 to 10).toList
        val List(a, b, _*) = list1
        println(a, b)
        val c :: d :: tail = list1
        println(c, d)

        val map = Map("张三" -> 23, "李四" -> 24, "王五" -> 23)
        //获取年龄为23的
        //法1
        for((k, v) <- map if v == 23) println(s"${k}=${v}")
        //法2
        for((k, 23) <- map) println(k + "=23")

    }

}
