package abaxis_xml_reader.gui 

import swing._
import swing.event._
import java.awt.Color._
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView
import abaxis_xml_reader.Controller

object SwingWrapper extends SimpleSwingApplication {
  // final dimensions (constant)
  private val mainDimension = new Dimension(888, 777)
  private val scrollPaneDimension = new Dimension(800, 500)
  private val textFieldDimension = new Dimension(500, 25)
  private val buttonDimension = new Dimension(50, 25)
  // buttons
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
    background=RED
    text = "Clear Text" ; minimumSize = buttonDimension
  }
  private val select_file_button = new Button
  {
    background=GREEN
    text = "choose file" ; minimumSize = buttonDimension
  }
  // radio buttons
  // text field,  area and scroll pane
  private  val pathAndFile = new TextField 
  { 
    text = ""; minimumSize = textFieldDimension; maximumSize = textFieldDimension 
  }
  private val textArea = new TextArea 
  {
    editable = false;
  }
  private val scrollPane = new ScrollPane(textArea) 
  {
    minimumSize = scrollPaneDimension; maximumSize = scrollPaneDimension 
  }

  def top = new MainFrame {
    title = "Abaxis XML Converter"
    size = mainDimension // application dimensions (window size)
    minimumSize = mainDimension
    // initialize controller class
    val controller = new Controller()
    pathAndFile.text = controller.load_value_from_storage("defDir")
    // DESIGN STRUCTURE
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
          contents += clearButton
        }
      }
//        border = Swing.EmptyBorder(30, 30, 10, 10)
    }
    listenTo(startButton)
    listenTo(startAllButton)
    listenTo(clearButton)
    listenTo(select_file_button)
    reactions +=
    {
      case ButtonClicked(b) =>
      {

        if(b.text == "Convert")
        {
          // file to convert
          val ret = controller.convert(pathAndFile.text)//xml.XML.loadFile(pathAndFile.text)

          // add data to text field
          textArea.text += ret

        } else if (b.text == "Clear Text")
        {
          textArea.text = ""
        } else if (b.text == "Convert All")
        {
          val ret = controller.convert_all(pathAndFile.text)
          textArea.text = ret

        }
        else if (b.text == "choose file")
        {
          val file_chooser = new JFileChooser(FileSystemView.getFileSystemView.getHomeDirectory())
          file_chooser.showOpenDialog(null)
          val file = file_chooser.getSelectedFile()
          pathAndFile.text = file.getAbsolutePath()
          controller.store_value("defDir", file.getParent)

        }

      }

    }


  }

}
