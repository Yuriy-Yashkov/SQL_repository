package dept.sklad.model;

import javax.swing.*;
import java.util.ArrayList;


public class ClientCombo extends JComboBox<String> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<Dogovor> arrayClient = new ArrayList<Dogovor>();


    public ClientCombo() {
    }

    public ClientCombo(ArrayList<Dogovor> dogovor) {

        arrayClient.clear();
        for (int i = 0; i < dogovor.size(); i++) {
            this.arrayClient.add(dogovor.get(i));
            addItem(dogovor.get(i).getDogovorDescr());
        }
    }

    public int getId() {
        return arrayClient.get(this.getSelectedIndex()).getDogovorId();
    }

    public void setDogovor(String dogovor) {
        for (int i = 0; i < arrayClient.size(); i++) {
            if (arrayClient.get(i).getDogovorDescr().trim().equals(dogovor.trim())) {
                this.setSelectedIndex(i);
                return;
            }
        }
    }
}
