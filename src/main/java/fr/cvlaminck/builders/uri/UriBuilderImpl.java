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

import fr.cvlaminck.builders.path.Path;
import fr.cvlaminck.builders.uri.encoding.UriEncoding;

class UriBuilderImpl
    extends BaseUriBuilderImpl
    implements UriBuilder {

    UriBuilderImpl(UriEncoding uriEncoding) {
        super(uriEncoding);
    }

    UriBuilderImpl(UriEncoding uriEncoding, Uri uri) {
        super(uriEncoding, uri);
    }

    @Override
    public UriBuilder withEncodedUserInformation(String encodedUserInformation) {
        getAuthorityBuilder().withEncodedUserInformation(encodedUserInformation);
        return this;
    }

    @Override
    public UriBuilder withUserInformation(String userInformation) {
        getAuthorityBuilder().withUserInformation(userInformation);
        return this;
    }

    @Override
    public UriBuilder withEncodedHost(String encodedHost) {
        getAuthorityBuilder().withEncodedHost(encodedHost);
        return this;
    }

    @Override
    public UriBuilder withHost(String host) {
        getAuthorityBuilder().withHost(host);
        return this;
    }

    @Override
    public UriBuilder withEmptyHost() {
        getAuthorityBuilder().withEmptyHost();
        return this;
    }

    @Override
    public UriBuilder withPort(int port) {
        getAuthorityBuilder().withPort(port);
        return this;
    }

    @Override
    public UriBuilder withoutPort() {
        getAuthorityBuilder().withoutPort();
        return this;
    }

    @Override
    public UriBuilder appendPathSegment(String pathSegment) {
        getPathBuilder().appendPathSegment(pathSegment);
        return this;
    }

    @Override
    public UriBuilder appendEncodedPathSegment(String pathSegment) {
        getPathBuilder().appendEncodedPathSegment(pathSegment);
        return this;
    }

    @Override
    public UriBuilder appendPath(Path path) {
        getPathBuilder().appendPath(path);
        return this;
    }

    @Override
    public UriBuilder removePathSegment(int index) {
        getPathBuilder().removePathSegment(index);
        return this;
    }

    @Override
    public UriBuilder removeLastPathSegment() {
        getPathBuilder().removeLastPathSegment();
        return this;
    }

    @Override
    public UriBuilder removeFirstPathSegment() {
        getPathBuilder().removeFirstPathSegment();
        return this;
    }

    @Override
    public UriBuilder appendEncodedQueryParameter(String encodedKey, String... encodedValues) {
        getQueryParametersBuilder().appendEncodedQueryParameter(encodedKey, encodedValues);
        return this;
    }

    @Override
    public UriBuilder appendQueryParameter(String key, String... values) {
        getQueryParametersBuilder().appendQueryParameter(key, values);
        return this;
    }

    @Override
    public UriBuilder removeQueryParameter(String name) {
        getQueryParametersBuilder().removeQueryParameter(name);
        return this;
    }

    @Override
    public UriBuilder replaceEncodedQueryParameter(String encodedName, String... encodedValues) {
        getQueryParametersBuilder().replaceEncodedQueryParameter(encodedName, encodedValues);
        return this;
    }

    @Override
    public UriBuilder replaceQueryParameter(String name, String... values) {
        getQueryParametersBuilder().replaceQueryParameter(name, values);
        return this;
    }

    @Override
    public UriBuilder removeQueryParameterWithEncodedName(String encodedName) {
        getQueryParametersBuilder().removeQueryParameterWithEncodedName(encodedName);
        return this;
    }
}
