/**
 * Copyright 2015 Cyril Vlaminck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.cvlaminck.builders.uri.encoding;

import fr.cvlaminck.builders.exception.MissingCharsetException;

import java.nio.charset.Charset;

public class UriEncoding {
    public static final char ENCODED_CHARACTER_PREFIX = '%';
    public static final String DEFAULT_CHARSET = "UTF-8";

    private Charset charset;
    private static String defaultCharsetName = DEFAULT_CHARSET;

    private UriEncoder encoder = null;
    private UriDecoder decoder = null;

    private UriEncoder getEncoder() {
        if (encoder == null) {
            synchronized (this) {
                if (encoder == null) {
                    encoder = new UriEncoder(charset);
                }
            }
        }
        return encoder;
    }

    private UriDecoder getDecoder() {
        if (decoder == null) {
            synchronized (this) {
                if (decoder == null) {
                    decoder = new UriDecoder(charset);
                }
            }
        }
        return decoder;
    }

    public UriEncoding(Charset charset) {
        if (charset == null) {
            throw new NullPointerException();
        }
        this.charset = charset;
    }

    /**
     * Returns an UriEncoding encoding/decoding using the default charset. By default, the default charset is UTF-8 but
     * may be changed using {@code setDefaultCharset}.
     *
     * @throws fr.cvlaminck.builders.exception.MissingCharsetException thrown if the default charset is not available on the platform
     */
    public static UriEncoding getDefault() {
        Charset defaultCharset = Charset.forName(defaultCharsetName);
        if (defaultCharset == null) {
            throw new MissingCharsetException(defaultCharsetName);
        }
        return new UriEncoding(defaultCharset);
    }

    /**
     * Set the default charset that will be used through all the library when encoding/decoding values.
     *
     * @param defaultCharset Name of the charset to use as default
     * @throws fr.cvlaminck.builders.exception.MissingCharsetException thrown if charset is not available on the platform
     */
    public static void setDefaultCharset(String defaultCharset) {
        Charset charset = Charset.forName(defaultCharsetName);
        if (charset == null) {
            throw new MissingCharsetException(defaultCharset);
        }
        defaultCharsetName = defaultCharset;
    }

    /**
     * Encode the input using the charset set for this instance of UriEncoding.
     */
    public String encode(String input) {
        return getEncoder().encode(input);
    }

    /**
     * Decode the input using the charset set for this instance of UriEncoding.
     */
    public String decode(String input) {
        return getDecoder().decode(input);
    }
}
