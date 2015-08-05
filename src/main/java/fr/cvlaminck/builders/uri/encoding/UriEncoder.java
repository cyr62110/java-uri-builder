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

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

class UriEncoder {
    private final static String hexEncodingCharacters = "0123456789ABCDEF";

    private Charset charset;

    UriEncoder(Charset charset) {
        this.charset = charset;
    }

    private List<CharacterSpan> nonEncodedCharacters = Arrays.asList(
            CharacterSpan.newRange('a', 'z'),
            CharacterSpan.newRange('A', 'Z'),
            CharacterSpan.newRange('0', '9'),
            CharacterSpan.newSingleCharacter('.'),
            CharacterSpan.newSingleCharacter('-'),
            CharacterSpan.newSingleCharacter('_'),
            CharacterSpan.newSingleCharacter('~')
    );

    public String encode(String input) {
        if (input == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer(input.length());
        int numberOfEncodedCharacters = 0;
        while (numberOfEncodedCharacters < input.length()) {
            char character = input.charAt(numberOfEncodedCharacters);
            if (shouldEncodeCharacter(character)) {
                sb.append(encodeCharacter(character));
            } else {
                sb.append(character);
            }
            numberOfEncodedCharacters++;
        }
        return sb.toString();
    }

    private boolean shouldEncodeCharacter(char character) {
        for (CharacterSpan span : nonEncodedCharacters) {
            if (span.contains(character)) {
                return false;
            }
        }
        return true;
    }

    public String encodeCharacter(char input) {
        String encodedCharacter = encodeSpecialCharacter(input);
        if (encodedCharacter == null) {
            encodedCharacter = encodeSimpleCharacter(input);
        }
        return encodedCharacter;
    }

    private String encodeSpecialCharacter(char input) {
        switch (input) {
            case ' ':
                return "+";
        }
        return null;
    }

    private String encodeSimpleCharacter(char input) {
        byte[] bytes = Character.toString(input).getBytes(charset);
        StringBuffer sb = new StringBuffer(3 * bytes.length);
        for (byte b : bytes) {
            sb.append(UriEncoding.ENCODED_CHARACTER_PREFIX);
            sb.append(hexEncodingCharacters.charAt((b >> 4) & 0x0F));
            sb.append(hexEncodingCharacters.charAt(b & 0xF));
        }
        return sb.toString();
    }


}
