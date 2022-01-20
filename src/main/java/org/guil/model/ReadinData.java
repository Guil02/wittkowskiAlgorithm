package org.guil.model;

import com.opencsv.CSVReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ReadinData {
    private Scanner sc;
    CSVReader reader;


    public ReadinData() {
    }

    public List<String[]> readAll(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = new ArrayList<>();
        list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }


    public void read(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<String[]> list = readAll(reader);
            for(int i = 0; i<list.size(); i++){
                System.out.println(Arrays.toString(list.get(i)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
