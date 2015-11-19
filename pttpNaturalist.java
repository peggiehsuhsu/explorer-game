import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;


public class pttpNaturalist extends Naturalist{
    
	/**
	@Override
	public void run() {
		while (true){
	
	//	Node[] exits= getExits();  // List of nodes that cane be reached from present location
		
		// Pick a random node and move there
	//	Random rng= getRandom();  // get the random number generator
		//Node choice= exits[rng.nextInt(exits.length)];  // pick a random exit node
		
		//if (choice.isLand()){
			//moveTo(choice);}
			String animalName ="abc";
		 //for() {
				LinkedList<Node > visited = new LinkedList<Node>();
				LinkedList<Node > path = new LinkedList<Node>();
			 if (explore(getLocation(), animalName, visited, path) == true){
				 getLocation().setUserData(path);
			// }
		 }
		 
		}
	}
	
		
		

//   private LinkedList<Node> getPath(String animal)
	private boolean explore(Node n, String animal, LinkedList<Node > visited, LinkedList<Node > path) {
		Node[] exits= getExits();  // List of nodes that can be reached from present location
		
		// Been here before?
		if(visited.contains(n))
			return false;
		// Remember that I was here once
		visited.addLast(n);
		// Optimistically, add this room to the end of the path
		path.addLast(n);
		// Did I find my goal?
		if(listAnimalsPresent().size()!=0){
			return true;}
		// Is there a path to my goal through some adjacent room?
		
		for(Node a:exits)
			
			if(a.isLand()){
				//if() - dont go back to visited 
				if(visited.contains(a)==false&&this.explore (a,animal, visited, path) == true)
					moveTo(a);
					return true;
			}
		// Can't get to the goal from this room, remove it from the path
		path.remove(n);
		return false;
	}}
*/
	public void run(){
		explore(getLocation());
	}
/*depth first search*/
   private void explore(Node n){
	   Queue<Node> Traverse=new LinkedList<Node>();
	   Stack<Node> Visited = new Stack<Node>(); 
	   
	   Visited.push(n); //add current node to the visited stack 
	   Traverse.add(n); //add current node to the queue 
	   
	   //Animal Part
	   Queue<Node> Path=new LinkedList<Node>();
	   Path.add(n);
	   
	   
	   
	   
	   while(!Visited.isEmpty())
	   {
		   moveTo(Visited.peek());
		   //Node top=Visited.peek();  
		   getExits();
		   if (unvisitedNeighbor(Traverse) != null){
			   Node next = unvisitedNeighbor(Traverse);
			   Visited.push(next); //add next node to the visited stack 
			   Traverse.add(next);
			   
			   Path.add(next);
		   }
		   else{
			   Path.add(Visited.peek());
			   Visited.pop();
			   
		   }
		   
	   
		   if(listAnimalsPresent().size()!=0){
			   n.setUserData(Path);
			   
			   Queue<Node> Path1= n.getUserData();
			   System.out.println("I am here");
			   System.out.println(Path1);
			   Path.clear(); 
		   }
	   
	   
	   }
	  
	   
   } 
   
   private Node unvisitedNeighbor(Queue<Node> Visited){
	   for(Node a : getExits())
	   {
		   if(!Visited.contains(a)&&a.isLand()){
			   return a;
		   }
	   }
	   return null;
   }
   

   }
