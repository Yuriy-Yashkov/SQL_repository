package dept.sbit.model;

/**
 * Модель описывает торговыу надбавку по подтипам производимой продукции
 * (трикотаж/чулочно-насочный/взрослый/детский)
 * @autor Andy
 *
 * */

public class SubTypeProduct {
    private int allowanceKnitwearAdult;
    private int allowanceStockingAdult;

    private int allowanceKnitwearBaby;
    private int allowanceStockingBaby;

    public SubTypeProduct(SubTypeProduct sTP) {
        allowanceKnitwearAdult = sTP.getAllowanceKnitwearAdult();
        allowanceStockingAdult = sTP.getAllowanceStockingAdult();
        allowanceKnitwearBaby = sTP.getAllowanceKnitwearBaby();
        allowanceStockingBaby = sTP.getAllowanceStockingBaby();
    }

    public SubTypeProduct(int aKA, int aSA, int aKB, int aSB) {
        allowanceKnitwearAdult = aKA;
        allowanceStockingAdult = aSA;
        allowanceKnitwearBaby = aKB;
        allowanceStockingBaby = aSB;
    }

    public SubTypeProduct() {
    }

    public int getAllowanceKnitwearAdult() {
        return allowanceKnitwearAdult;
    }

    public void setAllowanceKnitwearAdult(int allowanceKnitwearAdult) {
        this.allowanceKnitwearAdult = allowanceKnitwearAdult;
    }

    public int getAllowanceStockingAdult() {
        return allowanceStockingAdult;
    }

    public void setAllowanceStockingAdult(int allowanceStockingAdult) {
        this.allowanceStockingAdult = allowanceStockingAdult;
    }

    public int getAllowanceKnitwearBaby() {
        return allowanceKnitwearBaby;
    }

    public void setAllowanceKnitwearBaby(int allowanceKnitwearBaby) {
        this.allowanceKnitwearBaby = allowanceKnitwearBaby;
    }

    public int getAllowanceStockingBaby() {
        return allowanceStockingBaby;
    }

    public void setAllowanceStockingBaby(int allowanceStockingBaby) {
        this.allowanceStockingBaby = allowanceStockingBaby;
    }

}
