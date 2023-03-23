package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class CDRs {
    List<CDR> cdrs = new ArrayList<>();
    private double totalCost = 0;
    private long defaultTimer;
    private long tariffTimer;

    CDRs(String[] data) {
        for (int i = 0; i < data.length; i++)
            this.cdrs.add(i, new CDR(data[i]));
    }

    public List<CDR> FilterByNumber(List<CDR> cdrs, int number) {
        List<CDR> filtered = new ArrayList<>();

        for (CDR cdr : cdrs)
            if (cdr.getAgentNumber() == number)
                filtered.add(cdr);
        sortByCallStart(filtered);
        return filtered;
    }

    public void sortByCallStart(List<CDR> cdrs) {
        Comparator<CDR> comparator = Comparator.comparingLong(CDR::getCallStartLong);
        cdrs.sort(comparator);
    }

    public double getCallCost(CDR cdr) {
        long secs = 0;
        secs = cdr.getCallDuration()[0] * 3600L + cdr.getCallDuration()[1] * 60L + (cdr.getCallDuration()[2]);
        setDefaultTimer(getDefaultTimer() + secs);

        long trfPeriod = cdr.getTariff().getTariffPeriod();
        int tariff = cdr.getTariff().getTariffType();
        double defCost = cdr.getTariff().getDefaultCost();
        double trfCost = cdr.getTariff().getTariffPeriodCost();
        byte callType = cdr.getCallType();

        if (tariff == 03) {
            cdr.setCallCost(Math.floor(getDefaultTimer() / 60D) * defCost);
            setDefaultTimer(getDefaultTimer() % 60L);
        }
        if (tariff == 06) {
            if (getTariffTimer() <= trfPeriod * 60L) {
                cdr.setCallCost(0);
                while (getTariffTimer() < trfPeriod & getDefaultTimer() > 0) {
                    long temp = trfPeriod - getDefaultTimer();

                    if (getDefaultTimer() >= 60L & temp >= 60L) {
                        setTariffTimer(getTariffTimer() + 60L);
                        setDefaultTimer(getDefaultTimer() - 60);
                        if (getTariffTimer() == getTariffTimer())
                            cdr.setCallCost(cdr.getCallCost() + trfCost);
                    } else tarificationBounds(cdr, defCost, trfCost, temp);
                }
            } else if (getTariffTimer() > trfPeriod * 60L) {
                cdr.setCallCost(Math.floor(getDefaultTimer() / 60D) * defCost);
                setDefaultTimer(getDefaultTimer() % 60L);
            }
        }
        if (tariff == 11) {
            if (callType == 02) {
                cdr.setCallCost(0);
            }
            if (getTariffTimer() <= trfPeriod * 60L && callType == 01) {
                setTariffTimer(getTariffTimer() + secs);
                while (getTariffTimer() <= trfPeriod & getDefaultTimer() > 0) {
                    long temp = trfPeriod - getDefaultTimer();
                    if (getDefaultTimer() >= 60L & temp >= 60L) {
                        setTariffTimer(getTariffTimer() + 60L);
                        setDefaultTimer(getDefaultTimer() - 60);
                        cdr.setCallCost(cdr.getCallCost() + trfCost);

                    } else {
                        tarificationBounds(cdr, defCost, trfCost, temp);
                    }
                }
            } else if (getTariffTimer() > trfPeriod * 60L){
                    cdr.setCallCost(Math.floor(getDefaultTimer() / 60D) * defCost);
                    setDefaultTimer(getDefaultTimer() % 60L);
            }
        }

        return cdr.getCallCost();
    }

    private void tarificationBounds(CDR cdr, double defCost, double trfCost, long temp) {
        if (getDefaultTimer() < 60 & temp >= getDefaultTimer()) {
            setTariffTimer(getTariffTimer() + getDefaultTimer());
            setDefaultTimer(0);
        } else if (getDefaultTimer() >= 60L & temp < 60) {
            setTariffTimer(getDefaultTimer() - (getDefaultTimer() - temp));
            setDefaultTimer(getDefaultTimer() - temp);
            cdr.setCallCost(cdr.getCallCost() + defCost + trfCost);
        }
    }


    public long getDefaultTimer() {
        return defaultTimer;
    }

    public void setDefaultTimer(long defaultTimer) {
        this.defaultTimer = defaultTimer;
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
