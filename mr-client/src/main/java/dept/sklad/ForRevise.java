package dept.sklad;

import common.ProgressBar;
import dept.component.MyButton;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author user
 */
public class ForRevise extends JDialog {

    private JTextField jtfNumberDocument;
    private SkladDB sdb;
    private ProgressBar pbVariable;
    private JFrame paren;

    public ForRevise(JFrame parent) {
        super(parent);
        paren = parent;
        setSize(new Dimension(300, 200));
        initComp();
    }

    private void initComp() {

        final JLabel jlNumberDocument = new JLabel("Номер документа инвентаризации");
        jtfNumberDocument = new JTextField(10);
        final GridBagConstraints gbcVariable = new GridBagConstraints();
        final GridBagLayout gblVariable = new GridBagLayout();
        sdb = new SkladDB();
        setLayout(gblVariable);
        final MyButton mbClose = new MyButton("Закрыть");
        final MyButton mbOk = new MyButton("Отчет");

        mbClose.addActionListener(e -> dispose());
        mbOk.addActionListener(e -> userAction());

        gbcVariable.gridx = 0;
        gbcVariable.gridy = 0;
        gbcVariable.anchor = GridBagConstraints.WEST;
        add(jlNumberDocument, gbcVariable);
        gbcVariable.gridx = 1;
        gbcVariable.gridy = 0;
        gbcVariable.anchor = GridBagConstraints.EAST;
        add(jtfNumberDocument, gbcVariable);


        gbcVariable.gridx = 0;
        gbcVariable.gridy = 1;
        gbcVariable.anchor = GridBagConstraints.WEST;
        //gbcVariable.fill = GridBagConstraints.BOTH;

        add(mbOk, gbcVariable);
        gbcVariable.gridx = 1;
        gbcVariable.gridy = 1;
        gbcVariable.anchor = GridBagConstraints.EAST;
        add(mbClose, gbcVariable);
        setTitle("Формирование документа для сверки");
        setSize(400, 100);
        setLocationRelativeTo(paren);
        setVisible(true);

    }

    private void userAction() {
        pbVariable = new ProgressBar(this, false, "Получение деталей...");
        SWorker sw = new SWorker();
        sw.execute();
        pbVariable.setVisible(true);
    }

    class SWorker extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            if (!jtfNumberDocument.getText().trim().equals("")) {
                if (sdb.createDbfInventarization(jtfNumberDocument.getText().trim())) {
                    JOptionPane.showMessageDialog(paren, "Документ сформирован ");
                } else {
                    JOptionPane.showMessageDialog(paren, "Произошла ошибка\nобратитесь к разработчику.");
                }
            } else {
                JOptionPane.showMessageDialog(paren, "Пустое поле 'Номер накладной'");
            }
            return null;
        }

        @Override
        protected void done() {
            try {
                pbVariable.dispose();
            } catch (Exception ex) {
                System.err.println("Ошибка при получении результатов из фонового потока " + ex);
            }
        }
    }

}
