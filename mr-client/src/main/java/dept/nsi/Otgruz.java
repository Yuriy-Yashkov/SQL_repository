package dept.nsi;

import workDB.DBF;
import workDB.PDB;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author lidashka
 */
public class Otgruz {
    ArrayList list = new ArrayList();
    PDB pdb = null;
    boolean rezalt = false;

    public Otgruz(String url) {
        DBF dbf = new DBF();
        list = dbf.readDBF(url);
    }

    public void update() {
        try {
            MaskFormatter mask = new MaskFormatter("##-####");
            mask.setPlaceholderCharacter('0');

            String dao = ((Object[]) list.get(0))[0].toString();
            JFormattedTextField format = new javax.swing.JFormattedTextField(mask);
            format.setSelectionStart(0);
            format.setText(dao.substring(dao.length() - 2, dao.length()) + "-"
                    + new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime()));

            if (JOptionPane.showOptionDialog(null, format, "Обновить за... ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Отмена"}, format) == JOptionPane.YES_OPTION) {
                if (checkDate(format.getText().trim())) {
                    pdb = new PDB();
                    rezalt = pdb.insertOtgruz(format.getText().trim(), list);

                    if (rezalt)
                        JOptionPane.showMessageDialog(null, "Справочник отгрузки за " + format.getText().trim() + " успешно обновлён!  ", "Обновление справочника", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(null, "Справочник отгрузки за " + format.getText().trim() + " не обновлён! ", "Ошибка обновления", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ошибка: " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            pdb.disConn();
        }
    }

    protected boolean checkDate(String chDate) {
        Calendar cal = new GregorianCalendar();
        cal.setLenient(false);
        cal.clear();
        try {
            int d = Integer.parseInt("01");
            int m = Integer.parseInt(chDate.substring(0, 2));
            int y = Integer.parseInt(chDate.substring(4));
            cal.set(y, m - 1, d);
            java.util.Date dt = cal.getTime();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка преобразования даты: " + chDate + " " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
