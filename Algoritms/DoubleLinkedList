package DoubleLinkedList;

import java.util.Iterator;

public class DoubleLinkedList<t> implements Iterable<t> {

	private int size = 0;
	private Node<t> head = null, tail = null;

	@SuppressWarnings("hiding")
	private class Node<t> {
		t data;
		Node<t> prev, next;

		public Node(t data, Node<t> prev, Node<t> next) {
			super();
			this.data = data;
			this.prev = prev;
			this.next = next;
		}

		@Override
		public String toString() {
			return data.toString();
		}
	}

	public void clear() {
		Node<t> trav = head;
		while (trav != null) {
			Node<t> next = trav.next;
			trav.prev = trav.next = null;
			trav.data = null;
			trav = next;
		}
		head = tail = trav = null;
		size = 0;
	}

	public int getSize() {
		return size;
	}

	public boolean isEmpty() {
		return getSize() == 0;
	}

	// BY DEFAULT THIS ONE IS CHOOSE AND GOES TO THE TAIL
	public void add(t elem) {
		addLast(elem);
	}

	// ADDING TO THE BEGINNING OF THIS LINKED LIST
	public void addFirst(t elem) {
		if (isEmpty()) {
			head = tail = new Node<t>(elem, null, null);
		} else {
			head.prev = new Node<t>(elem, null, head);
			head = head.prev;
		}
		size++;
	}

	// ADDING TO THE LAST TAIL
	public void addLast(t elem) {
		if (isEmpty()) {
			head = tail = new Node<t>(elem, tail, null);
		} else {
			tail.next = new Node<t>(elem, tail, null);
			tail = tail.next;
		}
		size++;
	}

	// GET VALUE OF THE FIRST NODE
	public t peekFirst() {
		if (isEmpty())
			throw new RuntimeException("Empty list");
		return head.data;
	}

	// GET VALUE OF THE LAST NODE
	public t peekLast() {
		if (isEmpty())
			throw new RuntimeException("Empty list");
		return tail.data;
	}

	// REMOVING AT THE BEGINNING
	public t removeFirst() {
		if (isEmpty())
			throw new RuntimeException("Empty list");
		t data = head.data;
		head = head.next;
		--size;

		if (isEmpty())
			tail = null;
		else
			head.prev = null; // cleaning memory
		return data;
	}

	// REMOVING AT THE TAIL
	public t removeLast() {
		if (isEmpty())
			throw new RuntimeException("Empty list");
		t data = tail.data;
		tail = tail.prev;
		--size;

		if (isEmpty())
			head = null;
		else
			tail.next = null; // cleaning memory

		return data;
	}

	// REMOVING A CERTAIN NODE
	@SuppressWarnings("unused")
	private t remove(Node<t> node) {

		// it depends where the node will be
		if (node.prev == null)
			return removeFirst();
		if (node.next == null)
			return removeLast();

		// will skip the node
		node.next.prev = node.prev;
		node.prev.next = node.next;

		t data = node.data;

		// cleaning the memory
		node.data = null;
		node = node.prev = node.next = null;

		--size;

		return data;

	}

	// REMOVING USING AN INDEX
	public t removeAt(int index) {
		if (index < 0 || index >= size)
			throw new IllegalArgumentException();

		int i;
		Node<t> trav;

		if (index < size / 2) {
			for (i = 0, trav = head; i != index; i++)
				trav = trav.next;
		} else {
			for (i = size - 1, trav = tail; i != index; i--)
				trav = trav.prev;
		}
		return remove(trav);
	}

	// REMOVING WITH A PARTICULAR OBJECT
	public boolean remove(Object obj) {
		Node<t> trav = head;

		if (obj == null) {
			for (trav = head; trav != null; trav = trav.next) {
				if (trav.data == null) {
					remove(trav);
					return true;
				}
			}
		} else {
			for (trav = head; trav != null; trav = trav.next) {
				if (obj.equals(trav.data)) {
					remove(trav);
					return true;
				}
			}
		}

		return false;
	}

	// FIND AN INDEX OF A CERTAIN OBJECT
	public int indexOf(Object obj) {
		int index = 0;
		Node<t> trav = head;

		if (obj == null) {
			for (trav = head; trav != null; trav = trav.next, index++)
				if (trav.data == null)
					return index;
		} else {
			for (trav = head; trav != null; trav = trav.next, index++)
				if (obj.equals(trav.data))
					return index;
		}
		return -1;

	}

	public boolean contains(Object obj) {
		return indexOf(obj) != -1;
	}

	@Override
	public Iterator<t> iterator() {
		return new Iterator<t>() {
			private Node<t> trav = head;

			@Override
			public boolean hasNext() {
				return trav != null;
			}

			@Override
			public t next() {
				t data = trav.data;
				trav = trav.next;
				return data;
			}
		};
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		Node<t> trav = head;
		while (trav != null) {
			sb.append(trav.data + ", ");
			trav = trav.next;
		}
		sb.append(" ]");
		return sb.toString();
	}

}
