package by.march8.ecs.application.modules.packing.reception;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.entities.unknowns.CipherCode;

import javax.swing.*;
import java.util.ArrayList;

//import by.march8.ecs.framework.hardware.scanner.Cipher;

/**
 * Функциональный режим рабочего места приемки на упаковку
 * Created by Andy on 19.03.2015.
 */
public class ReceptionPackingFunctionalMode extends AbstractFunctionalMode {

    private RightEnum right;
    private JButton btnScanner;
    private ArrayList<Object> data;


    public ReceptionPackingFunctionalMode(MainController mainController) {
        controller = mainController;
        modeName = "Приемка продукции на упаковку";

        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        right = controller.getRight(modeName);

        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        btnScanner = new JButton();
        btnScanner.setIcon(new ImageIcon(MainController.getRunPath() + "/res/scanner.png", "Find"));
        btnScanner.setToolTipText("Принять данные со сканера");

        toolBar.registerEvents(this);
        toolBar.setRight(right);
        toolBar.add(btnScanner);
        toolBar.getBtnReport().setVisible(false);

        gridViewPort = new GridViewPort(CipherCode.class, false);
        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);

        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(int rowIndex, int columnIndex, Object object) {
                if (right == RightEnum.WRITE) {
                    editRecord();
                }
            }
        });

        initEvents();

        updateContent();
        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().showFrame();
    }

    private void initEvents() {
        btnScanner.addActionListener(e -> {
   /*         Cipher cipher = new Cipher();

            String[] res = cipher.getCodeListExternal(new Breaker());
            if (res != null) {
                int id = -1 ;
                data.clear();
                for (String s : res) {
                    id++ ;
                    data.add(new CipherCode(id,s)) ;
                }
                data.remove(0);
                frameViewPort.updateContent();
            }
            cipher.dispose() ;*/
        });
    }

    @Override
    public void addRecord() {

    }

    @Override
    public void editRecord() {

    }

    @Override
    public void deleteRecord() {

    }

    @Override
    public void updateContent() {

    }
}
