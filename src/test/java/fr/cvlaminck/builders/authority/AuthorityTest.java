package fr.cvlaminck.builders.authority;

import fr.cvlaminck.builders.exception.MalformedAuthorityException;
import fr.cvlaminck.builders.uri.encoding.UriEncoding;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AuthorityTest {

    @Test
    public void testEmptyHost() throws Exception {
        Authority emptyHost = Authority.emptyHost();

        assertNull(emptyHost.getEncodedUserInformation());
        assertEquals("", emptyHost.getEncodedHost());
        assertNull(emptyHost.getPort());
    }

    @Test
    public void testParse() throws Exception {
        Authority authority = Authority.parse("localhost");
        assertNull(authority.getEncodedUserInformation());
        assertNull(authority.getPort());
        assertEquals("localhost", authority.getEncodedHost());

        authority = Authority.parse("cvlaminck:password@localhost");
        assertNull(authority.getPort());
        assertEquals("cvlaminck:password", authority.getEncodedUserInformation());
        assertEquals("localhost", authority.getEncodedHost());

        authority = Authority.parse("cvlaminck:password@localhost:4242");
        assertNotNull(authority.getPort());
        assertEquals(4242, (int)authority.getPort());
        assertEquals("cvlaminck:password", authority.getEncodedUserInformation());
        assertEquals("localhost", authority.getEncodedHost());

        authority = Authority.parse("[::1]:4242");
        assertNull(authority.getEncodedUserInformation());
        assertEquals(4242, (int)authority.getPort());
        assertEquals("[::1]", authority.getEncodedHost());

        authority = Authority.parse("localhost:4242");
        assertNull(authority.getEncodedUserInformation());
        assertEquals(4242, (int)authority.getPort());
        assertEquals("localhost", authority.getEncodedHost());
    }

    @Test(expected = MalformedAuthorityException.class)
    public void testParseWithNullAuthority() throws Exception {
        Authority.parse(null);
    }

    @Test(expected = MalformedAuthorityException.class)
    public void testParseWithMalformedAuthority() throws Exception {
        Authority.parse("local@host@4242");
    }

    @Test
    public void testGetEncodedHost() throws Exception {
        Authority authority = Authority.parse("local%21");
        assertEquals("local%21", authority.getEncodedHost());
    }

    @Test
    public void testGetHost() throws Exception {
        Authority authority = Authority.parse("local%21");
        assertEquals("local!", authority.getHost());
    }

    @Test
    public void testGetEncodedUserInformation() throws Exception {
        Authority authority = Authority.parse("user%21@localhost");
        assertEquals("user%21", authority.getEncodedUserInformation());
    }

    @Test
    public void testGetUserInformation() throws Exception {
        Authority authority = Authority.parse("user%21@localhost");
        assertEquals("user!", authority.getUserInformation());
    }

    @Test
    public void testGetPort() throws Exception {
        Authority authority = Authority.parse("localhost:8080");
        assertEquals(8080, (long) authority.getPort());

        authority = Authority.parse("localhost");
        assertNull(authority.getPort());
    }

    @Test
    public void testToString() throws Exception {
        Authority authority = new Authority(UriEncoding.getDefault(), null, "localhost", null);
        assertEquals("localhost", authority.toString());

        authority = new Authority(UriEncoding.getDefault(), "cvlaminck:password", "localhost", null);
        assertEquals("cvlaminck:password@localhost", authority.toString());

        authority = new Authority(UriEncoding.getDefault(), "cvlaminck:password", "localhost", 4242);
        assertEquals("cvlaminck:password@localhost:4242", authority.toString());

        authority = new Authority(UriEncoding.getDefault(), null, "localhost", 4242);
        assertEquals("localhost:4242", authority.toString());
    }
}