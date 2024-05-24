package cn.cyj.traitdemo
import java.util.{HashSet => _, _}
object Trait4 {

    trait Handler {
        def handler(data: String) = {
            println("实际处理数据")
            println("data：" + data)
        }
    }

    trait DataValidHandler extends Handler {
        override def handler(data: String): Unit = {
            println("数据检验")
            super.handler(data)
            println("(")
        }
    }

    trait SignatureValidHandler extends Handler {
        override def handler(data: String): Unit = {
            println("签名检验")
            super.handler(data)
            println("((")
        }
    }

    class Payment extends DataValidHandler with SignatureValidHandler {
        def pay(data: String) = {
            println("用户发起支付请求")
            super.handler(data)
            println("(((")
        }
    }

    def main(args: Array[String]): Unit = {
        val p = new Payment
        p.pay("马云给我转了一个小目标")
    }
}
