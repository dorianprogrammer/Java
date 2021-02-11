package UnionFind;

public class UnionFind {

	private int size, numComponents;
	// first size is the number of elements are in this union find
	// and the other is to find the number of components in the union find

	private int[] sz, id;
	// sz is used to track the sizes of each of the componets
	// and id poits to the parents of i, if i[i] = i then i is a root node

	public UnionFind(int size) {

		if (size <= 0)
			throw new IllegalArgumentException("Negative numbers not allowed");

		this.size = numComponents = size;
		sz = new int[size];
		id = new int[size];

		for (int i = 0; i < size; i++) {
			id[i] = i;// link to itself (self root)
			sz[i] = 1;// each component is originally of size one
		}
	}

	// find which component/set 'p' belongs to, takes amortized constant time
	public int find(int p) {

		// find the root of the component/set
		int root = p;
		while (root != id[root])
			root = id[root];

		// compress the path leading back to the root.
		// doing this operation is called "path compression"
		// and is what gives us amortized constant time complexity
		while (p != root) {
			int next = id[p];
			id[p] = root;
			p = next;
		}
		return root;

	}

	// return whether or not the elements 'p' and
	// 'q' are int the same components/set
	public boolean connected(int p, int q) {
		return find(p) == find(p);
	}

	// return the size of the components/set 'p' belongs to
	public int componentSize(int p) {
		return sz[find(p)];
	}

	// return the number of elements in this UnionFind/Disjoint set
	public int size() {
		return size;
	}

	// returns the number of remaining components/sets
	public int componets() {
		return numComponents;
	}

	// unify the components/sets containing elemts 'p' and 'q'
	public void unify(int p, int q) {
		int root1 = find(p);
		int root2 = find(q);

		// these elements are already in the same group
		if (root1 == root2)
			return;

		// merge two components/sets together
		// merge smaller components/set into the larger one
		if (sz[root1] < sz[root2]) {
			sz[root2] += sz[root1];
			id[root1] = root2;
		} else {
			sz[root1] += sz[root2];
			id[root2] = root1;
		}

		// since the roots found are different we know that the
		// number of components/sets has decrased by one
		numComponents--;

	}

}
