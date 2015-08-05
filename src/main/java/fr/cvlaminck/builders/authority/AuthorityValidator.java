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
import fr.cvlaminck.builders.exception.MalformedIpLiteralException;
import fr.cvlaminck.builders.exception.MalformedIpV4AddressException;
import fr.cvlaminck.builders.exception.MalformedRegisteredNameException;
import fr.cvlaminck.builders.uri.UriValidator;

import java.util.regex.Pattern;

public class AuthorityValidator {
    public final static String IPVFUTURE = "v[0-9A-Fa-f]\\." + UriValidator.UNRESERVED + "|" + UriValidator.SUB_DELIMS + "|:";

    private Pattern userInformationPattern;
    private Pattern regNamePattern;
    private Pattern ipvFuturePattern;

    public AuthorityValidator() {
        this.userInformationPattern = Pattern.compile("(" + UriValidator.UNRESERVED + "|" + UriValidator.PCT_ENCODED + "|" + UriValidator.SUB_DELIMS + "|:)+");
        this.regNamePattern = Pattern.compile("(" + UriValidator.UNRESERVED + "|" + UriValidator.PCT_ENCODED + "|" + UriValidator.SUB_DELIMS + "|:)+");
        this.ipvFuturePattern = Pattern.compile(IPVFUTURE);
    }

    /**
     * Validate if the value passed as parameter is valid user information according to RFC3986 or
     * throws an MalformedAuthorityException.
     * <p/>
     * Format defined in RFC is : <p/>
     * userinfo    = *( unreserved / pct-encoded / sub-delims / ":" )
     *
     * @throws fr.cvlaminck.builders.exception.MalformedAuthorityException if the value passed as parameter is not valid user information.
     */
    public void validateUserInformation(String userInformation) {
        if (userInformation == null) {
            throw new NullPointerException();
        }
        if (!userInformationPattern.matcher(userInformation).matches()) {
            throw new MalformedAuthorityException();
        }
    }

    /**
     * Validate if the value passed as parameter is a valid host according to RFC3986 or
     * throws an MalformedAuthorityException. This function will not check the existence
     * of the host, it will only check if the format of the host is right.
     *
     * @throws fr.cvlaminck.builders.exception.MalformedAuthorityException if the value passed as parameter is not valid host.
     */
    public void validateHost(String host) {
        if (host == null) {
            throw new MalformedAuthorityException();
        }
        if (host.isEmpty()) {
            return;
        }
        if (host.startsWith("[")) {
            validateIpLiteral(host);
        } else {
            try {
                validateIpV4Address(host);
            } catch (MalformedIpV4AddressException ex) {
                validateRegName(host);
            }
        }
    }

    /**
     * Validate if the value passed as parameter is a valid ipv6 address according to RFC3986 or
     * throws an MalformedIpLiteralException.
     * <p/>
     * <p/>
     * Format defined in RFC is : <p/>
     * IP-literal = "[" ( IPv6address / IPvFuture  ) "]"
     *
     * @throws fr.cvlaminck.builders.exception.MalformedIpLiteralException if the value passed as parameter is not a valid ip literal.
     */
    public void validateIpLiteral(String host) {
        if (host == null) {
            throw new MalformedIpLiteralException();
        }
        if (!host.startsWith("[") || !host.endsWith("]")) {
            throw new MalformedIpLiteralException();
        }
        host = host.substring(1, host.length() - 1);
        if (host.startsWith("v")) {
            if (!ipvFuturePattern.matcher(host).matches()) {
                throw new MalformedIpLiteralException();
            }
        } else {
            validateIpv6Address(host);
        }
    }

    /**
     * Validate if the value passed as parameter is a valid ipv6 address according to RFC3986 or
     * throws an MalformedIpV6AddressException.
     * <p/>
     * <p/>
     * Format defined in RFC is : <p/>
     * IPv6address =                            6( h16 ":" ) ls32
     * /                       "::" 5( h16 ":" ) ls32
     * / [               h16 ] "::" 4( h16 ":" ) ls32
     * / [ *1( h16 ":" ) h16 ] "::" 3( h16 ":" ) ls32
     * / [ *2( h16 ":" ) h16 ] "::" 2( h16 ":" ) ls32
     * / [ *3( h16 ":" ) h16 ] "::"    h16 ":"   ls32
     * / [ *4( h16 ":" ) h16 ] "::"              ls32
     * / [ *5( h16 ":" ) h16 ] "::"              h16
     * / [ *6( h16 ":" ) h16 ] "::"
     * <p/>
     * ls32        = ( h16 ":" h16 ) / IPv4address
     * ; least-significant 32 bits of address
     * h16         = 1*4HEXDIG
     * ; 16 bits of address represented in hexadecimal
     *
     * @throws fr.cvlaminck.builders.exception.MalformedIpV6AddressException if the value passed as parameter is not a valid ip address.
     */
    public void validateIpv6Address(String host) {
        //FIXME
    }

    /**
     * Validate if the value passed as parameter is a valid ipv4 address according to RFC3986 or
     * throws an MalformedIpV4AddressException.
     * <p/>
     * <p/>
     * Format defined in RFC is : <p/>
     * IPv4address = dec-octet "." dec-octet "." dec-octet "." dec-octet
     * <p/>
     * dec-octet   = DIGIT                 ; 0-9
     * / %x31-39 DIGIT         ; 10-99
     * / "1" 2DIGIT            ; 100-199
     * / "2" %x30-34 DIGIT     ; 200-249
     * / "25" %x30-35          ; 250-255
     *
     * @throws fr.cvlaminck.builders.exception.MalformedIpV4AddressException if the value passed as parameter is not a valid ip address.
     */
    public void validateIpV4Address(String host) {
        if (host == null) {
            throw new MalformedIpV4AddressException();
        }
        if (host.isEmpty()) {
            throw new MalformedIpV4AddressException();
        }
        String[] ipAddressParts = host.split("\\.");
        if (ipAddressParts.length != 4) {
            throw new MalformedIpV4AddressException();
        }
        try {
            for (String ipAddressPart : ipAddressParts) {
                int value = Integer.parseInt(ipAddressPart);
                if (value < 0 || value > 255) {
                    throw new MalformedIpV4AddressException();
                }
            }
        } catch (NumberFormatException ex) {
            throw new MalformedIpV4AddressException();
        }
    }

    /**
     * Validate if the value passed as parameter is a valid registered name according to RFC3986 or
     * throws an MalformedAuthorityException. This function will not check the existence
     * of the host, it will only check if the format of the host is right.
     * <p/>
     * <p/>
     * Format defined in RFC is : <p/>
     * reg-name    = *( unreserved / pct-encoded / sub-delims )
     *
     * @throws fr.cvlaminck.builders.exception.MalformedRegisteredNameException if the value passed as parameter is not a valid registered name.
     */
    public void validateRegName(String host) {
        if (host == null) {
            throw new MalformedRegisteredNameException();
        }
        if (!regNamePattern.matcher(host).matches()) {
            throw new MalformedRegisteredNameException();
        }
    }

}
