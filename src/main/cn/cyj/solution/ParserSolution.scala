package cn.cyj.solution

object ParserSolution {

    private val strRegex = """.+=.+""".r
    private val switchRegex = "^[a-zA-Z][a-zA-Z0-9_]*[a-zA-Z0-9]$".r
    private val configRegex = """(.+)=(.+)""".r
    private case class ConfigParser(){
        def parse(configStr: String): Result = {
            val configList = strRegex.findAllMatchIn(configStr).map(_.matched).toList
            val switchMap = configList.groupBy(x => {
                val s = x.split("\\.")(0)
                switchRegex.findFirstIn(s) match {
                    case Some(x) => x
                    case None => return Result(None, Some("开关名格式错误"))
                }
            })
            var map: Map[String, String] = Map.empty
            var resultMap = Map[String, SwitchConfig]()
            switchMap.foreach(x => {
                for (config <- x._2) {
                    val configRegex(config1, config2) = config.trim.replaceAll("\\s", "").split("\\.", 2)(1)
                    config1 match {
                        case "enabled" => map += ("enabled" -> config2)
                        case "depList" => map += ("depList" -> config2)
                        case "metaInfo.owner" => map += ("metaInfo.owner" -> config2)
                        case "metaInfo.comment" => map += ("metaInfo.comment" -> config2)
                    }
                }

                for(key <- List("enabled", "depList", "metaInfo.owner", "metaInfo.comment"))
                    if(!map.contains(key)) return Result(None, Some(s"开关${x._1}的属性${key}不存在"))

                resultMap += x._1 -> SwitchConfig(x._1, map("enabled").toBoolean, map("depList").stripPrefix("[").stripSuffix("]").split(",").toList.map(_.trim.toInt),
                    List("metaInfo.owner", "metaInfo.comment").flatMap(key => map.get(key).map(value => key -> value)).toMap)
            })
            Result(Some(resultMap), None)
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
        val configStr: String = "switchA.enabled = true\nswitchA.depList = [1, 2, 3]\nswitchA.metaInfo.owner = \"userA\"\nswitchA.metaInfo.comment = \"hello world\"\n    \nswitchB.enabled = false\nswitchB.depList = [3, 4, 5]\nswitchB.metaInfo.owner = \"userB\"\n\nswitchB.metaInfo.comment = \"hello world\"" // 一段配置内容
        val parser = ConfigParser()
        val result = parser.parse(configStr)
        println(result)
    }
}
