package services

import models.Note
import play.api.db.Database
import play.db.NamedDatabase

import javax.inject.Inject
import scala.collection.mutable.ListBuffer

class NoteService @Inject()(@NamedDatabase("default") db: Database) {

    /**
     * 根据用户id获取任务列表
     *
     * @param id 用户id
     * @return
     */
    def getNotes(id: Int): ListBuffer[Note] = {
        val noteList = ListBuffer[Note]()
        db.withConnection { conn =>
            val sql = "select * from note where user_id = ?"
            val preparedStatement = conn.prepareStatement(sql)
            preparedStatement.setInt(1, id) //第1个参数是1
            val result = preparedStatement.executeQuery()
            while (result.next()) {
                val noteId = result.getInt("id")
                val content = result.getString("content")
                noteList += Note(noteId, content)
            }
            conn.close()
        }
        noteList
    }


}
