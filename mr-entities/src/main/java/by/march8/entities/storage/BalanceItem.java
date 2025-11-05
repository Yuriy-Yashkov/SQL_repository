package by.march8.entities.storage;

public class BalanceItem {
    private int articleId;
    private String itemName;
    private int modelNumber;
    private String articleNumber;
    private int articleCode ;
    private int grade;
    private int size;
    private int growth;
    private double price ;
    private double priceGrade ;
    private int amount ;
    private double cost;

    private boolean accessTypeData  = false;
    private int minSize;
    private int maxSize;
    private int minGrowth;
    private int maxGrowth;

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public int getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(int articleCode) {
        this.articleCode = articleCode;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPriceGrade() {
        return priceGrade;
    }

    public void setPriceGrade(double priceGrade) {
        this.priceGrade = priceGrade;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public boolean isAccessTypeData() {
        return accessTypeData;
    }

    public void setAccessTypeData(boolean accessTypeData) {
        this.accessTypeData = accessTypeData;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMinGrowth(int minGrowth) {
        this.minGrowth = minGrowth;
    }

    public int getMinGrowth() {
        return minGrowth;
    }

    public void setMaxGrowth(int maxGrowth) {
        this.maxGrowth = maxGrowth;
    }

    public int getMaxGrowth() {
        return maxGrowth;
    }
}
