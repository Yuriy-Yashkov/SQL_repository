package by.march8.ecs.application.modules.marketing.mode;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.model.PostgresPlanningDocument;
import dept.production.planning.PlanPDB;

import java.awt.*;
import java.util.List;

public class PlanningDocumentSelectorDialog extends BasePickDialog {

    private GridViewPort<PostgresPlanningDocument> gvItem;
    private List<PostgresPlanningDocument> data;

    private UCDatePeriodPicker datePeriodPicker;
    private PlanPDB pdb;

    public PlanningDocumentSelectorDialog(MainController controller) {
        super(controller);
        setTitle("Выберите из списка план производства");

        init();
        initEvents();
    }

    private void initEvents() {
        setButtonAction(new ButtonActionAdapter() {
            @Override
            public boolean canSave() {
                return gvItem.getSelectedItem() != null;
            }

            @Override
            public boolean canCancel() {
                return true;
            }
        });

        datePeriodPicker.addOnChangeAction(a -> {
            updateContent();
        });
    }

    private void updateContent() {
        data.clear();
        List<PostgresPlanningDocument> list = pdb.getPlanningDocumentsByPeriod(datePeriodPicker.getTimeLimitPeriod());
        if (list != null) {
            data.addAll(list);
        }
        gvItem.updateViewPort();
    }

    private void init() {

        pdb = new PlanPDB();

        setFrameSize(new Dimension(400, 450));
        gvItem = new GridViewPort<>(PostgresPlanningDocument.class, false);
        data = gvItem.getDataModel();

        datePeriodPicker = new UCDatePeriodPicker();
        datePeriodPicker.setName("datePeriodPicker");
        datePeriodPicker.setDatePickerBegin(DateUtils.getFirstDay(datePeriodPicker.getDatePickerEnd()));
        datePeriodPicker.setEditable(true);

        getToolBar().getBtnNewItem().setVisible(false);
        getToolBar().getBtnEditItem().setVisible(false);
        getToolBar().getBtnDeleteItem().setVisible(false);
        getToolBar().add(datePeriodPicker);
        getCenterContentPanel().add(gvItem);
    }

    public PostgresPlanningDocument selectPlanningDocument() {
        updateContent();

        if (showModalFrame()) {
            return gvItem.getSelectedItem();
        }
        return null;
    }
}
