package cn.cyj.io

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

object WriteDemo1 {

    //序列化和反序列化必须继承Serializable
    class Person(var name: String, var age: Int) extends Serializable
    def main(args: Array[String]): Unit = {
        //写入文件
/*        val fos = new FileOutputStream("./data/4.txt")
        fos.write("买车就买小米su7\n".getBytes)
        fos.write("买手机就买小米14".getBytes())
        fos.close()*/

        //序列化：将对象写到文件中
        val p = new Person("cyj", 23)
        val oos = new ObjectOutputStream(new FileOutputStream("./data/5.txt"))
        oos.writeObject(p)
        oos.close()
        //反序列化：从文件中加载对象
        val ois = new ObjectInputStream(new FileInputStream("./data/5.txt"))
        val p1 = ois.readObject().asInstanceOf[Person]
        println(p1.name, p1.age)
        ois.close()
    }
}
