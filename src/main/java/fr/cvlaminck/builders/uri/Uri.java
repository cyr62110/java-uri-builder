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
package fr.cvlaminck.builders.uri;

import fr.cvlaminck.builders.authority.Authority;
import fr.cvlaminck.builders.exception.MalformedQueryParametersException;
import fr.cvlaminck.builders.path.Path;
import fr.cvlaminck.builders.query.QueryParameters;
import fr.cvlaminck.builders.uri.encoding.UriEncoding;
import fr.cvlaminck.builders.uri.parser.UriParser;

public class Uri {
    public final static char schemeSeparator = ':';
    public final static char authorityPrefixCharacter = '/';
    public final static char querySeparator = '?';
    public final static char fragmentSeparator = '#';

    private UriEncoding uriEncoding;

    private String scheme;
    private Authority authority;
    private Path path;
    private String query;
    private QueryParameters queryParameters;
    private String fragment;

    Uri(UriEncoding uriEncoding, String scheme, Authority authority, Path path, QueryParameters queryParameters, String fragment) {
        this(uriEncoding, scheme, authority, path, queryParameters.toString(), fragment);
    }

    Uri(UriEncoding uriEncoding, String scheme, Authority authority, Path path, String query, String fragment) {
        this.uriEncoding = uriEncoding;

        this.scheme = scheme;
        this.authority = authority;
        this.path = path;
        this.query = query;
        this.fragment = fragment;

        try {
            this.queryParameters = QueryParameters.parse(query);
        } catch (MalformedQueryParametersException ex) {
            this.queryParameters = null;
        }
    }

    public static UriBuilder newBuilder() {
        return new UriBuilderImpl(UriEncoding.getDefault());
    }

    public UriBuilder buildUpon() {
        return new UriBuilderImpl(uriEncoding, this);
    }

    public static Uri parse(String sUri) {
        return new UriParser().parse(sUri);
    }

    public String getScheme() {
        return scheme;
    }

    public boolean hasAuthority() {
        return authority != null;
    }

    public Authority getAuthority() {
        return authority;
    }

    public Path getPath() {
        return path;
    }

    public boolean hasQuery() {
        return query != null && !query.isEmpty();
    }

    public String getQuery() {
        return uriEncoding.decode(query);
    }

    public String getEncodedQuery() {
        return query;
    }

    public QueryParameters getQueryParameters() {
        return queryParameters;
    }

    public String getEncodedFragment() {
        return fragment;
    }

    public String getFragment() {
        return uriEncoding.decode(fragment);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(scheme).append(schemeSeparator);
        if (authority != null) {
            sb.append(authorityPrefixCharacter);
            sb.append(authorityPrefixCharacter);
            sb.append(authority);
        }
        sb.append(path);
        if (query != null && !query.isEmpty()) {
            sb.append(querySeparator);
            sb.append(query);
        }
        if (fragment != null) {
            sb.append(fragmentSeparator).append(fragment);
        }
        return sb.toString();
    }
}
