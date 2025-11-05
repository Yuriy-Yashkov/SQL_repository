package dept.marketing.cena;

public class ProfitabilityItem {
    private double primeCostReference;
    private double profitabilityReference;

    private double primeCostCalc;
    private double profitabilityCalc;


    public double getPrimeCostReference() {
        return primeCostReference;
    }

    public void setPrimeCostReference(double primeCostReference) {
        this.primeCostReference = primeCostReference;
    }

    public double getProfitabilityReference() {
        return profitabilityReference;
    }

    public void setProfitabilityReference(double profitabilityReference) {
        this.profitabilityReference = profitabilityReference;
    }

    public double getPrimeCostCalc() {
        return primeCostCalc;
    }

    public void setPrimeCostCalc(double primeCostCalc) {
        this.primeCostCalc = primeCostCalc;
    }

    public double getProfitabilityCalc() {
        return profitabilityCalc;
    }

    public void setProfitabilityCalc(double profitabilityCalc) {
        this.profitabilityCalc = profitabilityCalc;
    }
}
