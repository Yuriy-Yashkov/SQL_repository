package by.march8.ecs.application.modules.references.materals.subreference;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.focus.FocusProcessing;
import by.march8.ecs.application.modules.references.materals.editor.CanvasComponentEditor;
import by.march8.ecs.framework.sdk.reference.abstracts.SubReferences;
import by.march8.entities.materials.CanvasComponent;
import by.march8.entities.materials.CanvasItem;

import java.awt.*;

/**
 * @author Andy 09.01.2015.
 */
public class CanvasComponentSubReference extends SubReferences {

    private CanvasItem source;

    public CanvasComponentSubReference(final IReference reference, final Container container) {
        super(reference, container, CanvasComponent.class);
        editPane = new CanvasComponentEditor(reference);
        editPane.setRight(reference.getRight());
        FocusProcessing fp = new FocusProcessing();
        fp.setBorderColor(editPane);
        updateContent();
    }

    @Override
    public void setSourceEntity(final Object object) {
        source = (CanvasItem) object;
    }

    @Override
    public void updateEntity(final Object object) {
        ((CanvasComponent) object).setCanvas(source);
    }

}
