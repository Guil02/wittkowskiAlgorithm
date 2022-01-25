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

public class Wittkowski {
    private List<String[]> list;
    private String[] firstRow;
    private final int amountOfProcessors;
    private final int amountOfRows;
    private ArrayList<Integer> output;
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
        System.out.println("Started partitioning list");
        long startTime = System.currentTimeMillis();
        List<List<String[]>> partition = Functions.partitionList(list, amountOfProcessors);
        System.out.println("Done partitioning list. Time taken: "+(System.currentTimeMillis()-startTime) +"ms\n started threading");
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
        long endTime = System.currentTimeMillis();
        System.out.println("Finished threading");
        System.out.println("Time taken: "+(endTime-startTime)+"ms");
        System.out.println(output);
        Functions.writeOutput(output);
//        Functions.toPrint(list);
    }


    private void intializeOutputList() {
       output = new ArrayList<>();
       for(int i = 0; i<amountOfRows; i++){
           output.add(0);
       }
    }

    public void processItem(String[] strings) {
        int val =0;
        for (int i = 0; i < list.size(); i++) {
            if (i+1 != Integer.parseInt(strings[0])) {
                val+=compare(strings, list.get(i));
            }
        }
        output.set(Integer.parseInt(strings[0])-1,val);
    }

    private int compare(String[] initial, String[] compare) {

        int[] values = new int[initial.length-10];
        for (int i = 10; i < initial.length; i++) {
            if (initial[i].equals("NA") || compare[i].equals("NA")){
                values[i-10]=0;
                continue;
            }
            int val1 = Integer.parseInt(initial[i]);
            int val2 = Integer.parseInt(compare[i]);
            if(val1>val2){
                values[i-10]=1;
            }
            else if(val1<val2){
                values[i-10]=-1;
            }
            else{
                values[i-10]=0;
            }
        }


        int max = -1;
        int min = 1;
        for(int i = 0; i<values.length; i++){
            if(values[i]>max){
                max = values[i];
            }
            else if(values[i]<min){
                min = values[i];
            }
        }

        if ((min == 0 && max == 0) || (min == -1 && max == 1)) {
//            System.out.println("i: "+initial[0]+" j: "+compare[0]+" internal: "+Arrays.toString(values)+"\nmin: "+min+" max: "+max+ " output: "+0);
            return 0;
        } else if (min >= 0 && max == 1) {
//            System.out.println("i: "+initial[0]+" j: "+compare[0]+" internal: "+Arrays.toString(values)+"\nmin: "+min+" max: "+max+ " output: "+1);
            return 1;
        } else if (min == -1 && max <= 0) {
//            System.out.println("i: "+initial[0]+" j: "+compare[0]+" internal: "+Arrays.toString(values)+"\nmin: "+min+" max: "+max+ " output: "+-1);
            return -1;
        }

        return 0;
    }

    private int[] gpuOutput;
    private static final boolean useThreading = true;

    public void GPURun(){
        int sampleSize = list.size();
        int amountOfFactors = list.get(0).length - 10;
        System.out.println("Started preparing array");
        long startPrepTime = System.currentTimeMillis();
        double[] values;
        if(useThreading){
            values = Functions.prepareArrayForGPUThreading(list, 10, amountOfProcessors);
        }
        else{
            values = Functions.prepareArrayForGPU(list, 10);

        }
        long endPrepTime = System.currentTimeMillis();
        System.out.println("Finished preparing array, time taken: " + (endPrepTime - startPrepTime) + "ms");
        int[] destinationArray = new int[sampleSize];

        OpenCLUser gpu = new OpenCLUser(sampleSize, amountOfFactors, values, destinationArray);
        System.out.println("GPU running started");
        long startTime = System.currentTimeMillis();
        gpu.run();
        long endTime = System.currentTimeMillis();
        System.out.println("GPU running finished");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
        gpuOutput = destinationArray;
    }

    public int[] getGpuOutput() {
        return gpuOutput;
    }
}
