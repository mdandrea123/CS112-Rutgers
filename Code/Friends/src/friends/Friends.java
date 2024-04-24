package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2. Chain is returned as a
	 * sequence of names starting with p1, and ending with p2. Each pair (n1,n2) of
	 * consecutive names in the returned chain is an edge in the graph.
	 * 
	 * @param g  Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there
	 *         is no path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {

		/** COMPLETE THIS METHOD **/

		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		if (g == null) {
			return null;
		}
		if (p1 == null) {
			return null;
		}
		if (p2 == null) {
			return null;
		}

		ArrayList<String> shortestPath = new ArrayList<String>();

		boolean[] visited = new boolean[g.members.length];

		Queue<Person> queue = new Queue<Person>();

		Person[] alreadyVisited = new Person[g.members.length];

		int i = g.map.get(p1);

		queue.enqueue(g.members[i]);

		visited[i] = true;

		while (!queue.isEmpty()) {
			Person pivot = queue.dequeue();

			int pIndex = g.map.get(pivot.name);

			visited[pIndex] = true;

			Friend neighbor = pivot.first;

			if (neighbor == null) {
				return null;
			}

			while (neighbor != null) {
				if (visited[neighbor.fnum] == false) {
					visited[neighbor.fnum] = true;
					alreadyVisited[neighbor.fnum] = pivot;
					queue.enqueue(g.members[neighbor.fnum]);

					if (g.members[neighbor.fnum].name.equals(p2)) {
						pivot = g.members[neighbor.fnum];

						while (pivot.name.equals(p1) == false) {
							shortestPath.add(0, pivot.name);
							pivot = alreadyVisited[g.map.get(pivot.name)];
						}
						shortestPath.add(0, p1);
						return shortestPath;
					}
				}
				neighbor = neighbor.next;
			}
		}
		return null;
	}

	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g      Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there
	 *         is no student in the given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {

		/** COMPLETE THIS METHOD **/

		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION

		if (g == null) {
			return null;
		}
		if (school == null) {
			return null;
		}

		ArrayList<ArrayList<String>> listOfCliques = new ArrayList<ArrayList<String>>();

		boolean[] visited = new boolean[g.members.length];

		return BFS(g, g.members[0], listOfCliques, visited, school);

	}

	private static ArrayList<ArrayList<String>> BFS(Graph g, Person start, ArrayList<ArrayList<String>> listOfCliques,
			boolean[] visited, String school) {

		ArrayList<String> cliquesResults = new ArrayList<String>();

		Queue<Person> queue = new Queue<Person>();

		queue.enqueue(start);

		visited[g.map.get(start.name)] = true;

		Person pivot = new Person();
		Friend neighbor;

		if (start.school == null || start.school.equals(school) == false) {

			queue.dequeue();

			for (int j = 0; j < visited.length; j++) {
				if (visited[j] == false) {
					return BFS(g, g.members[j], listOfCliques, visited, school);
				}
			}
		}

		while (queue.isEmpty() == false) {

			pivot = queue.dequeue();

			neighbor = pivot.first;
			cliquesResults.add(pivot.name);

			while (neighbor != null) {

				if (visited[neighbor.fnum] == false) {

					if (g.members[neighbor.fnum].school == null) {

					} else {
						if (g.members[neighbor.fnum].school.equals(school)) {
							queue.enqueue(g.members[neighbor.fnum]);
						}
					}
					visited[neighbor.fnum] = true;
				}
				neighbor = neighbor.next;
			}
		}

		if (listOfCliques.isEmpty() == false && cliquesResults.isEmpty()) {

		} else {
			listOfCliques.add(cliquesResults);
		}

		for (int i = 0; i < visited.length; i++) {
			if (visited[i] == false) {
				return BFS(g, g.members[i], listOfCliques, visited, school);
			}
		}

		return listOfCliques;
	}

	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no
	 *         connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {

		/** COMPLETE THIS METHOD **/

		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		if (g == null) {
			return null;
		}

		ArrayList<String> connectors = new ArrayList<String>();

		boolean[] visited = new boolean[g.members.length];

		ArrayList<String> predecessor = new ArrayList<String>();

		int[] numbersOfDFS = new int[g.members.length];

		int[] before = new int[g.members.length];

		for (int i = 0; i < g.members.length; i++) {
			if (visited[i] == false) {

				connectors = DFS(connectors, g, g.members[i], visited, new int[] { 0, 0 }, numbersOfDFS, before,
						predecessor, true);
			}
		}

		return connectors;

	}

	private static ArrayList<String> DFS(ArrayList<String> connectors, Graph g, Person start, boolean[] visited,
			int[] count, int[] numbersOfDFS, int[] back, ArrayList<String> backward, boolean started) {

		visited[g.map.get(start.name)] = true;

		Friend neighbor = start.first;

		numbersOfDFS[g.map.get(start.name)] = count[0];
		back[g.map.get(start.name)] = count[1];

		while (neighbor != null) {

			if (visited[neighbor.fnum] == false) {

				count[0]++;
				count[1]++;

				connectors = DFS(connectors, g, g.members[neighbor.fnum], visited, count, numbersOfDFS, back, backward,
						false);

				if (numbersOfDFS[g.map.get(start.name)] <= back[neighbor.fnum]) {

					if ((connectors.contains(start.name) == false && backward.contains(start.name))
							|| (connectors.contains(start.name) == false && started == false)) {
						connectors.add(start.name);
					}
				} else {

					int first = back[g.map.get(start.name)];

					int second = back[neighbor.fnum];

					if (first < second) {
						back[g.map.get(start.name)] = first;
					} else {
						back[g.map.get(start.name)] = second;
					}
				}
				backward.add(start.name);
			} else {

				int third = back[g.map.get(start.name)];

				int fourth = numbersOfDFS[neighbor.fnum];

				if (third < fourth) {
					back[g.map.get(start.name)] = third;
				} else {
					back[g.map.get(start.name)] = fourth;
				}
			}
			neighbor = neighbor.next;
		}

		return connectors;
	}

}
