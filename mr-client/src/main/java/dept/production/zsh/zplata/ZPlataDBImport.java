package dept.production.zsh.zplata;


import by.march8.ecs.framework.common.LogCrutch;

/**
 *
 * @author lidashka
 */
public class ZPlataDBImport extends DB_temp /*PDB_new*/ {
    // private static final Logger log = new Log().getLoger(ZPlataDBImport.class);
    private static final LogCrutch log = new LogCrutch();

    public boolean deleteItemsDataReport(long date, String kodBrig) throws Exception {
        String sql = "";
        boolean rezalt = false;

        try {

            sql = "Delete from zsalary_zsc where masterdate = ? and brigadid = ?";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(date));
            ps.setString(2, kodBrig);
            ps.execute();

            rezalt = true;

        } catch (Exception e) {
            rezalt = false;
            System.err.println("Ошибка deleteItemsDataReport() " + e);
            log.error("Ошибка deleteItemsDataReport()", e);
            throw new Exception("Ошибка deleteItemsDataReport() " + e.getMessage(), e);
        }

        return rezalt;
    }

    public boolean importItemDataReport(long date,
                                        String tabel,
                                        int kodOp,
                                        double arg,
                                        double arg2,
                                        double hours,
                                        double days,
                                        double sum,
                                        String kodBrig,
                                        String job) throws Exception {
        String sql = "";
        boolean rezalt = false;


        try {

            sql = "Insert into zsalary_zsc( "
                    + " masterid," +
                    "  masterdate," +
                    "  rootid," +
                    "  label," +
                    "  adate," +
                    "  indate," +
                    "  methodid," +
                    "  arg," +
                    "  arg2," +
                    "  hours," +
                    "  days," +
                    "  flag," +
                    "  calcsum," +
                    "  creatorid," +
                    "  finaldate," +
                    "  startdate,"
                    + "  departid, "
                    + "  brigadid,"
                    + "  jobid ) "
                    + " values( ?, ?, ?, ?, ?,"
                    + "         ?, ?, ?, ?, ?,"
                    + "         ?, ?, ?, ?, ?,"
                    + "         ?, ?, ?, ?)";


            ps = conn.prepareStatement(sql);
            ps.setInt(1, 1);
            ps.setDate(2, new java.sql.Date(date));
            ps.setString(3, tabel);
            ps.setString(4, "");
            ps.setDate(5, new java.sql.Date(date));
            ps.setDate(6, new java.sql.Date(date));
            ps.setInt(7, kodOp);
            ps.setDouble(8, arg);
            ps.setDouble(9, arg2);
            ps.setDouble(10, hours);
            ps.setDouble(11, days);
            ps.setString(12, "+");
            ps.setDouble(13, sum);
            ps.setInt(14, 1);
            ps.setDate(15, new java.sql.Date(date));
            ps.setDate(16, new java.sql.Date(date));
            ps.setString(17, "-1");
            ps.setString(18, kodBrig);
            ps.setString(19, job);
            ps.execute();

            rezalt = true;
        } catch (Exception e) {
            System.err.println("Ошибка importItemDataReport() " + e);
            log.error("Ошибка importItemDataReport()", e);
            throw new Exception("Ошибка importItemDataReport() " + e.getMessage(), e);
        }

        return rezalt;
    }
}
