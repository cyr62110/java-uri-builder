package fr.cvlaminck.builders.path;

import fr.cvlaminck.builders.exception.MalformedPathSegmentException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PathValidatorTest {

    @Test
    public void testValidate() {
        PathValidator validator = new PathValidator();

        validator.validate("Hello_World");
        validator.validate(".");
        validator.validate("..");
        validator.validate("%01");
        validator.validate("-._~!$&'()*+,;=:@");
    }

    @Test(expected = MalformedPathSegmentException.class)
    public void testValidateWithNull() {
        PathValidator validator = new PathValidator();

        validator.validate(null);
    }

    @Test(expected = MalformedPathSegmentException.class)
    public void testValidateWithMalformedPathSegment() {
        PathValidator validator = new PathValidator();

        validator.validate("Hello_World#");
    }

}