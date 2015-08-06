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
        String text = "€";
        String encodedText = "%E2%82%AC";

        QueryParameters queryParameters = QueryParameters.newBuilder()
                .appendQueryParameter(text, text)
                .build();

        assertEquals(encodedText, queryParameters.getQueryParameter(text).getEncodedName());
        assertEquals(encodedText, queryParameters.getQueryParameter(text).getEncodedValue());
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

    @Test
    public void testRemoveQueryParameter() throws Exception {
        String text = "€";

        QueryParameters queryParameters = QueryParameters.newBuilder()
                .appendQueryParameter(text, "test")
                .removeQueryParameter(text)
                .build();

        assertEquals(0, queryParameters.getQueryParameterCount());
    }

    @Test
    public void testReplaceEncodedQueryParameters() throws Exception {
        QueryParameters queryParameters = QueryParameters.newBuilder()
                .appendEncodedQueryParameter("test", "test.0")
                .replaceEncodedQueryParameter("test", "test.1")
                .build();

        assertEquals(1, queryParameters.getQueryParameterCount());
        assertEquals("test.1", queryParameters.getQueryParameter("test").getEncodedValue());
    }

    @Test
    public void testReplaceEncodedQueryParametersAppend() throws Exception {
        QueryParameters queryParameters = QueryParameters.newBuilder()
                .replaceEncodedQueryParameter("test", "test.0")
                .build();

        assertEquals(1, queryParameters.getQueryParameterCount());
        assertEquals("test.0", queryParameters.getQueryParameter("test").getEncodedValue());
    }

    @Test
    public void testReplaceEncodedQueryParametersRemove() throws Exception {
        QueryParameters queryParameters = QueryParameters.newBuilder()
                .appendEncodedQueryParameter("test", "test.0")
                .replaceEncodedQueryParameter("test")
                .build();

        assertEquals(0, queryParameters.getQueryParameterCount());
    }

    @Test(expected = MalformedQueryException.class)
    public void testReplaceEncodedQueryParametersThrowsMalformedQueryExceptionIfNameIsInvalid() {
        QueryParameters.newBuilder()
                .replaceEncodedQueryParameter("test=", "value");
    }

    @Test(expected = MalformedQueryException.class)
    public void testReplaceEncodedQueryParametersThrowsMalformedQueryExceptionIfOneValueIsInvalid() {
        QueryParameters.newBuilder()
                .replaceEncodedQueryParameter("test", "val&ue");
    }

    @Test
    public void testReplaceQueryParameters() throws Exception {
        String text = "€";
        String encodedText = "%E2%82%AC";

        QueryParameters queryParameters = QueryParameters.newBuilder()
                .appendQueryParameter(text, "test.0")
                .replaceQueryParameter(text, text)
                .build();

        assertEquals(1, queryParameters.getQueryParameterCount());
        assertEquals(encodedText, queryParameters.getQueryParameter(text).getEncodedValue());
    }
}