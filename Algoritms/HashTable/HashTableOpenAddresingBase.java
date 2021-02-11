package HashTable;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

//this a base class for hash tables with an open addressing collision resolution method such as linear
//probing, quadratic and double hashing.
public abstract class HashTableOpenAddresingBase<K, V> implements Iterable<K> {

	protected double loadFactor;
	protected int capacity, threshold, modificationCount = 0;

	// 'usedBukects' counts the total number of used buckets inside the
	// hash-table (includes cells marked as deleted). while 'keyCount'
	// tracks the number of unique keys currently inside the hash-table
	protected int usedBukects, keyCount;

	// arrays the store the key-value pairs
	protected K[] keys;
	protected V[] values;

	// special marker token used to indicate the deletion of a key-value pair
	@SuppressWarnings("unchecked")
	protected final K TOMBSTONE = (K) (new Object());

	private static final int DEFAULT_CAPACITY = 7;
	private static final double DEFAULT_LOAD_FACTOR = 0.65;

	protected HashTableOpenAddresingBase() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	protected HashTableOpenAddresingBase(int capacity) {
		this(capacity, DEFAULT_LOAD_FACTOR);
	}

	// designated constructor
	@SuppressWarnings("unchecked")
	protected HashTableOpenAddresingBase(int capacity, double loadFactor) {
		if (capacity <= 0)
			throw new IllegalArgumentException("Illegal capacity: " + capacity);

		if (loadFactor <= 0 || Double.isNaN(loadFactor) || Double.isInfinite(loadFactor))
			throw new IllegalArgumentException("illegal loadFactor: " + loadFactor);

		this.loadFactor = loadFactor;
		this.capacity = Math.max(DEFAULT_CAPACITY, capacity);
		adjustCapacity();

		threshold = (int) (this.capacity * loadFactor);

		keys = (K[]) new Object[this.capacity];
		values = (V[]) new Object[this.capacity];
	}

	// these three methods are used to dictate how the probing is to actually
	// occur for whatever open addressing scheme you are implementing
	protected abstract void setupProbing(K key);

	protected abstract int probe(int x);

	// adjusts the capacity if the hash table after it's been made larger.
	// this is important to be able to override because the size of the hashtable
	// controls the functionality of the probing function
	protected abstract void adjustCapacity();

	// increases the capacity of the hash table.
	protected void increaseCapacity() {
		capacity = (2 * capacity) + 1;
	}

	public void clear() {
		for (int i = 0; i < capacity; i++) {
			keys[i] = null;
			values[i] = null;
		}
		keyCount = usedBukects = 0;
		modificationCount++;
	}

	// currently keys inside the hash-table
	public int size() {
		return keyCount;
	}

	// returns the capacity if the hash table
	public int getCapacity() {
		return capacity;
	}

	public boolean isEmpty() {
		return keyCount == 0;
	}

	public V put(K key, V value) {
		return insert(key, value);
	}

	public V add(K key, V value) {
		return insert(key, value);
	}

	// returns boolean on whether a given key exists within the hast table
	public boolean containsKey(K key) {
		return hasKey(key);
	}

	// returns a list of keys found in the hash table
	public List<K> keys() {
		List<K> hashTableKeys = new ArrayList<>(size());
		for (int i = 0; i < capacity; i++) {
			if (keys[i] != null && keys[i] != TOMBSTONE)
				hashTableKeys.add(keys[i]);
		}
		return hashTableKeys;
	}

	// returns a list of non-unique values in the hash table
	public List<V> values() {
		List<V> hashTableValues = new ArrayList<>(size());
		for (int i = 0; i < capacity; i++) {
			if (keys[i] != null && keys[i] != TOMBSTONE)
				hashTableValues.add(values[i]);
		}
		return hashTableValues;
	}

	// double the size of the hash table
	@SuppressWarnings("unchecked")
	protected void resizeTable() {
		increaseCapacity();
		adjustCapacity();

		threshold = (int) (capacity * loadFactor);
		K[] oldKeyTable = (K[]) new Object[capacity];
		V[] oldValueTable = (V[]) new Object[capacity];

		// perform key table pointer swap
		K[] keyTableTmp = keys;
		keys = oldKeyTable;
		oldKeyTable = keyTableTmp;

		// perform value table pointer swap
		V[] valuesTableTmp = values;
		values = oldValueTable;
		oldValueTable = valuesTableTmp;

		// reset the key count and buckets used since we are about to
		// re-insert all the keys into the hash-table.
		keyCount = usedBukects = 0;

		for (int i = 0; i < oldKeyTable.length; i++) {
			if (oldKeyTable[i] != null && oldKeyTable[i] != TOMBSTONE)
				insert(oldKeyTable[i], oldValueTable[i]);
			oldValueTable[i] = null;
			oldKeyTable[i] = null;
		}
	}

	// converts a hash value to an index. Essentially, this strips the negative
	// sign and places the hash value in the domian[0, capacity]
	protected final int normalizeIndex(int keyHash) {
		return (keyHash & 0x7FFFFFF) % capacity;
	}

	// finds the greatest common denominator of a and b
	protected static final int gcd(int a, int b) {
		if (b == 0)
			return a;
		return gcd(b, a % b);
	}

	// place a key-value pair into the hash-table. If the value already
	// exists inside the hash-table then the value is updated
	public V insert(K key, V val) {
		if (key == null)
			throw new IllegalArgumentException("Null key");
		if (usedBukects >= threshold)
			resizeTable();

		setupProbing(key);
		final int offset = normalizeIndex(key.hashCode());

		for (int i = offset, j = -1, x = 1;; i = normalizeIndex(offset + probe(x++))) {

			// the current slot was previously deleted
			if (keys[i] == TOMBSTONE) {
				if (j == -1)
					j = i;

				// the current cell already contains a key
			} else if (keys[i] != null) {
				// the key we're trying to insert exists in he hash-table
				// so update its value with the most recent value
				if (keys[i].equals(key)) {

					V oldValue = values[i];
					if (j == -1) {
						values[i] = val;
					} else {
						keys[i] = TOMBSTONE;
						values[i] = null;
						keys[i] = key;
						values[j] = val;
					}
					modificationCount++;
					return oldValue;
				}

				// current cell is null so an insertion/update can occur
			} else {
				// no previously encountered deleted buckets
				if (j == -1) {
					usedBukects++;
					keyCount++;
					keys[i] = key;
					values[i] = val;

					// previously seen deleted bucket. Instead of inserting
					// the new element at i where the null element is insert
					// it where the deleted token was found
				} else {
					keyCount++;
					keys[j] = key;
					values[j] = val;
				}

				modificationCount++;
				return null;

			}
		}
	}

	public boolean hasKey(K key) {
		if (key == null)
			throw new IllegalArgumentException("null key");

		setupProbing(key);

		final int offset = normalizeIndex(key.hashCode());

		// start at the original hash value and probe until we find a spot where our key
		// is or hit a null element in which our case does not exists.
		for (int i = offset, j = -1, x = 1;; i = normalizeIndex(offset + probe(x++))) {

			// ignore deleted cells, but record where the first index
			// of a deleted cell is found to perform lazy relocation later.
			if (keys[i] == TOMBSTONE) {
				if (j == -1)
					j = i;
				// we hit a non-null key, perhaps it's the one we're looking for.
			} else if (keys[i] != null) {
				// the key we want is in the hash-table
				if (keys[i].equals(key)) {

					// if j!=-1 this means we previously encountered a deleted cell.
					// we can perform an optimization by swapping the entries in cells.
					// i and j so that the next time we search for this key it will be
					// found faster. this is called lazy deletion/relocation
					if (j != -1) {
						// swap the key-value pairs of position i and j.
						keys[i] = keys[i];
						values[j] = values[j];
						keys[i] = TOMBSTONE;
						values[i] = null;
					}
					return true;
				}

				// key was not found in the hash-table
			} else
				return false;
		}
	}

	// get the value associated with the input key.
	// NOTE: returns null if the value is null and also returns
	// null if the key does not exists.
	public V get(K key) {
		if (key == null)
			throw new IllegalArgumentException("Null key");

		setupProbing(key);
		final int offset = normalizeIndex(key.hashCode());

		// start at the original hash value and probe until we find a spot where our key
		// is or we hit a null element in which case our element does not exist.
		for (int i = offset, j = -1, x = 1;; i = normalizeIndex(offset + probe(x++))) {

			// Ignore deleted cells, but record where the first index
			// of a deleted cell is found to perform lazy relocation later.
			if (keys[i] == TOMBSTONE) {

				if (j == -1)
					j = i;

				// we hit a non-null key, perhaps it's the one we're looking for
			} else if (keys[i] != null) {

				// the key we want is in the hash-table
				if (keys[i].equals(key)) {

					// if j!=-1 this means we previously encountered a deleted cell.
					// we can perform an optimization by swapping the entries in cells/
					// i and j so that the next time we search for this key it will be
					// found faster. this is called lazy deletion/relocation
					if (j != -1) {

						// swap key-values pairs at indexes i and j
						keys[j] = keys[i];
						values[j] = values[i];
						keys[i] = TOMBSTONE;
						values[i] = null;
						return values[j];
					} else {
						return values[i];
					}
				}
				// element was not found in the hash-table
			} else
				return null;
		}
	}

	// removes a key from the map and returns the value.
	// NOTE: returns null if the value is null and also returns
	// null if the key does not exists.
	public V remove(K key) {
		if (key == null)
			throw new IllegalArgumentException("null key");

		setupProbing(key);
		final int offset = normalizeIndex(key.hashCode());

		// starting at the original hash probe until we find a spot where our key is
		// or we hit a null element in which case our element does not exist
		for (int i = offset, x = 1;; i = normalizeIndex(offset + probe(x++))) {

			// ignore deleted cells
			if (keys[i] == TOMBSTONE)
				continue;

			// key was not found in hash-table
			if (keys[i] == null)
				return null;

			// the key we want to remove is in the hash-table
			if (keys[i].equals(key)) {
				keyCount--;
				modificationCount++;
				V oldValue = values[i];
				keys[i] = TOMBSTONE;
				values[i] = null;
				return oldValue;
			}
		}
	}

	// return a string view of this hash-table
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0; i < capacity; i++) {
			if (keys[i] != null && keys[i] != TOMBSTONE)
				sb.append(keys[i] + " => " + values[i] + ",");
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	public Iterator<K> iterator() {
		// before the iteration begins record the number of modifications
		// done to he hash-table. This value should not change as we iterate
		// otherwise a concurrent modification has occurred
		final int MODIFICATION_COUNT = modificationCount;

		return new Iterator<K>() {
			int index, keysLeft = keyCount;

			@Override
			public boolean hasNext() {
				// the contents of the table have been altered
				if (MODIFICATION_COUNT != modificationCount)
					throw new ConcurrentModificationException();
				return keysLeft != 0;
			}

			// find the next element and return it
			@Override
			public K next() {
				while (keys[index] == null || keys[index] == TOMBSTONE)
					index++;
				keysLeft--;
				return keys[index++];
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
