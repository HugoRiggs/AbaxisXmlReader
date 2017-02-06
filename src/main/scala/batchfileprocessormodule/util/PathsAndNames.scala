package util

object PathsAndNames {

  def fixPath(path: String) = {

    val pathFixed = {
      val osName = System.getProperty("os.name")
        println(osName)
        osName match {
          case "Linux" => if(path.last=="/") path else path+"/"
          case "Windows 7" => if(path.last=="\\") path else path+"\\"
          case _ => print("Warning unsupported operating system.\n"); 
        }
    }

    pathFixed
  }

}
