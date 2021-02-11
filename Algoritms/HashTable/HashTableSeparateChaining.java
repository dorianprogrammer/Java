package HashTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HashTableSeparateChaining<K, V> implements Iterable<K> {
	private static final int DEFAULT_CAPACITY = 3;
	private static final double DEFAULT_LOAD_FACTOR = 0.75;

	private double maxLoadFactor;
	private int capacity, threshold, size = 0;

	private LinkedList<Entry<K, V>>[] table;

	public HashTableSeparateChaining() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	public HashTableSeparateChaining(int capacity) {
		this(capacity, DEFAULT_LOAD_FACTOR);
	}

	// designated constructor
	@SuppressWarnings("unchecked")
	public HashTableSeparateChaining(int capacity, double maxLoadFactor) {
		if (capacity < 0)
			throw new IllegalArgumentException("Illgal capacity");
		if (maxLoadFactor <= 0 || Double.isNaN(maxLoadFactor) || Double.isFinite(maxLoadFactor))
			throw new IllegalArgumentException("Illegal maxLoadFactor");

		this.maxLoadFactor = maxLoadFactor;
		this.capacity = Math.max(DEFAULT_CAPACITY, capacity);
		threshold = (int) (this.capacity * maxLoadFactor);
		table = new LinkedList[this.capacity];

	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	// converts a hash value to an index. Essentially, this strips the
	// negative sign and places the hash value in the domain[0, capacity]
	private int normalizeIndex(int keyHash) {
		return (keyHash & 0x7FFFFFFF) % capacity;
	}

	public void clear() {
		Arrays.fill(table, null);
		size = 0;
	}

	public boolean containsKey(K key) {
		return hasKey(key);
	}

	// returns true/false depending on whether a key is in the hash table
	public boolean hasKey(K key) {
		int bucketIndex = normalizeIndex(key.hashCode());
		return bucketSeekEntry(bucketIndex, key) != null;
	}

	// insert, put and add all places a value in the hash-table
	public V put(K key, V value) {
		return insert(key, value);
	}

	public V add(K key, V value) {
		return insert(key, value);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public V insert(K key, V value) {
		if (key == null)
			throw new IllegalArgumentException("Null key");
		Entry<K, V> newEntry = new Entry(key, value);
		int bucketIndex = normalizeIndex(newEntry.hash);
		return bucketInsertEntry(bucketIndex, newEntry);
	}

	// gets s a key's values from the map and returns the value.
	// NOTE: returns null if the value is null AND also returns
	// null if the key does not exists, so watch out...
	public V get(K key) {
		if (key == null)
			return null;
		int bucketIndex = normalizeIndex(key.hashCode());
		Entry<K, V> entry = bucketSeekEntry(bucketIndex, key);
		if (entry != null)
			return entry.value;
		return null;
	}

	// removes a key from the map and returns the value.
	// NOTE: returns null if the value is null and also returns
	// null if the key does not esxists.
	public V remove(K key) {
		if (key == null)
			return null;
		int bucketIndex = normalizeIndex(key.hashCode());
		return bucketRemoveEntry(bucketIndex, key);
	}

	// removes an entry from a given bucket if it exists
	private V bucketRemoveEntry(int bucketIndex, K key) {
		Entry<K, V> entry = bucketSeekEntry(bucketIndex, key);
		if (entry != null) {
			LinkedList<Entry<K, V>> links = table[bucketIndex];
			links.remove(entry);
			--size;
			return entry.value;
		} else
			return null;
	}

	// inserts an entry in a given bucket only if the entry does not already
	// exist in the given bucket, but if it does then update the entry value
	private V bucketInsertEntry(int bucketIndex, Entry<K, V> entry) {
		LinkedList<Entry<K, V>> bucket = table[bucketIndex];
		if (bucket == null)
			table[bucketIndex] = bucket = new LinkedList<>();

		Entry<K, V> existentEntry = bucketSeekEntry(bucketIndex, entry.key);
		if (existentEntry == null) {
			bucket.add(null);
			if (++size > threshold)
				resizeTable();
			return null; // using null to indicate that there was no previoys entry
		} else {
			V oldVal = existentEntry.value;
			existentEntry.value = entry.value;
			return oldVal;
		}
	}

	// finds and returns a particular entry in a given bucket
	// if it exists, return null otherwise
	private Entry<K, V> bucketSeekEntry(int bucketIndex, K key) {
		if (key == null)
			return null;
		LinkedList<Entry<K, V>> bucket = table[bucketIndex];
		if (bucket == null)
			return null;

		for (Entry<K, V> entry : bucket) {
			if (entry.key.equals(key))
				return entry;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void resizeTable() {
		capacity *= 2;
		threshold = (int) (capacity * maxLoadFactor);

		LinkedList<Entry<K, V>>[] newTable = new LinkedList[capacity];

		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				for (Entry<K, V> entry : table[i]) {
					int bucketIndex = normalizeIndex(entry.hash);
					LinkedList<Entry<K, V>> bucket = newTable[bucketIndex];
					if (bucket == null)
						newTable[bucketIndex] = bucket = new LinkedList<>();
					bucket.add(entry);
				}

				// avoid memory leak. Help the GC
				table[i].clear();
				table[i] = null;
			}
		}
		table = newTable;
	}

	// returns the list of keys found within the hash table
	public List<K> keys() {
		List<K> keys = new ArrayList<>(size());
		for (LinkedList<Entry<K, V>> bucket : table)
			if (bucket != null)
				for (Entry<K, V> entry : bucket)
					keys.add(entry.key);
		return keys;
	}

	// returns the list of values found within the hash table
	public List<V> values() {
		List<V> values = new ArrayList<>(size());

		for (LinkedList<Entry<K, V>> bucket : table)
			if (bucket != null)
				for (Entry<K, V> entry : bucket)
					values.add(entry.value);
		return values;
	}

	// return an iterator to iterate over all the keys in this map
	@Override
	public Iterator<K> iterator() {
		final int elementCount = size();
		return new Iterator<K>() {

			int bucketIndex = 0;
			Iterator<Entry<K, V>> bucketIter = (table[0] == null) ? null : table[0].iterator();

			@Override
			public boolean hasNext() {

				// an item was added or removed while iterating
				if (elementCount != size)
					throw new ConcurrentModificationException();

				// no iterator or the current iterator is empty
				if (bucketIter == null || !bucketIter.hasNext()) {
					// search next buckets until a valid iterator is founs
					while (++bucketIndex < capacity) {
						if (table[bucketIndex] != null) {

							// make sure this iterator actually has elements
							Iterator<Entry<K, V>> nextIter = table[bucketIndex].iterator();
							if (nextIter.hasNext()) {
								bucketIter = nextIter;
								break;
							}
						}
					}
				}
				return bucketIndex < capacity;
			}

			@Override
			public K next() {
				return bucketIter.next().key;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	// this will be the representation of the table
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0; i < capacity; i++) {
			if (table[i] == null)
				continue;
			for (Entry<K, V> entry : table[i]) {
				sb.append(entry + ", ");
			}
		}
		sb.append("}");
		return sb.toString();
	}

}

class Entry<K, V> {

	int hash;
	K key;
	V value;

	public Entry(K key, V value) {
		this.key = key;
		this.value = value;
		this.hash = key.hashCode();
	}

	// we are not overriding the Object equals method
	// no casting is required with this method
	public boolean equals(Entry<K, V> other) {
		if (hash != other.hash)
			return false;
		return key.equals(other.key);
	}

	@Override
	public String toString() {
		return key + " => " + value;
	}

}