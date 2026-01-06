# SortedLinkedList

A type-safe, sorted linked list implementation in Java 21.

## Overview

`SortedLinkedList` is a library providing a linked list that automatically maintains elements in sorted order. It supports only `String` or `Integer` values (not both in the same list), enforced at compile time through factory methods.

## Features

- **Type Safety**: Factory methods ensure only `String` or `Integer` lists can be created
- **Automatic Sorting**: Elements are always maintained in ascending natural order
- **Duplicates Allowed**: Same value can appear multiple times (like a standard List)
- **Null Safety**: Null values are rejected with `NullPointerException`
- **Fail-Fast Iterator**: Detects concurrent modifications during iteration
- **Stream API Support**: Full integration with Java Stream API
- **Modern Java**: Built with Java 21 features

## Quick Start

### Creating a List

```java
import com.shipmonk.collection.SortedLinkedList;

// For Integer values
SortedLinkedList<Integer> numbers = SortedLinkedList.ofIntegers();

// For String values
SortedLinkedList<String> words = SortedLinkedList.ofStrings();
```

### Basic Operations

```java
SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();

// Adding elements (automatically sorted)
list.add(5);
list.add(1);
list.add(9);
list.add(3);
// List is now: [1, 3, 5, 9]

// Querying
list.contains(5);     // true
list.size();          // 4
list.isEmpty();       // false
list.first();         // Optional[1]
list.last();          // Optional[9]
list.get(2);          // 5 (0-indexed)
list.indexOf(5);      // 2

// Removing
list.remove(5);       // removes first occurrence
list.removeAll(3);    // removes all occurrences of 3

// Clearing
list.clear();
```

### Working with Duplicates

```java
SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();

list.add(5);
list.add(5);
list.add(5);

list.count(5);        // 3
list.removeAll(5);    // returns 3, list is now empty
```

### Iteration

```java
SortedLinkedList<String> list = SortedLinkedList.ofStrings();
list.add("banana");
list.add("apple");
list.add("cherry");

// For-each loop
for (String fruit : list) {
    System.out.println(fruit);
}
// Output: apple, banana, cherry

// Stream API
list.stream()
    .filter(s -> s.startsWith("a"))
    .forEach(System.out::println);
// Output: apple
```

### Converting to Other Collections

```java
SortedLinkedList<Integer> list = SortedLinkedList.ofIntegers();
list.add(3);
list.add(1);
list.add(2);

// To ArrayList (returns independent copy)
List<Integer> arrayList = list.toList();
// arrayList is [1, 2, 3]
```

## API Reference

### Factory Methods

| Method | Description |
|--------|-------------|
| `SortedLinkedList.ofStrings()` | Creates empty list for String values |
| `SortedLinkedList.ofIntegers()` | Creates empty list for Integer values |

### Core Operations

| Method | Complexity | Description |
|--------|------------|-------------|
| `add(T value)` | O(n) | Insert value maintaining sorted order |
| `remove(T value)` | O(n) | Remove first occurrence, returns boolean |
| `removeAll(T value)` | O(n) | Remove all occurrences, returns count |
| `contains(T value)` | O(n) | Check if value exists |
| `get(int index)` | O(n) | Get element by index |
| `indexOf(T value)` | O(n) | Get index of first occurrence |
| `count(T value)` | O(n) | Count occurrences of value |

### Query Operations

| Method | Complexity | Description |
|--------|------------|-------------|
| `first()` | O(1) | Returns `Optional<T>` of smallest element |
| `last()` | O(1) | Returns `Optional<T>` of largest element |
| `size()` | O(1) | Returns number of elements |
| `isEmpty()` | O(1) | Returns true if list is empty |

### Conversion Operations

| Method | Description |
|--------|-------------|
| `toList()` | Returns independent `ArrayList<T>` copy |
| `stream()` | Returns `Stream<T>` for functional operations |
| `iterator()` | Returns fail-fast `Iterator<T>` |
| `toString()` | Returns string representation like `[1, 2, 3]` |

## Design Decisions

### Why Factory Methods?

The requirement states the list should hold "string or int values, but not both." Factory methods (`ofStrings()`, `ofIntegers()`) enforce this at compile time:

```java
// This compiles - correct usage
SortedLinkedList<Integer> intList = SortedLinkedList.ofIntegers();

// These would NOT compile - type safety enforced
// SortedLinkedList<Double> doubleList = SortedLinkedList.ofDoubles(); // No such method
// intList.add("string"); // Compilation error
```

### Why Doubly-Linked List?

Using a doubly-linked list allows O(1) removal once a node is found, and enables efficient traversal from both ends.

### Why Allow Duplicates?

This is a **List**, not a **Set**. Lists traditionally allow duplicates. If unique sorted values were needed, Java's `TreeSet` would be more appropriate.

### Why `Optional<T>` for first()/last()?

Using `Optional` instead of returning `null` makes the API safer and more explicit about the possibility of an empty list.

## Building

Requires Java 21 and Maven 3.6+.

```bash
# Compile
mvn compile

# Run tests
mvn test

# Package as JAR
mvn package
```

## Project Structure

```
src/
├── main/java/com/shipmonk/collection/
│   ├── SortedLinkedList.java    # Main implementation
│   └── Node.java                # Internal node class
└── test/java/com/shipmonk/collection/
    ├── SortedLinkedListIntegerTest.java
    ├── SortedLinkedListStringTest.java
    └── SortedLinkedListEdgeCasesTest.java
```

## Testing

The library includes comprehensive tests covering:

- Basic operations (add, remove, contains)
- Edge cases (empty list, single element, boundary values)
- Iterator behavior and fail-fast detection
- Stream API integration
- Null safety
- Type safety

```bash
mvn test
```

## License

MIT License
