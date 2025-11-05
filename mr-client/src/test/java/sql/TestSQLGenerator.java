package sql;

import org.junit.Ignore;
import org.junit.Test;

public class TestSQLGenerator {
    @Test
    @Ignore
    public void testGenerateLikeExpression() {
        String field = "articleNumber";
        String[] s = new String[]{"7С", "8С"};

        System.out.println(generateLikeExpression(field, s));

    }

    public String generateLikeExpression(String field, String[] list) {
        if (list == null) {
            return "";
        }

        if (list.length < 1) {
            return "";
        }
        StringBuilder result = new StringBuilder(" (");
        for (String s : list) {
            result.append(field).append(" LIKE '").append(s).append("%' OR ");
        }
        result = new StringBuilder(result.substring(0, result.length() - 4));
        result.append(") ");
        return result.toString();
    }
}
