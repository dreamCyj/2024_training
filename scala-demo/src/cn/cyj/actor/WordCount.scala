package cn.cyj.actor

import java.io.File

object WordCount {

    def main(args: Array[String]): Unit = {

        val dir  = "./data/"
        //WordCount1.txt  WordCount2.txt
        val fileNameList = new File(dir).list().filter(_.startsWith("WordCount")).toList
        // ./data/WordCount1.txt, ./data/WordCount2.txt
        val fileDirList = fileNameList.map(dir + _)
        val wordCountActors = fileNameList.map(_ => new WordCountActor)
        //将wordCountActors与txt对应
        val actorWithFile = wordCountActors.zip(fileDirList)

        //futureList存放所有Actor统计的结果
        val futureList = actorWithFile.map { keyVal =>
            val actor = keyVal._1
            actor.start()
            val future = actor !! WordCountTask(keyVal._2)
            future
        }

        //futureList中存在!isSet 就是存在还没有接收到返回的
        while (futureList.exists(!_.isSet)) {}
        //Any 转WordCountResult 取到里面的wordCountMap
        val wordCountMap = futureList.map(_.apply().asInstanceOf[WordCountResult].wordCountMap)
        println(wordCountMap)
        val result = wordCountMap.flatten.groupBy(_._1).map(kv => kv._1 -> kv._2.map(_._2).sum)
        println(result)
    }

}
