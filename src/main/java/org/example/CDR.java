package org.example;

import static java.lang.Byte.parseByte;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.String.format;


public class CDR {
    private final long agentNumber;
    private final byte callType;
    private final int[] callStart = new int[6];
    private final int[] callEnd = new int[6];
    private final int[] callDuration;
    private final Tariff tariff;
    private final long callStartLong;

    private double callCost;

    CDR(String entry) {
        String[] partial = entry.split(", ");
        this.callType = parseByte(partial[0]);
        this.agentNumber = parseLong(partial[1]);
        this.callStart[0] = parseInt(partial[2].substring(0, 4));
        this.callEnd[0] = parseInt(partial[3].substring(0, 4));
        for (int i = 0; i < 5; i++) {
            this.callStart[i + 1] = parseInt(partial[2].substring(4 + 2 * i, 6 + 2 * i));
            this.callEnd[i + 1] = parseInt(partial[3].substring(4 + 2 * i, 6 + 2 * i));
        }

        this.tariff = new Tariff(parseInt(partial[4]));
        this.callDuration = callDuration(callStart, callEnd);
        StringBuilder temp = new StringBuilder();
        for (int j : callStart) {
            temp.append(j);
        }
        this.callStartLong = Long.parseLong(temp.toString());
        this.callCost = 0;
    }

    public long getCallStartLong() {
        return callStartLong;
    }

    public int[] getCallDuration() {
        return callDuration;
    }

    public int[] getCallStart() {
        return callStart;
    }

    public byte getCallType() {
        return callType;
    }

    public long getAgentNumber() {
        return agentNumber;
    }

    public int[] getCallEnd() {
        return callEnd;
    }

    public Tariff getTariff() {
        return tariff;
    }

    private int[] callDuration(int[] start, int[] end) {
        int[] value = new int[3];
        long stInSec = 0, edInSec = 0;
        if (start[2] == end[2]) {
            stInSec = inSeconds(start, 3, 4, 5);
            edInSec = inSeconds(end, 3, 4, 5);
        } else if (start[2] != end[2]) {
            stInSec = inSeconds(start, 2, 3, 4, 5);
            edInSec = inSeconds(end, 2, 3, 4, 5);
        }

        long duration = edInSec - stInSec;
        value[0] = (int) Math.floor(duration / 3600);
        duration = duration - (long) value[0] * 3600;
        value[1] = (int) Math.floor(duration / 60);
        duration = duration - (long) value[1] * 60;
        value[2] = (int) duration;

        return value;
    }

    public long inSeconds(int[] arr, int h, int m, int s) {
        return arr[h] * 3600L + arr[m] * 60L + arr[s];
    }

    public long inSeconds(int[] arr, int d, int h, int m, int s) {
        return arr[d] * 24L * 3600L + arr[h] * 3600L + arr[m] * 60L + arr[s];
    }

    public double getCallCost() {
        return callCost;
    }

    public void setCallCost(double callCost) {
        this.callCost = callCost;
    }

    private String decFormat(int value) {
        if (value < 10) {
            return format("%02d", value);
        } else return Integer.toString(value);
    }

    public String dateFormat(int[] dateArr) {
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

}
