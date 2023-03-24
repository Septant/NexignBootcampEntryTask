package org.application;

/**
 * This class shows agent's Tariff.
 * It gets tariff type from {@link CDR} and sets up relevant settings.
 * Fields getters are provided.
 * */
public class Tariff {
    private final String type;
    private final int period;
    private final double trfCost;
    private final double defCost;

    /**
     * A constructor for {@link Tariff} class.
     *
     * @param tariffType : a type of Tariff, accepts values "03", "06" "11"
     * */
    Tariff(String tariffType) {
        this.type = tariffType;

        if (this.type.equals("03")) {           // setup for tariff 03: All calls 1.5 rub per min.
            this.defCost = 1.5;
            this.period  = -1;
            this.trfCost = 0;
        }

        else if (this.type.equals("06")) {      // setup for tariff 06: 300 min. free calls for 100 rub,
            this.period  = 300;                 // then 1 rub per min.
            this.trfCost = 100;
            this.defCost = 1;
        }

        else if (this.type.equals("11")) {      // setup for tariff 11: Free incoming,
            this.period  = 100;                 // 100 min. of outgoing for 0.5 per minute,
            this.trfCost = 0.5;                 // then outgoing 1.5 rub per min.
            this.defCost = 1.5;
        }
        else {                                  // otherwise
            this.period  = -1;
            this.trfCost = -1;
            this.defCost = -1;
        }
    }

    public double getDefCost() {
        return defCost;
    }

    public double getCost() {
        return trfCost;
    }

    public int getPeriod() {
        return period;
    }

    public String getType() {
        return type;
    }
}
