package by.gomel.freedev.ucframework.ucdao.model;

import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.march8.api.BaseEntity;
import by.march8.api.utils.DateUtils;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author Andy 13.11.2014.
 */
public class QueryBuilder {

    private String entityAlias;
    private String queryHeader;
    private String order = null;

    private ArrayList<CriteriaItem> criteriaList = new ArrayList<CriteriaItem>();


    public QueryBuilder(final Class<?> aClass) {
        //final Class<?> aClass1 = aClass;
        queryHeader = generateQueryHeader(aClass);
        //System.out.println(queryHeader);
    }

    private String generateQueryHeader(Class<?> aClass) {
        final String entityName = aClass.getSimpleName();
        entityAlias = aClass.getSimpleName().toLowerCase().substring(0, 1);

        return String.format("SELECT %s FROM %s AS %s", entityAlias, entityName, entityAlias);
    }

    public void setOrderBy(String... orderString) {
        order = " ORDER BY ";
        for (String s : orderString) {
            order += entityAlias + "." + s + ", ";
        }
        order = order.substring(0, order.length() - 2);
    }

    public String getQuery() {
        StringBuilder result = new StringBuilder();
        result.append(queryHeader);
        boolean isSetCondition = false;
        ArrayList<String> condition = new ArrayList<String>();
        // for(SelectionCriteria item: criteriaList){        // СОздание массива условий
        for (CriteriaItem item : criteriaList) {
            // Критерий найден, добавляем  к билдеру строку WHERE, и ставим флаг критерия, разово
            String conditionPart = getConditionPart(item);
            if (!conditionPart.equals("")) {
                if (!isSetCondition) {
                    result.append(" WHERE ");
                    isSetCondition = true;
                }
                condition.add(conditionPart);
            }
        }

        // Формируется конечный запрос к БД
        for (int i = 0; i < condition.size(); i++) {
            result.append(condition.get(i));
            if (i < condition.size() - 1) {
                result.append(" AND ");
            }
        }

        if (order != null) {
            result.append(order);
        }

        return result.toString();
    }

    private String getConditionPart(CriteriaItem item) {
        String value = "";
        if (item == null) {
            return "";
        }

        JComponent component = item.getComponent();
        if (component == null) {
            if (item.getSign().equals("LIKE")) {
                return String.format(" lower(%s.%s) LIKE lower(", entityAlias, item.getField()) + "\'%" + item.getValue() + "%\')";
            } else if (item.getSign().equals("date equals")) {//return String.format(" %s.%s = CONVERT(DATETIME,'%s', 102)", entityAlias, item.getField(), item.getValue());
                return String.format(" %s.%s = '%s'", entityAlias, item.getField(), item.getValue());
            } else {
                if (item.isValueIsString()) {
                    return String.format("%s.%s%s'%s'", entityAlias, item.getField(), item.getSign(), item.getValue());
                } else {
                    return String.format("%s.%s%s%s", entityAlias, item.getField(), item.getSign(), item.getValue());
                }
            }
        } else if (component instanceof UCDatePeriodPicker) {
            UCDatePeriodPicker picker = (UCDatePeriodPicker) component;
            return String.format("%s.%s BETWEEN CONVERT(DATETIME,'%s', 102) AND CONVERT(DATETIME,'%s', 102)", entityAlias, item.getField(),
                    DateUtils.dateFistToSQLTimestamp(picker.getDatePickerBegin()),
                    DateUtils.dateLastToSQLTimestamp(picker.getDatePickerEnd()));
        } else {
            if (component instanceof JTextField) {
                value = ((JTextField) component).getText();
                if (value.trim().equals("")) {
                    return "";
                }
            } else if (component instanceof ComboBoxPanel) {
                BaseEntity entity = (BaseEntity) ((ComboBoxPanel) component).getSelectedItem();
                if (entity == null) {
                    return "";
                }
                value = String.valueOf(entity.getId());
            } else if (component instanceof ComboBoxPanel) {
                BaseEntity entity = (BaseEntity) ((ComboBoxPanel) component).getSelectedItem();
                if (entity == null) {
                    return "";
                }
                value = String.valueOf(entity.getId());
            }
            return String.format("%s.%s%s%s", entityAlias, item.getField(), item.getSign(), value);
        }
    }

    public void updateCriteria(String key, int value) {
        for (CriteriaItem item : criteriaList) {
            if (item.getField().equals(key)) {
                item.setValue(String.valueOf(value));
                return;
            }
        }
    }

    public void addCriteria(CriteriaItem criteria) {

        if (criteria.getField() == null) {
            return;
        }

        if (criteria.getField().trim().equals("")) {
            return;
        }

        criteriaList.add(criteria);
    }
}
