package prices;

import by.march8.ecs.application.modules.marketing.model.ScaleItem;
import by.march8.ecs.application.modules.marketing.model.ScaleItemPreliminary;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 01.11.2017.
 */

public class TestPrepareScaleItem {

    @Test
    @Ignore
    public void parseScaleList() {
        // MarketingPriceListMarchJDBC db = new MarketingPriceListMarchJDBC();

        List<ScaleItemPreliminary> list = prepareScaleList();//db.getGetScaleItemListByItemId(42327523, 1);
        for (ScaleItemPreliminary item : list) {
            System.out.println(item);
        }

        createScales(list);

        // db.disConn();
    }

    private void createScales(List<ScaleItemPreliminary> list) {

        double currentSize = 0;
        double lastSize = 0;

        List<ScaleItem> scaleList = new ArrayList<>();

        for (ScaleItemPreliminary aList : list) {
            lastSize = aList.getPrice();
            if (currentSize != lastSize) {
                scaleList.add(new ScaleItem(aList.getSize(), aList.getSize(), (float) aList.getPrice()));
                currentSize = lastSize;
            } else {
                scaleList.get(scaleList.size() - 1).setMaxSize(aList.getSize());
            }
        }

        for (ScaleItem item : scaleList) {
            System.out.println(item);
        }
    }

    private List<ScaleItemPreliminary> prepareScaleList() {
        ArrayList<ScaleItemPreliminary> result = new ArrayList<ScaleItemPreliminary>();

        result.add(new ScaleItemPreliminary(56, 6.5));
        result.add(new ScaleItemPreliminary(60, 6.5));
        result.add(new ScaleItemPreliminary(64, 6.5));
        result.add(new ScaleItemPreliminary(68, 6.5));

        result.add(new ScaleItemPreliminary(72, 7.0));
        result.add(new ScaleItemPreliminary(76, 7.0));
        result.add(new ScaleItemPreliminary(80, 7.0));
        result.add(new ScaleItemPreliminary(84, 7.0));

        result.add(new ScaleItemPreliminary(88, 7.1));


        return result;
    }
}
