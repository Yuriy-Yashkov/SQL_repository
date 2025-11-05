package by.march8.ecs.application.modules.references.product.controlpane;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IEditableModule;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ControlPane;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.product.utils.UtilProduct;
import by.march8.entities.product.ModelSizeChart;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by lidashka.
 */
public class ModelSizeControl extends ControlPane {
    private IReference references;
    private FrameViewPort form;

    @Override
    public void beforeEmbedding(final FrameViewPort frameViewPort) {
        references = frameViewPort.getReference();
        form = frameViewPort;

        UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        final JButton btnCopy = new JButton();
        btnCopy.setIcon(new ImageIcon(MainController.getRunPath() + "/res/copy24.png", "Копировать"));
        btnCopy.setToolTipText("Новый размер модели изделия на основании выбранного");
        btnCopy.addMouseListener(frameViewPort.getController().getHintListener());

        JPanel searchPanel = toolBar.getSearchPanel();
        toolBar.remove(searchPanel);
        toolBar.add(btnCopy);
        toolBar.addSeparator();
        toolBar.add(searchPanel);

        btnCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                createCopyModelSizeChart();
            }
        });
    }

    @Override
    public void afterEmbedding() {
    }

    private ModelSizeChart getSelected() {
        return (ModelSizeChart) form.getGridViewPort().getSelectedItem();
    }

    private void createCopyModelSizeChart() {
        ModelSizeChart selectedSize = getSelected();
        if (selectedSize == null) {
            return;
        }

        if (JOptionPane.showOptionDialog(
                null,
                "Создать копию размера модели изделия на основании\n [" + selectedSize.toString() + "]",
                "Новый размер модели изделия",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Да", "Нет"},
                "Да") == JOptionPane.YES_OPTION) {

            DaoFactory factory = DaoFactory.getInstance();
            ICommonDao dao = factory.getCommonDao();
            try {
                Object o = dao.saveEntity(UtilProduct.createCopySize(selectedSize, "копия"));
                form.getGridViewPort().setUpdatedObject(o);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            IEditableModule module = references.getEditableModule();
            module.updateContent();
        }
    }
}
