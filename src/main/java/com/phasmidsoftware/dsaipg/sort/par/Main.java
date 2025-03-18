/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * CONSIDER tidy it up a bit.
 */
public class Main {

//    public static void main(String[] args) {
//        //processArgs(args);
//        System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());
//        Random random = new Random();
//        int[] array = new int[2000000];
//        ArrayList<Long> timeList = new ArrayList<>();
//        for (int j = 50; j < 100; j++) {
//            ParSort.cutoff = 10000 * (j + 1);
//            // for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
//            long time;
//            long startTime = System.currentTimeMillis();
//            for (int t = 0; t < 10; t++) {
//                for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
//                ParSort.sort(array, 0, array.length);
//            }
//            long endTime = System.currentTimeMillis();
//            time = (endTime - startTime);
//            timeList.add(time);
//
//
//            System.out.println("cutoff：" + (ParSort.cutoff) + "\t\t10times Time:" + time + "ms");
//
//        }
//        try {
//            FileOutputStream fis = new FileOutputStream("./src/result_fixed_array_size.csv");
//            OutputStreamWriter isr = new OutputStreamWriter(fis);
//            BufferedWriter bw = new BufferedWriter(isr);
//            int j = 0;
//            for (long i : timeList) {
//                String content = (double) 10000 * (j + 1) / 2000000 + "," + (double) i / 10 + "\n";
//                j++;
//                bw.write(content);
//                bw.flush();
//            }
//            bw.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

	    public static void main(String[] args) {
	        
	        int maxThreads = ForkJoinPool.getCommonPoolParallelism(); // Get available system threads
	        System.out.println("Degree of parallelism: " + maxThreads);
	        
	        // N=200000000 with t=2 (cutoff=100000000) gives us OutOfMemoryError: Java heap space
	        int[] arraySizes = {2000000, 4000000, 20000000, 40000000, 100000000};
	        
	        //currently my Degree of parallelism == 7, giving us max # of thread = 7+1 = 8
	        int[] threadCounts = {1, 2, 4, 8, 16}; // # of Threads

	        ArrayList<String> results = new ArrayList<>();
	        results.add("ArraySize,Threads,Cutoff,Time(ms)\n");

	        for (int N : arraySizes) {
	            for (int t : threadCounts) {
	                if (t > (maxThreads+1)*2) continue; // Prevent exceeding system parallelism
	                
	                int cutoff = N / t; // Set cutoff dynamically
	                ParSort.cutoff = cutoff;

	                System.out.println("Testing N=" + N + " with t=" + t + " (cutoff=" + cutoff + ")");

	                int[] array = new int[N];
	                Random random = new Random();
	                for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);

	                long startTime = System.currentTimeMillis();
	                ParSort.sort(array, 0, array.length);
	                long endTime = System.currentTimeMillis();

	                long timeTaken = endTime - startTime;
	                System.out.println("Time taken: " + timeTaken + "ms");
	                results.add(N + "," + t + "," + cutoff + "," + timeTaken + "\n");
	            }
	        }

	        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("result.csv")))) {
	            for (String line : results) writer.write(line);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}