package abaxis_xml_reader

import java.io.File
import reflect.io.Directory
import scala.io.Source
import java.io.PrintWriter

class Controller 
{
  
  def convert(path: String): String = 
  {
    // file to convert
    val file = new File(path)
    val loadnode = xml.XML.loadFile(file.getAbsolutePath)

    val ret_str = ("\n"+parser.Xml.comprehensive(loadnode).getResults+"\n\n")
    return ret_str
  }


  def convert_all(path: String): String = 
  {
    val file = new  File(path)
    var dirPath = file.getAbsolutePath
    if(file.isDirectory == false){
      dirPath = file.getParent
    }

    val files = Directory(dirPath).files
    val filtered_files = files.filter( f => f.name.contains("Comp") )
    // convert scala file type to java file type
    val mapped_files = filtered_files.map( f => f.jfile )

    var ret_str = ""
    def workMethod(xmlFile : File) = {
      val loadnode = xml.XML.loadFile(xmlFile)
      ret_str += ("\n"+parser.Xml.comprehensive(loadnode).getResults+"\n\n")
    }

    mapped_files.foreach( f => workMethod(f) )

    ret_str
  }

  def store_value(key: String, value: String): Boolean = 
  {
    val storage_dir = new File(System.getProperty("user.home"), "abaxis_xml_reader")
    val b = storage_dir.mkdir()
    if (b)
      print("Created directory " + storage_dir.getAbsolutePath) 
    val path = storage_dir.getAbsolutePath//new File(storage_dir.getPath).getPath
    println("PATH " + path)

    val store_file = new File(path, "store")
    store_file.createNewFile()
    println("STORE_FILE " + store_file.getPath)

    val temp_dir = new File(path, "tmp")
    val b1 = temp_dir.mkdir()
    println("TEMP_DIR " + temp_dir.getPath)

    val temp_file = new File(temp_dir, "t")
    temp_file.createNewFile()
    println("TEMP_FILE " + temp_file.getPath)

//    val w = new PrintWriter(temp_file)
    val w = new PrintWriter(store_file)
    var f = false
    Source.fromFile(store_file).getLines
      .map( x => if( x.contains("key"))
                    {
                      f = true
                      val a = x.split("=")
                      w.println(a(0) + "=" + value)
                    }
                else x )
      .foreach( x => w.println(x) )

    if(f == false)
      w.write(key+"="+value)

    w.close()

//    import java.nio.file.Files
//    import java.nio.file.StandardCopyOption
//    import java.io.FileInputStream
//    Files.copy(
//      new FileInputStream(temp_file),
//      store_file.toPath,
//      StandardCopyOption.REPLACE_EXISTING)

//    val b2 = temp_file.renameTo(store_file)
//    if (b2)
//      println("Renamed file successfully")
//    else 
//      println("Failed to renamed file ")
//
      false
  }

  def load_value(k: String) : String = 
  {
    val storage_dir = new File(System.getProperty("user.home"), "abaxis_xml_reader")
    val b = storage_dir.mkdir()
    if (b)
      print("Created directory " + storage_dir.getAbsolutePath) 
    val path = storage_dir.getPath
    println("PATH " + path)

    val store_file = new File(path, "store")
    store_file.createNewFile()
    println("STORE_FILE " + store_file.getPath)   

    var ret = ""
    for (line <- Source.fromFile(store_file).getLines) {
      if(line.contains(k)){
        val a = line.split("=")
        ret += a(1)
      }
    }
    println("returning " + ret)
    ret
  }

}
