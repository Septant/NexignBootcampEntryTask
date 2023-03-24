package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static java.lang.Long.parseLong;
import static java.lang.String.format;

/**
 * Functions not directly connected with other classes, but used in them.
 */

public class UtilFuncs {

    /**
     * @param arr : array size of 3.
     * @param h   : hours of cdr.
     * @param m   : minutes of cdr.
     * @param s   :  minutes of cdr.
     * @return : hh:mm:ss in seconds format.
     */
    public static long inSeconds(int[] arr, int h, int m, int s) {
        return arr[h] * 3600L + arr[m] * 60L + arr[s];
    }

    /**
     * @param arr : array size of 4.
     * @param d   : days of cdr.
     * @param h   : hours of cdr.
     * @param m   : minutes of cdr.
     * @param s   : minutes of cdr.
     * @return    : DD:hh:mm:ss in seconds format.
     */
    public static long inSeconds(int[] arr, int d, int h, int m, int s) {
        return arr[d] * 24L * 3600L + arr[h] * 3600L + arr[m] * 60L + arr[s];
    }

    /**
     * checks value and adds 0 if  value < 10.
     *
     * @param value : a number to check.
     * @return number in string form with condition above.
     */
    public static String decFormat(int value) {
        if (value < 10) {
            return format("%02d", value);
        } else return Integer.toString(value);
    }


    /**
     * Used to form a date for report.
     *
     * @param dateArr : arr size of 3|6 [hh,mm,ss]|[YYYY,MM,dd,hh,mm,ss].
     * @return : string format: "YYYY-MM-DD hh:mm:ss"|"hh:mm:ss".
     */
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

    /**
     * Reading data in format of the following string:"\d\d, [\d]{11}, [\d]{14}, [\d]{14}, \d\d",
     * then adds [\d]{11} to numbers
     *
     * @param path             : path to file with data.
     * @param incomingDataList : list to be fulled with {@link CDR}'s.
     * @param numbers          : agent numbers list.
     *                         <p>
     *                         Sends error message in case of some file-reading error.
     *                         </p>
     */
    public static void getData(String path, List<String> incomingDataList, List<Long> numbers) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                incomingDataList.add(line);
                numbers.add(parseLong(line.substring(4, 15)));
            }
        } catch (IOException e) {
            System.err.println("Error from reading file: " + e.getMessage());
        }
    }

    /**
     * Printing reports for all numbers in incoming data.
     *
     * @param numbers        : list of numbers without copies.
     * @param cdrs           : {@link CDRs}  list of cdrs. On iteration gets {@link CDR} and puts it into the report file.
     * @param reportRootPath : root-path to generate.
     */
    public static void printAllReport(CDRs cdrs, List<Long> numbers, String reportRootPath) {
        for (Long num : numbers) {
            printReport(cdrs, num, reportRootPath);
        }
    }

    /**
     * Prints a report on the requested number
     *
     * @param cdrs           : {@link CDRs} list to report.
     * @param reportRootPath : root-path to generate.
     *
     *                       <p>
     *                       Writes a message if a number wasn't found.
     *                       </p>
     */
    public static void printReportByNumber(CDRs cdrs, String reportRootPath) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the phone number to generate a report\n");

        long number = parseLong(scanner.nextLine()); // 11-digits
        boolean flag = hasNumber(cdrs, number);
        if (flag) {
            printReport(cdrs, number, reportRootPath);
        } else {
            System.out.print("Number not found");
        }
    }

    /**
     * Generates a report in a task-given format.
     *
     * @param cdrs           : list to report.
     * @param num            : number to report from the list.
     * @param reportRootPath : root-path to generate.
     *
     *                       <p>
     *                       Sends message in case of error.
     *                       </p>
     */
    private static void printReport(CDRs cdrs, long num, String reportRootPath) {

        String report = reportRootPath + num + ".txt";
        CDRs filteredByNumber = cdrs.FilterByNumber(cdrs.cdrList, num);
        try {
            FileWriter writer = new FileWriter(report);
            writer.write("Tariff Index: " + filteredByNumber.cdrList.get(0).getTariff().getTariffType() + "\n");
            writer.write("----------------------------------------------------------------------------\n");
            writer.write("Report for phone number " + filteredByNumber.cdrList.get(0).getAgentNumber() + "\n");
            writer.write("----------------------------------------------------------------------------\n");
            writer.write("| Call Type |     Start Time      |       End Time      | Duration | Cost |\n");
            writer.write("----------------------------------------------------------------------------\n");
            for (CDR cdr : filteredByNumber.cdrList) {
                writer.write("|     " + cdr.getCallType() + "     | " + dateFormat(cdr.getCallStart()) + " | " + dateFormat(cdr.getCallEnd()) + " | " + dateFormat(cdr.getCallDuration()) + " |  " + "" + cdrs.getCallCost(cdr) + " |\n");
            }
            writer.write("----------------------------------------------------------------------------\n");
            writer.write("|                                           Total Cost: |" + "    " + filteredByNumber.agentBilling(filteredByNumber.cdrList) + " rubles   |\n");
            writer.write("----------------------------------------------------------------------------");

            writer.close();
            System.out.println("Successful!");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to file " + report);
            e.printStackTrace();
        }
    }

    /**
     * Checks whether number is in base or not.
     *
     * @param cdrs   : {@link CDRs} to check.
     * @param number : number to compare.
     * @return true if there's a number in a list of numbers, false otherwise.
     */
    public static boolean hasNumber(CDRs cdrs, long number) {
        for (int i = 0; i < cdrs.cdrList.size(); i++) {
            if (cdrs.cdrList.get(i).getAgentNumber() == number) {
                return true;
            }
        }
        return false;
    }
}
