package cn.cyj.traitdemo

object Trait2 {
    trait playLOL {
        //抽象方法
        def top()
        def mid()
        def adc()
    }

    abstract class play extends  playLOL {
        //适配器类 重写特质所有抽象方法
        override def top(): Unit = {}
        override def mid(): Unit = {}
        override def adc(): Unit = {}
        }
    class player extends  play {
        //继承适配器类 需要哪个 重写哪个
        override  def top() = println("我玩上单")
    }
    def main(args: Array[String]): Unit = {
        val p = new player
        p.top()
    }
}
