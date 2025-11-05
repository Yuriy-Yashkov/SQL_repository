package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;

import javax.persistence.*;
import java.util.Date;

/**
 * Уцененное издели, со ссылкой на прейскурант уценки
 *
 * @author Andy 06.09.2017.
 */
@Entity
@Table(name = "V_REMAINS_PRICELIST_SIMPLE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class RemainsReductionItem extends BaseEntity{
    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "REF_REMAINS_PRICELIST")
    private int priceListId;

    @Column(name = "ITEM_ARTICLE_NUMBER")
    private String articleNumber;

    @Column(name = "ITEM_SIZE")
    private String sizeRange;

    @Column(name = "TRADE_ALLOWANCE_VALUE")
    private int tradeMarkupValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DOCUMENT_DATE")
    private Date documentDate;

    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber ;


    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getPriceListId() {
        return priceListId;
    }

    public void setPriceListId(final int priceListId) {
        this.priceListId = priceListId;
    }

    public String getArticleNumber() {
        if (articleNumber != null) {
            return articleNumber.trim();
        } else {
            return "";
        }
    }

    public void setArticleNumber(final String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getSizeRange() {
        if (sizeRange != null) {
            return sizeRange.trim();
        } else {
            return "";
        }
    }

    public void setSizeRange(final String sizeRange) {
        this.sizeRange = sizeRange;
    }

    /**
     * Возвращает минимальный размер по диапаазону размеров sizeRange
     *
     * @return минимальный размер изделия
     */
    public int getMinimumSize() {
        String[] pars = sizeRange.split("-");
        if (pars[0].trim().equals(""))
            pars = sizeRange.split("х");
        //System.out.println(Integer.valueOf(pars[0].trim()));
        try {
            return Integer.valueOf(pars[0].trim());
        }catch (Exception e){
            e.printStackTrace();
            return 0 ;
        }
    }

    /**
     * Возвращает максимальный размер по диапаазону размеров sizeRange
     *
     * @return максимальный размер изделия
     */
    public int getMaximumSize() {
        String[] pars = sizeRange.split("-");
        if (pars.equals(""))
            pars = sizeRange.split("x");
        if(pars.length>1) {
            //System.out.println(Integer.valueOf(pars[1].trim()));
            return Integer.valueOf(pars[1].trim());
        }else{
            return 999 ;
        }
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(final Date documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentNumber() {
        if(documentNumber!=null){
            return documentNumber.trim();
        }else{
            return "" ;
        }
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public int getTradeMarkupValue() {
        return tradeMarkupValue;
    }

    public void setTradeMarkupValue(final int tradeMarkupValue) {
        this.tradeMarkupValue = tradeMarkupValue;
    }
}
