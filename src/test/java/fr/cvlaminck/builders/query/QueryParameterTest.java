package fr.cvlaminck.builders.query;

import fr.cvlaminck.builders.uri.encoding.UriEncoding;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class QueryParameterTest {

    @Test
    public void testGetEncodedName() throws Exception {
        QueryParameter queryParameter = new QueryParameter(
                UriEncoding.getDefault(),
                QueryParameters.queryParameterSeparatorChar,
                "NAME",
                new ArrayList<String>());

        assertEquals("NAME", queryParameter.getEncodedName());
    }

    @Test
    public void testGetName() throws Exception {
        QueryParameter queryParameter = new QueryParameter(
                UriEncoding.getDefault(),
                QueryParameters.queryParameterSeparatorChar,
                "NAME%21",
                new ArrayList<String>());

        assertEquals("NAME!", queryParameter.getName());
    }

    @Test
    public void testGetEncodedValues() throws Exception {
        List<String> encodedValues = Arrays.asList("test", "test2");
        QueryParameter queryParameter = new QueryParameter(
                UriEncoding.getDefault(),
                QueryParameters.queryParameterSeparatorChar,
                "NAME",
                encodedValues);

        assertNotSame(encodedValues, queryParameter.getEncodedValues());
        assertEquals(encodedValues, queryParameter.getEncodedValues());
    }

    @Test
    public void testGetValues() throws Exception {
        QueryParameter queryParameter = new QueryParameter(
                UriEncoding.getDefault(),
                QueryParameters.queryParameterSeparatorChar,
                "NAME",
                Arrays.asList("test%21", "test2%21"));

        assertEquals(Arrays.asList("test!", "test2!"), queryParameter.getValues());
        //We also test that we are caching the right values.
        assertEquals(Arrays.asList("test!", "test2!"), queryParameter.getValues());
    }

    @Test
    public void testGetEncodedValue() throws Exception {
        QueryParameter queryParameter = new QueryParameter(
                UriEncoding.getDefault(),
                QueryParameters.queryParameterSeparatorChar,
                "NAME",
                Arrays.asList("test", "test2"));

        assertEquals("test", queryParameter.getEncodedValue());
    }

    @Test
    public void testGetValue() throws Exception {
        QueryParameter queryParameter = new QueryParameter(
                UriEncoding.getDefault(),
                QueryParameters.queryParameterSeparatorChar,
                "NAME",
                Arrays.asList("test%21", "test2%21"));

        assertEquals("test!", queryParameter.getValue());
    }

    @Test
    public void testGetValueWithCache() throws Exception {
        QueryParameter queryParameter = new QueryParameter(
                UriEncoding.getDefault(),
                QueryParameters.queryParameterSeparatorChar,
                "NAME",
                Arrays.asList("test%21", "test2%21"));

        //We create a cache of decoded values
        queryParameter.getValues();
        //And we extract one value from it.
        assertEquals("test!", queryParameter.getValue());
    }

    @Test
    public void testToString() throws Exception {
        QueryParameter queryParameter = new QueryParameter(
                UriEncoding.getDefault(),
                QueryParameters.queryParameterSeparatorChar,
                "NAME",
                Arrays.asList("test", "test2"));

        assertEquals("NAME=test&NAME=test2", queryParameter.toString());
    }
}