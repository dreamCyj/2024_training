package cn.cyj.io

object ReadDemo1 {

    def main(args: Array[String]): Unit = {
        //Source.fromFile创建的是一次性流 只能使用一次
/*        val source = Source.fromFile("./data/1.txt", "utf-8")//默认utf-8
        //按行读取
        val list = source.getLines().toList
        for (data <- list) println(data)*/

        //按字符读取
/*        val source = Source.fromFile("./data/1.txt")
        val iter = source.buffered
        while (iter.hasNext){
            print(iter.next())
        }*/

        //文件较小 可以直接
/*        val source = Source.fromFile("./data/1.txt")
        val res = source.mkString
        println(res)*/
        //读取词法单元
/*        val source = Source.fromFile("./data/2.txt")
        val strings = source.mkString.split("\\s+")
        val nums = strings.map(_.toInt)
        for (num <- nums) print(num + " ")*/

        //从URL读取
/*        val source = Source.fromURL("https://www.baidu.com")
        println(source.mkString)
        source.close()*/

        //读取二进制
/*        val file = new File("./data/3.png")
        val fis = new FileInputStream(file)
        val bys = new Array[Byte](file.length().toInt)
        //读取到的存入bys
        val len = fis.read(bys)
        println("读取到的有效字节数：" + len)
        println("字节数组的长度：" + bys.length)
        fis.close()*/
    }

}
