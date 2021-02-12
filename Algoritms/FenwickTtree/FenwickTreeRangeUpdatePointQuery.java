package FenwickTtree;

//a fenwick Tree implementation which supports range updates and point queries

public class FenwickTreeRangeUpdatePointQuery {

	// the size of the array holding the fenwick tree values
	final int N;

	// this array contains the original fenwick tree range
	// values from when it was first created
	private long[] originalTree;

	// the current tree will contain the updated range values
	private long[] currentTree;

	// construct a fenwick tree with an initial set of values.
	// the 'value' array MUST BE ONE BASED meaning values[0]
	// does not get used, O(n) construction
	public FenwickTreeRangeUpdatePointQuery(long[] values) {
		if (values == null)
			throw new IllegalArgumentException("values array cannot be null");

		N = values.length;
		values[0] = 0L;

		// making a clone of the values array since we manipulate
		// the array in place destroying all its original content
		long[] fenwickTree = values.clone();

		for (int i = 1; i < N; i++) {
			int parent = i + lsb(i);
			if (parent < N)
				fenwickTree[parent] += fenwickTree[i];
		}
		originalTree = fenwickTree;
		currentTree = fenwickTree.clone();
	}

	// update the interval [left, right] with the value 'val', O(log(n))
	public void updateRange(int left, int right, long val) {
		add(left, +val);
		add(right + 1, -val);
	}

	// add 'v' to index 'i' and all the ranges responsible for 'i', O(log(n))
	private void add(int i, long v) {
		while (i < N) {
			currentTree[i] += v;
			i += lsb(i);
		}
	}

	// get the value at a specific index. the logic behind this method is the
	// same as finding the prefix sum in a fenwick tree except that you need to
	// take the difference between the current tree and original to get
	// the point value
	public long get(int i) {
		return prefixSum(i, currentTree) - prefixSum(i - 1, originalTree);
	}

	// computes the prefix sum from [1, i], O(log(n))
	private long prefixSum(int i, long[] tree) {
		long sum = 0L;
		while (i != 0) {
			sum += tree[i];
			i &= ~lsb(i); // equivalently, i -= lsb(i)
		}
		return sum;
	}

	// Returns the value of the least significant bit (LSB)
	// lsb(108) = lsb(0b1101100) = 0b100 = 4
	// lsb(104) = lsb(0b1101000) = 0b1000 = 8
	// lsb(96) = lsb(0b1100000) = 0b100000 = 32
	// lsb(64) = lsb(0b1000000) = 0b1000000 = 64
	private static int lsb(int i) {
		// isolates the lowest one bit value
		return i & -i;

		// An alternative method is to use the Java's built in method
		// return Integer.lowestOneBit(i);
	}
}
