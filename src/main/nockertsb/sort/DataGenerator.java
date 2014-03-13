package main.nockertsb.sort;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by nockertsb on 3/12/14.
 */
public class DataGenerator {

    public static void generateRandomNumbers(int n) {
        File output = new File("data\\averageCase_" + n + ".txt");
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileOutputStream(output));

            for (int i = 0; i < n; i++) {
                int val = (int) Math.round(Math.random() * n*2);
                writer.write(val + " ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void generateBestCase(int n) {
        File output = new File("data\\bestCase_" + n + ".txt");
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileOutputStream(output));

            for (int i = 0; i < n; i++) {
                writer.write(i + " ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void generateWorstCase(int n) {
        File output = new File("data\\worstCase_" + n + ".txt");
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileOutputStream(output));

            for (int i = 0; i < n; i++) {
                writer.write((n - i) + " ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
