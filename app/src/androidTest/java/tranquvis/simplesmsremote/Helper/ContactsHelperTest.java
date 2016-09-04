package tranquvis.simplesmsremote.Helper;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;

import static org.junit.Assert.*;

/**
 * Created by Andi on 04.09.2016.
 */
public class ContactsHelperTest extends AppContextTest
{
    @Test
    public void testReadContacts() throws Exception {
        ContactsHelper.readContacts(appContext);
    }
}