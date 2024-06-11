package services

import models.Task
import play.api.db.Database
import play.db.NamedDatabase

import javax.inject.Inject
import scala.collection.mutable.ListBuffer

class TaskService @Inject() (@NamedDatabase("default") db: Database) {

    /**
     * 根据用户id获取任务列表
     * @param id 用户id
     * @return
     */
    def getTasks(id: Int): ListBuffer[Task] = {
        val taskList = ListBuffer[Task]()
        db.withConnection { conn =>
            val sql = "select * from task where user_id = ?"
            val preparedStatement = conn.prepareStatement(sql)
            preparedStatement.setInt(1, id) //第1个参数是1
            val result = preparedStatement.executeQuery()
            while (result.next()) {
                val taskId = result.getInt("id")
                val taskName = result.getString("name")
                taskList += Task(taskId, taskName)
            }
            conn.close()
        }
        taskList
    }

    /**
     * 根据任务id删除任务
     * @param id 任务id
     */
    def delTask(id: Int): Unit = {
        db.withConnection { conn =>
            val sql = "delete from task where id = ?"
            val preparedStatement = conn.prepareStatement(sql)
            preparedStatement.setInt(1, id) //第1个参数是1
            preparedStatement.executeUpdate()
            conn.close()
        }
    }

    def addTask(name: String, userId: Int): Unit = {
        db.withConnection { conn =>
            val sql = "insert into task(name, user_id) values (?, ?)"
            val preparedStatement = conn.prepareStatement(sql)
            preparedStatement.setString(1, name) //第1个参数是1
            preparedStatement.setInt(2, userId)
            preparedStatement.executeUpdate()
            conn.close()
        }
    }

}
