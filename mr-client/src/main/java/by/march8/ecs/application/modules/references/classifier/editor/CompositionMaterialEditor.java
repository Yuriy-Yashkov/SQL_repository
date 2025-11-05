package by.march8.ecs.application.modules.references.classifier.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.classifier.enums.ClassifierType;
import by.march8.ecs.application.modules.references.classifier.mode.ClassifierPickMode;
import by.march8.ecs.framework.sdk.reference.Reference;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.classifier.CompositionMaterial;
import by.march8.entities.classifier.CompositionMaterialView;
import by.march8.entities.classifier.MaterialType;
import by.march8.entities.materials.YarnItemView;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andy 26.10.2017.
 */
public class CompositionMaterialEditor extends EditingPane {

    private ComboBoxPanel<MaterialType> cbpMaterialType;
    private UCTextFieldPanel<String> tfpMaterialName;
    private JLabel lblMaterialInformation = new JLabel("");
    private MainController controller;
    private ClassifierPickMode pickMode;

    private CompositionMaterialView viewItem = null;


    public CompositionMaterialEditor(final FrameViewPort frameViewPort) {
        setPreferredSize(new Dimension(450, 150));
        controller = frameViewPort.getController();
        pickMode = new ClassifierPickMode(controller, ClassifierType.MATERIAL);
        this.setLayout(new MigLayout());
        init();
        initEvents();
    }

    private void initEvents() {
        tfpMaterialName.addButtonSelectActionListener(a -> {

            if (cbpMaterialType.getSelectedItem().getId() > 2) {

                Object selectedItem = pickMode.showSelectModal(null);
                if (selectedItem != null) {
                    ClassifierItem item = (ClassifierItem) selectedItem;
                    tfpMaterialName.getEditor().setText(item.getModel().getName());
                    lblMaterialInformation.setText(item.getModel().getArticleName());
                    viewItem.setMaterialId(item.getModel().getId());
                }
            } else {
                Reference ref = new Reference(controller, MarchReferencesType.MATERIAL_YARN,
                        MarchWindowType.PICKFRAME);

                final Object obj = ref.showPickFrame();
                if (obj != null) {
                    YarnItemView item = (YarnItemView) obj;
                    tfpMaterialName.getEditor().setText(item.getName());
                    lblMaterialInformation.setText(item.getNote());
                    viewItem.setMaterialId(item.getId());
                }
            }
        });
    }


    @Override
    public Object getSourceEntity() {
        CompositionMaterial source = new CompositionMaterial();
        source.setId(viewItem.getId());
        source.setMaterialType(cbpMaterialType.getSelectedItem().getId());
        source.setMaterialId(viewItem.getMaterialId());
        source.setProductId(viewItem.getProductId());
        return source;
    }


    @Override
    public void setSourceEntity(final Object object) {
        cbpMaterialType.updateValues();
        if (object != null) {
            viewItem = (CompositionMaterialView) object;
            System.out.println(viewItem.getId());
            if (viewItem.getId() < 1) {
                //Новая запись
                // Предустановка типа материала как ПРЯЖА (полотно пока не рассматриваем)
                cbpMaterialType.preset(2);
                // Предустановка типа материала
                tfpMaterialName.getEditor().setText("");
                // Очистка информации о компоненте
                lblMaterialInformation.setText("Компонент не указан");
            } else {
                //Редактирование записи
                cbpMaterialType.preset(viewItem.getMaterialTypeId());
                tfpMaterialName.getEditor().setText(viewItem.getMaterialName());
                lblMaterialInformation.setText("");
            }
        }
    }

    @Override
    public boolean verificationData() {
        return true;
    }

    @Override
    public void phaseBeforeShowing() {
        defaultFillingData();
    }

    private void init() {
        add(new JLabel("Тип материала: "));
        cbpMaterialType = new ComboBoxPanel<>(controller, MarchReferencesType.MATERIAL_TYPE, false);
        cbpMaterialType.setEasy(true);
        add(cbpMaterialType, "height 20:20, width 300:20:300,wrap");
        add(new JPanel(), "height 10:10,  wrap");

        tfpMaterialName = new UCTextFieldPanel<>();
        tfpMaterialName.getEditor().setEditable(false);
        add(new JLabel("Материал"));
        add(tfpMaterialName, "height 20:20, width 300:20:300,wrap");
        lblMaterialInformation.setForeground(Color.BLUE);
        add(new JLabel("Выбрано:"));
        add(lblMaterialInformation, "wrap");

        add(new JPanel(), "height 10:10,  wrap");

        this.setFocusTraversalKeysEnabled(false);
        Set<AWTKeyStroke> forwardKeySet = new HashSet<>();
        forwardKeySet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeySet);
    }
}
