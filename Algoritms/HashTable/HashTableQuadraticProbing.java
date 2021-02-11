package HashTable;

//an implementation of a hash table using open addressing with quadratic probing as a collision resolution method
//

//In this implementation we are using the following probing functions: H(k, x) = h(k) + f(x) mod 2^n

//where h(k) is the hash for the given key, f(x) = (x + x^2)/2 and n is a natural number. we
//are using this probing function because it is guaranteed to find an empty cell(it generates all
//the numbers in the range[0, 2^n])without repetition for the first 2^n numbers.
public class HashTableQuadraticProbing<K, V> extends HashTableOpenAddresingBase<K, V> {

	public HashTableQuadraticProbing() {
		super();
	}

	public HashTableQuadraticProbing(int capacity) {
		super(capacity);
	}

	// designated constructor
	public HashTableQuadraticProbing(int capacity, int loadFactor) {
		super(capacity, loadFactor);
	}

	// given a number this method finds the next
	// power of two above this value
	private static int nextPowerOfTwo(int n) {
		return Integer.highestOneBit(n) << 1;
	}

	// no set up required for quadratic probing
	@Override
	protected void setupProbing(K key) {
	}

	@Override
	protected int probe(int x) {
		// quadratic probing function (x^2+x)/2
		return (x * x + x) >> 1;
	}

	// increase the capacity of the hash table to the next power of two
	@Override
	protected void increaseCapacity() {
		capacity = nextPowerOfTwo(capacity);
	}

	// adjust the capacity of the hash table to be a power of two
	@Override
	protected void adjustCapacity() {
		int pow2 = Integer.highestOneBit(capacity);
		if (capacity == pow2)
			return;
		increaseCapacity();
	}

}
