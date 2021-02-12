package DynArray;

import java.util.Iterator;

@SuppressWarnings("unchecked")
public class Array<t> implements Iterable<t> {
	private t[] arr;
	private int len = 0; // lenght user thinks array is
	private int capacity = 0; // actual array size

	public Array() {
		this(10);
	}

	public Array(int capacity) {
		if (capacity < 0)
			throw new IllegalArgumentException("Illegal Capacity:" + capacity);
		this.capacity = capacity;
		arr = (t[]) new Object[capacity];
	}

	// ADD AN ELEMENT
	public void add(t elem) {

		// time to resize
		if (len + 1 >= capacity) {
			if (capacity == 0)
				capacity = 1;
			else
				capacity *= 2; // double the size
			t[] new_arr = (t[]) new Object[capacity];
			for (int i = 0; i < len; i++)
				new_arr[i] = arr[i];
			arr = new_arr; // arr has extra nulls padded
		}
		arr[len++] = elem;
	}

	// REMOVING BY ITEMS
	public t removeAt(int rm_index) {
		if (rm_index >= len && rm_index < 0)
			throw new IndexOutOfBoundsException();
		t data = arr[rm_index];
		t[] new_arr = (t[]) new Object[len - 1];
		for (int i = 0, j = 0; i < len; i++, j++)
			if (i == rm_index)
				j--;
			else
				new_arr[j] = arr[i];
		arr = new_arr;
		capacity = --len;
		return data;
	}

	public boolean remove(Object obj) {
		for (int i = 0; i < len; i++) {
			if (arr[i].equals(obj)) {
				removeAt(i);
				return true;
			}
		}
		return false;
	}

	public int indexOf(Object obj) {
		for (int i = 0; i < len; i++)
			if (arr[i].equals(obj))
				return i;
		return -1;
	}

	public boolean contains(Object obj) {
		return indexOf(obj) != -1;
	}

	@Override
	public Iterator<t> iterator() {
		return new Iterator<t>() {
			int index = 0;

			@Override
			public boolean hasNext() {
				return index < len;
			}

			@Override
			public t next() {
				return arr[index++];
			}
		};
	}

	@Override
	public String toString() {
		if (len == 0)
			return "[]";
		else {
			StringBuilder sb = new StringBuilder(len).append("[");
			for (int i = 0; i < len; i++)
				sb.append(arr[i] + ", ");

			return sb.append(arr[len - 1] + "]").toString();
		}
	}

	public int getLen() {
		return len;
	}

	public boolean isEmpty() {
		return getLen() == 0;
	}

	public t get(int index) {
		return arr[index];
	}

	public void set(int index, t elem) {
		arr[index] = elem;
	}

	public void clear() {
		for (int i = 0; i < capacity; i++)
			arr[i] = null;
		len = 0;
	}

}
