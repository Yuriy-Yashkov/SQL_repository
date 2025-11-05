package by.march8.entities.label;
//LabelOne


import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "LabelOne.findByPeriod",
                query = "SELECT item FROM LabelOne item WHERE item.data BETWEEN :periodBegin AND :periodEnd " +
                        "ORDER BY item.data")
})

/**
 * Created by suvarov on 18.12.14.
 */
@Entity
@Table(name = "label_one")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class LabelOne extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @TableHeader(name="BARCODE",width = 110,sequence = 9)
    @Column(name = "barcode")
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @TableHeader(name = "НАИМЕНОВАНИЕ", width = 200, sequence = 10)
    @Column(name = "naim")
    private String name;
    @TableHeader(name = "БРЕНД", width = 0, sequence = 20)
    @Column(name = "brend")
    private String brend;
    @Column(name = "komplekt")
    @TableHeader(name = "КОМПЛЕКТ", width = 0, sequence = 30)
    private String komplekt;
    @Column(name ="nar")
    @TableHeader(name="АРТИКУЛ", width = 80,sequence = 40)
    private String nar;
    @Column(name="fas")
    @TableHeader(name="МОДЕЛЬ", width=50,sequence = 50)
    private Integer model;
    @Column(name="rzm")
    @TableHeader(name="РАЗМЕР",width = 115,sequence = 60)
    private String razmer;

    @Column(name="srt")
    @TableHeader(name="СОРТ",width = 30,sequence = 70)
    private Integer sort;
    @Column(name="poshiv")
    @TableHeader(name="ПОШИВ",width = 0,sequence = 80)
    private Integer poshiv;
    @Column(name="printed")
    @TableHeader(name="ОТПЕЧАТАНО",width = 0,sequence = 90)
    private Integer printed;
    @Column(name="ncw")
    @TableHeader(name="ЦВЕТ",width = 80,sequence = 100)
    private String ncw;
    @Column(name="gost")
    @TableHeader(name="ГОСТ",width = 0,sequence = 110)
    private String standart;
    @Column(name="sostav1")
    @TableHeader(name="СОСТАВ 1",width = 0,sequence = 120)
    private String sostavOne;
    @Column(name="sostav2")
    @TableHeader(name="СОСТАВ 2",width = 0,sequence = 130)
    private String sostavTwo;
    @Column(name="sostav3")
    @TableHeader(name="СОСТАВ 3",width = 0,sequence = 140)
    private String sostavThree;
    @Column(name="sostav4")
    @TableHeader(name="СОСТАВ 4",width = 0,sequence = 150)
    private String sostavFour;
    @Column(name="textil")
    @TableHeader(name="ТЕКСТИЛЬ",width = 0,sequence = 160)
    private String textil;
    @Column(name="data")
    @Temporal(TemporalType.DATE)
    @TableHeader(name="ДАТА СОЗДАНИЯ",width = 50,sequence = 1)
    private Date data;
    @Column(name="eancode")
    @TableHeader(name="EANCODE",width = 100,sequence = 180)
    private String eancode;
    @Column(name="kod_marh")
    @TableHeader(name="КОД МАРШРУТНОГО ЛИСТА",width = 0,sequence = 190)
    private Integer kodMarh;
    @Column(name="nomer_marh")
    @TableHeader(name="НОМЕР МАРШРУТНОГО ЛИСТА",width = 50,sequence = 200)
    private double nomerMarh;
    @Column(name="rzm_marh")
    @TableHeader(name="rzm marh",width = 50,sequence = 210)
    private Integer rzmMarh;
    @Column(name="rst_marh")
    @TableHeader(name="rst marh",width = 50,sequence = 220)
    private Integer rstMarh;
    @Column(name="datevrkv")
    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name="datevrkv",width = 0,sequence = 230)
    private Date datevrkv;
    @Column(name="kod_izd")
    @TableHeader(name="КОД ИЗДЕЛИЯ",width = 0,sequence = 240)
    private Integer kodIzd;
    @Column(name="gost1")
    @TableHeader(name="gost1",width = 0,sequence = 250)
    private String standartOne;
    @Column(name="poshiv1")
    @TableHeader(name="ПОШИВ 1",width = 0,sequence = 260)
    private Integer poshivOne;
    @Column(name="kol_ed")
    @TableHeader(name="КОЛИЧЕСТВО ЕДИНИЦ",width = 0,sequence = 270)
    private Integer kolEd;
    @Column(name="kol_dop")
    @TableHeader(name="kol_dop",width = 0,sequence = 280)
    private Integer kolDop;
    @Column(name="fas0")
    @TableHeader(name="fas0",width = 0,sequence = 290)
    private String fasZero;
    @Column(name="del")
    @TableHeader(name="del",width = 0,sequence = 300)
    private Integer del;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrend() {
        return brend;
    }

    public void setBrend(String brend) {
        this.brend = brend;
    }

    public String getKomplekt() {
        return komplekt;
    }

    public void setKomplekt(String komplekt) {
        this.komplekt = komplekt;
    }

    public String getNar() {
        return nar;
    }

    public void setNar(String nar) {
        this.nar = nar;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public String getRazmer() {
        return razmer;
    }

    public void setRazmer(String razmer) {
        this.razmer = razmer;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getPoshiv() {
        return poshiv;
    }

    public void setPoshiv(Integer poshiv) {
        this.poshiv = poshiv;
    }

    public Integer getPrinted() {
        return printed;
    }

    public void setPrinted(Integer printed) {
        this.printed = printed;
    }

    public String getNcw() {
        return ncw;
    }

    public void setNcw(String ncw) {
        this.ncw = ncw;
    }

    public String getStandart() {
        return standart;
    }

    public void setStandart(String standart) {
        this.standart = standart;
    }

    public String getSostavOne() {
        return sostavOne;
    }

    public void setSostavOne(String sostavOne) {
        this.sostavOne = sostavOne;
    }

    public String getSostavTwo() {
        return sostavTwo;
    }

    public void setSostavTwo(String sostavTwo) {
        this.sostavTwo = sostavTwo;
    }

    public String getSostavThree() {
        return sostavThree;
    }

    public void setSostavThree(String sostavThree) {
        this.sostavThree = sostavThree;
    }

    public String getSostavFour() {
        return sostavFour;
    }

    public void setSostavFour(String sostavFour) {
        this.sostavFour = sostavFour;
    }

    public String getTextil() {
        if (textil!=null)
            return textil;
        else return "";
    }

    public void setTextil(String textil) {
        this.textil = textil;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getEancode() {
        return eancode;
    }

    public void setEancode(String eancode) {
        this.eancode = eancode;
    }

    public Integer getKodMarh() {
        return kodMarh;
    }

    public void setKodMarh(Integer kodMarh) {
        this.kodMarh = kodMarh;
    }

    public double getNomerMarh() {
        return nomerMarh;
    }

    public void setNomerMarh(double nomerMarh) {
        this.nomerMarh = nomerMarh;
    }

    public Integer getRzmMarh() {
        return rzmMarh;
    }

    public void setRzmMarh(Integer rzmMarh) {
        this.rzmMarh = rzmMarh;
    }

    public Integer getRstMarh() {
        return rstMarh;
    }

    public void setRstMarh(Integer rstMarh) {
        this.rstMarh = rstMarh;
    }

    public Date getDatevrkv() {
        return datevrkv;
    }

    public void setDatevrkv(Date datevrkv) {
        this.datevrkv = datevrkv;
    }

    public Integer getKodIzd() {
        return kodIzd;
    }

    public void setKodIzd(Integer kodIzd) {
        this.kodIzd = kodIzd;
    }

    public String getStandartOne() {
        return standartOne;
    }

    public void setStandartOne(String standartOne) {
        this.standartOne = standartOne;
    }

    public Integer getPoshivOne() {
        return poshivOne;
    }

    public void setPoshivOne(Integer poshivOne) {
        this.poshivOne = poshivOne;
    }

    public Integer getKolEd() {
        return kolEd;
    }

    public void setKolEd(Integer kolEd) {
        this.kolEd = kolEd;
    }

    public Integer getKolDop() {
        return kolDop;
    }

    public void setKolDop(Integer kolDop) {
        this.kolDop = kolDop;
    }

    public String getFasZero() {
        return fasZero;
    }

    public void setFasZero(String fasZero) {
        this.fasZero = fasZero;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }
    public String toString(){
        return getName();
    }
}
