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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Wittkowski {
    private List<String[]> list;
    private String[] firstRow;
    private final int amountOfProcessors;
    public Wittkowski(List<String[]> list) {
        firstRow = list.get(0);
        list.remove(0);
        this.list = list;
        amountOfProcessors = Runtime.getRuntime().availableProcessors();
    }

    public void run() {
        int sampleSize = list.size();
        ExecutorService executorService = Executors.newFixedThreadPool(amountOfProcessors);
        List<Future> tasks = new ArrayList<>();

        for (int offset = 0; offset < amountOfProcessors; offset++) {

            int finalParallelism = amountOfProcessors;
            int finalOffset = offset;
            Future task = executorService.submit(() -> {
                int i = finalOffset;
                while (list.size() > i) {
                    processItem(list.get(i));
                    i += finalParallelism;
                }
            });
            tasks.add(task);
        }
        for (Future task : tasks) {
            try {
                task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
    }
    private void processItem(String[] strings) {
        System.out.println(Arrays.toString(strings));
    }
}
