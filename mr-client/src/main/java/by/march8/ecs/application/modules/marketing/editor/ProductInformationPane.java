package by.march8.ecs.application.modules.marketing.editor;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andy 23.01.2019 - 9:05.
 */
public class ProductInformationPane extends JPanel {

    private JLabel lblItemValue;
    private JLabel lblArticleValue;
    private JLabel lblModelValue;
    private JLabel lblSizeValue;
    private JLabel lblGradeValue;


    private JLabel lblItem;
    private JLabel lblArticle;
    private JLabel lblModel;

    private JLabel lblSize;
    private JLabel lblGrade;
    private boolean additionInfoActivated;

    public ProductInformationPane() {
        super(new MigLayout());
        init();
    }

    private void init() {
        lblItem = new JLabel("Наименование: ");
        lblArticle = new JLabel("Артикул: ");
        lblModel = new JLabel("Модель: ");
        lblSize = new JLabel("Размер: ");
        lblGrade = new JLabel("Сорт: ");

        lblItemValue = new JLabel();
        lblArticleValue = new JLabel();
        lblModelValue = new JLabel();
        lblSizeValue = new JLabel("");
        lblGradeValue = new JLabel("");

        add(lblItem);
        add(lblItemValue, "wrap");
        add(lblArticle);
        add(lblArticleValue, "wrap");
        add(lblModel);
        add(lblModelValue, "wrap");
    }

    public void setPanelFont(Font font) {
        lblItem.setFont(font);
        lblItemValue.setFont(font);
        lblArticle.setFont(font);
        lblArticleValue.setFont(font);
        lblModel.setFont(font);
        lblModelValue.setFont(font);

        lblSize.setFont(font);
        lblSizeValue.setFont(font);
        lblGrade.setFont(font);
        lblGradeValue.setFont(font);
    }

    public void activateAdditionInfo() {
        if (!additionInfoActivated) {
            additionInfoActivated = true;
            add(new JPanel(), "height 5:5,  wrap");
            add(lblSize);
            add(lblSizeValue, "wrap");
            add(lblGrade);
            add(lblGradeValue, "wrap");
        }
    }

    public void setItemName(String value) {
        lblItemValue.setText(value);
    }

    public void setArticleNumber(String value) {
        lblArticleValue.setText(value);
    }

    public void setModelNumber(String value) {
        lblModelValue.setText(value);
    }

    public void setGrade(String value) {
        lblGradeValue.setText(value);
    }

    public void setSizeValue(String value) {
        lblSizeValue.setText(value);
    }

}
