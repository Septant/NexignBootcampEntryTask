package org.example;

public class Tariff {
    private final int tariffType;
    private int tariffPeriod;
    private double tariffCost;
    private double defaultCost;


    Tariff(int tariffType) {
        this.tariffType = tariffType;
        switch (tariffType) {
            case 03: {
                this.defaultCost = 1.5;
            }
            case 06: {
                this.tariffPeriod = 300;
                this.tariffCost = 100;
                this.defaultCost = 1;
            }
            case 11: {
                this.tariffPeriod = 100;
                this.tariffCost = 0.5;
                this.defaultCost = 1.5;
            }
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

    public int getTariffType() {
        return tariffType;
    }
}
