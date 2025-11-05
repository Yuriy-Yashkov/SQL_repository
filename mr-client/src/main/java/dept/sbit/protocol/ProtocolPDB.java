package dept.sbit.protocol;

import by.march8.ecs.framework.common.LogCrutch;
import workDB.PDB_new;

import java.util.Vector;

//import org.apache.log4j.Logger;

/**
 *
 * @author lidashka
 */
public class ProtocolPDB extends PDB_new {

    //private static final Logger log = new Log().getLoger(ProtocolPDB.class);
    private static final LogCrutch log = new LogCrutch();

    public Vector searchSarInPlanProduction(Object fas, int idPlan, boolean flagAllPlan) throws Exception {
        Vector elements = new Vector();
        String sql = "";

        try {

            if (flagAllPlan) {
                sql = "Select distinct sar "
                        + " From plan, plan_item "
                        + " Where plan.id = plan_item.id_plan and "
                        + "     fas::text like ? "
                        + "Order by sar ";


                if (fas != null) {
                    ps = conn.prepareStatement(sql);
                    ps.setObject(1, fas);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        elements.add(rs.getInt("sar"));
                    }
                }
            } else {
                sql = "Select distinct sar "
                        + " From plan, plan_item "
                        + " Where plan.id = plan_item.id_plan and "
                        + "     plan.id = ? and "
                        + "     fas::text like ? "
                        + "Order by sar ";

                if (fas != null) {
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, idPlan);
                    ps.setObject(2, fas);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        elements.add(rs.getInt("sar"));
                    }
                }
            }
        } catch (Exception e) {
            elements = new Vector();
            System.err.println("Ошибка searchSarInPlanProduction() " + e);
            log.error("Ошибка searchSarInPlanProduction()", e);
            throw new Exception("Ошибка searchSarInPlanProduction() " + e.getMessage(), e);
        }
        return elements;
    }

}
