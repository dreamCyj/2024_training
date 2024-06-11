package controllers

import play.api.db._
import play.api.mvc._
import services.{NoteService, UserService}

import javax.inject.{Inject, Singleton}

@Singleton
class NoteController @Inject()
    (userService: UserService, noteService: NoteService) extends Controller {

    def getNotes(id: Int): Action[AnyContent] = Action { implicit request =>
      val noteList = noteService.getNotes(id)
      val Some(username) = userService.getName(id)
      Ok(views.html.note(username, noteList))
    }

    def delNote(id: Int): Action[AnyContent] = TODO

    def addNote(): Action[AnyContent] = TODO
}
