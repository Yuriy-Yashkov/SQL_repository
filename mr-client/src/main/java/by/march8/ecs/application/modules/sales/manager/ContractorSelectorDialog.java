package by.march8.ecs.application.modules.sales.manager;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.general.ContractorDAO;
import by.march8.entities.readonly.ContractorEntityView;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.List;

public class ContractorSelectorDialog extends BasePickDialog {

    private GridViewPort<ContractorEntityView> gvItem;
    private List<ContractorEntityView> data;
    private TableRowSorter<TableModel> sorter;

    private JPanel pSearch;
    private JTextField tfSearchText;
    private JButton btnDoSearch;


    public ContractorSelectorDialog(MainController controller) {
        super(controller);
        setTitle("Выберите из списка контрагента");

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

        btnDoSearch.addActionListener(a -> {
            String text = tfSearchText.getText().trim();
            if (text.length() == 0) {
                sorter.setRowFilter(null);
            } else {
                try {
                    sorter.setRowFilter(RowFilter.regexFilter("(?iu)" + text));
                    if (gvItem.getTable().getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "По заданному запросу ничего не найдено", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tfSearchText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnDoSearch.doClick();
                }
            }
        });

/*        gvItem.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });*/
    }

    private void init() {
        setFrameSize(new Dimension(500, 450));
        gvItem = new GridViewPort<>(ContractorEntityView.class, false);
        data = gvItem.getDataModel();

        pSearch = new JPanel(new MigLayout());
        tfSearchText = new JTextField();
        btnDoSearch = new JButton("Поиск");

        pSearch.add(new JLabel("Ключевые слова в названии"), "width 300:20:300, height 20:20, span 2, wrap");
        pSearch.add(tfSearchText, "width 300:20:300, height 20:20");
        pSearch.add(btnDoSearch, "width 80:20:80, height 20:20");


        getTopContentPanel().remove(getToolBar());
        getTopContentPanel().add(pSearch);
        getToolBar().setVisible(false);
        getCenterContentPanel().add(gvItem);

        sorter = new TableRowSorter<TableModel>(gvItem.getTableModel()) {
            public Comparator<?> getComparator(int column) {
                if (column == 0) {
                    return new Comparator<String>() {
                        public int compare(String s1, String s2) {
                            return Integer.parseInt(s1) - Integer.parseInt(s2);
                        }
                    };
                }
                return super.getComparator(column);
            }
        };
        gvItem.getTable().setRowSorter(sorter);
    }

    public ContractorEntityView selectContractor() {
        data.clear();
        List<ContractorEntityView> list = ContractorDAO.getAllContractors();
        if (list != null) {
            data.addAll(list);
        }
        gvItem.updateViewPort();
        if (showModalFrame()) {
            return gvItem.getSelectedItem();
        }
        return null;
    }
}