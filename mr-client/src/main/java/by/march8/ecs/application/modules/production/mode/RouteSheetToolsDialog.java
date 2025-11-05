package by.march8.ecs.application.modules.production.mode;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.production.dao.RouteSheetJDBC;
import by.march8.ecs.application.modules.production.model.RouteSheetBasic;
import by.march8.ecs.framework.common.BackgroundTask;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;


/**
 * @author Andy on 28.02.2020 8:13
 */
public class RouteSheetToolsDialog extends BasePickDialog {

    private JPanel pContent;
    private GridViewPort<RouteSheetBasic> gvList;
    private List<RouteSheetBasic> list;
    private JLabel lblDocumentNumber;
    private UCTextField tfDocumentNumber;
    private JButton btnSearchDocument;

    private JLabel lblRouteSheet;
    private JComboBox<String> cbDepartments;
    private JButton btnSetDepartment;

    private RouteSheetJDBC db;

    public RouteSheetToolsDialog(MainController controller) {
        super(controller);
        getBtnSave().setVisible(false);
        getBtnCancel().setText("Закрыть");
        setTitle("Изменение цеха назначения документа");
        initComponents();

        initEvents();
    }

    private void initComponents() {
        setFrameSize(new Dimension(500, 350));
        pContent = new JPanel(new MigLayout());

        gvList = new GridViewPort<>(RouteSheetBasic.class, false);
        list = gvList.getDataModel();


        lblDocumentNumber = new JLabel("№ маршрута: ");
        tfDocumentNumber = new UCTextField("0.00");
        tfDocumentNumber.setComponentParams(lblDocumentNumber, Float.class, 2);
        btnSearchDocument = new JButton("Искать маршрут");


        lblRouteSheet = new JLabel("Код цеха :");
        cbDepartments = new JComboBox<>();
        btnSetDepartment = new JButton("Переместить маршрут");


        pContent.add(lblDocumentNumber);
        pContent.add(tfDocumentNumber, "height 20:20, width 150:20:150");
        pContent.add(btnSearchDocument, "height 20:20, width 200:20:200, wrap");

        pContent.add(new JPanel(), "height 10:10,  wrap");

        pContent.add(gvList, "span 3,height 150:150, width 460:20:460, wrap");

        pContent.add(new JPanel(), "height 10:10,  wrap");
        pContent.add(lblRouteSheet);
        pContent.add(cbDepartments, "height 20:20, width 150:20:150");
        pContent.add(btnSetDepartment, "height 20:20, width 200:20:200,wrap");

        getToolBar().setVisible(false);

        getCenterContentPanel().add(pContent);
        setButtonAction(new ButtonActionAdapter() {
            @Override
            public boolean canSave() {
                return true;
            }

            @Override
            public boolean canCancel() {
                return true;
            }
        });

        db = new RouteSheetJDBC();
        fillDepartments(cbDepartments);
    }

    private void initEvents() {
        btnSearchDocument.addActionListener(a -> {
            searchRouteSheet(tfDocumentNumber.getText());
        });


        tfDocumentNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnSearchDocument.doClick();
                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    tfDocumentNumber.setText("0.00");
                }
            }
        });

        gvList.setTableEventHandler(new TableEventAdapter<RouteSheetBasic>() {

            @Override
            public void onSelectChanged(final int rowIndex, final RouteSheetBasic item) {
                if (item != null) {
                    presetComboDepartmentOwner(cbDepartments, item.getDeptOwner());
                }
            }

            @Override
            public void onDoubleClick(final int rowIndex, int columnIndex, final RouteSheetBasic item) {

            }
        });

        btnSetDepartment.addActionListener(a -> {
            RouteSheetBasic doc = gvList.getSelectedItem();
            if (doc != null) {
                if (cbDepartments.getSelectedIndex() >= 0) {
                    final int answer = Dialogs.showQuestionDialog("Вы действительно хотите документу [" + doc.getDocumentNumber()
                                    + " от "
                                    + by.march8.api.utils.DateUtils.getNormalDateFormat(doc.getDocumentDate())
                                    + "] сменить владельца ?",
                            "Изменение параметров документа");
                    if (answer == 0) {
                        changeDocumentOwner(doc, Integer.valueOf((String) cbDepartments.getSelectedItem()));
                    }
                }
            }
        });
    }

    private void presetComboDepartmentOwner(JComboBox<String> cbDepartments, int deptOwner) {
        for (int i = 0; i < cbDepartments.getItemCount(); i++) {
            final String itemAt = cbDepartments.getItemAt(i).trim();

            if (itemAt.equals(String.valueOf(deptOwner))) {
                cbDepartments.setSelectedIndex(i);
                return;
            }
        }
        cbDepartments.setSelectedIndex(-1);
    }

    private void changeDocumentOwner(RouteSheetBasic document, int code) {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                db.changeDocumentOwner(document, code);
                gvList.updateViewPort();
                return true;
            }
        }
        Task task = new Task("Изменение маршрутного листа...");
        task.executeTask();
    }

    private void searchRouteSheet(String text) {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                list.clear();
                try {
                    double number = Double.valueOf(text);
                    list.addAll(db.getRouteSheetsByNumber(number));
                } catch (ArithmeticException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Номер документа не является числом ", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                gvList.updateViewPort();
                return true;
            }
        }
        Task task = new Task("Поиск маршрутного листа...");
        task.executeTask();
    }

    private void fillDepartments(JComboBox<String> comboBox) {
        List<String> list = db.getDepartmentCodes();
        if (list != null) {
            comboBox.setModel(new DefaultComboBoxModel(list.toArray()));
            comboBox.setSelectedIndex(-1);
        }
    }

    public boolean showDialog() {
        showModalFrame();
        return true;
    }
}
