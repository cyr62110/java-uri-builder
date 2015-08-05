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

/**
 * Representation of the authority part of an URI.
 */
public class Authority {
    public final static char userInformationHostSeparator = '@';
    public final static char hostPortSeparator = ':';
    public final static char ipv6PrefixCharacter = '[';
    public final static char ipv6PostfixCharacter = ']';

    private UriEncoding uriEncoding;

    private String userInformation;
    private String host;
    private Integer port;

    Authority(UriEncoding uriEncoding, String userInformation, String host, Integer port) {
        this.uriEncoding = uriEncoding;

        this.userInformation = userInformation;
        this.host = host;
        this.port = port;
    }

    public static Authority emptyHost() {
        return new Authority(UriEncoding.getDefault(), null, "", null);
    }

    public static AuthorityBuilder newBuilder() {
        return new AuthorityBuilderImpl(UriEncoding.getDefault());
    }

    public AuthorityBuilder buildUpon() {
        return new AuthorityBuilderImpl(uriEncoding, this);
    }

    public static Authority parse(String sAuthority) {
        if (sAuthority == null) {
            throw new MalformedAuthorityException();
        }

        AuthorityBuilder authorityBuilder = Authority.newBuilder();
        int currentParsedCharacterIndex = 0;
        int indexOfUserInfoHostSeparatorCharacter = sAuthority.indexOf(userInformationHostSeparator, currentParsedCharacterIndex);
        if (indexOfUserInfoHostSeparatorCharacter != -1) {
            authorityBuilder.withEncodedUserInformation(sAuthority.substring(currentParsedCharacterIndex, indexOfUserInfoHostSeparatorCharacter));
            currentParsedCharacterIndex = indexOfUserInfoHostSeparatorCharacter + 1;
        }
        int indexOfHostPortSeparatorCharacter;
        if (currentParsedCharacterIndex < sAuthority.length() &&
                sAuthority.charAt(currentParsedCharacterIndex) == ipv6PrefixCharacter) { //In case of host being an IPV6 or IPv.Future
            int indexOfIPV6PostfixCharacter = sAuthority.indexOf(ipv6PostfixCharacter, currentParsedCharacterIndex);
            indexOfHostPortSeparatorCharacter = sAuthority.indexOf(hostPortSeparator, indexOfIPV6PostfixCharacter + 1);
        } else { //For all other host case
            indexOfHostPortSeparatorCharacter = sAuthority.indexOf(hostPortSeparator, currentParsedCharacterIndex);
        }
        if (indexOfHostPortSeparatorCharacter == -1) {
            indexOfHostPortSeparatorCharacter = sAuthority.length();
        }
        authorityBuilder.withEncodedHost(sAuthority.substring(currentParsedCharacterIndex, indexOfHostPortSeparatorCharacter));
        currentParsedCharacterIndex = indexOfHostPortSeparatorCharacter + 1;
        if (currentParsedCharacterIndex <= sAuthority.length()) {
            try {
                authorityBuilder.withPort(Integer.parseInt(sAuthority.substring(currentParsedCharacterIndex)));
            } catch (NumberFormatException ex) {
                throw new MalformedAuthorityException();
            }
        }
        return authorityBuilder.build();
    }

    public String getEncodedUserInformation() {
        return userInformation;
    }

    public String getUserInformation() {
        return uriEncoding.decode(userInformation);
    }

    public String getEncodedHost() {
        return host;
    }

    public String getHost() {
        return uriEncoding.decode(host);
    }

    public Integer getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Authority authority = (Authority) o;

        if (!host.equals(authority.host)) return false;
        if (port != null ? !port.equals(authority.port) : authority.port != null) return false;
        if (userInformation != null ? !userInformation.equals(authority.userInformation) : authority.userInformation != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userInformation != null ? userInformation.hashCode() : 0;
        result = 31 * result + host.hashCode();
        result = 31 * result + (port != null ? port.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (userInformation != null) {
            sb.append(userInformation);
            sb.append(userInformationHostSeparator);
        }
        sb.append(host);
        if (port != null) {
            sb.append(hostPortSeparator);
            sb.append(port);
        }
        return sb.toString();
    }
}
