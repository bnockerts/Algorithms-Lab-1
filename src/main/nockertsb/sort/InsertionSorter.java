package main.nockertsb.sort;

/**
 * Created by nockertsb on 3/12/14.
 */
public class InsertionSorter implements Sorter {

    @Override
    public String getSortType() {
        return "insertion";
    }

    @Override
    public void sort(int[] values) {
        for (int i = 1; i < values.length; i++) {
            Integer value = values[i];

            int j = i - 1;

            while (j > -1 && values[j] > value) {
                values[j + 1] = values[j];
                j = j - 1;
            }

            values[j + 1] = value;
        }
    }
}
