package by.march8.tasks.mnsati.logic;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.ecs.framework.common.Settings;
import by.march8.entities.warehouse.SaleDocumentEntity;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 06.06.2016.
 */
public class IssuanceFactory {

    private static IssuanceFactory instance = null;

    private IssuanceFactory() {

    }

    public static IssuanceFactory getInstance() {
        if (instance == null) {
            instance = new IssuanceFactory();
        }
        return instance;
    }


    public void createIssuanceByDocumentNumber(String documentNumber) {
        SaleDocumentEntity entity = getDocumentEntityByDocumentNumber(documentNumber);
        if (entity != null) {
            IssuanceCreator issuanceCreator = new IssuanceCreator(Settings.ISSUANCE_DIR);
            try {
                issuanceCreator.createIssuance(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Ошибка получения данных для документа [" + documentNumber.trim() + "]");
        }
    }


    public void createIssuanceByDocumentNumberDialog(String documentNumber) {

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(Settings.LAST_DIALOG_DIR));
        chooser.setDialogTitle("Выбор каталога для сохранения документа");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            SaleDocumentEntity entity = getDocumentEntityByDocumentNumber(documentNumber);
            if (entity != null) {
                IssuanceCreator issuanceCreator = new IssuanceCreator(chooser.getSelectedFile().toString() + "/");
                try {
                    issuanceCreator.createIssuance(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Ошибка получения данных для документа [" + documentNumber.trim() + "]");
            }
            Settings.LAST_DIALOG_DIR = chooser.getSelectedFile().toString();
            //System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            //System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }
    }


    public void createObjectFromIssuance(String documentNumber) {
        IssuanceCreator issuanceCreator = new IssuanceCreator(Settings.ISSUANCE_DIR);
        issuanceCreator.createObject();
    }

    private SaleDocumentEntity getDocumentEntityByDocumentNumber(String documentNumber) {
        List<QueryProperty> criteria = new ArrayList<QueryProperty>();
        criteria.add(new QueryProperty("number", documentNumber));

        DaoFactory<SaleDocumentEntity> factoryMarch = DaoFactory.getInstance();
        IGenericDao<SaleDocumentEntity> daoMarch = factoryMarch.getGenericDao();
        try {
            return daoMarch.getEntityByNamedQuery(SaleDocumentEntity.class, "SaleDocumentEntity.findByNumber", criteria);

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
