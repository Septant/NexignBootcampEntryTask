package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class CDRs {
    List<CDR> cdrList;
    private double totalCost;
    private long currentTimer = 0;
    private long tariffTimer = 0;

    CDRs() {
        cdrList = new ArrayList<>();
    }

    CDRs(String[] data) {
        cdrList = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            this.totalCost = 0;
            this.cdrList.add(i, new CDR(data[i]));
        }
    }

    public CDRs FilterByNumber(List<CDR> cdrs, long number) {
        CDRs filtered = new CDRs();
        for (CDR cdr : cdrs)
            if (cdr.getAgentNumber() == number)
                filtered.cdrList.add(cdr);
        sortByCallStart(filtered);
        return filtered;
    }

    public void sortByCallStart(CDRs cdrs) {
        Comparator<CDR> comparator = Comparator.comparingLong(CDR::getCallStartLong);
        cdrs.cdrList.sort(comparator);
    }

    public double getCallCost(CDR cdr) {
        long secs = 0;
        secs = cdr.inSeconds(cdr.getCallDuration(), 0, 1, 2);

        long trfPeriod = cdr.getTariff().getTariffPeriod() * 60L;
        String tariff = cdr.getTariff().getTariffType();
        double defCost = cdr.getTariff().getDefaultCost();
        double trfCost = cdr.getTariff().getTariffCost();
        byte callType = cdr.getCallType();

        if (tariff.equals("3")) {
            setCurrentTimer(getCurrentTimer() + secs);
            cdr.setCallCost(Math.floor(getCurrentTimer() / 60D) * defCost);
            setCurrentTimer(getCurrentTimer() % 60L);
        }
        if (tariff.equals("6")) {
            setCurrentTimer(getCurrentTimer() + secs);
            if (getTariffTimer() <= trfPeriod) {
                cdr.setCallCost(0);
                while (getTariffTimer() < trfPeriod & getCurrentTimer() > 0)
                    callBilling(cdr, trfPeriod, tariff, defCost, trfCost);
            } else if (getTariffTimer() > trfPeriod) {
                cdr.setCallCost(Math.floor(getCurrentTimer() / 60D) * defCost);
                setCurrentTimer(getCurrentTimer() % 60L);
            }
        }
        if (tariff.equals("11")) {
            if (callType == 02) {
                cdr.setCallCost(0);
            }
            if (getTariffTimer() <= trfPeriod && callType == 01) {
                setCurrentTimer(getCurrentTimer() + secs);
                while (getTariffTimer() <= trfPeriod & getCurrentTimer() > 0)
                    callBilling(cdr, trfPeriod, tariff, defCost, trfCost);
            } else if (getTariffTimer() > trfPeriod) {
                cdr.setCallCost(Math.floor(getCurrentTimer() / 60D) * defCost);
                setCurrentTimer(getCurrentTimer() % 60L);
            }
        }
        return cdr.getCallCost();
    }

    private void callBilling(CDR cdr, long trfPeriod, String tariff, double defCost, double trfCost) {
        long temp = trfPeriod - getCurrentTimer();

        if (getCurrentTimer() >= 60L && temp >= 60L) {
            setTariffTimer(getTariffTimer() + 60L);
            setCurrentTimer(getCurrentTimer() - 60L);
            if (tariff.equals("11")) {
                cdr.setCallCost(trfCost);
            }
        } else if (getCurrentTimer() < 60 && temp >= getCurrentTimer()) {
            setTariffTimer(getTariffTimer() + getCurrentTimer());
            setCurrentTimer(0);
        } else if (getCurrentTimer() >= 60L && temp < 60) {
            setTariffTimer(getCurrentTimer() - (getCurrentTimer() - temp));
            setCurrentTimer(getCurrentTimer() - temp);
            if (tariff.equals("11")) {
                cdr.setCallCost(cdr.getCallCost() + trfCost);
            }

        }
    }


    public double agentBilling(List<CDR> cdrs) {
        for (CDR cdr : cdrs) {
            setTotalCost(getTotalCost() + getCallCost(cdr));
        }
        if (cdrs.get(0).getTariff().getTariffType().equals("6")) {
            setTotalCost(getTotalCost() + cdrs.get(0).getTariff().getTariffCost());
            return getTotalCost();
        }
        return getTotalCost();
    }

    public long getCurrentTimer() {
        return currentTimer;
    }

    public void setCurrentTimer(long currentTimer) {
        this.currentTimer = currentTimer;
    }

    public long getTariffTimer() {
        return tariffTimer;
    }

    public void setTariffTimer(long tariffTimer) {
        this.tariffTimer = tariffTimer;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
