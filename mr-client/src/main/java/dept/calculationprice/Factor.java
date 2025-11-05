/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

/**
 * @author user
 */
public class Factor {
    private Double smtp;
    private Double smvp;
    private Double vmtp;
    private Double prrp;
    private Double hzrp;
    private Double kmrp;
    private Double cnvp;
    private String status;
    private String type;
    private int id;

    /**
     * @return the smtp
     */
    public Double getSmtp() {
        return smtp;
    }

    /**
     * @param smtp the smtp to set
     */
    public void setSmtp(Double smtp) {
        this.smtp = smtp;
    }

    /**
     * @return the smvp
     */
    public Double getSmvp() {
        return smvp;
    }

    /**
     * @param smvp the smvp to set
     */
    public void setSmvp(Double smvp) {
        this.smvp = smvp;
    }

    /**
     * @return the vmtp
     */
    public Double getVmtp() {
        return vmtp;
    }

    /**
     * @param vmtp the vmtp to set
     */
    public void setVmtp(Double vmtp) {
        this.vmtp = vmtp;
    }

    /**
     * @return the prrp
     */
    public Double getPrrp() {
        return prrp;
    }

    /**
     * @param prrp the prrp to set
     */
    public void setPrrp(Double prrp) {
        this.prrp = prrp;
    }

    /**
     * @return the hzrp
     */
    public Double getHzrp() {
        return hzrp;
    }

    /**
     * @param hzrp the hzrp to set
     */
    public void setHzrp(Double hzrp) {
        this.hzrp = hzrp;
    }

    /**
     * @return the kmrp
     */
    public Double getKmrp() {
        return kmrp;
    }

    /**
     * @param kmrp the kmrp to set
     */
    public void setKmrp(Double kmrp) {
        this.kmrp = kmrp;
    }

    /**
     * @return the cnvp
     */
    public Double getCnvp() {
        return cnvp;
    }

    /**
     * @param cnvp the cnvp to set
     */
    public void setCnvp(int cnvp) {
        if (cnvp == 0) {
            this.cnvp = (double) 10;
        }
        if (cnvp == 1) {
            this.cnvp = (double) 20;
        }
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
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


}
