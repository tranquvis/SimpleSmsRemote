package tranquvis.simplesmsremote.Helper;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Andi on 03.09.2016.
 */
public class MobileDataHelperTest {

    private Context appContext;

    @Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testSetMobileDataState() throws Exception {
    }
}