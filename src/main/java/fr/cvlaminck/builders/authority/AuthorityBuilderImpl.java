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
package fr.cvlaminck.builders.authority;

import fr.cvlaminck.builders.exception.MalformedAuthorityException;
import fr.cvlaminck.builders.uri.encoding.UriEncoding;

class AuthorityBuilderImpl
        implements AuthorityBuilder {

    private UriEncoding uriEncoding;

    private AuthorityValidator authorityValidator;

    private String userInformation;
    private String host;
    private Integer port;

    AuthorityBuilderImpl(UriEncoding uriEncoding) {
        this.authorityValidator = new AuthorityValidator();
        this.uriEncoding = uriEncoding;
    }

    AuthorityBuilderImpl(UriEncoding uriEncoding, Authority authority) {
        this(uriEncoding);
        this.userInformation = authority.getEncodedUserInformation();
        this.host = authority.getEncodedHost();
        this.port = authority.getPort();
    }

    @Override
    public AuthorityBuilder withEncodedUserInformation(String encodedUserInformation) {
        if (encodedUserInformation != null && encodedUserInformation.isEmpty()) {
            encodedUserInformation = null;
        }
        if (encodedUserInformation != null) {
            authorityValidator.validateUserInformation(encodedUserInformation);
        }
        this.userInformation = encodedUserInformation;
        return this;
    }

    @Override
    public AuthorityBuilder withUserInformation(String userInformation) {
        return withEncodedUserInformation(uriEncoding.encode(userInformation));
    }

    @Override
    public AuthorityBuilder withEncodedHost(String encodedHost) {
        authorityValidator.validateHost(encodedHost);
        this.host = encodedHost;
        return this;
    }

    @Override
    public AuthorityBuilder withHost(String host) {
        return withEncodedHost(uriEncoding.encode(host));
    }

    @Override
    public AuthorityBuilder withEmptyHost() {
        return withEncodedHost("");
    }

    @Override
    public AuthorityBuilder withPort(int port) {
        if (port < 0 || port > 65535) {
            throw new MalformedAuthorityException();
        }
        this.port = port;
        return this;
    }

    @Override
    public AuthorityBuilder withoutPort() {
        this.port = null;
        return this;
    }

    @Override
    public Authority build() {
        if (host == null) {
            throw new MalformedAuthorityException();
        }
        return new Authority(uriEncoding, userInformation, host, port);
    }
}
