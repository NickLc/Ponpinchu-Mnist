package rnnn01;

import java.util.*;

public class Main {

    /**
     * @param args the command line arguments
     */
    static final Random rand = new Random();
    public static void main(String[] args) {
        
        System.out.println("Rand "+ getRandom());
        System.out.println("Rand "+ getRandom());
        System.out.println("Rand "+ getRandom());
        System.out.println("Rand "+ getRandom());
        System.out.println("Rand "+ getRandom());
        System.out.println("Rand "+ getRandom());
//        System.out.println("Rand "+ rand.nextDouble());
        //neuralnnn001 abc = new neuralnnn001(3,2,1);
    }
    	static double getRandom() {
		return (rand.nextDouble() * 2 - 1); // [-1;1[
	}

}
