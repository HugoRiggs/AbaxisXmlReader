package parser


import analyte._


object Xml {

  val re_nonDigit = """[^\d^\.]*""".r

  def numericDateToReadable(date: String): String = { 
    val yearAndTail = date.splitAt(4)
    val year = yearAndTail._1
    val monthNum = yearAndTail._2.splitAt(2)._1
    val day = yearAndTail._2.splitAt(2)._2

    val month = monthNum match {
      case "01" => "Jan"
      case "02" => "Feb"
      case "03" => "Mar"
      case "04" => "Apr"
      case "05" => "May"
      case "06" => "Jun"
      case "07" => "Jul"
      case "08" => "Aug"
      case "09" => "Sep"
      case "10" => "Oct"
      case "11" => "Nov"
      case "12" => "Dec"
    }

    day + " " + month + " " + year

  }

  def comprehensive(loadnode: xml.Node) = new Comprehensive(loadnode)
}


class Comprehensive(loadnode: xml.Node) {

  import Xml.{re_nonDigit, numericDateToReadable}

  // rotor name
  val rotorName = (loadnode \\ "rotorName").text

  // date
  val date = numericDateToReadable( (loadnode \\ "runDate").text )

  // sample type aka animal type 
  val sampleType = (loadnode \\ "sampleType").text

  // patient name aka patient id
  val patientName = (loadnode \\ "patientControlId").text


  /// ANALYTE TEST ///

  // buffer for results 
  val analyteResults = collection.mutable.ListBuffer[Analyte]()
  // analyte node sequence
  val analyteNodes = (loadnode \\ "analyte")
    // nested nodes
  val analyteNames = (analyteNodes \\ "name")
  val analyteValues = (analyteNodes \\ "value")
  val analyteLows = (analyteNodes \\ "lowReferenceRange")
  val analyteHighs = (analyteNodes \\ "highReferenceRange")
  val analyteConcentrations = (analyteNodes \\ "units")

  val allSameSize = (analyteNodes.size == analyteNames.size) == (analyteValues.size == analyteLows.size) == (analyteHighs.size == analyteNodes.size)
  if( ! allSameSize ) 
    println("Error: Not all sequences are the same size!")

  for(i <- 0 until analyteNodes.size) {
    analyteResults += 
      (new Analyte(analyteNames(i).text, re_nonDigit.replaceAllIn(analyteValues(i).text, "").toDouble, analyteLows(i).text.toDouble, analyteHighs(i).text.toDouble, analyteConcentrations(i).text))
  }


  /// QC  TEST ///

  // Hem
  val hem = (loadnode \\ "hem" \ "index").text

  // LIP 
  val lip = (loadnode \\ "lip" \ "index").text

  // ICT
  val ict = (loadnode \\ "ict" \ "index").text

  val horzSp = ("."*78) + "\n"
  private var printOut =
    rotorName + "\n" + date+ "\n" + "Sample Type:\t" + sampleType+ "\n" + "Patient ID:\t" + patientName+ "\n" + horzSp + analyteResults.mkString("\n") + "\n\nQC\n" + "HEM " + hem + "\tLIP " + lip +"\tICT " + ict

  def getResults = printOut
  
}
