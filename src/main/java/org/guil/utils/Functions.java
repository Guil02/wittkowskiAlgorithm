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

import java.io.FileWriter;
import java.io.IOException;
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

    public static void writeOutput(List<Integer> output){
        try {
            FileWriter writer = new FileWriter("output.txt");
            for(int i = 0; i<output.size(); i++){
                String out = output.get(i) + "\n";
                writer.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void print2dArray(int[][] array){
        for (int[] ints : array) {
            System.out.println(Arrays.toString(ints));
        }
    }
}
