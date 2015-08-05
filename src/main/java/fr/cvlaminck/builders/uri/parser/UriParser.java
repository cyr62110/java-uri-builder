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
package fr.cvlaminck.builders.uri.parser;

import fr.cvlaminck.builders.exception.MalformedSchemeException;
import fr.cvlaminck.builders.exception.MalformedUriException;
import fr.cvlaminck.builders.path.Path;
import fr.cvlaminck.builders.uri.Uri;
import fr.cvlaminck.builders.uri.UriBuilder;

public class UriParser {

    private UriParserOptions options;

    public UriParser() {
        this(UriParserOptions.getDefault());
    }

    public UriParser(UriParserOptions options) {
        this.options = options;
    }

    public Uri parse(String sUri) {
        if (sUri == null || sUri.isEmpty()) {
            throw new MalformedUriException();
        }
        UriBuilder builder = Uri.newBuilder();
        int currentParsedCharacterIndex = 0;
        currentParsedCharacterIndex += parseScheme(builder, sUri, currentParsedCharacterIndex);
        currentParsedCharacterIndex += parseAuthority(builder, sUri, currentParsedCharacterIndex);
        currentParsedCharacterIndex += parsePath(builder, sUri, currentParsedCharacterIndex);
        currentParsedCharacterIndex += parseQuery(builder, sUri, currentParsedCharacterIndex);
        parseFragment(builder, sUri, currentParsedCharacterIndex);
        return builder.build();
    }

    /**
     * @return Number of character parsed in the input string
     */
    private int parseScheme(UriBuilder builder, String sUri, int currentParsedCharacterIndex) {
        int indexOfSchemeSeparator = sUri.indexOf(Uri.schemeSeparator, currentParsedCharacterIndex);
        if (indexOfSchemeSeparator == -1) {
            throw new MalformedSchemeException();
        }
        builder.withScheme(sUri.substring(currentParsedCharacterIndex, indexOfSchemeSeparator));
        return indexOfSchemeSeparator + 1 - currentParsedCharacterIndex;
    }

    /**
     * @return Number of character parsed in the input string
     */
    private int parseAuthority(UriBuilder builder, String sUri, int currentParsedCharacterIndex) {
        //If the scheme separator is followed by // then we must parse an authority
        if (currentParsedCharacterIndex + 2 <= sUri.length()) {
            if (sUri.charAt(currentParsedCharacterIndex) == Uri.authorityPrefixCharacter
                    && sUri.charAt(currentParsedCharacterIndex + 1) == Uri.authorityPrefixCharacter) {
                int indexOfEndOfAuthority = sUri.indexOf(Path.pathSegmentSeparatorChar, currentParsedCharacterIndex + 2); //The end of authority may be the start of the path
                if (indexOfEndOfAuthority == -1) {
                    indexOfEndOfAuthority = sUri.indexOf(Uri.querySeparator, currentParsedCharacterIndex + 2); //Or the start of the query if we have an empty path
                }
                if (indexOfEndOfAuthority == -1) {
                    indexOfEndOfAuthority = sUri.indexOf(Uri.fragmentSeparator, currentParsedCharacterIndex + 2); //Or the start of the fragment if we have an empty path and no query
                }
                if (indexOfEndOfAuthority == -1) {
                    indexOfEndOfAuthority = sUri.length();
                }
                builder.withAuthority(sUri.substring(currentParsedCharacterIndex + 2, indexOfEndOfAuthority));
                return indexOfEndOfAuthority - currentParsedCharacterIndex;
            }
        }
        builder.withoutAuthority();
        return 0;
    }

    /**
     * @return Number of character parsed in the input string
     */
    private int parsePath(UriBuilder builder, String sUri, int currentParsedCharacterIndex) {
        if (currentParsedCharacterIndex >= sUri.length() ||
                sUri.charAt(currentParsedCharacterIndex) == Uri.querySeparator ||
                sUri.charAt(currentParsedCharacterIndex) == Uri.fragmentSeparator) {
            builder.withEmptyPath();
            return 0;
        }
        int indexOfEndOfPath = sUri.indexOf(Uri.querySeparator, currentParsedCharacterIndex);
        if (indexOfEndOfPath == -1) { //No query
            indexOfEndOfPath = sUri.indexOf(Uri.fragmentSeparator, currentParsedCharacterIndex);
        }
        if (indexOfEndOfPath == -1) { //No fragment
            indexOfEndOfPath = sUri.length();
        }
        builder.withPath(sUri.substring(currentParsedCharacterIndex, indexOfEndOfPath));
        return indexOfEndOfPath - currentParsedCharacterIndex;
    }

    /**
     * @return Number of character parsed in the input string
     */
    int parseQuery(UriBuilder builder, String sUri, int currentParsedCharacterIndex) {
        if (currentParsedCharacterIndex >= sUri.length() ||
                sUri.charAt(currentParsedCharacterIndex) != Uri.querySeparator) {
            builder.withoutQuery();
            return 0;
        }
        int indexOfFragmentSeparator = sUri.indexOf(Uri.fragmentSeparator, currentParsedCharacterIndex);
        if (indexOfFragmentSeparator == -1) {
            indexOfFragmentSeparator = sUri.length();
        }
        builder.withQuery(sUri.substring(currentParsedCharacterIndex + 1, indexOfFragmentSeparator));
        return indexOfFragmentSeparator - currentParsedCharacterIndex;
    }

    private int parseFragment(UriBuilder builder, String sUri, int currentParsedCharacterIndex) {
        if (currentParsedCharacterIndex >= sUri.length()) {
            builder.withoutFragment();
            return 0;
        }
        builder.withFragment(sUri.substring(currentParsedCharacterIndex + 1));
        return sUri.length() - currentParsedCharacterIndex;
    }

}
