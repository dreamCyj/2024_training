package cn.cyj.scala

import java.text.SimpleDateFormat
import java.util.Date

object ClassDemo4 {

    //1.定义工具类DateUtils
    object DateUtils{
      //2.定义SimpleDateFormat对象
      var sdf: SimpleDateFormat = null

      //3.定义date2String方法
      def date2String(date: Date, template: String) = {
        sdf = new SimpleDateFormat(template)
        sdf.format(date)
      }
      //4.定义string2Date方法
      def string2Date(dateString: String, template: String) = {
        sdf = new SimpleDateFormat(template)
        sdf.parse(dateString)
      }
    }

    def main(args: Array[String]): Unit = {
      //5.测试date2String方法
      println(DateUtils.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"))
      //6.测试string2Date方法
      println(DateUtils.string2Date("2024年5月23日", "yyyy年MM月dd日"))
    }

}
