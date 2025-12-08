package by.march8.ecs.application.modules.cut.report;

import by.march8.ecs.application.modules.cut.model.CuttingRouteRow;
import by.march8.ecs.application.modules.cut.model.SewingRouteRow;

import java.util.List;

public class OdtTableBuilder {

    public static String buildCuttingRouteRows(List<CuttingRouteRow> rows) {
        StringBuilder sb = new StringBuilder();

        for (CuttingRouteRow row : rows) {
            sb.append("<table:table-row>");

            sb.append(cell(row.getArticleCanvas()));
            sb.append(cell(row.getName()));
            sb.append(cell(row.getArticle()));       // ← исправлено
            sb.append(cell(row.getModelNumber()));
            sb.append(cell(row.getColor()));
            sb.append(cell(row.getSize()));
            sb.append(cell(row.getHeight()));
            sb.append(cell(row.getQuantity()));
            sb.append(cell(row.getBrigade()));       // ← добавлено
            sb.append(cell(row.getCodeList()));      // ← добавлено
            sb.append(cell(row.getSewDate()));

            sb.append("</table:table-row>");
        }

        return sb.toString();
    }

    public static String buildSewingRows(List<SewingRouteRow> rows) {
        StringBuilder sb = new StringBuilder();

        for (SewingRouteRow row : rows) {
            sb.append("<table:table-row>");

            sb.append(cell(row.getArticle()));
            sb.append(cell(row.getName()));
            sb.append(cell(row.getArticle2()));
            sb.append(cell(row.getModelNumber()));
            sb.append(cell(row.getColor()));
            sb.append(cell(row.getGrade()));
            sb.append(cell(row.getSize()));
            sb.append(cell(row.getHeight()));
            sb.append(cell(row.getQuantity()));
            sb.append(cell(row.getBrigade()));   // добавлено
            sb.append(cell(row.getCodeList()));  // добавлено
            sb.append(cell(row.getSewDate()));   // добавлено

            sb.append("</table:table-row>");
        }

        return sb.toString();
    }

    private static String cell(Object text) {
        return "<table:table-cell><text:p>" +
                (text == null ? "" : text.toString()) +
                "</text:p></table:table-cell>";
    }
}
