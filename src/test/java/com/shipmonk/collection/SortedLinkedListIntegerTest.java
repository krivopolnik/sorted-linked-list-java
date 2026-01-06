package com.shipmonk.collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for SortedLinkedList with Integer values.
 */
@DisplayName("SortedLinkedList<Integer>")
class SortedLinkedListIntegerTest {

    private SortedLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = SortedLinkedList.ofIntegers();
    }

    @Nested
    @DisplayName("Creation and Factory Methods")
    class FactoryMethodTests {

        @Test
        @DisplayName("ofIntegers() creates an empty list")
        void ofIntegers_createsEmptyList() {
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
            list.add(42);

            assertEquals(1, list.size());
            assertTrue(list.contains(42));
            assertEquals(42, list.get(0));
        }

        @Test
        @DisplayName("add() maintains sorted order")
        void add_maintainsSortedOrder() {
            list.add(5);
            list.add(1);
            list.add(9);
            list.add(3);
            list.add(7);

            assertEquals(List.of(1, 3, 5, 7, 9), list.toList());
        }

        @Test
        @DisplayName("add() allows duplicates")
        void add_allowsDuplicates() {
            list.add(5);
            list.add(5);
            list.add(5);

            assertEquals(3, list.size());
            assertEquals(List.of(5, 5, 5), list.toList());
        }

        @Test
        @DisplayName("add() handles negative numbers")
        void add_handlesNegativeNumbers() {
            list.add(0);
            list.add(-5);
            list.add(5);
            list.add(-10);

            assertEquals(List.of(-10, -5, 0, 5), list.toList());
        }

        @Test
        @DisplayName("add() handles Integer boundary values")
        void add_handlesBoundaryValues() {
            list.add(Integer.MAX_VALUE);
            list.add(Integer.MIN_VALUE);
            list.add(0);

            assertEquals(List.of(Integer.MIN_VALUE, 0, Integer.MAX_VALUE), list.toList());
        }

        @Test
        @DisplayName("add() throws NullPointerException for null value")
        void add_throwsForNull() {
            assertThrows(NullPointerException.class, () -> list.add(null));
        }

        @Test
        @DisplayName("add() inserts at head correctly")
        void add_insertsAtHead() {
            list.add(10);
            list.add(20);
            list.add(5);

            assertEquals(5, list.first().orElseThrow());
            assertEquals(List.of(5, 10, 20), list.toList());
        }

        @Test
        @DisplayName("add() inserts at tail correctly")
        void add_insertsAtTail() {
            list.add(10);
            list.add(5);
            list.add(20);

            assertEquals(20, list.last().orElseThrow());
            assertEquals(List.of(5, 10, 20), list.toList());
        }

        @Test
        @DisplayName("add() inserts in middle correctly")
        void add_insertsInMiddle() {
            list.add(10);
            list.add(30);
            list.add(20);

            assertEquals(List.of(10, 20, 30), list.toList());
        }
    }

    @Nested
    @DisplayName("Remove Operations")
    class RemoveTests {

        @Test
        @DisplayName("remove() returns true when element exists")
        void remove_returnsTrueWhenExists() {
            list.add(5);
            list.add(10);

            assertTrue(list.remove(5));
            assertEquals(List.of(10), list.toList());
        }

        @Test
        @DisplayName("remove() returns false when element doesn't exist")
        void remove_returnsFalseWhenNotExists() {
            list.add(5);
            list.add(10);

            assertFalse(list.remove(7));
            assertEquals(List.of(5, 10), list.toList());
        }

        @Test
        @DisplayName("remove() only removes first occurrence of duplicate")
        void remove_removesFirstOccurrence() {
            list.add(5);
            list.add(5);
            list.add(5);

            assertTrue(list.remove(5));
            assertEquals(2, list.size());
            assertEquals(List.of(5, 5), list.toList());
        }

        @Test
        @DisplayName("remove() works on head element")
        void remove_worksOnHead() {
            list.add(1);
            list.add(2);
            list.add(3);

            assertTrue(list.remove(1));
            assertEquals(List.of(2, 3), list.toList());
            assertEquals(2, list.first().orElseThrow());
        }

        @Test
        @DisplayName("remove() works on tail element")
        void remove_worksOnTail() {
            list.add(1);
            list.add(2);
            list.add(3);

            assertTrue(list.remove(3));
            assertEquals(List.of(1, 2), list.toList());
            assertEquals(2, list.last().orElseThrow());
        }

        @Test
        @DisplayName("remove() works on single element list")
        void remove_worksOnSingleElementList() {
            list.add(42);

            assertTrue(list.remove(42));
            assertTrue(list.isEmpty());
            assertTrue(list.first().isEmpty());
            assertTrue(list.last().isEmpty());
        }

        @Test
        @DisplayName("remove() throws NullPointerException for null value")
        void remove_throwsForNull() {
            assertThrows(NullPointerException.class, () -> list.remove(null));
        }

        @Test
        @DisplayName("removeAll() removes all occurrences")
        void removeAll_removesAllOccurrences() {
            list.add(1);
            list.add(5);
            list.add(5);
            list.add(5);
            list.add(10);

            int removed = list.removeAll(5);

            assertEquals(3, removed);
            assertEquals(List.of(1, 10), list.toList());
        }

        @Test
        @DisplayName("removeAll() returns 0 when element doesn't exist")
        void removeAll_returnsZeroWhenNotExists() {
            list.add(1);
            list.add(10);

            assertEquals(0, list.removeAll(5));
            assertEquals(List.of(1, 10), list.toList());
        }

        @Test
        @DisplayName("removeAt() removes element at specified index")
        void removeAt_removesAtIndex() {
            list.add(1);
            list.add(2);
            list.add(3);
            list.add(4);
            list.add(5);

            Integer removed = list.removeAt(2);

            assertEquals(3, removed);
            assertEquals(4, list.size());
            assertEquals(List.of(1, 2, 4, 5), list.toList());
        }

        @Test
        @DisplayName("removeAt() removes head element (index 0)")
        void removeAt_removesHead() {
            list.add(1);
            list.add(2);
            list.add(3);

            Integer removed = list.removeAt(0);

            assertEquals(1, removed);
            assertEquals(2, list.first().orElseThrow());
            assertEquals(List.of(2, 3), list.toList());
        }

        @Test
        @DisplayName("removeAt() removes tail element (last index)")
        void removeAt_removesTail() {
            list.add(1);
            list.add(2);
            list.add(3);

            Integer removed = list.removeAt(2);

            assertEquals(3, removed);
            assertEquals(2, list.last().orElseThrow());
            assertEquals(List.of(1, 2), list.toList());
        }

        @Test
        @DisplayName("removeAt() works on single element list")
        void removeAt_worksOnSingleElement() {
            list.add(42);

            Integer removed = list.removeAt(0);

            assertEquals(42, removed);
            assertTrue(list.isEmpty());
        }

        @Test
        @DisplayName("removeAt() throws IndexOutOfBoundsException for invalid index")
        void removeAt_throwsForInvalidIndex() {
            list.add(1);
            list.add(2);

            assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(2));
            assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(100));
        }

        @Test
        @DisplayName("removeAt() works correctly for indices in second half")
        void removeAt_worksForSecondHalf() {
            // Add 10 elements
            for (int i = 1; i <= 10; i++) {
                list.add(i);
            }

            // Remove from second half (index 7 = value 8)
            Integer removed = list.removeAt(7);

            assertEquals(8, removed);
            assertEquals(9, list.size());
            assertFalse(list.contains(8));
        }
    }

    @Nested
    @DisplayName("Query Operations")
    class QueryTests {

        @Test
        @DisplayName("contains() returns true for existing element")
        void contains_returnsTrueWhenExists() {
            list.add(1);
            list.add(5);
            list.add(10);

            assertTrue(list.contains(5));
        }

        @Test
        @DisplayName("contains() returns false for non-existing element")
        void contains_returnsFalseWhenNotExists() {
            list.add(1);
            list.add(10);

            assertFalse(list.contains(5));
        }

        @Test
        @DisplayName("contains() returns false for empty list")
        void contains_returnsFalseForEmptyList() {
            assertFalse(list.contains(5));
        }

        @Test
        @DisplayName("get() returns correct element by index")
        void get_returnsCorrectElement() {
            list.add(30);
            list.add(10);
            list.add(20);

            assertEquals(10, list.get(0));
            assertEquals(20, list.get(1));
            assertEquals(30, list.get(2));
        }

        @Test
        @DisplayName("get() throws IndexOutOfBoundsException for invalid index")
        void get_throwsForInvalidIndex() {
            list.add(1);

            assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(100));
        }

        @Test
        @DisplayName("get() works correctly for indices in first half (traverses from head)")
        void get_worksForFirstHalf() {
            // Add 10 elements
            for (int i = 1; i <= 10; i++) {
                list.add(i);
            }

            // Access indices 0-4 (first half)
            assertEquals(1, list.get(0));
            assertEquals(2, list.get(1));
            assertEquals(3, list.get(2));
            assertEquals(4, list.get(3));
            assertEquals(5, list.get(4));
        }

        @Test
        @DisplayName("get() works correctly for indices in second half (traverses from tail)")
        void get_worksForSecondHalf() {
            // Add 10 elements
            for (int i = 1; i <= 10; i++) {
                list.add(i);
            }

            // Access indices 5-9 (second half)
            assertEquals(6, list.get(5));
            assertEquals(7, list.get(6));
            assertEquals(8, list.get(7));
            assertEquals(9, list.get(8));
            assertEquals(10, list.get(9));
        }

        @Test
        @DisplayName("get() works correctly at boundary (middle index)")
        void get_worksAtMiddle() {
            // Add 11 elements (odd count)
            for (int i = 1; i <= 11; i++) {
                list.add(i);
            }

            // Middle element (index 5 for size 11)
            assertEquals(6, list.get(5));
        }

        @Test
        @DisplayName("indexOf() returns correct index")
        void indexOf_returnsCorrectIndex() {
            list.add(30);
            list.add(10);
            list.add(20);

            assertEquals(0, list.indexOf(10));
            assertEquals(1, list.indexOf(20));
            assertEquals(2, list.indexOf(30));
        }

        @Test
        @DisplayName("indexOf() returns -1 for non-existing element")
        void indexOf_returnsMinusOneWhenNotExists() {
            list.add(10);
            list.add(30);

            assertEquals(-1, list.indexOf(20));
        }

        @Test
        @DisplayName("count() returns correct count of occurrences")
        void count_returnsCorrectCount() {
            list.add(5);
            list.add(5);
            list.add(5);
            list.add(10);

            assertEquals(3, list.count(5));
            assertEquals(1, list.count(10));
            assertEquals(0, list.count(1));
        }
    }

    @Nested
    @DisplayName("First and Last Operations")
    class FirstLastTests {

        @Test
        @DisplayName("first() returns smallest element")
        void first_returnsSmallest() {
            list.add(50);
            list.add(10);
            list.add(30);

            Optional<Integer> first = list.first();

            assertTrue(first.isPresent());
            assertEquals(10, first.get());
        }

        @Test
        @DisplayName("first() returns empty Optional for empty list")
        void first_returnsEmptyForEmptyList() {
            assertTrue(list.first().isEmpty());
        }

        @Test
        @DisplayName("last() returns largest element")
        void last_returnsLargest() {
            list.add(10);
            list.add(50);
            list.add(30);

            Optional<Integer> last = list.last();

            assertTrue(last.isPresent());
            assertEquals(50, last.get());
        }

        @Test
        @DisplayName("last() returns empty Optional for empty list")
        void last_returnsEmptyForEmptyList() {
            assertTrue(list.last().isEmpty());
        }
    }

    @Nested
    @DisplayName("Size and Clear Operations")
    class SizeAndClearTests {

        @Test
        @DisplayName("size() returns correct count")
        void size_returnsCorrectCount() {
            assertEquals(0, list.size());

            list.add(1);
            assertEquals(1, list.size());

            list.add(2);
            list.add(3);
            assertEquals(3, list.size());
        }

        @Test
        @DisplayName("isEmpty() returns true for empty list")
        void isEmpty_returnsTrueForEmptyList() {
            assertTrue(list.isEmpty());
        }

        @Test
        @DisplayName("isEmpty() returns false for non-empty list")
        void isEmpty_returnsFalseForNonEmptyList() {
            list.add(1);
            assertFalse(list.isEmpty());
        }

        @Test
        @DisplayName("clear() removes all elements")
        void clear_removesAllElements() {
            list.add(1);
            list.add(2);
            list.add(3);

            list.clear();

            assertTrue(list.isEmpty());
            assertEquals(0, list.size());
            assertTrue(list.first().isEmpty());
            assertTrue(list.last().isEmpty());
        }
    }

    @Nested
    @DisplayName("Conversion Operations")
    class ConversionTests {

        @Test
        @DisplayName("toList() returns correct ArrayList")
        void toList_returnsCorrectList() {
            list.add(3);
            list.add(1);
            list.add(2);

            List<Integer> result = list.toList();

            assertEquals(List.of(1, 2, 3), result);
        }

        @Test
        @DisplayName("toList() returns independent copy")
        void toList_returnsIndependentCopy() {
            list.add(1);
            list.add(2);

            List<Integer> copy = list.toList();
            copy.add(100);

            assertEquals(2, list.size());
            assertFalse(list.contains(100));
        }

        @Test
        @DisplayName("toString() returns formatted string")
        void toString_returnsFormattedString() {
            list.add(3);
            list.add(1);
            list.add(2);

            assertEquals("[1, 2, 3]", list.toString());
        }

        @Test
        @DisplayName("toString() returns [] for empty list")
        void toString_returnsEmptyBracketsForEmptyList() {
            assertEquals("[]", list.toString());
        }
    }

    @Nested
    @DisplayName("Stream Operations")
    class StreamTests {

        @Test
        @DisplayName("stream() provides correct elements")
        void stream_providesCorrectElements() {
            list.add(3);
            list.add(1);
            list.add(2);

            List<Integer> streamResult = list.stream().toList();

            assertEquals(List.of(1, 2, 3), streamResult);
        }

        @Test
        @DisplayName("stream() supports filter and map")
        void stream_supportsFilterAndMap() {
            list.add(1);
            list.add(2);
            list.add(3);
            list.add(4);
            list.add(5);

            List<Integer> evenDoubled = list.stream()
                    .filter(n -> n % 2 == 0)
                    .map(n -> n * 2)
                    .toList();

            assertEquals(List.of(4, 8), evenDoubled);
        }

        @Test
        @DisplayName("stream() supports reduce")
        void stream_supportsReduce() {
            list.add(1);
            list.add(2);
            list.add(3);

            int sum = list.stream().reduce(0, Integer::sum);

            assertEquals(6, sum);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("equals() returns true for same content")
        void equals_returnsTrueForSameContent() {
            SortedLinkedList<Integer> other = SortedLinkedList.ofIntegers();

            list.add(1);
            list.add(2);
            list.add(3);

            other.add(3);
            other.add(1);
            other.add(2);

            assertEquals(list, other);
        }

        @Test
        @DisplayName("equals() returns false for different content")
        void equals_returnsFalseForDifferentContent() {
            SortedLinkedList<Integer> other = SortedLinkedList.ofIntegers();

            list.add(1);
            list.add(2);

            other.add(1);
            other.add(3);

            assertNotEquals(list, other);
        }

        @Test
        @DisplayName("equals() returns false for different size")
        void equals_returnsFalseForDifferentSize() {
            SortedLinkedList<Integer> other = SortedLinkedList.ofIntegers();

            list.add(1);
            list.add(2);

            other.add(1);

            assertNotEquals(list, other);
        }

        @Test
        @DisplayName("hashCode() is consistent with equals")
        void hashCode_consistentWithEquals() {
            SortedLinkedList<Integer> other = SortedLinkedList.ofIntegers();

            list.add(1);
            list.add(2);

            other.add(2);
            other.add(1);

            assertEquals(list.hashCode(), other.hashCode());
        }
    }
}
