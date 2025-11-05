/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

/**
 * @author user
 */
public final class MyBean {
    private String NIZ1;
    private String POL;
    private String name;
    private String article;
    private String model;
    private String size;
    private String costWithoutNds;
    private String betNds;
    private String costWithNds;
    private String prim;
    private double Cc;
    private int id;
    private boolean check;

    public MyBean(String name, String article, String model, String size, String costWithoutNds, String betNds, String costWithNds, String prim, String pol, String niz1, double Cc, int id) {
        this.setName(name);
        this.setArticle(article);
        this.setModel(model);
        this.setSize(size);
        this.setCostWithoutNds(costWithoutNds);
        this.setBetNds(betNds);
        this.setCostWithNds(costWithNds);
        this.setPrim(prim);
        this.setId(id);
        this.setNIZ1(niz1);
        this.setPOL(pol);
        this.setCc(Cc);

    }

    public MyBean(String name, String article, String model, String size, String costWithoutNds, String betNds, String costWithNds, String prim, int id) {
        this.setName(name);
        this.setArticle(article);
        this.setModel(model);
        this.setSize(size);
        this.setCostWithoutNds(costWithoutNds);
        this.setBetNds(betNds);
        this.setCostWithNds(costWithNds);
        this.setPrim(prim);
        this.setId(id);


    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the article
     */
    public String getArticle() {
        return article;
    }

    /**
     * @param article the article to set
     */
    public void setArticle(String article) {
        this.article = article;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @return the costWithoutNds
     */
    public String getCostWithoutNds() {
        return costWithoutNds;
    }

    /**
     * @param costWithoutNds the costWithoutNds to set
     */
    public void setCostWithoutNds(String costWithoutNds) {
        this.costWithoutNds = costWithoutNds;
    }

    /**
     * @return the betNds
     */
    public String getBetNds() {
        return betNds;
    }

    /**
     * @param betNds the betNds to set
     */
    public void setBetNds(String betNds) {
        this.betNds = betNds;
    }

    /**
     * @return the costWithNds
     */
    public String getCostWithNds() {
        return costWithNds;
    }

    /**
     * @param costWithNds the costWithNds to set
     */
    public void setCostWithNds(String costWithNds) {
        this.costWithNds = costWithNds;
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
     * @return the check
     */
    public boolean isCheck() {
        return check;
    }

    /**
     * @param check the check to set
     */
    public void setCheck(boolean check) {
        this.check = check;
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

    /**
     * @return the NIZ1
     */
    public String getNIZ1() {
        return NIZ1;
    }

    /**
     * @param NIZ1 the NIZ1 to set
     */
    public void setNIZ1(String NIZ1) {
        this.NIZ1 = NIZ1;
    }

    /**
     * @return the POL
     */
    public String getPOL() {
        return POL;
    }

    /**
     * @param POL the POL to set
     */
    public void setPOL(String POL) {
        this.POL = POL;
    }

    /**
     * @return the Cc
     */
    public double getCc() {
        return Cc;
    }

    /**
     * @param Cc the Cc to set
     */
    public void setCc(double Cc) {
        this.Cc = Cc;
    }


}

