package io.github.hugoriggs.AbaxisBloodMachineXmlReader.gui

import swing._
import swing.event._
import java.awt.Color._ 
import io.github.hugoriggs.AbaxisBloodMachineXmlReader.{parser, analyte}
import io.github.hugoriggs.fileprocs.{Database => db, Directory}
import io.github.hugoriggs.fileprocs.DatabaseUtils._
import io.github.hugoriggs.fileprocs.DirectoryUtils._


object SwingWrapper extends SimpleSwingApplication {

  val debugPrinting = true

  def debug(msg: String) = if (debugPrinting) { println(msg) }

  def top = new MainFrame {

    val s = this.getClass.getProtectionDomain.getCodeSource.getLocation.toURI.getPath
    debug (" \n\n Running directory = " + s + "\n\n")
    db.getDatabase( s )

    // WINDOW DIMENSIONS AND TITLE
    title = "Abaxis XML Converter"
    preferredSize = new Dimension(500, 1024)

    // FINAL DIMENSIONS (CONSTANT)
    val mainDimension = new Dimension(1000, 1500)
    val scrollPaneDimension = new Dimension(900, 1000)
    val textFieldDimension = new Dimension(500, 40)
    val buttonDimension = new Dimension(60, 30)

    // APPLICATION DIMENSIONS (WINDOW SIZE)
    size = mainDimension

    // BUTTONS
    val startButton = new Button { name = "Convert"; text = "Convert"; minimumSize = buttonDimension }
    val clearButton = new Button { name="Clear Text"; text = "Clear Text" ; minimumSize = buttonDimension }

    // RADIO BUTTONS
    val comprehensiveRadioButton = new RadioButton { name="CompRadi"; text = "Comprehensive Diagnostics" ; minimumSize = buttonDimension }
    val kidneyRadioButton = new RadioButton { name="KidRadi"; text = "kidney Diagnostics"; minimumSize = buttonDimension  }
    val allCompInPathRadioButton = new RadioButton { name="allComp"; text = "All Comprehensive in directory"; minimumSize = buttonDimension  }

    // TEXT FIELD,  AREA AND SCROLL PANE
    val pathAndFile = new TextField { text = db.getVal("DefDir")+""; minimumSize = textFieldDimension; maximumSize = textFieldDimension } 
    val textArea = new TextArea {editable = false; minimumSize = scrollPaneDimension; maximumSize = scrollPaneDimension}
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
        border = Swing.EmptyBorder(20, 20, 10, 10)
    }

    listenTo(startButton)
    listenTo(clearButton)
    listenTo(comprehensiveRadioButton)
    listenTo(allCompInPathRadioButton)
    listenTo(kidneyRadioButton)

    // LOGICAL REACTIONS TO GUI INTERFACE ABOVE
    reactions +=  {
      case ButtonClicked(button) => {
        button.name match {
          case "Convert" => {

            debug("setting default directory...")
            db.setDefDir(PathsAndNames.justDir(pathAndFile.text)) // DATABASE SAVES PATH

            // GET FILE PATH AND CORRECT MISSING EXTENTION IF NEEDED
            var xmlAbsPth = pathAndFile.text
            if(xmlAbsPth.reverse.substring(0, 4) != "lmx."){
              val tmp = xmlAbsPth +".xml"
              val potentialFile = new java.io.File(tmp)
              if(potentialFile.exists && potentialFile.isFile)
                xmlAbsPth += ".xml"
            }

            debug("xmlAbsPth = " + xmlAbsPth)
            val dir = Directory(xmlAbsPth, ".*Comp.*xml") // FILTER: COMPREHENSIVE XML FILES
            val fileExists = new java.io.File(xmlAbsPth).exists // DETECT IF FILE EXISTS 

            // IF COMPREHENSIVE RADIO BUTTON DEPRESSED AND FILE WAS DETECTED
            if(comprehensiveRadioButton.selected && fileExists){

              // LOAD DATA TO PARSE
              val loadnode = xml.XML.loadFile(xmlAbsPth)
              // ADD PARSED DATA TO TEXT FIELD
              textArea.text += parser.Xml.comprehensive(loadnode).getResults
            } // OTHERWISE IF ALL COMPREHENSIVES IS SELECTED 
            else if (allCompInPathRadioButton.selected){

              def workMethod(xmlFile : java.io.File) = {
                val loadnode = xml.XML.loadFile(xmlFile)
                textArea.text += ("\n"+parser.Xml.comprehensive(loadnode).getResults+"\n\n")
              }

              debug("Files in directory = " + dir.getFiles.mkString("\n"))
              debug("Converting all files in directory... ")

              CurriedWorkAction(dir.getFiles)(workMethod)
            }

          }
          case "Clear Text" => {
            textArea.text = ""
          }
          case "CompRadi" => {
            allCompInPathRadioButton.selected = false
            kidneyRadioButton.selected = false
          }
          case "KidRadi" => {
            comprehensiveRadioButton.selected = false
            allCompInPathRadioButton.selected = false
          }
          case "allComp" => {
            comprehensiveRadioButton.selected = false
            kidneyRadioButton.selected = false
          }
        }

//        if(b.text == "Convert"){
//
//          debug("setting default directory...")
//          db.setDefDir(PathsAndNames.justDir(pathAndFile.text))
//
//          // GET FILE PATH AND CORRECT MISSING EXTENTION IF NEEDED
//          var xmlAbsPth = pathAndFile.text
//          if(xmlAbsPth.reverse.substring(0, 4) != "lmx."){
//            val tmp = xmlAbsPth +".xml"
//            val potentialFile = new java.io.File(tmp)
//            if(potentialFile.exists && potentialFile.isFile)
//              xmlAbsPth += ".xml"
//          }
//
//          debug("xmlAbsPth = " + xmlAbsPth)
//
//          val dir = Directory(xmlAbsPth, ".*Comp.*xml")
//
//          val fileExists = new java.io.File(xmlAbsPth).exists
//
//          if(comprehensiveRadioButton.selected && fileExists){
//
//            // FILE TO CONVERT
//            val loadnode = xml.XML.loadFile(xmlAbsPth)
//
//            // ADD DATA TO TEXT FIELD
//            textArea.text += parser.Xml.comprehensive(loadnode).getResults
//          } else if (allCompInPathRadioButton.selected){
//
//            def workMethod(xmlFile : java.io.File) = {
//              val loadnode = xml.XML.loadFile(xmlFile)
//              textArea.text += ("\n"+parser.Xml.comprehensive(loadnode).getResults+"\n\n")
//            }
//
//            debug("Files in directory = " + dir.getFiles.mkString("\n"))
//            debug("Converting all files in directory... ")
//
//            CurriedWorkAction(dir.getFiles)(workMethod)
//          }
//
//        } else if (b.text == "Clear Text"){
//
//          textArea.text = ""
//
//        }
//


      }
    }


  }

}
