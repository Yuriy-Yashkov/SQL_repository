package by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IButtonAction;

/**
 * @author Andy 20.01.2016.
 */
public abstract class ButtonActionAdapter implements IButtonAction {

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public boolean canCancel() {
        return true;
    }
}
