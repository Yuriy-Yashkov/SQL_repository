package by.march8.ecs.application.modules.art.subeditor;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.focus.FocusProcessing;
import by.march8.ecs.application.modules.art.editor.ModelConfectionMapCanvasEditor;
import by.march8.ecs.framework.sdk.reference.abstracts.SubReferences;
import by.march8.entities.product.ConfectionMap;
import by.march8.entities.product.ConfectionMapCanvas;

import java.awt.*;

/**
 * Created by lidashka.
 */
public class ModelConfectionMapSubCanvas extends SubReferences {
    private ConfectionMap source;

    public ModelConfectionMapSubCanvas(IReference reference, final Container container) {
        super(reference, container, ConfectionMapCanvas.class);
        //initSizeColumn();
        //initComponent();
        editPane = new ModelConfectionMapCanvasEditor(reference);
        editPane.setRight(reference.getRight());
        FocusProcessing fp = new FocusProcessing();
        fp.setBorderColor(editPane);
        updateContent();
    }

    @Override
    public void setSourceEntity(Object object) {
        source = (ConfectionMap) object;

    }

    @Override
    public void updateEntity(Object object) {
        ((ConfectionMapCanvas) object).setConfectionMap(source);
    }
}
