package BalancedBinaryTree;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Stack;

import utils.TreePrinter;
import utils.TreePrinter.PrintableNode;

public class AVLTreeRecursive<T extends Comparable<T>> implements Iterable<T> {

	public class Node implements PrintableNode {

		public int bf; // 'bf' is short for balance factor
		public T value; // the value/data contained within the one
		public int height; // the height of this node in the tree
		public Node left, right; // the left and the right children of this node

		public Node(T value) {
			this.value = value;
		}

		@Override
		public PrintableNode getLeft() {
			return left;
		}

		@Override
		public PrintableNode getRight() {
			return right;
		}

		@Override
		public String getText() {
			return value.toString();
		}
	}

	public Node root; // the root of the AVL tree
	private int nodeCount; // tracks the number of nodes inside the tree

	// the height of a rooted tree is the number of edges between the tree's
	// root and its furthest leaf. this means that a tree containing a single
	// node has a height of 0
	public int height() {
		if (root == null)
			return 0;
		return root.height;
	}

	public int size() { // returns th number of nodes in the tree
		return nodeCount;
	}

	public boolean isEmpty() {// returns whether or not the tree is empty
		return size() == 0;
	}

	public boolean contains(T value) {// return true/false depending on whether a value exists in the tree
		return contains(root, value);
	}

	private boolean contains(Node node, T value) { // recursive method helper method
		if (node == null)
			return false;

		// compare current value to the value in the node
		int cmp = value.compareTo(node.value);

		// dig into left subtree
		if (cmp < 0)
			return contains(node.left, value);

		// dig into right subtree
		if (cmp > 0)
			return contains(node.right, value);

		// found value in tree
		return true;
	}

	public boolean insert(T value) {
		if (value == null)
			return false;
		if (!contains(root, value)) {
			root = insert(root, value);
			nodeCount++;
			return true;
		}
		return false;
	}

	private Node insert(Node node, T value) { // inserts a value inside the AVL tree
		if (node == null) // base case.
			return new Node(value);

		// compare current value to the value in the node
		int cmp = value.compareTo(node.value);

		// insert node in left subtree
		if (cmp < 0) {
			node.left = insert(node.left, value);

			// insert node in right subtree
		} else {
			node.right = insert(node.right, value);
		}

		update(node); // update balance factor and height values.

		return balance(node); // re-balance tree
	}

	private void update(Node node) { // update a node's height and balance factor
		int leftNodeHeight = (node.left == null) ? -1 : node.left.height;
		int rightNodeHeight = (node.right == null) ? -1 : node.right.height;

		// update this node's height
		node.height = 1 + Math.max(leftNodeHeight, rightNodeHeight);

		// update balance factor
		node.bf = rightNodeHeight - leftNodeHeight;
	}

	// Re-balance a node if its balance factor is +2 or -2
	private Node balance(Node node) {
		if (node.bf == -2) { // left heavy subtree
			if (node.left.bf <= 0) { // left-left case
				return leftLeftCase(node);
			} else { // left-right case
				return lefRightCase(node);
			}
		} else if (node.bf == +2) { // right heavy subtree needs balancing
			if (node.right.bf >= 0) { // right-right case
				return rightRightCase(node);
			} else { // right-left case.
				return rightLeftCase(node);
			}
		}
		return node; // node either has a balance factor of 0, +1 or -1 which is fine
	}

	private Node leftLeftCase(Node node) {
		return rightRotation(node);
	}

	private Node lefRightCase(Node node) {
		node.left = leftRotation(node.left);
		return leftLeftCase(node);
	}

	private Node rightRightCase(Node node) {
		return leftRotation(node);
	}

	private Node rightLeftCase(Node node) {
		node.right = rightRotation(node.right);
		return rightRightCase(node);
	}

	private Node leftRotation(Node node) {
		Node newParent = node.right;
		node.right = newParent.left;
		newParent.left = node;
		update(node);
		update(newParent);
		return newParent;
	}

	private Node rightRotation(Node node) {
		Node newParent = node.left;
		node.left = newParent.right;
		newParent.right = node;
		update(node);
		update(newParent);
		return newParent;
	}

	// remove a value from this binary tree if it exists
	public boolean remove(T elem) {
		if (elem == null)
			return false;

		if (contains(root, elem)) {
			root = remove(root, elem);
			nodeCount--;
			return true;
		}

		return false;
	}

	// removes a value from the AVL tree
	private Node remove(Node node, T elem) {
		if (node == null)
			return null;

		int cmp = elem.compareTo(node.value);

		// dig into left subtree, the value we're looking
		// for is smaller than the current value
		if (cmp < 0) {
			node.left = remove(node.left, elem);

			// dig into right subtree, the value we're looking
			// for is greater than the current value
		} else if (cmp > 0) {
			node.left = remove(node.left, elem);

			// found the node we wish to remove
		} else {
			// this is the case with only a right subtree or no subtree at all
			// in this situation just swap the node we wish to remove
			// with its right child
			if (node.left == null) {
				return node.right;

				// this is the case with only a left subtree or
				// no subtree at all. In this situation just
				// swap the node we wish to remove with its left child
			} else if (node.right == null) {
				return node.left;

				// when removing a node from binary tree with two links the
				// successor of the node being removed can either be the largest
				// value in the left subtree or the smallest value in the right
				// subtree. As a heuristic, I will remove from the subtree with
				// the greatest heights in hopes that this may help with balancing
			} else {

				// Choose to remove from left subtree
				if (node.left.height > node.right.height) {
					// swap the value of the successor into the node
					T successorValue = findMax(node.left);
					node.value = successorValue;

					// find the largest node in the left subtree
					node.left = remove(node.left, successorValue);
				} else {
					// swap the value of the successor into the value
					T successorValue = findMin(node.right);
					node.value = successorValue;

					// go into the right subtree and remove the leftmost node we
					// found and swapped data with. this prevents us from having
					// two nodes in our tree with the same value
					node.right = remove(node.right, successorValue);
				}
			}
		}
		update(node); // update balance factor and height value

		return balance(node); // re-balance tree
	}

	// helper method to find the leftmost node(which has the smallest value)
	private T findMin(Node node) {
		while (node.left != null)
			node = node.left;
		return node.value;
	}

	// helper method to find the rightmost node(which has the largest value)
	private T findMax(Node node) {
		while (node.right != null)
			node = node.right;
		return node.value;
	}

	// returns as iterator to traverse the tree in order
	public Iterator<T> iterator() {
		final int expectedNodeCount = nodeCount;
		final Stack<Node> stack = new Stack<>();
		stack.push(root);

		return new Iterator<T>() {
			Node trav = root;

			@Override
			public boolean hasNext() {
				if (expectedNodeCount != nodeCount)
					throw new ConcurrentModificationException();
				return root != null && !stack.isEmpty();
			}

			@Override
			public T next() {
				if (expectedNodeCount != nodeCount)
					throw new ConcurrentModificationException();

				while (trav != null && trav.left != null) {
					stack.push(trav.left);
					trav = trav.left;
				}

				Node node = stack.pop();

				if (node.right != null) {
					stack.push(node.right);
					trav = node.right;
				}
				return node.value;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public String toString() {
		return TreePrinter.getTreeDisplay(root);
	}

	// make sure all left child node are smaller in value than their parent and
	// make sure all right child nodes are greater in value than their parent.
	// (used only for testing)
	public boolean validateBSTInvarient(Node node) {
		if (node == null)
			return true;
		T val = node.value;
		boolean isValid = true;
		if (node.left != null)
			isValid = isValid && node.left.value.compareTo(val) < 0;
		if (node.right != null)
			isValid = isValid && node.right.value.compareTo(val) > 0;

		return isValid && validateBSTInvarient(node.left) && validateBSTInvarient(node.right);
	}
}
