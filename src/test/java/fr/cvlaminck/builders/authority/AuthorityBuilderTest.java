package fr.cvlaminck.builders.authority;

import fr.cvlaminck.builders.exception.MalformedAuthorityException;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuthorityBuilderTest {

    @Test
    public void testWithEncodedUserInformation() throws Exception {
        Authority authority = Authority.newBuilder()
                .withEncodedHost("localhost")
                .withEncodedUserInformation("test")
                .build();

        assertEquals("test", authority.getEncodedUserInformation());
    }

    @Test
    public void testWithEncodedUserInformationEmpty() throws Exception {
        Authority authority = Authority.newBuilder()
                .withEncodedHost("localhost")
                .withEncodedUserInformation("")
                .build();

        assertNull(authority.getEncodedUserInformation());
    }

    @Test
    public void testWithEncodedUserInformationNull() throws Exception {
        Authority authority = Authority.newBuilder()
                .withEncodedHost("localhost")
                .withEncodedUserInformation(null)
                .build();

        assertNull(authority.getEncodedUserInformation());
    }

    @Test(expected = MalformedAuthorityException.class)
    public void testWithEncodedUserInformationThrowsMalformedAuthorityException() throws Exception {
        Authority.newBuilder()
                .withEncodedUserInformation("test@test");
    }

    @Test
    public void testWithUserInformation() throws Exception {
        Authority authority = Authority.newBuilder()
                .withEncodedHost("localhost")
                .withUserInformation("user!")
                .build();

        assertEquals("user%21", authority.getEncodedUserInformation());
    }

    @Test
    public void testWithEncodedHost() throws Exception {
        Authority authority = Authority.newBuilder()
                .withEncodedHost("localhost")
                .build();

        assertEquals("localhost", authority.getEncodedHost());
    }

    @Test(expected = MalformedAuthorityException.class)
    public void testWithEncodedHostWithNullHost() throws Exception {
        Authority.newBuilder()
                .withEncodedHost(null);
    }

    @Test(expected = MalformedAuthorityException.class)
    public void testWithEncodedThrowsMalformedHost() throws Exception {
        Authority.newBuilder()
                .withEncodedHost("test@localhost");
    }

    @Test
    public void testWithHost() throws Exception {
        Authority authority = Authority.newBuilder()
                .withHost("localhost!")
                .build();

        assertEquals(authority.getEncodedHost(), "localhost%21");
    }

    @Test
    public void testWithEmptyHost() throws Exception {
        Authority authority = Authority.newBuilder()
                .withEncodedHost("localhost")
                .withEmptyHost()
                .build();

        assertEquals("", authority.getEncodedHost());
    }

    @Test
    public void testWithPort() throws Exception {
        Authority authority = Authority.newBuilder()
                .withEncodedHost("localhost")
                .withPort(80)
                .build();

        assertNotNull(authority.getPort());
        assertEquals(80, (int) authority.getPort());
    }

    @Test(expected = MalformedAuthorityException.class)
    public void testWithPortThrowsMalformedAuthorityException() throws Exception {
        Authority.newBuilder()
                .withPort(100000);
    }

    @Test
    public void testWithoutPort() throws Exception {
        Authority authority = Authority.newBuilder()
                .withEncodedHost("localhost")
                .withPort(80)
                .build();

        authority = authority.buildUpon()
                .withoutPort()
                .build();

        assertNull(authority.getPort());
    }
}