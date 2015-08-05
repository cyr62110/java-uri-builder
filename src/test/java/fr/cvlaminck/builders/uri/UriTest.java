package fr.cvlaminck.builders.uri;

import fr.cvlaminck.builders.authority.Authority;
import fr.cvlaminck.builders.path.Path;
import fr.cvlaminck.builders.query.QueryParameters;
import fr.cvlaminck.builders.uri.encoding.UriEncoding;
import org.junit.Test;

import static org.junit.Assert.*;

public class UriTest {

    @Test
    public void testToString() throws Exception {
        Uri uri = new Uri(UriEncoding.getDefault(), "file", null, Path.rootPath(), (String)null, null);
        assertEquals("file:/", uri.toString());

        uri = new Uri(UriEncoding.getDefault(), "file", Authority.emptyHost(), Path.rootPath(), (String)null, null);
        assertEquals("file:///", uri.toString());

        uri = new Uri(UriEncoding.getDefault(), "http", Authority.parse("localhost"), Path.rootPath(), (String)null, null);
        assertEquals("http://localhost/", uri.toString());

        uri = new Uri(UriEncoding.getDefault(), "http", Authority.parse("localhost"), Path.parse("/test"), (String)null, null);
        assertEquals("http://localhost/test", uri.toString());

        uri = new Uri(UriEncoding.getDefault(), "http", Authority.parse("localhost"), Path.parse("/test"), "", null);
        assertEquals("http://localhost/test", uri.toString());

        uri = new Uri(UriEncoding.getDefault(), "http", Authority.parse("localhost"), Path.parse("/test"), "query", null);
        assertEquals("http://localhost/test?query", uri.toString());

        uri = new Uri(UriEncoding.getDefault(), "http", Authority.parse("localhost"), Path.parse("/test"), QueryParameters.parse("test=test"), null);
        assertEquals("http://localhost/test?test=test", uri.toString());

        uri = new Uri(UriEncoding.getDefault(), "http", Authority.parse("localhost"), Path.parse("/test"), "query", "fragment");
        assertEquals("http://localhost/test?query#fragment", uri.toString());
    }
}