package by.march8.ecs.application.modules.references.materals.subreference;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.focus.FocusProcessing;
import by.march8.ecs.application.modules.references.materals.editor.CanvasMarkingComponentEditor;
import by.march8.ecs.framework.sdk.reference.abstracts.SubReferences;
import by.march8.entities.materials.CanvasItem;
import by.march8.entities.materials.CanvasMarkingComposition;

import javax.swing.*;

/**
 * @author Andy 06.04.2015.
 */
public class CanvasMarkingComponentSubReference extends SubReferences {

    private CanvasItem source;

    public CanvasMarkingComponentSubReference(final IReference reference, final JPanel pMarkingComponent) {
        super(reference, pMarkingComponent, CanvasMarkingComposition.class);
        editPane = new CanvasMarkingComponentEditor(reference);
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
        ((CanvasMarkingComposition) object).setCanvas(source);
    }
}
