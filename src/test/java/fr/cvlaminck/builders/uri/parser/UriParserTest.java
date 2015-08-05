package fr.cvlaminck.builders.uri.parser;

import fr.cvlaminck.builders.exception.MalformedSchemeException;
import fr.cvlaminck.builders.exception.MalformedUriException;
import fr.cvlaminck.builders.uri.Uri;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class UriParserTest {

    @Parameter(0)
    public String uri;

    @Parameter(1)
    public boolean isValid;

    @Parameter(2)
    public Class<? extends MalformedUriException> exceptionClass;

    @Test
    public void testParseValidUri() throws Exception {
        if (!isValid) {
            return;
        }
        new UriParser().parse(uri);
    }

    @Test
    public void testParseInvalidUri() throws Exception {
        if (isValid) {
            return;
        }
        Exception parseException = null;
        try {
            new UriParser().parse(uri);
        } catch (Exception e) {
            parseException = e;
        }
        assertEquals(exceptionClass, parseException.getClass());
    }

    @Parameters(name = "Uri: {0}, isValid: {1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                //Technical test made to ensure 100% code coverage of the parser
                {"file:/", true, null},
                {"file:/mnt", true, null},
                {"file:m/", true, null},
                {"file:mnt", true, null},
                {"file:///", true, null},
                {"file://", true, null}, //Valid -> Empty authority and empty path.
                {"file://?query", true, null},
                {"file://#fragment", true, null},
                {"http://localhost?query#fragment", true, null},
                {null, false, MalformedUriException.class},
                {"", false, MalformedUriException.class},
                {"localhost", false, MalformedSchemeException.class},
                //Examples from RFC3986
                {"ftp://ftp.is.co.za/rfc/rfc1808.txt", true, null},
                {"http://www.ietf.org/rfc/rfc2396.txt", true, null},
                {"ldap://[2001:db8::7]/c=GB?objectClass?one", true, null},
                {"telnet://192.0.2.16:80/", true, null},
                //FIXME: Give option to the parser {"news:comp.infosystems.www.servers.unix", 3},
        });
    }
}