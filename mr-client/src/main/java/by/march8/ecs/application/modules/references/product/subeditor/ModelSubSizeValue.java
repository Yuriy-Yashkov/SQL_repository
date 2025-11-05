package by.march8.ecs.application.modules.references.product.subeditor;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.focus.FocusProcessing;
import by.march8.ecs.application.modules.references.product.editor.ModelSubSizeValueEditor;
import by.march8.ecs.framework.sdk.reference.abstracts.SubReferences;
import by.march8.entities.product.ModelSizeChart;
import by.march8.entities.product.ModelSizeValue;

import java.awt.*;

/**
 * Created by lidashka.
 */
public class ModelSubSizeValue extends SubReferences {
    private ModelSizeChart source;

    public ModelSubSizeValue(final IReference reference, final Container container) {
        super(reference, container, ModelSizeValue.class);
        editPane = new ModelSubSizeValueEditor(reference);
        editPane.setRight(reference.getRight());
        FocusProcessing fp = new FocusProcessing();
        fp.setBorderColor(editPane);
        updateContent();
    }

    @Override
    public void setSourceEntity(Object object) {
        source = (ModelSizeChart) object;
    }

    @Override
    public void updateEntity(Object object) {
        ((ModelSizeValue) object).setModelSize(source);
    }
}
