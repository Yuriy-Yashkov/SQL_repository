/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

import javax.swing.*;
import java.util.Arrays;

/**
 * @author user
 */
@SuppressWarnings("all")
public class ValueCalculation {

    private String[] nsr;
    private double[] wsr;
    private double[] csr;
    private double sm;
    private double vm;
    private double te;
    private double ten;
    private double vms;
    private double snp;
    private double hzrp;
    private double kmrp;
    private double smtp;
    private double smts;
    private double smvp;
    private double smvs;
    private double vmtp;
    private double vmts;
    private double zpo;
    private double zpd;
    private double sn;
    private double prr;
    private double hzr;
    private double pss;
    private double kmr;
    private double cnr;
    private double cnr2;
    private double cnv;
    private double cnv2;
    private double cno;
    private double cno2;
    private String fas;
    private String nar;
    private String rzmn;
    private String dat;
    private String vid;
    private String grup;
    private String ptk;
    private double cc;
    private double cnvp;
    private String niz;
    private String niz1;
    private String niz2;
    private String obr;
    private String pol;
    private double prb;
    private double prbp;
    private double prrp;
    private String rzmk;
    private String syr1;
    private String syr2;
    private String tek;
    private double tes;
    private double vmk;
    private int id;
    private double usto;
    private double zp;
    private int nds;
    private int numberPrice;
    private int id_type_calculation;
    private String prim;

    private double percentCredit;       // PERCENT_CREDIT
    private double valueCredit;        // VALUE_CREDIT
    private double primeCostCredit;    // PRIMECOST_CREDIT
    private double profitCredit;       // PROFIT_CREDIT
    private double cnoCredit;           // CNO_CREDIT
    private double cno2Credit;          // CNO2_CREDIT
    private double cnrCredit;           // CNR_CREDIT
    private double cnr2Credit;          // CNR2_CREDIT
    private double cnvCredit;           // CNV_CREDIT
    private double cnv2Credit;          // CNV2_CREDIT


    /**
     * @return the nsr
     */
    public String[] getNsr() {
        return nsr;
    }

    /**
     * @param nsr the nsr to set
     */
    public void setNsr(String[] nsr) {
        this.nsr = nsr;
    }

    /**
     * @return the wsr
     */
    public double[] getWsr() {
        return wsr;
    }

    /**
     * @param wsr the wsr to set
     */
    public void setWsr(double[] wsr) {
        int i = 0;
        try {
            for (i = 0; i < wsr.length; i++) {
                double value = wsr[i];
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ошибка ввода кг[" + i + 1 + "]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.wsr = wsr;

    }

    /**
     * @return the csr
     */
    public double[] getCsr() {
        return csr;
    }

    /**
     * @param csr the csr to set
     */
    public void setCsr(double[] csr) {
        this.csr = csr;
    }

    /**
     * @return the sm
     */
    public double getSm() {
        return sm;
    }

    /**
     * @param sm the sm to set
     */
    public void setSm(double sm) {
        this.sm = sm;
    }

    /**
     * @return the vm
     */
    public double getVm() {
        return vm;
    }

    /**
     * @param vm the vm to set
     */
    public void setVm(double vm) {
        this.vm = vm;
    }

    /**
     * @return the te
     */
    public double getTe() {
        return te;
    }

    /**
     * @param te the te to set
     */
    public void setTe(double te) {
        this.te = te;
    }

    /**
     * @return the ten
     */
    public double getTen() {
        return ten;
    }

    /**
     * @param ten the ten to set
     */
    public void setTen(double ten) {
        this.ten = ten;
    }

    /**
     * @return the vms
     */
    public double getVms() {
        return vms;
    }

    /**
     * @param vms the vms to set
     */
    public void setVms(double vms) {
        this.vms = vms;
    }

    /**
     * @return the snp
     */
    public double getSnp() {
        return snp;
    }

    /**
     * @param snp the snp to set
     */
    public void setSnp(double snp) {
        this.snp = snp;
    }

    /**
     * @return the hzrp
     */
    public double getHzrp() {
        return hzrp;
    }

    /**
     * @param hzrp the hzrp to set
     */
    public void setHzrp(double hzrp) {
        this.hzrp = hzrp;
    }

    /**
     * @return the kmrp
     */
    public double getKmrp() {
        return kmrp;
    }

    /**
     * @param kmrp the kmrp to set
     */
    public void setKmrp(double kmrp) {
        this.kmrp = kmrp;
    }

    /**
     * @return the smtp
     */
    public double getSmtp() {
        return smtp;
    }

    /**
     * @param smtp the smtp to set
     */
    public void setSmtp(double smtp) {
        this.smtp = smtp;
    }

    /**
     * @return the smts
     */
    public double getSmts() {
        return smts;
    }

    /**
     * @param smts the smts to set
     */
    public void setSmts(double smts) {
        this.smts = smts;
    }

    /**
     * @return the smvp
     */
    public double getSmvp() {
        return smvp;
    }

    /**
     * @param smvp the smvp to set
     */
    public void setSmvp(double smvp) {
        this.smvp = smvp;
    }

    /**
     * @return the smvs
     */
    public double getSmvs() {
        return smvs;
    }

    /**
     * @param smvs the smvs to set
     */
    public void setSmvs(double smvs) {
        this.smvs = smvs;
    }

    /**
     * @return the vmtp
     */
    public double getVmtp() {
        return vmtp;
    }

    /**
     * @param vmtp the vmtp to set
     */
    public void setVmtp(double vmtp) {
        this.vmtp = vmtp;
    }

    /**
     * @return the vmts
     */
    public double getVmts() {
        return vmts;
    }

    /**
     * @param vmts the vmts to set
     */
    public void setVmts(double vmts) {
        this.vmts = vmts;
    }

    /**
     * @return the zpo
     */
    public double getZpo() {
        return zpo;
    }

    /**
     * @param zpo the zpo to set
     */
    public void setZpo(double zpo) {
        this.zpo = zpo;
    }

    /**
     * @return the zpd
     */
    public double getZpd() {
        return zpd;
    }

    /**
     * @param zpd the zpd to set
     */
    public void setZpd(double zpd) {
        this.zpd = zpd;
    }

    /**
     * @return the sn
     */
    public double getSn() {
        return sn;
    }

    /**
     * @param sn the sn to set
     */
    public void setSn(double sn) {
        this.sn = sn;
    }

    /**
     * @return the prr
     */
    public double getPrr() {
        return prr;
    }

    /**
     * @param prr the prr to set
     */
    public void setPrr(double prr) {
        this.prr = prr;
    }

    /**
     * @return the hzr
     */
    public double getHzr() {
        return hzr;
    }

    /**
     * @param hzr the hzr to set
     */
    public void setHzr(double hzr) {
        this.hzr = hzr;
    }

    /**
     * @return the pss
     */
    public double getPss() {
        return pss;
    }

    /**
     * @param pss the pss to set
     */
    public void setPss(double pss) {
        this.pss = pss;
    }

    /**
     * @return the kmr
     */
    public double getKmr() {
        return kmr;
    }

    /**
     * @param kmr the kmr to set
     */
    public void setKmr(double kmr) {
        this.kmr = kmr;
    }

    /**
     * @return the cnr
     */
    public double getCnr() {
        return cnr;
    }

    /**
     * @param cnr the cnr to set
     */
    public void setCnr(double cnr) {
        this.cnr = cnr;
    }

    /**
     * @return the cnr2
     */
    public double getCnr2() {
        return cnr2;
    }

    /**
     * @param cnr2 the cnr2 to set
     */
    public void setCnr2(double cnr2) {
        this.cnr2 = cnr2;
    }

    /**
     * @return the cnv
     */
    public double getCnv() {
        return cnv;
    }

    /**
     * @param cnv the cnv to set
     */
    public void setCnv(double cnv) {
        this.cnv = cnv;
    }

    /**
     * @return the cnv2
     */
    public double getCnv2() {
        return cnv2;
    }

    /**
     * @param cnv2 the cnv2 to set
     */
    public void setCnv2(double cnv2) {
        this.cnv2 = cnv2;
    }

    /**
     * @return the cno
     */
    public double getCno() {
        return cno;
    }

    /**
     * @param cno the cno to set
     */
    public void setCno(double cno) {
        this.cno = cno;
    }

    /**
     * @return the cno2
     */
    public double getCno2() {
        return cno2;
    }

    /**
     * @param cno2 the cno2 to set
     */
    public void setCno2(double cno2) {
        this.cno2 = cno2;
    }

    /**
     * @return the fas
     */
    public String getFas() {
        return fas;
    }

    /**
     * @param fas the fas to set
     */
    public void setFas(String fas) {
        this.fas = fas;
    }

    /**
     * @return the nar
     */
    public String getNar() {
        return nar;
    }

    /**
     * @param nar the nar to set
     */
    public void setNar(String nar) {
        this.nar = nar;
    }

    /**
     * @return the rzmn
     */
    public String getRzmn() {
        return rzmn;
    }

    /**
     * @param rzmn the rzmn to set
     */
    public void setRzmn(String rzmn) {
        this.rzmn = rzmn;
    }

    /**
     * @return the dat
     */
    public String getDat() {
        return dat;
    }

    /**
     * @param dat the dat to set
     */
    public void setDat(String dat) {
        this.dat = dat;
    }

    /**
     * @return the vid
     */
    public String getVid() {
        return vid;
    }

    /**
     * @param vid the vid to set
     */
    public void setVid(String vid) {
        this.vid = vid;
    }

    /**
     * @return the grup
     */
    public String getGrup() {
        return grup;
    }

    /**
     * @param grup the grup to set
     */
    public void setGrup(String grup) {
        this.grup = grup;
    }

    /**
     * @return the ptk
     */
    public String getPtk() {
        return ptk;
    }

    /**
     * @param ptk the ptk to set
     */
    public void setPtk(String ptk) {
        this.ptk = ptk;
    }

    /**
     * @return the cc
     */
    public double getCc() {
        return cc;
    }

    /**
     * @param cc the cc to set
     */
    public void setCc(double cc) {
        this.cc = cc;
    }

    /**
     * @return the cnvp
     */
    public double getCnvp() {
        return cnvp;
    }

    /**
     * @param cnvp the cnvp to set
     */
    public void setCnvp(double cnvp) {
        this.cnvp = cnvp;
    }

    /**
     * @return the niz
     */
    public String getNiz() {
        return niz;
    }

    /**
     * @param niz the niz to set
     */
    public void setNiz(String niz) {
        this.niz = niz;
    }

    /**
     * @return the niz1
     */
    public String getNiz1() {
        return niz1;
    }

    /**
     * @param niz1 the niz1 to set
     */
    public void setNiz1(String niz1) {
        this.niz1 = niz1;
    }

    /**
     * @return the niz2
     */
    public String getNiz2() {
        return niz2;
    }

    /**
     * @param niz2 the niz2 to set
     */
    public void setNiz2(String niz2) {
        this.niz2 = niz2;
    }

    /**
     * @return the obr
     */
    public String getObr() {
        return obr;
    }

    /**
     * @param obr the obr to set
     */
    public void setObr(String obr) {
        this.obr = obr;
    }

    /**
     * @return the pol
     */
    public String getPol() {
        return pol;
    }

    /**
     * @param pol the pol to set
     */
    public void setPol(String pol) {
        this.pol = pol;
    }

    /**
     * @return the prb
     */
    public double getPrb() {
        return prb;
    }

    /**
     * @param prb the prb to set
     */
    public void setPrb(double prb) {
        this.prb = prb;
    }

    /**
     * @return the prbp
     */
    public double getPrbp() {
        return prbp;
    }

    /**
     * @param prbp the prbp to set
     */
    public void setPrbp(double prbp) {
        this.prbp = prbp;
    }

    /**
     * @return the prrp
     */
    public double getPrrp() {
        return prrp;
    }

    /**
     * @param prrp the prrp to set
     */
    public void setPrrp(double prrp) {
        this.prrp = prrp;
    }

    /**
     * @return the rzmk
     */
    public String getRzmk() {
        return rzmk;
    }

    /**
     * @param rzmk the rzmk to set
     */
    public void setRzmk(String rzmk) {
        this.rzmk = rzmk;
    }

    /**
     * @return the syr1
     */
    public String getSyr1() {
        return syr1;
    }

    /**
     * @param syr1 the syr1 to set
     */
    public void setSyr1(String syr1) {
        this.syr1 = syr1;
    }

    /**
     * @return the syr2
     */
    public String getSyr2() {
        return syr2;
    }

    /**
     * @param syr2 the syr2 to set
     */
    public void setSyr2(String syr2) {
        this.syr2 = syr2;
    }

    /**
     * @return the tek
     */
    public String getTek() {
        return tek;
    }

    /**
     * @param tek the tek to set
     */
    public void setTek(String tek) {
        this.tek = tek;
    }

    /**
     * @return the tes
     */
    public double getTes() {
        return tes;
    }

    /**
     * @param tes the tes to set
     */
    public void setTes(double tes) {
        this.tes = tes;
    }

    /**
     * @return the vmk
     */
    public double getVmk() {
        return vmk;
    }

    /**
     * @param vmk the vmk to set
     */
    public void setVmk(double vmk) {
        this.vmk = vmk;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the usto
     */
    public double getUsto() {
        return usto;
    }

    /**
     * @param usto the usto to set
     */
    public void setUsto(double usto) {
        this.usto = usto;
    }

    /**
     * @return the zp
     */
    public double getZp() {
        return zp;
    }

    /**
     * @param zp the zp to set
     */
    public void setZp(double zp) {
        this.zp = zp;
    }

    /**
     * @return the nds
     */
    public int getNds() {
        return nds;
    }

    /**
     * @param nds the nds to set
     */
    public void setNds(int nds) {
        this.nds = nds;
    }

    /**
     * @return the numberPrice
     */
    public int getNumberPrice() {
        return numberPrice;
    }

    /**
     * @param numberPrice the numberPrice to set
     */
    public void setNumberPrice(int numberPrice) {
        this.numberPrice = numberPrice;
    }

    /**
     * @return the id_type_calculation
     */
    public int getId_type_calculation() {
        return id_type_calculation;
    }

    /**
     * @param id_type_calculation the id_type_calculation to set
     */
    public void setId_type_calculation(int id_type_calculation) {
        this.id_type_calculation = id_type_calculation;
    }

    /**
     * @return the prim
     */
    public String getPrim() {
        return prim;
    }

    /**
     * @param prim the prim to set
     */
    public void setPrim(String prim) {
        this.prim = prim;
    }

    @Override
    public String toString() {
        return "ValueCalculation{" +
                "nsr=" + Arrays.toString(nsr) +
                ", wsr=" + Arrays.toString(wsr) +
                ", csr=" + Arrays.toString(csr) +
                ", sm=" + sm +
                ", vm=" + vm +
                ", te=" + te +
                ", ten=" + ten +
                ", vms=" + vms +
                ", snp=" + snp +
                ", hzrp=" + hzrp +
                ", kmrp=" + kmrp +
                ", smtp=" + smtp +
                ", smts=" + smts +
                ", smvp=" + smvp +
                ", smvs=" + smvs +
                ", vmtp=" + vmtp +
                ", vmts=" + vmts +
                ", zpo=" + zpo +
                ", zpd=" + zpd +
                ", sn=" + sn +
                ", prr=" + prr +
                ", hzr=" + hzr +
                ", pss=" + pss +
                ", kmr=" + kmr +
                ", cnr=" + cnr +
                ", cnr2=" + cnr2 +
                ", cnv=" + cnv +
                ", cnv2=" + cnv2 +
                ", cno=" + cno +
                ", cno2=" + cno2 +
                ", fas='" + fas + '\'' +
                ", nar='" + nar + '\'' +
                ", rzmn='" + rzmn + '\'' +
                ", dat='" + dat + '\'' +
                ", vid='" + vid + '\'' +
                ", grup='" + grup + '\'' +
                ", ptk='" + ptk + '\'' +
                ", cc=" + cc +
                ", cnvp=" + cnvp +
                ", niz='" + niz + '\'' +
                ", niz1='" + niz1 + '\'' +
                ", niz2='" + niz2 + '\'' +
                ", obr='" + obr + '\'' +
                ", pol='" + pol + '\'' +
                ", prb=" + prb +
                ", prbp=" + prbp +
                ", prrp=" + prrp +
                ", rzmk='" + rzmk + '\'' +
                ", syr1='" + syr1 + '\'' +
                ", syr2='" + syr2 + '\'' +
                ", tek='" + tek + '\'' +
                ", tes=" + tes +
                ", vmk=" + vmk +
                ", id=" + id +
                ", usto=" + usto +
                ", zp=" + zp +
                ", nds=" + nds +
                ", numberPrice=" + numberPrice +
                ", id_type_calculation=" + id_type_calculation +
                ", prim='" + prim + '\'' +
                '}';
    }

    public double getPercentCredit() {
        return percentCredit;
    }

    public void setPercentCredit(double percentCredit) {
        this.percentCredit = percentCredit;
    }

    public double getValueCredit() {
        return valueCredit;
    }

    public void setValueCredit(double valueCredit) {
        this.valueCredit = valueCredit;
    }

    public double getPrimeCostCredit() {
        return primeCostCredit;
    }

    public void setPrimeCostCredit(double primeCostCredit) {
        this.primeCostCredit = primeCostCredit;
    }

    public double getProfitCredit() {
        return profitCredit;
    }

    public void setProfitCredit(double profitCredit) {
        this.profitCredit = profitCredit;
    }

    public double getCnoCredit() {
        return cnoCredit;
    }

    public void setCnoCredit(double cnoCredit) {
        this.cnoCredit = cnoCredit;
    }

    public double getCno2Credit() {
        return cno2Credit;
    }

    public void setCno2Credit(double cno2Credit) {
        this.cno2Credit = cno2Credit;
    }

    public double getCnrCredit() {
        return cnrCredit;
    }

    public void setCnrCredit(double cnrCredit) {
        this.cnrCredit = cnrCredit;
    }

    public double getCnr2Credit() {
        return cnr2Credit;
    }

    public void setCnr2Credit(double cnr2Credit) {
        this.cnr2Credit = cnr2Credit;
    }

    public double getCnvCredit() {
        return cnvCredit;
    }

    public void setCnvCredit(double cnvCredit) {
        this.cnvCredit = cnvCredit;
    }

    public double getCnv2Credit() {
        return cnv2Credit;
    }

    public void setCnv2Credit(double cnv2Credit) {
        this.cnv2Credit = cnv2Credit;
    }
}
