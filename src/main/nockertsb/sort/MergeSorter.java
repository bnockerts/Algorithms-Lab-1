package main.nockertsb.sort;

import java.util.Arrays;

/**
 * Created by nockertsb on 3/12/14.
 * Algorithm based on approved solution to the question found at
 * http://stackoverflow.com/questions/13727030/mergesort-in-java
 */
public class MergeSorter implements Sorter {

    @Override
    public String getSortType() {
        return "merge";
    }

    @Override
    public void sort(int[] values) {
        if (values.length > 1) {
            int middle = values.length / 2;

            int[] left = Arrays.copyOfRange(values, 0, middle);
            int[] right = Arrays.copyOfRange(values, middle, values.length);

            sort(left);
            sort(right);

            merge(values, left, right);
        }
    }

    private void merge(int[] A, int[] left, int[] right) {
        int n = left.length + right.length;
        int i = 0; //index for full array
        int il = 0; //index for left array
        int ir = 0; //index for right array

        while (i < n) {
            if ((il < left.length) && (ir < right.length)) {
                if (left[il] < right[ir]) {
                    A[i] = left[il];
                    il++;
                } else {
                    A[i] = right[ir];
                    ir++;
                }

                i++;
            } else {
                if (il >= left.length) {
                    while (ir < right.length) {
                        A[i] = right[ir];
                        i++;
                        ir++;
                    }
                } else {
                    while (il < left.length) {
                        A[i] = left[il];
                        i++;
                        il++;
                    }
                }
            }
        }
    }
}
