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

/**
 * Interface defining all operations available to manipulate and construct the authority part of an uri.
 */
public interface AuthorityBuilderOperations<Builder> {

    public Builder withEncodedUserInformation(String encodedUserInformation);

    public Builder withUserInformation(String userInformation);

    public Builder withEncodedHost(String encodedHost);

    public Builder withHost(String host);

    public Builder withEmptyHost();

    public Builder withPort(int port);

    public Builder withoutPort();

}
