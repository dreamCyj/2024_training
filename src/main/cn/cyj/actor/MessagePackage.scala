package cn.cyj.actor

/**
 * MessagePackage用来记录所有样例类
 */

/**
 * MainActor给每一个WordCountActor发送的消息
 * @param fileName
 */
case class WordCountTask(fileName: String)

/**
 * 每个WordCountActor返回的结果
 * @param wordCountMap
 */
case class WordCountResult(wordCountMap: Map[String, Int])
