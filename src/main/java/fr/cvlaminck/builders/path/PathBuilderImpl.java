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

import fr.cvlaminck.builders.uri.encoding.UriEncoding;

import java.util.ArrayList;
import java.util.List;

class PathBuilderImpl
        implements PathBuilder {

    private UriEncoding uriEncoding;

    private PathValidator pathValidator;

    private char pathSegmentSeparatorCharacter;
    private List<String> pathSegments;
    private boolean absolute;

    PathBuilderImpl(UriEncoding uriEncoding, Path path) {
        this(uriEncoding);
        for (int i = 0; i < path.getPathSegmentCount(); i++) {
            pathSegments.add(path.getPathSegment(i));
        }
        this.absolute = path.isAbsolute();
        this.pathSegmentSeparatorCharacter = path.getPathSegmentSeparator();
    }

    PathBuilderImpl(UriEncoding uriEncoding) {
        this.uriEncoding = uriEncoding;
        this.pathValidator = new PathValidator();
        this.pathSegments = new ArrayList<String>();
        this.absolute = true;
        this.pathSegmentSeparatorCharacter = Path.pathSegmentSeparatorChar;
    }

    @Override
    public PathBuilder absolute() {
        absolute = true;
        return this;
    }

    @Override
    public PathBuilder relative() {
        absolute = false;
        return this;
    }

    @Override
    public PathBuilder appendPathSegment(String pathSegment) {
        return appendEncodedPathSegment(uriEncoding.encode(pathSegment));
    }

    @Override
    public PathBuilder appendEncodedPathSegment(String pathSegment) {
        pathValidator.validate(pathSegment);
        pathSegments.add(pathSegment);
        return this;
    }

    @Override
    public PathBuilder removePathSegment(int index) {
        if (index < 0 || index >= pathSegments.size()) {
            throw new IndexOutOfBoundsException();
        }
        pathSegments.remove(index);
        return this;
    }

    @Override
    public PathBuilder removeLastPathSegment() {
        if (!pathSegments.isEmpty()) {
            pathSegments.remove(pathSegments.size() - 1);
        }
        return this;
    }

    @Override
    public PathBuilder removeFirstPathSegment() {
        if (!pathSegments.isEmpty()) {
            pathSegments.remove(0);
        }
        return this;
    }

    @Override
    public Path build() {
        return new Path(uriEncoding, absolute, pathSegmentSeparatorCharacter, pathSegments);
    }

}
