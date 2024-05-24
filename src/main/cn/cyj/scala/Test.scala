import java.util.Scanner
import scala.io.StdIn
import scala.util.control.Breaks._
object Test {

  def main(args: Array[String]): Unit = {
    println("hello world")
    //java方式接收键盘输入
    println("我是" + new Scanner(System.in).nextLine())
    //scala方式接受键盘输入
    val str = StdIn.readLine()
    println("我是" + str)

    //for循环
    for(i <- 1 to 5){
      for(j <- 1 to 5){
        print(".")
      }
    }
    for(i <- 1 to 5; j <- 2 to 4) print("*")

    for(i <- 1 to 10 if i % 2 == 0) print(" " + i)

    val v = for (i <- 1 to 10) yield i * 10
    println(v)

    //实现break
    breakable{
      for(i <- 1 to 10)
        if(i == 5) break() else println(i)
    }
    //实现continue
    for(i <- 1 to 8){
      breakable{
        if (i == 5) break() else println(i)
      }
    }

    //九九乘法表
    for(i <- 1 to 9){
      for ( j <- 1 to i){
        print(s"${j} * ${i} = ${i * j}\t")
      }
      println()
    }

    for(i <- 1 to 9;  j <- 1 to i){
      print(s"${j} * ${i} = ${i * j}\t")
      if(i == j) println()
    }

    //模拟登录
    breakable{
      for( i<- 1 to 3) {
        println("请输入账号:")
        val account = StdIn.readLine()
        println("请输入密码:")
        val password = StdIn.readLine()
        if(account == "cyj" && password == "123"){
          println("登陆成功")
          break()
        }else{
            if(i == 3)  println("账号被锁定")
            else println(s"用户名或密码错误，还有${3-i}次机会")
        }
      }
    }

    def func(n: Int): Int = if(n == 1) 1 else n * func(n - 1)
    println(func(5))

    def sum1(a: Int = 10, b: Int = 20): Int = a + b
    println(sum1())

    def sum2(nums: Int *) = nums.sum
    println(sum2(1, 2, 3))

    //方法调用
    //后缀
    println(Math.abs(-1))
    //中缀
    println(Math abs -1)
    //花括号 方法只有一个参数时
    println(Math.abs{-1})
    //无括号
    def say() = println("cyj")
    say
    val getSum = (x: Int, y: Int) => x + y
    println(getSum(1, 2))

    val a = sum1 _
    println(a(1,2))
    //方法
    def nn1(n: Int): Unit = {
      for (i <- 1 to n; j <- 1 to i) {
        print(s"${j} * ${i} = ${i * j}\t")
        if (i == j) println()
      }
    }

    //函数
    val nn2 = (n: Int) => {
      for (i <- 1 to n; j <- 1 to i) {
        print(s"${j} * ${i} = ${i * j}\t")
        if (i == j) println()
      }
    }

    val num = StdIn.readInt()
    nn1(num)
    nn2(num)

  }
}

