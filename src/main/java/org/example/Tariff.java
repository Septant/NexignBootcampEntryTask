package org.example;

/**
 * This class is showing agent's Tariff.
 * It gets tariff type from {@link CDR} and sets up relevant settings.
 * Tariff options have getters to access
 *
 * */
public class Tariff {
    private final String tariffType;
    private int tariffPeriod;
    private double tariffCost;
    private double defaultCost;

    /**
     * A constructor for {@link Tariff} class.
     *
     * @param tariffType : a type of Tariff, accepts values "03", "06" "11"
     * */
    Tariff(String tariffType) {
        this.tariffType = tariffType;
        if (this.tariffType.equals("03")) {
            this.defaultCost = 1.5;
            this.tariffPeriod = -1;
        } else if (this.tariffType.equals("06")) {
            this.tariffPeriod = 300;
            this.tariffCost = 100;
            this.defaultCost = 1;
        } else if (this.tariffType.equals("11")) {
            this.tariffPeriod = 100;
            this.tariffCost = 0.5;
            this.defaultCost = 1.5;
        }
    }

    public double getDefaultCost() {
        return defaultCost;
    }

    public double getTariffCost() {
        return tariffCost;
    }

    public int getTariffPeriod() {
        return tariffPeriod;
    }

    public String getTariffType() {
        return tariffType;
    }
}
