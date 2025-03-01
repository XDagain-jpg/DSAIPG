package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.Comparator;
import java.util.Random;


import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;


public class HeapBenchmark_Assignment4 {
	
	public static void main(String[] args) {
        int M = 4095; // Initial heap size
        int insertions = 16000;
        int removals = 4000;
        int numTests = 5; // Run the test 5 times, doubling each time

        System.out.println("HeapType,M,Insertions,Removals,Time(ms),MaxPrioritySpilled");

        for (int i = 0; i < numTests; i++) {
            runBenchmark("BinaryHeap", M, insertions, removals, false, false);
            runBenchmark("BinaryHeap-Floyd", M, insertions, removals, false, true);
            runBenchmark("4aryHeap", M, insertions, removals, true, false);
            runBenchmark("4aryHeap-Floyd", M, insertions, removals, true, true);

            // Double values for next test
            M *= 2;
            insertions *= 2;
            removals *= 2;
        }

        System.out.println("Benchmark completed.");
    }

    private static void runBenchmark(String heapType, int M, int insertions, int removals, boolean is4ary, boolean floyd) {
    	Random random = new Random();

        PriorityQueue<Integer> pq = createPriorityQueue(M, is4ary, floyd);

        Benchmark_Timer<PriorityQueue<Integer>> benchmark = new Benchmark_Timer<>(
                heapType,
                heap -> {
                    for (int i = 0; i < insertions; i++) 
                        heap.give(random.nextInt(1000000));
                
                    for (int i = 0; i < removals; i++) {
                        if (!heap.isEmpty()) 
                            try {
                                heap.take();
                            } catch (PQException e) {
                                e.printStackTrace();
                            };
                    }
                }
        );

        double timeMs = benchmark.runFromSupplier(() -> pq, 10);

        System.out.printf("%s,%d,%d,%d,%.3f,%d%n",
                heapType, M, insertions, removals, timeMs, pq.getMaxPrioritySpilled());
    }

    private static PriorityQueue<Integer> createPriorityQueue(int M, boolean is4ary, boolean floyd) {
        if (is4ary) {
            return new PriorityQueue_4ary<Integer>(M, true ,Comparator.naturalOrder(), floyd);
        } else {
            return new PriorityQueue<Integer>(M, true, Comparator.naturalOrder(), floyd);
        }
    }
}
