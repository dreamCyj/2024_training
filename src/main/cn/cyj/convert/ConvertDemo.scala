package cn.cyj.convert

import java.io.File
import scala.io.Source
import scala.language.implicitConversions

object ConvertDemo {

    private class RichFile(file: File) {
        def read(): String = Source.fromFile(file).mkString
    }

    private object ImplicitDemo {
        //自动调用
        implicit def file2RichFile(file: File): RichFile = new RichFile(file)
    }
    def main(args: Array[String]): Unit = {

        //手动导入 隐式转换
        import ImplicitDemo.file2RichFile
        val file = new File("./data/1.txt")
        println(file.read())
    }

}
