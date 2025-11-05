package by.march8.ecs.application.modules.art.report;

import by.march8.ecs.application.modules.art.model.ProductionReportData;
import by.march8.ecs.application.modules.art.model.ProductionReportItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andy 24.01.2017.
 */
public class SecondProductionReport {
    protected String month;
    private List<ProductionReportItem> date;

    public SecondProductionReport(final ProductionReportData report, int monthId) {
        date = report.getData();

        switch (monthId) {
            case 1:
                month = "Январь";
                break;
            case 2:
                month = "Февраль";
                break;
            case 3:
                month = "Март";
                break;
            case 4:
                month = "Апрель";
                break;
            case 5:
                month = "Май";
                break;
            case 6:
                month = "Июнь";
                break;
            case 7:
                month = "Июль";
                break;
            case 8:
                month = "Август";
                break;
            case 9:
                month = "Сентябрь";
                break;
            case 10:
                month = "Октябрь";
                break;
            case 11:
                month = "Ноябрь";
                break;
            case 12:
                month = "Декабрь";
                break;
            default:
                month = "------";
                break;
        }

        Creation(date);
        //ean();

    }

    //List<FirstProductionReportModel>
    private void Creation(List<ProductionReportItem> date) {

        Map<String, List<ProductionReportItem>> map = new HashMap<>();
        //map = date.stream().collect(groupingBy(ProductionReportItem::getArticleName));
        for (ProductionReportItem item : date) {
            if (!map.containsKey(item.getArticleName())) {
                List<ProductionReportItem> buf = new ArrayList<>();
                buf.add(item);
                map.put(item.getArticleName(), buf);
            } else {
                map.get(item.getArticleName()).add(item);
            }
            //            List<ProductionReportItem> 0= map.put(item.getArticleName(), );
        }
        System.out.println(map);
    }

//    private void ean(){
//        DB db = new DB();
//            db.del_dublecate_EAN();
//        //db.eanTransfer();
//    }

}
