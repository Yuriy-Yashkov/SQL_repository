package by.march8.entities.label;


import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by suvarov on 30.01.15.
 */
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
@NamedStoredProcedureQuery(
        name = "modelByNar",
        resultClasses = VModelTestP.class,
        procedureName = "[dbo].[P_MODEL_TEST]",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name="nar", type=String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name="fas", type=Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name="sar", type=Integer.class)

        }
)
@Entity
public class VModelTestP extends BaseEntity implements Serializable{
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private double id;
    @Column(name="naim")
    @TableHeader(name="НАИМЕНОВАИЕ",width = 200,sequence = 0)
    private String naim;
    @Column(name="brend")
    @TableHeader(name="БРЕНД",width = 200,sequence = 1)
    private String brend;
    @Column(name="komplekt")
    @TableHeader(name="КОМПЛЕКТ",width = 200,sequence = 2)
    private String komplekt;
    @Column(name="nar")
    @TableHeader(name="АРТИКУЛ",width = 200,sequence = 3)
    private String nar;
    @Column(name="fas")
    @TableHeader(name="МОДЕЛЬ",width = 200,sequence = 4)
    private Integer fas;
    @Column(name="rzm")
    @TableHeader(name="РАЗМЕР",width = 200,sequence = 5)
    private String rzm;
    @Column(name="srt")
    @TableHeader(name="СОРТ",width = 200,sequence = 6)
    private Integer srt;
    @Column(name="poshiv")
    @TableHeader(name="ПОШИВ",width = 200,sequence = 7)
    private Integer poshiv;
    @Column(name="ncw")
    @TableHeader(name="ЦВЕТ",width = 200,sequence = 8)
    private String ncw;
    @Column(name="eancode")
    @TableHeader(name="EAN КОД",width = 200,sequence = 9)
    private String eancode;
    @Column(name="kod_marh")
    @TableHeader(name="КОД МАРШР. ЛИСТА",width = 200,sequence = 10)
    private int kod_marh;
    @Column(name="nomer_marh")
    @TableHeader(name="НОМЕР МАРШР. ЛИСТА",width = 200,sequence = 11)
    private double nomer_marh;
    @Column(name="rzm_marh")
    @TableHeader(name="rzm marh",width = 200,sequence = 12)
    private Integer rzm_marh;
    @Column(name="rst_marh")
    @TableHeader(name="rst marh",width = 200,sequence = 13)
    private Integer rst_marh;
    @Column(name="kod_izd")
    @TableHeader(name="КОД ИЗДЕЛИЯ",width = 200,sequence = 14)
    private int kod_izd;
    @Column(name="datevrkv")
    @TableHeader(name="datevrkv",width = 200,sequence = 15)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datevrkv;
    @Column(name="sar")
    @TableHeader(name="ШИФР АРТИКУЛА", width = 200,sequence = 16)
    private Integer sar;

    public Integer getSar() {
        return sar;
    }

    public void setSar(Integer sar) {
        this.sar = sar;
    }

    public String getNaimenovanie() {
        return naim;
    }

    public void setNaimenovanie(String naim) {
        this.naim = naim;
    }

    public String getBrend() {
        return brend;
    }

    public void setBrend(String brend) {
        this.brend = brend;
    }

    public String getSostavKomplekta() {
        return komplekt;
    }

    public void setSostavKomplekta(String komplekt) {
        this.komplekt = komplekt;
    }

    public String getArticul() {
        return nar;
    }

    public void setArticul(String nar) {
        this.nar = nar;
    }

    public Integer getModel() {
        return fas;
    }

    public void setModel(Integer fas) {
        this.fas = fas;
    }

    public String getRazmer() {
        return rzm;
    }

    public void setRazmer(String rzm) {
        this.rzm = rzm;
    }

    public Integer getSort() {
        return srt;
    }

    public void setSort(Integer srt) {
        this.srt = srt;
    }

    public Integer getPoshiv() {
        return poshiv;
    }

    public void setPoshiv(Integer poshiv) {
        this.poshiv = poshiv;
    }

    public String getNcw() {
        return ncw;
    }

    public void setNcw(String ncw) {
        this.ncw = ncw;
    }

    public String getEancode() {
        if (eancode==null || eancode.isEmpty())
            return "";
        else
            return eancode;
    }

    public void setEancode(String eancode) {
        this.eancode = eancode;
    }

    public int getKod_marh() {
        return kod_marh;
    }

    public void setKod_marh(int kod_marh) {
        this.kod_marh = kod_marh;
    }

    public double getNomer_marh() {
        return nomer_marh;
    }

    public void setNomer_marh(double nomer_marh) {
        this.nomer_marh = nomer_marh;
    }

    public Integer getRzmMarh() {
        return rzm_marh;
    }

    public void setRzmMarh(Integer rzm_marh) {
        this.rzm_marh = rzm_marh;
    }

    public Integer getRstMarh() {
        return rst_marh;
    }

    public void setRstMarh(Integer rst_marh) {
        this.rst_marh = rst_marh;
    }

    public int getKodIzd() {
        return kod_izd;
    }

    public void setKodIzd(int kod_izd) {
        this.kod_izd = kod_izd;
    }

    public Date getDatevrkv() {
        return datevrkv;
    }

    public void setDatevrkv(Date datevrkv) {
        this.datevrkv = datevrkv;
    }
    public String toString(){
        String ean="";
        if (this.eancode==null || this.eancode.isEmpty()) ean="EAN отсутствует";
        else ean=eancode;
        return ean+" "+getNaimenovanie();
    }

}
