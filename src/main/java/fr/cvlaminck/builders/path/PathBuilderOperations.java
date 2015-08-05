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

/**
 * Interface defining all operations available to manipulate and construct the path of an uri.
 */
public interface PathBuilderOperations<Builder> {

    /**
     * Append a new segment at the end of the path constructed using the builder.
     * The segment will be encoded before being appended to the path.
     *
     * @param pathSegment Must respect the format defined in RFC3986.
     * @return the current PathBuilder
     * @throws fr.cvlaminck.builders.exception.MalformedPathSegmentException if pathSegment is empty.
     * @throws java.lang.NullPointerException                                if pathSegment is null.
     */
    public Builder appendPathSegment(String pathSegment);

    /**
     * Append a new segment at the end of the path constructed using the builder.
     * The segment must already be encoded if it contains non-valid character according to RFC3986.
     *
     * @param pathSegment Must respect the format defined in RFC3986.
     * @return the current PathBuilder
     * @throws fr.cvlaminck.builders.exception.MalformedPathSegmentException if pathSegment parameter
     *                                                                       do not respect the format defined in RFC3986. An empty pathSegment is not valid and will throw this exception.
     * @throws java.lang.NullPointerException                                if pathSegment is null.
     */
    public Builder appendEncodedPathSegment(String pathSegment);

    /**
     * Remove the i-th segment.
     *
     * @throws java.lang.IndexOutOfBoundsException thrown if the index is greater or equals to the number of segments.
     */
    public Builder removePathSegment(int index);

    /**
     * Remove the last segment. Do nothing if the path is empty.
     */
    public Builder removeLastPathSegment();

    /**
     * Remove the first segment. Do nothing if the path is empty.
     */
    public Builder removeFirstPathSegment();
}
