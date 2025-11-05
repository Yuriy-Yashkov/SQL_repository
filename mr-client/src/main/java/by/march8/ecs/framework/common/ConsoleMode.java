package by.march8.ecs.framework.common;

import by.gomel.freedev.ucframework.ucswing.frame.BaseFrame;
import by.march8.ecs.MainController;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andy 08.08.2016.
 */
public class ConsoleMode extends BaseFrame {


    public ConsoleMode(MainController controller) {
        setTitle("Поток вывода служебной информации информации");
        Container cc = getContentPane();
        JTextArea textComponent = new JTextArea();
        cc.add(new JScrollPane(textComponent));

        Font font = new Font("monospaced", Font.PLAIN, 11);
        textComponent.setFont(font);

        MessageConsole mc = new MessageConsole(textComponent);
        mc.redirectOut();
        mc.redirectErr(Color.RED, null);
        mc.setMessageLines(100);


        Dimension screenSize = controller.getMainForm().getSize();
        Dimension dimension = new Dimension(600, 300);
        Point p = new Point(screenSize.width / 2 - dimension.width / 2,
                screenSize.height / 2 - dimension.height / 2);
        setSize(dimension);
        setLocation(p);
        setLocationRelativeTo(controller.getMainForm());
        setResizable(true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
