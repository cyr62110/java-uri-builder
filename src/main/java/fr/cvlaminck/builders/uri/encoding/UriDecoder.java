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

class UriDecoder {
    private Charset charset;

    public UriDecoder(Charset charset) {
        this.charset = charset;
    }

    public String decode(String input) {
        if (input == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer(input.length());
        int numberOfDecodedCharacters = 0;
        while (numberOfDecodedCharacters < input.length()) {
            char character = input.charAt(numberOfDecodedCharacters);
            if (character == UriEncoding.ENCODED_CHARACTER_PREFIX) {
                numberOfDecodedCharacters += decodeEncodedCharacters(sb, input, numberOfDecodedCharacters);
            } else {
                sb.append(decodeSpecialCharacter(character));
                numberOfDecodedCharacters++;
            }
        }

        return sb.toString();
    }

    public char decodeSpecialCharacter(char c) {
        switch (c) {
            case '+':
                return ' ';
        }
        return c;
    }

    public int decodeEncodedCharacters(StringBuffer output, String input, int index) {
        int numberOfBytes = 0;
        while (index + numberOfBytes * 3 < input.length() && input.charAt(index + numberOfBytes * 3) == UriEncoding.ENCODED_CHARACTER_PREFIX) {
            numberOfBytes++;
        }
        if (numberOfBytes == 0) {
            return 0;
        }
        byte[] encodedCharacterBytes = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
            encodedCharacterBytes[i] = (byte) (hexToInt(input.charAt(index + i * 3 + 1)) << 4 | hexToInt(input.charAt(index + i * 3 + 2)));
        }
        output.append(new String(encodedCharacterBytes, charset));
        return numberOfBytes * 3;
    }

    public int hexToInt(char c) {
        if (c >= 'a' && c <= 'z') {
            return c - 'a' + 10;
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 10;
        }
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        throw new IllegalArgumentException("'" + c + "' is not a valid hexadecimal character");
    }

}
