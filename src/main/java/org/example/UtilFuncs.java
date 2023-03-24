package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static java.lang.Long.parseLong;
import static java.lang.String.format;

public class UtilFuncs {
    public static long inSeconds(int[] arr, int h, int m, int s) {
        return arr[h] * 3600L + arr[m] * 60L + arr[s];
    }

    public static long inSeconds(int[] arr, int d, int h, int m, int s) {
        return arr[d] * 24L * 3600L + arr[h] * 3600L + arr[m] * 60L + arr[s];
    }

    public static String decFormat(int value) {
        if (value < 10) {
            return format("%02d", value);
        } else return Integer.toString(value);
    }

    public static String dateFormat(int[] dateArr) {
        String temp = "";
        if (dateArr.length == 6) {
            temp += dateArr[0];
            temp += ("-" + decFormat(dateArr[1]) + "-" + decFormat(dateArr[2]) + " ");
            temp += (decFormat(dateArr[3]) + ":" + decFormat(dateArr[4]) + ":" + decFormat(dateArr[5]));
        } else {
            temp += (decFormat(dateArr[0]) + ":" + decFormat(dateArr[1]) + ":" + decFormat(dateArr[2]));
        }
        return temp;
    }

    public static void printReport(List<Long> numbers, CDRs cdrs) {
        for (Long num : numbers) {


           /* Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the phone number to generate a report\n");
            long number = parseLong(scanner.nextLine()); */

            long number = num;

            boolean flag = false;

            for (int i = 0; i < cdrs.cdrList.size(); i++) {
                if (cdrs.cdrList.get(i).getAgentNumber() == number) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                String report = "D:\\practice\\NexignBootcampEntry\\NexignBootcampEntryTask\\reports\\report_for_" + numbers.indexOf(num) + ".txt";
                CDRs filteredByNumber = cdrs.FilterByNumber(cdrs.cdrList, number);
                try {
                    FileWriter writer = new FileWriter(report);
                    writer.write("Tariff Index: " + filteredByNumber.cdrList.get(0).getTariff().getTariffType() + "\n");
                    writer.write("----------------------------------------------------------------------------\n");
                    writer.write("Report for phone number " + filteredByNumber.cdrList.get(0).getAgentNumber() + "\n");
                    writer.write("----------------------------------------------------------------------------\n");
                    writer.write("| Call Type |     Start Time      |       End Time      | Duration | Cost |\n");
                    writer.write("----------------------------------------------------------------------------\n");
                    for (CDR cdr : filteredByNumber.cdrList) {
                        writer.write("|     " + cdr.getCallType() + "     | " + dateFormat(cdr.getCallStart()) +
                                " | " + dateFormat(cdr.getCallEnd()) + " | " + dateFormat(cdr.getCallDuration()) + " |  " +
                                "" + cdrs.getCallCost(cdr) + " |\n");
                    }
                    writer.write("----------------------------------------------------------------------------\n");
                    writer.write("|                                           Total Cost: |" +
                            "    " + filteredByNumber.agentBilling(filteredByNumber.cdrList) + " rubles   |\n");
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

    public static void getData(String path, List<String> incomingDataList, List<Long> numbers) {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(path))) {

            String line;
            while ((line = reader.readLine()) != null) {
                incomingDataList.add(line);
                numbers.add(parseLong(line.substring(4, 15)));
            }
        } catch (IOException e) {
            System.err.println("Error from reading file: " + e.getMessage());
        }
    }


}
