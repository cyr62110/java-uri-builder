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
import fr.cvlaminck.builders.uri.UriValidator;

import java.util.regex.Pattern;

public class QueryParameterValidator {

    private Pattern queryParameterNamePattern;
    private Pattern queryParameterValuePattern;

    public static String QUERY_PARAMETER_NAME_SUB_DELIMS = "[!$&'()*+,;]";
    public static String QUERY_PARAMETER_VALUE_SUB_DELIMS = "[!$'()*+,;=]";

    public QueryParameterValidator() {
        queryParameterNamePattern = Pattern.compile("(" + UriValidator.UNRESERVED + "|" + UriValidator.PCT_ENCODED + "|" + QUERY_PARAMETER_NAME_SUB_DELIMS + "|[:@]" + "|[/?])+");
        queryParameterValuePattern = Pattern.compile("(" + UriValidator.UNRESERVED + "|" + UriValidator.PCT_ENCODED + "|" + QUERY_PARAMETER_VALUE_SUB_DELIMS + "|[:@]" + "|[/?])*");
    }

    /**
     * Validate if the value passed as parameter is a valid query parameter name or
     * throws an MalformedQueryException.
     *
     * @throws fr.cvlaminck.builders.exception.MalformedQueryParametersException if the value passed as parameter is not a valid parameter name.
     */
    public void validateQueryParameterName(String name) {
        if (name == null) {
            throw new MalformedQueryParametersException();
        }
        if (!queryParameterNamePattern.matcher(name).matches()) {
            throw new MalformedQueryParametersException();
        }
    }

    /**
     * Validate if the value passed as parameter is a valid query parameter name or
     * throws an MalformedQueryException.
     *
     * @throws fr.cvlaminck.builders.exception.MalformedQueryParametersException if the value passed as parameter is not a valid parameter value.
     */
    public void validateQueryParameterValue(String value) {
        if (value == null) {
            throw new MalformedQueryParametersException();
        }
        if (!queryParameterValuePattern.matcher(value).matches()) {
            throw new MalformedQueryParametersException();
        }
    }

}
