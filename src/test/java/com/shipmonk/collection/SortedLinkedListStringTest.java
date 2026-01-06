package com.shipmonk.collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for SortedLinkedList with String values.
 */
@DisplayName("SortedLinkedList<String>")
class SortedLinkedListStringTest {

    private SortedLinkedList<String> list;

    @BeforeEach
    void setUp() {
        list = SortedLinkedList.ofStrings();
    }

    @Nested
    @DisplayName("Creation and Factory Methods")
    class FactoryMethodTests {

        @Test
        @DisplayName("ofStrings() creates an empty list")
        void ofStrings_createsEmptyList() {
            assertNotNull(list);
            assertTrue(list.isEmpty());
            assertEquals(0, list.size());
        }
    }

    @Nested
    @DisplayName("Add Operations")
    class AddTests {

        @Test
        @DisplayName("add() inserts single element")
        void add_singleElement() {
            list.add("hello");

            assertEquals(1, list.size());
            assertTrue(list.contains("hello"));
            assertEquals("hello", list.get(0));
        }

        @Test
        @DisplayName("add() maintains lexicographic sorted order")
        void add_maintainsLexicographicOrder() {
            list.add("banana");
            list.add("apple");
            list.add("cherry");
            list.add("date");
            list.add("apricot");

            assertEquals(List.of("apple", "apricot", "banana", "cherry", "date"), list.toList());
        }

        @Test
        @DisplayName("add() allows duplicates")
        void add_allowsDuplicates() {
            list.add("test");
            list.add("test");
            list.add("test");

            assertEquals(3, list.size());
            assertEquals(List.of("test", "test", "test"), list.toList());
        }

        @Test
        @DisplayName("add() handles empty strings")
        void add_handlesEmptyStrings() {
            list.add("b");
            list.add("");
            list.add("a");

            assertEquals(List.of("", "a", "b"), list.toList());
        }

        @Test
        @DisplayName("add() handles case-sensitive sorting")
        void add_handlesCaseSensitiveSorting() {
            list.add("banana");
            list.add("Apple");
            list.add("cherry");

            // Uppercase letters come before lowercase in Unicode
            assertEquals(List.of("Apple", "banana", "cherry"), list.toList());
        }

        @Test
        @DisplayName("add() handles unicode characters")
        void add_handlesUnicodeCharacters() {
            list.add("über");
            list.add("apple");
            list.add("żółć");

            // Natural Unicode ordering
            assertEquals(3, list.size());
            assertTrue(list.contains("über"));
            assertTrue(list.contains("żółć"));
        }

        @Test
        @DisplayName("add() throws NullPointerException for null value")
        void add_throwsForNull() {
            assertThrows(NullPointerException.class, () -> list.add(null));
        }

        @Test
        @DisplayName("add() handles strings with spaces")
        void add_handlesStringsWithSpaces() {
            list.add("hello world");
            list.add("hello");
            list.add("hello there");

            assertEquals(List.of("hello", "hello there", "hello world"), list.toList());
        }

        @Test
        @DisplayName("add() handles numeric strings")
        void add_handlesNumericStrings() {
            list.add("10");
            list.add("2");
            list.add("1");

            // Lexicographic, not numeric order
            assertEquals(List.of("1", "10", "2"), list.toList());
        }
    }

    @Nested
    @DisplayName("Remove Operations")
    class RemoveTests {

        @Test
        @DisplayName("remove() returns true when element exists")
        void remove_returnsTrueWhenExists() {
            list.add("apple");
            list.add("banana");

            assertTrue(list.remove("apple"));
            assertEquals(List.of("banana"), list.toList());
        }

        @Test
        @DisplayName("remove() returns false when element doesn't exist")
        void remove_returnsFalseWhenNotExists() {
            list.add("apple");
            list.add("banana");

            assertFalse(list.remove("cherry"));
            assertEquals(List.of("apple", "banana"), list.toList());
        }

        @Test
        @DisplayName("remove() only removes first occurrence of duplicate")
        void remove_removesFirstOccurrence() {
            list.add("test");
            list.add("test");
            list.add("test");

            assertTrue(list.remove("test"));
            assertEquals(2, list.size());
        }

        @Test
        @DisplayName("remove() is case-sensitive")
        void remove_isCaseSensitive() {
            list.add("Apple");
            list.add("apple");

            assertTrue(list.remove("Apple"));
            assertEquals(1, list.size());
            assertTrue(list.contains("apple"));
        }

        @Test
        @DisplayName("removeAll() removes all occurrences")
        void removeAll_removesAllOccurrences() {
            list.add("a");
            list.add("b");
            list.add("b");
            list.add("b");
            list.add("c");

            int removed = list.removeAll("b");

            assertEquals(3, removed);
            assertEquals(List.of("a", "c"), list.toList());
        }
    }

    @Nested
    @DisplayName("Query Operations")
    class QueryTests {

        @Test
        @DisplayName("contains() returns true for existing element")
        void contains_returnsTrueWhenExists() {
            list.add("apple");
            list.add("banana");

            assertTrue(list.contains("banana"));
        }

        @Test
        @DisplayName("contains() returns false for non-existing element")
        void contains_returnsFalseWhenNotExists() {
            list.add("apple");
            list.add("cherry");

            assertFalse(list.contains("banana"));
        }

        @Test
        @DisplayName("contains() is case-sensitive")
        void contains_isCaseSensitive() {
            list.add("Apple");

            assertTrue(list.contains("Apple"));
            assertFalse(list.contains("apple"));
        }

        @Test
        @DisplayName("get() returns correct element by index")
        void get_returnsCorrectElement() {
            list.add("cherry");
            list.add("apple");
            list.add("banana");

            assertEquals("apple", list.get(0));
            assertEquals("banana", list.get(1));
            assertEquals("cherry", list.get(2));
        }

        @Test
        @DisplayName("indexOf() returns correct index")
        void indexOf_returnsCorrectIndex() {
            list.add("cherry");
            list.add("apple");
            list.add("banana");

            assertEquals(0, list.indexOf("apple"));
            assertEquals(1, list.indexOf("banana"));
            assertEquals(2, list.indexOf("cherry"));
        }

        @Test
        @DisplayName("count() returns correct count")
        void count_returnsCorrectCount() {
            list.add("test");
            list.add("test");
            list.add("other");

            assertEquals(2, list.count("test"));
            assertEquals(1, list.count("other"));
            assertEquals(0, list.count("missing"));
        }
    }

    @Nested
    @DisplayName("First and Last Operations")
    class FirstLastTests {

        @Test
        @DisplayName("first() returns lexicographically smallest")
        void first_returnsSmallest() {
            list.add("cherry");
            list.add("apple");
            list.add("banana");

            Optional<String> first = list.first();

            assertTrue(first.isPresent());
            assertEquals("apple", first.get());
        }

        @Test
        @DisplayName("last() returns lexicographically largest")
        void last_returnsLargest() {
            list.add("apple");
            list.add("cherry");
            list.add("banana");

            Optional<String> last = list.last();

            assertTrue(last.isPresent());
            assertEquals("cherry", last.get());
        }
    }

    @Nested
    @DisplayName("Conversion Operations")
    class ConversionTests {

        @Test
        @DisplayName("toList() returns correct ArrayList")
        void toList_returnsCorrectList() {
            list.add("cherry");
            list.add("apple");
            list.add("banana");

            List<String> result = list.toList();

            assertEquals(List.of("apple", "banana", "cherry"), result);
        }

        @Test
        @DisplayName("toString() returns formatted string")
        void toString_returnsFormattedString() {
            list.add("cherry");
            list.add("apple");
            list.add("banana");

            assertEquals("[apple, banana, cherry]", list.toString());
        }
    }

    @Nested
    @DisplayName("Stream Operations")
    class StreamTests {

        @Test
        @DisplayName("stream() provides correct elements")
        void stream_providesCorrectElements() {
            list.add("cherry");
            list.add("apple");
            list.add("banana");

            List<String> streamResult = list.stream().toList();

            assertEquals(List.of("apple", "banana", "cherry"), streamResult);
        }

        @Test
        @DisplayName("stream() supports filter")
        void stream_supportsFilter() {
            list.add("apple");
            list.add("apricot");
            list.add("banana");
            list.add("avocado");

            List<String> startsWithA = list.stream()
                    .filter(s -> s.startsWith("a"))
                    .toList();

            assertEquals(List.of("apple", "apricot", "avocado"), startsWithA);
        }

        @Test
        @DisplayName("stream() supports map to uppercase")
        void stream_supportsMapToUppercase() {
            list.add("apple");
            list.add("banana");

            List<String> uppercased = list.stream()
                    .map(String::toUpperCase)
                    .toList();

            assertEquals(List.of("APPLE", "BANANA"), uppercased);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("equals() returns true for same content")
        void equals_returnsTrueForSameContent() {
            SortedLinkedList<String> other = SortedLinkedList.ofStrings();

            list.add("apple");
            list.add("banana");

            other.add("banana");
            other.add("apple");

            assertEquals(list, other);
        }

        @Test
        @DisplayName("equals() returns false for different content")
        void equals_returnsFalseForDifferentContent() {
            SortedLinkedList<String> other = SortedLinkedList.ofStrings();

            list.add("apple");
            other.add("banana");

            assertNotEquals(list, other);
        }
    }
}
