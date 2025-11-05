package dept.tech.innovation;

import workDB.DB_new;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author vova
 * @date 08.12.2011
 */
@SuppressWarnings("all")
public class ProductionDB extends DB_new {

    public static Vector sar;

    /**
     * Возвращает список инновационных изделий
     *
     * @return Vector[Vector] (наименование, модель, артикул, дата добавления, код)
     * @throws Exception
     * @author vova
     * @date 8.12.2011
     */
    // public static int fC;
    public Vector getTechItemsList() throws Exception {

        Vector items = new Vector();
        int i = 0;

        String query = " select ngpr, fas, nar, date_ins, kod, kod_naim from (select kod_izd, date_ins, kod_naim from _tech_items)as ii " +
                " left join (select nar, fas, ngpr, kod from nsi_kld)as kld on kld.kod = ii.kod_izd " +
                " order by date_ins ";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector item = new Vector();
                item.add(false);
                item.add(rs.getString(1).trim());
                item.add(rs.getString(2).trim());
                item.add(rs.getString(3).trim());
                item.add(rs.getString(4).trim());
                item.add(rs.getInt(5));
                item.add(rs.getInt(6));
                items.add(item);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getItemsList() " + e);
            throw new Exception("Ошибка получения списка инновационных изделий ", e);
        }
        return items;
    }

    public Vector getTechIItemsList() throws Exception {

        Vector items = new Vector();
        int i = 0;

        String query = " select name, fas, date_ins, kod_naim from _tech_items" +
                " order by date_ins ";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector item = new Vector();
                item.add(false);
                item.add(rs.getString(1).trim());
                item.add(rs.getString(2).trim());
                item.add(rs.getString(3).trim());
                item.add(rs.getInt(4));
                items.add(item);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getItemsList() " + e);
            throw new Exception("Ошибка получения списка инновационных изделий ", e);
        }
        return items;
    }

    public Vector getTradeItemsList() throws Exception {

        Vector items = new Vector();
        int i = 0;

        String query = " select ngpr, fas, nar, date_ins, kod, kod_naim from (select kod_izd, date_ins, kod_naim from _trade_items)as ii " +
                " left join (select nar, fas, ngpr, kod from nsi_kld)as kld on kld.kod = ii.kod_izd " +
                " order by date_ins ";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector item = new Vector();
                item.add(false);
                item.add(rs.getString(1).trim());
                item.add(rs.getString(2).trim());
                item.add(rs.getString(3).trim());
                item.add(rs.getString(4).trim());
                item.add(rs.getInt(5));
                item.add(rs.getInt(6));
                items.add(item);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getItemsList() " + e);
            throw new Exception("Ошибка получения списка инновационных изделий ", e);
        }
        return items;
    }

    public Vector getTradeIItemsList() throws Exception {

        Vector items = new Vector();
        int i = 0;

        String query = " select name, fas, date_ins, kod_naim from _trade_items" +
                " order by date_ins ";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector item = new Vector();
                item.add(false);
                item.add(rs.getString(1).trim());
                item.add(rs.getString(2).trim());
                item.add(rs.getString(3).trim());
                item.add(rs.getInt(4));
                items.add(item);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getItemsList() " + e);
            throw new Exception("Ошибка получения списка инновационных изделий ", e);
        }
        return items;
    }

    public String getProjDescription(int kod_naim) throws Exception {
        String description = "";
        String query = " select description from _tech_description where kod = ? ";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, kod_naim);
            rs = ps.executeQuery();
            while (rs.next()) {
                description = rs.getString(1);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getItemsList() " + e);
            throw new Exception("Ошибка получения списка инновационных изделий ", e);
        }
        return description;
    }

    public String getTProjDescription(int kod_naim) throws Exception {
        String description = "";
        String query = " select description from _trade_description where kod = ? ";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, kod_naim);
            rs = ps.executeQuery();
            while (rs.next()) {
                description = rs.getString(1);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getItemsList() " + e);
            throw new Exception("Ошибка получения списка инновационных изделий ", e);
        }
        return description;
    }

    public boolean updateTech(int fas, int kod_naim, String name) throws Exception {
//          int kod_izd = -1;
//          try{
//              String query =  " select kod_izd from (select kod_izd from _tech_items) as ii "+ 
//                  " left join (select fas, nar, ngpr, kod from nsi_kld) as kld on kld.kod = ii.kod_izd where kld.fas = ? and rtrim(ltrim(kld.nar)) = ? and rtrim(ltrim(kld.ngpr)) = ? ";
//              ps = conn.prepareStatement(query);
//              ps.setInt(1, fas);
//              ps.setString(2, nar);
//              ps.setString(3, ngpr);
//              rs = ps.executeQuery();
//                while(rs.next()){
//                   kod_izd = rs.getInt(1);
//                }
//             }catch(Exception e){
//                 System.err.println("Ошибка updateInov(int fas, int kod_naim) "+ e);
//                 throw new Exception("Ошибка при обновлении ", e);
//          }
        try {
            String query = " update _tech_items set kod_naim = ? where fas = ? and name = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, kod_naim);
            ps.setInt(2, fas);
            ps.setString(3, name);
            if (ps.executeUpdate() == 0) {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Ошибка updateInov(int fas, int kod_naim) " + e);
            throw new Exception("Ошибка при обновлении ", e);
        }

        return true;
    }

    public boolean updateTrade(int fas, int kod_naim, String name) throws Exception {
//          int kod_izd = -1;
//          try{
//              String query =  " select kod_izd from (select kod_izd from _trade_items) as ii "+ 
//                  " left join (select fas, nar, ngpr, kod from nsi_kld) as kld on kld.kod = ii.kod_izd where kld.fas = ? and rtrim(ltrim(kld.nar)) = ? and rtrim(ltrim(kld.ngpr)) = ? ";
//              ps = conn.prepareStatement(query);
//              ps.setInt(1, fas);
//              ps.setString(2, nar);
//              ps.setString(3, ngpr);
//              rs = ps.executeQuery();
//                while(rs.next()){
//                   kod_izd = rs.getInt(1);
//                }
//             }catch(Exception e){
//                 System.err.println("Ошибка updateInov(int fas, int kod_naim) "+ e);
//                 throw new Exception("Ошибка при обновлении ", e);
//          }
        try {
            String query = " update _trade_items set kod_naim = ? where fas = ? and name = ? ";
            ps = conn.prepareStatement(query);
            ps.setInt(1, kod_naim);
            ps.setInt(2, fas);
            ps.setString(2, name);
            if (ps.executeUpdate() == 0) {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Ошибка updateInov(int fas, int kod_naim) " + e);
            throw new Exception("Ошибка при обновлении ", e);
        }

        return true;
    }

    public boolean checkItemsSettings(Vector vec) throws Exception {
        int updated_values = 0;
        int inserted_values = 0;
        int deleted_values = 0;
        boolean out_value = true;
        Vector check = new Vector();
        try {
            String query = " select kod, description from _tech_description ";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tm = new Vector();
                tm.add(rs.getInt(1));
                tm.add(rs.getString(2));
                check.add(tm);
            }
        } catch (Exception e) {
            out_value = false;
            System.err.println("Ошибка checkItemsSettings(Vector vector) " + e);
            throw new Exception("Ошибка получения списка проектов для проверки ", e);
        }

        int check_size = Integer.valueOf(check.size());
        int vec_size = Integer.valueOf(vec.size());

        if (check_size > 0) {
            int last_value_check = Integer.parseInt(((Vector) check.elementAt(check_size - 1)).get(0).toString().trim());
            if (check_size == vec_size) {
                for (int i = 0; i < check_size; i++) {
                    for (int j = 0; j < vec_size; j++) {
                        if (Integer.parseInt(((Vector) check.elementAt(i)).get(0).toString().trim()) ==
                                Integer.parseInt(((Vector) vec.elementAt(j)).get(0).toString().trim()) &&
                                !((Vector) check.elementAt(i)).get(1).toString().trim().equals(((Vector) vec.elementAt(j)).get(1).toString().trim())) {
                            int int_val = Integer.parseInt(((Vector) vec.elementAt(j)).get(0).toString().trim());
                            String string_val = ((Vector) vec.elementAt(j)).get(1).toString().trim();
                            try {
                                String query = " update _tech_description set description = ? where kod = ? ";
                                ps = conn.prepareStatement(query);
                                ps.setString(1, string_val);
                                ps.setInt(2, int_val);
                                if (ps.executeUpdate() == 0) {
                                    out_value = false;
                                }
                                updated_values = updated_values + 1;
                            } catch (Exception e) {
                                System.err.println("Ошибка checkItemsSettings(Vector vector) " + e);
                                throw new Exception("Ошибка обновления списка проектов ", e);
                            }
                        }
                    }
                }
            }

            if (check_size != vec_size) {
                if (check_size < vec_size) {
                    int dif_vectors = vec_size - check_size;
                    for (int i = 0; i < check_size; i++) {
                        for (int j = 0; j < vec_size - dif_vectors; j++) {
                            if (Integer.parseInt(((Vector) check.elementAt(i)).get(0).toString().trim()) ==
                                    Integer.parseInt(((Vector) vec.elementAt(j)).get(0).toString().trim()) &&
                                    !((Vector) check.elementAt(i)).get(1).toString().trim().equals(((Vector) vec.elementAt(j)).get(1).toString().trim())) {
                                int int_val = Integer.parseInt(((Vector) vec.elementAt(j)).get(0).toString().trim());
                                String string_val = ((Vector) vec.elementAt(j)).get(1).toString().trim();
                                try {
                                    String query = " update _tech_description set description = ? where kod = ? ";
                                    ps = conn.prepareStatement(query);
                                    ps.setString(1, string_val);
                                    ps.setInt(2, int_val);
                                    if (ps.executeUpdate() == 0) {
                                        out_value = false;
                                    }
                                    updated_values = updated_values + 1;
                                } catch (Exception e) {
                                    System.err.println("Ошибка checkItemsSettings(Vector vector) " + e);
                                    throw new Exception("Ошибка обновления списка проектов ", e);
                                }
                            }
                        }
                    }
                    int j = 0;
                    for (int i = vec_size - dif_vectors; i < vec_size; i++) {
                        j = j + 1;
                        int int_val = Integer.parseInt(((Vector) vec.elementAt(i)).get(0).toString().trim());
                        String string_val = ((Vector) vec.elementAt(i)).get(1).toString().trim();
                        if (int_val != last_value_check + j) {
                            int_val = last_value_check + j;
                        }
                        try {
                            String query = " insert into _tech_description values(?, ?) ";
                            ps = conn.prepareStatement(query);
                            ps.setInt(1, int_val);
                            ps.setString(2, string_val);
                            if (ps.executeUpdate() == 0) {
                                out_value = false;
                            }
                            inserted_values = inserted_values + 1;
                        } catch (Exception e) {
                            System.err.println("Ошибка addItemsSettings(Vector vector) " + e);
                            throw new Exception("Ошибка при добавлении наименований проектов ", e);
                        }
                    }
                }

                if (check_size > vec_size) {
                    int dif_vectors = check_size - vec_size;
                    for (int i = 0; i < check_size - dif_vectors; i++) {
                        for (int j = 0; j < vec_size; j++) {
                            if (Integer.parseInt(((Vector) check.elementAt(i)).get(0).toString().trim()) ==
                                    Integer.parseInt(((Vector) vec.elementAt(j)).get(0).toString().trim()) &&
                                    !((Vector) check.elementAt(i)).get(1).toString().trim().equals(((Vector) vec.elementAt(j)).get(1).toString().trim())) {
                                int int_val = Integer.parseInt(((Vector) vec.elementAt(j)).get(0).toString().trim());
                                String string_val = ((Vector) vec.elementAt(j)).get(1).toString().trim();
                                try {
                                    String query = " update _tech_description set description = ? where kod = ? ";
                                    ps = conn.prepareStatement(query);
                                    ps.setString(1, string_val);
                                    ps.setInt(2, int_val);
                                    if (ps.executeUpdate() == 0) {
                                        out_value = false;
                                    }
                                    updated_values = updated_values + 1;
                                } catch (Exception e) {
                                    System.err.println("Ошибка checkItemsSettings(Vector vector) " + e);
                                    throw new Exception("Ошибка обновления списка проектов ", e);
                                }
                            }
                        }
                    }
                    for (int i = check_size - dif_vectors; i < check_size; i++) {
                        int int_val = Integer.parseInt(((Vector) check.elementAt(i)).get(0).toString().trim());
                        try {
                            String query = " delete from _tech_description where kod = ? ";
                            ps = conn.prepareStatement(query);
                            ps.setInt(1, int_val);
                            if (ps.executeUpdate() == 0) {
                                out_value = false;
                            }
                            deleted_values = deleted_values + 1;
                        } catch (Exception e) {
                            System.err.println("Ошибка addItemsSettings(Vector vector) " + e);
                            throw new Exception("Ошибка при удалении наименований проектов ", e);
                        }
                    }

                }
            }
            JOptionPane.showMessageDialog(null, "Добавлено строк: " + inserted_values + " " + "Обновлено строк: " + updated_values + " " + "Удалено строк: " + deleted_values);
        } else {
            for (int i = 0; i < vec.size(); i++) {
                int int_val = Integer.parseInt(((Vector) vec.elementAt(i)).get(0).toString().trim());
                String string_val = ((Vector) vec.elementAt(i)).get(1).toString().trim();
                try {
                    String query = " insert into _tech_description values(?, ?) ";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, int_val);
                    ps.setString(2, string_val);
                    if (ps.executeUpdate() == 0) {
                        out_value = false;
                    }
                    inserted_values = inserted_values + 1;
                } catch (Exception e) {
                    System.err.println("Ошибка addItemsSettings(Vector vector) " + e);
                    throw new Exception("Ошибка при добавлении наименований проектов ", e);
                }
            }
            if (out_value == true) {
                JOptionPane.showMessageDialog(null, "Наименования успешно добавлены! Добавлено строк: " + inserted_values);
            }
        }
        return out_value;
    }

    public boolean checkIItemsSettings(Vector vec) throws Exception {
        int updated_values = 0;
        int inserted_values = 0;
        int deleted_values = 0;
        boolean out_value = true;
        Vector check = new Vector();
        try {
            String query = " select kod, description from _trade_description ";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector tm = new Vector();
                tm.add(rs.getInt(1));
                tm.add(rs.getString(2));
                check.add(tm);
            }
        } catch (Exception e) {
            out_value = false;
            System.err.println("Ошибка checkItemsSettings(Vector vector) " + e);
            throw new Exception("Ошибка получения списка проектов для проверки ", e);
        }

        int check_size = Integer.valueOf(check.size());
        int vec_size = Integer.valueOf(vec.size());

        if (check_size > 0) {
            int last_value_check = Integer.parseInt(((Vector) check.elementAt(check_size - 1)).get(0).toString().trim());
            if (check_size == vec_size) {
                for (int i = 0; i < check_size; i++) {
                    for (int j = 0; j < vec_size; j++) {
                        if (Integer.parseInt(((Vector) check.elementAt(i)).get(0).toString().trim()) ==
                                Integer.parseInt(((Vector) vec.elementAt(j)).get(0).toString().trim()) &&
                                !((Vector) check.elementAt(i)).get(1).toString().trim().equals(((Vector) vec.elementAt(j)).get(1).toString().trim())) {
                            int int_val = Integer.parseInt(((Vector) vec.elementAt(j)).get(0).toString().trim());
                            String string_val = ((Vector) vec.elementAt(j)).get(1).toString().trim();
                            try {
                                String query = " update _trade_description set description = ? where kod = ? ";
                                ps = conn.prepareStatement(query);
                                ps.setString(1, string_val);
                                ps.setInt(2, int_val);
                                if (ps.executeUpdate() == 0) {
                                    out_value = false;
                                }
                                updated_values = updated_values + 1;
                            } catch (Exception e) {
                                System.err.println("Ошибка checkItemsSettings(Vector vector) " + e);
                                throw new Exception("Ошибка обновления списка проектов ", e);
                            }
                        }
                    }
                }
            }

            if (check_size != vec_size) {
                if (check_size < vec_size) {
                    int dif_vectors = vec_size - check_size;
                    for (int i = 0; i < check_size; i++) {
                        for (int j = 0; j < vec_size - dif_vectors; j++) {
                            if (Integer.parseInt(((Vector) check.elementAt(i)).get(0).toString().trim()) ==
                                    Integer.parseInt(((Vector) vec.elementAt(j)).get(0).toString().trim()) &&
                                    !((Vector) check.elementAt(i)).get(1).toString().trim().equals(((Vector) vec.elementAt(j)).get(1).toString().trim())) {
                                int int_val = Integer.parseInt(((Vector) vec.elementAt(j)).get(0).toString().trim());
                                String string_val = ((Vector) vec.elementAt(j)).get(1).toString().trim();
                                try {
                                    String query = " update _trade_description set description = ? where kod = ? ";
                                    ps = conn.prepareStatement(query);
                                    ps.setString(1, string_val);
                                    ps.setInt(2, int_val);
                                    if (ps.executeUpdate() == 0) {
                                        out_value = false;
                                    }
                                    updated_values = updated_values + 1;
                                } catch (Exception e) {
                                    System.err.println("Ошибка checkItemsSettings(Vector vector) " + e);
                                    throw new Exception("Ошибка обновления списка проектов ", e);
                                }
                            }
                        }
                    }
                    int j = 0;
                    for (int i = vec_size - dif_vectors; i < vec_size; i++) {
                        j = j + 1;
                        int int_val = Integer.parseInt(((Vector) vec.elementAt(i)).get(0).toString().trim());
                        String string_val = ((Vector) vec.elementAt(i)).get(1).toString().trim();
                        if (int_val != last_value_check + j) {
                            int_val = last_value_check + j;
                        }
                        try {
                            String query = " insert into _trade_description values(?, ?) ";
                            ps = conn.prepareStatement(query);
                            ps.setInt(1, int_val);
                            ps.setString(2, string_val);
                            if (ps.executeUpdate() == 0) {
                                out_value = false;
                            }
                            inserted_values = inserted_values + 1;
                        } catch (Exception e) {
                            System.err.println("Ошибка addItemsSettings(Vector vector) " + e);
                            throw new Exception("Ошибка при добавлении наименований проектов ", e);
                        }
                    }
                }

                if (check_size > vec_size) {
                    int dif_vectors = check_size - vec_size;
                    for (int i = 0; i < check_size - dif_vectors; i++) {
                        for (int j = 0; j < vec_size; j++) {
                            if (Integer.parseInt(((Vector) check.elementAt(i)).get(0).toString().trim()) ==
                                    Integer.parseInt(((Vector) vec.elementAt(j)).get(0).toString().trim()) &&
                                    !((Vector) check.elementAt(i)).get(1).toString().trim().equals(((Vector) vec.elementAt(j)).get(1).toString().trim())) {
                                int int_val = Integer.parseInt(((Vector) vec.elementAt(j)).get(0).toString().trim());
                                String string_val = ((Vector) vec.elementAt(j)).get(1).toString().trim();
                                try {
                                    String query = " update _trade_description set description = ? where kod = ? ";
                                    ps = conn.prepareStatement(query);
                                    ps.setString(1, string_val);
                                    ps.setInt(2, int_val);
                                    if (ps.executeUpdate() == 0) {
                                        out_value = false;
                                    }
                                    updated_values = updated_values + 1;
                                } catch (Exception e) {
                                    System.err.println("Ошибка checkItemsSettings(Vector vector) " + e);
                                    throw new Exception("Ошибка обновления списка проектов ", e);
                                }
                            }
                        }
                    }
                    for (int i = check_size - dif_vectors; i < check_size; i++) {
                        int int_val = Integer.parseInt(((Vector) check.elementAt(i)).get(0).toString().trim());
                        try {
                            String query = " delete from _trade_description where kod = ? ";
                            ps = conn.prepareStatement(query);
                            ps.setInt(1, int_val);
                            if (ps.executeUpdate() == 0) {
                                out_value = false;
                            }
                            deleted_values = deleted_values + 1;
                        } catch (Exception e) {
                            System.err.println("Ошибка addItemsSettings(Vector vector) " + e);
                            throw new Exception("Ошибка при удалении наименований проектов ", e);
                        }
                    }

                }
            }
            JOptionPane.showMessageDialog(null, "Добавлено строк: " + inserted_values + " " + "Обновлено строк: " + updated_values + " " + "Удалено строк: " + deleted_values);
        } else {
            for (int i = 0; i < vec.size(); i++) {
                int int_val = Integer.parseInt(((Vector) vec.elementAt(i)).get(0).toString().trim());
                String string_val = ((Vector) vec.elementAt(i)).get(1).toString().trim();
                try {
                    String query = " insert into _trade_description values(?, ?) ";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, int_val);
                    ps.setString(2, string_val);
                    if (ps.executeUpdate() == 0) {
                        out_value = false;
                    }
                    inserted_values = inserted_values + 1;
                } catch (Exception e) {
                    System.err.println("Ошибка addItemsSettings(Vector vector) " + e);
                    throw new Exception("Ошибка при добавлении наименований проектов ", e);
                }
            }
            if (out_value == true) {
                JOptionPane.showMessageDialog(null, "Наименования успешно добавлены! Добавлено строк: " + inserted_values);
            }
        }
        return out_value;
    }

    public Vector getItemsSettings() throws Exception {

        Vector itemsSettings = new Vector();
        int i = 0;

        String query = " select kod, description from _tech_description ";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector itemSettings = new Vector();
                itemSettings.add(rs.getInt(1));
                itemSettings.add(rs.getString(2).trim());
                itemsSettings.add(itemSettings);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getItemsSettings() " + e);
            throw new Exception("Ошибка получения списка наименований проектов ", e);
        }
        return itemsSettings;
    }

    public Vector getIItemsSettings() throws Exception {

        Vector itemsSettings = new Vector();
        int i = 0;

        String query = " select kod, description from _trade_description ";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector itemSettings = new Vector();
                itemSettings.add(rs.getInt(1));
                itemSettings.add(rs.getString(2).trim());
                itemsSettings.add(itemSettings);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getItemsSettings() " + e);
            throw new Exception("Ошибка получения списка наименований проектов ", e);
        }
        return itemsSettings;
    }

    public Vector getSettingKod() throws Exception {

        Vector itemSetting = new Vector();
        int i = 0;

        String query = " select kod from _tech_description ";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector it = new Vector();
                it.add(rs.getInt(1));
                itemSetting.add(it);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getSettingKod() " + e);
            throw new Exception("Ошибка получения списка для ComboBox ", e);
        }
        return itemSetting;
    }


    public Vector getComboVal(int fas) throws Exception {

        Vector items = new Vector();
        int i = 0;

        String query = " select ngpr from nsi_kld where fas = ? group by ngpr ";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, fas);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector it = new Vector();
                it.add(rs.getString(1).trim());
                items.add(it);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getSettingKod() " + e);
            throw new Exception("Ошибка получения списка для ComboBox ", e);
        }
        return items;
    }

    public Vector getTSettingKod() throws Exception {

        Vector itemSetting = new Vector();
        int i = 0;

        String query = " select kod from _trade_description ";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector it = new Vector();
                it.add(rs.getInt(1));
                itemSetting.add(it);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getSettingKod() " + e);
            throw new Exception("Ошибка получения списка для ComboBox ", e);
        }
        return itemSetting;
    }

    public Vector getItemsListforPeriod(long d, long d1) throws Exception {

        Vector items = new Vector();
        int i = 0;

        String query = " select ngpr, fas, nar, date_ins, kod, kod_naim from (select kod_izd, date_ins, kod_naim from _inov_items)as ii " +
                " left join (select nar, fas, ngpr, kod from nsi_kld)as kld on kld.kod = ii.kod_izd " +
                " where date_ins >= ? and date_ins <= ? " +
                " order by date_ins";
        try {
            ps = conn.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(d));
            ps.setDate(2, new java.sql.Date(d1));
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector item = new Vector();
                item.add(false);
                item.add(rs.getString(1).trim());
                item.add(rs.getString(2).trim());
                item.add(rs.getString(3).trim());
                item.add(rs.getString(4).trim());
                item.add(rs.getInt(5));
                item.add(rs.getInt(6));
                items.add(item);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getItemsList() " + e);
            throw new Exception("Ошибка получения списка инновационных изделий ", e);
        }
        return items;
    }

    /**
     * Возвращает список изделий из справочника изделий
     *
     * @param fas -- модель изделия
     * @return Vector[Vector] (наименование, модель, артикул, код)
     * @throws Exception
     * @author vova
     * @date 8.12.2011
     */
//    public Vector searchItems(int fas)throws Exception{
//        Vector items = new Vector();
//        Vector fSar = new Vector();
//        String query =  " select ngpr, fas, nar, kod, sar from nsi_kld where fas = ? "; 
//        try{
//            ps = conn.prepareStatement(query);
//            ps.setInt(1, fas);
//            rs = ps.executeQuery();
//            while(rs.next()){
//                Vector item = new Vector();
//                Vector tsar = new Vector();
//                item.add(rs.getString(1).trim());
//                item.add(rs.getString(2).trim());
//                item.add(rs.getString(3).trim());
//                item.add(rs.getInt(4));
//                //item.add(rs.getString(5).trim());
//                items.add(item);
//                tsar.add(rs.getInt(4));
//                tsar.add(rs.getString(5).trim());
//                fSar.add(tsar);
//            }
//
//           sar = fSar;
//        }catch(Exception e){
//            System.err.println("Ошибка getItemsList() "+ e);
//            throw new Exception("Ошибка получения списка инновационных изделий ", e);
//        }
//        return items;
//    }
    public Vector searchItems(int fas) throws Exception {
        Vector items = new Vector();
        String query = " select ngpr, fas from nsi_kld where fas = ? group by ngpr, fas ";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, fas);
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector item = new Vector();
                item.add(rs.getString(1).trim());
                item.add(rs.getInt(2));
                items.add(item);
            }

        } catch (Exception e) {
            System.err.println("Ошибка getItemsList() " + e);
            throw new Exception("Ошибка получения списка инновационных изделий ", e);
        }
        return items;
    }

    /**
     * Добавляет изделие в в таблицу инновационных изделий
     *
     * @param kod_izd -- код изделия
     * @return true/false
     * @throws Exception
     * @author vova
     * @date 8.12.2011
     */
    public boolean addTechItems(int fas, String ngpr, int kod_naim) throws Exception {

        String query = " insert into _tech_items(fas, name, kod_naim) values(?, ?, ?) ";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, fas);
            ps.setString(2, ngpr);
            ps.setInt(3, kod_naim);


            if (ps.executeUpdate() == 0) {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Ошибка addItems(int kod_izd, int kod_naim) " + e);
            throw new Exception("Ошибка при добавлении инновационных изделий ", e);
        }
        return true;
    }

    public boolean addTradeItems(int fas, String ngpr, int kod_naim) throws Exception {

        String query = " insert into _trade_items(fas, name, kod_naim) values(?, ?, ?) ";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, fas);
            ps.setString(2, ngpr);
            ps.setInt(3, kod_naim);

            if (ps.executeUpdate() == 0) {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Ошибка addItems(int kod_izd, int kod_naim) " + e);
            throw new Exception("Ошибка при добавлении инновационных изделий ", e);
        }
        return true;
    }

    /**
     * Удаляет изделие из таблицы инновационных изделий
     *
     * @param kod_izd -- код изделия
     * @return true/false
     * @throws Exception
     * @author vova
     * @date 12.12.2011
     */
    public boolean delItems(int fas, String name) throws Exception {
        String query = " delete from _tech_items where fas = ? and name = ? ";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, fas);
            ps.setString(2, name);

            if (ps.executeUpdate() == 0) {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Ошибка delItems(int kod_izd) " + e);
            throw new Exception("Ошибка при удалении и изделий ", e);
        }
        return true;
    }

    public boolean delTItems(int fas, String name) throws Exception {
        String query = " delete from _trade_items where fas = ? and name = ? ";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, fas);
            ps.setString(2, name);

            if (ps.executeUpdate() == 0) {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Ошибка delItems(int kod_izd) " + e);
            throw new Exception("Ошибка при удалении и изделий ", e);
        }
        return true;
    }

    public Vector getProductionInfo(ArrayList o, Vector oo, Long sd, Long ed) throws Exception {

        Iterator it = o.iterator();
        try {
            String query = "create table _innov(sar int not null, rst int, rzm int, count int);";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            query = "insert into _innov values(?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            while (it.hasNext()) {
                Object[] ob = new Object[4];
                ob = (Object[]) it.next();
                ps.setInt(1, Integer.parseInt(ob[0].toString()));
                ps.setInt(2, Integer.parseInt(ob[2].toString()));
                ps.setInt(3, Integer.parseInt(ob[1].toString()));
                ps.setInt(4, Math.round(Float.parseFloat(ob[3].toString())));
                ps.executeUpdate();
            }

            query = " select ngpr, fas, round(sum(pln.[count])*0.001,1) as spc, round(sum(pln.[count]*cno)*0.000001,1) as sps, " +
                    " svc = case when round(sum(vp2.sumP)*0.001,1) IS NULL THEN 0 ELSE round(sum(vp2.sumP)*0.001,1) END, " +
                    " svs = case when round(sum(vp2.sumP*cno)*0.000001,1) IS NULL THEN 0 ELSE  round(sum(vp2.sumP*cno)*0.000001,1) END, " +
                    " soc = case when round(sum(ot2.sumO)*0.001,1) IS NULL THEN 0 ELSE round(sum(ot2.sumO)*0.001,1) END, " +
                    " sos = case when round(sum(ot2.sumO*cno)*0.000001,1) IS NULL THEN 0 ELSE round(sum(ot2.sumO*cno)*0.000001,1) END, " +
                    " kd = case when inov.kod_naim IS NULL THEN 0 ELSE inov.kod_naim END " +
                    " from (select sar, rst, rzm, [count] from _innov) as pln " +
                    " left join (select nar, fas, ngpr, kod, sar from nsi_kld)as kld on kld.sar = pln.sar " +
                    " left join (select kod, kod1, rst, rzm, cno from nsi_sd where srt = 1) as sd on sd.kod = kld.kod and sd.rst = pln.rst and sd.rzm = pln.rzm " +
                    " join (select kod_izd, kod_naim from _inov_items)as inov on inov.kod_izd = sd.kod " +
                    " left join (select sum(kol* kol_in_upack) as sumP, kod_izd from vnperem2 where doc_id in (select item_id from vnperem1 where date >= ? and  date<= ? and status = 0)group by kod_izd)  as vp2 on vp2.kod_izd = sd.kod1 " +
                    " left join (select sum(kol* kol_in_upack) as sumO, kod_izd from otgruz2 where doc_id in (select item_id from otgruz1 where date >= ? and  date<= ? and status = 0 and export = 1)group by kod_izd) as ot2 on ot2.kod_izd = sd.kod1 " +
                    " group by fas, nar, ngpr, kod_naim order by kod_naim ";

            ps = conn.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(sd));
            ps.setDate(2, new java.sql.Date(ed));
            ps.setDate(3, new java.sql.Date(sd));
            ps.setDate(4, new java.sql.Date(ed));

            rs = ps.executeQuery();
            while (rs.next()) {
                Vector item = new Vector();
                item.add(rs.getString(1).trim());
                item.add(rs.getInt(2));
                item.add(rs.getFloat(3));
                item.add(rs.getFloat(4));
                item.add(rs.getFloat(5));
                item.add(rs.getFloat(6));
                item.add(rs.getFloat(7));
                item.add(rs.getFloat(8));
                item.add(rs.getInt(9));
                oo.add(item);
            }

            query = " DROP TABLE _innov";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Ошибка getProductionInfo(ArrayList o, Vector oo, Long sd, Long ed) " + e);
            throw new Exception("Ошибка получения списка импортозамещающей продукции ", e);
        }
        return oo;
    }

    public Vector getProductionList(ArrayList plan, ArrayList produced, ArrayList export, Long sd, Long ed) throws Exception {
        Vector v = new Vector();
        Iterator it = plan.iterator();
        Iterator it_1 = produced.iterator();
        Iterator it_2 = export.iterator();
        try {

            String query = "IF OBJECT_ID('_plan_tmp', 'U') IS NOT NULL  DROP TABLE _plan_tmp";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            query = "IF OBJECT_ID('_produced_tmp', 'U') IS NOT NULL DROP TABLE _produced_tmp";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            query = "IF OBJECT_ID('_export_tmp', 'U') IS NOT NULL DROP TABLE _export_tmp";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();


            query = "create table _plan_tmp(sar int not null, rzm int not null, rst int not null, cnt int not null);";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

            query = "insert into _plan_tmp values(?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            while (it.hasNext()) {
                Object[] ob = new Object[4];
                ob = (Object[]) it.next();
                ps.setInt(1, Integer.parseInt(ob[0].toString()));
                ps.setInt(2, Integer.parseInt(ob[1].toString()));
                ps.setInt(3, Integer.parseInt(ob[2].toString()));
                ps.setInt(4, Math.round(Float.parseFloat(ob[3].toString())));
                ps.executeUpdate();
            }

            query = "create table _produced_tmp(sar int not null, fas int not null, rzm int not null, rst int not null, cnt numeric not null, ceno numeric(8,2) not null);";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

            query = "insert into _produced_tmp values(?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            while (it_1.hasNext()) {
                Object[] ob = new Object[4];
                ob = (Object[]) it_1.next();
                ps.setInt(1, Integer.parseInt(ob[0].toString()));
                ps.setInt(2, Integer.parseInt(ob[1].toString()));
                ps.setInt(3, Integer.parseInt(ob[2].toString()));
                ps.setInt(4, Integer.parseInt(ob[3].toString()));
                ps.setInt(5, Math.round(Float.parseFloat(ob[4].toString())));
                ps.setDouble(6, Double.parseDouble(ob[5].toString()));
                ps.executeUpdate();
            }

            query = "create table _export_tmp(sar int not null, fas int not null, rzm int not null, rst int not null, cnt numeric not null, ceno numeric(8,2) not null);";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

            query = "insert into _export_tmp values(?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            while (it_2.hasNext()) {
                Object[] ob = new Object[4];
                ob = (Object[]) it_2.next();
                ps.setInt(1, Integer.parseInt(ob[0].toString()));
                ps.setInt(2, Integer.parseInt(ob[1].toString()));
                ps.setInt(3, Integer.parseInt(ob[2].toString()));
                ps.setInt(4, Integer.parseInt(ob[3].toString()));
                ps.setInt(5, Math.round(Float.parseFloat(ob[4].toString())));
                ps.setDouble(6, Double.parseDouble(ob[5].toString()));
                ps.executeUpdate();
            }

//           query =  " select ngpr, fas, sum(pln.cnt) as count_plan, sum(pln.cnt*cno) as sum_plan, "+
//		        " count_produced = case when sum(_prod.sumc) IS NULL THEN 0 ELSE sum(_prod.sumc) END, "+
//                        " sum_produced = case when sum(_prod.sumcen) IS NULL THEN 0 ELSE sum(_prod.sumcen) END, "+
//			" count_export = case when sum(_exp.sumec) IS NULL THEN 0 ELSE sum(_exp.sumec) END, "+
//                        " sum_export = case when sum(_exp.sumecen) IS NULL THEN 0 ELSE sum(_exp.sumecen) END, "+
//                        " kd = case when _tech.kod_naim IS NULL THEN 0 ELSE _tech.kod_naim END" +
//                                     " from (select sar, rst, rzm, cnt from _plan_tmp) as pln "+
//					" left join (select nar, fas, ngpr, kod, sar from nsi_kld)as kld on kld.sar = pln.sar "+
//					" left join (select kod, kod1, rst, rzm, cno from nsi_sd where srt = 1) as sd on sd.kod = kld.kod and sd.rst = pln.rst and sd.rzm = pln.rzm "+
//					" join (select kod_izd, kod_naim from _tech_items)as _tech on _tech.kod_izd = sd.kod "+
//                                        " left join (select sar as sr, fas as fs, rst, rzm, sum(cnt) as sumc, sum(cnt*ceno) as sumcen from _produced_tmp group by sar, fas, rst, rzm) as _prod on _prod.sr = kld.sar and _prod.fs = kld.fas and _prod.rzm = sd.rzm and _prod.rst = sd.rst " +
//                                        " left join (select sar as sr, fas as fs, rst, rzm, sum(cnt) as sumec, sum(cnt*ceno) as sumecen from _export_tmp group by sar, fas, rst, rzm) as _exp on _exp.sr = kld.sar and _exp.fs = kld.fas and _exp.rzm = sd.rzm and _exp.rst = sd.rst "+
//                             " group by fas, nar, ngpr, kod_naim order by kod_naim "; 

            query = " select  _tech_items.name, _tech_items.fas, " +
                    " c_pl = case when sum(pln.cnt) is null then 0 else sum(pln.cnt) end, " +
                    " s_pl = case when sum(pln.cnt*pln.cno) is null then 0 else sum(pln.cnt*pln.cno) end, " +
                    " c_pr = case when prd.c_cnt is null then 0 else prd.c_cnt end, " +
                    " s_pr = case when prd.s_ceno is null then 0 else prd.s_ceno end, " +
                    " c_ex = case when ex.c_cnt is null then 0 else ex.c_cnt end, " +
                    " s_ex = case when ex.s_ceno is null then 0 else ex.s_ceno end, " +
                    " _tech_items.kod_naim " +
                    " from _tech_items " +
                    " left join " +
                    " (select _plan_tmp.sar, sd.fas, _plan_tmp.rzm, _plan_tmp.rst, _plan_tmp.cnt, sd.cno from _plan_tmp " +
                    " left join " +
                    " (select nsi_sd.kod, nsi_kld.sar, nsi_kld.fas, nsi_sd.rzm, nsi_sd.rst, nsi_sd.cno from nsi_sd " +
                    " left join nsi_kld on nsi_sd.kod = nsi_kld.kod where nsi_sd.srt = 1) as sd " +
                    " on _plan_tmp.sar = sd.sar and _plan_tmp.rzm = sd.rzm and _plan_tmp.rst = sd.rst) as pln " +
                    " on _tech_items.fas = pln.fas " +
                    " left join " +
                    " (select _produced_tmp.fas, sum(_produced_tmp.cnt) as c_cnt, sum(_produced_tmp.ceno*_produced_tmp.cnt) as s_ceno " +
                    " from _produced_tmp group by _produced_tmp.fas) as prd " +
                    " on pln.fas = prd.fas " +
                    " left join " +
                    " (select _export_tmp.fas, sum(_export_tmp.cnt) as c_cnt, sum(_export_tmp.ceno*_export_tmp.cnt) as s_ceno " +
                    " from _export_tmp group by  _export_tmp.fas) as ex " +
                    " on prd.fas = ex.fas " +
                    " group by _tech_items.name, _tech_items.fas, prd.c_cnt, prd.s_ceno, ex.c_cnt, ex.s_ceno, _tech_items.kod_naim ";

            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            int num = 0;
            while (rs.next()) {
                num = num + 1;
                Vector item = new Vector();
                item.add(num);
                item.add(rs.getString(1).trim());
                item.add(rs.getInt(2));
                item.add(rs.getFloat(3));
                item.add(rs.getFloat(4));
                item.add(rs.getFloat(5));
                item.add(rs.getFloat(6));
                item.add(rs.getFloat(7));
                item.add(rs.getFloat(8));
                item.add(rs.getInt(9));
                v.add(item);
            }
/*
            query = " DROP TABLE _plan_tmp";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            query = " DROP TABLE _produced_tmp";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            query = " DROP TABLE _export_tmp";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();
            */

        } catch (Exception e) {
            //System.err.println("Ошибка getProductionList(ArrayList plan, ArrayList produced, ArrayList export, Long sd, Long ed) "+ e);
            //throw new Exception("Ошибка получения списка импортозамещающей продукции ", e);
            e.printStackTrace();
        }
        return v;
    }

    public Vector getTProductionList(ArrayList plan, ArrayList produced, ArrayList export, Long sd, Long ed) throws Exception {
        Vector v = new Vector();
        Iterator it = plan.iterator();
        Iterator it_1 = produced.iterator();
        Iterator it_2 = export.iterator();
        try {
            String query = "create table _plan_temp(sar int not null, rzm int not null, rst int not null, cnt int not null);";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

            query = "insert into _plan_temp values(?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            while (it.hasNext()) {
                Object[] ob = new Object[4];
                ob = (Object[]) it.next();
                ps.setInt(1, Integer.parseInt(ob[0].toString()));
                ps.setInt(2, Integer.parseInt(ob[1].toString()));
                ps.setInt(3, Integer.parseInt(ob[2].toString()));
                ps.setInt(4, Math.round(Float.parseFloat(ob[3].toString())));
                ps.executeUpdate();
            }

            query = "create table _produced_temp(sar int not null, fas int not null, rzm int not null, rst int not null, cnt numeric not null, ceno numeric(8,2) not null);";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

            query = "insert into _produced_temp values(?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            while (it_1.hasNext()) {
                Object[] ob = new Object[4];
                ob = (Object[]) it_1.next();
                ps.setInt(1, Integer.parseInt(ob[0].toString()));
                ps.setInt(2, Integer.parseInt(ob[1].toString()));
                ps.setInt(3, Integer.parseInt(ob[2].toString()));
                ps.setInt(4, Integer.parseInt(ob[3].toString()));
                ps.setInt(5, Math.round(Float.parseFloat(ob[4].toString())));
                ps.setDouble(6, Double.parseDouble(ob[5].toString()));
                ps.executeUpdate();
            }

            query = "create table _export_temp(sar int not null, fas int not null, rzm int not null, rst int not null, cnt numeric not null, ceno numeric(8,2) not null);";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

            query = "insert into _export_temp values(?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            while (it_2.hasNext()) {
                Object[] ob = new Object[4];
                ob = (Object[]) it_2.next();
                ps.setInt(1, Integer.parseInt(ob[0].toString()));
                ps.setInt(2, Integer.parseInt(ob[1].toString()));
                ps.setInt(3, Integer.parseInt(ob[2].toString()));
                ps.setInt(4, Integer.parseInt(ob[3].toString()));
                ps.setInt(5, Math.round(Float.parseFloat(ob[4].toString())));
                ps.setDouble(6, Double.parseDouble(ob[5].toString()));
                ps.executeUpdate();
            }

//           query =  " select ngpr, fas, sum(pln.cnt) as count_plan, sum(pln.cnt*cno) as sum_plan, "+
//		        " count_produced = case when sum(_prod.sumc) IS NULL THEN 0 ELSE sum(_prod.sumc) END, "+
//                        " sum_produced = case when sum(_prod.sumcen) IS NULL THEN 0 ELSE sum(_prod.sumcen) END, "+
//			" count_export = case when sum(_exp.sumec) IS NULL THEN 0 ELSE sum(_exp.sumec) END, "+
//                        " sum_export = case when sum(_exp.sumecen) IS NULL THEN 0 ELSE sum(_exp.sumecen) END, "+
//                        " kd = case when _trade.kod_naim IS NULL THEN 0 ELSE _trade.kod_naim END" +
//                                     " from (select sar, rst, rzm, cnt from _plan_temp) as pln "+
//					" left join (select nar, fas, ngpr, kod, sar from nsi_kld)as kld on kld.sar = pln.sar "+
//					" left join (select kod, kod1, rst, rzm, cno from nsi_sd where srt = 1) as sd on sd.kod = kld.kod and sd.rst = pln.rst and sd.rzm = pln.rzm "+
//					" join (select kod_izd, kod_naim from _trade_items)as _trade on _trade.kod_izd = sd.kod "+
//                                        " left join (select sar as sr, fas as fs, rst, rzm, sum(cnt) as sumc, sum(cnt*ceno) as sumcen from _produced_temp group by sar, fas, rst, rzm) as _prod on _prod.sr = kld.sar and _prod.fs = kld.fas and _prod.rzm = sd.rzm and _prod.rst = sd.rst " +
//                                        " left join (select sar as sr, fas as fs, rst, rzm, sum(cnt) as sumec, sum(cnt*ceno) as sumecen from _export_temp group by sar, fas, rst, rzm) as _exp on _exp.sr = kld.sar and _exp.fs = kld.fas and _exp.rzm = sd.rzm and _exp.rst = sd.rst "+
//                             " group by fas, nar, ngpr, kod_naim order by kod_naim "; 

            query = " select  _trade_items.name, _trade_items.fas, " +
                    " c_pl = case when sum(pln.cnt) is null then 0 else sum(pln.cnt) end, " +
                    " s_pl = case when sum(pln.cnt*pln.cno) is null then 0 else sum(pln.cnt*pln.cno) end, " +
                    " c_pr = case when prd.c_cnt is null then 0 else prd.c_cnt end, " +
                    " s_pr = case when prd.s_ceno is null then 0 else prd.s_ceno end, " +
                    " c_ex = case when ex.c_cnt is null then 0 else ex.c_cnt end, " +
                    " s_ex = case when ex.s_ceno is null then 0 else ex.s_ceno end, " +
                    " _trade_items.kod_naim " +
                    " from _trade_items " +
                    " left join " +
                    " (select _plan_temp.sar, sd.fas, _plan_temp.rzm, _plan_temp.rst, _plan_temp.cnt, sd.cno from _plan_temp " +
                    " left join " +
                    " (select nsi_sd.kod, nsi_kld.sar, nsi_kld.fas, nsi_sd.rzm, nsi_sd.rst, nsi_sd.cno from nsi_sd " +
                    " left join nsi_kld on nsi_sd.kod = nsi_kld.kod where nsi_sd.srt = 1) as sd " +
                    " on _plan_temp.sar = sd.sar and _plan_temp.rzm = sd.rzm and _plan_temp.rst = sd.rst) as pln " +
                    " on _trade_items.fas = pln.fas " +
                    " left join " +
                    " (select _produced_temp.fas, sum(_produced_temp.cnt) as c_cnt, sum(_produced_temp.ceno*_produced_temp.cnt) as s_ceno " +
                    " from _produced_temp group by _produced_temp.fas) as prd " +
                    " on pln.fas = prd.fas " +
                    " left join " +
                    " (select _export_temp.fas, sum(_export_temp.cnt) as c_cnt, sum(_export_temp.ceno*_export_temp.cnt) as s_ceno " +
                    " from _export_temp group by  _export_temp.fas) as ex " +
                    " on prd.fas = ex.fas " +
                    " group by _trade_items.name, _trade_items.fas, prd.c_cnt, prd.s_ceno, ex.c_cnt, ex.s_ceno, _trade_items.kod_naim ";

            ps = conn.prepareStatement(query);

            rs = ps.executeQuery();
            int num = 0;
            while (rs.next()) {
                Vector item = new Vector();
                num = num + 1;
                item.add(num);
                item.add(rs.getString(1).trim());
                item.add(rs.getInt(2));
                item.add(rs.getFloat(3));
                item.add(rs.getFloat(4));
                item.add(rs.getFloat(5));
                item.add(rs.getFloat(6));
                item.add(rs.getFloat(7));
                item.add(rs.getFloat(8));
                item.add(rs.getInt(9));
                v.add(item);
            }

            query = " DROP TABLE _plan_temp";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            query = " DROP TABLE _produced_temp";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

            query = " DROP TABLE _export_temp";
            ps = conn.prepareStatement(query);
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Ошибка getTProductionList(ArrayList plan, ArrayList produced, ArrayList export, Long sd, Long ed) " + e);
            throw new Exception("Ошибка получения списка продукции по торговым маркам ", e);
        }
        return v;
    }
}