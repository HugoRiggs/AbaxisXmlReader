package gui 

import swing._
import swing.event._
import java.awt.Color._ 

object SwingWrapper extends SimpleSwingApplication {

  def top = new MainFrame {

    title = "Abaxis XML Converter"

    // final dimensions (constant)
    val mainDimension = new Dimension(1000, 1500)
    val scrollPaneDimension = new Dimension(900, 500)
    val textFieldDimension = new Dimension(500, 40)
    val buttonDimension = new Dimension(60, 30)

    // application dimensions (window size)
    size = mainDimension

    // buttons
    val startButton = new Button { text = "Convert"; minimumSize = buttonDimension }
    val clearButton = new Button { text = "Clear Text" ; minimumSize = buttonDimension }

    // radio buttons
    val comprehensiveRadioButton = new RadioButton { text = "Comprehensive Diagnostics" ; minimumSize = buttonDimension }
    val kidneyRadioButton = new RadioButton { text = "kidney Diagnostics"; minimumSize = buttonDimension  }
    val allCompInPathRadioButton = new RadioButton { text = "All Comprehensive in directory"; minimumSize = buttonDimension  }


    // text field,  area and scroll pane
    val pathAndFile = new TextField { text = ""; minimumSize = textFieldDimension; maximumSize = textFieldDimension } 
    val textArea = new TextArea {editable = false;}
    val scrollPane = new ScrollPane(textArea) { minimumSize = scrollPaneDimension; maximumSize = scrollPaneDimension }

    contents = new BoxPanel(Orientation.Vertical) {

      contents += pathAndFile

      contents += new BoxPanel(Orientation.Horizontal){

        contents += new BoxPanel(Orientation.Vertical) {

          contents += comprehensiveRadioButton
          contents += allCompInPathRadioButton
          contents += startButton
        }

        contents += new BoxPanel(Orientation.Vertical) {

          contents += kidneyRadioButton
          contents += clearButton
        }
      }

        contents += scrollPane 
        border = Swing.EmptyBorder(30, 30, 10, 10)
    }

    listenTo(startButton)
    listenTo(clearButton)
    listenTo(comprehensiveRadioButton)
    listenTo(kidneyRadioButton)

    reactions +=  {
      case ButtonClicked(b) => {

        if(b.text == "Convert"){

          if(comprehensiveRadioButton.selected){

            // file to convert
              val loadnode = xml.XML.loadFile(pathAndFile.text)

            // add data to text field
            textArea.text += parser.Xml.comprehensive(loadnode).getResults
          } else if (allCompInPathRadioButton.selected){

            val fileProcessor = filehandler.BatchFileProcessor(pathAndFile.text, "Comp")
          
            def workMethod(xmlFile : java.io.File) = {
              val loadnode = xml.XML.loadFile(xmlFile)
              textArea.text += ("\n"+parser.Xml.comprehensive(loadnode).getResults+"\n\n")
            }

            worker.CurriedWorkAction(fileProcessor.getFiles)(workMethod)
          }

        } else if (b.text == "Clear Text"){

          textArea.text = ""

        }



      }
    }


  }

}
