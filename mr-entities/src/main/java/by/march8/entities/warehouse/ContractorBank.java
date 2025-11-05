package by.march8.entities.warehouse;

/**
 * Структура класса БАНК КОНТРАГЕНТА
 *
 * @author Andy 30.05.2016.
 */
public class ContractorBank {


    /**
     * Наименование банка
     */
    String name;

    /**
     * Почтовый адрес
     */
    String address;

    /**
     * МФО номер
     */
    String codeMFO;
    /**
     * ОКПО номер
     */
    String codeOKPO;
    /**
     * УНН номер
     */
    String codeUNN;


    /**
     * Тип расчетного счета
     */
    String accountType;
    /**
     * Номер расчетного счета
     */
    String accountNumber;


    /**
     * Корреспондентский счет
     */
    String accountCorrespondentNumber;


    public String getName() {
        if (name != null) {
            return name.trim();
        } else {
            return "";
        }
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAddress() {
        if (address != null) {
            return address.trim();
        } else {
            return "";
        }
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getCodeMFO() {
        if (codeMFO != null) {
            return codeMFO.trim();
        } else {
            return "";
        }
    }

    public void setCodeMFO(final String codeMFO) {
        this.codeMFO = codeMFO;
    }

    public String getCodeOKPO() {
        if (codeOKPO != null) {
            return codeOKPO.trim();
        } else {
            return "";
        }
    }

    public void setCodeOKPO(final String codeOKPO) {
        this.codeOKPO = codeOKPO;
    }

    public String getCodeUNN() {
        if (codeUNN != null) {
            return codeUNN.trim();
        } else {
            return "";
        }
    }

    public void setCodeUNN(final String codeUNN) {
        this.codeUNN = codeUNN;
    }

    public String getAccountType() {
        if (accountType != null) {
            return accountType.trim();
        } else {
            return "";
        }
    }

    public void setAccountType(final String accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        if (accountNumber != null) {
            return accountNumber.trim();
        } else {
            return "";
        }
    }

    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountCorrespondentNumber() {
        if (accountCorrespondentNumber != null) {
            return accountCorrespondentNumber.trim();
        } else {
            return "";
        }
    }

    public void setAccountCorrespondentNumber(final String accountCorrespondentNumber) {
        this.accountCorrespondentNumber = accountCorrespondentNumber;
    }

    /**
     * Форматированнная строка реквизитов расчетного счета
     *
     * @return "Расч/счет: [Тип счета]: [Номер счета]"
     */
    public String getAccountFormat(){
        String result = "";
        if(!getAccountType().equals("")){
            result = "Расч/счет :"+accountType.trim();
        }
        if(!getAccountNumber().equals("")){
            result = result + " :"+accountNumber.trim();
        }
        return result ;
    }


    public String getCodeUNNFormat() {
        if (!getCodeUNN().equals("")) {
            return "УНН:" +codeUNN.trim();
        } else {
            return "";
        }
    }

    public String getAccountCorrespondentNumberFormat() {
        if (!getAccountCorrespondentNumber().equals("")) {
            return "К/С:" +accountCorrespondentNumber.trim();
        } else {
            return "";
        }
    }

    public String getCodeMFOFormat() {
        if (!getCodeMFO().equals("")) {
            return "МФО:" +codeMFO.trim();
        } else {
            return "";
        }
    }
}
