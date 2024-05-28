package cn.cyj.solution

import scala.Option

object ParserSolution {

    private val strRegex = """.+=.+""".r
    private val switchRegex = "^[a-zA-Z][a-zA-Z0-9_]*[a-zA-Z0-9]$".r
    private val depListRegex = """\[\s*\d+(\s*,\s*\d+\s*)*]""".r
    private val metaInfoRegex = """(metaInfo\.[a-zA-Z0-9_-]+)""".r
    private case class ConfigParser(){
        def parse(configStr: String): Result = {
            //提取所有键值对
            val configList = strRegex.findAllMatchIn(configStr).map(_.matched).toList
            //println(configList)
            //根据开关名进行分组 并使用switchRegex正则对开关名进行校验
            val switchMap = configList.groupBy(config => {
                val switchName = config.split("\\.")(0)
                switchRegex.findFirstIn(switchName) match {
                    case Some(config) => config
                    case None => return Result(None, Some(s"开关名${switchName}格式错误"))
                }
            })
            //println(switchMap)
            var map: Map[String, String] = Map.empty
            var resultMap = Map[String, SwitchConfig]()
            //对于每一个开关 正则匹配取出属性名和对应的值 并进行校验
            switchMap.foreach(switchConfig => {
                for (config <- switchConfig._2) {
                    val (propName, propValue) = parseConfig(config)
                    //val configRegex(config1, config2) = config.trim.replaceAll("\\s", "").split("\\.", 2)(1) bug:属性值如"hello world"中的空格也去除了
                    //使用match将对应属性放入map
                    propName match {
                        //enabled校验
                        case "enabled" => if(List("true", "false").contains(propValue)) map += (propName -> propValue)
                                          else return Result(None, Some(s"开关${switchConfig._1}的属性${propName}格式错误只能为true or false"))
                        //depList校验
                        case "depList" => if(propValue != null && depListRegex.findFirstIn(propValue).isDefined)
                                                map += (propName -> propValue)
                                          else return Result(None, Some(s"开关${switchConfig._1}的属性${propName}格式错误 要是被[]包裹的是数字列表且不能为空"))
                        //正则匹配metaInfo.xyz
                        case metaInfoRegex(propName) => map += (propName -> propValue)
                        //非法属性校验
                        case _ => return Result(None, Some(s"开关${switchConfig._1}的配置文件中存在非法属性${propName}"))
                    }
                }
                //设置enabled属性默认值
                if(!map.contains("enabled")) map += "enabled" -> "true"
                for(key <- List("depList")) //"metaInfo.owner", "metaInfo.comment"
                    if(!map.contains(key)) return Result(None, Some(s"开关${switchConfig._1}的属性${key}不存在"))
                resultMap += switchConfig._1 -> SwitchConfig(switchConfig._1, map("enabled").toBoolean, map("depList").stripPrefix("[").stripSuffix("]").split(",").toList.map(_.trim.toInt),
                    map.filterKeys(key => metaInfoRegex.pattern.matcher(key).matches()))
                map = map.empty
            })
            //结果在resultMap中 封装成Result返回
            Result(Some(resultMap), None)
        }
        private var map: Map[String, SwitchConfig] = Map.empty
        private var result = Result(Some(map), Some(""))
        private var switchConfig = SwitchConfig("", enabled = true, List(), Map())
        private var metaInfoMap = Map[String, String]()
        def parseLine(line: String): Unit = {
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
                                        metaInfoMap += (switchName + "." + propertyName) -> propValue
                                        switchConfig = switchConfig.copy(metaInfo = metaInfoMap.filterKeys(_.startsWith(switchName)))
                                        map += switchName -> switchConfig
                                        result = result.copy(data = Some(map))
                //非法属性校验
                case _ => result = result.copy(None, Some(s"开关${switchName}的配置文件中存在非法属性${propName}"))
            }
        }
        // 获得解析结果
        def getResult: Result = {
            result
        }

        private def parseConfig(config: String): (String, String) = {
            val propName = config.split("=")(0).split("\\.",2)(1).trim
            val propValue = config.split("=")(1).trim
            (propName, propValue)
        }
    }

    private object ConfigParser  {
        def stringify(map: Map[String, SwitchConfig]): String = {
            var configString = ""
            map.foreach(switchConfig => {
                configString += (s"${switchConfig._1}.enabled = ${switchConfig._2.enabled}\n${switchConfig._1}.depList = ${switchConfig._2.depList}\n"
                + s"${(for ((key, value) <- switchConfig._2.metaInfo) yield s"${switchConfig._1}.${key} = ${value}").mkString("\n")}\n")
            })
            configString
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
        val configStr: String = "switchA.depList = [1, 2, 3]\nswitchA.metaInfo.cyj = \"userA\"\nswitchA.metaInfo.comment = \"hello world\"\n    \nswitchB.enabled = true\nswitchB.depList = [   3,    4   ,    6]\nswitchB.metaInfo.owner = \"cyj\"\n\nswitchB.metaInfo.comment = \"hello           1world\"" // 一段配置内容
        val parser = ConfigParser()
        val result = parser.parse(configStr)
        println(result)
        val resultMap = result.data.get
        val configString = ConfigParser.stringify(resultMap)
        println(configString)
        parser.parseLine("switchA.depList = [ 1 , 2 , 3]")
        parser.parseLine("switchA.enabled = true")
        parser.parseLine("switchA.metaInfo.owner = \"userA\"")

        parser.parseLine("switchB.enabled = false")
        parser.parseLine("switchB.metaInfo.comment = \"hello 2world\"")
        parser.parseLine("switchA.metaInfo.comment = \"hello 333world\"")
        val res: Result = parser.getResult
        println(res)
    }
}
