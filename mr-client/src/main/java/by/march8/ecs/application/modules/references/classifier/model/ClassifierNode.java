package by.march8.ecs.application.modules.references.classifier.model;

import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Andy 08.01.2019 - 10:45.
 */
public class ClassifierNode {

    private int id;
    private int parentId;
    private ClassifierNodeType type;
    private DefaultMutableTreeNode node;
    private int code;
    private String name;
    private String assortmentName;
    private ClassifierNode parentNode;

    public ClassifierNode() {

    }

    public ClassifierNode(ClassifierNode node) {
        this.id = node.getId();
        this.parentId = node.getParentId();
        this.name = node.getName();
        this.code = node.getCode();
        this.type = node.getType();
        this.node = node.getNode();
    }

    public static ClassifierNode getCustomNode(ClassifierNode node, ClassifierNodeType type) {
        ClassifierNode node_ = new ClassifierNode(node);
        node_.setType(type);
        if (type == ClassifierNodeType.GROUP) {
            node_.setCode(Integer.valueOf(String.valueOf(node.getCode()).substring(0, 2)));
            node_.setName(ClassifierTree.getNameAssortmentByArticleSegment(node_.getCode()));
        } else if (type == ClassifierNodeType.CATEGORY) {
            node_.setName(ClassifierTree.getNameAssortmentByArticleSegment(node_.getCode()));
        } else if (type == ClassifierNodeType.ASSORTMENT) {
            node_.setName(node.getAssortmentName());
            node_.setAssortmentName(node.getAssortmentName());
        } else if (type == ClassifierNodeType.MODEL) {
            node_.setName(node.getName());
            node_.setAssortmentName(node.getAssortmentName());
        }
        return node_;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public ClassifierNodeType getType() {
        return type;
    }

    public void setType(ClassifierNodeType type) {
        this.type = type;
    }

    public DefaultMutableTreeNode getNode() {
        return node;
    }

    public void setNode(DefaultMutableTreeNode node) {
        this.node = node;
    }

    public String getName() {
        if (name != null) {
            return name.trim();
        } else {
            return "";
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAssortmentName() {
        if (assortmentName != null) {
            return assortmentName.trim();
        } else {
            return "";
        }
    }

    public void setAssortmentName(String assortmentName) {
        this.assortmentName = assortmentName;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getNodeInformation() {
        return getName() + " - " + getAssortmentName() + " - " + getCode() + " - " + getType();
    }

    public ClassifierNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(ClassifierNode parentNode) {
        this.parentNode = parentNode;
    }
}
