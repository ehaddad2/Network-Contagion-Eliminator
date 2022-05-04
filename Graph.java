import java.util.*;

public class Graph {

	int vertices;
	// create an array of linked list (each element in array is a vertex)
	LinkedList<Integer> adjList[];
	// class variable keeping track of the CI values of the graph
	int[] collInfArr;
	
	// constructor
	public Graph(int vertices) {
		
		this.vertices = vertices;
		collInfArr = new int[vertices];
		// initialize arr of linked lists based on num of vertices
		adjList = new LinkedList[vertices];
		
		// fill each element in array with a new linked list as prep
		for (int i = 1; i < vertices; i++) {
			
			adjList[i] = new LinkedList<>();
		}
	}
	
	// will find right ll group element in the arr to add the source to dest and vice versa since
	// graph is not directed.
	
	public void addEdge(int source, int destination) {
		
		adjList[source].add(destination);
		adjList[destination].add(source);
	}
	
	/*method to print out elements in graph
	 */
	public void displayAL() {
		
		for (int i = 1; i < vertices; i++) {
			// ensure ll isnt empty, if so move on
			if (adjList[i].size() >= 1) {
				
				System.out.println("Vertex " + i + " is connected to:-");
				for (int j = 0; j < adjList[i].size(); j++) {
					
					System.out.print(adjList[i].get(j) + " ");
				}
				
				System.out.println();
			}
		}
	}

	// initializes the class variable array as the first step in the inoculate2 process
	public void findAllCollectiveInfluences(int r) {
		
		for (int i = 1; i < adjList.length; i++) {
			
			collInfArr[i] = findCollectiveInfluence(i, r);
		}
	}
	
	// finds the CI of just one vertex
	public int findCollectiveInfluence(int i, int r) {
		
		int collInf = 0;
		int ki = 0;
		int partialBall = 0;
			
		  partialBall = partialBall(i, r);
		  ki = adjList[i].size();
		  collInf = (ki-1) * partialBall; 

		return collInf;
	}
	// will delete vertex with highest collective influence and then update the CI of vert. in ball(i,r+1) 
	public void deleteColInfVert(int i, int r) {
		
		ArrayList<Integer> ball = new ArrayList<>();
		int c = 0;
		int level = 0;
		boolean visited[] = new boolean[vertices];
		
		LinkedList<Integer> queue = new LinkedList<>();
		
		visited[i] = true;
		queue.add(i);
		c = queue.size();
		// dequeue a vertex from the queue and print 
		while (!queue.isEmpty()) {
			// add all vertices in ball(i, r+1) to the arrayList
			// get adj vertices from dequeued vertex and visit
			Iterator<Integer> it = adjList[i].listIterator();
			
			// iterate through linked adj list 
			while (it.hasNext()) {
				
				int n = it.next();
				// will treat each node as vertex and perform same procedure on each
				if (!visited[n]) {
					
					visited[n] = true;
					queue.add(n);
				}
			}
			c--;
			if (c == 0) {
				
				level++;
			}
			
			if (level == r + 1) {
				break;
			}
			
			i = queue.poll();
			ball.add(i);
			// reset c for next level
			if (c == 0) {
				
				c = queue.size() + 1;
			}	
		}
		
		ball.addAll(queue);
		// get rid of source vertex
		isolate(ball.get(0));
		collInfArr[ball.get(0)] = 0;
		ball.remove(0);
		// updates CI values
		for (int j = 0; j < ball.size(); j++) {
		
			collInfArr[ball.get(j)] = findCollectiveInfluence(ball.get(j), r);
		}
	}
	
	
	// variation of BFS to find partial ball for the collective inf. equation
	public int partialBall(int i, int r) {
		
		int totalSum = 0;
		int c = 0;
		int level = 0;
		int queueSize = 0;
		boolean visited[] = new boolean[vertices];
		ArrayList<Integer> arrToSum = new ArrayList<>();
		
		LinkedList<Integer> queue = new LinkedList<>();
		
		visited[i] = true;
		queue.add(i);
		c = queue.size();
		// dequeue a vertex from the queue and print 
		while (!queue.isEmpty()) {

			// get adj vertices from dequeued vertex and visit
			Iterator<Integer> it = adjList[i].listIterator();
			
			// iterate through linked adj list 
			while (it.hasNext()) {
				
				int n = it.next();
				
				// will treat each node as vertex and perform same procedure on each
				if (!visited[n]) {
					
					visited[n] = true;
					queue.add(n);
				}
			}
			
			// very special case
			if (r == 1) {
				i = queue.poll();
			}
			c--;
			if (c == 0) {
				
				level++;
			}
			
			if (level == r) {
				break;
			}
			
			i = queue.poll();
			// reset c for next level
			if (c == 0) {
				
				c = queue.size() + 1;
			}
			
		}
		queueSize = queue.size();
		// calculates final value for partial ball(i,r)
		for (int j = 0; j < queueSize; j++) {
			
			totalSum += (adjList[queue.poll()].size()) - 1;
		}
		return totalSum;
	}
	
	// will find the most influential vetex (ie the one with the highest degree)
	public int findInfVertex() {
		
		int largestDeg = 0;
		int largestDegLoc = 0;
		
		for (int i = 1; i < adjList.length ; i++) {
			
			if (adjList[i].size() > largestDeg) {
				
				largestDeg = adjList[i].size();
				largestDegLoc = i;
			}
		}

		return largestDegLoc;
	}
	
	// method to remove an edge from the graph, used in isolate method
	public void removeEdge(int source, int destination) {
		
		// both for loops will ensure that the source and destination values exist and remove 
		// each element from the adj list (since graph is undirected, must consider 2 vals)
		for (int i = 0; i < adjList[source].size(); i++) {
			
			if (adjList[source].get(i) == destination) {
				
				adjList[source].remove(i);
				break;
			}
		}
		
		for (int i = 0; i < adjList[destination].size(); i++) {
			
			if (adjList[destination].get(i) == source) {
				
				adjList[destination].remove(i);
				break;
			}
		}
		
	}
	// will isolate a vertex v by removing all edges to adjacent vertices
	public void isolate(int v) {

		// remove all adj elements from vertex (ie ll)
		while (!adjList[v].isEmpty()) {
			
			removeEdge(v, adjList[v].element());
		}
		
	}
	
	// finds greatest value in the CI arr 
	public int findMostCINode() {
		
		int mostInfVal = 0;
		int mostInf = 0;
		
		for (int i = 0; i < collInfArr.length; i++) {
			
			if (collInfArr[i] > mostInfVal) {
				
				mostInfVal = collInfArr[i];
				mostInf = i;
			}
		}
		
		return mostInf;
	}
	
	// method to display all CI values in the graph
	public void printCollInfArr() {
		
		for (int i = 0; i < collInfArr.length; i++) {
			
			System.out.println("CI of " + i + " = " + collInfArr[i]);
		}
	}
	
}
