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
package fr.cvlaminck.builders.uri.encoding;

class CharacterSpan {

    private char startCharacter;
    private char endCharacter;

    private CharacterSpan(char startCharacter, char endCharacter) {
        this.startCharacter = startCharacter;
        this.endCharacter = endCharacter;
    }

    public static CharacterSpan newRange(char startCharacter, char endCharacter) {
        if (Character.compare(startCharacter, endCharacter) > 0) {
            throw new IllegalArgumentException(); //FIXME
        }
        return new CharacterSpan(startCharacter, endCharacter);
    }

    public static CharacterSpan newSingleCharacter(char character) {
        return new CharacterSpan(character, character);
    }

    public boolean contains(char character) {
        return Character.compare(character, startCharacter) >= 0 && Character.compare(character, endCharacter) <= 0;
    }
}
