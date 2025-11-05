package by.march8.ecs.application.modules.warehouse.external.shipping.editor;

import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.march8.api.BaseEntity;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.ecs.application.modules.warehouse.external.shipping.mode.ResponsiblePersonsMode;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.FinishedGoodsHelper;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.ResponsiblePersonsService;
import by.march8.entities.readonly.AddressBaseEntity;
import by.march8.entities.readonly.AddressEntity;
import by.march8.entities.readonly.ContractEntity;
import by.march8.entities.readonly.ContractorEntity;
import by.march8.entities.warehouse.ResponsiblePersons;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDriving;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static by.march8.ecs.framework.common.Settings.COLOR_DISABLED;
import static by.march8.ecs.framework.common.Settings.COLOR_ENABLED;

/**
 * @author Andy 12.11.2015.
 */
public class DrivingDocumentEditor extends EditingPane {


    private JPanel panelTransport;
    private JPanel panelGoods;


    private JLabel lblDocumentNumber = new JLabel("Путевой лист № ");
    private JLabel lblDocumentDate = new JLabel(" от ");


    private JTextField tfDocumentNumber = new JTextField();
    private UCDatePicker dpDocumentDate = new UCDatePicker(new Date());


    private JComboBox tfCarNumber = new JComboBox();
    private JButton btnCarNumber;

    private JTextField tfCarTrailerNumber = new JTextField();
    private JComboBox tfCarDriverName = new JComboBox();
    private JButton btnCarDriverName;

    private JComboBox cbCarOwner = new JComboBox();
    private JTextField tfCarCustomer = new JTextField();
    private JTextField tfCarPayer = new JTextField();

    private JComboBox<AddressBaseEntity> cbShipperAddress = new JComboBox<>();
    //private JTextField tfConsigneeName = new JTextField();
    private JComboBox<AddressBaseEntity> cbConsigneeAddress = new JComboBox<>();

    private JComboBox<ContractEntity> cbContractId = new JComboBox<>();
    private JComboBox<AddressEntity> cbLoadingAddress = new JComboBox<>();
    private JComboBox<AddressEntity> cbUnloadingAddress = new JComboBox<>();
    private JButton btnAddUnloadingAddress;

    private JTextField tfReaddressing = new JTextField();

    private JComboBox tfSaleAllowed = new JComboBox();
    private JButton btnSaleAllowed;

    private JComboBox tfShipperPassed = new JComboBox();
    private JButton btnShipperPassed;

    private JComboBox tfTransportationReceive = new JComboBox();
    private JButton btnTransportationReceive;

    private JComboBox cbSealNumber = new JComboBox();
    private JComboBox cbLoadingDoer = new JComboBox();
    private JComboBox cbLoadingMethod = new JComboBox();
    private JComboBox cbDocumentType = new JComboBox();
    private JTextField tfWarrantNumber = new JTextField();
    private UCDatePicker dpWarrantDate = new UCDatePicker(new Date());

    private JComboBox tfWarrantIssued = new JComboBox();
    private JButton btnWarrantIssued;

    private JTextField tfWarrantReceive = new JTextField();
    private JTextField tfWarrantSealNumber = new JTextField();

    private JTextField tfSupportDocument = new JTextField("");

    private JComboBox cbNote = new JComboBox();
    private JButton btnNote;

    private ContractorEntity contractorSender;
    private ContractorEntity contractorReceiver;

    private SaleDocumentManager manager;
    private SaleDocumentBase saleDocument;
    private SaleDocumentDriving source;
    private ResponsiblePersons responsiblePersons = null;

    private NewFocusListener listener = new NewFocusListener();
    private boolean bitCarCustomer = false;

    private ResponsiblePersonsService service = null;

    public DrivingDocumentEditor(MainController mainController, SaleDocumentManager manager, SaleDocumentBase document) {
        setPreferredSize(new Dimension(970, 570));
        this.setLayout(null);
        controller = mainController;
        this.manager = manager;
        saleDocument = document;


        init();
        initEvents();
        //add(mainPanel);

        service = ResponsiblePersonsService.getInstance();
        service.primaryInitialization();

        tfSaleAllowed.setEditable(true); //0
        tfShipperPassed.setEditable(true); // 1
        tfTransportationReceive.setEditable(true); //2
        tfCarDriverName.setEditable(true);  //3
        tfCarNumber.setEditable(true);
        tfWarrantIssued.setEditable(true);
        cbNote.setEditable(true);

        service.loadComboBoxDate(tfSaleAllowed, 0, SaleDocumentManager.isExportDocument(saleDocument));
        service.loadComboBoxDate(tfShipperPassed, 1, false);
        service.loadComboBoxDate(tfTransportationReceive, 2, false);
        service.loadComboBoxDate(tfCarDriverName, 3, false);
        service.loadComboBoxDate(tfCarNumber, 4, false);
        service.loadComboBoxDate(tfWarrantIssued, 5, false);
        service.loadComboBoxDate(cbNote, 6, false);

        FinishedGoodsHelper.getSealeNumber(cbSealNumber);
        FinishedGoodsHelper.getLoadingDoer(cbLoadingDoer);
        FinishedGoodsHelper.getLoadingMethod(cbLoadingMethod);
        //FinishedGoodsHelper.getNotes(cbNote);
        FinishedGoodsHelper.getCarOwners(cbCarOwner);


        /*
        dpDocumentDate.initComponent(".");
        dpWarrantDate.initComponent(".");
        */
        setFocusEvents(this);

    }

    private void initEvents() {
        btnAddUnloadingAddress.addActionListener(a -> {
            BaseEditorDialog editor = new BaseEditorDialog(controller,
                    RecordOperationType.NEW);
            EditingPane addressEditor = new AddressEditor(controller);
            // Для созданного пустого диалога устанавливаем панель редактирования
            editor.setEditorPane(addressEditor);
            // Модально показываем форму и ожидаем закрытия
            // AddressEntity selectedAddress = (AddressEntity)cbUnloadingAddress.getSelectedItem();
            addressEditor.setSourceEntity(null);
            if (editor.showModal()) {
                AddressEntity newAddress = (AddressEntity) addressEditor.getSourceEntity();
                if (newAddress != null) {
                    //cbUnloadingAddress.removeAllItems();
                    //cbUnloadingAddress.addItem(newAddress);
                    contractorReceiver.getAddressList().add(newAddress);
                    newAddress.setContractor(contractorReceiver);

                    final DaoFactory factory = DaoFactory.getInstance();
                    // ПОлучаем интерфейс для работы с БД
                    final ICommonDao dao = factory.getCommonDao();
                    try {
                        int code = contractorReceiver.getCode();
                        dao.updateEntity(contractorReceiver);
                        contractorReceiver = manager.getContractorByCode(code, "");
                        //contractorReceiver = (ContractorEntity) dao.getEntityById(ContractorEntity.class, id);
                        fillingComboBoxContractor(cbUnloadingAddress, contractorReceiver.getAddressList());
                        //System.out.println("Индекс "+newAddress.getId());
                    } catch (final SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnSaleAllowed.addActionListener(a -> {
            new ResponsiblePersonsMode(controller, SaleDocumentManager.isExportDocument(saleDocument), 0);

            updateService();
        });

        btnShipperPassed.addActionListener(a -> {
            new ResponsiblePersonsMode(controller, false, 1);
            updateService();
        });

        btnTransportationReceive.addActionListener(a -> {
            new ResponsiblePersonsMode(controller, false, 2);
            updateService();
        });

        btnCarDriverName.addActionListener(a -> {
            new ResponsiblePersonsMode(controller, false, 3);
            updateService();
        });

        btnCarNumber.addActionListener(a -> {
            new ResponsiblePersonsMode(controller, false, 4);
            updateService();
        });

        btnWarrantIssued.addActionListener(a -> {
            new ResponsiblePersonsMode(controller, false, 5);
            updateService();
        });

        btnNote.addActionListener(a -> {
            new ResponsiblePersonsMode(controller, false, 6);
            updateService();
        });
    }

    private void updateService() {

        service.updateService();
        service.loadComboBoxDate(tfSaleAllowed, 0, SaleDocumentManager.isExportDocument(saleDocument));
        service.loadComboBoxDate(tfShipperPassed, 1, false);
        service.loadComboBoxDate(tfTransportationReceive, 2, false);
        service.loadComboBoxDate(tfCarDriverName, 3, false);
        service.loadComboBoxDate(tfCarNumber, 4, false);
        service.loadComboBoxDate(tfWarrantIssued, 5, false);
        service.loadComboBoxDate(cbNote, 6, false);
    }

    private void init() {

        initGoodsPanel();
        initTransportPanel();
        initTransportPanel();
        int x = 0;
        int y = 0;

        lblDocumentNumber.setBounds(x + 10, y + 10, 120, 20);
        add(lblDocumentNumber);
        tfDocumentNumber.setBounds(x + 165, y + 10, 100, 20);
        add(tfDocumentNumber);
        lblDocumentDate.setBounds(x + 270, y + 10, 30, 20);
        add(lblDocumentDate);

        dpDocumentDate.setBounds(x + 295, y + 10, 100, 20);

        add(panelGoods);
        add(panelTransport);
        add(dpDocumentDate);

        this.setFocusTraversalKeysEnabled(false);
        Set<AWTKeyStroke> forwardKeySet = new HashSet<>();
        forwardKeySet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeySet);
    }

    private void initTransportPanel() {
        panelTransport = new JPanel();
        panelTransport.setLayout(null);
        panelTransport.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelTransport.setBorder(BorderFactory.createTitledBorder("Транспорт"));
        panelTransport.setBounds(5, 410, 955, 120);

        //форимрование закладки "Транспорт"
        JLabel lblCarNumber = new JLabel("Автомобиль");
        JLabel lblCarTrailerNumber = new JLabel("Прицеп");
        JLabel lblCarDriverName = new JLabel("Водитель");
        JLabel lblCarOwner = new JLabel("Владелец автомобиля");
        JLabel lblCarCustomer = new JLabel("Заказчик автомобиля");
        JLabel lblCarPayer = new JLabel("Плательщик");

        int x = 0;
        int y = 20;

        lblCarNumber.setBounds(x + 10, y, 100, 20);
        panelTransport.add(lblCarNumber);

        tfCarNumber.setBounds(x + 150, y, 150, 20);

        btnCarNumber = new JButton();
        btnCarNumber.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/book.png", "Справочник"));
        btnCarNumber.setBounds(x + 300, y, 20, 20);

        panelTransport.add(tfCarNumber);
        panelTransport.add(btnCarNumber);

        lblCarTrailerNumber.setBounds(x + 335, y, 60, 20);
        panelTransport.add(lblCarTrailerNumber);
        tfCarTrailerNumber.setBounds(x + 485, y, 150, 20);
        panelTransport.add(tfCarTrailerNumber);

        lblCarDriverName.setBounds(x + 10, y + 30, 100, 20);
        panelTransport.add(lblCarDriverName);

        tfCarDriverName.setBounds(x + 150, y + 30, 220, 20);
        panelTransport.add(tfCarDriverName);

        btnCarDriverName = new JButton();
        btnCarDriverName.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/book.png", "Справочник"));
        btnCarDriverName.setBounds(x + 370, y + 30, 20, 20);

        panelTransport.add(btnCarDriverName);

        lblCarOwner.setBounds(x + 305, y + 30, 180, 20);
        //panelTransport.add(lblCarOwner);
        cbCarOwner.setBounds(x + 485, y + 30, 150, 20);
        //panelTransport.add(cbCarOwner);

        lblCarCustomer.setBounds(x + 305, y + 60, 180, 20);
        panelTransport.add(lblCarCustomer);
        tfCarCustomer.setBounds(x + 485, y + 60, 430, 20);

        JButton btnRepopulateCarCustomer = new JButton();
        btnRepopulateCarCustomer.setBounds(x + 915, y + 60, 20, 20);
        btnRepopulateCarCustomer.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/books.png", "Добавить адрес"));

        panelTransport.add(tfCarCustomer);
        panelTransport.add(btnRepopulateCarCustomer);

        lblCarPayer.setBounds(x + 10, y + 60, 180, 20);
        panelTransport.add(lblCarPayer);
        tfCarPayer.setBounds(x + 150, y + 60, 150, 20);
        panelTransport.add(tfCarPayer);

        btnRepopulateCarCustomer.addActionListener(e -> {
            if (source != null) {
                if (bitCarCustomer) {
                    if (contractorSender != null) {
                        tfCarCustomer.setText(contractorSender.getName() + "," + contractorSender.getLegalAddress().toString());
                        tfCarPayer.setText(contractorSender.getCodeUNN());
                        bitCarCustomer = false;
                    }
                } else {
                    if (contractorReceiver != null) {
                        tfCarCustomer.setText(contractorReceiver.getName() + "," + contractorReceiver.getLegalAddress().toString());
                        tfCarPayer.setText(contractorReceiver.getCodeUNN());
                        bitCarCustomer = true;
                    }
                }
            }
        });
    }


    private void initGoodsPanel() {
        int x = 5;
        int y = 0;
        panelGoods = new JPanel(null);
        panelGoods.setLayout(null);
        panelGoods.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelGoods.setBorder(BorderFactory.createTitledBorder("Груз"));
        panelGoods.setBounds(x, y + 40, 955, 360);

        JLabel lblShipperName = new JLabel("Грузоотправитель");
        JLabel lblConsigneeName = new JLabel("Грузополучатель");
        JLabel lblContractName = new JLabel("Основание отпуска");
        JLabel lblLoadingAddress = new JLabel("Пункт погрузки");
        JLabel lblUnloadingAddress = new JLabel("Пункт разгрузки");
        JLabel lblReaddressing = new JLabel("Переадресовка");

        JLabel lblSaleAllowed = new JLabel("Отпуск разрешил");
        JLabel lblShipperPassed = new JLabel("Сдал грузоотправитель");
        JLabel lblTransportationReceive = new JLabel("Товар к перевозке принял");

        String[] documentTypes = {"По доверенности №", "По договору №"};
        JLabel lblWarrantDate = new JLabel("от");
        JLabel lblWarrantIssued = new JLabel("Выданной:");
        JLabel lblWarrantReceive = new JLabel("Принял грузополучатель");
        JLabel lblWarrantSealNumber = new JLabel("Номер пломбы");

        JLabel lblSealNumber = new JLabel("Номер пломбы");
        JLabel lblLoadingDoer = new JLabel("Исполнитель погрузки");
        JLabel lblLoadingMethod = new JLabel("Способ погрузки");
        JLabel lblSupportDocument = new JLabel("С товаром переданы документы");
        JLabel lblNote = new JLabel("Примечание");


        dpWarrantDate.setBounds(x + 835, y + 230, 100, 20);

        lblShipperName.setBounds(x + 10, y + 20, 140, 20);
        panelGoods.add(lblShipperName);
        cbShipperAddress.setBounds(x + 155, y + 20, 780, 20);
        panelGoods.add(cbShipperAddress);

        lblConsigneeName.setBounds(x + 10, y + 50, 140, 20);
        panelGoods.add(lblConsigneeName);

        cbConsigneeAddress.setBounds(x + 155, y + 50, 780, 20);
        panelGoods.add(cbConsigneeAddress);

        lblContractName.setBounds(x + 10, y + 80, 150, 20);
        panelGoods.add(lblContractName);
        cbContractId.setBounds(x + 155, y + 80, 780, 20);
        panelGoods.add(cbContractId);

        lblLoadingAddress.setBounds(x + 10, y + 110, 150, 20);
        panelGoods.add(lblLoadingAddress);
        cbLoadingAddress.setBounds(x + 155, y + 110, 780, 20);
        panelGoods.add(cbLoadingAddress);

        lblUnloadingAddress.setBounds(x + 10, y + 140, 150, 20);
        panelGoods.add(lblUnloadingAddress);

        cbUnloadingAddress.setBounds(x + 155, y + 140, 760, 20);
        panelGoods.add(cbUnloadingAddress);
        cbUnloadingAddress.setEditable(false);

        btnAddUnloadingAddress = new JButton();
        btnAddUnloadingAddress.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/add16.png", "Добавить адрес"));
        btnAddUnloadingAddress.setBounds(x + 915, y + 140, 20, 20);
        panelGoods.add(btnAddUnloadingAddress);

        lblReaddressing.setBounds(x + 10, y + 170, 150, 20);
        panelGoods.add(lblReaddressing);
        tfReaddressing.setBounds(x + 155, y + 170, 780, 20);
        panelGoods.add(tfReaddressing);

        lblSaleAllowed.setBounds(x + 10, y + 200, 150, 20);
        panelGoods.add(lblSaleAllowed);

        tfSaleAllowed.setBounds(x + 225, y + 200, 240, 20);
        panelGoods.add(tfSaleAllowed);
        btnSaleAllowed = new JButton();

        btnSaleAllowed.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/book.png", "Справочник"));
        btnSaleAllowed.setBounds(x + 465, y + 200, 20, 20);
        panelGoods.add(btnSaleAllowed);


        lblShipperPassed.setBounds(x + 500, y + 200, 180, 20);
        panelGoods.add(lblShipperPassed);

        tfShipperPassed.setBounds(x + 695, y + 200, 220, 20);
        panelGoods.add(tfShipperPassed);

        btnShipperPassed = new JButton();
        btnShipperPassed.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/book.png", "Справочник"));
        btnShipperPassed.setBounds(x + 915, y + 200, 20, 20);
        panelGoods.add(btnShipperPassed);

        lblTransportationReceive.setBounds(x + 10, y + 230, 210, 20);
        panelGoods.add(lblTransportationReceive);

        tfTransportationReceive.setBounds(x + 225, y + 230, 240, 20);
        panelGoods.add(tfTransportationReceive);

        btnTransportationReceive = new JButton();
        btnTransportationReceive.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/book.png", "Справочник"));
        btnTransportationReceive.setBounds(x + 465, y + 230, 20, 20);
        panelGoods.add(btnTransportationReceive);

        cbDocumentType.setBounds(x + 490, y + 230, 150, 20);
        cbDocumentType.setModel(new DefaultComboBoxModel(documentTypes));
        panelGoods.add(cbDocumentType);
        tfWarrantNumber.setBounds(x + 650, y + 230, 150, 20);
        panelGoods.add(tfWarrantNumber);

        lblWarrantDate.setBounds(x + 810, y + 230, 30, 20);
        panelGoods.add(lblWarrantDate);

        lblWarrantIssued.setBounds(x + 10, y + 260, 210, 20);
        panelGoods.add(lblWarrantIssued);
        tfWarrantIssued.setBounds(x + 155, y + 260, 130, 20);
        panelGoods.add(tfWarrantIssued);

        btnWarrantIssued = new JButton();
        btnWarrantIssued.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/book.png", "Справочник"));
        btnWarrantIssued.setBounds(x + 285, y + 260, 20, 20);
        panelGoods.add(btnWarrantIssued);


        lblWarrantReceive.setBounds(x + 310, y + 260, 180, 20);
        panelGoods.add(lblWarrantReceive);
        tfWarrantReceive.setBounds(x + 490, y + 260, 150, 20);
        panelGoods.add(tfWarrantReceive);

        lblWarrantSealNumber.setBounds(x + 650, y + 260, 110, 20);
        panelGoods.add(lblWarrantSealNumber);
        tfWarrantSealNumber.setBounds(x + 785, y + 260, 150, 20);
        panelGoods.add(tfWarrantSealNumber);

        lblSealNumber.setBounds(x + 10, y + 290, 120, 20);
        panelGoods.add(lblSealNumber);
        cbSealNumber.setBounds(x + 155, y + 290, 150, 20);
        panelGoods.add(cbSealNumber);

        lblLoadingDoer.setBounds(x + 310, y + 290, 170, 20);
        panelGoods.add(lblLoadingDoer);
        cbLoadingDoer.setBounds(x + 490, y + 290, 150, 20);
        panelGoods.add(cbLoadingDoer);
        lblLoadingMethod.setBounds(x + 650, y + 290, 120, 20);
        panelGoods.add(lblLoadingMethod);
        cbLoadingMethod.setBounds(x + 785, y + 290, 100, 20);
        panelGoods.add(cbLoadingMethod);

        lblNote.setBounds(x + 10, y + 320, 100, 20);
        panelGoods.add(lblNote);
        cbNote.setBounds(x + 155, y + 320, 315, 20);
        panelGoods.add(cbNote);

        btnNote = new JButton();
        btnNote.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/book.png", "Справочник"));
        btnNote.setBounds(x + 470, y + 320, 20, 20);
        panelGoods.add(btnNote);


        lblSupportDocument.setBounds(x + 530, y + 320, 250, 20);
        panelGoods.add(lblSupportDocument);
        tfSupportDocument.setBounds(x + 785, y + 320, 150, 20);
        panelGoods.add(tfSupportDocument);
        panelGoods.add(dpWarrantDate);

        dpWarrantDate.getEditor().setEditable(false);
        dpDocumentDate.getEditor().setEditable(false);

        dpWarrantDate.addActionListener(e -> {
            try {
                source.setWarrantDate(dpWarrantDate.getDate());
                System.out.println("Ввод даты доверенности");
            } catch (Exception ex) {
                System.out.println("Ошибка ввода даты доверенности");
            }
        });

  /*      dpDocumentDate.getEditor().addActionListener(e -> {
            try{
                source.setDocumentDate(dpDocumentDate.getDate());
                System.out.println("Ввода даты документа");
            }catch (Exception ex){
                System.out.println("Ошибка ввода даты документа");
            }
        });*/
    }

    private void setFocusEvents(Component component) {

        if (component instanceof JTextComponent) {
            component.addFocusListener(listener);
        }

        if (component instanceof JComboBox) {
            component.addFocusListener(listener);
        }

        if (component instanceof JXDatePicker) {
            ((JXDatePicker) component).getEditor().addFocusListener(listener);
        }

        if (component instanceof JPanel) {
            for (Component child : ((Container) component).getComponents()) {
                setFocusEvents(child);
            }
        }
    }

    /**
     * Метод заполняет комбики и текстовые поля грузополучателя и грузоотправителя
     * Документы в целом подразделяются на 2 типа, это отгрузка и возврат.
     * В связи с этим комбики и поля будут заполняться в этих случаях от разных источников-контрагентов
     *
     * @param document документ накладная
     * @param driving  документ путевой лист
     */
    private void presetContractors(SaleDocumentBase document, SaleDocumentDriving driving) {
        if (driving == null || document == null) {
            System.out.println("Нулевое значение, выходим");
            return;
        }

        // если документ позвратный
        if (SaleDocumentManager.isDocumentRefund(document)) {
            // Получаем сведения о контрагенте, грузоотправителе
            contractorSender = manager.getContractorByCode(document.getRecipientCode(), document.getDocumentNumber());
            // Получатель по накладной (код получаем из накладной)
            contractorReceiver = manager.getContractorByCode(-1, document.getDocumentNumber());
            // Получаем договора по контрагенту
            fillingComboBoxContractor(cbContractId, contractorSender.getContractList());
        } else {
            //Получаем сведения о контрагенте, грузополучателе
            contractorSender = manager.getContractorByCode(-1, document.getDocumentNumber());
            // Получатель по накладной (код получаем из накладной)
            contractorReceiver = manager.getContractorByCode(document.getRecipientCode(), document.getDocumentNumber());
            // Получаем договора по контрагенту
            fillingComboBoxContractor(cbContractId, contractorReceiver.getContractList());
        }

        // Грузоотправитель, наименование, адрес
        // tfShipperName.setText(contractorSender.getName().trim());
        presetContractorAddress(cbShipperAddress, contractorSender, source.getShipperId());
        // Грузоотправитель, адреса погрузки
        fillingComboBoxContractor(cbLoadingAddress, contractorSender.getAddressList());

        // Грузополучатель, наименование
        //tfConsigneeName.setText(contractorReceiver.getName().trim());

        // Грузополучатель, наименование, адрес
        presetContractorAddress(cbConsigneeAddress, contractorReceiver, source.getConsigneeId());

        // Грузополучатель, адреса разгрузки
        fillingComboBoxContractor(cbUnloadingAddress, contractorReceiver.getAddressList());

        presetComboBox(cbLoadingAddress, driving.getLoadingAddressId());
        presetComboBox(cbUnloadingAddress, driving.getUnloadingAddressId());
        // Костыль для неправильно заполненного справочника контрагента

        //Если договора нет
        if (document.getRecipientContractId() == null) {
            presetComboBox(cbContractId, 0);
        } else {
            presetComboBox(cbContractId, document.getRecipientContractId());
        }
    }

    @SuppressWarnings("unchecked")
    private void presetContractorAddress(final JComboBox<AddressBaseEntity> comboBox, final ContractorEntity contractorReceiver, int addressId) {

        comboBox.setModel(new DefaultComboBoxModel<>());
        //comboBox.setModel(new DefaultComboBoxModel(contractorReceiver.getAddressList().toArray()));

        for (AddressEntity address : contractorReceiver.getAddressList()) {
            comboBox.addItem(new AddressBaseEntity(address.getId(), contractorReceiver.getName() + ", " + address.getFullName()));
        }

        if (addressId > 0) {
            presetComboBoxCondition(comboBox, addressId, contractorReceiver.getLegalAddress().getId());
        } else {
            presetComboBox(comboBox, contractorReceiver.getLegalAddress().getId());
        }
    }

    @SuppressWarnings("unchecked")
    private void fillingComboBoxContractor(JComboBox comboBox, List<?> list) {
        comboBox.setModel(new DefaultComboBoxModel<>());
        comboBox.setModel(new DefaultComboBoxModel(list.toArray()));
    }

    private void presetComboBox(JComboBox comboBox, int id) {
        if (id == 0 || comboBox == null) {
            return;
        }

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i) instanceof BaseEntity) {
                BaseEntity entity = (BaseEntity) comboBox.getItemAt(i);
                if (entity.getId() == id) {
                    comboBox.setSelectedIndex(i);
                    System.out.println("Предустановка комбика " + i + " entity " + entity.getId());
                    return;
                }
            }
        }
    }

    private void presetComboBoxCondition(JComboBox comboBox, int ifId, int elseId) {
        if (ifId == 0 || comboBox == null) {
            return;
        }

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i) instanceof BaseEntity) {
                BaseEntity entity = (BaseEntity) comboBox.getItemAt(i);
                if (entity.getId() == ifId) {
                    comboBox.setSelectedIndex(i);
                    System.out.println("Предустановка комбика " + i + " entity " + entity.getId());
                    return;
                }
            }
        }

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i) instanceof BaseEntity) {
                BaseEntity entity = (BaseEntity) comboBox.getItemAt(i);
                if (entity.getId() == elseId) {
                    comboBox.setSelectedIndex(i);
                    System.out.println("Предустановка комбика альтернативная " + i + " entity " + entity.getId());
                    return;
                }
            }
        }
    }

    @Override
    public Object getSourceEntity() {

        source.setSaleDocumentId(saleDocument.getId());

        source.setDocumentNumber(tfDocumentNumber.getText().trim());
        source.setDocumentDate(dpDocumentDate.getDate());


        source.setShipperId(cbShipperAddress.getItemAt(cbShipperAddress.getSelectedIndex()).getId());
        source.setConsigneeId(cbConsigneeAddress.getItemAt(cbConsigneeAddress.getSelectedIndex()).getId());
        saleDocument.setRecipientContractId(((ContractEntity) cbContractId.getSelectedItem()).getId());
        source.setLoadingAddressId(((AddressEntity) cbLoadingAddress.getSelectedItem()).getId());
        source.setUnloadingAddressId(((AddressEntity) cbUnloadingAddress.getSelectedItem()).getId());
        source.setReaddressing(tfReaddressing.getText().trim());
        source.setSaleAllowed(tfSaleAllowed.getEditor().getItem().toString().trim());
        source.setShipperPassed(tfShipperPassed.getEditor().getItem().toString().trim());
        source.setTransportationReceive(tfTransportationReceive.getEditor().getItem().toString().trim());
        source.setSealNumberId(cbSealNumber.getSelectedIndex());
        source.setLoadingDoer(cbLoadingDoer.getSelectedIndex());
        source.setLoadingMethod(cbLoadingMethod.getSelectedIndex());
        source.setNote(cbNote.getEditor().getItem().toString().trim());
        source.setCarOwner(cbCarOwner.getEditor().getItem().toString().trim());
        source.setSupportDocument(tfSupportDocument.getText().trim());
        source.setCarNumber(tfCarNumber.getEditor().getItem().toString().trim());
        source.setCarTrailerNumber(tfCarTrailerNumber.getText().trim());
        source.setCarDriverName(tfCarDriverName.getEditor().getItem().toString().trim());
        source.setCarPayer(tfCarPayer.getText().trim());
        source.setCarCustomer(tfCarCustomer.getText().trim());
        source.setWarrantNumber(tfWarrantNumber.getText().trim());
        source.setDocumentType(cbDocumentType.getSelectedItem().toString());
        if (source.getWarrantNumber().trim().equals("")) {
            source.setWarrantDate(new Date());
        } else {
            source.setWarrantDate(dpWarrantDate.getDate());
        }

        source.setWarrantIssued(tfWarrantIssued.getEditor().getItem().toString().trim());


        source.setWarrantReceive(tfWarrantReceive.getText().trim());
        source.setWarrantSealNumber(tfWarrantSealNumber.getText().trim());

        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            System.out.println("Новый путевой лист");
            source = new SaleDocumentDriving();
            presetContractors(saleDocument, source);

            if (SaleDocumentManager.isDocumentRefund(saleDocument)) {
                source.setDocumentNumber(saleDocument.getDocumentNumber());
            } else {
                source.setDocumentNumber("");
            }

            source.setDocumentDate(new Date());

            source.setReaddressing("");

            source.setSaleAllowed("спец. по продажам ОС ");
            source.setShipperPassed("кладовщик ");
            source.setTransportationReceive("водитель ");

            source.setNote("");
            source.setSupportDocument("ТТН №" + saleDocument.getDocumentNumber());
            source.setCarNumber("");
            source.setCarTrailerNumber("");
            source.setCarDriverName("");
            source.setCarOwner("грузоотправитель");

            source.setCarPayer(contractorSender.getCodeUNN());
            source.setCarCustomer(contractorSender.getName() + "," + contractorSender.getLegalAddress().toString());

            source.setWarrantDate(new Date());
            source.setWarrantNumber("");
            source.setWarrantIssued("");
            source.setWarrantReceive("");
            source.setWarrantSealNumber("");

            if (saleDocument.getDocumentExport() < 1) {
                cbNote.getEditor().setItem("Франко-станция назначения");
            } else {
                source.setSaleAllowed("специалист ОВЭС ");
                source.setShipperPassed("Зав. складом г.п. ");
                cbNote.getEditor().setItem("Условие поставки ");
            }

        } else {
            source = (SaleDocumentDriving) object;
            presetContractors(saleDocument, source);
            cbNote.getEditor().setItem(source.getNote().trim());
        }

        defaultFillingData();
    }

    @Override
    public boolean verificationData() {
        return true;
    }

    @Override
    public void defaultFillingData() {

        // Номер путевого листа
        tfDocumentNumber.setText(source.getDocumentNumber().trim());
        // Дата путевого листа
        dpDocumentDate.setDate(source.getDocumentDate());
        tfReaddressing.setText(source.getReaddressing().trim());
        tfSaleAllowed.getEditor().setItem(source.getSaleAllowed().trim());
        tfShipperPassed.getEditor().setItem(source.getShipperPassed().trim());
        tfTransportationReceive.getEditor().setItem(source.getTransportationReceive().trim());

        tfCarNumber.getEditor().setItem(source.getCarNumber().trim());
        tfCarTrailerNumber.setText(source.getCarTrailerNumber().trim());
        tfCarDriverName.getEditor().setItem(source.getCarDriverName().trim());

        if (source.getCarPayer() != null) {
            tfCarPayer.setText(source.getCarPayer().trim());
        }
        if (source.getCarCustomer() != null) {
            tfCarCustomer.setText(source.getCarCustomer().trim());
        }

        tfSupportDocument.setText(source.getSupportDocument().trim());

        dpWarrantDate.setDate(source.getWarrantDate());
        tfWarrantNumber.setText(source.getWarrantNumber().trim());
        cbDocumentType.setSelectedItem(source.getDocumentTypeOrDefault());

        tfWarrantIssued.getEditor().setItem(source.getWarrantIssued().trim());

        tfWarrantReceive.setText(source.getWarrantReceive().trim());
        tfWarrantSealNumber.setText(source.getWarrantSealNumber().trim());

        cbSealNumber.setSelectedIndex(source.getSealNumberId());
        cbLoadingDoer.setSelectedIndex(source.getLoadingDoer());
        cbLoadingMethod.setSelectedIndex(source.getLoadingMethod());


        cbCarOwner.getEditor().setItem(source.getCarOwner().trim());
    }

    private class NewFocusListener implements FocusListener {

        Color normalColor;

        @Override
        public void focusGained(final FocusEvent e) {
            Component component = e.getComponent();
            normalColor = component.getBackground();
            if (component instanceof JTextComponent) {
                if (((JTextComponent) component).isEditable()) {
                    component.setBackground(COLOR_ENABLED);
                    // ((JTextComponent) component).selectAll();
                } else {
                    component.setBackground(COLOR_DISABLED);
                }
            } else {
                if (component instanceof JComboBox) {
                    if (((JComboBox) component).isEditable()) {
                        component.setBackground(COLOR_ENABLED);
                    } else {
                        component.setBackground(COLOR_DISABLED);
                    }
                } else if (((UCDatePicker) component).isEditable()) {
                    component.setBackground(COLOR_ENABLED);
                } else {
                    component.setBackground(COLOR_DISABLED);
                }
            }
        }

        @Override
        public void focusLost(final FocusEvent e) {
            Component component = e.getComponent();
            if (component.isEnabled()) {
                component.setBackground(Color.white);
            } else {
                component.setBackground(Color.LIGHT_GRAY);
            }
        }
    }
}
