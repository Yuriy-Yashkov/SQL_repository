package by.march8.ecs.application.modules.references.classifier.model;

import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;

public class ProductCategoryItem {
    private String rawName;
    private String name;
    private int code;
    private String classifierGroup;
    private String classifierCategory;

    private String category;
    private String group;

    private String gender;
    private boolean isSet;
    private int setSize;

    private String finalName;

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        String sCode = String.valueOf(code);
        if (sCode.length() > 2) {
            String group = sCode.substring(0, 2);
            setClassifierGroup(ClassifierTree.getNameAssortmentByArticleSegment(Integer.valueOf(group)));
            setClassifierCategory(ClassifierTree.getNameAssortmentByArticleSegment(code));
        }
    }

    public String getClassifierGroup() {
        return classifierGroup;
    }

    public void setClassifierGroup(String classifierGroup) {
        this.classifierGroup = classifierGroup;
    }

    public String getClassifierCategory() {
        return classifierCategory;
    }

    public void setClassifierCategory(String classifierCategory) {
        this.classifierCategory = classifierCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public int getSetSize() {
        return setSize;
    }

    public void setSetSize(int setSize) {
        this.setSize = setSize;
    }

    public String getFinalName() {
        return finalName;
    }

    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
