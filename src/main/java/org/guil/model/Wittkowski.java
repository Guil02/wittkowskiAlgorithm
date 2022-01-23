/*
 * MIT License
 *
 * Copyright (c) 2022 Guil02
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.guil.model;

import org.guil.utils.Functions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Wittkowski {
    private List<String[]> list;
    private String[] firstRow;
    private final int amountOfProcessors;
    private final int amountOfRows;
    private ArrayList<Double> output;
    private static final boolean temp = false;

    public Wittkowski(List<String[]> list) {
        firstRow = list.get(0);
        list.remove(0);
        this.list = list;

        amountOfProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println(amountOfProcessors + " processors available");
        amountOfRows = list.size();
        intializeOutputList();
    }

    public void run() {
        List<List<String[]>> partition = Functions.partitionList(list, amountOfProcessors);
        ProcessingThread[] task = new ProcessingThread[amountOfProcessors];
        Thread[] threads = new Thread[amountOfProcessors];
        for(int i = 0; i<amountOfProcessors; i++){
            task[i] = new ProcessingThread(partition.get(i), this);
            threads[i] = new Thread(task[i]);
            threads[i].start();
        }
        for(int i = 0; i <task.length; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(list.size());
    }


    private void intializeOutputList() {
       output = new ArrayList<>();
       for(int i = 0; i<amountOfRows; i++){
           output.add(0.0);
       }
    }
    public void processItem(String[] strings) {
//        System.out.println(Arrays.toString(strings));
    }
}
