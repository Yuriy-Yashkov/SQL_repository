package by.march8.entities.label;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by suvarov on 22.12.14.
 */
@Entity
@Table(name = "V_MODEL_TEST")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class VModel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @TableHeader(name = "ШИФР", width = 200, sequence = 0)
    @Column(name = "shifr")
    private String shifr;
    @TableHeader(name = "АРТИКУЛ", width = 200, sequence = 1)
    @Column(name = "articul")
    private String articul;
    @TableHeader(name="МОДЕЛЬ",width = 200,sequence = 2)
    @Column(name="model")
    private Integer model;
    @TableHeader(name="НАИМЕНОВАНИЕ",width = 200,sequence = 3)
    @Column(name="naimenovanie")
    private String naimenovanie;
    @TableHeader(name="БРЕНД",width = 200,sequence = 4)
    @Column(name="brend")
    private String brend;
    @TableHeader(name="razmer",width = 200,sequence = 5)
    @Column(name="razmer")
    private String razmer;
    @TableHeader(name="razmer1",width = 200,sequence = 6)
    @Column(name="razmer1")
    private String razmer1;
    @TableHeader(name="razmer2",width = 200,sequence = 7)
    @Column (name="raxmer2")
    private String raxmer2;
    @TableHeader(name="razmer3",width = 200,sequence = 8)
    @Column (name="razmer3")
    private String razmer3;
    @TableHeader(name="СОРТ",width = 200,sequence = 9)
    @Column (name="sort")
    private String sort;
    @TableHeader(name="ОПТОВАЯ ЦЕНА",width = 200,sequence =11)
    @Column (name="optovaya_cena")
    private Double optovaya_cena;
    @TableHeader(name="НДС",width = 200,sequence = 12)
    @Column (name="nds")
    private Integer nds;
    @Column (name="summa_nds")
    private String summaNDS;
    @Column(name="cena_s_nds")
    private Double cena_s_nds;
    @Column(name="preiskurant")
    private String preiskurant;
    @Column (name="data_preiskuranta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPreiskuranta;
    @Column (name="sostav_komplekta")
    private String sostavKomplekta;
    @Column (name="cvet")
    private String cvet;
    @TableHeader(name="EAN CODE",width = 200,sequence = 10)
    @Column (name="ean")
    private String ean;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShifr() {
        return shifr;
    }

    public void setShifr(String shifr) {
        this.shifr = shifr;
    }

    public String getArticul() {
        return articul;
    }

    public void setArticul(String articul) {
        this.articul = articul;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public String getNaimenovanie() {
        return naimenovanie;
    }

    public void setNaimenovanie(String naimenovanie) {
        this.naimenovanie = naimenovanie;
    }

    public String getBrend() {
        return brend;
    }

    public void setBrend(String brend) {
        this.brend = brend;
    }

    public String getRazmer() {
        return razmer;
    }

    public void setRazmer(String razmer) {
        this.razmer = razmer;
    }

    public String getRazmer1() {
        if (razmer1==null || razmer1.trim().isEmpty())
            return "";
        return razmer1;
    }

    public void setRazmer1(String razmer1) {
        this.razmer1 = razmer1;
    }

    public String getRaxmer2() {
        if (raxmer2==null || raxmer2.trim().isEmpty())
            return "";
        else
            return raxmer2;
    }

    public void setRaxmer2(String raxmer2) {
        this.raxmer2 = raxmer2;
    }

    public String getRazmer3() {
        if (razmer3==null || razmer3.trim().isEmpty())
            return "";
        else
            return razmer3;
    }

    public void setRazmer3(String razmer3) {
        this.razmer3 = razmer3;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Double getOptovaya_cena() {
        return optovaya_cena;
    }

    public void setOptovaya_cena(Double optovaya_cena) {
        this.optovaya_cena = optovaya_cena;
    }

    public Integer getNds() {
        return nds;
    }

    public void setNds(Integer nds) {
        this.nds = nds;
    }

    public String getSummaNDS() {
        return summaNDS;
    }

    public void setSummaNDS(String summaNDS) {
        this.summaNDS = summaNDS;
    }

    public Double getCena_s_nds() {
        return cena_s_nds;
    }

    public void setCena_s_nds(Double cena_s_nds) {
        this.cena_s_nds = cena_s_nds;
    }

    public String getPreiskurant() {
        return preiskurant;
    }

    public void setPreiskurant(String preiskurant) {
        this.preiskurant = preiskurant;
    }

    public Date getDataPreiskuranta() {
        return dataPreiskuranta;
    }

    public void setDataPreiskuranta(Date dataPreiskuranta) {
        this.dataPreiskuranta = dataPreiskuranta;
    }

    public String getSostavKomplekta() {
        return sostavKomplekta;
    }

    public void setSostavKomplekta(String sostavKomplekta) {
        this.sostavKomplekta = sostavKomplekta;
    }

    public String getCvet() {
        return cvet;
    }

    public void setCvet(String cvet) {
        this.cvet = cvet;
    }

    public String getEan() {
        if (ean==null || ean.isEmpty())
            return "";
        else
            return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String toString(){
        String ean="";
        if (this.ean==null || this.ean.isEmpty()) ean="EAN отсутствует";
        else ean=getEan();
        return ean+" "+getNaimenovanie();
    }
}
