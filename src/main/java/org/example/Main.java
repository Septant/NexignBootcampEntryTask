package org.example;

import static org.example.UtilFuncs.*;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        List<Long> numbers = new ArrayList<>();
        List<String> incomingDataList = new ArrayList<>();

        String path = "D:\\practice\\NexignBootcampEntry\\NexignBootcampEntryTask\\data\\test.txt";

        getData(path, incomingDataList, numbers);

        Set<Long> set = new HashSet<Long>(numbers);
        numbers.clear();
        numbers.addAll(set);

        String[] incomingData = incomingDataList.toArray(new String[0]);
        CDRs cdrs = new CDRs(incomingData);
        printReport(numbers, cdrs);

    }
}