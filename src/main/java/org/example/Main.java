package org.example;

import static org.example.UtilFuncs.*;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        List<Long> numbers = new ArrayList<>();
        List<String> incomingDataList = new ArrayList<>();

        String path = "D:\\practice\\NexignBootcampEntry\\NexignBootcampEntryTask\\data\\test.txt";
        String reportRootPath = "D:\\practice\\NexignBootcampEntry\\NexignBootcampEntryTask\\reports\\report_for_";
        getData(path, incomingDataList, numbers);

        Set<Long> set = new HashSet<>(numbers);
        numbers.clear();
        numbers.addAll(set);

        String[] incomingData = incomingDataList.toArray(new String[0]);
        CDRs cdrs = new CDRs(incomingData);

        //uncomment to get report for all numbers/for 1 certain number
        //printAllReport(cdrs, numbers, reportRootPath);

        /*
        printReportByNumber(cdrs, reportRootPath);
        */




    }
}