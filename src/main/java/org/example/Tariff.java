package org.example;

public class Tariff {
    private final String tariffType;
    private int tariffPeriod;
    private double tariffCost;
    private double defaultCost;


    Tariff(int tariffType) {
        this.tariffType = Integer.toString(tariffType);
        if (this.tariffType.equals("3")) {
            this.defaultCost = 1.5;
            this.tariffPeriod = -1;
        } else if (this.tariffType.equals("6")) {
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
