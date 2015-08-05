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
import fr.cvlaminck.builders.path.Path;
import fr.cvlaminck.builders.path.PathBuilder;
import fr.cvlaminck.builders.query.QueryParameters;
import fr.cvlaminck.builders.query.QueryParametersBuilder;

/**
 * Interface defining all available operations to manipulate and construct an uri.
 */
public interface UriBuilderOperations<Builder> {

    public Builder withScheme(String scheme);

    public Builder withAuthority(String authority);

    public Builder withAuthority(Authority authority);

    public Builder withAuthority(AuthorityBuilder authorityBuilder);

    public Builder withoutAuthority();

    public Builder withPath(String path);

    public Builder withPath(Path path);

    public Builder withPath(PathBuilder pathBuilder);

    public Builder withEmptyPath();

    public Builder withEncodedQuery(String encodedQuery);

    public Builder withQuery(String query);

    public Builder withoutQuery();

    public Builder withQueryParameters(QueryParameters queryParameters);

    public Builder withQueryParameters(QueryParametersBuilder queryParametersBuilder);

    public Builder withEncodedFragment(String encodedFragment);

    public Builder withFragment(String fragment);

    public Builder withoutFragment();

}
