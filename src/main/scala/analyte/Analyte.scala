package abaxis_xml_reader.analyte

class Analyte(name: String, value: Double, low: Double, high: Double, concentration: String) {

  // Example:  1.4 => 1.4, 3.0 => 3
  def simplifyWholeDouble(d: Double): String = if(d.isWhole) d.toInt.toString else d.toString

  override def toString = 
    name + "\t" + simplifyWholeDouble(value) + " " + mark + "\t" + simplifyWholeDouble(low)  + "-" + simplifyWholeDouble(high) + "\t" + concentration 

  val mark = if(value > high || value < low) "*" else ""
}

