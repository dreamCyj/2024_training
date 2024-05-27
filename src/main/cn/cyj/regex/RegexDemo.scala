package cn.cyj.regex

object RegexDemo {

    val email = "2677575051@qq.com"

    val regex = """.+@.+\..+""".r


    def main(args: Array[String]): Unit = {
        if(regex.findAllMatchIn(email).nonEmpty){
            println(s"${email}是合法邮箱")
        }
        else {
            println(s"${email}是非法邮箱")
        }

        val emailList = List("2677575051@qq.com", "chenyj010110@gamil.com", "adljaodaoi.com")
        //过滤出非法邮箱
        val illegalEmailList = emailList.filter(regex.findAllMatchIn(_).isEmpty)
        println(illegalEmailList)

        val regex1 = """.+@(.+)\..+""".r
        val list2 = emailList.map {
            case x @ regex1(company) => x -> company
            case x => x -> "未匹配"
        }
        println(list2)

    }
}
