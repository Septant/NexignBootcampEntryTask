package org.application;

import static org.application.UtilFuncs.*;
import java.util.*;


/**
 * According to the task, this program gets a set of data of a certain format {@link CDR}.
 * After that it performs tarification: filtering each {@link CDR} by agent number.
 * Each certain CDR list is sorted by start of the call time.
 * Each CDR contains: type, time start, time end, duration, cost and final cost for tariff period.
 * */
public class Main {
    public static void main(String[] args) {

        List<Long> numbers = new ArrayList<>();
        List<String> incomingDataList = new ArrayList<>();             // initiates lists for agents and data from input.

        String path = "./data/test.txt";
        String reportRootPath = "./reports/";

        getData(path, incomingDataList, numbers);                      // gets data from input file.

        Set<Long> set = new HashSet<>(numbers);
        numbers.clear();                                               // deletes copies.
        numbers.addAll(set);

        String[] incomingData = incomingDataList.toArray(new String[0]);
        CDRs cdrs = new CDRs(incomingData);                            // initiates list of records based on data got.

        printAllReport(cdrs, numbers, reportRootPath);


        //printReportByNumber(cdrs, reportRootPath);                   // for a specified agent numbers

    }
}