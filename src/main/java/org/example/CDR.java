package org.example;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static org.example.UtilFuncs.*;

public class CDR {
    private final long agentNumber;
    private final String callType;
    private final int[] callStart = new int[6];
    private final int[] callEnd = new int[6];
    private final int[] callDuration;
    private final Tariff tariff;

    private double callCost;

    CDR(String entry) {
        String[] partial = entry.split(", ");
        this.callType = partial[0];
        this.agentNumber = parseLong(partial[1]);
        this.callStart[0] = parseInt(partial[2].substring(0, 4));
        this.callEnd[0] = parseInt(partial[3].substring(0, 4));
        for (int i = 0; i < 5; i++) {
            this.callStart[i + 1] = parseInt(partial[2].substring(4 + 2 * i, 6 + 2 * i));
            this.callEnd[i + 1] = parseInt(partial[3].substring(4 + 2 * i, 6 + 2 * i));
        }

        this.tariff = new Tariff(partial[4]);
        this.callDuration = callDuration(callStart, callEnd);
        this.callCost = 0;
    }

    public long callStartToLong() {
        StringBuilder temp = new StringBuilder();
        for (int j : callStart) {
            temp.append(j);
        }
        return Long.parseLong(temp.toString());
    }

    public int[] getCallDuration() {
        return callDuration;
    }

    public int[] getCallStart() {
        return callStart;
    }

    public String getCallType() {
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
            stInSec =  inSeconds(start, 3, 4, 5);
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

    public double getCallCost() {
        return callCost;
    }

    public void setCallCost(double callCost) {
        this.callCost = callCost;
    }





}
