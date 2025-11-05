package dept.production.zsh.zplata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Vector;

/**
 *
 * @author lidashka
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemBuh {
    private String tabelNum;
    private String fio;
    private String prof;
    private int razryad;
    private Vector dataTabel;
    private double vyrBrig;
    private double vyrItog;
    private double procent;
    private double stoim;
    private double clVech;
    private int dNoch;  //10
    private double clNoch;
    private int dOtrab;
    private double clOtrab;
    private double vyrX2;
    private int dVnedr;
    private double clVnedr;
    private int dTarif;
    private double clTarif;
    private int dSredn;
    private double clSredn; //10   
    private double vrednost;
    private double stoimPlus;
    private double stoimMinus;
    private double kofUch;
    private double vyrDK;
    private double nesort;
    private int dTarif23;
    private double clTarif23;
    private int dOtpusk;
    private int dBList;     //10
    private int dPerehod;
    private int dSOtpusk;
    private int dDMateri;
    private int dMk;
    private double vyrOsvoen;
    private double percent;
    private double minusPercent;

    public String getTabelNum() {
        return tabelNum;
    }

    public void setTabelNum(String tabelNum) {
        this.tabelNum = tabelNum;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public int getRazryad() {
        return razryad;
    }

    public void setRazryad(int razryad) {
        this.razryad = razryad;
    }

    public Vector getDataTabel() {
        return dataTabel;
    }

    public void setDataTabel(Vector dataTabel) {
        this.dataTabel = dataTabel;
    }

    public double getVyrBrig() {
        return vyrBrig;
    }

    public void setVyrBrig(double vyrBrig) {
        this.vyrBrig = vyrBrig;
    }

    public double getVyrItog() {
        return vyrItog;
    }

    public void setVyrItog(double vyrItog) {
        this.vyrItog = vyrItog;
    }

    public double getProcent() {
        return procent;
    }

    public void setProcent(double procent) {
        this.procent = procent;
    }

    public double getStoim() {
        return stoim;
    }

    public void setStoim(double stoim) {
        this.stoim = stoim;
    }

    public double getClVech() {
        return clVech;
    }

    public void setClVech(double clVech) {
        this.clVech = clVech;
    }

    public int getdNoch() {
        return dNoch;
    }

    public void setdNoch(int dNoch) {
        this.dNoch = dNoch;
    }

    public double getClNoch() {
        return clNoch;
    }

    public void setClNoch(double clNoch) {
        this.clNoch = clNoch;
    }

    public int getdOtrab() {
        return dOtrab;
    }

    public void setdOtrab(int dOtrab) {
        this.dOtrab = dOtrab;
    }

    public double getClOtrab() {
        return clOtrab;
    }

    public void setClOtrab(double clOtrab) {
        this.clOtrab = clOtrab;
    }

    public double getVyrX2() {
        return vyrX2;
    }

    public void setVyrX2(double vyrX2) {
        this.vyrX2 = vyrX2;
    }

    public int getdVnedr() {
        return dVnedr;
    }

    public void setdVnedr(int dVnedr) {
        this.dVnedr = dVnedr;
    }

    public double getClVnedr() {
        return clVnedr;
    }

    public void setClVnedr(double clVnedr) {
        this.clVnedr = clVnedr;
    }

    public int getdTarif() {
        return dTarif;
    }

    public void setdTarif(int dTarif) {
        this.dTarif = dTarif;
    }

    public double getClTarif() {
        return clTarif;
    }

    public void setClTarif(double clTarif) {
        this.clTarif = clTarif;
    }

    public int getdSredn() {
        return dSredn;
    }

    public void setdSredn(int dSredn) {
        this.dSredn = dSredn;
    }

    public double getClSredn() {
        return clSredn;
    }

    public void setClSredn(double clSredn) {
        this.clSredn = clSredn;
    }

    public double getVrednost() {
        return vrednost;
    }

    public void setVrednost(double vrednost) {
        this.vrednost = vrednost;
    }

    public double getStoimPlus() {
        return stoimPlus;
    }

    public void setStoimPlus(double stoimPlus) {
        this.stoimPlus = stoimPlus;
    }

    public double getStoimMinus() {
        return stoimMinus * (-1);
    }

    public void setStoimMinus(double stoimMinus) {
        this.stoimMinus = stoimMinus;
    }

    public double getKofUch() {
        return kofUch;
    }

    public void setKofUch(double kofUch) {
        this.kofUch = kofUch;
    }

    public double getVyrDK() {
        return vyrDK;
    }

    public void setVyrDK(double vyrDK) {
        this.vyrDK = vyrDK;
    }

    public double getNesort() {
        return nesort;
    }

    public void setNesort(double nesort) {
        this.nesort = nesort;
    }

    public int getdTarif23() {
        return dTarif23;
    }

    public void setdTarif23(int dTarif23) {
        this.dTarif23 = dTarif23;
    }

    public double getClTarif23() {
        return clTarif23;
    }

    public void setClTarif23(double clTarif23) {
        this.clTarif23 = clTarif23;
    }

    public int getdOtpusk() {
        return dOtpusk;
    }

    public void setdOtpusk(int dOtpusk) {
        this.dOtpusk = dOtpusk;
    }

    public int getdBList() {
        return dBList;
    }

    public void setdBList(int dBList) {
        this.dBList = dBList;
    }

    public int getdPerevod() {
        return dPerehod;
    }

    public int getdSOtpusk() {
        return dSOtpusk;
    }

    public void setdSOtpusk(int dSOtpusk) {
        this.dSOtpusk = dSOtpusk;
    }

    public int getdDMateri() {
        return dDMateri;
    }

    public void setdDMateri(int dDMateri) {
        this.dDMateri = dDMateri;
    }

    public int getdMk() {
        return dMk;
    }

    public void setdMk(int dMk) {
        this.dMk = dMk;
    }

    public double getVyrOsvoen() {
        return vyrOsvoen;
    }

    public void setVyrOsvoen(double vyrOsvoen) {
        this.vyrOsvoen = vyrOsvoen;
    }

    public void setdPerehod(int dPerehod) {
        this.dPerehod = dPerehod;
    }
}
