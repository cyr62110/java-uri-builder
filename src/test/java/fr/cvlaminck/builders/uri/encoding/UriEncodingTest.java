package fr.cvlaminck.builders.uri.encoding;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UriEncodingTest {

    /**
     * This test must be executed first to ensure that the default charset used by the library
     * is not modified before the execution of this test. This is why the class is annotated with
     * FixMethodOrder and prefixed with a
     */
    @Test
    public void aTestGetDefaultUsesUTF8Charset() throws Exception {
        String text = "€";
        String encodedText = "%E2%82%AC";

        assertEquals(encodedText, UriEncoding.getDefault().encode(text));
    }

    /**
     * This test is prefixed with b so we ensure that this test is never executed before the
     * previous one.
     */
    @Test
    public void bTestSetDefaultCharset() throws Exception {
        String text = "€";
        String encodedText = "%FE%FF%20%AC";

        UriEncoding.setDefaultCharset("UTF-16");
        String encodedUsingJavaNet = URLEncoder.encode(text, "UTF-16");
        String encodedByUriEncoding = UriEncoding.getDefault().encode(text);

        assertEquals(encodedText, encodedByUriEncoding);
        assertEquals(encodedUsingJavaNet, encodedByUriEncoding);

        //Reset the default charset to avoid side effect
        UriEncoding.setDefaultCharset(UriEncoding.DEFAULT_CHARSET);
    }

    @Test
    public void testEncode() throws Exception {
        String text = "This text must be encoded!";
        String encodedText = "This+text+must+be+encoded%21";

        String encodedUsingJavaNet = URLEncoder.encode(text, "UTF-8");
        String encodedByUriEncoding = new UriEncoding(Charset.forName("UTF-8")).encode(text);

        assertEquals(encodedText, encodedByUriEncoding);
        assertEquals(encodedUsingJavaNet, encodedByUriEncoding);
    }

    @Test
    public void testEncodeWithUTF8Character() throws Exception {
        String text = "42 €";
        String encodedText = "42+%E2%82%AC";

        String encodedUsingJavaNet = URLEncoder.encode(text, "UTF-8");
        String encodedByUriEncoding = new UriEncoding(Charset.forName("UTF-8")).encode(text);

        assertEquals(encodedText, encodedByUriEncoding);
        assertEquals(encodedUsingJavaNet, encodedByUriEncoding);
    }

    //@Test
    public void testEncodePerformance() throws Exception {
        long numberOfEncode = 1000000;
        String text = "This text must be encoded!";

        long startTimeJavaNet = System.currentTimeMillis();
        for (long i = 0; i < numberOfEncode; i++) {
            URLEncoder.encode(text, "UTF-8");
        }
        long timeJavaNet = System.currentTimeMillis() - startTimeJavaNet;

        long startTimeUriEncoding = System.currentTimeMillis();
        UriEncoding uriEncoding = new UriEncoding(Charset.forName("UTF-8"));
        for (long i = 0; i < numberOfEncode; i++) {
            uriEncoding.encode(text);
        }
        long timeUriEncoding = System.currentTimeMillis() - startTimeUriEncoding;

        assertEquals(timeJavaNet, timeUriEncoding);
    }

    @Test
    public void testDecode() throws Exception {
        String text = "This+text+is+encoded%21";
        String decodedText = "This text is encoded!";

        String decodedUsingJavaNet = URLDecoder.decode(text, "UTF-8");
        String decodedByUriEncoding = new UriEncoding(Charset.forName("UTF-8")).decode(text);

        assertEquals(decodedText, decodedByUriEncoding);
        assertEquals(decodedUsingJavaNet, decodedByUriEncoding);
    }

    @Test
    public void testDecodeWithUTF8Character() throws Exception {
        String text = "42+%E2%82%AC";
        String decodedText = "42 €";

        String decodedUsingJavaNet = URLDecoder.decode(text, "UTF-8");
        String decodedByUriEncoding = new UriEncoding(Charset.forName("UTF-8")).decode(text);

        assertEquals(decodedText, decodedByUriEncoding);
        assertEquals(decodedUsingJavaNet, decodedByUriEncoding);
    }

    //@Test
    public void testDecodePerformance() throws Exception {
        long numberOfDecode = 1000000;
        String text = "This+text+is+encoded%21";

        long startTimeJavaNet = System.currentTimeMillis();
        for (long i = 0; i < numberOfDecode; i++) {
            URLDecoder.decode(text, "UTF-8");
        }
        long timeJavaNet = System.currentTimeMillis() - startTimeJavaNet;

        long startTimeUriEncoding = System.currentTimeMillis();
        UriEncoding uriEncoding = new UriEncoding(Charset.forName("UTF-8"));
        for (long i = 0; i < numberOfDecode; i++) {
            uriEncoding.decode(text);
        }
        long timeUriEncoding = System.currentTimeMillis() - startTimeUriEncoding;

        assertEquals(timeJavaNet, timeUriEncoding);
    }
}