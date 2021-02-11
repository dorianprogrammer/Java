package BinarySearchTree;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinarySearchTree<t extends Comparable<t>> {

	private int nodeCount = 0;
	private Node root = null;

	private class Node {
		t data;
		Node left, right;

		public Node(Node left, Node right, t elem) {
			super();
			this.data = elem;
			this.left = left;
			this.right = right;
		}

	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		return nodeCount;
	}

	public boolean contains(t elem) {
		return contains(root, elem);
	}

	// just to know if the successfully was added an insertion
	public boolean add(t elem) {

		// checking if the value already exist
		if (contains(elem)) {
			return false;

			// in case the does not exist
		} else {
			root = add(root, elem);
			nodeCount++;
			return true;
		}
	}

	// private method to recursively add a value in the binary tree
	private Node add(Node node, t elem) {

		// base case: found a leaf node
		if (node == null) {
			node = new Node(null, null, elem);
		} else {
			// placing lower elements values in left subtree
			if (elem.compareTo(node.data) < 0) {
				node.left = add(node.left, elem);
			} else {
				node.right = add(node.right, elem);
			}
		}
		return node;
	}

	// remove a value from this binary tree, if it exists
	public boolean remove(t elem) {

		// making sure the node we want to remove
		// actually exists before we remove it
		if (contains(elem)) {
			root = remove(root, elem);
			nodeCount--;
			return true;
		}

		return false;

	}

	private Node remove(Node node, t elem) {

		if (node == null)
			return null;

		int cmp = elem.compareTo(node.data);

		// dig into left subtree, the value we're looking
		// for is smaller than the current value
		if (cmp < 0) {
			node.left = remove(node.left, elem);

			// dig into right subtree, the value we're looking
			// for is greater than the current value
		} else if (cmp > 0) {
			node.right = remove(node.right, elem);

			// found the node we wish to remove
		} else {

			// this is the case with only a right subtree or
			// no subtree at all. in this situation just
			// swap the node we wish to remove with its right child
			if (node.left == null) {

				Node rightChild = node.right;
				node.data = null;
				node = null;

				return rightChild;

				// this is the case with only a left subtree or
				// no subtree at all. In this case situation just
				// swap the node we wish to remove with its left child.
			} else if (node.right == null) {

				Node leftChild = node.left;
				node.data = null;
				node = null;
				return leftChild;

				// when removing a node from a binary tree with two links the
				// succesor of the node being removed can either be the largest
				// value in the left subtree or the smallest value in the right
				// subtree. In this implementation I have decided to find the
				// smallest value in the right subtree which can be found by
				// traversing as far left as possible in the right subtree.
			} else {

				// find the leftmost node in the right subtree
				Node tmp = findMin(node.right);

				// swap the data
				node.data = tmp.data;

				// go into the right subtree and remove the leftmost node we
				// found and swapped data with. This prevents us from having
				// two nodes in our tree with the same value.
				node.right = remove(node.right, tmp.data);

				// if instead we wanted to find the largest node in the left
				// subtree as oppposed to smallest node in the right subtree
				// here is what we would do:
				// Node tmp = findMax(node.left);
				// node.data = tmp.data;
				// node.left = remove(node.left, tmp.data);
			}
		}
		return node;
	}

	// helper method to find the leftmost node (which has the smallest value)
	private Node findMin(Node node) {
		Node cur = node;
		while (cur.left != null)
			cur = cur.left;
		return cur;

	}

	// helper method to find the rightmost node (which has the largest value)
	@SuppressWarnings("unused")
	private Node findMax(Node node) {
		Node cur = node;
		while (cur.right != null)
			cur = cur.right;
		return cur;
	}

	// private recursive method to find an element in the tree
	private boolean contains(Node node, t elem) {

		// base case, reached bottom, value not found
		if (node == null)
			return false;

		int cmp = elem.compareTo(node.data);

		// dig into the left subtree because the value we're
		// looking for is smaller than the current value
		if (cmp < 0)
			return contains(node.left, elem);

		// dig into the right subtree because the value we're
		// looking for is greater than the current value
		else if (cmp > 0)
			return contains(node.right, elem);

		// we found the value we were looking for
		else
			return true;
	}

	// computes the height of the tree, O(n)
	public int height() {
		return height(root);
	}

	// recursive helper method to compute the height of the tree
	private int height(Node node) {
		if (node == null)
			return 0;
		return Math.max(height(node.left), height(node.right)) + 1;
	}

	// this method returns an iterator for given TreeTraversalOrder
	// the ways in which you can traverse the tree are in four different ways:
	// preorder, inorder, postorder and levelorder
	public Iterator<t> traverse(TreeTraversalOrder order) {
		switch (order) {
		case PRE_ORDER:
			return preOrderTraversal();
		case IN_ORDER:
			return inOrderTraversal();
		case POST_ORDER:
			return postOrderTraversal();
		case LEVEL_ORDER:
			return levelOrderTraversal();
		default:
			throw new IllegalArgumentException("Unexpected value: " + order);
		}
	}

	// returns as iterator to traverse the tree in pre order
	private Iterator<t> preOrderTraversal() {
		final int expectedNodeCount = nodeCount;
		final Stack<Node> stack = new Stack<>();

		stack.push(root);

		return new Iterator<t>() {

			@Override
			public boolean hasNext() {
				if (expectedNodeCount != nodeCount)
					throw new ConcurrentModificationException();
				return root != null && !stack.isEmpty();
			}

			@Override
			public t next() {
				if (expectedNodeCount != nodeCount)
					throw new ConcurrentModificationException();
				Node node = stack.pop();
				if (node.right != null)
					stack.push(node.right);
				if (node.right != null)
					stack.push(node.left);
				return node.data;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	private Iterator<t> inOrderTraversal() {
		final int expectedNodeCount = nodeCount;
		final Stack<Node> stack = new Stack<>();
		stack.push(root);

		return new Iterator<t>() {

			Node trav = root;

			@Override
			public boolean hasNext() {
				if (expectedNodeCount != nodeCount)
					throw new ConcurrentModificationException();
				return root != null && !stack.isEmpty();
			}

			@Override
			public t next() {
				if (expectedNodeCount != nodeCount)
					throw new ConcurrentModificationException();

				// dig left
				while (trav != null && trav.left != null) {
					stack.push(trav.left);
					trav = trav.left;
				}

				Node node = stack.pop();

				// try moving down right once
				if (node.right != null) {
					stack.push(node.right);
					trav = node.right;
				}

				return node.data;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	// returns as iterator to traverse the tree in post order
	private Iterator<t> postOrderTraversal() {
		final int expectedNodeCount = nodeCount;
		final Stack<Node> stack1 = new Stack<>();
		final Stack<Node> stack2 = new Stack<>();
		stack1.push(root);

		while (!stack1.isEmpty()) {
			Node node = stack1.pop();
			if (node != null) {
				stack2.push(node);
				if (node.left != null)
					stack1.push(node.left);
				if (node.right != null)
					stack1.push(node.right);
			}
		}

		return new Iterator<t>() {

			@Override
			public boolean hasNext() {
				if (expectedNodeCount != nodeCount)
					throw new ConcurrentModificationException();
				return root != null && !stack2.isEmpty();
			}

			@Override
			public t next() {
				if (expectedNodeCount != nodeCount)
					throw new ConcurrentModificationException();
				return stack2.pop().data;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	// returns as iterator to traverse the tree in level order
	private Iterator<t> levelOrderTraversal() {
		final int expectedNodeCount = nodeCount;
		final Queue<Node> queue = new LinkedList<>();
		queue.offer(root);

		return new Iterator<t>() {

			@Override
			public boolean hasNext() {
				if (expectedNodeCount != nodeCount)
					throw new ConcurrentModificationException();
				return root != null && !queue.isEmpty();
			}

			@Override
			public t next() {
				if (expectedNodeCount != nodeCount)
					throw new ConcurrentModificationException();
				Node node = queue.poll();
				if (node.left != null)
					queue.offer(node.left);
				if (node.right != null)
					queue.offer(node.right);
				return node.data;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public enum TreeTraversalOrder {
		PRE_ORDER, IN_ORDER, POST_ORDER, LEVEL_ORDER
	}
}
