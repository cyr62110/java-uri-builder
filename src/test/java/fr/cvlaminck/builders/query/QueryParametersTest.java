package fr.cvlaminck.builders.query;

import fr.cvlaminck.builders.uri.encoding.UriEncoding;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class QueryParametersTest {

    @Test
    public void testParse() throws Exception {
        QueryParameters queryParameters = null;

        queryParameters = QueryParameters.parse("test=test.0&test1=test1.0");
        assertEquals("test.0", queryParameters.getQueryParameter("test").getEncodedValue());
        assertEquals("test1.0", queryParameters.getQueryParameter("test1").getEncodedValue());

        queryParameters = QueryParameters.parse("test=");
        assertEquals("", queryParameters.getQueryParameter("test").getEncodedValue());

        queryParameters = QueryParameters.parse("test=&test1=test1.0");
        assertEquals("", queryParameters.getQueryParameter("test").getEncodedValue());
        assertEquals("test1.0", queryParameters.getQueryParameter("test1").getEncodedValue());

        queryParameters = QueryParameters.parse("test=test.0&test=test.1&test=test.2");
        assertEquals(Arrays.asList("test.0", "test.1", "test.2"), queryParameters.getQueryParameter("test").getEncodedValues());
    }

    @Test
    public void testBuildUpon() throws Exception {
        Map<String, List<String>> rawParameters = new HashMap<String, List<String>>();
        rawParameters.put("test", Arrays.asList("test.0", "test.1"));
        rawParameters.put("test2", Arrays.asList("test2.0"));
        QueryParameters queryParameters = new QueryParameters(UriEncoding.getDefault(),
                QueryParameters.queryParameterSeparatorChar,
                rawParameters);

        assertNotSame(queryParameters, queryParameters.buildUpon().build());
        assertEquals(queryParameters, queryParameters.buildUpon().build());
    }

    @Test
    public void testGetQueryParameterCount() throws Exception {
        Map<String, List<String>> rawParameters = new HashMap<String, List<String>>();
        rawParameters.put("test", Arrays.asList("test.0", "test.1"));
        rawParameters.put("test2", Arrays.asList("test2.0"));
        QueryParameters queryParameters = new QueryParameters(UriEncoding.getDefault(),
                QueryParameters.queryParameterSeparatorChar,
                rawParameters);

        assertEquals(2, queryParameters.getQueryParameterCount());
    }

    @Test
    public void testGetQueryParameters() throws Exception {
        Map<String, List<String>> rawParameters = new HashMap<String, List<String>>();
        rawParameters.put("test", Arrays.asList("test.0", "test.1"));
        rawParameters.put("test2", Arrays.asList("test2.0"));
        QueryParameters queryParameters = new QueryParameters(UriEncoding.getDefault(),
                QueryParameters.queryParameterSeparatorChar,
                rawParameters);

        Set<QueryParameter> params = queryParameters.getQueryParameters();
        Iterator<QueryParameter> it = params.iterator();

        assertEquals(2, params.size());
        assertEquals("test", it.next().getEncodedName());
        assertEquals("test2.0", it.next().getEncodedValues().get(0));
    }

    @Test
    public void testGetQueryParameterWithEncodedName() throws Exception {
        Map<String, List<String>> rawParameters = new HashMap<String, List<String>>();
        rawParameters.put("test", Arrays.asList("test.0", "test.1"));
        QueryParameters queryParameters = new QueryParameters(UriEncoding.getDefault(),
                QueryParameters.queryParameterSeparatorChar,
                rawParameters);

        assertEquals("test", queryParameters.getQueryParameter("test").getEncodedName());
        assertEquals(rawParameters.get("test"), queryParameters.getQueryParameter("test").getEncodedValues());
    }

    @Test
    public void testToString() throws Exception {
        Map<String, List<String>> rawParameters = new HashMap<String, List<String>>();
        rawParameters.put("test", Arrays.asList("test.0", "test.1"));
        rawParameters.put("test2", Arrays.asList("test2.0"));
        QueryParameters queryParameters = new QueryParameters(
                UriEncoding.getDefault(),
                QueryParameters.queryParameterSeparatorChar,
                rawParameters);

        assertEquals("test2=test2.0&test=test.0&test=test.1", queryParameters.toString());
    }
}