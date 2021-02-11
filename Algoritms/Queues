package Queue;

import java.util.Iterator;
import java.util.LinkedList;

public class Queue<t> implements Iterable<t> {

	LinkedList<t> list = new LinkedList<t>();

	public Queue(t fisrtElem) {
		offer(fisrtElem);
	}

	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public t peek() {
		if (isEmpty())
			throw new RuntimeException("Queue empty");
		return list.peekFirst();
	}

	public t poll() {
		if (isEmpty())
			throw new RuntimeException("Queue empty");
		return list.removeFirst();
	}
	public void offer(t elem) {
		list.addLast(elem);
	}

	@Override
	public Iterator<t> iterator() {
		return list.iterator();
	}

}
