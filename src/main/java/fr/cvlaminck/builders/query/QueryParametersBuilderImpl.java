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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class QueryParametersBuilderImpl
        implements QueryParametersBuilder {

    private UriEncoding uriEncoding;

    private QueryParameterValidator queryParameterValidator;

    private Map<String, List<String>> queryParameters;
    private char separatorChar;

    QueryParametersBuilderImpl(UriEncoding uriEncoding) {
        this.uriEncoding = uriEncoding;
        this.queryParameterValidator = new QueryParameterValidator();
        this.queryParameters = new HashMap<String, List<String>>();
        this.separatorChar = QueryParameters.queryParameterSeparatorChar;
    }

    QueryParametersBuilderImpl(UriEncoding uriEncoding, QueryParameters queryParameters) {
        this(uriEncoding);
        this.separatorChar = queryParameters.getSeparatorChar();
        for (QueryParameter queryParameter : queryParameters.getQueryParameters()) {
            this.queryParameters.put(queryParameter.getEncodedName(), queryParameter.getEncodedValues());
        }
    }

    @Override
    public QueryParametersBuilder appendEncodedQueryParameter(String encodedKey, String... encodedValues) {
        queryParameterValidator.validateQueryParameterName(encodedKey);
        if (encodedValues.length > 0) {
            List<String> queryParameterValues = queryParameters.get(encodedKey);
            if (queryParameterValues == null) {
                queryParameterValues = new ArrayList<String>();
                queryParameters.put(encodedKey, queryParameterValues);
            }
            for (String encodedValue : encodedValues) {
                queryParameterValidator.validateQueryParameterValue(encodedValue);
                queryParameterValues.add(encodedValue);
            }
        }
        return this;
    }

    @Override
    public QueryParametersBuilder appendQueryParameter(String key, String... values) {
        String encodedKey = uriEncoding.encode(key);
        String[] encodedValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            encodedValues[i] = uriEncoding.encode(values[i]);
        }
        return appendEncodedQueryParameter(encodedKey, encodedValues);
    }

    @Override
    public QueryParametersBuilder removeQueryParameterWithEncodedName(String encodedName) {
        queryParameters.remove(encodedName);
        return this;
    }

    @Override
    public QueryParametersBuilder removeQueryParameter(String name) {
        String encodedKey = uriEncoding.encode(name);
        return removeQueryParameterWithEncodedName(encodedKey);
    }

    @Override
    public QueryParametersBuilder removeQueryParameterWithEncodedName(String encodedName) {
        queryParameters.remove(encodedName);
        return this;
    }

    @Override
    public QueryParameters build() {
        return new QueryParameters(uriEncoding, separatorChar, queryParameters);
    }
}
