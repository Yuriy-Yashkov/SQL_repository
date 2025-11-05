/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.component;

import dept.MyReportsModule;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author user
 */
@SuppressWarnings({"serial"})
public class MyButton extends JButton {

    private String labelButton;
    private ImageIcon iconka;

    public MyButton(String label) {
        String path = MyReportsModule.progPath + "/Img/24x24/";
        if (label.toLowerCase().equals("ok") || label.equals("Применить")) {
            iconka = new ImageIcon(path + "Apply.png");
        }
        if (label.equals("Отмена") || label.equals("Закрыть") || label.equals("Выход")) {
            iconka = new ImageIcon(path + "Exit.png");
        }
        if (label.equals("Печать") || label.equals("Отчет") || label.equals("Сформировать")) {
            iconka = new ImageIcon(path + "Report.png");
        }
        if (label.equals("Настройки") || label.equals("Установки")) {
            iconka = new ImageIcon(path + "Pinion.png");
        }
        if (label.equals("Фильтр/Изменить") || label.equals("Фильтр")) {
            iconka = new ImageIcon(path + "Filter.png");
        }
        if (label.toLowerCase().equals("записать") || label.toLowerCase().equals("сохранить")) {
            iconka = new ImageIcon(path + "Save.png");
        }
        if (label.toLowerCase().equals("изменить") || label.toLowerCase().equals("редактировать")) {
            iconka = new ImageIcon(path + "Modify.png");
        }
        if (label.toLowerCase().equals("удалить") || label.toLowerCase().equals("стереть")) {
            iconka = new ImageIcon(path + "Delete.png");
        }
        if (label.toLowerCase().equals("найти") || label.toLowerCase().equals("поиск")) {
            iconka = new ImageIcon(path + "Find.png");
        }


        this.setIcon(iconka);
        this.labelButton = label;
        this.setLabelButton(labelButton);

        this.setPreferredSize(new Dimension(150, 25));
    }

    public void setNewSize(int newWidth, int newHieght) {
        this.setPreferredSize(new Dimension(newWidth, newHieght));
    }

    private void setLabelButton(String str) {
        this.setText(str);
    }
}
