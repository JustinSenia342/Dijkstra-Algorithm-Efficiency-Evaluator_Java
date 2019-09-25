/*
 * Name: Justin Senia
 * E-Number: E00851822
 * Date: 3/9/2017
 * Class: COSC 314
 * Project: #2, Part 2
 */

import java.util.*;
import java.io.*;

public class Main {
    
    //Declaring random variable outside of main so it can be used by all methods
    public static Random randGen;
    
    //main method for Project 2 
    public static void main(String[] args) throws IOException{
        
        //declaring variable "n" to be used as a universal "size" variable
        int n;
		
		//decalring variable "sourceVertex" to be used as starting vertex for Dijkstra
		int sourceVertex;
        
        //FOR COSC 314 PROJECT #2, PART 2:
        
        //creating file locations to be used to locate external files and also
        //creating a location for the output file to be saved to.
		File inFileThree = new File("file3.txt");
		File inFileFour = new File("file4.txt");
        File outFile = new File("OutputFile.txt");
        
        //creating a printwriter for the output file
        //(passed: outFile= out file location to be saved)
        PrintWriter pWriter = new PrintWriter(outFile);
        
        //scanner created for file external reading
        Scanner fIn = new Scanner(inFileThree); 
        
		
		
		//Label at top of output
		pWriter.println("COSC 314 Project #2, Part 2: ");
		pWriter.println(" ");
		
        //scanner re-initialized with new value for external file reading
        fIn = new Scanner(inFileThree);
		
        //read size of matrix from file
        n = fIn.nextInt();
		
        //create & populate matrix with external file data
        //(passed: n= size, fIn= scanner linked to external file)
		//verticies that don't have connections to one another are marked with 9999999
		//for ease of lowest comparisons later, self referential connections are marked with a zero
        int[][] fMatrixThree = popFileMatrix(n, fIn);
		
		//gets starting vertex value from external file
		sourceVertex = fIn.nextInt();
		
		//writing descriptor of data to file
        pWriter.println("Djikstra's Algorithm file3.txt Output: ");
		
		//performs Dijkstra's algorithm
		//passed: fMatrixThree= matrix from ext file, n= size, 
		//sourceVertex= starting vert for Dijkstra, pWriter= printwriter to write results to file
		dijkAlgorithm(fMatrixThree, n, sourceVertex, pWriter);
		
		//closes fIn so it can be re-used for the next external file
        fIn.close();
		
		
		
        //scanner re-initialized with new value for external file reading
        fIn = new Scanner(inFileFour);
		
        //read size of matrix from file
        n = fIn.nextInt();
		
        //create & populate matrix with external file data
        //(passed: n= size, fIn= scanner linked to external file)
		//verticies that don't have connections to one another are marked with 9999999
		//for ease of lowest comparisons later, self referential connections are marked with a zero
        int[][] fMatrixFour = popFileMatrix(n, fIn);
		
		//gets starting vertex value from external file
		sourceVertex = fIn.nextInt();
		
		//writing descriptor of data to file
        pWriter.println("Djikstra's Algorithm file4.txt Output: ");
		
		//performs Dijkstra's algorithm
		//passed: fMatrixThree= matrix from ext file, n= size, 
		//sourceVertex= starting vert for Dijkstra, pWriter= printwriter to write results to file
		dijkAlgorithm(fMatrixFour, n, sourceVertex, pWriter);
		
		//closes fIn to maintain data integrity
        fIn.close();
		
        
        
		/*
        //Inneficiency testing for COSC 314 Project #2, part 2:
        //Each section of code does the following:
		//changes the value of n to the new desired array size to be tested.
		//declares a new matrix and initializes it with random distance values (between 1 and 1000)
		//declares and initializes a 10 length array with random values based on size stored in variable "n"
		//these random numbers will be used as starting vertex values for timed trial runs.
		//creates a "for loop" that runs ten times
		//runs the timedDijkstraMethod which calculates time it takes to complete finding all shortest paths from
		//the source vertex to all other vertices, and then outputs the time taken on the console.
		//this section is disabled due to calculated time overwriting the previous output file with unecessary data.
        n = 100;
        int[][] rndMatrixOne = randPopMatrix(n, 13);
		int[] rndArrayOne = randPopArray(n, 13);
        for (int q = 0; q < 10; q++){
			timedDijkstraMethod(rndMatrixOne, n, rndArrayOne[q], pWriter);
        }
		System.out.println("");
		
        n = 200;
        int[][] rndMatrixTwo = randPopMatrix(n, 13);
		int[] rndArrayTwo = randPopArray(n, 13);
        for (int w = 0; w < 10; w++){
			timedDijkstraMethod(rndMatrixTwo, n, rndArrayTwo[w], pWriter);
		}
		System.out.println("");
		
        n = 500;
        int[][] rndMatrixThree = randPopMatrix(n, 13);
		int[] rndArrayThree = randPopArray(n, 13);
        for (int e = 0; e < 10; e++){
			timedDijkstraMethod(rndMatrixThree, n, rndArrayThree[e], pWriter);
		}
		System.out.println("");
		
        n = 1000;
        int[][] rndMatrixFour = randPopMatrix(n, 13);
		int[] rndArrayFour = randPopArray(n, 13);
        for (int r = 0; r < 10; r++){
			timedDijkstraMethod(rndMatrixFour, n, rndArrayFour[r], pWriter);
        }
		System.out.println("");
		
		*/
		
        //closing printwriter for data protection
        pWriter.close();
    }
   
    
    
	//Dijkstra's Algorithm
	//The main code block for Project 2: Part 2
	//(passed: int[][] distanceMatrix= matrix of vertices and distances between them,
	//(passed (cont): int nElements= one of the dimensions of the distance matrix, int startVert= starting vertex
	//(passed (cont): for use in Dijkstra's algorithm, PrintWriter pW= used for writing to an external file)
	//dijkAlgorithm creates three arrays, one for keeping track of previously used verticies,
	//one for storing current shortest distances and one for storing paths for shortest distances
	//back to the starting vertex. It iterates through lowest unused vertex paths, calculates if
	//the new path is more efficient than any old ones, if it is, it updates all arrays accordingly:
	//changing the new lowest path values, the path directions and the vertex used so it won't be used again
	//this is repeated until all verticies are used and the algorithm is complete. then the results are printed
	//to an external file for ease of reading.
	//***NOTE all elements read from the matrix have a -1 modifier attached so that everything works with
	//the -1 array address issue, +1 is added at the end to each value to restore it's original value for output.***
	public static void dijkAlgorithm(int[][] distanceMatrix, int nElements, int startVert, PrintWriter pW){
		
		//creating temp array for checking whether element has been used yet
		int[] checkOffArray = new int[nElements];
		
		//creating array for storing distance values for distance comparison
		int[] distanceArray = new int[nElements];
		
		//creating array for storing shortest path information
		int[] pathArray = new int[nElements];
		
		//populating distance array with proper row of initial data
		//populating path array with starting vert
		//populating check off array with zeros
		for (int p = 0; p < nElements; p++){
			distanceArray[p] = distanceMatrix[startVert-1][p];
			pathArray[p] = startVert-1;
			checkOffArray[p] = 0;
		}
		
		//creating string variable to store most efficient path for printing later
		String mostEfficientPath = "";
		
		//creating int variable to store most efficient distance for printing later
		int mostEfficientDistance = 0;
		
		//setting initial starting value for checkoff array as used
		checkOffArray[startVert-1] = 1;
		
		//creating and initializing int variables to store temporary vertex labels and values
		int tempMinVal = 9999999;
		int tempMinName = 9999999;
		
		while (true){
			
			//resets temp minVal and minName variables to 9999999, so they can be used to determine lowest 
			//remaining vertex value in the arrays
			tempMinVal = 9999999;
			tempMinName = 9999999;
			
			//calling method arrayMinIntAddress to find location of lowest distance available from unchecked verticies
			//then assigning the address of what it found to "tempMinName" variable for use later
			//passed: distanceArray= array that has current shortest distances, checkOffArray= array that
			//shows whether or not which vertices have been used already
			tempMinName = arrayMinIntAddress(distanceArray, checkOffArray);
			
			//if the resturned value from arrayMinIntAddress equals -1, its a terminal value to signify all 
			//vertices have been used and the algorithm is done, so it terminates the iterative loop
			if (tempMinName == -1){
				break;
			}
			
			//marks the checkOffArray's address as used by marking it's location with a 1
			checkOffArray[tempMinName] = 1;
			
			//Main Dijkstra comparison workhorse, makes sure it only checks previously unused vertices for comparisons
			//then makes sure that any distances of zero and "infinity" (infinity = 9999999 = used to signify no connection)
			//are not used for comparisons, because zero distance would mean the vert path is self referential
			//and anything added to an "infinite distance" would by default be greater than any pre-existing value
			//so calculations don't need to be done to determine if it's less than any existing value.
			//if the previous distance value is greater than the new potential value when factoring in the new lowest path
			//it updates the current shortest distance array value and the path array as well with the new findings
			//if the new calculations are not more efficient, go to next iteration of the loop
			for (int u = 0; u < nElements; u ++){
				
				if (checkOffArray[u] == 0){
					
					if (distanceMatrix[tempMinName][u] != 0 && distanceMatrix[tempMinName][u] != 9999999){
						
						if (distanceArray[tempMinName] + distanceMatrix[tempMinName][u] < distanceArray[u]){
							
							distanceArray[u] = distanceArray[tempMinName] + distanceMatrix[tempMinName][u];
							pathArray[u] = tempMinName;
							
						}
					}
				}
			}
		}
		
		//printing header for the Dijkstra's output file, with proper identifiers for headings
		pW.println("******************************************************************************");
		pW.println("*                     Dijkstra's Shortest Path Algorithm                     *");
		pW.println("******************************************************************************");
		pW.println("*        Path Name         *    Distance    *           Path Taken           *");
		pW.println("******************************************************************************");
		
		//for loop to print out each combination of path name, distance and full path for each possible combination
		//starting at the starting vertex supplied at the start of the algorithm
		for (int y = 0; y < nElements; y++){
			
			//stores shortest numerical distance
			mostEfficientDistance = distanceArray[y];
			
			//obtains a string of the full shortest path taken by calling the createPathString method 
			//passed: pathArray= array that contains directions for shortest path to take, startVert-1=
			//the starting vertex -1 to be used in calculating best path, y= desired path destination
			mostEfficientPath = createPathString(pathArray, startVert-1, y);
			pW.printf("*   From %5d to %5d    *    %10d  *   %s", startVert, y + 1, mostEfficientDistance, mostEfficientPath);  
			pW.println("");
		}
		
		pW.println(" ");
	}
    
    
    
	//algorithm for creating a string of the most efficient path after fully calculating with dijkstra's algorithm
	public static String createPathString(int[] pathIn, int startHere, int endHere){
		
		//creating string variable to store full path string
		String tempString = "";
		
		//creating stack to store the int values of the path locations
		//because you need to work backwards from the destination this was the most efficient method
		Stack <Integer> st = new Stack <Integer> ();
		
		//creating variable used to determine if path is complete
		int currentLoc = endHere;
		
		//continues to push vertex names from path onto Stack until it reaches the beginning then breaks loop
		while (true)
		{
			st.push(currentLoc + 1);
			if (currentLoc == startHere){
				break;
			}
			
			//updates current location to previous step in path
			currentLoc = pathIn[currentLoc];
		}
		
		//continues to assemble final string by popping values off of stack, and putting them together
		//with hyphens in between to signify the path, breaks upon the stack becoming empty
		while (true)
		{
			tempString = tempString + st.pop();
			if (st.empty()){
				break;
			}
			tempString = tempString + " - ";
		}
		
		//return final constructed string
		return tempString;
	}
    
    
    
	//min array value algorithm
	//returns location of minimum value in array of ints
	//(passed: toBeTested= array containing current shortest paths from original vertex,
	//(passed (cont): alreadyUsed= array containing information on whether or not a vert has been previously used)
	public static int arrayMinIntAddress(int[] toBeTested, int[] alreadyUsed){
		
		//declaring and initializing variables to be used as comparitors to find the lowest value in an array
		int arrayAddressValue = 9999999;
		int tempMin = 9999999;
		
		//iterates through all unused vertices and finds the one with the lowest value
		for (int i = 0; i < toBeTested.length; i++){
			if (alreadyUsed[i] == 0){
				if (tempMin > toBeTested[i]){
					tempMin = toBeTested[i];
					arrayAddressValue = i;
				}
			}
		}
		
		//if the above loop completes without making any changes, tempMin will still have it's original value,
		//therefore return a -1 to signify that all all vertices have been used and signifify the original
		//caling code to move on to the next step
		if (tempMin == 9999999){
			return -1;
		}
		//if all vertices haven't been used yet, return the address of the lowest value found.
		else{
			return arrayAddressValue;
		}
	}
    
    
    
    //Makes a new matrix based off of properly formatted external file input, returns matrix
    //(passed: nPopFile= matrix dimension size based on external file
    //(passed (cont): keyInF = Scanner with file location attached to it)
    public static int[][] popFileMatrix(int nPopFile, Scanner keyInF){
        
		//creating temp variable to store read value for checking
		int pathExist = 0;
		
        //create new matrix based on read size in external file
        int[][] fileMatrix = new int[nPopFile][nPopFile];
        
        //copies all external file values to comparable size matrix
        for (int d = 0; d < nPopFile; d++){
            for (int f = 0; f < nPopFile; f++){
				
				//created variable to check if no path exists in matrix
				pathExist = keyInF.nextInt();
				
				//if the vert's destination is itself, mark as zero
				if (d == f){
					fileMatrix[d][f] = 0;
				}
				
				//if there is no path, mark as 9999999(to signify infinite distance between two verts)
				else if (pathExist == 0){
					fileMatrix[d][f] = 9999999;
				}
				
				//else, put distance value in new matrix
				else{
					fileMatrix[d][f] = pathExist;
				}
            }
        }
        
        //returns file populated matrix
        return fileMatrix;
    }
    
    
    
    //Creates a matrix based off of supplied matrix dimension size,
    //seed value and the user supplied probability of getting a connection
    //returns the random matrix
    //(passed: n= matrix size dimension, seedVal= seedvalue, pcnt= probability)
    public static int[][] randPopMatrix(int n, int seedVal){
        
        //create new random  generator based on suplied seed value
        randGen = new Random(seedVal);
        
        //creating matrix based on supplied size value
        int[][] randMatrix = new int[n][n];
        
        //populates matrix with approprate values at appropriate places based on probability parameter
        for (int t = 0; t < n; t++){
            for (int y = 0; y < n; y++){
				
				//fills matrix with values between 1 and 1000 randomly	
				randMatrix[t][y] = 1 + randGen.nextInt(1000);
				
            }
        }
        
        //return the populated matrix
        return randMatrix;
    }
    
    
    
    //Creates an array based off of supplied array size and variability,
    //seed value returns array with random values
    //(passed: n= matrix size dimension, seedVal= seedvalue)
    public static int[] randPopArray(int n, int seedVal){
        
        //create new random  generator based on suplied seed value
        randGen = new Random(seedVal);
        
        //creating matrix based on supplied size value
        int[] randArray = new int[n];
        
        //populates matrix with approprate values at appropriate places based on probability parameter
		for (int y = 0; y < n; y++){
				randArray[y] = 1 + randGen.nextInt(n);
		}

		//return newly created array with it's random values
        return randArray;
    }
    
    
    
	//Executes the Dijkstra's method and keeps track of the time it takes to execute.
	//used for efficiency testing
	//(passed: distMtrx= matrix with all vertex connection and distances, nElem= size of one dimension of matrix,
	//(passed (cont): strtVert= source vertex, pW= printwriter for writing to external file.)
	public static void timedDijkstraMethod(int[][] distMtrx, int nElem, int strtVert, PrintWriter pW){
        
		//makes note of initial time before Dijkstra's algorithm begins
        long startTime = System.currentTimeMillis();
		
        //executes Dijkstra's algorithm on supplied data
        //(passed: distMtrx= matrix with all vert connections and distances, nElem= size of one dimension of matrix,
		//(passed (cont): strtVert= source vertex, pW= printwriter for writing to external file.)
        dijkAlgorithm(distMtrx, nElem, strtVert, pW);
		
        //makes note of end time after Dijkstra's has completed
        long endTime = System.currentTimeMillis();
		
        //prints out properly formatted data to describe timing results of Dijkstra's algorithm
        System.out.println("Matrix Size: " + nElem + "x" + nElem + 
		" Time taken to calculate shortest paths using Dijkstra's algorithm: " +
        (endTime - startTime) + " Milliseconds");
	}
}