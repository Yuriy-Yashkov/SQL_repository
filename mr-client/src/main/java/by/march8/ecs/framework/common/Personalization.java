package by.march8.ecs.framework.common;

import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.shell.model.WorkSession;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Класс добавляет персонализацию в интерфейс. Сохраняет пользовательские настройки ширины колонок
 * положение и размеры форм и диалогов и.т.д что будет реализовано. Расширяемо....
 * Created by Andy-book on 25.10.14.
 * Возможно добавление ситемы оформления пользовательского интерфейса LookAndFeel
 */
public class Personalization {

    // Текущий профиль
    private WorkSession session;

    /**
     * Конструктор персонализатора, аргументом передается объект текущей сессии пользователя
     */
    public Personalization(final WorkSession session) {
        this.session = session;
        prepareDirectories();
    }

    /**
     * Метод подготавливает дирректорию для сохранения профилей
     */
    @SuppressWarnings("all")
    private void prepareDirectories() {
        File workDir = new File(Settings.HOME_DIR);
        if (!workDir.exists()) {
            workDir.mkdir();
        }
        workDir = new File(Settings.TEMPORARY_DIR);
        if (!workDir.exists()) {
            workDir.mkdir();
        }

        workDir = new File(Settings.PROFILES_DIR);
        if (!workDir.exists()) {
            workDir.mkdir();
        }

        workDir = new File(Settings.ISSUANCE_DIR);
        if (!workDir.exists()) {
            workDir.mkdir();
        }

        workDir = new File(Settings.PROFILES_DIR + session.getAccount().getUserLogin().toLowerCase());
        if (!workDir.exists()) {
            workDir.mkdir();
        }
        session.setCurrentProfileDir(workDir.getAbsolutePath());
        System.out.println("Текущий профиль..." + session.getCurrentProfileDir());
    }

    /**Загрузка профиля для экземпляра с именем instanceName*/
    public void loadState(final String instanceName, final Object... object) {
        Properties properties = new Properties();
        File f = new File(session.getCurrentProfileDir() + "\\" + instanceName.toLowerCase().replace("\"", "") + ".config");
        if (!f.exists()) {
            return;
        }

        try {
            properties.load(new FileInputStream(f.getAbsolutePath()));

            for (Object item : object) {
                if (item instanceof JTable) {
                    //System.out.println("Передана таблица для загрузки");
                    loadJTableState(properties, (JTable) item);
                } else if (item instanceof JFrame) {
                    //System.out.println("Передана форма для загрузки");
                    loadJFrameState(properties, (JFrame) item);
                } else if (item instanceof JDialog) {
                    //System.out.println("Передан диалог для загрузки");
                    loadJDialogState(properties, (JDialog) item);
                } else if (item instanceof UCDatePeriodPicker) {
                    //System.out.println("Передан диалог для загрузки");
                    loadDatePeriodPickerState(properties, (UCDatePeriodPicker) item);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveState(final String instanceName, final Object... object) {
        Properties properties = new Properties();
        for (Object item : object) {
            if (item instanceof JTable) {
                //System.out.println("Передана таблица для сохранения состояния");
                saveJTableState(properties, (JTable) item);
            } else if (item instanceof JFrame) {
                //System.out.println("Передана форма для сохранения состояния");
                saveJFrameState(properties, (JFrame) item);
            } else if (item instanceof JDialog) {
                //System.out.println("Передан диалог для сохранения состояния");
                saveJDialogState(properties, (JDialog) item);
            } else if (item instanceof UCDatePeriodPicker) {
                //System.out.println("Передан диалог для сохранения состояния");
                saveDatePeriodPickerState(properties, (UCDatePeriodPicker) item);
            }
        }

        try {
            properties.store(new FileOutputStream(session.getCurrentProfileDir() + "\\" + instanceName.toLowerCase().replace("\"", "") + ".config"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadJTableState(final Properties profileProps, final JTable table) {
        String prefix = table.getName() + "_table_columnWidth";
        String[] columnWidths = (profileProps.getProperty(prefix, String.valueOf(50))).split(",");
        int columnCount = table.getColumnModel().getColumnCount();

        for (int i = 0; i < columnCount; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setResizable(true);
            if (columnWidths.length != columnCount) {
                column.setPreferredWidth(50);
            } else {
                column.setPreferredWidth(Integer.valueOf(columnWidths[i]));
            }
        }
    }

    private void saveJTableState(final Properties profileProps, final JTable table) {
        String prefix = table.getName() + "_table_columnWidth";
        StringBuilder sbWidth = new StringBuilder();
        int columnCount = table.getColumnModel().getColumnCount();

        for (int i = 0; i < columnCount; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            sbWidth.append(column.getWidth());
            if (i < columnCount - 1) {
                sbWidth.append(",");
            }
        }
        profileProps.setProperty(prefix, sbWidth.toString());
    }

    private void loadJFrameState(Properties properties, JFrame frame) {
        String prefix = frame.getName() + "_form_";

        Dimension oldSize = new Dimension(frame.getWidth(), frame.getHeight());
        Dimension newSize = new Dimension();
        newSize.width = Integer.valueOf(properties.getProperty(prefix + "width", String.valueOf(oldSize.width)));
        newSize.height = Integer.valueOf(properties.getProperty(prefix + "height", String.valueOf(oldSize.height)));
        frame.setPreferredSize(newSize);

        Point oldLocation = new Point(frame.getLocation());
        Point newLocation = new Point();
        newLocation.x = Integer.valueOf(properties.getProperty(prefix + "positionx", String.valueOf(oldLocation.x)));
        newLocation.y = Integer.valueOf(properties.getProperty(prefix + "positiony", String.valueOf(oldLocation.y)));
        frame.setLocation(newLocation);
    }

    private void saveJFrameState(final Properties profileProps, final JFrame frame) {
        String prefix = frame.getName() + "_form_";
        Dimension size = frame.getSize();
        Point location = frame.getLocation();

        profileProps.setProperty(prefix + "width", String.valueOf(size.width));
        profileProps.setProperty(prefix + "height", String.valueOf(size.height));
        profileProps.setProperty(prefix + "positionx", String.valueOf(location.x));
        profileProps.setProperty(prefix + "positiony", String.valueOf(location.y));
    }

    private void saveJDialogState(final Properties profileProps, final JDialog frame) {
        String prefix = frame.getName() + "_dialog_";
        Dimension size = frame.getSize();
        Point location = frame.getLocation();

        profileProps.setProperty(prefix + "width", String.valueOf(size.width));
        profileProps.setProperty(prefix + "height", String.valueOf(size.height));
        profileProps.setProperty(prefix + "positionx", String.valueOf(location.x));
        profileProps.setProperty(prefix + "positiony", String.valueOf(location.y));
    }

    private void loadJDialogState(Properties properties, JDialog frame) {
        String prefix = frame.getName() + "_dialog_";

        Dimension oldSize = new Dimension(frame.getWidth(), frame.getHeight());
        Dimension newSize = new Dimension();
        newSize.width = Integer.valueOf(properties.getProperty(prefix + "width", String.valueOf(oldSize.width)));
        newSize.height = Integer.valueOf(properties.getProperty(prefix + "height", String.valueOf(oldSize.height)));
        frame.setSize(newSize);

        Point oldLocation = new Point(frame.getLocation());
        Point newLocation = new Point();
        newLocation.x = Integer.valueOf(properties.getProperty(prefix + "positionx", String.valueOf(oldLocation.x)));
        newLocation.y = Integer.valueOf(properties.getProperty(prefix + "positiony", String.valueOf(oldLocation.y)));
        frame.setLocation(newLocation);
    }

    public void getPersonalSettings(AbstractFunctionalMode functionalMode, JComponent component) {
        loadState(functionalMode.getModeName(), component);
    }

    public void setPersonalSettings(AbstractFunctionalMode functionalMode, JComponent component) {
        saveState(functionalMode.getModeName(), component);
    }

    public void getPersonalSettings(String name, JComponent component) {
        loadState(name, component);
    }

    public void setPersonalSettings(String name, JComponent component) {
        saveState(name, component);
    }


    private void loadDatePeriodPickerState(final Properties properties, final UCDatePeriodPicker item) {
        String prefix = item.getName() + "_period_";
        Date dateBegin = DateUtils.getDateByStringValue(properties.getProperty(prefix + "begin", DateUtils.getNormalDateFormat(item.getDatePickerBegin())));
        Date dateEnd = DateUtils.getDateByStringValue(properties.getProperty(prefix + "end", DateUtils.getNormalDateFormat(item.getDatePickerEnd())));
        item.setDatePickerBegin(dateBegin);
        item.setDatePickerEnd(dateEnd);
    }

    private void saveDatePeriodPickerState(final Properties properties, final UCDatePeriodPicker item) {
        String prefix = item.getName() + "_period_";
        properties.setProperty(prefix + "begin", DateUtils.getNormalDateFormat(item.getDatePickerBegin()));
        properties.setProperty(prefix + "end", DateUtils.getNormalDateFormat(item.getDatePickerEnd()));
    }

}
