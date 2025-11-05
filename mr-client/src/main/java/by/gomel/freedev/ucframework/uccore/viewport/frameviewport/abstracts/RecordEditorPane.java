package by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts;

import by.gomel.freedev.ucframework.uccore.enums.RecordStatusType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IEditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCController;
import by.march8.ecs.MainController;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * @author Andy 21.12.14.
 */
public abstract class RecordEditorPane extends JPanel implements IEditingPane {
    private static final long serialVersionUID = -2799999429843944418L;
    protected RecordStatusType recordStatus = RecordStatusType.NOT_SAVED;
    protected RightEnum right;
    protected MainController controller;

    public RecordEditorPane(MainController controller) {
        super(null);
    }

    /**
     * Метод возвращает текущее состояние записи, по умолчанию - NOT_SAVED
     * Необходим(?) для сущностей связанных OneToMany типом
     */
    public RecordStatusType getRecordStatus() {
        return recordStatus;
    }

    @Override
    public void defaultFillingData() {

    }

    public void setEnablePanel(Component component, boolean isReadOnly) {

        if (component instanceof JTextComponent) {
            ((JTextComponent) component).setEditable(isReadOnly);
        }

        if (component instanceof JComboBox) {
            ((JComboBox) component).setEditable(isReadOnly);
        }

        if (component instanceof AbstractButton) {
            component.setEnabled(isReadOnly);
        }

        if (component instanceof ComboBoxPanel) {
            ComboBoxPanel cbp = (ComboBoxPanel) component;
            cbp.getComboBox().setEnabled(false);
            cbp.getComboBox().setEditable(false);
            cbp.getSelectButton().setVisible(isReadOnly);
            cbp.getClearButton().setVisible(isReadOnly);
            Component[] components = cbp.getComboBox().getComponents();
            for (Component comp : components) {
                if (comp instanceof AbstractButton) {
                    cbp.getComboBox().remove(comp);
                }
            }
        } else if (component instanceof JToolBar) {
            System.out.println("найдены панели инструментов");
        } else if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setEnablePanel(child, isReadOnly);
            }
        }
    }

    @Override
    public UCController getUCController() {
        return null;
    }

    public RightEnum getRight() {
        return right;
    }

    public void setRight(final RightEnum right) {
        this.right = right;
    }
}
