package cn.cyj.actor

import scala.actors.Actor
import scala.io.Source

class WordCountActor extends Actor {
    override def act(): Unit = {

        loop {
            react {
                case WordCountTask(fileName) =>
                    val source = Source.fromFile(fileName)
                    //从文件读取
                    val lineList = source.getLines().toList
                    //展开
                    val strList = lineList.flatMap(_.split(" "))
                    //给每个单词加上出现的次数
                    val wordAndCount = strList.map(_ -> 1)
                    //根据单词进行分组
                    val groupMap = wordAndCount.groupBy(_._1)
                    //出现次数求和
                    val wordCountMap = groupMap.map(kv => kv._1 -> kv._2.map(_._2).sum)
                    //返回结果给MainActor
                    sender ! WordCountResult(wordCountMap)

            }
        }
    }
}
