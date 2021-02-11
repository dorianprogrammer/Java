package Queue;

import java.util.*;

public class PQueue<t extends Comparable<t>> {

	private int heapSize = 0;
	private int heapCapacity = 0;
	private List<t> heap = null; // helps tracking the elements inide

	// keeps track of the posible indeces of a node value is found in the heap
	private Map<t, TreeSet<Integer>> map = new HashMap<>();

	public PQueue() {
		this(1);
	}

	public PQueue(int sz) {
		heap = new ArrayList<>(sz);
	}

	public PQueue(t[] elems) {
		heapSize = heapCapacity = elems.length;
		heap = new ArrayList<t>(heapCapacity);

		// placing the elems
		for (int i = 0; i < heapSize; i++) {
			mapAdd(elems[i], i);
			heap.add(elems[i]);
		}
		for (int i = Math.max(0, (heapSize / 2) - 1); i >= 0; i--)
			sink(i);

	}

	// WHAT USUALLY PEOPLE DOES
	public PQueue(Collection<t> elems) {
		this(elems.size());
		for (t elem : elems)
			add(elem);
	}

	public int size() {
		return heapSize;
	}

	public boolean isEmpty() {
		return heapSize == 0;
	}

	public void clear() {
		for (int i = 0; i < heapCapacity; i++)
			heap.set(i, null);
		heapSize = 0;
		map.clear();
	}

	public t peek() { // returns the value of the element with lowest priority
		if (isEmpty())
			return null;
		return heap.get(0);
	}

	public t poll() { // removes the root of the heap
		return removeAt(0);
	}

	public boolean contains(t elem) {

		if (elem == null)
			return false;
		return map.containsKey(elem);

	}

	public void add(t elem) {

		if (elem == null)
			throw new IllegalArgumentException();
		if (heapSize < heapCapacity) {
			heap.set(heapSize, elem);
		} else {
			heap.add(elem);
			heapCapacity++;
		}
		mapAdd(elem, heapSize);

		swin(heapSize);
		heapSize++;
	}

	// testing if the value of node i <= node j
	@SuppressWarnings("unused")
	private boolean less(int i, int j) {
		t node1 = heap.get(i);
		t node2 = heap.get(j);

		return node1.compareTo(node2) <= 0;
	}

	// going up to the root
	private void swin(int k) {
		int parent = (k - 1) / 2;

		// getting to the root
		while (k > 0 && less(k, parent)) {
			swap(parent, k);
			k = parent;
			parent = (k - 1) / 2; // getting the next parent
		}
	}

	private void sink(int k) {
		while (true) {
			int left = 2 * k + 1, right = 2 * k + 2, smallest = left;

			// find which is smaller left or right
			// if right is smaller set smallest to be right
			if (right < heapSize && less(right, left))
				smallest = right;

			// stop if we're outside the bounds of the tree
			// ot stop early if we cannot sink k anymore
			if (left >= heapSize || less(k, smallest))
				break;

			swap(smallest, k);
			k = smallest;

		}
	}

	private void swap(int i, int j) {
		t i_elem = heap.get(i);
		t j_elem = heap.get(j);

		heap.set(i, i_elem);
		heap.set(j, j_elem);

		mapSwap(i_elem, j_elem, i, j);
	}

	public boolean remove(t element) {
		if (element == null)
			return false;
		Integer index = mapGet(element);
		if (index != null)
			removeAt(index);
		return index != null;
	}

	private t removeAt(int i) {
		if (isEmpty())
			return null;
		heapSize--;
		t remove_data = heap.get(i);
		swap(i, heapSize);

		heap.set(heapSize, null);
		mapRemove(remove_data, heapSize);

		if (i == heapSize)
			return remove_data;

		t elem = heap.get(i);

		sink(i);

		if (heap.get(i).equals(elem))
			swin(i);

		return remove_data;
	}

	// Recursively checks if this heap is a min heap
	// this method is just for testing purposes to make
	// sure the heap invariant is still being maintained
	// called this method with k =0 to start at the root
	public boolean isMinHeap(int k) {
		if (k >= heapSize)
			return true;
		int left = 2 * k + 1;
		int right = 2 * k + 2;

		// Making sure that the current node k is less than
		// both of itd children left, and right if they exist
		// return false otherwise to indicate an invalid heap
		if (left < heapSize && !less(k, left))
			return false;
		if (right < heapSize && !less(k, right))
			return false;

		// Recurse on both children to make sure they're also valid heaps
		return isMinHeap(left) && isMinHeap(right);
	}

	// AADING THE NODE VALUE AND ITS INDEX TO THE MAP
	private void mapAdd(t value, int index) {
		TreeSet<Integer> set = map.get(value);

		if (set == null) {
			set = new TreeSet<>();
			set.add(index);
			map.put(value, set);
		} else
			set.add(index);
	}

	// REMOVING THE INDEX AT A GIVEN VALUE
	private void mapRemove(t value, int index) {
		TreeSet<Integer> set = map.get(value);
		set.remove(index);
		if (set.size() == 0)
			map.remove(value);
	}

	private Integer mapGet(t value) { // gets back an index position in the heap for the given value
		TreeSet<Integer> set = map.get(value);
		if (set != null)
			return set.last();
		return null;
	}

	private void mapSwap(t val1, t val2, int val1Index, int val2Index) {
		Set<Integer> set1 = map.get(val1);
		Set<Integer> set2 = map.get(val2);

		set1.remove(val1Index);
		set2.remove(val2Index);

		set1.add(val2Index);
		set2.add(val1Index);
	}

	@Override
	public String toString() {
		return heap.toString();
	}
}
