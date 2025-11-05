package dept.tools.imgmanager;

public class ItemModel {
    private String fullName;
    private String name;
    private int submodel;
    private int sketch;
    private int imageNumber;

    public ItemModel(String fullName) {
        this.setFullName(fullName.trim());
    }

    @Override
    public String toString() {
        return "<" + fullName + ">: MODEL NUMBER " + name + ">: IMAGE " + imageNumber + " : SUBMODEL " + submodel + " : SKETCH " + sketch;
    }

    public ItemModel getItemModel() {
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubmodel() {
        return submodel;
    }

    public void setSubmodel(int submodel) {
        this.submodel = submodel;
    }

    public int getSketch() {
        return sketch;
    }

    public void setSketch(int sketch) {
        this.sketch = sketch;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(int imageNumber) {
        this.imageNumber = imageNumber;
    }


}
