package cn.cyj.solution

import scala.Option

object ParserSolution {

    private val strRegex = """.+=.+""".r
    private val switchRegex = "^[a-zA-Z][a-zA-Z0-9_]*[a-zA-Z0-9]$".r
    private val configRegex = """(.+)=(.+)*""".r
    private val depListRegex = """\[\d+(,\d+)*]""".r
    private val metaInfoRegex = """(metaInfo\.[a-zA-Z0-9_-]+)""".r
    private case class ConfigParser(){
        def parse(configStr: String): Result = {
            //提取所有键值对
            val configList = strRegex.findAllMatchIn(configStr).map(_.matched).toList
            //println(configList)
            //根据开关名进行分组 并使用switchRegex正则对开关名进行校验
            val switchMap = configList.groupBy(x => {
                val s = x.split("\\.")(0)
                switchRegex.findFirstIn(s) match {
                    case Some(x) => x
                    case None => return Result(None, Some("开关名格式错误"))
                }
            })
            //println(switchMap)
            var map: Map[String, String] = Map.empty
            var resultMap = Map[String, SwitchConfig]()
            //对于每一个开关 正则匹配取出属性名和对应的值 并进行校验
            switchMap.foreach(x => {
                for (config <- x._2) {
                    val configRegex(config1, config2) = config.trim.replaceAll("\\s", "").split("\\.", 2)(1)
                    //使用match将对应属性放入map
                    config1 match {
                        //enabled校验
                        case "enabled" => if(List("true", "false").contains(config2)) map += (config1 -> config2)
                                          else return Result(None, Some(s"开关${x._1}的属性${config1}格式错误只能为true or false"))
                        //depList校验
                        case "depList" => if(config2 != null && depListRegex.findFirstIn(config2).isDefined)
                                                map += (config1 -> config2)
                                          else return Result(None, Some(s"开关${x._1}的属性${config1}格式错误 要是被[]包裹的是数字列表且不能为空"))
                        //正则匹配metaInfo.xyz
                        case metaInfoRegex(config1) => map += (config1 ->config2)
                        //非法属性校验
                        case _ => return Result(None, Some(s"开关${x._1}的配置文件中存在非法属性${config1}"))
                    }
                }
                //设置enabled属性默认值
                if(!map.contains("enabled")) map += "enabled" -> "true"
                for(key <- List("depList")) //"metaInfo.owner", "metaInfo.comment"
                    if(!map.contains(key)) return Result(None, Some(s"开关${x._1}的属性${key}不存在"))
                resultMap += x._1 -> SwitchConfig(x._1, map("enabled").toBoolean, map("depList").stripPrefix("[").stripSuffix("]").split(",").toList.map(_.trim.toInt),
                    map.filterKeys(key => metaInfoRegex.pattern.matcher(key).matches()))
                map = map.empty
            })
            //结果在resultMap中 封装成Result返回
            Result(Some(resultMap), None)
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
        val configStr: String = "switchA.depList = [1, 2, 3]\nswitchA.metaInfo.cyj = \"userA\"\nswitchA.metaInfo.comment = \"hello world\"\n    \nswitchB.enabled = false\nswitchB.depList = [3, 4, 5]\nswitchB.metaInfo.owner = \"userB\"\n\nswitchB.metaInfo.comment = \"hello 1world\"" // 一段配置内容
        val parser = ConfigParser()
        val result = parser.parse(configStr)
        //println(result)
        val resultMap = result.data.get
        println(resultMap)
        val configString = ConfigParser.stringify(resultMap)
        println(configString)
    }
}
