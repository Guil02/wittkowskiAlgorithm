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

package org.guil.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Functions {
    public static List<List<String[]>> partitionList(List<String[]> list, int partitions) {
        List<List<String[]>> output = new ArrayList<>();
        int val = list.size()/partitions +1;

        for(int i = 0; i<partitions; i++){
            List<String[]> part = new ArrayList<>();
            for(int j = i*val; j<(i+1)*val && j<list.size(); j++){
                part.add(list.get(j));
            }
            output.add(part);
        }
        return output;
    }

    public static void toPrint(List<String[]> output){
        int index = 0;
        for(int i = 0; i<output.size(); i++){
            for(int j = 10; j<output.get(i).length; j++){
                System.out.println("srcArrayA["+index+"]="+output.get(i)[j]+";");
                index++;
            }
        }
    }

    public static void writeOutput(List<Integer> output){
        try (FileWriter writer = new FileWriter("output.csv");
             BufferedWriter bw = new BufferedWriter(writer)) {
            for(int i = 0; i<output.size(); i++){
                bw.write((i+1)+","+output.get(i)+"\n");
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public static void writeOutput(int[] output) {
        try (FileWriter writer = new FileWriter("output.csv");
             BufferedWriter bw = new BufferedWriter(writer)) {
            for(int i = 0; i<output.length; i++){
                bw.write((i+1)+","+output[i]+"\n");
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public static void print2dArray(int[][] array){
        for (int[] ints : array) {
            System.out.println(Arrays.toString(ints));
        }
    }

    public static int[] prepareArrayForGPU(List<String[]> input, int startIndex){
        int amountOfFactors = input.get(0).length - startIndex;
        int[] value = new int[amountOfFactors *input.size()];
        for(int j = 0; j<input.size(); j++){
            for(int i = startIndex; i<input.get(j).length; i++){
                if(input.get(j)[i].equals("NA")){
                    value[j*amountOfFactors+i]=-2;
                }
                else{
                    value[j*amountOfFactors+(i-startIndex)] = Integer.parseInt(input.get(j)[i]);
                }
            }
        }
        return value;
    }

    public static void addOutputToCsv(int[] destinationArray, File file, int startIndex) {
        String[] lines = new String[destinationArray.length+1];
        String[] output = new String[destinationArray.length+1];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            int index = 0;
            while(index<destinationArray.length+1){
                lines[index++] = reader.readLine();
            }

            for(int i = 0; i<lines.length; i++){
                int amountOfCommas = 0;
                int insertionIndex = 0;
                while(amountOfCommas<startIndex){
                    if(lines[i].charAt(insertionIndex)==','){
                        amountOfCommas++;
                    }
                    insertionIndex++;
                }
                StringBuilder builder = new StringBuilder(lines[i]);
                StringBuilder subBuilder = new StringBuilder();
                if(i == 0){
                    subBuilder.append("rank,");
                    builder.insert(insertionIndex,subBuilder);
                    output[i]=builder.toString();
                    continue;
                }
                subBuilder.append(destinationArray[i-1]);
                subBuilder.append(",");
                builder.insert(insertionIndex, subBuilder);
                output[i]=builder.toString();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter("output.csv");
             BufferedWriter bw = new BufferedWriter(writer)) {
            for(int i = 0; i<output.length; i++){
                bw.write(output[i]+"\n");
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

    }
}
