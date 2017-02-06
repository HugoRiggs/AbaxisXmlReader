import org.scalatest.FunSuite

class GuiTest extends FunSuite {

  test("gui test"){
   gui.SwingWrapper.main(Array())
   io.StdIn.readLine()
  }
}
