package org.example;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;


public class Main {
    public static void main(String[] args) {
        List<String> incomingDataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\practice\\NexignBootcampEntry\\NexignBootcampEntryTask\\data\\test.txt"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                incomingDataList.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error from reading file: " + e.getMessage());
        }

        String[] incomingData = incomingDataList.toArray(new String[0]);

        CDRs cdrs = new CDRs(incomingData);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the phone number to generate a report");
        long number = parseLong(scanner.nextLine());

        boolean flag = false;

        for (int i = 0; i < incomingData.length; i++) {
            if (cdrs.cdrList.get(i).getAgentNumber() == number) {
                flag = true;
                break;
            }
        }
        if (flag) {
            String report = "D:\\practice\\NexignBootcampEntry\\NexignBootcampEntryTask\\reports\\report.txt";
            CDRs filteredByNumber = cdrs.FilterByNumber(cdrs.cdrList, number);
            try {
                FileWriter writer = new FileWriter(report);
                writer.write("Tariff Index: "+filteredByNumber.cdrList.get(0).getTariff().getTariffType()+"\n");
                writer.write("----------------------------------------------------------------------------\n");
                writer.write("Report for phone number " + filteredByNumber.cdrList.get(0).getAgentNumber()+"\n");
                writer.write("----------------------------------------------------------------------------\n");
                writer.write("| Call Type |     Start Time      |       End Time      | Duration | Cost |\n");
                writer.write("----------------------------------------------------------------------------\n");
                for (CDR cdr: filteredByNumber.cdrList) {
                    writer.write("|     "+cdr.getCallType()+"     | "+cdr.dateFormat(cdr.getCallStart())+
                            " | "+cdr.dateFormat(cdr.getCallEnd())+" | "+cdr.dateFormat(cdr.getCallDuration())+" |  " +
                            ""+cdrs.getCallCost(cdr)+" |\n");
                }
                writer.write("----------------------------------------------------------------------------\n");
                writer.write("|                                           Total Cost: |     "+filteredByNumber.getTotalCost() +"rubles   |\n");
                writer.write("----------------------------------------------------------------------------");

                writer.close();
                System.out.println("Successful!");
            } catch (IOException e) {
                System.out.println("An error occurred while writing to file " + report);
                e.printStackTrace();
            }
        } else {
            System.out.print("Number not found");
        }

        // ввод номера
        System.out.println("Hello world!");
    }
}