package by.march8.ecs.application.modules.art.model;

/**
 * @author Andy 23.01.2017.
 */
public class DosNsiItem {
    private String name;
    private String articleName;
    private String articleCode;

    public DosNsiItem(String articleCode_, String name_, String articleName_) {
        articleCode = articleCode_;
        name = name_;
        articleName = articleName_;
    }

    public DosNsiItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(final String articleName) {
        this.articleName = articleName;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(final String articleCode) {
        this.articleCode = articleCode;
    }

    @Override
    public String toString() {
        return "NsiItem{" +
                "name='" + name + '\'' +
                ", articleName='" + articleName + '\'' +
                ", articleCode=" + articleCode +
                '}';
    }
}
