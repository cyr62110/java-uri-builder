package fr.cvlaminck.builders.path;

import fr.cvlaminck.builders.exception.MalformedPathException;
import fr.cvlaminck.builders.exception.MalformedPathSegmentException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PathTest {

    @Test
    public void testRootPath() throws Exception {
        Path rootPath = Path.rootPath();

        assertTrue(rootPath.isAbsolute());
        assertEquals(0, rootPath.getPathSegmentCount());
    }

    @Test
    public void testEmptyPath() throws Exception {
        Path emptyPath = Path.emptyPath();

        assertFalse(emptyPath.isAbsolute());
        assertEquals(0, emptyPath.getPathSegmentCount());
    }

    @Test
    public void testGetEncodedPathSegment() throws Exception {
        Path path = Path.parse("/test%21");
        assertEquals("test%21", path.getEncodedPathSegment(0));
    }

    @Test
    public void testGetPathSegment() throws Exception {
        Path path = Path.parse("/test%21");
        assertEquals("test!", path.getPathSegment(0));
    }

    @Test
    public void testParse() throws Exception {
        Path path = Path.parse("");
        assertFalse(path.isAbsolute());
        assertTrue(path.isEmpty());

        path = Path.parse("/");
        assertTrue(path.isAbsolute());
        assertTrue(path.isEmpty());

        path = Path.parse("test/test1");
        assertFalse(path.isAbsolute());
        assertEquals(2, path.getPathSegmentCount());
        assertEquals("test", path.getPathSegment(0));
        assertEquals("test1", path.getPathSegment(1));

        path = Path.parse("/test/test1");
        assertTrue(path.isAbsolute());
        assertEquals(2, path.getPathSegmentCount());
        assertEquals("test", path.getPathSegment(0));
        assertEquals("test1", path.getPathSegment(1));

        path = Path.parse("/test/");
        assertEquals(1, path.getPathSegmentCount());
        assertEquals("test", path.getPathSegment(0));
    }

    @Test(expected = MalformedPathSegmentException.class)
    public void testParseThrowsWithDoublePathSegmentSeparator() throws Exception {
        Path.parse("//test");
    }

    @Test(expected = MalformedPathSegmentException.class)
    public void testParseThrowsWithEmptyPathSegment() throws Exception {
        Path.parse("/test//test2");
    }

    @Test(expected = MalformedPathException.class)
    public void testParseWithNullPath() {
        Path.parse(null);
    }

    @Test
    public void testAsRelative() {
        Path originalRelativePath = Path.newBuilder()
                .relative()
                .appendPathSegment("test")
                .build();

        Path path1 = originalRelativePath.asRelative();
        assertEquals(originalRelativePath, path1);
        assertNotSame(originalRelativePath, path1);

        Path originalAbsolutePath = Path.newBuilder()
                .absolute()
                .appendPathSegment("test")
                .build();

        Path path2 = originalAbsolutePath.asRelative();
        assertEquals("test", path2.toString());
    }

    @Test
    public void testAsAbsolute() {
        Path originalRelativePath = Path.newBuilder()
                .relative()
                .appendPathSegment("test")
                .build();

        Path path1 = originalRelativePath.asAbsolute();
        assertEquals("/test", path1.toString());

        Path originalAbsolutePath = Path.newBuilder()
                .absolute()
                .appendPathSegment("test")
                .build();

        Path path2 = originalAbsolutePath.asAbsolute();
        assertEquals(originalAbsolutePath, path2);
        assertNotSame(originalAbsolutePath, path2);
    }

    @Test
    public void testToStringAbsolutePath() throws Exception {
        Path path = Path.newBuilder()
                .absolute()
                .appendEncodedPathSegment("test")
                .appendEncodedPathSegment("test1")
                .build();

        assertEquals("/test/test1", path.toString());
    }

    @Test
    public void testToStringRelativePath() throws Exception {
        Path path = Path.newBuilder()
                .relative()
                .appendEncodedPathSegment("test")
                .appendEncodedPathSegment("test1")
                .build();

        assertEquals("test/test1", path.toString());
    }

    @Test
    public void testToStringWithoutRoot() throws Exception {
        Path path = Path.newBuilder()
                .absolute()
                .appendEncodedPathSegment("test")
                .build();

        Path.FormattingOptions options = new Path.FormattingOptions();
        options.withoutRoot = true;

        assertEquals("test", path.toString(options));
    }

    @Test
    public void testToStringNonEncoded() throws Exception {
        Path path = Path.newBuilder()
                .absolute()
                .appendPathSegment("test test")
                .build();

        Path.FormattingOptions options = new Path.FormattingOptions();
        options.nonEncoded = true;

        assertEquals("/test test", path.toString(options));
    }

    @Test
    public void testEquals() throws Exception {
        PathBuilder builder = Path.newBuilder()
                .absolute()
                .appendEncodedPathSegment("test");

        assertTrue(Path.parse("/test").equals(builder.build()));
        assertFalse(Path.parse("test").equals(builder.build()));
        assertFalse(Path.parse("/test2").equals(builder.build()));
        assertFalse(Path.parse("test2").equals(builder.build()));
    }
}
