package tranquvis.simplesmsremote.Utils;

/**
 * Created by Kaltenleitner Andreas on 29.10.2016.
 */

public class UnitTestUtils {
    /**
     * Get tested class from unit test class.
     * The name of the unit test class must equal: [tested class name]Test.
     *
     * @param unitTestClass class of the unit test
     * @return tested class
     * @throws Exception
     */
    public static Class GetTestedClassFrom(Class unitTestClass)
            throws ClassNotFoundException {
        String testUnitClassName = unitTestClass.getName();
        String testedClassName = testUnitClassName.replace("Test", "");
        return Class.forName(testedClassName);
    }

    /**
     * Counterpart to {@code GetTestedClassFrom}.
     *
     * @see #GetTestedClassFrom(Class)
     */
    public static Class GetUnitTestClassFrom(Class testedClass) throws ClassNotFoundException {
        String testedClassName = testedClass.getName();
        String testUnitClassName = testedClassName + "Test";
        return Class.forName(testUnitClassName);
    }
}
