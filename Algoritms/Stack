package Stack;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.LinkedList;

public class Stack<t> implements Iterable<t> {

	LinkedList<t> list = new LinkedList<t>();

	public Stack() {
		super();
	}

	public Stack(t firstElem) {

	}

	public int getSize() {
		return list.size();
	}

	public boolean isEmpty() {
		return getSize() == 0;
	}

	public void push(t elem) {
		list.addLast(elem);
	}

	public t pop(t elem) {
		if (isEmpty())
			throw new EmptyStackException();
		return list.removeLast();
	}

	public t peek() {
		if (isEmpty())
			throw new EmptyStackException();
		return list.peekLast();
	}

	@Override
	public Iterator<t> iterator() {
		return list.iterator();
	}

}
