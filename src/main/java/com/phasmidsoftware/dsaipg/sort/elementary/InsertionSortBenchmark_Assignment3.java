package com.phasmidsoftware.dsaipg.sort.elementary;


import com.phasmidsoftware.dsaipg.sort.elementary.InsertionSort;
import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;
import com.phasmidsoftware.dsaipg.util.Config;
import com.phasmidsoftware.dsaipg.util.Config_Benchmark;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;


public class InsertionSortBenchmark_Assignment3  {
	
	public static void main(String[] args) throws IOException {

        int[] sizes = {500, 1000, 2000, 4000, 8000};
        
        Config config = Config_Benchmark.setupConfigFixes();
        
        
        System.out.println("n,ordering,time(ms)");
        for (int n : sizes) {
            benchmark(n, config);
        }
        System.out.println("Done.");
    }

   
    private static void benchmark(int n, Config config) {
       
        runBenchmark("random", n, () -> generateRandomArray(n));
        runBenchmark("ordered", n, () -> generateOrderedArray(n));
        runBenchmark("partially-ordered", n, () -> generatePartiallyOrderedArray(n));
        runBenchmark("reverse-ordered", n, () -> generateReverseOrderedArray(n));
    }

    
    private static void runBenchmark(String orderingType, int n, Supplier<Integer[]> supplier) {

        Benchmark_Timer<Integer[]> benchmark = new Benchmark_Timer<>(
                "Insertion Sort " + orderingType,
                array -> {
                    Integer[] copy = Arrays.copyOf(array, array.length);
                    new InsertionSort<Integer>().mutatingSort(copy);
                }
        );

        Integer[] array = supplier.get();

        double timeMs = benchmark.runFromSupplier(() -> Arrays.copyOf(array, array.length), 10);

        System.out.printf("%d,%s,%.3f%n", n, orderingType, timeMs);
    }

    /**
     * Generates a randomly generated array of size n.
     */
    private static Integer[] generateRandomArray(int n) {
        Random random = new Random();
        Integer[] array = new Integer[n];
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(10000);
        }
        return array;
    }

    /**
     * Generates an ordered (ascending) array of size n.
     */
    private static Integer[] generateOrderedArray(int n) {
        Integer[] array = new Integer[n];
        for (int i = 0; i < n; i++) {
            array[i] = i;
        }
        return array;
    }

    /**
     * Generates a partially ordered array, to simplify this process, 
     * we'll let the first half of array sorted, the second half random.
     */
    private static Integer[] generatePartiallyOrderedArray(int n) {
        Integer[] array = new Integer[n];
        Random random = new Random();

        for (int i = 0; i < n / 2; i++) {
            array[i] = i;
        }

        for (int i = n / 2; i < n; i++) {
            array[i] = random.nextInt(10000);
        }
        return array;
    }

    /**
     * Generates a reverse-ordered (descending) array.
     */
    private static Integer[] generateReverseOrderedArray(int n) {
        Integer[] array = new Integer[n];
        for (int i = 0; i < n; i++) {
            array[i] = n - i;
        }
        return array;
    }
}