package com.phasmidsoftware.dsaipg.misc.randomwalk;

import java.io.FileWriter;
import java.io.IOException;

public class RandomWalkExperiment {

	public static void main(String[] args) {
		int[] m_Array = {10, 20, 50, 100, 200, 500, 1000, 2000, 5000}; 
        int n = 200; 
        String outputFile = "random_walk_results.csv";

        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write("n(#_of_runs),m(#_of_Steps),d(AvgDist)\n");
            System.out.println("#_of_runs: " + n );
            for (int m : m_Array) {
                double AvgDist = RandomWalk.randomWalkMulti(m, n);
                writer.write(n + "," + m + "," + AvgDist + "\n");
                System.out.println("#_of_Steps: " + m + ", AvgDist: " + AvgDist);
            }
           
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    

	}

}
