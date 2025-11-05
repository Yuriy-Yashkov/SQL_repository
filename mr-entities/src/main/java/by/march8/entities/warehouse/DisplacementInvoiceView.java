package by.march8.entities.warehouse;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Сущность в виде гномика для накладных внутреннего перемещения
 */
@Entity
@Table(name = "v_vnperem1")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class DisplacementInvoiceView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name = "Дата док.", width = -80, sequence = 1)
    @Column(name="date")
    private Date date ;

    @TableHeader(name = "Номер док.", width = -80, sequence = 2)
    @Column(name = "ndoc")
    private String documentNumber;

    @TableHeader(name = "Операция", sequence = 3)
    @Column(name="operac")
    private String operationName;

    @TableHeader(name = "ОТПРАВИТЕЛЬ_Код", width = -50, sequence = 4)

    @Column(name = "kpodot")
    private int senderCode;

    @TableHeader(name = "ОТПРАВИТЕЛЬ_Наименование", sequence = 5)
    @Column(name = "naim")
    private String senderName;

    @TableHeader(name = "ПОЛУЧАТЕЛЬ_Код",width = -50, sequence = 6)
    @Column(name = "kpodto")
    private int recipientCode;

    @TableHeader(name = "ПОЛУЧАТЕЛЬ_Наименование", sequence = 7)
    @Column(name = "EXPR1")
    private String recipientName;

    @Column(name = "status")
    private int statusCode;

    @Column(name="kola")
    private int amountAll;

    @Column(name="kolk")
    private int amountPack;

    @Column(name="kolr")
    private int amountUnPack;

    @Column(name = "ucenka3s")
    private double reduction3Grade ;


    private String statusName ;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final String number) {
        this.documentNumber = number;
    }


    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(final String operationName) {
        this.operationName = operationName;
    }

    public int getSenderCode() {
        return senderCode;
    }

    public void setSenderCode(final int senderCode) {
        this.senderCode = senderCode;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(final String senderName) {
        this.senderName = senderName;
    }

    public int getRecipientCode() {
        return recipientCode;
    }

    public void setRecipientCode(final int recipientCode) {
        this.recipientCode = recipientCode;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(final String recipientName) {
        this.recipientName = recipientName;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }

    public int getAmountAll() {
        return amountAll;
    }

    public void setAmountAll(final int amountAll) {
        this.amountAll = amountAll;
    }

    public int getAmountPack() {
        return amountPack;
    }

    public void setAmountPack(final int amountPack) {
        this.amountPack = amountPack;
    }

    public int getAmountUnPack() {
        return amountUnPack;
    }

    public void setAmountUnPack(final int amountUnPack) {
        this.amountUnPack = amountUnPack;
    }

    @TableHeader(name = "Статус", sequence = 8)
    public String getStatusName() {
        if(statusCode==0){
            return "ЗАКРЫТ" ;
        }else if (statusCode==3){
            return "ФОРМИРОВАНИЕ" ;
        }else if (statusCode==2){
            return "ОТПРАВЛЕН" ;
        }else if (statusCode==1){
            return "УДАЛЕН" ;
        }
        return "НЕТ ДАННЫХ";
    }

    public void setStatusName(final String statusName) {
        this.statusName = statusName;
    }

    public double getReduction3Grade() {
        return reduction3Grade;
    }

    public void setReduction3Grade(final double reduction3Grade) {
        this.reduction3Grade = reduction3Grade;
    }
}
