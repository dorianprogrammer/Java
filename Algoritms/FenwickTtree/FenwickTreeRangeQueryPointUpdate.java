package FenwickTtree;

import java.util.Arrays;
// a Fenwick  tree implementation which supports point updates and sum range queries

public class FenwickTreeRangeQueryPointUpdate {

	// the size of the array holding the Fenwick tree values
	final int N;

	// this array contains the Fenwick tree values ranges
	private long[] tree;

	// create an empty Fenwick Tree with 'sz' parameter zero base
	public FenwickTreeRangeQueryPointUpdate(int sz) {
		tree = new long[N = sz + 1];
	}

	// Construct a fenwick tree with an initial set of values.
	// the 'values' array MUST BE ONE BASED meaning values[0]
	// does not used, O(n) construction
	public FenwickTreeRangeQueryPointUpdate(long[] values) {
		if (values == null)
			throw new IllegalArgumentException("values array cannot be null");

		N = values.length;
		values[0] = 0L;

		// making a clone of the value array since we manipulate
		// the array in place destroying all its original content
		tree = values.clone();

		for (int i = 1; i < N; i++) {
			int parent = i + lsb(i);
			if (parent < N)
				tree[parent] += tree[i];
		}
	}

	// this returns the value of the least significant bit(LSB)
	// lsb(108) = lsb(0b1101100) = 0b100 = 4
	// lsb(104) = lsb(0b1101000) = 0b1000 = 8
	// lsb(96) = lsb(0b1100000) = 0b100000 = 32
	// lsb(64) = lsb(0b1000000) = 0b1000000 = 64
	private static int lsb(int i) {

		// isolates the lowest one bit value
		return i & -i;

		// an alternative method is to use the Java's built in method
		// return Integer.lowestOneBit(i)
	}

	// computes the prefix sum from [1, i], O(log(n))
	private long prefixSum(int i) {
		long sum = 0L;
		while (i != 0) {
			sum += tree[i];
			i &= ~lsb(i); // Equivalently, i -= lsb(i)
		}
		return sum;
	}

	// returns the sum of the interval [left, right], O(log(n))
	public long sum(int left, int right) {
		if (right < left)
			throw new IllegalArgumentException("Make sure right >= left");
		return prefixSum(right) - prefixSum(left - 1);
	}

	// get the value at index i
	public long get(int i) {
		return sum(i, i);
	}

	// add 'v' to index 'i', O(log(n))
	public void add(int i, long v) {
		while (i < N) {
			tree[i] += v;
			i += lsb(i);
		}
	}

	// set index i to be equal to v, O(log(n))
	public void set(int i, long v) {
		add(i, v - sum(i, i));
	}

	@Override
	public String toString() {
		return Arrays.toString(tree);
	}
}
