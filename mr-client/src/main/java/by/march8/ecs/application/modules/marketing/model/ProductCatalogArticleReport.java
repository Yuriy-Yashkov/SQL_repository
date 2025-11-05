package by.march8.ecs.application.modules.marketing.model;

import by.march8.entities.product.ProductionCatalogProductReport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductCatalogArticleReport implements Serializable {
    private int id;
    private int documentId;
    private int articleId;
    private int articleCode;
    private int productCode;
    private String itemName;
    private int modelNumber;
    private String articleNumber;
    private String articleCipher;
    private String category;
    private String image;
    private String colorsArticle;
    private String colorsProduct;
    private String materialComposition;
    private String brandName;
    private String priceRange;
    private String sizeRange;
    private int amount;

    private List<ProductionCatalogProductReport> data;

    public static ProductCatalogArticleReport getProductCatalogArticleReport(ProductionCatalogProductReport item) {
        ProductCatalogArticleReport result = new ProductCatalogArticleReport();
        result.setItemName(item.getItemName());
        result.setImage(item.getImage());
        result.setArticleNumber(item.getArticleNumber());
        result.setModelNumber(item.getModelNumber());
        result.setCategory(item.getCategory());
        result.setBrandName(item.getBrandName());
        result.setArticleCode(item.getArticleCode());
        result.setArticleCipher(item.getArticleCipher());
        result.setArticleId(item.getArticleId());
        result.setColorsArticle(item.getColorsArticle());
        result.setMaterialComposition(item.getMaterialComposition());
        result.setPriceRange(item.getPriceRange());
        result.setSizeRange(item.getSizeRange());
        return result;
    }

    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    public List<ProductionCatalogProductReport> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(List<ProductionCatalogProductReport> data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(int articleCode) {
        this.articleCode = articleCode;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public String getItemName() {
        if (itemName != null) {
            return itemName.trim();
        }
        return null;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getArticleNumber() {
        if (articleNumber != null) {
            return articleNumber.trim();
        }
        return null;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getArticleCipher() {
        return articleCipher;
    }

    public void setArticleCipher(String articleCipher) {
        this.articleCipher = articleCipher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        if (image != null) {
            return image.trim();
        }
        return null;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColorsArticle() {
        if (colorsArticle != null) {
            return colorsArticle.trim();
        }
        return null;
    }

    public void setColorsArticle(String colorsArticle) {
        this.colorsArticle = colorsArticle;
    }

    public String getColorsProduct() {
        return colorsProduct;
    }

    public void setColorsProduct(String colorsProduct) {
        this.colorsProduct = colorsProduct;
    }

    public String getMaterialComposition() {
        if (materialComposition != null) {
            return materialComposition.trim();
        }
        return null;
    }

    public void setMaterialComposition(String materialComposition) {
        this.materialComposition = materialComposition;
    }

    public String getBrandName() {
        if (brandName != null) {
            return brandName.trim();
        }
        return "-";
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPriceRange() {
        if (priceRange != null) {
            return priceRange.trim();
        }
        return "";
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getSizeRange() {
        if (sizeRange != null) {
            return sizeRange.trim();
        }
        return "";
    }

    public void setSizeRange(String sizeRange) {
        this.sizeRange = sizeRange;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
