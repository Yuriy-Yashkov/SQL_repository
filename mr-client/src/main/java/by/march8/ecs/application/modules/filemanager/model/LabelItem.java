package by.march8.ecs.application.modules.filemanager.model;

/**
 * @author Andy 10.01.2019 - 13:31.
 */
public class LabelItem {
    private String model;
    private boolean exist;

    public LabelItem(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    @Override
    public String toString() {
        if (model != null) {
            return model.trim();
        } else {
            return "";
        }
    }
}
