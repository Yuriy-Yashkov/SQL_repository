package by.march8.ecs.application.modules.warehouse.external.shipping.editor;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.BaseLabelInformation;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.EanCodeSaleDocumentItem;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.NSIEanCode;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andy 05.10.2018 - 7:05.
 */
public class EanCodeSaleDocumentEditor extends EditingPane {

    private JLabel lblColor = new JLabel("Цвет :");
    private JTextField tfColor = new JTextField();

    private JLabel lblEanCode = new JLabel("EAN код :");
    private JTextField tfEanCode = new JTextField();

    private JLabel lblLabelBarCode = new JLabel("BAR :");
    private JLabel lblLabelEanCode = new JLabel("EAN-13:");
    private JLabel lblLabelColor = new JLabel("Цвет :");

    private JLabel lblValueBarCode = new JLabel("-");
    private JLabel lblValueEanCode = new JLabel("-");
    private JLabel lblValueColor = new JLabel("-");


    private GridViewPort gridViewPort;
    private ArrayList<Object> data;

    private Color c = Color.GREEN.darker().darker();

    private EanCodeSaleDocumentItem source;

    public EanCodeSaleDocumentEditor(MainController mainController) {
        controller = mainController;
        setPreferredSize(new Dimension(400, 400));
        this.setLayout(new BorderLayout());
        init();

        this.setFocusTraversalKeysEnabled(false);
        Set<AWTKeyStroke> forwardKeySet = new HashSet<>();
        forwardKeySet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeySet);
    }

    private void init() {
        JPanel pEditor = new JPanel(new MigLayout());
        pEditor.setSize(new Dimension(300, 200));
        pEditor.add(lblColor, "");
        pEditor.add(tfColor, "width 300:20:300, wrap");

        pEditor.add(lblEanCode, "");
        pEditor.add(tfEanCode, "width 300:20:300, wrap");

        pEditor.add(new JPanel(), "height 10:10,  wrap");

        JPanel pLabelInformation = new JPanel(new MigLayout());
        pLabelInformation.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pLabelInformation.setBorder(BorderFactory.createTitledBorder("Данные на этикетке"));


        pLabelInformation.add(lblLabelBarCode, "");
        pLabelInformation.add(lblValueBarCode, "width 200:20:200, wrap");

        pLabelInformation.add(lblLabelEanCode, "");
        pLabelInformation.add(lblValueEanCode, "width 200:20:200, wrap");

        lblLabelEanCode.setForeground(c);
        lblValueEanCode.setForeground(c);

        pLabelInformation.add(lblLabelColor, "");
        pLabelInformation.add(lblValueColor, "width 200:20:200,  wrap");

        pEditor.add(pLabelInformation, "width 380:90:380, height 90:90, span 2, wrap");

        pEditor.add(new JPanel(), "height 10:10,  wrap");


        add(pEditor, BorderLayout.NORTH);

        gridViewPort = new GridViewPort(NSIEanCode.class, false);
        data = gridViewPort.getDataModel();
        gridViewPort.primaryInitialization();

        add(gridViewPort, BorderLayout.CENTER);

        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(int rowIndex, int columnIndex, Object object) {
                NSIEanCode item = (NSIEanCode) object;
                if (item != null) {
                    tfEanCode.setText(item.getEanCode());
                }
            }
        });
    }

    @Override
    public Object getSourceEntity() {
        source.setColor(tfColor.getText().trim());
        source.setEanCode(tfEanCode.getText().trim());
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object != null) {
            source = (EanCodeSaleDocumentItem) object;

            tfColor.setText(source.getColor());
            tfEanCode.setText(source.getEanCode());
            updateEanList(source);
        }
    }

    private void updateEanList(EanCodeSaleDocumentItem itemCode) {
        // Получаем данные из этикетки по баркоду
        SaleDocumentJDBC db = new SaleDocumentJDBC();

        BaseLabelInformation information = db.getLabelInformation(itemCode);

        if (information != null) {

            if (information.getBarCode().length() == 8) {
                information.setBarCode(information.getBarCode() + "(Упаковка)");
            }
            lblValueColor.setText(information.getColor());
            lblValueBarCode.setText(information.getBarCode());
            lblValueEanCode.setText(information.getEanCode());
        } else {
            lblValueColor.setText("-");
            lblValueBarCode.setText("-");
            lblValueEanCode.setText("-");
        }

        // ТАщим из справочника EAN цветов
        DaoFactory<NSIEanCode> factory = DaoFactory.getInstance();
        IGenericDao<NSIEanCode> dao = factory.getGenericDao();
        try {
            java.util.List<QueryProperty> criteria = new ArrayList<>();
            criteria.add(new QueryProperty("itemCode", itemCode.getItemCode()));
            data.clear();
            data.addAll(dao.getEntityListByNamedQuery(NSIEanCode.class, "NSIEanCode.findByItemCode", criteria));
        } catch (final Exception e) {
            e.printStackTrace();
        }


        gridViewPort.updateViewPort();
    }

    @Override
    public boolean verificationData() {
        if (tfColor.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Цвет\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfColor.requestFocusInWindow();
            return false;
        }

        if (tfEanCode.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"EAN код\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfEanCode.requestFocusInWindow();
            return false;
        }

        if (tfEanCode.getText().trim().length() != 13) {
            JOptionPane.showMessageDialog(null,
                    "\"EAN код\" должен иметь длинну 13 символов", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfEanCode.requestFocusInWindow();
            return false;
        }

        return true;
    }
}

