import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.io.*;
/*Elias Haddad (URID: ehaddad2)
 * 4/12/22
 * CSC 172 
 * project 3
 */
public class StopContagion {
	
	public static void main(String[] args) throws IOException {      
	       
		Boolean inoculate1 = false;
		int r = 2;
		int numNodes = 0;
		String inputFile = "";
		int[] allElementsArr;
		BufferedReader brOG = null;
		BufferedReader br = null;
		BufferedReader brCount = null;
		File file = null;
		
		// reads input from cmd line
		if (args[0].equals("-d") && (args.length == 4)) {
			
			inoculate1 = true;
			r = 2;
			numNodes = Integer.parseInt(args[1]);
			inputFile = args[2];
		}
		
		else if ((args[0].equals("-d")) && (args.length == 5)) {
			
			inoculate1 = true;
			r = Integer.parseInt(args[2]);
			numNodes = Integer.parseInt(args[3]);
			inputFile = args[4];
		}
		
		else if ((args.length == 4) && (args[0].equals("-d") == false)) {
			
			inoculate1 = false;
			r = Integer.parseInt(args[1]);	
			numNodes = Integer.parseInt(args[2]);
			inputFile = args[3];
		}
		
		else {
			
			inoculate1 = false;
			r = 2;
			numNodes = Integer.parseInt(args[0]);
			inputFile = args[1];
		}
		
		// ensure user doesn't enter an impossible r
		if (r <= 0) {
			
			System.out.println("Please enter a radius value greater than 0. Terminating.");
		}
			String line1 = "";
			String command1[];
			int allElementsArrSize = 0;
			int i = 0;
			file = new File(inputFile);
		    brOG = new BufferedReader(new FileReader(file));
		    br = new BufferedReader(new FileReader(file));
		    brCount = new BufferedReader(new FileReader(file));
		    
		    // get info about the graph such as number of vertices
		    while ((line1 = brCount.readLine()) != null) {
		    	
		    	allElementsArrSize += 2;
		    }
		    
		     allElementsArr = new int[allElementsArrSize];
		    
			while ((line1 = brOG.readLine()) != null) {
				
				command1 = line1.split(" ");
				allElementsArr[i] = Integer.parseInt(command1[0]);
				i++;
				allElementsArr[i] = Integer.parseInt(command1[1]);
				i++;
			}
			
		Arrays.sort(allElementsArr);
		// constructor of new graph
		Graph g = new Graph(allElementsArr[allElementsArrSize - 1] +1);
		String line = "";
		String[] command;
		
		// add all edges to it
		while ((line = br.readLine()) != null) {
			command = line.split(" ");
			
			g.addEdge(Integer.parseInt(command[0]), Integer.parseInt(command[1]));
		}
	   
	   StopContagion s = new StopContagion();
	   
	   // chose which inoculation method to call based on user preference
	   if (inoculate1 == true) {
		   
		   s.inoculate1(g, numNodes);  
	   }
	   
	   else {
		   
		   s.inoculate2(g, numNodes, r);
	   }
	   
	}

	// simply finds the vertex in the graph with the greatest degree and isolates it numNodes number of times
	public void inoculate1(Graph g, int numNodes) {
		int nodeToDelete = 0;
		
		for (int i = 0; i < numNodes; i++) {
			
			nodeToDelete = g.findInfVertex();
			System.out.println(nodeToDelete + " " + g.adjList[nodeToDelete].size());
			g.isolate(g.findInfVertex());
		}
	}
	
	/* finds the vertex with the greatest CI, isolates it, and 
	 * updates all CI values within r+1 radius of the vertex. 
	 * Will repeat numNodes number of times.
	 */
	public void inoculate2(Graph g, int numNodes, int radius) {
		
		int nodeToDelete = 0;
		g.findAllCollectiveInfluences(radius);//TODO replace w radius
		
		for (int i = 0; i < numNodes; i++) {
			
			nodeToDelete = g.findMostCINode();
			System.out.println(nodeToDelete + " " + g.collInfArr[nodeToDelete]);
			g.deleteColInfVert(nodeToDelete, radius);
		}
	}
	
}
