package forms;

import org.junit.Ignore;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andy 14.12.2018 - 10:48.
 */
public class TextUI {

    @Test
    @Ignore
    public void testDnD() {
        new DragDropText();
    }


    public class DragDropText extends JDialog {

        public DragDropText() {
            // setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            this.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);

            JTextField field1 = new JTextField("Life's a drag", 20);
            JTextField field2 = new JTextField("and then you drop", 20);
            field1.setDragEnabled(true);
            field2.setDragEnabled(true);
            Container content = getContentPane();

            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.add(field1);
            content.add(field2);

            pack();

            setVisible(true);
        }

    }
}
