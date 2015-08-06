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

import fr.cvlaminck.builders.exception.MalformedPathException;
import fr.cvlaminck.builders.uri.encoding.UriEncoding;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of the path part of an URI.
 */
public class Path {
    public static final char pathSegmentSeparatorChar = '/';

    private UriEncoding uriEncoding;

    private boolean absolute;
    private List<String> pathSegments;
    private char pathSegmentSeparator;

    private Path(UriEncoding uriEncoding, boolean absolute, char pathSegmentSeparator) {
        this.uriEncoding = uriEncoding;

        this.absolute = absolute;
        this.pathSegments = new ArrayList<String>();
        this.pathSegmentSeparator = pathSegmentSeparator;
    }

    Path(UriEncoding uriEncoding, boolean absolute, char pathSegmentSeparator, List<String> pathSegments) {
        this(uriEncoding, absolute, pathSegmentSeparator);
        this.pathSegments.addAll(pathSegments);
    }

    /**
     * Returns an absolute empty path.
     */
    private static Path rootPath = new Path(UriEncoding.getDefault(), true, Path.pathSegmentSeparatorChar);

    public static Path rootPath() {
        return rootPath;
    }

    /**
     * Returns an empty path.
     */
    private static Path emptyPath = new Path(UriEncoding.getDefault(), false, Path.pathSegmentSeparatorChar);

    public static Path emptyPath() {
        return emptyPath;
    }

    /**
     * Parse the value passed in parameter and transform it into a Path.
     *
     * @throws fr.cvlaminck.builders.exception.MalformedPathSegmentException if the value is not a valid path according to the format defined in RFC3986
     * @throws java.lang.NullPointerException                                if the value is null
     */
    public static Path parse(String sPath) {
        return parse(sPath, Path.pathSegmentSeparatorChar);
    }

    private static Path parse(String sPath, char pathSegmentSeparator) {
        if (sPath == null) {
            throw new MalformedPathException();
        }
        PathBuilder pathBuilder = newBuilder();
        pathBuilder.relative();
        int currentParsedCharacterIndex = 0;
        if (sPath.length() != 0) {
            //We parse the first character to determine if sPath is absolute
            if (sPath.charAt(currentParsedCharacterIndex) == pathSegmentSeparator) {
                pathBuilder.absolute();
                currentParsedCharacterIndex++;
            }
            //Then we parse all segments.
            while (currentParsedCharacterIndex < sPath.length()) {
                int indexOfPathSeparatorCharacter = sPath.indexOf(pathSegmentSeparatorChar, currentParsedCharacterIndex);
                if (indexOfPathSeparatorCharacter != -1) { //If it is not a trailing path separator character
                    pathBuilder.appendEncodedPathSegment(sPath.substring(currentParsedCharacterIndex, indexOfPathSeparatorCharacter));
                    currentParsedCharacterIndex = indexOfPathSeparatorCharacter + 1;
                } else {
                    pathBuilder.appendEncodedPathSegment(sPath.substring(currentParsedCharacterIndex, sPath.length()));
                    currentParsedCharacterIndex = sPath.length();
                }
            }
        }
        return pathBuilder.build();
    }

    public static PathBuilder newBuilder() {
        return new PathBuilderImpl(UriEncoding.getDefault());
    }

    public PathBuilder buildUpon() {
        return new PathBuilderImpl(uriEncoding, this);
    }

    /**
     * According to RFC3986, an absolute path starts with a slash '/' character.
     *
     * @return true if this path is no-scheme, false otherwise.
     */
    public boolean isAbsolute() {
        return absolute;
    }

    /**
     * According to RFC3986, a no-scheme path starts with a segment that does not
     * contain a colon ':' character.
     *
     * @return true if this path is no-scheme, false otherwise.
     */
    public boolean isNoScheme() {
        return false; //FIXME
    }

    /**
     * According to RFC3986, an rootless path do no start with a slash '/' character.
     *
     * @return true if this path is rootless, false otherwise.
     */
    public boolean isRootLess() {
        return !isAbsolute();
    }

    /**
     * According to RFC3986, an empty path do not have any segment.
     *
     * @return true if this path is empty, false otherwise.
     */
    public boolean isEmpty() {
        return getPathSegmentCount() == 0;
    }

    /**
     * Returns the path segment at index.
     *
     * @throws java.lang.IndexOutOfBoundsException if index is negative or greater or equals than the count of path
     *                                             segments.
     */
    public String getPathSegment(int index) {
        return uriEncoding.decode(getEncodedPathSegment(index));
    }

    /**
     * Returns the raw path segment at index.
     * This function do not decoded pct-encoded characters in returned value.
     *
     * @throws java.lang.IndexOutOfBoundsException if index is negative or greater or equals than the count of path
     *                                             segments.
     */
    public String getEncodedPathSegment(int index) {
        if (index < 0 || index >= getPathSegmentCount()) {
            throw new IndexOutOfBoundsException();
        }
        return pathSegments.get(index);
    }

    /**
     * Returns the number of path segment.
     */
    public int getPathSegmentCount() {
        return pathSegments.size();
    }

    /**
     * Returns the character used to separate path segments.
     */
    public char getPathSegmentSeparator() {
        return pathSegmentSeparator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Path path = (Path) o;

        if (absolute != path.absolute) return false;
        if (pathSegmentSeparator != path.pathSegmentSeparator) return false;
        if (!pathSegments.equals(path.pathSegments)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (absolute ? 1 : 0);
        result = 31 * result + pathSegments.hashCode();
        result = 31 * result + (int) pathSegmentSeparator;
        return result;
    }

    @Override
    public String toString() {
        return toString(FormattingOptions.defaultOptions());
    }

    public String toString(FormattingOptions options) {
        if (options == null) {
            throw new NullPointerException();
        }
        StringBuilder sb = new StringBuilder();
        if (absolute && !options.withoutRoot) {
            sb.append(pathSegmentSeparatorChar);
        }
        for (int i = 0; i < pathSegments.size(); i++) {
            if (i != 0) {
                sb.append(pathSegmentSeparatorChar);
            }
            sb.append(pathSegments.get(i));
        }
        return sb.toString();
    }

    public static class FormattingOptions {
        /**
         * Absolute path will be printed, as relative path, without the
         * first separator char at the start of the path.
         */
        public boolean withoutRoot;

        public static FormattingOptions defaultOptions() {
            FormattingOptions options = new FormattingOptions();
            options.withoutRoot = false;
            return options;
        }
    }
}
