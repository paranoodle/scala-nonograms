package heigvd.nonograms

import junit.framework._
import Assert._

object NonogramsAppTest {
    def suite: Test = {
        val suite = new TestSuite(classOf[NonogramsAppTest])
        suite
    }

    def main(args : Array[String]) {
        junit.textui.TestRunner.run(suite)
    }
}

/**
 * Unit test for simple App.
 */
class NonogramsAppTest extends TestCase("app") {

    /**
     * Rigourous Tests :-)
     */
    def testOK() = {
      NonogramsOffline.main(Array[String]())
      assertTrue(true)
    }
    //def testKO() = assertTrue(false);
    

}
