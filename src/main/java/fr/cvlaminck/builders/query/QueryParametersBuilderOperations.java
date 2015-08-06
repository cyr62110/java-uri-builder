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

/**
 * Interface defining all available operations to manipulate and construct the query part of an uri as
 * a list of parameters.</p>
 * <p/>
 * Query parameters are not normalized in the RFC3986.
 */
public interface QueryParametersBuilderOperations<Builder> {

    public Builder appendEncodedQueryParameter(String encodedName, String... encodedValues);

    public Builder appendQueryParameter(String name, String... values);

    public Builder removeQueryParameterWithEncodedName(String encodedName);

    public Builder removeQueryParameter(String name);

    public Builder replaceEncodedQueryParameter(String encodedName, String... encodedValues);

    public Builder replaceQueryParameter(String name, String... values);

}
