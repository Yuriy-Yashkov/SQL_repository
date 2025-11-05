package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentEntity;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.uno.UnoRuntime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Прототип отчета, обкатка технологии
 *
 * @author Andy 04.08.2016.
 */
public class SummaryStatementByContractorReport extends AbstractCustomSpreadsheetReport {

    private final String queryGetDocsByCriteria = "select driving.DOCUMENT_NUMBER, otgruz.ndoc, otgruz.item_id, otgruz.status, otgruz.operac from DRIVING_DIRECTION_DOCUMENT as driving " +
            "JOIN (select item_id, ndoc, status, operac, kpl from otgruz1 )as otgruz on otgruz.item_id  = driving.SALE_DOCUMENT_ID " +
            "where ((driving.DOCUMENT_DATE >= CONVERT(DATETIME, ?, 102))and(driving.DOCUMENT_DATE <= CONVERT(DATETIME, ?, 102)))and(otgruz.status = 0)" +
            "and (otgruz.kpl = ?) ORDER BY driving.DOCUMENT_DATE";

    private int contractorCode;
    private String contractorName;
    private Date beginDate;
    private Date endDate;
    private SaleDocumentDataProvider provider;


    public SummaryStatementByContractorReport(final int contractorCode, final Date beginDate, final Date endDate, String contractorName) {

        super();
        this.contractorCode = contractorCode;
        this.contractorName = contractorName;
        this.beginDate = beginDate;
        this.endDate = endDate;

        provider = new SaleDocumentDataProvider();
        create();
    }

    @Override
    ReportProperties prepareProperties(final ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("summary_statement_" + contractorCode);
        properties.getTemplate().setTemplateName("firmMag.ots");
        return properties;
    }

    @Override
    boolean populateData(final XComponent component) {
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            xNamed.setName("Сводная ведомость по " + contractorName);
            // Заполнение тела документа


            float charSize = 10;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 25;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.LeftLine = aLine;
            aBorder.TopLine = aLine;
            aBorder.RightLine = aLine;
            aBorder.IsBottomLineValid = true;
            aBorder.IsLeftLineValid = true;
            aBorder.IsRightLineValid = true;
            aBorder.IsTopLineValid = true;

            setPropertyAndValue(xSpreadsheet, 0, 0, "Сводная ведомость по: " + contractorName + "\n Период: " + DateUtils.getNormalDateFormat(beginDate) + " по " + DateUtils.getNormalDateFormat(endDate), 11, aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 0, 1, "Накладная", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 1, 1, "Приложение", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 2, 1, "Количество", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 3, 1, "Сум_Опт", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 4, 1, "НДС", charSize, aBorder,
                    "CENTER");
            setPropertyAndValue(xSpreadsheet, 5, 1, "Сум_ТН", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 6, 1, "НДС_ТН", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 7, 1, "ВсегоНДС", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 8, 1, "Розн_ДЕТ", charSize,
                    aBorder, "CENTER");
            setPropertyAndValue(xSpreadsheet, 9, 1, "Розн_ВЗР", charSize,
                    aBorder, "CENTER");

            int lineStart = 2;

            for (SaleDocumentEntity entity : documentList) {
                try {
                    SaleDocumentReport item = provider.prepareDocument(entity.getId(), false);


                    if (item != null) {

                        final SaleDocumentBase documentBase = item.getDocument();
                        final HashMap<String, Object> detailMap = item.getDetailMap();
                        final TotalSummingUp summingUp = item.getSummingUp();


                        setPropertyAndValue(xSpreadsheet, 0, lineStart,
                                documentBase.getDocumentNumber(), charSize, aBorder,
                                "LEFT");
                        setPropertyAndValue(xSpreadsheet, 1, lineStart,
                                "'" + detailMap.get("DOCUMENT_NUMBER"), charSize, aBorder,
                                "LEFT");
                        setPropertyAndValue(xSpreadsheet, 2, lineStart, String.valueOf(summingUp.getAmount())
                                , charSize, aBorder, "RIGHT");
                        setPropertyAndValue(xSpreadsheet, 3, lineStart, String.valueOf(summingUp.getValueSumCost())
                                , charSize, aBorder, "RIGHT");
                        setPropertyAndValue(xSpreadsheet, 4, lineStart, String.valueOf(summingUp.getValueSumVat())
                                , charSize, aBorder,
                                "RIGHT");
                        setPropertyAndValue(xSpreadsheet, 5, lineStart, String.valueOf(summingUp.getValueSumAllowance())
                                , charSize, aBorder,
                                "RIGHT");
                        setPropertyAndValue(xSpreadsheet, 6, lineStart, String.valueOf(summingUp.getValueSumVatRetail())
                                , charSize, aBorder, "RIGHT");
                        setPropertyAndValue(xSpreadsheet, 7, lineStart, String.valueOf(summingUp.getValueSumVatRetail() + summingUp.getValueSumVat())
                                , charSize, aBorder,
                                "RIGHT");

                        if (documentBase.getDocumentVatValue() == 10.0) {
                            setPropertyAndValue(xSpreadsheet, 8, lineStart, String.valueOf(summingUp.getValueSumCostRetail())
                                    , charSize, aBorder,
                                    "RIGHT");
                            setPropertyAndValue(xSpreadsheet, 9, lineStart,
                                    "0", charSize, aBorder,
                                    "RIGHT");
                        } else if (documentBase.getDocumentVatValue() == 20.0) {
                            setPropertyAndValue(xSpreadsheet, 9, lineStart,
                                    String.valueOf(summingUp.getValueSumCostRetail()), charSize, aBorder,
                                    "RIGHT");
                            setPropertyAndValue(xSpreadsheet, 8, lineStart,
                                    "0", charSize, aBorder,
                                    "RIGHT");
                        }
                        lineStart++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            setPropertyAndValue(xSpreadsheet, 0, lineStart, "", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 1, lineStart, "ИТОГО:", charSize,
                    aBorder, "LEFT");
            setPropertyAndValue(xSpreadsheet, 2, lineStart, "=SUM(C1:C"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 3, lineStart, "=SUM(D1:D"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 4, lineStart, "=SUM(E1:E"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 5, lineStart, "=SUM(F1:F"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 6, lineStart, "=SUM(G1:G"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 7, lineStart, "=SUM(H1:H"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 8, lineStart, "=SUM(I1:I"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            setPropertyAndValue(xSpreadsheet, 9, lineStart, "=SUM(J1:J"
                            + String.valueOf(lineStart) + ")", charSize, aBorder,
                    "RIGHT");
            lineStart++;
            setPropertyAndValue(
                    xSpreadsheet,
                    9,
                    lineStart,
                    "=SUM(I" + String.valueOf(lineStart) + ":J"
                            + String.valueOf(lineStart) + ")", charSize,
                    aBorder, "RIGHT");

        } catch (Exception e) {
            System.out.println("Ошибка формирования отчета ");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    boolean getDocumentList(final Connection connection) throws Exception {
        documentList = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement(queryGetDocsByCriteria);

        ps.setDate(1, new java.sql.Date(beginDate.getTime()));
        ps.setDate(2, new java.sql.Date(endDate.getTime()));
        ps.setInt(3, contractorCode);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            SaleDocumentEntity item = new SaleDocumentEntity();
            item.setId(rs.getInt("item_id"));
            item.setDocumentDate(new Date());
            item.setDocumentNumber(rs.getString("ndoc"));
            item.setDocumentStatus(rs.getInt("status"));
            documentList.add(item);
        }

        return true;
    }

    @Override
    public void useToken() {

    }
}
