package org.example;

import java.util.Arrays;

import static java.lang.Byte.parseByte;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class CDR {
    private final int agentNumber;
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

    public int getAgentNumber() {
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
        for (int i = 3; i < 5; i++)
            value[i - 3] = callEnd[i] - callStart[i];
        return value;
    }

    public double getCallCost() {
        return callCost;
    }

    public void setCallCost(double callCost) {
        this.callCost = callCost;
    }

}
