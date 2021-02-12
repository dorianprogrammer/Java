package DoubleLinkedList;

public class Main {

	public static void main(String[] args) {
		DoubleLinkedList<Integer> dl = new DoubleLinkedList<Integer>();
		
		dl.add(1);
		dl.add(2);
		dl.add(3);
		dl.add(4);
		dl.add(5);
		dl.add(6);
		dl.add(7);
		dl.add(8);
		dl.add(9);
		dl.add(10);
		dl.addFirst(7);
		dl.addLast(50);
		
//		System.out.println(dl.indexOf(9));
//		System.out.println(dl.contains(8));
//		System.out.println(dl.iterator().next());
		
//		dl.removeAt(3);
//		dl.removeFirst();
//		dl.removeLast();
//		dl.remove(1);
		
		System.out.println();
		for (Integer i : dl) {
			System.out.println(i);
		}
	}

}
