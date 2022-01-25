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

import java.util.List;

public class PartitioningThread implements Runnable{
    private double[] values;
    private List<String[]> input;
    private int startIndex;
    private int startX;

    public PartitioningThread(double[] values, List<String[]> inputs, int startIndex, int startX) {
        this.values = values;
        this.input = inputs;
        this.startIndex = startIndex;
        this.startX = startX;
    }

    @Override
    public void run() {
        int amountOfFactors = input.get(0).length - startIndex;

        for(int j = startX; j<(startX+input.size()); j++){
            for(int i = startIndex; i<input.get(j-startX).length; i++){
                int v = j * amountOfFactors + (i - startIndex);
                if(input.get(j-startX)[i].equals("NA")){
                    values[v]=2147483647;
                }
                else{
                    values[v] = Double.parseDouble(input.get(j-startX)[i]);
                }
            }
        }
    }
}
