package by.march8.ecs.application.modules.references.product.subeditor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucdao.PostgresDataSource;
import by.gomel.freedev.ucframework.ucdao.SqlServerDataSources;
import by.gomel.freedev.ucframework.ucdao.SqlServerMarch8DS;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.focus.FocusProcessing;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.product.editor.ModelSizeEditor;
import by.march8.ecs.application.modules.references.product.utils.UtilProduct;
import by.march8.ecs.framework.sdk.reference.Reference;
import by.march8.ecs.framework.sdk.reference.abstracts.SubReferences;
import by.march8.entities.product.ModelProduct;
import by.march8.entities.product.ModelSizeChart;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

/**
 * Created by lidashka.
 */
public class ModelSubSize extends SubReferences {
    private ModelProduct source;

    public ModelSubSize(final IReference reference, final Container container) {
        super(reference, container, ModelSizeChart.class);
        initSizeColumn();
        initComponent();
        editPane = new ModelSizeEditor(reference, true);
        editPane.setRight(reference.getRight());
        FocusProcessing fp = new FocusProcessing();
        fp.setBorderColor(editPane);
        updateContent();
    }

    private void initSizeColumn() {
        TableColumnModel columnModel = gridViewPort.getTable().getColumnModel();
        TableColumn column = columnModel.getColumn(0);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column = columnModel.getColumn(1);
        column.setMinWidth(0);
        column.setMaxWidth(0);
    }

    public void initComponent() {
        final JButton btnCopy = new JButton();
        btnCopy.setIcon(new ImageIcon(MainController.getRunPath() + "/res/copy24.png", "Копировать"));
        btnCopy.setToolTipText("Новый размер модели изделия на основании выбранного");

        final JButton btnCheckList = new JButton();
        btnCheckList.setIcon(new ImageIcon(MainController.getRunPath() + "/res/checklist.png", "Копировать"));
        btnCheckList.setToolTipText("Выбрать из списка размеров");

        toolBar.add(btnCopy);
        toolBar.add(btnCheckList);
        toolBar.getBtnReport().setVisible(false);

        btnCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                createRecord();
            }
        });
        btnCheckList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                checkListRecord();
            }
        });
    }

    private void createRecord() {
        if (gridViewPort.getDataModel().size() == 0) {
            return;
        }

        ModelSizeChart selectedSize = (ModelSizeChart) gridViewPort.getSelectedItem();

        if (selectedSize == null) {
            return;
        }

        editPane.setSourceEntity(UtilProduct.createCopySize(selectedSize, ""));

        final BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW_ID);

        editor.setEditorPane(editPane);

        if (editor.showModal()) {
            gridViewPort.getDataModel().add(editPane.getSourceEntity());
            updateEntity(editPane.getSourceEntity());
            gridViewPort.updateViewPort();
            toolBar.updateButton(gridViewPort.getDataModel().size());
        }
        editor.dispose();
    }

    private void checkListRecord() {
        /*
        FrameViewPort viewPort = new FrameViewPort(controller, MarchWindowType.PICKCHECK);
        viewPort.getFrameControl().setTitleFrame("Тестовый вьюпорт");
        viewPort.setGridViewPort(new GridViewPort(ModelSizeChart.class, true));
        viewPort.getFrameRegion().getToolBar().clearToolBar();

        ArrayList<Object> data = viewPort.getGridViewPort().getDataModel();


        //QueryBuilder queryBuilder;

       // queryBuilder = new QueryBuilder(ModelSizeChart.class);
       // queryBuilder.addCriteria(new CriteriaItem(127, "Id", "="));

        java.util.List<Object> result = null;

        float height = 192;

        final EntityManager manager = getEntityManagerByEntity(ModelSizeChart.class);
        try {
            final Query query = manager
                    .createQuery("SELECT y FROM ModelSizeChart y where height = :height");

            query.setParameter("height", height);
            result = query.getResultList();
        } catch (final Exception e) {
            MainController.exception(e, "Ошибка проверки наличия пряжи в БД для sar=" + height);
        } finally {
            manager.close();
        }

        System.err.println("ПОЛУЧЕНИЕ ДАННЫХ");
        final DaoFactory factory = DaoFactory.getInstance();
        final ICommonDaoThread dao = factory.getCommonDaoThread();
        try {
            data.clear();
          //  data.addAll(dao.getAllEntityThread(MarchReferencesType.MODEL_SIZE.getClassifierClass()));
           // data.addAll(dao.getAllEntityByStringQueryThread(ModelSizeChart.class, queryBuilder.getQuery()));

           // ArrayList<Object> objectsList=ReferencesDao.loadDataForFile(Document.class,"Select c from Document c");

            //objectsList=ReferencesDao.loadDataForFile(ModelSizeChart.class, "Select c from ModelSizeChart c ");


            data.addAll(ReferencesDao.loadDataForFile(ModelSizeChart.class, "Select c from ModelSizeChart c "));

        } catch (final Exception e) {
            e.printStackTrace();
        }
        viewPort.updateContent();

        viewPort.getFrameControl().showFrame();

        if (viewPort.getFrameControl().showModalFrame()) {
            IMultiSelector multiSelector = viewPort.getGridViewPort().getMultiSelector();
            Set<Object> set = multiSelector.getSelectedItems();
            for (Object o : set){
                ModelSizeChart empl = (ModelSizeChart)o ;
                System.out.println(String.format("%s, %s, %s",empl.getHeight(), empl.getSize(), empl.getPrintsize()));
            }
        }

        private GridViewPort gridViewPort;
        private FrameViewPort viewPort;

        final Reference ref = new Reference(controller,
                MarchReferencesType.MODEL_SIZE,
                MarchWindowType.PICKCHECK);

        try {
            ArrayList<Object> objectsList= ReferencesDao.loadDataForFile(ModelSizeChart.class, "Select * from MOD_SIZE_CHART ");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Set<Object> set = ref.selectFromReference() ;
        for (Object o : set){
            ModelSizeChart empl = (ModelSizeChart)o ;
            System.out.println(String.format("%s, %s, %s",empl.getHeight(), empl.getSize(), empl.getPrintsize()));
        }

        */

        final Reference ref = new Reference(controller,
                MarchReferencesType.MODEL_SIZE,
                MarchWindowType.PICKCHECK);

        Set<Object> set = ref.selectFromReference();

        if (set == null) {
            return;
        }

        if (JOptionPane.showOptionDialog(
                null,
                "Создать размеры модели изделия на основании выбранных",
                "Новые размеры модели изделия",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Да", "Нет"},
                "Да") == JOptionPane.YES_OPTION) {

            for (Object o : set) {
                ModelSizeChart newsize = UtilProduct.createCopySize((ModelSizeChart) o, "");
                gridViewPort.getDataModel().add(newsize);
                updateEntity(newsize);
            }

            gridViewPort.updateViewPort();
            toolBar.updateButton(gridViewPort.getDataModel().size());
        }

    }

    protected EntityManager getEntityManagerByEntity(final Class<?> entity) {
        if (entity.isAnnotationPresent(RedirectToEntityManager.class)) {
            final RedirectToEntityManager redirect = entity
                    .getAnnotation(RedirectToEntityManager.class);
            if (redirect.redirectTo() == MarchDataSourceEnum.DS_SQLSERVER)
                return SqlServerDataSources.getEntityManager();
            if (redirect.redirectTo() == MarchDataSourceEnum.DS_SQLMARCH8)
                return SqlServerMarch8DS.getEntityManager();
            else
                return PostgresDataSource.getEntityManager();

        }

        return null;
    }

    @Override
    public void setSourceEntity(Object object) {
        source = (ModelProduct) object;
    }

    @Override
    public void updateEntity(Object object) {
        ((ModelSizeChart) object).setModel(source);
    }
}
