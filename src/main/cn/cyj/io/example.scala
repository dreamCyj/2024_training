package cn.cyj.io

import java.io.{BufferedWriter, FileWriter}
import scala.collection.mutable.ListBuffer
import scala.io.Source

object example {

    case class Student(var name: String, var chinese: Int, var math: Int, var english: Int){

        def getTotalScore() = chinese + math + english
    }

    def main(args: Array[String]): Unit = {
        var source = Source.fromFile("./data/student.txt")
        val stuArray = source.getLines().map(_.split("\\s+"))
        val stuList = new ListBuffer[Student]()
        for(stu <- stuArray) {
            stuList += new Student(stu(0), stu(1).toInt, stu(2).toInt, stu(3).toInt)
        }
        //println(stuList)
        val sortList = stuList.sortBy(_.getTotalScore()).reverse.toList
        //println(sortList)
        //           高效的字符输出流      普通字符输出流
        val bw = new BufferedWriter(new FileWriter("./data/stu.txt"))
        bw.write("姓名 语文成绩 数学成绩 英语成绩 总成绩")
        bw.newLine()
        for(s <- sortList) {
            bw.write(s"${s.name} ${s.chinese}     ${s.math}     ${s.english}     ${s.getTotalScore()}")
            bw.newLine()
        }
        bw.close()
        source.close()
    }

}
