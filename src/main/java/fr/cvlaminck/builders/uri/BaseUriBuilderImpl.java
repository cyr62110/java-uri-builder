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
import fr.cvlaminck.builders.authority.AuthorityBuilder;
import fr.cvlaminck.builders.exception.MalformedPathException;
import fr.cvlaminck.builders.exception.MalformedQueryParametersException;
import fr.cvlaminck.builders.path.Path;
import fr.cvlaminck.builders.path.PathBuilder;
import fr.cvlaminck.builders.query.QueryParameters;
import fr.cvlaminck.builders.query.QueryParametersBuilder;
import fr.cvlaminck.builders.uri.encoding.UriEncoding;

/**
 * Abstract UriBuilder implementing only methods from UriBuilderOperations interface.
 * This class is only meant to be used internally to separate methods of the UriBuilder
 * that have a built-in logic and the one that are only delegating to part builders.
 */
abstract class BaseUriBuilderImpl
        implements UriBuilder {

    private UriEncoding uriEncoding;

    private UriValidator uriValidator;

    private String scheme;

    private AuthorityBuilder authorityBuilder;

    private PathBuilder pathBuilder;

    private String query;
    private QueryParametersBuilder queryParametersBuilder;

    private String fragment;

    protected BaseUriBuilderImpl(UriEncoding uriEncoding) {
        this.uriEncoding = uriEncoding;

        this.uriValidator = new UriValidator();
        this.pathBuilder = Path.emptyPath().buildUpon();
    }

    protected BaseUriBuilderImpl(UriEncoding uriEncoding, Uri uri) {
        this(uriEncoding);

        this.scheme = uri.getScheme();
        if (uri.hasAuthority()) {
            this.authorityBuilder = uri.getAuthority().buildUpon();
        }
        this.pathBuilder = uri.getPath().buildUpon();
        if (uri.hasQuery()) {
            try {
                this.queryParametersBuilder = QueryParameters.parse(uri.getEncodedQuery()).buildUpon();
            } catch (MalformedQueryParametersException ex) {
                this.query = uri.getEncodedQuery();
            }
        }
        this.fragment = uri.getEncodedFragment();
    }

    protected AuthorityBuilder getAuthorityBuilder() {
        if (authorityBuilder == null) {
            authorityBuilder = Authority.newBuilder();
        }
        return authorityBuilder;
    }

    protected PathBuilder getPathBuilder() {
        return pathBuilder;
    }

    protected QueryParametersBuilder getQueryParametersBuilder() {
        if (queryParametersBuilder == null) {
            queryParametersBuilder = QueryParameters.newBuilder();
            query = null;
        }
        return queryParametersBuilder;
    }

    @Override
    public UriBuilder withScheme(String scheme) {
        uriValidator.validateScheme(scheme);
        this.scheme = scheme;
        return this;
    }

    @Override
    public UriBuilder withAuthority(String sAuthority) {
        Authority authority = (sAuthority != null) ? Authority.parse(sAuthority) : null;
        return withAuthority(authority);
    }

    @Override
    public UriBuilder withAuthority(Authority authority) {
        AuthorityBuilder builder = (authority != null) ? authority.buildUpon() : null;
        return withAuthority(builder);
    }

    @Override
    public UriBuilder withAuthority(AuthorityBuilder authorityBuilder) {
        this.authorityBuilder = authorityBuilder;
        return this;
    }

    @Override
    public UriBuilder withoutAuthority() {
        return withAuthority((Authority) null);
    }

    @Override
    public UriBuilder withPath(String path) {
        return withPath(Path.parse(path));
    }

    @Override
    public UriBuilder withPath(Path path) {
        if (path == null) {
            throw new MalformedPathException();
        }
        return withPath(path.buildUpon());
    }

    @Override
    public UriBuilder withPath(PathBuilder pathBuilder) {
        if (pathBuilder == null) {
            throw new MalformedPathException();
        }
        this.pathBuilder = pathBuilder;
        return this;
    }

    @Override
    public UriBuilder withEmptyPath() {
        this.pathBuilder = Path.emptyPath().buildUpon();
        return this;
    }

    @Override
    public UriBuilder withEncodedQuery(String query) {
        if (query != null) {
            uriValidator.validateQuery(query);
        }
        this.query = query;
        this.queryParametersBuilder = null;
        return this;
    }

    @Override
    public UriBuilder withQuery(String query) {
        return withEncodedQuery(uriEncoding.encode(query));
    }

    @Override
    public UriBuilder withoutQuery() {
        return withEncodedQuery(null);
    }

    @Override
    public UriBuilder withQueryParameters(QueryParameters queryParameters) {
        QueryParametersBuilder builder = (queryParameters != null) ? queryParameters.buildUpon() : null;
        return withQueryParameters(builder);
    }

    @Override
    public UriBuilder withQueryParameters(QueryParametersBuilder queryParametersBuilder) {
        this.query = null;
        this.queryParametersBuilder = queryParametersBuilder;
        return this;
    }

    @Override
    public UriBuilder withEncodedFragment(String fragment) {
        if (fragment == null || fragment.isEmpty()) {
            this.fragment = null;
        } else {
            uriValidator.validateFragment(fragment);
            this.fragment = fragment;
        }
        return this;
    }

    @Override
    public UriBuilder withFragment(String fragment) {
        return withEncodedFragment(uriEncoding.encode(fragment));
    }

    @Override
    public UriBuilder withoutFragment() {
        this.fragment = null;
        return this;
    }

    @Override
    public Uri build() {
        Authority authority = (authorityBuilder != null) ? authorityBuilder.build() : null;
        Path path;
        if (authority != null) { //When authority is present, the path must either be empty or begin with a slash ("/") character.
            pathBuilder.absolute();
        }
        path = pathBuilder.build();

        if (authority == null && path.getPathSegmentCount() != 0) {
            if (path.getPathSegment(0).startsWith("//")) {
                throw new MalformedPathException("When authority is not present, the path cannot begin with two slash characters (\"//\").");
            }
        }

        Uri uri;
        if (queryParametersBuilder != null) {
            uri = new Uri(uriEncoding, scheme, authority, path, queryParametersBuilder.build(), fragment);
        } else {
            uri = new Uri(uriEncoding, scheme, authority, path, query, fragment);
        }
        return uri;
    }
}
