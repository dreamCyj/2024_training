package services

import models.User
import play.api.db.Database
import play.db.NamedDatabase

import javax.inject.Inject

class UserService @Inject()(@NamedDatabase("default") db: Database) {

    def validateUser(username: String, password: String): Option[User] = {
        db.withConnection { conn =>
            val sql = "select * from user where name = ? and password = ?"
            val preparedStatement = conn.prepareStatement(sql)
            preparedStatement.setString(1, username)
            preparedStatement.setString(2, password)
            val result = preparedStatement.executeQuery()
            if (result.next()) {
                val userId = result.getInt("id")
                val age = result.getInt("age")
                result.close()
                preparedStatement.close()
                conn.close()
                Some(User(userId, username, "***", Some(age)))
            }else {
                result.close()
                preparedStatement.close()
                conn.close()
                None
            }
        }
    }
    def getName(id: Int) : Option[String] = {
        db.withConnection { conn =>
            val sql = "select name from user where id = ?"
            val preparedStatement = conn.prepareStatement(sql)
            preparedStatement.setInt(1, id)
            val result = preparedStatement.executeQuery()
            if (result.next()) {
                val name = result.getString("name")
                result.close()
                preparedStatement.close()
                conn.close()
                Some(name)
            }else {
                result.close()
                preparedStatement.close()
                conn.close()
                None
            }
        }
    }
}
