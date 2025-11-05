package by.march8.ecs.application.modules.warehouse.internal.displacement.dao;

/**
 * @author Andy 26.07.2016.
 */
public class DisplacementQueries {
    public static String allDisplacementsByPeriod =
            "select * from v_vnperem1 where ((date >= CONVERT(DATETIME, ?, 102))and(date <= CONVERT(DATETIME, ?, 102))) " +
                    " order by date";
}

