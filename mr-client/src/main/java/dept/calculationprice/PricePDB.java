package dept.calculationprice;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.PDBCalc;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.logging.Level;


@SuppressWarnings("all")
public final class PricePDB extends PDBCalc {

    //private static final Logger log = new Log().getLoger(dept.calculationprice.PricePDB.class);
    private static final LogCrutch log = new LogCrutch();
    private String query;
    private ArrayList<MyBean> listBean;

    //public PricePDB(){     
//	conn();
    //  }
    //Список групп

    /**
     * @param typeCalculation
     * @return
     */
    public ArrayList<Group> getGroup(int typeCalculation) {

        ArrayList<Group> groupList = new ArrayList<Group>();
        Group group;

        query = "SELECT "
                + "   * "
                + "  FROM"
                + " \"group\" "
                + " WHERE "
                + " type_calculation  = ? ";

        try {
            ps = getConn().prepareStatement(query);
            ps.setInt(1, typeCalculation);
            rs = ps.executeQuery();

            while (rs.next()) {

                group = new Group();
                group.setId(rs.getInt("id"));
                group.setIdTypeProd(rs.getInt("id_type_prod"));
                group.setName(rs.getString("name"));
                group.setTypeCalculation(rs.getInt("type_calculation"));
                groupList.add(group);

            }
            return groupList;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return null;
    }

    // Получить типы продукции

    /**
     * @return
     */
    public ArrayList<TypeProduction> getTypeProduction() {
        ArrayList<TypeProduction> typeProductionList = new ArrayList<TypeProduction>();
        TypeProduction typeProduction;

        query = "SELECT "
                + "   * "
                + "  FROM"
                + " \"type_prod\" ";

        try {
            ps = getConn().prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {

                typeProduction = new TypeProduction();
                typeProduction.setId(rs.getInt("id"));
                typeProduction.setName(rs.getString("name"));
                typeProductionList.add(typeProduction);

            }
            return typeProductionList;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return null;

    }

    //Список видов у группы

    /**
     * @param idGroup
     * @return
     */
    public ArrayList<Type> getType(int idGroup) {

        ArrayList<Type> typeList = new ArrayList<Type>();
        Type type;

        query = "SELECT "
                + "   * "
                + "  FROM"
                + " \"type\" "
                + " WHERE "
                + " id_group  = ? ";

        try {
            ps = getConn().prepareStatement(query);
            ps.setInt(1, idGroup);
            rs = ps.executeQuery();

            while (rs.next()) {

                type = new Type();
                type.setId(rs.getInt("id"));
                type.setIdGroup(rs.getInt("id_group"));
                type.setName(rs.getString("name"));
                type.setAgeType(rs.getInt("age_type"));
                typeList.add(type);

            }
            return typeList;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return null;
    }

    //Получить ассортимент 

    /**
     * @param idGroup
     * @param idType
     * @return
     */
    public ArrayList<Assortment> getAssortment(int idGroup, int idType) {

        ArrayList<Assortment> assortmentList = new ArrayList<Assortment>();
        Assortment assortment;

        query = "SELECT"
                + " assortment.id , assortment.id_group , assortment.id_type , assortment.ptk , nsi_pkd.\"NPTK\" AS  name "
                + " FROM assortment , nsi_pkd"
                + " WHERE"
                + " assortment.id_group = ?  AND assortment.id_type = ? AND assortment.ptk = nsi_pkd.\"PTK\" ORDER BY nsi_pkd.\"NPTK\"";

        try {
            ps = getConn().prepareStatement(query);
            ps.setInt(1, idGroup);
            ps.setInt(2, idType);
            rs = ps.executeQuery();

            while (rs.next()) {

                assortment = new Assortment();
                assortment.setId(rs.getInt("id"));
                assortment.setIdGroup(rs.getInt("id_group"));
                assortment.setIdType(rs.getInt("id_type"));
                assortment.setPtk(rs.getString("ptk"));
                assortment.setName(rs.getString("name"));
                assortmentList.add(assortment);

            }
            return assortmentList;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return null;
    }

    /**
     * @return
     */
    public ArrayList<Assortment> getNameFree() {
        ArrayList<Assortment> nameList = new ArrayList<Assortment>();
        Assortment assortment;

        query = "SELECT"
                + " nsi_pkd.\"PTK\" AS \"ptk\" , nsi_pkd.\"NPTK\" AS \"name\""
                + " FROM nsi_pkd"
                + " WHERE NOT EXISTS (SELECT NULL FROM assortment WHERE assortment.ptk = nsi_pkd.\"PTK\" )";

        try {
            ps = getConn().prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {

                assortment = new Assortment();
                assortment.setName(rs.getString("name"));
                assortment.setPtk(rs.getString("ptk"));
                nameList.add(assortment);

            }
            return nameList;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return null;

    }

    //Добавить группу

    /**
     * @param idTypeProd
     * @param name
     * @param typeCalculation
     * @return
     */
    public boolean addGroup(int idTypeProd, String name, int typeCalculation) {

        query = "INSERT INTO "
                + "\"group\" "
                + "(id_type_prod , name , type_calculation ) "
                + " VALUES  "
                + "( ?, ? , ?)";

        try {

            ps = getConn().prepareStatement(query);
            ps.setInt(1, idTypeProd);
            ps.setString(2, name);
            ps.setInt(3, typeCalculation);
            ps.executeUpdate();

            return true;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return false;

    }

    //Добавить вид у группы

    /**
     * @param idGroup
     * @param name
     * @param ageType
     * @return
     */
    public boolean addType(int idGroup, String name, int ageType) {

        query = "INSERT INTO "
                + "type "
                + "(id_group , name , age_type ) "
                + " VALUES  "
                + "( ?, ? , ?)";

        try {

            ps = getConn().prepareStatement(query);
            ps.setInt(1, idGroup);
            ps.setString(2, name);
            ps.setInt(3, ageType);
            ps.executeUpdate();

            return true;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return false;

    }

    //Добавить ассортимент

    /**
     * @param idGroup
     * @param idType
     * @param ptk
     * @return
     */
    public boolean addAssortment(int idGroup, int idType, String ptk) {

        query = "INSERT INTO "
                + "assortment "
                + "(id_group , id_type , ptk ) "
                + " VALUES  "
                + "( ?, ? , ?)";

        try {

            ps = getConn().prepareStatement(query);
            ps.setInt(1, idGroup);
            ps.setInt(2, idType);
            ps.setString(3, ptk);
            ps.executeUpdate();

            return true;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return false;

    }

    //Изменить вид и группу ассортимента

    /**
     * @param idGroup
     * @param idType
     * @param ptk
     * @return
     */
    public boolean updateAssortment(int idGroup, int idType, String ptk) {

        query = "UPDATE "
                + "assortment "
                + "set   "
                + " id_group = ?   , id_type = ? "
                + " WHERE"
                + " ptk = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setInt(1, idGroup);
            ps.setInt(2, idType);
            ps.setString(3, ptk);
            ps.executeUpdate();

            query = "UPDATE "
                    + " \"C1_Cen\" "
                    + " set   "
                    + " \"GRUP\" = ?   , \"VID\" = ? "
                    + " WHERE"
                    + "  \"PTK\" = ?";

            ps = getConn().prepareStatement(query);
            ps.setInt(1, idGroup);
            ps.setInt(2, idType);
            ps.setString(3, ptk);
            ps.executeUpdate();

            return true;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return false;

    }

    /**
     * @param ptk
     * @return
     */
    public ArrayList<MyBean> getListArticle(String ptk, String articlePrefix) {

        listBean = new ArrayList<MyBean>();
        MyBean bean;

        query = "SELECT "
                + "   \"NIZ\"  , \"NAR\" ,\"FAS\"  ,\"RZMN\" || '-' || \"RZMK\" AS size   ,\"CNO\" ,\"CNVP\" , \"CNR\" ,prim,\"POL\",\"NIZ1\",\"CC\", id "
                + "  FROM"
                + " \"C1_Cen\""
                + " WHERE "
                + "\"PTK\" = ?  AND status !='del' AND \"NAR\" LIKE ? "
                + "ORDER BY \"FAS\"  , \"NAR\" , \"RZMN\" ";
        try {
            ps = getConn().prepareStatement(query);
            ps.setString(1, ptk);
            ps.setString(2, articlePrefix + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                //System.out.print(rs.getString("NAR"));
                bean = new MyBean(rs.getString("NIZ"),
                        rs.getString("NAR"),
                        rs.getString("FAS"),
                        rs.getString("size"),
                        String.format("%.2f", rs.getFloat("CNO")),
                        rs.getString("CNVP"),
                        rs.getString("CNR"),
                        rs.getString("prim"),
                        rs.getString("POL"),
                        rs.getString("NIZ1"),
                        rs.getDouble("CC"),
                        rs.getInt("id"));

                listBean.add(bean);
            }

            return listBean;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        return new ArrayList<MyBean>();
    }

    /**
     * @param ptk
     * @return
     */
    public ArrayList<MyBean> getListArticle(String ptk) {

        listBean = new ArrayList<MyBean>();
        MyBean bean;

        query = "SELECT "
                + "   \"NIZ\"  , \"NAR\" ,\"FAS\"  ,\"RZMN\" || '-' || \"RZMK\" AS size   ,\"CNO\" ,\"CNVP\" , \"CNR\" ,prim,\"POL\",\"NIZ1\",\"CC\", id "
                + "  FROM"
                + " \"C1_Cen\""
                + " WHERE "
                + "\"PTK\" = ?  AND status !='del'"
                + "ORDER BY \"FAS\"  , \"NAR\" , \"RZMN\" ";
        try {
            ps = getConn().prepareStatement(query);
            ps.setString(1, ptk);

            rs = ps.executeQuery();

            while (rs.next()) {
                //System.out.print(rs.getString("NAR"));
                bean = new MyBean(rs.getString("NIZ"),
                        rs.getString("NAR"),
                        rs.getString("FAS"),
                        rs.getString("size"),
                        String.format("%.2f", rs.getFloat("CNO")),
                        rs.getString("CNVP"),
                        rs.getString("CNR"),
                        rs.getString("prim"),
                        rs.getString("POL"),
                        rs.getString("NIZ1"),
                        rs.getDouble("CC"),
                        rs.getInt("id"));

                listBean.add(bean);
            }

            return listBean;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        return new ArrayList<MyBean>();
    }

    /**
     * @param model
     * @return
     */
    public ArrayList<MyBean> getSearchModel(String model) {

        listBean = new ArrayList<MyBean>();
        MyBean bean;

        query = "SELECT "
                //+ "   \"NIZ\"  , \"NAR\" ,\"FAS\"  ,\"RZMN\" || '-' || \"RZMK\" AS size   ,\"CNO\" ,\"CNVP\" , \"CNR\" ,\"CC\", prim, id "
                + "   \"NIZ\"  , \"NAR\" ,\"FAS\"  ,\"RZMN\" || '-' || \"RZMK\" AS size   ,\"CNO\" ,\"CNVP\" , \"CNR\" ,prim,\"POL\",\"NIZ1\",\"CC\", id "
                + "  FROM"
                + " \"C1_Cen\""
                + " WHERE "
                + "\"FAS\" = ?  AND status !='del'"
                + "ORDER BY \"FAS\"  , \"NAR\" , \"RZMN\" ";
        try {
            ps = getConn().prepareStatement(query);

            try {
                String fas = model;
                ps.setString(1, fas);

            } catch (NumberFormatException e) {
                return new ArrayList<MyBean>();
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("NAR"));
                bean = new MyBean(rs.getString("NIZ"),
                        rs.getString("NAR"),
                        rs.getString("FAS"),
                        rs.getString("size"),
                        rs.getString("CNO"),
                        rs.getString("CNVP"),
                        rs.getString("CNR"),
                        rs.getString("prim"),
                        rs.getString("POL"),
                        rs.getString("NIZ1"),
                        rs.getDouble("CC"),
                        rs.getInt("id"));
                listBean.add(bean);
            }

            return listBean;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        return new ArrayList<MyBean>();
    }

    /**
     * @param pol
     * @return
     */
    public ArrayList<MyBean> getSearchPol(String pol) {

        listBean = new ArrayList<MyBean>();
        MyBean bean;

        query = "SELECT "
                //  + "   \"NIZ\"  , \"NAR\" ,\"FAS\"  ,\"RZMN\" || '-' || \"RZMK\" AS size   ,\"CNO\" ,\"CNVP\" , \"CNR\" ,\"CC\" prim , id "
                + "   \"NIZ\"  , \"NAR\" ,\"FAS\"  ,\"RZMN\" || '-' || \"RZMK\" AS size   ,\"CNO\" ,\"CNVP\" , \"CNR\" ,prim,\"POL\",\"NIZ1\",\"CC\", id "
                + "  FROM"
                + " \"C1_Cen\""
                + " WHERE "
                + " \"POL\" like '"
                + pol
                + "%'"
                + " AND status !='del'"

                + "ORDER BY \"FAS\"  , \"NAR\" , \"RZMN\"";
        try {
            ps = getConn().prepareStatement(query);

            //ps.setString(1, pol);

            rs = ps.executeQuery();

            while (rs.next()) {
                // System.out.println(rs.getString("NAR"));
                bean = new MyBean(rs.getString("NIZ"),
                        rs.getString("NAR"),
                        rs.getString("FAS"),
                        rs.getString("size"),
                        rs.getString("CNO"),
                        rs.getString("CNVP"),
                        rs.getString("CNR"),
                        rs.getString("prim"),
                        rs.getString("POL"),
                        rs.getString("NIZ1"),
                        rs.getDouble("CC"),
                        rs.getInt("id"));
                listBean.add(bean);
            }

            return listBean;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        return new ArrayList<MyBean>();
    }

    /**
     * @param id
     * @return
     */
    public ValueCalculation getDataCalculation(int id) {

        ValueCalculation valueCalculation = new ValueCalculation();
        String[] nsr;
        double[] csr, wsr;

        nsr = new String[9];
        csr = new double[9];
        wsr = new double[9];

        query = "SELECT  "
                + " * "
                + "  FROM"
                + " \"C1_Cen\""
                + " WHERE "
                + " \"id\" =  ?  AND status !='del'";
        try {
            ps = getConn().prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {

                valueCalculation.setCc(rs.getDouble("CC"));
                valueCalculation.setCno(rs.getDouble("CNO"));
                valueCalculation.setCno2(rs.getDouble("CNO2"));
                valueCalculation.setCnr(rs.getDouble("CNR"));
                valueCalculation.setCnr2(rs.getDouble("CNR2"));
                valueCalculation.setCnv(rs.getDouble("CNV"));
                valueCalculation.setCnv2(rs.getDouble("CNV2"));
                valueCalculation.setCnvp(rs.getDouble("CNVP"));
                valueCalculation.setFas(rs.getString("FAS"));
                valueCalculation.setDat(rs.getString("DAT"));
                valueCalculation.setGrup(rs.getString("GRUP"));
                valueCalculation.setHzr(rs.getDouble("HZR"));
                valueCalculation.setHzrp(rs.getDouble("HZRP"));
                valueCalculation.setId(rs.getInt("id"));
                valueCalculation.setKmr(rs.getDouble("KMR"));
                valueCalculation.setKmrp(rs.getDouble("KMRP"));
                valueCalculation.setNar(rs.getString("NAR"));
                valueCalculation.setNiz(rs.getString("NIZ"));
                valueCalculation.setNiz1(rs.getString("NIZ1"));
                valueCalculation.setNiz2(rs.getString("NIZ2"));
                valueCalculation.setObr(rs.getString("OBR"));
                valueCalculation.setPol(rs.getString("POL"));
                valueCalculation.setPrb(rs.getDouble("PRB"));
                valueCalculation.setPrbp(rs.getDouble("PRBP"));
                valueCalculation.setPrr(rs.getDouble("PRR"));
                valueCalculation.setPrrp(rs.getDouble("PRRP"));
                valueCalculation.setPss(rs.getDouble("PSS"));
                valueCalculation.setPtk(rs.getString("PTK"));
                valueCalculation.setRzmk(rs.getString("RZMK"));
                valueCalculation.setRzmn(rs.getString("RZMN"));
                valueCalculation.setSm(rs.getDouble("SM"));
                valueCalculation.setSmtp(rs.getDouble("SMTP"));
                valueCalculation.setSmts(rs.getDouble("SMTS"));
                valueCalculation.setSmvp(rs.getDouble("SMVP"));
                valueCalculation.setSmvs(rs.getDouble("SMVS"));
                valueCalculation.setSn(rs.getDouble("SN"));
                valueCalculation.setSnp(rs.getDouble("SNP"));
                valueCalculation.setSyr2(rs.getString("SYR2"));
                valueCalculation.setSyr1(rs.getString("SYR1"));
                valueCalculation.setTe(rs.getDouble("TE"));
                valueCalculation.setTek(rs.getString("TEK"));
                valueCalculation.setTen(rs.getDouble("TEN"));
                valueCalculation.setTes(rs.getDouble("TES"));
                valueCalculation.setVid(rs.getString("VID"));
                valueCalculation.setVm(rs.getDouble("VM"));
                valueCalculation.setVmk(rs.getDouble("VMK"));
                valueCalculation.setVms(rs.getDouble("VMS"));
                valueCalculation.setVmtp(rs.getDouble("VMTP"));
                valueCalculation.setVmts(rs.getDouble("VMTS"));
                valueCalculation.setZpd(rs.getDouble("ZPD"));
                valueCalculation.setZpo(rs.getDouble("ZPO"));
                valueCalculation.setUsto(rs.getDouble("USTO"));
                valueCalculation.setZp(rs.getDouble("ZP"));
                valueCalculation.setCnvp(rs.getDouble("CNVP"));
                valueCalculation.setNumberPrice(rs.getInt("numberPrice"));
                valueCalculation.setPrim(rs.getString("prim"));

                // 22:08:2019 Буду добавлять мноооооого полей
                valueCalculation.setPercentCredit(rs.getDouble("PERCENT_CREDIT"));
                valueCalculation.setValueCredit(rs.getDouble("VALUE_CREDIT"));
                valueCalculation.setPrimeCostCredit(rs.getDouble("PRIMECOST_CREDIT"));
                valueCalculation.setProfitCredit(rs.getDouble("PROFIT_CREDIT"));

                valueCalculation.setCnoCredit(rs.getDouble("CNO_CREDIT"));
                valueCalculation.setCno2Credit(rs.getDouble("CNO2_CREDIT"));
                valueCalculation.setCnrCredit(rs.getDouble("CNR_CREDIT"));
                valueCalculation.setCnr2Credit(rs.getDouble("CNR2_CREDIT"));
                valueCalculation.setCnvCredit(rs.getDouble("CNV_CREDIT"));
                valueCalculation.setCnv2Credit(rs.getDouble("CNV2_CREDIT"));

                for (int i = 0; i < nsr.length; i++) {

                    nsr[i] = rs.getString("NSR" + (i + 1));
                    csr[i] = rs.getDouble("CSR" + (i + 1));
                    wsr[i] = rs.getDouble("WSR" + (i + 1));

                }
                valueCalculation.setNsr(nsr);
                valueCalculation.setCsr(csr);
                valueCalculation.setWsr(wsr);

                query = "SELECT  "
                        + " \"type_calculation\" "
                        + "  FROM "
                        + " \"group\""
                        + " WHERE "
                        + " \"id\" =  ? ";

                ps = getConn().prepareStatement(query);
                ps.setInt(1, Integer.valueOf(valueCalculation.getGrup()));
                rs = ps.executeQuery();

                while (rs.next()) {
                    valueCalculation.setId_type_calculation(rs.getInt("type_calculation"));
                }

            }

            return valueCalculation;
        } catch (Exception e) {
            System.err.print("получения калькуляции " + e.getMessage());
        }

        return null;
    }

    /**
     * @param id
     * @param status
     * @return
     */
    public Factor getFactor(int id, String status) {

        Factor factor;
        factor = new Factor();
        query = "SELECT "
                + "factor.smvp , factor.smtp , factor.vmtp , factor.prrp , factor.hzrp , factor.kmrp ,  type.age_type AS \"cnvp\" "
                + "FROM factor , type "
                + "WHERE factor.status = ? "
                + "AND factor.id_type_prod IN "
                + "(SELECT id_type_prod FROM \"group\" WHERE id IN "
                + "(SELECT \"GRUP\" FROM \"C1_Cen\" WHERE id =?)) "
                + "AND type.id IN "
                + "(SELECT id  FROM \"type\" WHERE id IN "
                + "(SELECT \"VID\" FROM \"C1_Cen\" WHERE id = ?)) "
                + "AND type.id_group IN "
                + "(SELECT id_group  FROM \"type\" WHERE id_group IN "
                + "(SELECT \"GRUP\" FROM \"C1_Cen\" WHERE id = ?)) ";

        try {
            ps = getConn().prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.setInt(3, id);
            ps.setInt(4, id);

            rs = ps.executeQuery();

            while (rs.next()) {

                factor.setCnvp(rs.getInt("cnvp"));
                factor.setHzrp(rs.getDouble("hzrp"));
                factor.setKmrp(rs.getDouble("kmrp"));
                factor.setPrrp(rs.getDouble("prrp"));
                factor.setSmtp(rs.getDouble("smtp"));
                factor.setSmvp(rs.getDouble("smvp"));
                factor.setVmtp(rs.getDouble("vmtp"));

            }

            return factor;
        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        return null;
    }

    /**
     * @param id
     * @param valueCalculation
     * @return
     */
    public boolean updateCalculation(int id, ValueCalculation valueCalculation) {
        query = "UPDATE "
                + " \"C1_Cen\" "
                + "SET   "
                + " \"NIZ\"= ?, \"FAS\"=?, \"NAR\"=?, \"POL\"=?, \"RZMN\"=?, \"RZMK\"=?, \"DAT\"=?,"
                + "   \"SM\"=?, \"TE\"=?, \"VM\"=?, "
                + "  \"ZP\"=?,  \"CC\"=?, "
                + "  \"PRB\"=?, \"CNO\"=?, \"CNO2\"=?, \"CNV\"=?,"
                + " \"CNV2\"=?, \"CNR\"=?, \"CNR2\"=?, \"NSR1\"=?, \"WSR1\"=?, \"CSR1\"=?, \"NSR2\"=?, "
                + " \"WSR2\"=?, \"CSR2\"=?, \"NSR3\"=?, \"WSR3\"=?, \"CSR3\"=?, \"NSR4\"=?, \"WSR4\"=?, "
                + " \"CSR4\"=?, \"NSR5\"=?, \"WSR5\"=?, \"CSR5\"=?, \"NSR6\"=?, \"WSR6\"=?, \"CSR6\"=?, "
                + " \"NSR7\"=?, \"WSR7\"=?, \"CSR7\"=?, \"NSR8\"=?, \"WSR8\"=?, \"CSR8\"=?, \"NSR9\"=?, "
                + " \"WSR9\"=?, \"CSR9\"=?, \"TEN\"=?, \"TES\"=?, \"VMS\"=?, "
                + "  \"PRRP\"=?, \"HZRP\"=?, \"KMRP\"=?,   \"PRBP\"=?, "
                + "  \"CNVP\"=?, "
                + "  \"SMTP\"=?, \"SMTS\"=?, \"SMVP\"=?, \"SMVS\"=?, \"VMTP\"=?, \"VMTS\"=?, "
                + " \"ZPO\"=?, \"ZPD\"=?, \"SN\"=?, \"PRR\"=?, \"HZR\"=?,  \"KMR\"=?, "
                + " \"NIZ1\"=?, \"NIZ2\"=?, \"SYR1\"=?, \"SYR2\"=?, \"OBR\"=?, \"USTO\"=?, "
                + " \"numberPrice\" =? ,\"SNP\"=? , \"PSS\" = ? , prim = ?, "
                + " \"PERCENT_CREDIT\"=?, \"VALUE_CREDIT\"=?, \"PRIMECOST_CREDIT\"=?, \"PROFIT_CREDIT\"=?, "
                + " \"CNO_CREDIT\"=?, \"CNO2_CREDIT\"=?, \"CNV_CREDIT\"=?, \"CNV2_CREDIT\"=?, \"CNR_CREDIT\"=?, \"CNR2_CREDIT\"=? "
                + " WHERE"
                + " \"id\"= ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setString(1, valueCalculation.getNiz());
            ps.setString(2, valueCalculation.getFas());
            ps.setString(3, valueCalculation.getNar());
            ps.setString(4, valueCalculation.getPol());
            ps.setString(5, valueCalculation.getRzmn());
            ps.setString(6, valueCalculation.getRzmk());

            ps.setString(7, new SimpleDateFormat("dd.MM.yyyy").format(new GregorianCalendar().getTime()));
            ps.setDouble(8, valueCalculation.getSm());
            ps.setDouble(9, valueCalculation.getTe());
            ps.setDouble(10, valueCalculation.getVm());
            ps.setDouble(11, valueCalculation.getZp());
            ps.setDouble(12, valueCalculation.getCc());
            ps.setDouble(13, valueCalculation.getPrb());
            ps.setDouble(14, valueCalculation.getCno());
            ps.setDouble(15, valueCalculation.getCno2());
            ps.setDouble(16, valueCalculation.getCnv());
            ps.setDouble(17, valueCalculation.getCnv2());
            ps.setDouble(18, valueCalculation.getCnr());
            ps.setDouble(19, valueCalculation.getCnr2());
            ps.setString(20, valueCalculation.getNsr()[0]);
            ps.setDouble(21, valueCalculation.getWsr()[0]);
            ps.setDouble(22, valueCalculation.getCsr()[0]);
            ps.setString(23, valueCalculation.getNsr()[1]);
            ps.setDouble(24, valueCalculation.getWsr()[1]);
            ps.setDouble(25, valueCalculation.getCsr()[1]);
            ps.setString(26, valueCalculation.getNsr()[2]);
            ps.setDouble(27, valueCalculation.getWsr()[2]);
            ps.setDouble(28, valueCalculation.getCsr()[2]);
            ps.setString(29, valueCalculation.getNsr()[3]);
            ps.setDouble(30, valueCalculation.getWsr()[3]);
            ps.setDouble(31, valueCalculation.getCsr()[3]);
            ps.setString(32, valueCalculation.getNsr()[4]);
            ps.setDouble(33, valueCalculation.getWsr()[4]);
            ps.setDouble(34, valueCalculation.getCsr()[4]);
            ps.setString(35, valueCalculation.getNsr()[5]);
            ps.setDouble(36, valueCalculation.getWsr()[5]);
            ps.setDouble(37, valueCalculation.getCsr()[5]);
            ps.setString(38, valueCalculation.getNsr()[6]);
            ps.setDouble(39, valueCalculation.getWsr()[6]);
            ps.setDouble(40, valueCalculation.getCsr()[6]);
            ps.setString(41, valueCalculation.getNsr()[7]);
            ps.setDouble(42, valueCalculation.getWsr()[7]);
            ps.setDouble(43, valueCalculation.getCsr()[7]);
            ps.setString(44, valueCalculation.getNsr()[8]);
            ps.setDouble(45, valueCalculation.getWsr()[8]);
            ps.setDouble(46, valueCalculation.getCsr()[8]);
            ps.setDouble(47, valueCalculation.getTen());
            ps.setDouble(48, valueCalculation.getTes());
            ps.setDouble(49, valueCalculation.getVms());

            ps.setDouble(50, valueCalculation.getPrrp());
            ps.setDouble(51, valueCalculation.getHzrp());
            ps.setDouble(52, valueCalculation.getKmrp());
            ps.setDouble(53, valueCalculation.getPrbp());
            ps.setDouble(54, valueCalculation.getCnvp());
            ps.setDouble(55, valueCalculation.getSmtp());
            ps.setDouble(56, valueCalculation.getSmts());
            ps.setDouble(57, valueCalculation.getSmvp());
            ps.setDouble(58, valueCalculation.getSmvs());
            ps.setDouble(59, valueCalculation.getVmtp());
            ps.setDouble(60, valueCalculation.getVmts());
            ps.setDouble(61, valueCalculation.getZpo());
            ps.setDouble(62, valueCalculation.getZpd());
            ps.setDouble(63, valueCalculation.getSn());
            ps.setDouble(64, valueCalculation.getPrr());
            ps.setDouble(65, valueCalculation.getHzr());
            ps.setDouble(66, valueCalculation.getKmr());
            ps.setString(67, valueCalculation.getNiz1());
            ps.setString(68, valueCalculation.getNiz2());
            ps.setString(69, valueCalculation.getSyr1());
            ps.setString(70, valueCalculation.getSyr2());
            ps.setString(71, valueCalculation.getObr());
            ps.setDouble(72, valueCalculation.getUsto());
            ps.setInt(73, valueCalculation.getNumberPrice());
            ps.setDouble(74, valueCalculation.getSnp());
            ps.setDouble(75, valueCalculation.getPss());
            ps.setString(76, valueCalculation.getPrim());
            // И сюда буду добавлять мноооооого полей

            ps.setDouble(77, valueCalculation.getPercentCredit());
            ps.setDouble(78, valueCalculation.getValueCredit());
            ps.setDouble(79, valueCalculation.getPrimeCostCredit());
            ps.setDouble(80, valueCalculation.getProfitCredit());

            ps.setDouble(81, valueCalculation.getCnoCredit());
            ps.setDouble(82, valueCalculation.getCno2Credit());

            ps.setDouble(83, valueCalculation.getCnvCredit());
            ps.setDouble(84, valueCalculation.getCnv2Credit());

            ps.setDouble(85, valueCalculation.getCnrCredit());
            ps.setDouble(86, valueCalculation.getCnr2Credit());

            ps.setInt(87, id);
            ps.executeUpdate();

            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Проверьте правильность ввода", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
            return false;

        }
    }

    //Новый расчет

    /**
     * @param ptk
     * @return
     */
    public int newCalcalculation(String ptk) {

        int group = 0, type = 0;

        query = "SELECT "
                + " \"id_type\" , \"id_group\" "
                + " FROM "
                + " \"assortment\""
                + " WHERE"
                + " \"ptk\" = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setString(1, ptk);
            ps.executeQuery();

            rs = ps.executeQuery();

            while (rs.next()) {

                group = rs.getInt("id_group");
                type = rs.getInt("id_type");

            }
            query = "INSERT INTO "
                    + " \"C1_Cen\" "
                    + "(  "
                    + " \"GRUP\" , \"VID\" , \"PTK\" , \"numberPrice\" ) "
                    + "VALUES(?,?,?,?)"
                    + " RETURNING \"id\" ";

            ps = getConn().prepareStatement(query);
            ps.setInt(1, group);
            ps.setInt(2, type);
            ps.setString(3, ptk);
            ps.setInt(4, 0);
            rs = ps.executeQuery();

            while (rs.next()) {

                return rs.getInt("id");

            }

        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return 0;

    }

    /**
     * @param prod
     */
    public void addProd(String prod) {

        String ptk;

        query = "SELECT "
                + " \"PTK\" "
                + " FROM "
                + " \"nsi_pkd\""
                + " WHERE"
                + " \"PTK\" =?";

        Random rand = new Random();
        ptk = String.valueOf(300 + rand.nextInt(500));

        System.out.println(ptk);

        try {

            ps = getConn().prepareStatement(query);
            ps.setString(1, ptk);
            ps.executeQuery();

            rs = ps.executeQuery();

            if (rs.next()) {
                addProd(prod);
            }

            query = "INSERT INTO "
                    + " \"nsi_pkd\" "
                    + "(  "
                    + " \"PTK\" , \"NPTK\"  )"
                    + "VALUES(?,?)";
            ps = getConn().prepareStatement(query);
            ps.setString(1, ptk);
            ps.setString(2, prod);
            ps.execute();

        } catch (Exception e) {

            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

    }

    /**
     * @return
     */
    public String[] getLastName() {

        String[] lastName;
        lastName = new String[10];

        query = "SELECT "
                + " \"Zam\" , \"PEO\" , \"Director\" ,\"ZamPost\" , \"PeoPost\" , \"DirectorPost\" , \"VesPost\" , \"VesName\" , \"ZamKomPost\"   , \"ZamKomName\"  "
                + " FROM "
                + " \"LastName\"";

        try {

            ps = getConn().prepareStatement(query);
            ps.executeQuery();
            rs = ps.executeQuery();

            while (rs.next()) {

                lastName[0] = rs.getString("Zam");
                lastName[1] = rs.getString("PEO");
                lastName[2] = rs.getString("Director");
                lastName[3] = rs.getString("ZamPost");
                lastName[4] = rs.getString("PeoPost");
                lastName[5] = rs.getString("DirectorPost");
                lastName[7] = rs.getString("VesPost");
                lastName[6] = rs.getString("VesName");
                lastName[8] = rs.getString("ZamKomPost");
                lastName[9] = rs.getString("ZamKomName");
                return lastName;

            }

        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return null;

    }

    /**
     * @param zam
     * @param peo
     */
    public void updateLastName(String zam, String peo, String director, String zamPost, String peoPost, String directorPost, String vesPost, String vesName) {

        query = "UPDATE "
                + "\"LastName\" "
                + " SET \"Zam\"  = ? , \"PEO\" = ? , \"Director\" = ? ,\"ZamPost\"  = ? , \"PeoPost\" = ? , \"DirectorPost\" = ? , \"VesPost\" = ? , \"VesName\" = ? ";

        try {
            ps = getConn().prepareStatement(query);
            ps.setString(1, zam);
            ps.setString(2, peo);
            ps.setString(3, director);
            ps.setString(4, zamPost);
            ps.setString(5, peoPost);
            ps.setString(6, directorPost);
            ps.setString(8, vesPost);
            ps.setString(7, vesName);

            ps.executeUpdate();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PricePDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @return
     */
    public ArrayList<Factor> getFactorAll() {

        ArrayList<Factor> listFactor = new ArrayList<Factor>();

        Factor factor;

        query = "SELECT "
                + " factor.smvp , factor.smtp , factor .vmtp , factor.prrp , factor.hzrp ,"
                + " factor.kmrp ,  factor.status , type_prod.name   , factor.id"
                + " FROM \"factor\" , type_prod "
                + "WHERE factor.id_type_prod = type_prod.id "
                + " ORDER BY id";

        try {
            ps = getConn().prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                factor = new Factor();

                factor.setHzrp(rs.getDouble("hzrp"));
                factor.setKmrp(rs.getDouble("kmrp"));
                factor.setPrrp(rs.getDouble("prrp"));
                factor.setSmtp(rs.getDouble("smtp"));
                factor.setSmvp(rs.getDouble("smvp"));
                factor.setVmtp(rs.getDouble("vmtp"));
                if (rs.getString("status").equals("old")) {

                    factor.setStatus("Старые");
                } else {
                    factor.setStatus("Новые");
                }
                factor.setType(rs.getString("name"));
                factor.setId(rs.getInt("id"));

                listFactor.add(factor);

            }

            return listFactor;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        return new ArrayList<Factor>();
    }

    /**
     * @param factor
     * @return
     */
    public boolean updateFactor(Factor factor) {

        query = "UPDATE "
                + "\"factor\" "
                + "set   "
                + " smvp = ? , smtp = ? , vmtp = ? , prrp  = ?, hzrp  = ?,"
                + " kmrp = ?  "
                + " WHERE"
                + " id = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setDouble(1, factor.getSmvp());
            ps.setDouble(2, factor.getSmtp());
            ps.setDouble(3, factor.getVmtp());
            ps.setDouble(4, factor.getPrrp());
            ps.setDouble(5, factor.getHzrp());
            ps.setDouble(6, factor.getKmrp());
            ps.setInt(7, factor.getId());

            ps.executeUpdate();

            return true;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return false;

    }

    /**
     * @param name
     * @param idTypeCalculation
     * @return
     */
    public boolean addPrice(String name, int idTypeCalculation) {

        query = "INSERT INTO "
                + "price "
                + "(name , id_type_calculation ) "
                + " VALUES  "
                + "( ?, ? )";

        try {

            ps = getConn().prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, idTypeCalculation);

            ps.executeUpdate();

            return true;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return false;

    }

    /**
     * @param idTypeCalculation
     * @return
     */
    public ArrayList<Price> getPrice(int idTypeCalculation) {

        ArrayList<Price> listPrice = new ArrayList<Price>();

        Price price;

        query = "SELECT "
                + " * "
                + " FROM \"price\"  "
                + "WHERE id_type_calculation =" + idTypeCalculation
                + " ORDER BY \"name\" ASC ";

        try {
            ps = getConn().prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {

                price = new Price();
                price.setId(rs.getInt("id"));
                price.setId_type_calculation(idTypeCalculation);
                price.setName(rs.getString("name"));

                listPrice.add(price);

            }

            return listPrice;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        return new ArrayList<Price>();
    }

    /**
     * @param idTypeCalculation
     * @return
     */
    public ArrayList<Price> getPriceNoInclude(int idTypeCalculation) {

        ArrayList<Price> listPrice = new ArrayList<Price>();

        Price price;

        query = "SELECT "
                + " * "
                + " FROM \"price\"  "
                + "WHERE id_type_calculation =" + idTypeCalculation
                + " ORDER BY \"name\" ASC ";

        try {
            ps = getConn().prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getInt("id") != 0) {
                    price = new Price();
                    price.setId(rs.getInt("id"));
                    price.setId_type_calculation(idTypeCalculation);
                    price.setName(rs.getString("name"));

                    listPrice.add(price);
                }
            }

            return listPrice;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        return new ArrayList<Price>();
    }

    /**
     * @param id
     * @return
     */
    public String getPriceName(int id) {

        // if(id==0){
        //    return "  ";
        // }
        query = "SELECT "
                + " name "
                + " FROM \"price\"  "
                + "WHERE id =" + id;

        try {
            ps = getConn().prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {

                return rs.getString("name");

            }

            return "";
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        return "";
    }

    /**
     * @param id
     * @return
     */
    public ArrayList<MyBean> getListPriceCen(int id) {

        listBean = new ArrayList<MyBean>();
        MyBean bean;

        query = "SELECT "
                + "   \"NIZ\"  , \"NAR\" ,\"FAS\"  ,\"RZMN\" || '-' || \"RZMK\" AS size   ,\"CNO\" ,\"CNVP\" , \"CNR\", \"CC\",prim, id "
                + "  FROM"
                + " \"C1_Cen\""
                + " WHERE "
                + "\"numberPrice\" = ?  AND status !='del'"
                + " ORDER BY \"FAS\"  , \"NAR\" , \"RZMN\"  ";
        try {
            ps = getConn().prepareStatement(query);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            while (rs.next()) {
                //System.out.print(rs.getString("NAR"));
                bean = new MyBean(rs.getString("NIZ"),
                        rs.getString("NAR"),
                        rs.getString("FAS"),
                        rs.getString("size"),
                        rs.getString("CNO"),
                        rs.getString("CNVP"),
                        rs.getString("CNR"),
                        rs.getString("prim"),
                        rs.getInt("id"));
                listBean.add(bean);
            }

            return listBean;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        return new ArrayList<MyBean>();
    }

    /**
     * @return the conn
     */
    public Connection getConn() {
        return conn;
    }

    /**
     * @param conn the conn to set
     */
    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**
     * @param prim
     * @return
     */
    public boolean setPrim(String prim) {

        query = "UPDATE "
                + "\"prim_price\" "
                + "set   "
                + " \"prim\" = ? ";

        try {

            ps = getConn().prepareStatement(query);
            ps.setString(1, prim);
            ps.executeUpdate();

            return true;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return false;

    }

    /**
     * @return
     */
    public String getPrim() {

        query = "SELECT prim "
                + " FROM \"prim_price\" ";

        try {

            ps = getConn().prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {

                return rs.getString("prim");

            }

            return "";
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        return "";

    }

    /**
     * @param idGroup
     * @return
     */
    public int getTypeProdByGroup(int idGroup) {

        listBean = new ArrayList<MyBean>();
        MyBean bean;

        query = "SELECT "
                + "   id_type_prod  "
                + "  FROM"
                + " \"group\" "
                + " WHERE "
                + "id = ? ";
        try {
            ps = getConn().prepareStatement(query);
            ps.setInt(1, idGroup);

            rs = ps.executeQuery();

            while (rs.next()) {
                return rs.getInt("id_type_prod");

            }

            return 0;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        return 0;
    }

    /**
     * @param id
     * @return
     */
    public ArrayList<ValueCalculation> getCheckCalc(ArrayList<Integer> id) {

        ArrayList<ValueCalculation> list = new ArrayList<ValueCalculation>();

        query = "SELECT  "
                + " * "
                + "  FROM"
                + " \"C1_Cen\""
                + " WHERE "
                + " \"id\" = any(array[?])   AND status !='del' ";
        try {
            ps = getConn().prepareStatement(query);
            ps.setArray(1, conn.createArrayOf("int", id.toArray(new Integer[id.size()])));
            rs = ps.executeQuery();

            while (rs.next()) {
                ValueCalculation valueCalculation = new ValueCalculation();

                valueCalculation.setCc(rs.getDouble("CC"));
                valueCalculation.setCno(rs.getDouble("CNO"));
                valueCalculation.setCno2(rs.getDouble("CNO2"));
                valueCalculation.setCnr(rs.getDouble("CNR"));
                valueCalculation.setCnr2(rs.getDouble("CNR2"));
                valueCalculation.setCnv(rs.getDouble("CNV"));
                valueCalculation.setCnv2(rs.getDouble("CNV2"));
                valueCalculation.setCnvp(rs.getDouble("CNVP"));
                valueCalculation.setFas(rs.getString("FAS"));
                valueCalculation.setDat(rs.getString("DAT"));
                valueCalculation.setGrup(rs.getString("GRUP"));
                valueCalculation.setHzr(rs.getDouble("HZR"));
                valueCalculation.setHzrp(rs.getDouble("HZRP"));
                valueCalculation.setId(rs.getInt("id"));
                valueCalculation.setKmr(rs.getDouble("KMR"));
                valueCalculation.setKmrp(rs.getDouble("KMRP"));
                valueCalculation.setNar(rs.getString("NAR"));
                valueCalculation.setNiz(rs.getString("NIZ"));
                valueCalculation.setNiz1(rs.getString("NIZ1"));
                valueCalculation.setNiz2(rs.getString("NIZ2"));
                valueCalculation.setObr(rs.getString("OBR"));
                valueCalculation.setPol(rs.getString("POL"));
                valueCalculation.setPrb(rs.getDouble("PRB"));
                valueCalculation.setPrbp(rs.getDouble("PRBP"));
                valueCalculation.setPrr(rs.getDouble("PRR"));
                valueCalculation.setPrrp(rs.getDouble("PRRP"));
                valueCalculation.setPss(rs.getDouble("PSS"));
                valueCalculation.setPtk(rs.getString("PTK"));
                valueCalculation.setRzmk(rs.getString("RZMK"));
                valueCalculation.setRzmn(rs.getString("RZMN"));
                valueCalculation.setSm(rs.getDouble("SM"));
                valueCalculation.setSmtp(rs.getDouble("SMTP"));
                valueCalculation.setSmts(rs.getDouble("SMTS"));
                valueCalculation.setSmvp(rs.getDouble("SMVP"));
                valueCalculation.setSmvs(rs.getDouble("SMVS"));
                valueCalculation.setSn(rs.getDouble("SN"));
                valueCalculation.setSnp(rs.getDouble("SNP"));
                valueCalculation.setSyr2(rs.getString("SYR2"));
                valueCalculation.setSyr1(rs.getString("SYR1"));
                valueCalculation.setTe(rs.getDouble("TE"));
                valueCalculation.setTek(rs.getString("TEK"));
                valueCalculation.setTen(rs.getDouble("TEN"));
                valueCalculation.setTes(rs.getDouble("TES"));
                valueCalculation.setVid(rs.getString("VID"));
                valueCalculation.setVm(rs.getDouble("VM"));
                valueCalculation.setVmk(rs.getDouble("VMK"));
                valueCalculation.setVms(rs.getDouble("VMS"));
                valueCalculation.setVmtp(rs.getDouble("VMTP"));
                valueCalculation.setVmts(rs.getDouble("VMTS"));
                valueCalculation.setZpd(rs.getDouble("ZPD"));
                valueCalculation.setZpo(rs.getDouble("ZPO"));
                valueCalculation.setUsto(rs.getDouble("USTO"));
                valueCalculation.setZp(rs.getDouble("ZP"));
                valueCalculation.setCnvp(rs.getDouble("CNVP"));
                valueCalculation.setNumberPrice(rs.getInt("numberPrice"));
                list.add(valueCalculation);
            }
            return list;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        return null;
    }

    /**
     * @param id
     * @param name
     */
    public void renameGroup(int id, String name) {

        query = "UPDATE "
                + "\"group\" "
                + "set   "
                + " \"name\" = ? "
                + " WHERE id = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

    }

    /**
     * @param id
     * @param name
     */
    public void renameType(int id, String name) {
        query = "UPDATE "
                + "\"type\" "
                + "set   "
                + " \"name\" = ? "
                + " WHERE id = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

    }

    /**
     * @param ptk
     * @param name
     */
    public void renameAssortment(String ptk, String name) {
        query = "UPDATE "
                + "\"nsi_pkd\" "
                + "set   "
                + " \"NPTK\" = ? "
                + " WHERE \"PTK\" = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, ptk);
            ps.executeUpdate();

        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
    }

    /**
     * @param id
     */
    public void deleteGroup(int id) {
        /*
        query = "DELETE FROM "
                + "\"group\" "
                + " WHERE id = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
     */
    }

    /**
     * @param id
     */
    public void deleteType(int id) {
        /*
        query = "DELETE FROM"
                + "\"type\" "
                + " WHERE id = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setInt(1, id);
            ps.execute();

        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        */
    }

    /**
     * @param id
     */
    public void deleteTypeGroup(int id) {
        /*
        query = "DELETE FROM"
                + "\"type\" "
                + " WHERE id_group = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setInt(1, id);
            ps.execute();

        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
         */
    }

    /**
     * @param ptk
     */
    public void deletePtk(String ptk) {
        /*
        query = "DELETE FROM"
                + "\"nsi_pkd\" "
                + " WHERE \"PTK\" = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setString(1, ptk);
            ps.execute();

        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
          */
    }

    /**
     * @param id
     */
    public void deleteCalcGroup(int id) {
        /*
        query = "DELETE FROM"
                + "\"C1_Cen\" "
                + " WHERE \"GRUP\" = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setInt(1, id);
            ps.execute();

        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        */
    }

    /**
     * @param id
     */
    public void deleteCalcType(int id) {
        /*
        query = "DELETE FROM"
                + "\"C1_Cen\" "
                + " WHERE \"VID\" = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setInt(1, id);
            ps.execute();

        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
 */
    }

    /**
     * @param ptk
     */
    public void deleteCalcPtk(String ptk) {
        /*
        query = "DELETE FROM"
                + "\"C1_Cen\" "
                + " WHERE \"PTK\" = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setString(1, ptk);
            ps.execute();

        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        */
    }

    /**
     * @param id
     */
    public void deleteAssortmentGroup(int id) {
        
        /*
        query = "DELETE FROM"
                + "\"assortment\" "
                + " WHERE id_group = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setInt(1, id);
            ps.execute();

        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
       */
    }

    /**
     * @param id
     */
    public void deleteAssortmentType(int id) {
        /*
        query = "DELETE FROM"
                + "\"assortment\" "
                + " WHERE id_type = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setInt(1, id);
            ps.execute();

        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
        */
    }

    /**
     * @param id
     */
    public void deleteAssortmentPrk(int id) {

        query = "DELETE FROM"
                + "\"assortment\" "
                + " WHERE ptk = ?";

        try {
            ps = getConn().prepareStatement(query);
            ps.setString(1, id + "");
            ps.execute();
        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }
    }

    /**
     * @param id
     */
    public void deleteCalc(int id) {
        query = "UPDATE "
                + "\"C1_Cen\" "
                + " SET status =?"
                + " WHERE id = ?";

        try {

            ps = getConn().prepareStatement(query);
            ps.setString(1, "del");
            ps.setInt(2, id);
            ps.execute();

        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

    }

    public double[] getKurs() {
        query = "SELECT * "
                + " FROM \"kurs\" ";
        double[] curr = {0.0, 0.0, 0.0};

        try {

            ps = getConn().prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {

                curr[0] = rs.getDouble("kurs_ru");
                curr[1] = rs.getDouble("kurs_usd");
                curr[2] = rs.getDouble("kurs_eur");
                return curr;
            }

        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());

        }
        return curr;
    }

    public void updateKurs(double kurs_ru, double kurs_usd, double kurs_eur) {
        query = "UPDATE "
                + "\"kurs\" "
                + "set   "
                + " \"kurs_ru\" =  " + kurs_ru + ", \"kurs_usd\" =  " + kurs_usd + ", \"kurs_eur\" =  " + kurs_eur;

        try {

            ps = getConn().prepareStatement(query);

            ps.execute();


        } catch (SQLException e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

    }

    public ArrayList<MyBean> getSearch(String ptk, String fas) {

        listBean = new ArrayList<MyBean>();
        MyBean bean;

        query = "SELECT "
                + "   \"NIZ\"  , \"NAR\" ,\"FAS\"  ,\"RZMN\" || '-' || \"RZMK\" AS size   ,\"CNO\" ,\"CNVP\" , \"CNR\" , prim ,\"POL\",\"NIZ1\" ,\"CC\",  id "
                + "  FROM"
                + " \"C1_Cen\""
                + " WHERE "
                + "\"PTK\"= ?"
                + " AND"
                + " \"FAS\" like '"
                + fas
                + "%'"
                + " AND status !='del'"

                + "ORDER BY \"FAS\"  , \"NAR\" , \"RZMN\"";
        try {
            ps = getConn().prepareStatement(query);
            ps.setString(1, ptk);

            //ps.setString(1, pol);

            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.print(rs.getString("NAR"));
                bean = new MyBean(rs.getString("NIZ"),
                        rs.getString("NAR"),
                        rs.getString("FAS"),
                        rs.getString("size"),
                        rs.getString("CNO"),
                        rs.getString("CNVP"),
                        rs.getString("CNR"),
                        rs.getString("prim"),
                        rs.getString("POL"),
                        rs.getString("NIZ1"),
                        rs.getDouble("CC"),
                        rs.getInt("id"));
                listBean.add(bean);
            }

            return listBean;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        return new ArrayList<MyBean>();
    }

    public ArrayList<MyBean> getSearchArt(String ptk, String nar) {

        listBean = new ArrayList<MyBean>();
        MyBean bean;

        query = "SELECT "
                + "   \"NIZ\"  , \"NAR\" ,\"FAS\"  ,\"RZMN\" || '-' || \"RZMK\" AS size   ,\"CNO\" ,\"CNVP\" , \"CNR\" , prim ,\"POL\",\"NIZ1\" ,\"CC\",  id "
                + "  FROM"
                + " \"C1_Cen\""
                + " WHERE "
                + "\"PTK\"= ?"
                + " AND"
                + " \"NAR\" like '"
                + nar
                + "%'"
                + " AND status !='del'"

                + "ORDER BY \"FAS\"  , \"NAR\" , \"RZMN\"";
        try {
            ps = getConn().prepareStatement(query);
            ps.setString(1, ptk);

            //ps.setString(1, pol);

            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.print(rs.getString("NAR"));
                bean = new MyBean(rs.getString("NIZ"),
                        rs.getString("NAR"),
                        rs.getString("FAS"),
                        rs.getString("size"),
                        rs.getString("CNO"),
                        rs.getString("CNVP"),
                        rs.getString("CNR"),
                        rs.getString("prim"),
                        rs.getString("POL"),
                        rs.getString("NIZ1"),
                        rs.getDouble("CC"),
                        rs.getInt("id"));
                listBean.add(bean);
            }

            return listBean;
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            System.err.print("Ошибка при входе в систему: " + e.getMessage());
        }

        return new ArrayList<MyBean>();
    }
}

