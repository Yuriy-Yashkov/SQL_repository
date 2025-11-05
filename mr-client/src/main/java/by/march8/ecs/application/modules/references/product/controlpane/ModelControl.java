package by.march8.ecs.application.modules.references.product.controlpane;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IEditableModule;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ControlPane;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.entities.product.ModelProduct;
import by.march8.entities.product.ModelSizeChart;
import by.march8.entities.product.ModelSizeValue;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by lidashka.
 */
public class ModelControl extends ControlPane {
    private IReference references;
    private FrameViewPort form;

    @Override
    public void beforeEmbedding(final FrameViewPort frameViewPort) {
        references = frameViewPort.getReference();
        form = frameViewPort;

        UCToolBar toolBar = form.getFrameRegion().getToolBar();
        final JButton btnCopy = new JButton();
        btnCopy.setIcon(new ImageIcon(MainController.getRunPath() + "/res/copy24.png", "Копировать"));
        btnCopy.setToolTipText("Новая модель изделия на основании выбранной");
        btnCopy.addMouseListener(frameViewPort.getController().getHintListener());

        JPanel searchPanel = form.getFrameRegion().getToolBar().getSearchPanel();
        toolBar.remove(searchPanel);
        toolBar.add(btnCopy);
        toolBar.addSeparator();
        toolBar.add(searchPanel);

        btnCopy.addActionListener(e -> createCopyModelProduct());
    }

    @Override
    public void afterEmbedding() {

    }

    private ModelProduct getSelected() {
        return (ModelProduct) form.getGridViewPort().getSelectedItem();
    }

    private void createCopyModelProduct() {
        ModelProduct selectedModel = getSelected();
        if (selectedModel == null) {
            return;
        }

        if (JOptionPane.showOptionDialog(
                null,
                "Создать копию модели изделия на основании\n [" + selectedModel.toString() + "]",
                "Новая модель изделия",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Да", "Нет"},
                "Да") == JOptionPane.YES_OPTION) {

            ModelProduct newModel = new ModelProduct();
            newModel.setModel(selectedModel.getModel());
            newModel.setName("КОПИЯ " + selectedModel.getName());
            newModel.setNote(selectedModel.getNote());
            newModel.setKind(selectedModel.getKind());
            newModel.setBrand(selectedModel.getBrand());
            newModel.setCollection(selectedModel.getCollection());
            newModel.setGroup(selectedModel.getGroup());
            newModel.setRange(selectedModel.getRange());
            newModel.setUnit(selectedModel.getUnit());

            Set<ModelSizeChart> sizes = selectedModel.getModelSize();
            for (ModelSizeChart itemSize : sizes) {
                ModelSizeChart newSize = new ModelSizeChart();
                newSize.setModel(newModel);
                newSize.setHeight(itemSize.getHeight());
                newSize.setSize(itemSize.getSize());
                newSize.setNote(itemSize.getNote());

                Set<ModelSizeValue> sizeValue = itemSize.getModelSizeValues();
                for (ModelSizeValue itemSizeValue : sizeValue) {
                    ModelSizeValue newSizeValue = new ModelSizeValue(itemSizeValue);
                    newSizeValue.setModelSize(newSize);

                    newSize.getModelSizeValues().add(newSizeValue);
                }

                newModel.getModelSize().add(newSize);
            }

            newModel.setStandards(selectedModel.getStandards());
            newModel.setComposition(selectedModel.getComposition());

            newModel.setDescription(selectedModel.getDescription());
            newModel.setPainter(selectedModel.getPainter());
            newModel.setConstructor(selectedModel.getConstructor());

            DaoFactory factory = DaoFactory.getInstance();
            ICommonDao dao = factory.getCommonDao();

            try {
                dao.addEntity(newModel);
                form.getGridViewPort().setUpdatedObject(selectedModel);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            IEditableModule module = references.getEditableModule();
            module.updateContent();
        }
    }
}
