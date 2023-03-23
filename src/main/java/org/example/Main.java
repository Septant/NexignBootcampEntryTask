package org.example;


import java.util.*;

import static java.lang.Byte.parseByte;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

class CDR {
    int agentNumber;
    byte callType;
    int[] callStart = new int[6];
    int[] callEnd = new int[6];
    int[] callDuration;
    Tariff tariff;
    long callStartLong;

    CDR(String entry) {
        String[] partial = entry.split(", ");
        this.callType = parseByte(partial[0]);
        this.agentNumber = parseInt(partial[1]);
        this.callStart[0] = parseInt(partial[2].substring(0, 3));
        this.callEnd[0] = parseInt(partial[3].substring(0, 3));
        partial[2] = partial[2].substring(4, partial.length - 1);
        partial[3] = partial[3].substring(4, partial.length - 1);
        for (int i = 0; i < 4; i++) {
            this.callStart[i] = parseInt(partial[2].substring(i * 2, (i * 2 + 1)));
            this.callEnd[i] = parseInt(partial[3].substring(i * 2, (i * 2 + 1)));
        }
        this.tariff = new Tariff(parseInt(partial[4]));
        // 02 тип звонка, 79876543221 номер, 2023 03 21 16 04 55 начало разговора, 20230321163211 конец, 11 тариф
        this.callDuration = callDuration(callStart, callEnd);
        this.callStartLong = parseLong(Arrays.toString(callStart));
    }

    public long getCallStartLong() {
        return callStartLong;
    }


    private int[] callDuration(int[] start, int[] end) {
        int[] value = new int[3];
        for (int i = 3; i < 5; i++)
            value[i - 3] = callEnd[i] - callStart[i];
        return value;
    }
}


class Tariff {
    int tariffType;
    public boolean isFreeIncoming;
    public boolean hasTariffPeriod;
    public int tariffPeriod;
    public double tariffPeriodCost;
    public double defaultCost;

    Tariff(int tariffType) {
        this.tariffType = tariffType;
        switch (tariffType) {
            case 03: {
                this.isFreeIncoming = false;
                this.hasTariffPeriod = false;
                this.defaultCost = 1.5;
            }
            case 06: {
                this.isFreeIncoming = false;
                this.hasTariffPeriod = true;
                this.tariffPeriod = 300;
                this.tariffPeriodCost = 100;
                this.defaultCost = 1;
            }
            case 11: {
                this.isFreeIncoming = true;
                this.hasTariffPeriod = true;
                this.tariffPeriod = 100;
                this.tariffPeriodCost = 0.5;
                this.defaultCost = 1.5;
            }
        }
    }
}

class CDRs {
    List<CDR> cdrs = new ArrayList<>();
    double callCost;
    double totalCost;

    CDRs(String[] data) {
        for (int i = 0; i < data.length; i++)
            this.cdrs.add(i, new CDR(data[i]));
    }

    public List<CDR> FilterByNumber(List<CDR> cdrs, int number) {
        List<CDR> filtered = new ArrayList<>();

        for (int i = 0; i < cdrs.size(); i++)
            if (cdrs.get(i).agentNumber == number)
                filtered.add(cdrs.get(i));
        sortByCallStart(filtered);
        return filtered;
    }

    public void sortByCallStart(List<CDR> cdrs) {
        Comparator<CDR> comparator = Comparator.comparingLong(CDR::getCallStartLong);
        cdrs.sort(comparator);
    }



}

public class Main {
    public static void main(String[] args) {

        String[] entryStr = new String[2];
        entryStr[0] = "02, 79876543221, 20230321160455, 20230321163211, 11";
        entryStr[1] = "02, 79876543221, 20230321160455, 20230321163211, 11";

        List<CDR> cdrs = new ArrayList<>();
        cdrs.add(0, new CDR(entryStr[0]));
        cdrs.add(1, new CDR(entryStr[1]));

        // ввод номера
        System.out.println("Hello world!");
    }
}