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
package fr.cvlaminck.builders.path;

import fr.cvlaminck.builders.exception.MalformedPathSegmentException;
import fr.cvlaminck.builders.uri.UriValidator;

import java.util.regex.Pattern;

public class PathValidator {
    private Pattern pathSegmentPattern;

    public PathValidator() {
        pathSegmentPattern = Pattern.compile("(" + UriValidator.PCHAR + ")+");
    }

    /**
     * Validate if the value passed as parameter is a valid path segment according to RFC3986 or
     * throws an MalformedPathSegmentException.
     * <p/>
     * Format defined in RFC is : <p/>
     * segment       = *pchar <p/>
     * pchar         = unreserved / pct-encoded / sub-delims / ":" / "@" <p/>
     *
     * @throws MalformedPathSegmentException if the value passed as parameter is not a valid path segment.
     */
    public void validate(String pathSegment) throws MalformedPathSegmentException {
        if (pathSegment == null) {
            throw new MalformedPathSegmentException();
        }
        if (!pathSegmentPattern.matcher(pathSegment).matches()) {
            throw new MalformedPathSegmentException();
        }
    }

}
