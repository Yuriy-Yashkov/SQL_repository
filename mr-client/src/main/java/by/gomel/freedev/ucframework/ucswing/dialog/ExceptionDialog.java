package by.gomel.freedev.ucframework.ucswing.dialog;

import by.march8.ecs.MainController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Диалог для вывода сообщения об ошибке
 *
 * @author andy-linux
 */
public class ExceptionDialog extends JDialog {

    protected JLabel message = new JLabel("<html> Tooltip </html>");
    protected JTextArea messageDetail;
    protected JToggleButton btnDetail;
    protected JButton btnCancel;
    private JPanel content = new JPanel(new BorderLayout());
    private JPanel panelMessage = new JPanel(new BorderLayout());
    private JPanel panelControl = new JPanel(new BorderLayout());
    private JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private JPanel panelScroll = new JPanel(new BorderLayout());
    private Dimension errorDetailSize = new Dimension(300, 300);

    public ExceptionDialog(MainController mainController, final Throwable cause) {
        super();
        //setSize(new Dimension(450, 150));
        setResizable(false);
        // setLocationRelativeTo();
        initFrame();
        initEvents();
        //setErrorProcessing(cause);
        JFrame parent = mainController.getMainForm();
        final Dimension screenSize;
        if (parent != null) {
            screenSize = mainController.getMainForm().getSize();
        } else {
            Toolkit toolKit = Toolkit.getDefaultToolkit();
            screenSize = toolKit.getScreenSize();
        }

        Dimension frameSize = new Dimension(450, 150);
        Point p = new Point(screenSize.width / 2 - frameSize.width / 2,
                screenSize.height / 2 - frameSize.height / 2);

        setModalityType(ModalityType.TOOLKIT_MODAL);

        setSize(frameSize);
        setLocation(p);

        setLocationRelativeTo(mainController.getMainForm());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Ошибка в программе (сообщения в тесте)");
    }

    public ExceptionDialog(MainController mainController) {
        this(mainController, null);
    }


    /**
     * Инициализация компонентов формы
     */
    private void initFrame() {
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setVerticalAlignment(SwingConstants.CENTER);

        panelMessage.add(message, BorderLayout.CENTER);
        panelMessage.setPreferredSize(new Dimension(0, 87));

        messageDetail = new JTextArea("Test Message", 5, 10);
        messageDetail.setEditable(false);
        Font font = new Font("monospaced", Font.PLAIN, 11);
        messageDetail.setFont(font);


        btnCancel = new JButton("Закрыть");
        btnDetail = new JToggleButton("Детали");

        Dimension buttonSize = new Dimension(100, 25);
        btnCancel.setPreferredSize(buttonSize);
        btnDetail.setPreferredSize(buttonSize);

        panelButton.add(btnDetail, BorderLayout.NORTH);
        panelButton.add(btnCancel, BorderLayout.NORTH);

        panelScroll.add(new JScrollPane(messageDetail), BorderLayout.CENTER);
        panelScroll.setBorder(new EmptyBorder(0, 5, 5, 5));

        panelControl.add(panelScroll, BorderLayout.CENTER);
        panelControl.setPreferredSize(new Dimension(0, 34));
        panelControl.add(panelButton, BorderLayout.NORTH);

        content.add(panelMessage, BorderLayout.NORTH);
        content.add(panelControl, BorderLayout.CENTER);

        setContentPane(content);
    }

    /**
     * Метод инициализации событий формы
     */
    private void initEvents() {
        btnDetail.addActionListener(e -> rollDetailPanel());

        btnCancel.addActionListener(e -> setVisible(false));
    }

    /**
     * Метод управляет видимостью компонента детальной информации
     */
    private void rollDetailPanel() {
        if (btnDetail.isSelected()) {
            Dimension formSize = this.getSize();
            this.setSize(new Dimension(formSize.width, formSize.height + errorDetailSize.height));
        } else {
            errorDetailSize = panelScroll.getSize();
            Dimension formSize = new Dimension(this.getSize().width, this.getSize().height - errorDetailSize.height);
            this.setSize(formSize);
        }
    }


    /**
     * Устанавливает текст сообщения
     *
     * @param text содержания сообщения
     */
    public void setExceptionInformation(final String text) {
        message.setText(text);
    }

    /**
     * Установка текста для компонента содержащего подробную информацию
     *
     * @param text ссылка на билдер сообщения
     */
    public void setExceptionDetail(final StringBuilder text) {
        messageDetail.setText("");
        messageDetail.append(text.toString());
        messageDetail.setCaretPosition(0);
    }
}
