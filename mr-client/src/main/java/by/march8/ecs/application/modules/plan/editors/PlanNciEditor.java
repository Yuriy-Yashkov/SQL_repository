package by.march8.ecs.application.modules.plan.editors;

import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.entities.plan.PlanItem;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dpliushchai on 01.12.2014.
 * Панель редактирования для логина nci(НЦИ)
 */
public class PlanNciEditor extends EditingPane {

    private static final long serialVersionUID = -3954031689774875650L;
    private final MainController mainController;
    private JTextField tfArticleCanvas;
    private JLabel lArticleCanvas;
    private JTextArea taNote;
    private JLabel lNote;


    private PlanItem source = new PlanItem();

    public PlanNciEditor(final MainController mainController) {
        this.mainController = mainController;
        setPreferredSize(new Dimension(270, 250));
        tfArticleCanvas = new JTextField();
        lArticleCanvas = new JLabel("Артикул полотна");
        taNote = new JTextArea();
        lNote = new JLabel("Примечание");


        lArticleCanvas.setBounds(10, 10, 250, 25);
        tfArticleCanvas.setBounds(10, 30, 250, 25);
        lNote.setBounds(10, 60, 250, 25);
        taNote.setBounds(10, 80, 250, 100);
        taNote.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        this.add(lArticleCanvas);
        this.add(tfArticleCanvas);
        this.add(lNote);
        this.add(taNote);


    }

    @Override
    public Object getSourceEntity() {


        source.setArticle_canvas(tfArticleCanvas.getText().trim());
        source.setNote(taNote.getText());

        return source;
    }

    @Override
    public void setSourceEntity(Object object) {

        if (object == null) {
            this.source = new PlanItem();


        } else {
            this.source = (PlanItem) object;


            tfArticleCanvas.setText(this.source.getArticle_canvas());
            taNote.setText(this.source.getNote());

        }
    }

    @Override
    public boolean verificationData() {


        return true;
    }
}
