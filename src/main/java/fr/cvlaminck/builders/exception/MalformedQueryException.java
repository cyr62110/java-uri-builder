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
package fr.cvlaminck.builders.exception;

public class MalformedQueryException
        extends MalformedUriException {
    private final static String MESSAGE = "Query format do not match format defined in RFC3986";

    public MalformedQueryException() {
        super(MESSAGE);
    }

    MalformedQueryException(String message) {
        super(message);
    }
}
