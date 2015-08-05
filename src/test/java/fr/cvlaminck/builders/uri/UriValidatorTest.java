package fr.cvlaminck.builders.uri;

import fr.cvlaminck.builders.exception.MalformedSchemeException;
import org.junit.Test;

import static org.junit.Assert.*;

public class UriValidatorTest {

    @Test
    public void testValidateScheme() throws Exception {
        new UriValidator().validateScheme("http");
    }

    @Test(expected = MalformedSchemeException.class)
    public void testValidateSchemeThrowsMalformedSchemeException() throws Exception {
        new UriValidator().validateScheme("http:");
    }
}