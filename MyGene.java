/**
 * 
 */

/** An instance maintains the dna string of a gene
 */
public class MyGene extends Gene implements Comparable<MyGene> {
    public String dna; // The gene. It is not empty, does not contain with
                       // the end delimiter, and contains only (C T G A)
    
    /**Score for each common instance of a character*/
    public static final int COMMON_COST = 15;
    
    /**Score for difference in number of instances*/
    public static final int DIFFERENCE_COST = 8;
    
    /**Score for characters appearing in only one gene fragment*/
    public static final int ONLY_IN_ONE_COST = 25;
    
    /**Sliding window minimum match length*/
    public static final int MIN_MATCH = 4;    
    
    /** Constructor: uninitialized Gene */
    public MyGene() {
        
    }
    
    /** Constructor: an instance with gene dna. 
    Throw an IllegalArgumentException if the dna is not well-formed, i.e. if it
    is empty, contains end delimiter TGC, or contains a char other than C T G A. */
    public MyGene(String dna) throws IllegalArgumentException {
        setDNA(dna);
    }

    /** Set the DNA associated with this gene to dna.
    Throw an IllegalArgumentException if the dna is not well-formed, i.e. if it
    is empty, contains end delimiter TGC, or contains a char other than C T G A. */
    public @Override void setDNA(String dna) throws IllegalArgumentException {
        if (dna == null || dna.length() == 0)
                throw new IllegalArgumentException("parameter null or empty");
        if (dna.contains("TGC"))
            throw new IllegalArgumentException("parameter contains TGC");
        for (int i= 0; i < dna.length(); i= i+1) {
            char c= dna.charAt(i);
            if (c != 'C' && c != 'T' && c != 'G' && c != 'A') {
                throw new IllegalArgumentException("parameter contains illegal character");
            }
        }
        this.dna= dna;

    }

    /** Get the DNA associated with this gene */
    public @Override String getDNA() {
        return dna;
    }
    
    /** return the DNA associated with this gene */
   public @Override String toString() {
       return dna;
   }

	@Override
	public int compareTo(MyGene o) {
		if (this.dna.length() < o.dna.length()){
			return -1;
		}
		else if (this.dna.length() > o.dna.length()){
			return 1;
		} else {
			return this.dna.compareTo(o.dna); 
		}
	}
	/** This method trims and sort the array according to the conditions specified by A2 and returns a String array.
	 * Trimming the dna: getting rid of the same starting chars for both strings, and same ending chars.
	 * Sorting the string: The shorter String comes first, the order of the strings with same length is determined in 
	 * alphabetical order.
	 */
	public String[] trimsort(String dna1, String dna2){
		int length1 = dna1.length();		//first get the length of both strings for restrctions later. 
		int length2 = dna2.length(); 
		int index = 0, index1 = 0;			//declare a index for each string. 
		
		for(int i = 0; i < length1 && i < length2; i++){	//this loop checks for the prefix of S0 and S1.
			if(dna1.charAt(i) == dna2.charAt(i)){			//Plan: first get the number of chars of prefixes, and trim it all at once.
				index++;									//once we get the indexes(of common chars) for both strings, exit loop(break).
			}
			else{
				break;
			}
		}
		
		for(int i = 1; i < length1 && i < length2; i++){	// this loop checks for the common suffixes in each string.
			if(dna1.charAt(length1-i) == dna2.charAt(length2-i)){	//start from the end of the loop, and get the number
				index1++; 											//of common chars at the end of both strings.
			}
			else{
				break;												//after finding the index, exit loop.
			}
		}
		
		/**Sorting the string part: The shorter String comes first, the order of the strings with same length is determined in 
		 * alphabetical order.
		 */
		String S0, S1;									//declare two strings.				
		S0 = dna1.substring(index, length1-index1);		//get both strings without the prefix and suffixes.
		S1 = dna2.substring(index, length2-index1);
		String temp;									//declare temp for swapping.
		if(S0.length() > S1.length()){					//if S0 has a greater length, swap S0 for S1.
			temp = S0; 
			S0 = S1;
			S1 = temp;
		}
		else if(S0.length() == S1.length()){			//if both strings have equal length, check for alphabetical order.
			if(S0.charAt(0) > S1.charAt(0)){			//since the first char is guarenteed to be different for 
				temp = S0; 								//both string, if first character of S0 is bigger(ascii), swap both strings.
				S0 = S1;
				S1 = temp;
			}
		}
		String[] a = {S0, S1};							//declare string array with 2 elements after trimming and sorting.
		return a; 										//return strin array.
	}
	/**
	 * Given two trimmed, sorted genes S0 and S1, the distance algorithm will recursively identify and compare
	 * segments that differ between the genes.Although the genes have been trimmed, there might be identical internal
	 * fragments of DNA; the Òsliding window algorithmÓ is designed to identify identical segments of DNA.
	 * (above specification from A2 doc.)
	 * Therefore, in this method, we use the technique of sliding window matching to find the matching sequences.
	 * The parameter index is a int array we get from the distance recursive function with default elements.
	 * We get the String array from trimsort method.
	 * We get counter(left index of S0), and counter2(left index of S1) from the call in distance recursive function with 0's.
	 */
	public int[] findMatch(int[] index, String[] a, int counter, int counter2){
       
		String S0, S1, key, lock;					//declare string variables, key = string segments of S0, lock = string segments of S1.
		int counter1 = counter + MIN_MATCH;			//counter1 = right index of S0 segment, originally set to be 4 elements right of counter.
		int counter3 = counter2 + MIN_MATCH;		//counter3 = right index of S1 segment, originally set to be 4 elements right of counter2.
		
		S0 = a[0];									//get S0 & S1 from first element of String array parameter. 
		S1 = a[1];
		
		if(S0.length() < MIN_MATCH || S1.length() < MIN_MATCH){		//base case: if S0 or S1 are already shorter than 4 elements.
			index[0] = 0;											//return the index with a int array of 4 0's.
			index[1] = 0;
			index[2] = 0;
			index[3] = 0;
			return index;
		}

		key = S0.substring(counter, counter1); 						//if strings pass the base case, get key and lock of both strings.
		lock = S1.substring(counter2,counter3);
		if(!key.equals(lock)){							//start matching. If key does not equal lock, shift index and match again.
			if(counter2==S1.length()-MIN_MATCH){		//if lock already reaches the end of S1
				if(counter==S0.length()-MIN_MATCH){
					index[0] = 0;
					index[1] = 0;
					index[2] = 0;
					index[3] = 0;
					return index; 
				}
				else{
					counter++;
					findMatch(index, a, counter, 0);
				}
			}
			else{						
				counter2++; 
				findMatch(index, a, counter, counter2);
			}
		}
		else{				//Growing Match
			index[0] = counter;		//first index of S0 and S! are set
			index[2] = counter2;
			
			while(counter1 < S0.length() && counter3 <S1.length()&&S0.charAt(counter1)==S1.charAt(counter3)){
				index[1] = counter1;
				index[3] = counter3;
				counter1++;					
				counter3++;
			}
			index[1] = counter1;
			index[3] = counter3;
			
		}
		return index;
	}	
	public int basicAlgorithm(String dna1, String dna2){
		int c0 = 0, c1 = 0 , a0 = 0, a1 = 0, t0 = 0, t1 = 0, g0 = 0, g1 = 0;
		for (int i = 0; i < dna1.length(); i++){
			if(dna1.charAt(i) == 'C'){
				c0++;
			}
			if(dna1.charAt(i) == 'A'){
				a0++;
			}
			if(dna1.charAt(i) == 'T'){
				t0++;
			}
			if(dna1.charAt(i) == 'G'){
				g0++;
			}
		}
		for (int i = 0; i < dna2.length(); i++){
			if(dna2.charAt(i) == 'C'){
				c1++;
			}
			if(dna2.charAt(i) == 'A'){
				a1++;
			}
			if(dna2.charAt(i) == 'T'){
				t1++;
			}
			if(dna2.charAt(i) == 'G'){
				g1++;
			}
		}
		int score = 0; 
		if(a0 == 0 || a1 == 0){
			if(a0 == 0){
				score = score + ONLY_IN_ONE_COST * a1;
			}
			else{
				score = score + ONLY_IN_ONE_COST * a0;
			}
		}
		else{
			if(a0 > a1){
				score = a1 * COMMON_COST;
				score = score + (a0-a1) * DIFFERENCE_COST;
			}
			else{
				score = a0 * COMMON_COST;
				score = score + (a1-a0) * DIFFERENCE_COST;
			}
		}
		if(c0 == 0 || c1 == 0){
			if(c0 == 0){
				score = score + ONLY_IN_ONE_COST * c1;
			}
			else{
				score = score + ONLY_IN_ONE_COST * c0;
			}
		}
		else{
			if(c0 > c1){
				score = c1 * COMMON_COST;
				score = score + (c0-c1) * DIFFERENCE_COST;
			}
			else{
				score = c0 * COMMON_COST;
				score = score + (c1-c0) * DIFFERENCE_COST;
			}
		}
		if(t0 == 0 || t1 == 0){
			if(t0 == 0){
				score = score + ONLY_IN_ONE_COST * t1;
			}
			else{
				score = score + ONLY_IN_ONE_COST * t0;
			}
		}
		else{
			if(t0 > t1){
				score = t1 * COMMON_COST;
				score = score + (t0-t1) * DIFFERENCE_COST;
			}
			else{
				score = t0 * COMMON_COST;
				score = score + (t1-t0) * DIFFERENCE_COST;
			}
		}
		if(g0 == 0 || g1 == 0){
			if(g0 == 0){
				score = score + ONLY_IN_ONE_COST * g1;
			}
			else{
				score = score + ONLY_IN_ONE_COST * g0;
			}
		}
		else{
			if(g0 > g1){
				score = g1 * COMMON_COST;
				score = score + (g0-g1) * DIFFERENCE_COST;
			}
			else{
				score = g0 * COMMON_COST;
				score = score + (g1-g0) * DIFFERENCE_COST;
			}
		}
		return score; 
	}
	
	public int distance(Gene Ga, Gene Gb){
		return distanceRecursive(Ga.toString(), Gb.toString());
	}
	
	public int distanceRecursive(String S0, String S1){
		int[] a = new int[4];
		String[] s = new String[2];
		s = trimsort(S0, S1);
		a = findMatch(a, s, 0, 0);
		if(a[0]==0 || a[1]==0 || a[2]==0 || a[3]==0){
			return basicAlgorithm(s[0], s[1]);
		}
		else{
			String left, left1, right, right1;
			left = S0.substring(0, a[0]);
			left1 = S1.substring(0, a[2]);
			right = S0.substring(a[1]);
			right1 = S1. substring(a[3]);
			System.out.println(left + left1 + right + right1);
			return basicAlgorithm(left, left1) + distanceRecursive(right, right1);
		}
	}
	/** Return the distance between this gene and gene g. Calculate the distance
	according to the rules given in the A2 handout. */
	public int getDistance(MyGene g) {
		return distance(this, g);
	}
}


