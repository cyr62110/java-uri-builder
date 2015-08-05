package fr.cvlaminck.builders.query;

import fr.cvlaminck.builders.exception.MalformedQueryException;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class QueryParametersBuilderTest {

    @Test
    public void testAppendEncodedQueryParameter() throws Exception {
        QueryParameters queryParameters = QueryParameters.newBuilder()
                .appendEncodedQueryParameter("test", "test.0")
                .build();

        assertEquals(1, queryParameters.getQueryParameterCount());
        assertNotNull(queryParameters.getQueryParameter("test"));
        assertEquals("test.0", queryParameters.getQueryParameterWithEncodedName("test").getEncodedValue());
    }

    @Test
    public void testAppendEncodedQueryParameterWithExistingName() throws Exception {
        QueryParameters queryParameters = QueryParameters.newBuilder()
                .appendEncodedQueryParameter("test", "test.0")
                .appendEncodedQueryParameter("test", "test.1")
                .appendEncodedQueryParameter("test", "test.2")
                .build();

        assertEquals(1, queryParameters.getQueryParameterCount());
        assertNotNull(queryParameters.getQueryParameter("test"));
        assertEquals(Arrays.asList("test.0", "test.1", "test.2"), queryParameters.getQueryParameterWithEncodedName("test").getEncodedValues());
    }

    @Test
    public void testAppendEncodedQueryParameterWithMultipleValue() throws Exception {
        QueryParameters queryParameters = QueryParameters.newBuilder()
                .appendEncodedQueryParameter("test", "test.0", "test.1", "test.2")
                .build();

        assertEquals(1, queryParameters.getQueryParameterCount());
        assertNotNull(queryParameters.getQueryParameterWithEncodedName("test"));
        assertEquals(Arrays.asList("test.0", "test.1", "test.2"), queryParameters.getQueryParameterWithEncodedName("test").getEncodedValues());
    }

    @Test(expected = MalformedQueryException.class)
    public void testAppendEncodedQueryParametersThrowsMalformedQueryExceptionIfNameIsInvalid() {
        QueryParameters.newBuilder()
                .appendEncodedQueryParameter("test=", "value");
    }

    @Test(expected = MalformedQueryException.class)
    public void testAppendEncodedQueryParametersThrowsMalformedQueryExceptionIfOneValueIsInvalid() {
        QueryParameters.newBuilder()
                .appendEncodedQueryParameter("test", "val&ue");
    }

    @Test
    public void testAppendQueryParameter() throws Exception {

    }

    @Test
    public void testRemoveQueryParameterWithEncodedName() throws Exception {
        QueryParameters queryParameters = QueryParameters.newBuilder()
                .appendEncodedQueryParameter("test", "test.0", "test.1")
                .appendEncodedQueryParameter("test1", "test1.0")
                .removeQueryParameterWithEncodedName("test")
                .build();

        assertEquals(1, queryParameters.getQueryParameterCount());
        assertNull(queryParameters.getQueryParameter("test"));
    }
}