package com.shipmonk.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for edge cases and iterator behavior.
 */
@DisplayName("SortedLinkedList Edge Cases")
class SortedLinkedListEdgeCasesTest {

    @Nested
    @DisplayName("Iterator Behavior")
    class IteratorTests {

        @Test
        @DisplayName("iterator() traverses all elements in order")
        void iterator_traversesInOrder() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();
            list.add(3);
            list.add(1);
            list.add(2);

            Iterator<Integer> iterator = list.iterator();

            assertTrue(iterator.hasNext());
            assertEquals(1, iterator.next());
            assertTrue(iterator.hasNext());
            assertEquals(2, iterator.next());
            assertTrue(iterator.hasNext());
            assertEquals(3, iterator.next());
            assertFalse(iterator.hasNext());
        }

        @Test
        @DisplayName("iterator() throws NoSuchElementException when exhausted")
        void iterator_throwsWhenExhausted() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();
            list.add(1);

            Iterator<Integer> iterator = list.iterator();
            iterator.next();

            assertThrows(NoSuchElementException.class, iterator::next);
        }

        @Test
        @DisplayName("iterator() supports for-each loop")
        void iterator_supportsForEach() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();
            list.add(3);
            list.add(1);
            list.add(2);

            int sum = 0;
            for (Integer value : list) {
                sum += value;
            }

            assertEquals(6, sum);
        }

        @Test
        @DisplayName("iterator().remove() removes current element")
        void iteratorRemove_removesCurrentElement() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();
            list.add(1);
            list.add(2);
            list.add(3);

            Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() == 2) {
                    iterator.remove();
                }
            }

            assertEquals(2, list.size());
            assertFalse(list.contains(2));
        }

        @Test
        @DisplayName("iterator().remove() throws without prior next()")
        void iteratorRemove_throwsWithoutNext() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();
            list.add(1);

            Iterator<Integer> iterator = list.iterator();

            assertThrows(IllegalStateException.class, iterator::remove);
        }

        @Test
        @DisplayName("iterator() detects concurrent modification on add")
        void iterator_detectsConcurrentModificationOnAdd() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();
            list.add(1);
            list.add(2);

            Iterator<Integer> iterator = list.iterator();
            iterator.next();

            list.add(3); // Modify during iteration

            assertThrows(ConcurrentModificationException.class, iterator::next);
        }

        @Test
        @DisplayName("iterator() detects concurrent modification on remove")
        void iterator_detectsConcurrentModificationOnRemove() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();
            list.add(1);
            list.add(2);

            Iterator<Integer> iterator = list.iterator();
            iterator.next();

            list.remove(2); // Modify during iteration

            assertThrows(ConcurrentModificationException.class, iterator::next);
        }

        @Test
        @DisplayName("iterator() detects concurrent modification on clear")
        void iterator_detectsConcurrentModificationOnClear() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();
            list.add(1);
            list.add(2);

            Iterator<Integer> iterator = list.iterator();
            iterator.next();

            list.clear(); // Modify during iteration

            assertThrows(ConcurrentModificationException.class, iterator::next);
        }

        @Test
        @DisplayName("iterator() works on empty list")
        void iterator_worksOnEmptyList() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();
            Iterator<Integer> iterator = list.iterator();

            assertFalse(iterator.hasNext());
        }
    }

    @Nested
    @DisplayName("Edge Cases - Empty List")
    class EmptyListTests {

        @Test
        @DisplayName("operations on empty list work correctly")
        void emptyList_operationsWork() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();

            assertTrue(list.isEmpty());
            assertEquals(0, list.size());
            assertTrue(list.first().isEmpty());
            assertTrue(list.last().isEmpty());
            assertFalse(list.contains(1));
            assertFalse(list.remove(1));
            assertEquals(0, list.removeAll(1));
            assertEquals(0, list.count(1));
            assertEquals(-1, list.indexOf(1));
            assertTrue(list.toList().isEmpty());
            assertEquals("[]", list.toString());
        }

        @Test
        @DisplayName("clear() on empty list works")
        void clear_onEmptyListWorks() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();

            assertDoesNotThrow(list::clear);
            assertTrue(list.isEmpty());
        }
    }

    @Nested
    @DisplayName("Edge Cases - Single Element")
    class SingleElementTests {

        @Test
        @DisplayName("single element list operations work correctly")
        void singleElement_operationsWork() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();
            list.add(42);

            assertFalse(list.isEmpty());
            assertEquals(1, list.size());
            assertEquals(42, list.first().orElseThrow());
            assertEquals(42, list.last().orElseThrow());
            assertTrue(list.contains(42));
            assertEquals(42, list.get(0));
            assertEquals(0, list.indexOf(42));
            assertEquals(1, list.count(42));
        }

        @Test
        @DisplayName("removing single element results in empty list")
        void removeSingleElement_resultsInEmptyList() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();
            list.add(42);

            assertTrue(list.remove(42));

            assertTrue(list.isEmpty());
            assertTrue(list.first().isEmpty());
            assertTrue(list.last().isEmpty());
        }
    }

    @Nested
    @DisplayName("Edge Cases - Large Data")
    class LargeDataTests {

        @Test
        @DisplayName("handles 1000 elements correctly")
        void handles1000Elements() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();

            // Add in reverse order
            for (int i = 1000; i >= 1; i--) {
                list.add(i);
            }

            assertEquals(1000, list.size());
            assertEquals(1, list.first().orElseThrow());
            assertEquals(1000, list.last().orElseThrow());

            // Verify sorted order
            int prev = 0;
            for (Integer value : list) {
                assertTrue(value > prev);
                prev = value;
            }
        }

        @Test
        @DisplayName("handles many duplicates correctly")
        void handlesManyDuplicates() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();

            for (int i = 0; i < 100; i++) {
                list.add(5);
            }

            assertEquals(100, list.size());
            assertEquals(100, list.count(5));
            assertEquals(5, list.first().orElseThrow());
            assertEquals(5, list.last().orElseThrow());
        }
    }

    @Nested
    @DisplayName("Edge Cases - Boundary Values")
    class BoundaryValueTests {

        @Test
        @DisplayName("Integer boundary values are handled correctly")
        void integerBoundaries_handledCorrectly() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();

            list.add(Integer.MAX_VALUE);
            list.add(Integer.MIN_VALUE);
            list.add(0);
            list.add(-1);
            list.add(1);

            assertEquals(Integer.MIN_VALUE, list.first().orElseThrow());
            assertEquals(Integer.MAX_VALUE, list.last().orElseThrow());
            assertEquals(0, list.get(2));
        }

        @Test
        @DisplayName("Empty string is handled correctly")
        void emptyString_handledCorrectly() {
            SortedLinkedList<String> list = SortedLinkedList.ofStrings();

            list.add("");
            list.add("a");
            list.add("b");

            assertEquals("", list.first().orElseThrow());
            assertTrue(list.contains(""));
            assertTrue(list.remove(""));
            assertFalse(list.contains(""));
        }
    }

    @Nested
    @DisplayName("Type Safety")
    class TypeSafetyTests {

        @Test
        @DisplayName("String list only accepts strings")
        void stringList_onlyAcceptsStrings() {
            SortedLinkedList<String> stringList = SortedLinkedList.ofStrings();

            // This compiles and works
            stringList.add("test");

            // The following would NOT compile (type safety enforced at compile time):
            // stringList.add(123);  // Compilation error
            // stringList.add(1.5);  // Compilation error

            assertEquals(1, stringList.size());
        }

        @Test
        @DisplayName("Integer list only accepts integers")
        void integerList_onlyAcceptsIntegers() {
            SortedLinkedList<Integer> intList = SortedLinkedList.ofIntegers();

            // This compiles and works
            intList.add(42);

            // The following would NOT compile (type safety enforced at compile time):
            // intList.add("test");  // Compilation error
            // intList.add(1.5);     // Compilation error

            assertEquals(1, intList.size());
        }

        @Test
        @DisplayName("Empty lists of same type are equal")
        void emptyLists_areEqual() {
            SortedLinkedList<Integer> list1 = SortedLinkedList.ofIntegers();
            SortedLinkedList<Integer> list2 = SortedLinkedList.ofIntegers();

            // Both empty and same type
            assertEquals(list1, list2);
        }
    }

    @Nested
    @DisplayName("Null Safety")
    class NullSafetyTests {

        @Test
        @DisplayName("add() rejects null")
        void add_rejectsNull() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> list.add(null)
            );

            assertEquals("Value cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("remove() rejects null")
        void remove_rejectsNull() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();

            assertThrows(NullPointerException.class, () -> list.remove(null));
        }

        @Test
        @DisplayName("contains() rejects null")
        void contains_rejectsNull() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();

            assertThrows(NullPointerException.class, () -> list.contains(null));
        }

        @Test
        @DisplayName("indexOf() rejects null")
        void indexOf_rejectsNull() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();

            assertThrows(NullPointerException.class, () -> list.indexOf(null));
        }

        @Test
        @DisplayName("count() rejects null")
        void count_rejectsNull() {
            SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();

            assertThrows(NullPointerException.class, () -> list.count(null));
        }
    }
}
