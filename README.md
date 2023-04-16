# Bloom Filter Implementation in Java

This repository contains a Java implementation of a Bloom Filter, a probabilistic data structure used for efficient set membership testing. The Bloom Filter is implemented using Java's BitSet class and provides a space-efficient way to store a large number of elements while minimizing the number of false positives.

## Usage

To use the Bloom Filter, simply create a new instance of the BloomFilter class with the desired number of expected elements and false positive rate:
```
int expectedElements = 1000000;
double falsePositiveRate = 0.01;
BloomFilter<String> filter = new BloomFilter<>(expectedElements, falsePositiveRate);
```

You can then add elements to the filter using the add method:
```
filter.add("example");
```

To check if an element is in the filter, use the contains method:
```
if (filter.contains("example")) {
    System.out.println("Element is in the filter");
} else {
    System.out.println("Element is not in the filter");
}
```

You can also calculate the false positive rate of the filter using the falsePositiveRate method:
```
double actualFalsePositiveRate = filter.falsePositiveRate();
System.out.println("Actual false positive rate: " + actualFalsePositiveRate);
```

## Implementation Details

The Bloom Filter implementation includes a hash function that maps elements to a set of indices in the BitSet, and multiple hash functions can be used to reduce
the false positive rate. The implementation also includes a test suite that verifies the correctness and performance of the Bloom Filter.

Murmur3 hash function and Fowler–Noll–Vo hash function were used. 

## Applications

This Bloom Filter implementation in Java is useful for developers who need to efficiently test set membership in large datasets, such as in web caching, network routers, and database systems. The Bloom Filter provides a lightweight and scalable solution for these use cases.

## License

This repository is licensed under the MIT License. See the LICENSE file for details.
