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
package fr.cvlaminck.builders.query;

import fr.cvlaminck.builders.uri.encoding.UriEncoding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representation of a query parameter. A query parameter can have multiple value if its name is
 * included multiple times in the query part of the URI. ex. ?test=1&test=2.
 */
public class QueryParameter {
    private UriEncoding uriEncoding;

    private char separatorChar;
    private String name;
    private List<String> encodedValues;
    private List<String> values;

    QueryParameter(UriEncoding uriEncoding, char separatorChar, String name, List<String> values) {
        this.uriEncoding = uriEncoding;

        this.separatorChar = separatorChar;
        this.name = name;
        this.encodedValues = values;
    }

    public String getEncodedName() {
        return name;
    }

    public String getName() {
        return uriEncoding.decode(name);
    }

    public List<String> getEncodedValues() {
        return Collections.unmodifiableList(encodedValues);
    }

    public List<String> getValues() {
        if (values == null) {
            ArrayList<String> values = new ArrayList<String>();
            for (String encodedValue : encodedValues) {
                values.add(uriEncoding.decode(encodedValue));
            }
            this.values = values;
        }
        return Collections.unmodifiableList(values);
    }

    public String getEncodedValue() {
        return encodedValues.iterator().next();
    }

    public String getValue() {
        if (values != null) {
            return values.get(0);
        } else {
            return uriEncoding.decode(getEncodedValue());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryParameter that = (QueryParameter) o;

        if (!encodedValues.equals(that.encodedValues)) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + encodedValues.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String encodedValue : encodedValues) {
            if (sb.length() != 0) {
                sb.append(separatorChar);
            }
            sb.append(String.format("%s=%s", name, encodedValue));
        }
        return sb.toString();
    }
}
