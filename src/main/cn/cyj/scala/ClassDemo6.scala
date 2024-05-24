package cn.cyj.scala

object ClassDemo6 {
    abstract class Shape {
        def area(): Double
    }

    class Square(var edge: Double) extends Shape {
        //子类继承抽象类必须重写所有父类方法(子类也为抽象类则不用)
        override def area(): Double = edge * edge
    }

    class Rectangle(var length: Double, var width: Double) extends Shape {
        override def area(): Double = length * width
    }

    class Circle(var raius: Double) extends Shape {
        override def area(): Double = Math.PI * raius * raius
    }

    def main(args: Array[String]): Unit = {
        val s = new Square(2.0)
        println(s.area())
        val r = new Rectangle(2.0, 4.0)
        println(r.area())
        val c = new Circle(2.0)
        println(c.area())
        new Shape {
            override def area(): Double = 1.0 * 1.0
        }.area()
    }

}
