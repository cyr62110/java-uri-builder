package fr.cvlaminck.builders.uri;

import fr.cvlaminck.builders.authority.Authority;
import fr.cvlaminck.builders.exception.MalformedFragmentException;
import fr.cvlaminck.builders.exception.MalformedPathException;
import fr.cvlaminck.builders.exception.MalformedQueryException;
import fr.cvlaminck.builders.exception.MalformedSchemeException;
import fr.cvlaminck.builders.path.Path;
import fr.cvlaminck.builders.query.QueryParameters;
import org.junit.Test;

import static org.junit.Assert.*;

public class UriBuilderUriBuilderOperationsTest {

    @Test
    public void testWithScheme() throws Exception {
        Uri uri = Uri.newBuilder()
                .withScheme("http")
                .withAuthority("localhost")
                .build();

        assertEquals("http", uri.getScheme());
    }

    @Test(expected = MalformedSchemeException.class)
    public void testWithMalformedScheme() throws Exception {
        Uri.newBuilder()
                .withScheme("http:");
    }

    @Test
    public void testWithAuthority() throws Exception {
        Uri uri = Uri.newBuilder()
                .withScheme("http")
                .withAuthority("localhost")
                .build();

        assertEquals("localhost", uri.getAuthority().getEncodedHost());
    }

    @Test
    public void testWithoutAuthority() throws Exception {
        Uri uri = Uri.newBuilder()
                .withScheme("http")
                .withAuthority("localhost")
                .withoutAuthority()
                .build();

        assertNull(uri.getAuthority());
    }

    @Test
    public void testWithPath() throws Exception {
        Uri uri = Uri.newBuilder()
                .withScheme("http")
                .withPath("test")
                .build();

        assertEquals("test", uri.getPath().getPathSegment(0));
    }

    @Test(expected = MalformedPathException.class)
    public void testWithPathNull() throws Exception {
        Uri.newBuilder()
                .withScheme("http")
                .withPath((Path) null);
    }

    @Test
    public void testWithEmptyPath() throws Exception {
        Uri uri = Uri.newBuilder()
                .withScheme("http")
                .withEmptyPath()
                .build();

        assertTrue(uri.getPath().isEmpty());
    }

    @Test
    public void testWithEncodedQuery() throws Exception {
        Uri uri = Uri.newBuilder()
                .withScheme("http")
                .withEncodedQuery("query")
                .build();

        assertEquals("query", uri.getEncodedQuery());
    }

    @Test
    public void testWithEncodedQueryNull() throws Exception {
        Uri uri = Uri.newBuilder()
                .withScheme("http")
                .withEncodedQuery(null)
                .build();

        assertNull(uri.getEncodedQuery());
    }

    @Test
    public void testWithoutQuery() throws Exception {
        Uri uri = Uri.newBuilder()
                .withScheme("http")
                .withEncodedQuery("query")
                .withoutQuery()
                .build();

        assertNull(uri.getEncodedQuery());
    }

    @Test(expected = MalformedQueryException.class)
    public void testWithMalformedEncodedQuery() throws Exception {
        Uri.newBuilder()
                .withEncodedQuery("é")
                .build();
    }

    @Test
    public void testWithQueryParameters() throws Exception {
        QueryParameters queryParameters = QueryParameters.newBuilder()
                .appendEncodedQueryParameter("test", "test")
                .build();

        Uri uri = Uri.newBuilder()
                .withScheme("http")
                .withQueryParameters(queryParameters)
                .build();

        assertNotNull(uri.getQueryParameters());
        assertEquals("test", uri.getQueryParameters().getQueryParameter("test").getEncodedValue());
    }

    @Test
    public void testWithQueryParametersNull() throws Exception {
        Uri uri = Uri.newBuilder()
                .withScheme("http")
                .withQueryParameters((QueryParameters) null)
                .build();

        assertNull(uri.getQueryParameters());
    }

    @Test
    public void testWithEncodedFragment() throws Exception {
        Uri uri = Uri.newBuilder()
                .withScheme("http")
                .withEncodedFragment("fragment")
                .build();

        assertEquals("fragment", uri.getEncodedFragment());
    }

    @Test
    public void testWithEncodedFragmentNull() throws Exception {
        Uri uri = Uri.newBuilder()
                .withScheme("http")
                .withEncodedFragment(null)
                .build();

        assertNull(uri.getEncodedFragment());
    }

    @Test(expected = MalformedFragmentException.class)
    public void testWithMalformedEncodedFragment() throws Exception {
        Uri.newBuilder()
                .withEncodedFragment("é")
                .build();
    }

    @Test
    public void testWithoutFragment() throws Exception {
        Uri uri = Uri.newBuilder()
                .withScheme("http")
                .withEncodedFragment("fragment")
                .withEncodedFragment(null)
                .build();

        assertNull(uri.getEncodedFragment());
    }

    //Special cases

    @Test(expected = MalformedPathException.class)
    public void testWithSlashAndPathStartingWithDoubleSlash() throws Exception {
        Uri.newBuilder()
                .withScheme("http")
                .withAuthority("localhost")
                .withPath("//test")
                .build();
    }

    @Test
    public void testBuildWithEmptyAuthorityAndEmptyPath() throws Exception {
        Uri uri = Uri.newBuilder()
                .withScheme("file")
                .withAuthority(Authority.emptyHost())
                .withEmptyPath()
                .build();

        assertEquals("file:///", uri.toString());
    }
}