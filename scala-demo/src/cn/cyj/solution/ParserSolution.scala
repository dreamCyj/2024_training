package cn.cyj.solution

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.control.Breaks.{break, breakable}

object ParserSolution {

    private val strRegex = """.+=.+""".r  //提取键值对正则
    private val switchRegex = "^[a-zA-Z][a-zA-Z0-9_]*[a-zA-Z0-9]$".r  //开关名正则
    private val depListRegex = """\[\s*\d+(\s*,\s*\d+\s*)*]""".r  //depList格式正则
    private val metaInfoRegex = """(metaInfo\.[a-zA-Z0-9_-]+)""".r  //metaInfo.xyz正则
    private case class ConfigParser(){

        private var map: Map[String, SwitchConfig] = Map.empty
        private var result = Result(Some(map), None)
        private var switchConfig = SwitchConfig("", enabled = true, List(), Map())
        private var metaInfoMap = Map[String, Map[String, String]]()

        //从配置字符串中提取出第一个键值对
        private def extractOneKV(configStr: String): Option[(String, String)] = {
            strRegex.findFirstMatchIn(configStr) match {
                case Some(config) =>
                    val keyValue = config.matched
                    val switchName = keyValue.split("\\.")(0)
                    if(switchRegex.findFirstIn(switchName).isEmpty) {
                         result = result.copy(None, Some(s"开关名${switchName}格式错误"))
                         break()
                    }
                    // 获取剩余字符串
                    val remaining = configStr.substring(config.end).trim
                    Some((keyValue, remaining))
                case None => None
            }
        }
        def parse(configStr: String): Result = {
            var remaining = configStr
            breakable{
                while (remaining.nonEmpty) {
                    extractOneKV(remaining) match {
                        case Some((kv, rem)) =>
                            //解析每个kv的结果
                            val res = parseLine(kv)
                            //解析出现错误 则break 直接返回result
                            if(res.error.isDefined) {
                                //只要有出错 后续便不再执行
                                break()
                            }
                            remaining = rem
                        case None => break
                    }
                }
                //字符串中的所有kv解析完毕
                result.data.get.foreach(switchConfig => if(switchConfig._2.depList.isEmpty) {
                    result = result.copy(None, Some(s"开关${switchConfig._1}的属性depList为空列表"));break()
                    }
                )
            }
            result
        }

        def parseLine(line: String): Result = {
            val switchName = line.split("=")(0).split("\\.",2)(0)
            val (propName, propValue) = parseConfig(line)
            switchConfig = map.getOrElse(switchName, SwitchConfig("", enabled = true, List(), Map()))
            switchConfig = switchConfig.copy(switch = switchName)
            propName match {
                //enabled校验
                case "enabled" => if(List("true", "false").contains(propValue))  {
                    switchConfig = switchConfig.copy(enabled = propValue.toBoolean)
                    map += switchName -> switchConfig
                    result = result.copy(data = Some(map))
                }
                else result = result.copy(None, Some(s"开关${switchName}的属性${propName}格式错误只能为true or false"))
                //depList校验
                case "depList" => if(propValue != null && depListRegex.findFirstIn(propValue).isDefined) {
                    switchConfig = switchConfig.copy(depList = propValue.stripPrefix("[").stripSuffix("]").split(",").toList.map(_.trim.toInt))
                    map += switchName -> switchConfig
                    result = result.copy(data = Some(map))
                }
                else result = result.copy(None, Some(s"开关${switchName}的属性${propName}格式错误 要是被[]包裹的是数字列表且不能为空"))
                //正则匹配metaInfo.xyz
                case metaInfoRegex(propertyName) =>
                    //从metaInfoMap拿到当前开关的metaInfo.xyz
                    var newMap = metaInfoMap.getOrElse(switchName, Map.empty)
                    //更新metaInfo.xyz
                    newMap += propertyName -> propValue
                    metaInfoMap += switchName -> newMap
                    //更新对应的SwitchConfig
                    switchConfig = switchConfig.copy(metaInfo = newMap)
                    map += switchName -> switchConfig
                    //更新Result
                    result = result.copy(data = Some(map))
                //非法属性校验
                case _ => result = result.copy(None, Some(s"开关${switchName}的配置文件中存在非法属性${propName}"))
            }
            getResult
        }
        // 获得解析结果
        def getResult: Result = {
            result
        }

        // 并行解析多份配置文本，并返回结果列表
        def parseAll(configStrList: List[String]): Future[List[Result]] = {
            val parseRes = configStrList.map { configStr =>
                Future {
                    parse(configStr)
                }
            }
            //组合所有的Future
            Future.sequence(parseRes)
        }
        private def parseConfig(config: String): (String, String) = {
            val propName = config.split("=")(0).split("\\.",2)(1).trim
            val propValue = config.split("=")(1).trim
            (propName, propValue)
        }
    }

    private object ConfigParser  {
        def stringify(map: Map[String, SwitchConfig]): String = {
            var configStr = ""
            map.foreach { switchAndConfig =>
                val switchName = switchAndConfig._1
                val switchConfig = switchAndConfig._2
                val str = s"${switchName}.enabled = ${switchConfig.enabled}\n${switchName}.depList = ${switchConfig.depList}\n"
                val metaInfoStr = (for((key, value) <- switchConfig.metaInfo) yield s"${switchName}.${key} = ${value}").mkString("\n")
                configStr = s"$configStr$str$metaInfoStr\n"
            }
            configStr
        }
    }
    private case class Result(
                               data: Option[Map[String, SwitchConfig]],
                               error: Option[String]
                             )
    private case class SwitchConfig(
                                     switch: String, // 开关的名称
                                     enabled: Boolean = true, // 是否激活. 默认为 true
                                     depList: List[Int], // 依赖的模块 id 列表. 如果为空，需要报错。
                                     metaInfo: Map[String, String] // 一些元数据。默认为空 Map
                                   )

    def main(args: Array[String]): Unit = {
        val configStr = "switchA.enabled = false\nswitchA.metaInfo.cyj = \"userA\"\nswitchA.metaInfo.comment = \"hello world\"\n    \nswitchB.enabled = true\nswitchB.depList = [   3,    4   , 5]\nswitchB.metaInfo.owner = \"cyj\"\n\nswitchB.metaInfo.comment = \"hello           1world\"\nswitchA.depList = [ 1,2,3]\n" // 一段配置内容
        val parser = ConfigParser()
        val result = parser.parse(configStr)
        println(s"基础题解: \n${result}\n")
/*        val resultMap = result.data.get
        val configString = ConfigParser.stringify(resultMap)
        println(s"bonus1：\n${configString}")

        //val parser = ConfigParser()
        parser.parseLine("switchA.depList = [ 1 ,10, 3]")
        parser.parseLine("switchA.enabled = false")
        parser.parseLine("switchA.metaInfo.owner = \"userA\"")
        parser.parseLine("switchB.enabled = false")
        parser.parseLine("switchB.metaInfo.comment = \"hello 2world\"")
        parser.parseLine("switchA.metaInfo.comment = \"hello 333world\"")
        val res: Result = parser.getResult
        println(s"bonus2：\n${res}\n")*/

/*        val parser = ConfigParser()
        val configStrList = (1 to 500).map(i => s"switch${i}.depList = [  9,10,98]").toList
        println("bonus3: ")
        val start = System.nanoTime()
        val results = parser.parseAll(configStrList)
        results.onComplete{
            case Success(results) =>
            //results.foreach(println)
        }
        val end = System.nanoTime()
        println(s"parseAll时间${(end - start).toDouble / 1e6}")
        //println(results)
        val start1 = System.nanoTime()
        val resultList = configStrList.map(parser.parse)
        val end1 = System.nanoTime()
        println(s"parse时间${(end1 - start1).toDouble / 1e6}")*/
        //println(resultList)
    }
}