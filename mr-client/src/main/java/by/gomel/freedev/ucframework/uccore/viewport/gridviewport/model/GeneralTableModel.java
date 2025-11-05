package by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.ICustomCellEditor;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.IMultiSelector;
import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;
import by.march8.ecs.framework.common.comparators.SortBySequence;

import javax.swing.table.AbstractTableModel;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс модели прадставления. Формирует модель для JTable на основании Имени класса (Entity)
 * <p>
 * Created by Andy on 09.10.2014.
 */
@SuppressWarnings("all")
public class GeneralTableModel<T> extends AbstractTableModel implements IMultiSelector<T> {

    private final ArrayList<T> data;
    private final ArrayList<ColumnProperty> aColumnProperties = new ArrayList<>();
    protected ICustomCellEditor customCellEditor;
    private boolean isMultiselect = false;
    private boolean columnLimit = false;
    private List<Boolean> aChecker = null;
    private ArrayList<Integer> aCheckList;
    private boolean footerModel = false;

    public GeneralTableModel(final ArrayList<T> array, final Class<?> c) {
        data = array;
        reflectClass(c, false);
    }

    public GeneralTableModel(final ArrayList<T> array, final Class<?> c, boolean setMultiselect) {
        data = array;
        isMultiselect = setMultiselect;
        reflectClass(c, false);
        initChecker();
        initialCheckList();
    }

    public GeneralTableModel(final ArrayList<T> array, final Class<?> c, boolean setMultiselect, boolean columnLimit) {
        data = array;
        isMultiselect = setMultiselect;
        this.columnLimit = columnLimit;
        reflectClass(c, false);
        initChecker();
        initialCheckList();
    }

    public GeneralTableModel(final ArrayList<T> array, final Class<?> c, T item) {
        data = array;
        footerModel = true;
        isMultiselect = false;
        reflectClass(c, true);
    }

    private void initialCheckList() {
        aCheckList = new ArrayList<>();
    }

    private void initChecker() {
        if (!data.isEmpty()) {
            if (isMultiselect) {
                aChecker = Arrays.asList(new Boolean[data.size()]);
                for (int i = 0; i < aChecker.size(); i++) {
                    aChecker.set(i, isCheck(((BaseEntity) data.get(i)).getId()));
                }
            }
        }
    }


    private void setCheck(Object o, int id) {
        if ((Boolean) o == Boolean.FALSE) {
            for (int i = 0; i < aCheckList.size(); i++) {
                if (id == aCheckList.get(i)) {
                    aCheckList.remove(i);
                    return;
                }
            }
        } else {
            for (int i = 0; i < aCheckList.size(); i++) {
                if (id == aCheckList.get(i)) {
                    return;
                }
            }
            aCheckList.add(id);
        }
    }

    private boolean isCheck(int id) {
        for (int i = 0; i < aCheckList.size(); i++) {
            if (id == aCheckList.get(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void fireTableDataChanged() {
        initChecker();
        super.fireTableDataChanged();
    }

    public void setCustomCellEditor(ICustomCellEditor customCellEditor) {
        this.customCellEditor = customCellEditor;
    }

    private void reflectClass(Class<?> o, boolean isFooter) {
        try {
            final Field[] fields = o.getDeclaredFields();

            for (Field item : fields) {
                if (item.isAnnotationPresent(TableHeader.class)) {
                    // Поле аннотировано нашей аннотацией
                    final TableHeader titleColumn = item.getAnnotation(TableHeader.class);
                    item.setAccessible(true);
                    if ((!columnLimit) || ((columnLimit) && (titleColumn.weight() > 0))) {
                        // Получаем аргументы аннотации, и заполняем структуру ColumnProperty
                        String name = titleColumn.name();
                        int width = titleColumn.width();
                        int sequence = titleColumn.sequence();
                        // Объект колонки таблицы добавляем в массив
                        ColumnProperty property = new ColumnProperty(name, width, sequence, item);
                        // TODO Привести в божеский вид через кейсы
                        if (item.getType().toString().equals("int")) {
                            property.setClazz(Integer.class);
                        } else {
                            property.setClazz(item.getType());
                        }

                        if (isFooter) {
                            if (titleColumn.footer()) {
                                property.setFooter(true);
                            }
                        }
                        aColumnProperties.add(property);

                    }
                }
            }

            final ArrayList<Method> methods = new ArrayList<>();
            for (PropertyDescriptor propertyDescriptor :
                    Introspector.getBeanInfo(o).getPropertyDescriptors()) {
                methods.add(propertyDescriptor.getReadMethod());
            }
            // Получаем массив геттеров у класса
            // В цикле проверяем, аннотированы ли поля нашей аннотацией
            for (Method item : methods) {
                if ((item != null) && (item.isAnnotationPresent(TableHeader.class))) {
                    // Поле аннотировано нашей аннотацией
                    final TableHeader titleColumn = item.getAnnotation(TableHeader.class);
                    if ((!columnLimit) || ((columnLimit) && (titleColumn.weight() > 0))) {
                        // Получаем аргументы аннотации, и заполняем структуру ColumnProperty
                        String name = titleColumn.name();
                        int width = titleColumn.width();
                        int sequence = titleColumn.sequence();
                        // Объект колонки таблицы добавляем в массив
                        ColumnProperty property = new ColumnProperty(name, width, sequence, item);
                        // TODO Привести в божеский вид через кейсы
                        if (item.getReturnType().toString().equals("int")) {
                            property.setClazz(Integer.class);
                        } else {
                            property.setClazz(item.getReturnType());
                        }

                        if (isFooter) {
                            if (titleColumn.footer()) {
                                property.setFooter(true);
                            }
                        }
                        aColumnProperties.add(property);
                    }
                }
            }
            // ЕСЛИ НЕ ОБНАРУЖЕНО аннотированых полей и методов
            if (aColumnProperties.isEmpty()) {
                return;
            }

            // Сортировака полей по sequence
            Collections.sort(aColumnProperties, new SortBySequence());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        if (isMultiselect) {
            return aColumnProperties.size() + 1;
        } else {
            return aColumnProperties.size();
        }
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        Object result = null;
        if (isMultiselect) {
            if (columnIndex > 0) {
                result = getValueFromDataModel(rowIndex, columnIndex - 1);
            } else {
                if (rowIndex >= aChecker.size()) {
                    result = false;
                } else {
                    result = aChecker.get(rowIndex);
                }
            }
        } else {
            result = getValueFromDataModel(rowIndex, columnIndex);
        }

        return ValueFormatter.getObjectValue(result);
    }

    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        if (aChecker != null) {
            if (columnIndex == 0) {
                aChecker.set(rowIndex, (Boolean) aValue);
                setCheck(aValue, ((BaseEntity) data.get(rowIndex)).getId());
            }
        }

        if (customCellEditor != null) {
            if (isMultiselect) {
                if (columnIndex > 0) {
                    customCellEditor.setValueAt(columnIndex, data.get(rowIndex), aValue);
                }
            } else {
                customCellEditor.setValueAt(columnIndex, data.get(rowIndex), aValue);
            }
        }

        fireTableCellUpdated(rowIndex, columnIndex);

    }

    private Object getValueFromDataModel(final int rowIndex, final int columnIndex) {
        if ((data.isEmpty()) || (aColumnProperties.isEmpty())) {
            return null;
        }

        Object result = null;
        try {
            Object object = data.get(rowIndex);
            // Если аннотировано поле класса
            if (footerModel) {
                if (aColumnProperties.get(columnIndex).isFooter()) {
                    if (aColumnProperties.get(columnIndex).getViewPort() == ElementType.FIELD) {
                        result = aColumnProperties.get(columnIndex).getField().get(object);
                    } else {
                        result = aColumnProperties.get(columnIndex).getMethod().invoke(object);
                    }
                } else {
                    return "";
                }
            } else {
                if (aColumnProperties.get(columnIndex).getViewPort() == ElementType.FIELD) {
                    result = aColumnProperties.get(columnIndex).getField().get(object);
                } else {
                    result = aColumnProperties.get(columnIndex).getMethod().invoke(object);
                }
            }

        } catch (Exception e) {
            // e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getColumnName(final int column) {
        if (isMultiselect) {
            if (column == 0) {
                return "S";
            } else {
                return aColumnProperties.get(column - 1).getName();
            }
        } else {
            return aColumnProperties.get(column).getName();
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        if (isMultiselect) {
            if (columnIndex == 0) {
                return Boolean.class;
            } else {
                return aColumnProperties.get(columnIndex - 1).getClazz();
            }
        } else {
            if (footerModel) {
                if (aColumnProperties.get(columnIndex).isFooter()) {
                    return aColumnProperties.get(columnIndex).getClazz();
                } else {
                    return String.class;
                }
            } else {
                return aColumnProperties.get(columnIndex).getClazz();
            }
        }

    }

    public ArrayList<ColumnProperty> getColumnProperties() {
        return aColumnProperties;
    }

    public ArrayList<T> getDataModel() {
        return data;
    }

    @Override
    public boolean isCellEditable(int row, int column) {

        if ((column == 0) && (isMultiselect)) {
            return true;
        }

        if (customCellEditor != null) {
            if ((column > 0) && (isMultiselect)) {
                return customCellEditor.isCellEditable(column - 1);
            }
            if (!isMultiselect) {
                return customCellEditor.isCellEditable(column);
            }
        }
        return false;
    }


    @Override
    public void preset(final ArrayList<T> presetCollection) {
        aCheckList.clear();
        for (Object o : presetCollection) {
            BaseEntity item = (BaseEntity) o;
            aCheckList.add(item.getId());
        }
        initChecker();
    }

    @Override
    public Set<T> getSelectedItems() {
        ArrayList<T> tempList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Boolean check = aChecker.get(i);
            if (check) {
                tempList.add(data.get(i));
            }
        }
        return new HashSet<T>(tempList);
    }

    public void selectAll(boolean clear) {
        if (isMultiselect) {
            if (aChecker == null) {
                initChecker();
                initialCheckList();
            }
            for (int i = 0; i < data.size(); i++) {
                BaseEntity e = (BaseEntity) data.get(i);
                if (aChecker.get(i).booleanValue()) {
                    aChecker.set(i, false);
                    setCheck(false, e.getId());
                } else {
                    aChecker.set(i, !clear);
                    setCheck(!clear, e.getId());
                }
            }
        }
    }

    public void selectCheck(int id, boolean b) {
        if (isMultiselect) {
            for (int i = 0; i < data.size(); i++) {
                BaseEntity e = (BaseEntity) data.get(i);
                if (e.getId() == id) {


                    aChecker.set(i, true);
                    setCheck(true, e.getId());
                }
            }
        }
    }
}
