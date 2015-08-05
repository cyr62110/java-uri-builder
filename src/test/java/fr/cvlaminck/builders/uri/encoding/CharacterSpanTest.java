package fr.cvlaminck.builders.uri.encoding;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterSpanTest {

    @Test
    public void testContainsWithSingleCharacterRange() throws Exception {
        CharacterSpan span = CharacterSpan.newRange('a', 'z');

        assertTrue(span.contains('a'));
        assertTrue(span.contains('g'));
        assertTrue(span.contains('z'));

        assertFalse(span.contains('A'));
    }

    @Test
    public void testContainsWithMultiCharactersRange() throws Exception {
        CharacterSpan span = CharacterSpan.newSingleCharacter('a');

        assertTrue(span.contains('a'));
        assertFalse(span.contains('b'));
    }
}