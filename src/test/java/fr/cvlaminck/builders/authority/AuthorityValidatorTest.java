package fr.cvlaminck.builders.authority;

import fr.cvlaminck.builders.exception.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuthorityValidatorTest {

    @Test
    public void testValidateUserInformation() throws Exception {
        new AuthorityValidator().validateUserInformation("cvlaminck:uribuilder");
    }

    @Test(expected = MalformedAuthorityException.class)
    public void testValidateUserInformationThrowsMalformedAuthorityException() {
        new AuthorityValidator().validateUserInformation("cvlaminck:uri@builder");
    }

    @Test
    public void testValidateHost() throws Exception {
        AuthorityValidator validator = new AuthorityValidator();

        validator.validateHost("127.0.0.1");
        validator.validateHost("[::1]");
        validator.validateHost("localhost");
        validator.validateHost("github.com");
    }

    @Test
    public void testValidateIpLiteral() throws Exception {
        AuthorityValidator validator = new AuthorityValidator();

        validator.validateIpLiteral("[vA.a]");
    }

    @Test(expected = MalformedIpLiteralException.class)
    public void testValidateIpLiteralWithInvalidValue() {
        new AuthorityValidator().validateIpLiteral("[vA:a]");
    }

    @Test
    public void testValidateIpV6() throws Exception {
        AuthorityValidator validator = new AuthorityValidator();

        validator.validateIpLiteral("[::1]"); //FIXME add more test for ipv6
    }

    //FIXME: @Test(expected = MalformedIpV6AddressException.class)
    public void testValidateIpV6WithInvalidIpAddress() throws Exception {
        new AuthorityValidator().validateIpLiteral("[:::1]");
    }

    @Test
    public void testValidateIpV4Address() throws Exception {
        AuthorityValidator validator = new AuthorityValidator();

        validator.validateIpV4Address("127.0.0.1");
    }

    @Test(expected = MalformedIpV4AddressException.class)
    public void testValidateIpV4AddressWithInvalidValue() throws Exception {
        new AuthorityValidator().validateIpV4Address("256.255.0.0");
    }

    @Test
    public void testValidateRegisteredName() throws Exception {
        AuthorityValidator validator = new AuthorityValidator();

        validator.validateRegName("localhost");
        validator.validateRegName("github.com");
    }

    @Test(expected = MalformedRegisteredNameException.class)
    public void testValidateRegisteredNameWithInvalidValue() throws Exception {
        new AuthorityValidator().validateRegName("git@github.com");
    }
}