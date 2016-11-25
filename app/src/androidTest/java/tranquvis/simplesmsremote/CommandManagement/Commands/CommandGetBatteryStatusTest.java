package tranquvis.simplesmsremote.CommandManagement.Commands;

/**
 * Created by Kaltenleitner Andreas on 29.10.2016.
 */
public class CommandGetBatteryStatusTest extends CommandTest {

    @Override
    public void testPattern() throws Exception {
        assertThat("\n get  Battery status \r").matches();
        assertThat("is battery charging").matches();
        assertThat("is charging").matches();
        assertThat("is battery charging?").matches();
        assertThat("is battery loading").matches();
        assertThat("charging?").matches();
        assertThat("battery loading?").matches();

        assertThat("charging").doesNotMatch();
    }

    @Override
    public void testExecution() throws Exception {
        assertThat("is battery charging?").matches().executes();
    }
}