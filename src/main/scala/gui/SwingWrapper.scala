package abaxis_xml_reader.gui 

/*
 * boundary class for the abaxis xml reader program
 * note: declared as an object (see Scala lang.), it is always running during program execution
 */

import swing._
import swing.event._
import swing.Component._

import java.awt.Robot
import java.awt.event.KeyEvent
import java.awt.Color._
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView

import abaxis_xml_reader.Controller


object SwingWrapper extends SimpleSwingApplication {

  // FINAL DIMENSIONS (CONSTANT)
  private val mainDimension = new Dimension(850, 750)
  private val scrollPaneDimension = new Dimension(800, 700)
  private val textFieldDimension = new Dimension(800, 25)
  private val buttonDimension = new Dimension(50, 25)

  // BUTTONS
  private val startButton = new Button
  {
    text = "Convert"; minimumSize = buttonDimension
  }
  private val startAllButton = new Button
  {
    text = "Convert All"; minimumSize = buttonDimension
  }
  private val clearButton = new Button
  {
    foreground=WHITE
    background=RED
    text = "Clear Text" ; minimumSize = buttonDimension
  }
  private val select_file_button = new Button
  {
    background=GREEN
    text = "choose file" ; minimumSize = buttonDimension
  }
  private val select_all_button = new Button
  {
    background=YELLOW
    foreground=BLUE
    text = "select all" ; minimumSize = buttonDimension
  }
  private val copy_button = new Button
  {
    background=YELLOW
    foreground=BLUE
    text = "copy" ; minimumSize = buttonDimension
  }

  // TEXT FIELD,  AREA AND SCROLL PANe
  private  val pathAndFile = new TextField 
  { 
    text = ""; minimumSize = textFieldDimension; maximumSize = textFieldDimension 
  }
  private val textArea = new TextArea 
  {
    editable = false;
    listenTo(mouse.clicks)
    reactions += 
    {
      case c: MouseClicked => 
        val p = c.point
        if ( c.modifiers == 256 ) {
          ctrl_c()

          println(  "mouse clicked at " + c.point
                 +"\nmodifier = " + c.modifiers)
        }
    }
  }
  private val scrollPane = new ScrollPane(textArea) 
  {
    minimumSize = scrollPaneDimension; maximumSize = scrollPaneDimension 
  }

  def ctrl_c() = {
     val robot = new Robot()
     robot.keyPress(KeyEvent.VK_CONTROL)
     Thread.sleep(200)
     robot.keyPress(KeyEvent.VK_C)
     Thread.sleep(200)
     robot.keyRelease(KeyEvent.VK_C)
     Thread.sleep(200)
     robot.keyRelease(KeyEvent.VK_CONTROL)
     Thread.sleep(200)
  }
  
  // ENTRY POINT
  def top = new MainFrame 
  {
    title = "Abaxis XML Converter"
    size = mainDimension // application dimensions (window size)
    minimumSize = mainDimension
    val controller = new Controller() // initialize controller class
    pathAndFile.text = controller.load_value("defDir")

    // DESIGN STRUCTURE~
    // VERTICAL
    contents = new BoxPanel(Orientation.Vertical)
    {

      // HORIZONTAL BOX PANEL
      contents += new BoxPanel(Orientation.Horizontal)
      {
        contents += select_file_button
        contents += pathAndFile
      }

      // VERTICAL BOX PANEL
      contents += new BoxPanel(Orientation.Vertical)
      {
        contents += scrollPane
        // HORIZONTAL BOX PANEL
        contents += new BoxPanel(Orientation.Horizontal)
        {
          contents += startButton
          contents += startAllButton
//          contents += select_all_button
          contents += copy_button
          contents += clearButton
        }
      }
    }

    // DECLARE COMPONENTS AS LISTENERS~
    listenTo(startButton)
    listenTo(startAllButton)
    listenTo(clearButton)
    listenTo(select_file_button)
//    listenTo(select_all_button)
    listenTo(copy_button)

    reactions +=
    {
      case ButtonClicked(b) =>
      {
        if(b.text =="select all")
        {
          textArea.selectAll()
        }

        if(b.text == "copy")
        {
//          ctrl_c()
          import java.awt.datatransfer.StringSelection
          import java.awt.datatransfer.Clipboard
          import java.awt.Toolkit

          val selection = new StringSelection(textArea.text);
          val clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
          clipboard.setContents(selection, selection); 
          println("COPIED TO CLIPBOARD")
        }

        if(b.text == "Convert") // button convert
        {
          // file to convert
          val ret = controller.convert(pathAndFile.text)

          // add converted data to text field
          textArea.text += ret

        } else if (b.text == "Clear Text") // button clear text
        {
          textArea.text = "" // make text body null
        } else if (b.text == "Convert All") // button convert all xml files
        {
          // get results of convert all method
          val ret = controller.convert_all(pathAndFile.text) 

          // add results to text body
          textArea.text = ret

        }
        else if (b.text == "choose file")
        {
          // load the previously used directory
          var start_in = controller.load_value("defDir")

          // open on user home
          //val file_chooser = new JFileChooser(FileSystemView.getFileSystemView.getHomeDirectory())
          // open on previous dir
          val file_chooser = new JFileChooser(start_in)
          // open file chooser 
          file_chooser.showOpenDialog(null)
          val file = file_chooser.getSelectedFile()
          pathAndFile.text = file.getAbsolutePath()
          controller.store_value("defDir", file.getParent)

        }

      }

    }


  }

}
