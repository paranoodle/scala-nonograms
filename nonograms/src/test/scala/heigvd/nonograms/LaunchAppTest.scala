package heigvd.nonograms

import junit.framework.Assert._
import junit.framework._

object LaunchAppTest {
  def suite: Test = {
    val suite = new TestSuite(classOf[LaunchAppTest])
    suite
  }

  def main(args: Array[String]) {
    junit.textui.TestRunner.run(suite)
  }
}

/**
  * Launch test of simple App.
  */
class LaunchAppTest extends TestCase("app") {

  /**
    * Launch the GUI and does not test anything programmatically
    *
    * TODO: uncomment this line to enable quick-GUI-test in IDE or with mvn test
    * MUST BE COMMENTED TO ENABLE MVN TEST FOR ALL TEST SUITE & MVN INSTALL TO RELEASE
    */

  def testOK() = {
    //NonogramsOffline.main(Array[String]())
    assertTrue(true)
  }
}
