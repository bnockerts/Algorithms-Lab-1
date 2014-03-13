package main.nockertsb;

import main.nockertsb.sort.InsertionSorter;
import main.nockertsb.sort.MergeSorter;
import main.nockertsb.sort.Sorter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by nockertsb on 3/12/14.
 */
public class SorterApp {

    public static String OUTPUT_FILE_BASE = "results";
    public static String OUTPUT_FILE_EXTENSION = ".csv";
    public static int[] DATA_SIZES = {100, 1000}; /*10000, 50000, 100000, 200000, 300000, 400000, 500000, 600000, 700000,
            800000, 900000, 1000000};*/
    public static int NUM_TRIALS = 1;
    public static String[] TRIAL_TYPES = {"best", "average", "worst"};

    private Map<String, Map<Integer, List<Double>>> results;
    private File outputFile;

    public static void main(String[] args) {

        // Generate test data
        for (int i: DATA_SIZES) {
            main.nockertsb.sort.DataGenerator.generateRandomNumbers(i);
            main.nockertsb.sort.DataGenerator.generateBestCase(i);
            main.nockertsb.sort.DataGenerator.generateWorstCase(i);
        }

        SorterApp app = new SorterApp();
        app.benchmarkSortAlgorithm(new MergeSorter());
    }

    public SorterApp() {
        results = new HashMap();

        for (String trialType : TRIAL_TYPES) {
            Map<Integer, List<Double>> treeMap = new TreeMap();
            for (int dataSetSize : DATA_SIZES) {
                treeMap.put(new Integer(dataSetSize), new ArrayList<Double>());
            }

            results.put(trialType, treeMap);
        }

    }

    public void benchmarkSortAlgorithm(Sorter sorter) {
        outputFile = new File(OUTPUT_FILE_BASE + "_" + sorter.getSortType() + OUTPUT_FILE_EXTENSION);
        long testStart = System.nanoTime();

        //for (String trialType : TRIAL_TYPES) {
            for (int trial = 1; trial <= NUM_TRIALS; trial++) {
                //                System.out.println("Starting Trial " + trial + "......");
                //System.out.printf("Starting %s case Trial %d...%n", trialType, trial);
                long trialStart = System.nanoTime();

                runTrial(sorter, "average");

                long trialEnd = System.nanoTime();

                //System.out.println("Trial " + trial + " complete in " + getTimeSpanInMillis(trialStart, trialEnd) + " milliseconds\n");
                //System.out.printf("%s case Trial %d completed in %6.2f milliseconds%n%n", trialType, trial, getTimeSpanInMillis(trialStart, trialEnd));
            }
        //}

        long testEnd = System.nanoTime();

        //System.out.println("\n" + NUM_TRIALS + " trials completed in " + getTimeSpanInSeconds(testStart, testEnd) + "seconds");
        System.out.printf("%n%d trials completed in %6.2f seconds%n%n", NUM_TRIALS, getTimeSpanInSeconds(testStart, testEnd));

        System.out.printf("Writing results to %s%n%n", outputFile.getAbsoluteFile());


        boolean success = printResultsToFile();

        if (success) {
            System.out.printf("SUCCESS - Results written to %s%n%n", outputFile.getAbsoluteFile());
        } else {
            System.out.printf("FAILURE - Could not write to %s.%n", outputFile.getAbsoluteFile());
            System.out.println("Would you like to retry [Y/N]?");
            Scanner scanner = new Scanner(System.in);
            String input = null;

            while (scanner.hasNext()) {
                input = scanner.next();

                if (input.equalsIgnoreCase("Y")) {
                    printResultsToFile();
                } else {
                    System.exit(1);
                }
            }
        }


    }

    private void runTrial(Sorter sorter, String type) {
        for (int numDataPoints : DATA_SIZES) {
            System.out.printf("  Sorting data set of size %d%n", numDataPoints);
            int[] data = new int[numDataPoints];
            String fileName = "data/" + type + "Case_" + numDataPoints + ".txt";

            readDataFromFile(fileName, data);

            if (data != null) {
                double sortTime = doSort(sorter, data);
                results.get(type).get(new Integer(numDataPoints)).add(sortTime);
            }

            System.out.println();

            for (int i = 0; i < data.length; i++) {
                System.out.print(data[i] + " ");
            }

            System.out.println();
        }
    }

    private void readDataFromFile(String filename, int[] data) {
        int i = 0;
        Scanner scanner = null;

        try {
            scanner = new Scanner(new File(filename));

            while (scanner.hasNext()) {
                data[i] = Integer.parseInt(scanner.next());
                i++;
            }
        } catch (FileNotFoundException e) {
            data = null;
            System.err.println("Could not find file " + filename);
            System.exit(1);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private Double doSort(Sorter sorter, int[] data) {
        long startTime = System.nanoTime();
        sorter.sort(data);
        long endTime = System.nanoTime();

        return new Double(getTimeSpanInMillis(startTime, endTime));
    }

    private boolean printResultsToFile() {
        boolean success = false;
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(outputFile);
            writer.write(getOutputString());
            success = true;
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

        return success;
    }

    private String getOutputString() {
        StringBuilder builder = new StringBuilder("Trial Type,Data Points,");

        //Build header row for .csv file
        for (int i = 0; i < NUM_TRIALS; i++) {
            builder.append("Trial ")
                    .append((i + 1))
                    .append(",");

        }

        builder.append("Average\n");

        int row = 2;

        //Build a row for each different size dataset used that includes all the trials for each
        for (Map.Entry<String, Map<Integer, List<Double>>> entry : results.entrySet()) {
            for (Map.Entry<Integer, List<Double>> innerEntry : entry.getValue().entrySet()) {
                List<Double> trialResults = innerEntry.getValue();
                builder.append(entry.getKey() + "," + innerEntry.getKey().intValue() + ",");

                for (int i = 0; i < trialResults.size(); i++) {
                    builder.append(trialResults.get(i))
                            .append(",");

                }

                // Add an extra column to each row that calculates the average of the trials when
                // you open the .csv file in Excel
                builder.append("=AVERAGE(C")
                        .append(row)
                        .append(":")
                        .append((char) ('C' + NUM_TRIALS - 1))
                        .append(row)
                        .append(")\n");

                row++;
            }

            builder.append("\n");
            row++;
        }

        return builder.toString();
    }

    private double getTimeSpanInMillis(long timeStart, long timeEnd) {
        return (timeEnd - timeStart) * 0.000001;
    }

    private double getTimeSpanInSeconds(long timeStart, long timeEnd) {
        return (timeEnd - timeStart) * 0.000000001;
    }
}
