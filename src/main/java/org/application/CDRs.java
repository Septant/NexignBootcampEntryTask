package org.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.application.UtilFuncs.*;


/**
 * Class for array-like {@link CDR}.
 * Contains list of CDRs, total cost for a list, tarification timers.
 * Provided with getters and setters
 * */
public class CDRs
{
    List<CDR> cdrList;
    private double totalCost;
    private long currentTimer = 0;
    private long tariffTimer = 0;

    /**
     * Creates an empty copy of CDRs
     * */
    CDRs()
    {
        cdrList = new ArrayList<>();
    }

    /**
     * Creates a CDRs instance based on incoming data, sets default total cost for instance.
     * @param data : list of strings format "\d\d, [\d]{11}, [\d]{14}, [\d]{14}, \d\d".
     * */
    CDRs(String[] data)
    {
        cdrList = new ArrayList<>();

        for (int i = 0; i < data.length; i++)
        {
            this.totalCost = 0;
            this.cdrList.add(i, new CDR(data[i]));
        }

    }

    public long getCurrentTimer()
    {
        return currentTimer;
    }

    public void setCurrentTimer(long currentTimer)
    {
        this.currentTimer = currentTimer;
    }

    public long getTariffTimer()
    {
        return tariffTimer;
    }

    public void setTariffTimer(long tariffTimer)
    {
        this.tariffTimer = tariffTimer;
    }

    public double getTotalCost()
    {
        return totalCost;
    }

    public void setTotalCost(double totalCost)
    {
        this.totalCost = totalCost;
    }

    /**
     * Creates a filtered and sorted by call start list of CDRs for further actions.
     * @param cdrs   : a list to filter.
     * @param number : a filter.
     * @return       : new CDR with the same agent number.
     * */
    public CDRs FilterByNumber(List<CDR> cdrs, long number)
    {
        CDRs filtered = new CDRs();

        for (CDR cdr : cdrs)                            // iterates & adds cdr with specified number to filtered list
            if (cdr.getAgentNumber() == number)
                filtered.cdrList.add(cdr);

        sortByCallStart(filtered);

        return filtered;
    }

    /**
     * Sort function for CDRs list. Sorts by call start time.
     * @param cdrs : a list to sort.
     * */
    public void sortByCallStart(CDRs cdrs)
    {
        Comparator<CDR> comparator = Comparator.comparingLong(CDR::callStartToLong);
        cdrs.cdrList.sort(comparator);
    }

    /**
     * Calculates cost of the call based on tariff parameters.
     * @param cdr : a data record.
     * @return    : cost of the call (type: double).
     * */
    public double getCallCost(CDR cdr)
    {
        long secs = inSeconds(cdr.getCallDuration(), 0, 1, 2);

        long trfPeriod = cdr.getTariff().getPeriod() * 60L;
        String tariff = cdr.getTariff().getType();
        double defCost = cdr.getTariff().getDefCost();
        double trfCost = cdr.getTariff().getCost();
        String callType = cdr.getCallType();

        if (tariff.equals("03"))
        {
            setCurrentTimer(getCurrentTimer() + secs);                          // adds remains from previous call
            cdr.setCallCost(Math.floor(getCurrentTimer() / 60D) * defCost);     // to the current one and calculates
            setCurrentTimer(getCurrentTimer() % 60L);                           // cost of it.
        }

        if (tariff.equals("06"))
        {
            setCurrentTimer(getCurrentTimer() + secs);

            if (getTariffTimer() <= trfPeriod)
            {
                cdr.setCallCost(0);
                while (getTariffTimer() < trfPeriod & getCurrentTimer() > 0)    // calculations for tariff
                    tariffBilling(cdr, trfPeriod, tariff, trfCost);
            }

            else if (getTariffTimer() > trfPeriod)
            {
                cdr.setCallCost(Math.floor(getCurrentTimer() / 60D) * defCost); // calculations after the reaching
                setCurrentTimer(getCurrentTimer() % 60L);                       // tariff limit
            }
        }

        if (tariff.equals("11"))
        {
            if (callType.equals("02"))
                cdr.setCallCost(0);                                             // free incoming calls for this tariff

            if (getTariffTimer() <= trfPeriod && callType.equals("01"))
            {
                setCurrentTimer(getCurrentTimer() + secs);
                while (getTariffTimer() <= trfPeriod & getCurrentTimer() > 0)   // calculations for tariff
                    tariffBilling(cdr, trfPeriod, tariff, trfCost);
            }

            else if (getTariffTimer() > trfPeriod)
            {
                cdr.setCallCost(Math.floor(getCurrentTimer() / 60D) * defCost); // calculations after the reaching
                setCurrentTimer(getCurrentTimer() % 60L);                       // tariff limit
            }
        }
        return cdr.getCallCost();
    }

    /**
     *  Calculations according to the tariff privilege plan
     * @param cdr       : data record to analyze
     * @param tariff    : tariff type
     * @param trfCost   : cost of privelege minute
     * @param trfPeriod : tariff privelege time
     * */
    private void tariffBilling(CDR cdr, long trfPeriod, String tariff, double trfCost)
    {
        long trfLeft = trfPeriod - getCurrentTimer();

        if (getCurrentTimer() >= 60L && trfLeft >= 60L)
        {
            setTariffTimer(getTariffTimer() + 60L);                                 // uncounted call time &
            setCurrentTimer(getCurrentTimer() - 60L);                               // left tariff minutes > 1 minute

            if (tariff.equals("11"))
                cdr.setCallCost(trfCost);

        }

        else if (getCurrentTimer() < 60 && trfLeft >= getCurrentTimer())
        {
            setTariffTimer(getTariffTimer() + getCurrentTimer());                   // uncounted call time < 1 minute
            setCurrentTimer(0);                                                     // tariff minutes >= 1 minute
        }

        else if (getCurrentTimer() >= 60L && trfLeft < 60)
        {
            setTariffTimer(getCurrentTimer() - (getCurrentTimer() - trfLeft));      // tariff minutes are going to end
            setCurrentTimer(getCurrentTimer() - trfLeft);

            if (tariff.equals("11"))
                cdr.setCallCost(cdr.getCallCost() + trfCost);
        }
    }

    /**
     * calculates tariff period cost for a specified agent number
     * @param cdrs : agent number calls to bill
     * @return     : tariff period cost
     * */
    public double agentBilling(List<CDR> cdrs)
    {
        Tariff tariff =  cdrs.get(0).getTariff();
        String type = tariff.getType();
        double cost = tariff.getCost();

        for (CDR cdr : cdrs)
            setTotalCost(getTotalCost() + getCallCost(cdr));

        if (type.equals("06"))
        {
            setTotalCost(getTotalCost() + cost);                    // watch Tariff.java for more information
            return getTotalCost();
        }

        return getTotalCost();
    }
}
