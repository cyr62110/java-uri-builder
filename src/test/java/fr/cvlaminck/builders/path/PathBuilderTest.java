package fr.cvlaminck.builders.path;

import fr.cvlaminck.builders.exception.MalformedPathSegmentException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PathBuilderTest {

    @Test
    public void testAppendPathSegment() throws Exception {
        Path path = Path.newBuilder()
                .appendPathSegment("test!")
                .build();

        assertEquals(1, path.getPathSegmentCount());
        assertEquals("test%21", path.getEncodedPathSegment(0));
    }

    @Test
    public void testAppendEncodedPathSegment() throws Exception {
        Path path = Path.newBuilder()
                .appendEncodedPathSegment("test")
                .build();

        assertEquals(1, path.getPathSegmentCount());
        assertEquals("test", path.getEncodedPathSegment(0));
    }

    @Test(expected = MalformedPathSegmentException.class)
    public void testAppendEncodedPathSegmentThrowsMalformedPathSegmentException() throws Exception {
        Path.newBuilder()
                .appendEncodedPathSegment("a b")
                .build();
    }

    @Test
    public void testRemovePathSegment() throws Exception {
        Path path = Path.newBuilder()
                .appendEncodedPathSegment("test")
                .appendEncodedPathSegment("test1")
                .appendEncodedPathSegment("test2")
                .removePathSegment(1)
                .build();

        assertEquals(2, path.getPathSegmentCount());
        assertEquals("test", path.getPathSegment(0));
        assertEquals("test2", path.getPathSegment(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemovePathSegmentThrowsOutOfBoundException() throws Exception {
        Path.newBuilder()
                .removePathSegment(0)
                .build();
    }

    @Test
    public void testRemoveLastPathSegment() throws Exception {
        Path path = Path.newBuilder()
                .appendEncodedPathSegment("test")
                .appendEncodedPathSegment("test1")
                .removeLastPathSegment()
                .build();

        assertEquals(1, path.getPathSegmentCount());
        assertEquals("test", path.getPathSegment(0));
    }

    @Test
    public void testRemoveLastPathSegmentDoNotThrowsIfPathIsEmpty() throws Exception {
        Path path = Path.newBuilder()
                .removeLastPathSegment()
                .appendEncodedPathSegment("test")
                .build();

        assertEquals(1, path.getPathSegmentCount());
        assertEquals("test", path.getPathSegment(0));
    }

    @Test
    public void testRemoveFirstPathSegment() throws Exception {
        Path path = Path.newBuilder()
                .appendEncodedPathSegment("test")
                .appendEncodedPathSegment("test1")
                .removeFirstPathSegment()
                .build();

        assertEquals(1, path.getPathSegmentCount());
        assertEquals("test1", path.getPathSegment(0));
    }

    @Test
    public void testRemoveFirstPathSegmentDoNotThrowsIfPathIsEmpty() throws Exception {
        Path path = Path.newBuilder()
                .removeFirstPathSegment()
                .appendEncodedPathSegment("test")
                .build();

        assertEquals(1, path.getPathSegmentCount());
        assertEquals("test", path.getPathSegment(0));
    }
}