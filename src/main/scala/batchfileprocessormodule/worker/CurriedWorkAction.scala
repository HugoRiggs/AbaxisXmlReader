package worker

import java.io.File

object CurriedWorkAction {

  def apply(files: List[File])(action: => Function1[File, Unit]) = {
    files.foreach(file => action(file))
  }

}
