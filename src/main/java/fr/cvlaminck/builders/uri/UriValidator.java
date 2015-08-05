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

import fr.cvlaminck.builders.exception.MalformedFragmentException;
import fr.cvlaminck.builders.exception.MalformedQueryException;
import fr.cvlaminck.builders.exception.MalformedSchemeException;

import java.util.regex.Pattern;

public class UriValidator {

    public static String UNRESERVED = "[a-zA-Z0-9\\-\\._~]";

    public static String PCT_ENCODED = "(%[a-fA-F0-9][a-fA-F0-9])";

    public static String SUB_DELIMS = "[!$&'()*+,;=]";

    public static String PCHAR = UNRESERVED + "|" + PCT_ENCODED + "|" + SUB_DELIMS + "|[:@]";

    private Pattern schemePattern;
    private Pattern queryPattern;
    private Pattern fragmentPattern;

    public UriValidator() {
        this.schemePattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9+.\\-]*");
        this.queryPattern = Pattern.compile("(" + UriValidator.PCHAR + "|[/?])+");
        this.fragmentPattern = Pattern.compile("(" + UriValidator.PCHAR + "|[/?])*");
    }

    /**
     * Validate if the value passed as parameter is a valid scheme according to RFC3986 or
     * throws an MalformedSchemeException.
     * <p/>
     * Format defined in RFC is : <p/>
     * scheme      = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." ) <p/>
     *
     * @throws fr.cvlaminck.builders.exception.MalformedSchemeException if the value passed as parameter is not a valid scheme.
     */
    public void validateScheme(String scheme) {
        if (scheme == null) {
            throw new MalformedSchemeException();
        }
        if (!schemePattern.matcher(scheme).matches()) {
            throw new MalformedSchemeException();
        }
    }

    /**
     * Validate if the value passed as parameter is a valid query according to RFC3986 or
     * throws an MalformedQueryException.
     * <p/>
     * Format defined in RFC is : <p/>
     * query         = *( pchar / "/" / "?" ) <p/>
     * pchar         = unreserved / pct-encoded / sub-delims / ":" / "@" <p/>
     *
     * @throws fr.cvlaminck.builders.exception.MalformedQueryException if the value passed as parameter is not a valid query.
     */
    public void validateQuery(String sQuery) {
        if (sQuery == null) {
            throw new MalformedQueryException();
        }
        if (!queryPattern.matcher(sQuery).matches()) {
            throw new MalformedQueryException();
        }
    }

    /**
     * Validate if the value passed as parameter is a valid fragment according to RFC3986 or
     * throws an MalformedFragmentException.
     * <p/>
     * Format defined in RFC is : <p/>
     * fragment      = *( pchar / "/" / "?" )
     * pchar         = unreserved / pct-encoded / sub-delims / ":" / "@" <p/>
     *
     * @throws fr.cvlaminck.builders.exception.MalformedFragmentException if the value passed as parameter is not a valid query.
     */
    public void validateFragment(String sFragment) {
        if (sFragment == null) {
            throw new MalformedFragmentException();
        }
        if (!fragmentPattern.matcher(sFragment).matches()) {
            throw new MalformedFragmentException();
        }
    }

}
