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

import fr.cvlaminck.builders.exception.MalformedQueryParametersException;
import fr.cvlaminck.builders.uri.encoding.UriEncoding;

import java.util.*;

/**
 * Representation of the query part of an URI in the form of an associative map
 * of parameters.
 */
public class QueryParameters
        implements Iterable<QueryParameter> {
    public final static char queryParameterSeparatorChar = '&';

    private UriEncoding uriEncoding;

    private Map<String, QueryParameter> queryParameters;
    private char separatorChar;

    private QueryParameters(UriEncoding uriEncoding, char separatorChar) {
        this.uriEncoding = uriEncoding;

        this.separatorChar = separatorChar;
        this.queryParameters = new HashMap<String, QueryParameter>();
    }

    QueryParameters(UriEncoding uriEncoding, char separatorChar, Map<String, List<String>> queryParameters) {
        this(uriEncoding, separatorChar);
        for (Map.Entry<String, List<String>> param : queryParameters.entrySet()) {
            QueryParameter queryParameter = new QueryParameter(uriEncoding, separatorChar, param.getKey(), param.getValue());
            this.queryParameters.put(queryParameter.getEncodedName(), queryParameter);
        }
    }

    public static QueryParametersBuilder newBuilder() {
        return new QueryParametersBuilderImpl(UriEncoding.getDefault());
    }

    public QueryParametersBuilder buildUpon() {
        return new QueryParametersBuilderImpl(uriEncoding, this);
    }

    public static QueryParameters parse(String sQuery) {
        return parse(sQuery, QueryParameters.queryParameterSeparatorChar);
    }

    private static QueryParameters parse(String sQuery, char queryParameterSeparatorChar) {
        if (sQuery == null || sQuery.isEmpty()) {
            throw new MalformedQueryParametersException();
        }
        QueryParametersBuilder builder = newBuilder();
        int currentParsedCharacterIndex = 0;
        int indexOfEqualCharacter;
        int indexOfSeparatorCharacter;
        if (sQuery.length() > 0) {
            while (currentParsedCharacterIndex < sQuery.length()) {
                indexOfEqualCharacter = sQuery.indexOf('=', currentParsedCharacterIndex);
                indexOfSeparatorCharacter = sQuery.indexOf(queryParameterSeparatorChar, currentParsedCharacterIndex);
                if (indexOfEqualCharacter == -1) {
                    throw new MalformedQueryParametersException();
                }
                if (indexOfSeparatorCharacter == -1) {
                    indexOfSeparatorCharacter = sQuery.length();
                }
                String encodedParameterName = sQuery.substring(currentParsedCharacterIndex, indexOfEqualCharacter);
                String encodedParameterValue;
                if (indexOfEqualCharacter == indexOfSeparatorCharacter - 1) {
                    encodedParameterValue = "";
                } else {
                    encodedParameterValue = sQuery.substring(indexOfEqualCharacter + 1, indexOfSeparatorCharacter);
                }
                builder.appendEncodedQueryParameter(encodedParameterName, encodedParameterValue);
                currentParsedCharacterIndex = indexOfSeparatorCharacter + 1;
            }
        }
        return builder.build();
    }

    /**
     * Returns the number of query parameter.
     */
    public int getQueryParameterCount() {
        return queryParameters.size();
    }

    /**
     * Returns a set of all query parameter names.
     */
    public Set<QueryParameter> getQueryParameters() {
        return new HashSet<QueryParameter>(this.queryParameters.values());
    }

    /**
     * Returns the query parameter with the name provided in parameter or null.
     * The name provided to this method will be encoded before trying to find
     * the corresponding query parameter.
     */
    public QueryParameter getQueryParameter(String key) {
        return this.queryParameters.get(key); //TODO
    }

    /**
     * Returns the query parameter with the name provided in parameter or null.
     * The name provided to this method will be used raw. If the name of the query
     * parameter is encoded in the query string, this method WILL return null.
     */
    public QueryParameter getQueryParameterWithEncodedName(String encodedName) {
        return this.queryParameters.get(encodedName);
    }

    public char getSeparatorChar() {
        return separatorChar;
    }

    @Override
    public Iterator<QueryParameter> iterator() {
        return getQueryParameters().iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryParameters that = (QueryParameters) o;

        if (separatorChar != that.separatorChar) return false;
        if (!queryParameters.equals(that.queryParameters)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = queryParameters.hashCode();
        result = 31 * result + (int) separatorChar;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (QueryParameter queryParameter : queryParameters.values()) {
            if (sb.length() != 0) {
                sb.append(separatorChar);
            }
            sb.append(queryParameter.toString());
        }
        return sb.toString();
    }

}
