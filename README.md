# Stop-Contagion

Some prereqs:

Before using the program, please ensure you have a file with the desired undirected graph you want to inoculate. It should be in the following (x, y\n) format:

- 1 2\n 1 3\n 2 4\n etc.

- Each pair of x and y above will have an undirected link.

Invoking the program:

- Template: java StopContagion (-d) (-r RADIUS) num_nodes input_file

•	-d: optional argument; if given,the program will use the degree of each node and not its collective influence (refer to inoculate1 method below for more info).

•	-r RADIUS: optional argument; the program will use the value RADIUS for r, or will default to r=2 otherwise.

•	num_nodes: the number of nodes to remove; mandatory argument.

•	input_file: the name of the file describing the graph. mandatory argument.


Description: This program contains 2 seperate ways to inoculate a graph based on a problem proposed by Prof. Panos Louridas, Department of Management Science and Technology, Athens University of Economics and Business. The first main way (inoculate1) will find the vertices in the graph holding the greatest influence (ie. greatest degree) and isolate them. The second (inoculate2) will find the vertices that hold the highest collective influence (CI) value and isolate those vertices. The radius at which to probe can be determined by the user, as well as the number of nodes the user wishes to remove. 

What is CI?

CI is defined by the following equation: <img src="http://www.sciweavers.org/tex2img.php?eq=CI%28i%2Cr%29%3D%28k_i-1%29%5Csum_%7Bj%5Cin%E2%88%82Ball%28i%2Cr%29%20%7D%5E%7B%7D%28k_j-1%29&bc=Black&fc=White&im=jpg&fs=12&ff=arev&edit=0" align="center" border="0" alt="CI(i,r)=(k_i-1)\sum_{j\in∂Ball(i,r) }^{}(k_j-1)" width="262" height="43" />


- i is the node whose collective influence we are calculating and ki is the degree of node i.
- δBall(i, r) is the set of nodes whose shortest path from node i is exactly r. If you could draw a circle with radius r links around node i, nodes j are the nodes that fall on the perimeter of the circle.
- After calculating the collective influence of a node and removing it from the graph, all nodes from this one up till but not including r+1 are affected. If we want to isolate multipe nodes, it would be very inefficient to go through the array of CI values and update each one if only the vertices inside circle r+1 are affected. Thus we can define Ball(i, r+1) as being all the nodes within this radius (but not on the perimeter). These CI values can then be selectively extracted and updated.


About (detailed class/method description)
Major classes:

- Graph.java (typical adjacency list implementation of a graph and implementation of all below methods as well)
- StopContagion.java (Includes code to accept user args and appropriate calls to Graph methods)

Major methods:

- findAllCollectiveInfluences (param: radius)
Initializes the class variable array by finding the CI values of all vertices as the first step in the inoculate2 process

- findCollectiveInfluence (param: graph vertex, radius)
Finds the CI of just one vertex by calling the partial ball function on a radius r and then multiplying that by the radius of the source vertex - 1.

- deleteColInfVert (param: graph vertex, radius)
Will delete vertex with highest collective influence and then update the CI of the vertices in ball(i,r+1)

- partialBall (param: graph vertex, radius)
Variation of BFS to find partial ball for the collective inf. equation. This method is built to keep track of how deep (ie current radius) the bfs is currently and will take only the values in the queue once it's reached (ie the perimeter of ball(i,r) and return the sum of each of these vertices degrees -1. 

- findInfVertex:
Method to find the vertex with the highest degree, used for inoculate1.

Algorithm asymptotic analysis and related notes:

- In consideration of the method Ball(i, r+1), we can see the algorithm runs much more efficiently. More specifically, if we updated the CI array every time we want to isolate a node, this would warrent O(n^2) time worst case since every vertex is visited. However, this method of CI updating takes linear time, thus allowing a time complexity of O(nlogn). 
- One update I plan to implement is substituting the data structure used for the CI array to a max heap which would result in O(1) retrieval of the vertex with highest CI value rather than a linear time worst case.

Additional Notes:
- When I use "vertex" or "node", the meaning is intended to be synonymous. 














