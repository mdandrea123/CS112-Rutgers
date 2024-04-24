package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie.
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {

	// prevent instantiation
	private Trie() {
	}

	/**
	 * Builds a trie by inserting all words in the input array, one at a time, in
	 * sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!) The words in the
	 * input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/

		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		TrieNode front = new TrieNode(null, null, null);

		if (allWords.length == 0) {
			return front;
		}

		if (front.firstChild == null) {
			TrieNode newLeaf;
			Indexes substr = new Indexes(0, (short) 0, (short) (allWords[0].length() - 1));
			newLeaf = new TrieNode(substr, null, null);
			front.firstChild = newLeaf;
		}

		for (int i = 1; i < allWords.length; i++) {
			buildTrie(front.firstChild, allWords, i, front);
		}
		return front;
	}

	private static void buildTrie(TrieNode curr, String[] allWords, int index, TrieNode parent) {

		int compared = compareNode(curr, allWords[index], allWords);

		if (compared == 0) {
			if (curr.firstChild == null) {
				return;
			} else {
				buildTrie(curr.firstChild, allWords, index, curr);
			}
		} else if (compared == -1) {

			if (curr.sibling == null) {
				Indexes substr;
				if (parent.substr == null) {
					substr = new Indexes(index, (short) 0, (short) (allWords[index].length() - 1));
				} else {
					if (parent.sibling == curr) {
						substr = new Indexes(index, (short) parent.substr.startIndex,
								(short) (allWords[index].length() - 1));
					} else {
						substr = new Indexes(index, (short) (parent.substr.endIndex + 1),
								(short) (allWords[index].length() - 1));
					}
				}

				TrieNode newLeaf = new TrieNode(substr, null, null);
				curr.sibling = newLeaf;

			} else {

				if (curr.sibling != null) {
					buildTrie(curr.sibling, allWords, index, curr);
				} else {

					Indexes substr;
					if (parent.substr == null) {
						substr = new Indexes(index, (short) 0, (short) (allWords[index].length() - 1));
					} else {
						if (parent.sibling == curr) {
							substr = new Indexes(index, (short) parent.substr.startIndex,
									(short) (allWords[index].length() - 1));
						} else {
							substr = new Indexes(index, (short) (parent.substr.endIndex + 1),
									(short) (allWords[index].length() - 1));
						}
					}
					TrieNode newLeaf = new TrieNode(substr, null, null);
					curr.sibling = newLeaf;

				}
			}

		} else if (compared == 1) {
			relationBuild(parent, curr, index, allWords);
		} else {
			buildTrie(curr.firstChild, allWords, index, curr);
		}

	}

	private static int compareNode(TrieNode node, String word, String[] allWords) {

		if (node.substr == null)
			return -1;
		String nodeWord = allWords[node.substr.wordIndex];

		int startIndex = node.substr.startIndex;
		int counter = node.substr.startIndex;
		int endIndex = node.substr.endIndex;

		for (int i = startIndex; i < Math.min(nodeWord.length(), word.length()); i++) {
			if (nodeWord.charAt(i) == word.charAt(i)) {
				counter++;
				continue;
			}
			break;
		}

		if (counter == startIndex) {
			return -1;
		} else if (counter < endIndex + 1) {
			return 1;
		} else if (counter == endIndex + 1) {
			return 0;
		} else {
			return 2;
		}

	}

	private static void relationBuild(TrieNode parent, TrieNode child, int index, String[] allWords) {

		int firstAppearance = Math.min(child.substr.wordIndex, index);
		int startIndex = child.substr.startIndex;
		int i = 0;

		for (i = startIndex; i < child.substr.endIndex; i++) {
			if (allWords[child.substr.wordIndex].charAt(i) == allWords[index].charAt(i)) {
				continue;
			}
			break;
		}

		Indexes tempIndexes = new Indexes(firstAppearance, (short) startIndex, (short) (i - 1));
		TrieNode temp = new TrieNode(tempIndexes, child, child.sibling);

		if (parent.firstChild == child) {
			parent.firstChild = temp;
		} else {
			parent.sibling = temp;
		}

		child.substr.startIndex = (short) (i);
		child.sibling = null;

		TrieNode newLeaf;
		Indexes substr = new Indexes(index, (short) i, (short) (allWords[index].length() - 1));
		newLeaf = new TrieNode(substr, null, null);
		child.sibling = newLeaf;

	}

	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf
	 * nodes in the trie whose words start with this prefix. For instance, if the
	 * trie had the words "bear", "bull", "stock", and "bell", the completion list
	 * for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell";
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and
	 * "bell", and for prefix "bell", completion would be the leaf node that holds
	 * "bell". (The last example shows that an input prefix can be an entire word.)
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be", the
	 * returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root     Root of Trie that stores all words to search on for
	 *                 completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix   Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the
	 *         prefix, order of leaf nodes does not matter. If there is no word in
	 *         the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/

		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		ArrayList<TrieNode> list = new ArrayList<>();

		if (prefix.length() == 0) {
			return getIndexes(root, allWords, prefix);
		}

		if (root.substr == null) {
			if (root.firstChild == null) {
				return null;
			}
			return completionList(root.firstChild, allWords, prefix);
		}

		int compared = compareForCompletion(root, prefix, allWords);

		if (compared == -1) {
			if (root.sibling == null) {
				return null;
			} else {
				return completionList(root.sibling, allWords, prefix);
			}
		}

		if (compared == 1) {
			if (root.firstChild != null) {
				list.addAll(getIndexes(root.firstChild, allWords, prefix));
			} else {
				list.add(root);
			}
		}

		if (compared == 2) {
			if (root.firstChild == null) {
				return null;
			} else {
				return completionList(root.firstChild, allWords, prefix);
			}
		}

		if (list.isEmpty()) {
			return null;
		} else {
			return list;
		}
	}

	private static int compareForCompletion(TrieNode node, String prefix, String[] allWords) {
		String nodeWord = allWords[node.substr.wordIndex];
		int start = node.substr.startIndex;
		int end = node.substr.endIndex;

		if (prefix.equals(nodeWord.substring(0, Math.min(end + 1, prefix.length())))) {
			return 1;
		} else {
			if (prefix.length() - 1 > end) {
				if ((prefix.substring(0, end + 1).equals(nodeWord.substring(0, end + 1)))) {
					return 2;
				}
			}
			return -1;
		}
	}

	private static ArrayList<TrieNode> getIndexes(TrieNode curr, String[] allWords, String prefix) {
		ArrayList<TrieNode> nodes = new ArrayList<>();

		if (curr.sibling == null) {
		} else {
			nodes.addAll(getIndexes(curr.sibling, allWords, prefix));
		}

		if (curr.firstChild == null) {
			nodes.add(curr);
			return nodes;
		} else {
			nodes.addAll(getIndexes(curr.firstChild, allWords, prefix));
		}

		return nodes;
	}

	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}

	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			String pre = words[root.substr.wordIndex].substring(0, root.substr.endIndex + 1);
			System.out.println("      " + pre);
		}

		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}

		for (TrieNode ptr = root.firstChild; ptr != null; ptr = ptr.sibling) {
			for (int i = 0; i < indent - 1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent + 1, words);
		}
	}
}
