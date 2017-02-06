package filehandler

import java.io.{File, BufferedWriter, FileWriter}

object BatchFileProcessor {
  def apply(dir: String) = new BatchFileProcessor(dir, ".*")
  def apply(dir: String, regex: String) = new BatchFileProcessor(dir, regex)
}

class BatchFileProcessor(dir: String, regex: String) {
  

  def getFiles = {

    // method to grab all files in directory provided.
    def getListOfFiles(dir: String):List[File] = {
      val d = new File(dir)
      if (d.exists && d.isDirectory) {
        d.listFiles.filter(_.isFile).toList
      } else {
        List[File]()
      }
    }

    // Initialize list of files and put all files into this variable
    var files = getListOfFiles(dir)

    // Create regex pattern matcher from passed string
    val re = regex.r

    // Filter wanted files by matching against the regex.
    files.filter(file => re.findFirstIn(file.toString).size > 0)
  }

  def makeFile(name: String, text: String = "") = {

    // Use method to fix postfix directory issue
    val fixedDir = util.PathsAndNames.fixPath(dir)
    
    // used fixed directory to create a new file in memory
    val file = new File(fixedDir + name)

    // Create buffered writer which buffers a new file writter, for the file already in memory
    val bw = new BufferedWriter(new FileWriter(file)) 

    // write the optional text to the file and create the file in the file/folder system
    bw.write(text)

    // close the buffer
    bw.close()
  }

  def deleteFile(name: String) = {
    // TODO: implement delete method
  }

}


