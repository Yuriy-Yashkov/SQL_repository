package by.march8.ecs.application.modules.production.editor;

import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.production.mode.RouteSheetMode;
import by.march8.entities.production.RouteSheet;
import by.march8.entities.production.RouteSheetSelected;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lidashka on 17.10.2018.
 */
public class RouteSheetEditor extends EditingPane {
    private UCTextFieldPanel<String> textFieldPanel;

    private Set<RouteSheet> routeSheets = null;
    private Set<RouteSheetSelected> routeSheetSelecteds = null;
    private RouteSheetSelected routeSheetSelected = null;

    private RouteSheetMode sheetMode;

    public RouteSheetEditor(MainController mainController, Date dateBegin, Date dateEnd, List<Integer> idList) {
        controller = mainController;
        setLayout(new MigLayout());
        setPreferredSize(new Dimension(350, 80));

        sheetMode = new RouteSheetMode(controller, dateBegin, dateEnd, idList);

        init();
        initEvents();
    }


    private void initEvents() {
        textFieldPanel.addButtonSelectActionListener(a -> {

            Object selectedItem = sheetMode.showSelectModal(null);

            if (selectedItem != null) {
                routeSheets = (Set<RouteSheet>) selectedItem;
                prepareItemInformation(routeSheets);
            }
        });
    }

    private void prepareItemInformation(Set<RouteSheet> sheets) {
        if (sheets != null) {
            routeSheetSelecteds = new HashSet<>();

            for (RouteSheet sheet : sheets) {
                if (sheet.getId() > 0 && sheet.getNomer() > 0) {
                    textFieldPanel.getEditor().setText(textFieldPanel.getEditor().getText() + " " + String.valueOf(sheet.getNomer()));

                    routeSheetSelected = new RouteSheetSelected();
                    routeSheetSelected.setKod(sheet);
                    routeSheetSelected.setDate(DateUtils.getDateNow());
                    routeSheetSelected.setType(0);

                    routeSheetSelecteds.add(routeSheetSelected);
                } else {
                    textFieldPanel.getEditor().setText("НЕТ ДАННЫХ");
                }
            }

        } else {
            textFieldPanel.getEditor().setText("НЕТ ДАННЫХ");
        }
    }

    private void init() {
        textFieldPanel = new UCTextFieldPanel<>();
        textFieldPanel.getEditor().setEditable(false);

        add(new JLabel("№:   "));
        add(textFieldPanel, "height 20:20, width 280:20:280,wrap");
    }

    @Override
    public Object getSourceEntity() {
        return routeSheetSelecteds;
    }

    @Override
    public void setSourceEntity(Object object) {

    }

    @Override
    public boolean verificationData() {
        if (routeSheets == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо выбрать маршрутный лист", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        /*
        if (routeSheet.getId() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо выбрать маршрутный лист", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }*/

        //routeSheetSelected = new RouteSheetSelected();
        //routeSheetSelected.getKod(routeSheet);

        return true;
    }
}
