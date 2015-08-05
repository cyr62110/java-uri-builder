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

import fr.cvlaminck.builders.path.Path;

/**
 * Options to change the default behavior of the {@code UriParser}.
 */
public class UriParserOptions {
    /**
     * Character used to separate two segment of path in an uri.
     * This character is also used as prefix for absolute path.
     */
    public char pathSegmentSeparator;

    public static UriParserOptions getDefault() {
        UriParserOptions options = new UriParserOptions();
        options.pathSegmentSeparator = Path.pathSegmentSeparatorChar;
        return options;
    }
}
