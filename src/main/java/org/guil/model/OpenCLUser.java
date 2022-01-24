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
import org.jocl.*;

import static org.jocl.CL.*;
import static org.jocl.CL.clReleaseContext;

public class OpenCLUser {
    private final static String programSource =
            "__kernel void sampleKernel(__global int* arr, __global int *out, __global const int *amountOfFactors, __global const int *size){" +
                    "      int k = get_global_id(0);" +
                    "    " +
                    "        for (int i = 0; i < *size; i++) {" +
                    "            if (k != i) {" +
                    "                int evals[*amountOfFactors];" +
                    "                for (int j = 0; j < *amountOfFactors; j++) {" +
                    "                    if (arr[k**amountOfFactors+j] == -2 || arr[i**amountOfFactors+j] == -2) {" +
                    "                        evals[j] = 0;" +
                    "                    } else if (arr[k**amountOfFactors+j] < arr[i**amountOfFactors+j]) {" +
                    "                        evals[j] = -1;" +
                    "                    } else if (arr[k**amountOfFactors+j] > arr[i**amountOfFactors+j]) {" +
                    "                        evals[j] = 1;" +
                    "                    } else {" +
                    "                        evals[j] = 0;" +
                    "                    }" +
                    "                }" +
                    "" +
                    "                int max = -1;" +
                    "                int min = 1;" +
                    "                for (int j = 0; j < *amountOfFactors; j++) {" +
                    "                    if (evals[j] > max) {" +
                    "                        max = evals[j];" +
                    "                    }" +
                    "                    if (evals[j] < min) {" +
                    "                        min = evals[j];" +
                    "                    }" +
                    "                }" +
                    "" +
                    "                if ((min == 0 && max == 0) || (min == -1 && max == 1)) {" +
                    "                    out[k] = out[k] + 0;" +
                    "                } else if (min >= 0 && max == 1) {" +
                    "                    out[k] = out[k] + 1;" +
                    "                } else if (min == -1 && max <= 0) {" +
                    "                    out[k] = out[k] - 1;" +
                    "                }" +
                    "            }" +
                    "        " +
                    "    }" +
                    "}";
    private int n;
    private int amountOfFactors;
    private int[] srcArrayA;
    private int[] dstArray;

    public OpenCLUser(int n, int amountOfFactors, int[] srcArrayA, int[] dstArray) {
        this.n = n;
        this.amountOfFactors = amountOfFactors;
        this.srcArrayA = srcArrayA;
        this.dstArray = dstArray;
    }

    public void run(){
        Pointer srcA = Pointer.to(srcArrayA);
        Pointer dst = Pointer.to(dstArray);
        Pointer factors = Pointer.to(new int[]{amountOfFactors});
        Pointer size = Pointer.to(new int[]{n});

        // The platform, device type and device number
        // that will be used
        final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_ALL;
        final int deviceIndex = 0;

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];

        // Obtain a device ID
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Create a context for the selected device
        cl_context context = clCreateContext(
                contextProperties, 1, new cl_device_id[]{device},
                null, null, null);

        // Create a command-queue for the selected device
        cl_command_queue commandQueue =
                clCreateCommandQueue(context, device, 0, null);

        // Allocate the memory objects for the input- and output data
        cl_mem memObjects[] = new cl_mem[4];
        memObjects[0] = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * n*amountOfFactors, srcA, null);
        memObjects[1] = clCreateBuffer(context,
                CL_MEM_READ_WRITE,
                Sizeof.cl_int * n, null, null);
        memObjects[2] = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int, factors, null);
        memObjects[3] = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int, size, null);

        // Create the program from the source code
        cl_program program = clCreateProgramWithSource(context,
                1, new String[]{ programSource }, null, null);

        // Build the program
        clBuildProgram(program, 0, null, null, null, null);

        // Create the kernel
        cl_kernel kernel = clCreateKernel(program, "sampleKernel", null);

        // Set the arguments for the kernel
        clSetKernelArg(kernel, 0,
                Sizeof.cl_mem, Pointer.to(memObjects[0]));
        clSetKernelArg(kernel, 1,
                Sizeof.cl_mem, Pointer.to(memObjects[1]));
        clSetKernelArg(kernel, 2,
                Sizeof.cl_mem, Pointer.to(memObjects[2]));
        clSetKernelArg(kernel, 3,
                Sizeof.cl_mem, Pointer.to(memObjects[3]));

        // Set the work-item dimensions
        long global_work_size[] = new long[]{n};
        long local_work_size[] = new long[]{1};

        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        // Read the output data
        clEnqueueReadBuffer(commandQueue, memObjects[1], CL_TRUE, 0,
                n * Sizeof.cl_int, dst, 0, null, null);

        // Release kernel, program, and memory objects
        clReleaseMemObject(memObjects[0]);
        clReleaseMemObject(memObjects[1]);
        clReleaseMemObject(memObjects[2]);
        clReleaseMemObject(memObjects[3]);
        clReleaseKernel(kernel);
        clReleaseProgram(program);
        clReleaseCommandQueue(commandQueue);
        clReleaseContext(context);

//
        if (n <= 100)
        {
            System.out.println("Result: "+java.util.Arrays.toString(dstArray));
        }

        Functions.writeOutput(dstArray);
    }
}
