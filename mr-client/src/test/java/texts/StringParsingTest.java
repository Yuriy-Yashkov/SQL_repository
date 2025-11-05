package texts;

import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.economists.model.PriceListDocumentProperty;
import by.march8.ecs.application.modules.economists.report.OfficeImporter;
import by.march8.ecs.framework.helpers.digits.CurrencyType;
import by.march8.ecs.framework.helpers.digits.DigitToWords;
import by.march8.entities.classifier.RemainPriceListDetailItem;
import org.junit.Ignore;
import org.junit.Test;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 15.03.2017.
 */

public class StringParsingTest {

    private static String ConvertAccountingToNewView(String oldView) {

        if (oldView == null) {
            return "";
        }

        if (oldView.trim().equals("")) {
            return "";
        }

        if (oldView.trim().length() < 28) {
            return oldView.trim();
        }

        String oldViewTrim = oldView.trim();

        if (oldViewTrim.length() < 34 && oldViewTrim.length() >= 28) {
            System.out.println("TUT");
            oldViewTrim = oldViewTrim.replace(" ", "");
            String outString = "";
            System.out.println(oldViewTrim);
            int c = 1;
            for (int i = 0; i < oldViewTrim.length(); i++) {
                outString += oldViewTrim.charAt(i);
                if (c == 4) {
                    outString += " ";
                    c = 1;
                    continue;
                }
                c++;
            }
            return outString.trim();
        } else {
            return oldViewTrim;
        }
    }

    @Test
    @Ignore
    public void testDigitToWord() {
        String s = new DigitToWords(12535.112535, CurrencyType.BYN).num2str();
        System.out.println(s);
    }

    @Test
    @Ignore
    public void testContractorParser() {
        final String pl = "123456789";
        //String v = pl.substring(1, 1);
        System.out.println(pl.substring(2, 3));
    }

    @Test
    @Ignore
    public void testFileNamePriceListParsingTest() {
        String fileName = "прейскурант_уценка_№2_5%_01_02_17.ods";
        System.out.println(PriceListDocumentProperty.parse(fileName));
    }

    @Test
    @Ignore
    public void testDialog() {
        String fileName = "прейскурант_уценка_№2_5%_01_02_17.ods";
        PriceListDocumentProperty property = PriceListDocumentProperty.parse(fileName);

        String[] buttons = {"Перезаписать прейскурант", "Добавить в прейскурант", "Отмена"};
        int rc = JOptionPane.showOptionDialog(null, "<html><div style=\"text-align: left;\">Выбранный прейскурант уцененных изделий:<br>" +
                        "<p><p>Документ №" + "<font color=\"blue\">" + property.getNumber() + "</font> от " +
                        "<font color=\"blue\">" + DateUtils.getNormalDateFormat(property.getDate()) + "</font> с торговой надбавкой " +
                        "<font color=\"blue\">" + property.getTradeAllowanceValue() + "%</font>" +
                        "<p><p>уже присутствует в базе, укажите дальнейшие действия...", "Импорт прейскуранта уценки",
                JOptionPane.INFORMATION_MESSAGE, 1, null, buttons, buttons[2]);

        System.out.println(rc);
    }

    @Test
    @Ignore
    public void testOpenOffice() {
        // Импорт спецификации прейскуранта из ODS документа
        OfficeImporter importer = new OfficeImporter("c://прейскурант.ods");
        List<RemainPriceListDetailItem> detailList = importer.getPriceListDetail();
        for (RemainPriceListDetailItem item : detailList) {
            System.out.println(item.getArticleNumber());
        }
    }

    @Test
    @Ignore
    public void testParse() {
        String inString = "\\\\file-server\\catalog\\resize\\СУВЕНИРКА\\МУЖСКОЙ АССОРТИМЕНТ\\26960_4_джемпер.jpg";
        String[] parsArray;
        String key;

        parsArray = inString.split(File.separator + File.separator);
        key = parsArray[parsArray.length - 1];
        System.err.println(key + "[" + inString + "]");
    }

    @Test
    @Ignore
    public void testAccounting() {
        System.out.println(ConvertAccountingToNewView("BY86 BLBB3  0141200000000300800"));
    }

    //@Test
    public void testCreateSaleDocumentNumber() {
        List<String> list = new ArrayList<>();
        String ndoc = "яя128534";

        list.add(ndoc);
//        list.add(ndoc+"_1");
//        list.add(ndoc+"_2");
//        list.add(ndoc+"_5");

        System.out.println(ndoc + " - " + getDocumentNumber(ndoc, list));
    }

    public String getDocumentNumber(String documentNumber, List<String> list) {
        int prefixIndex = 0;
        for (String s : list) {
            if (s != null) {
                String[] seg = s.split("_");
                if (seg.length > 1) {
                    int curr = 0;
                    try {
                        curr = Integer.valueOf(seg[1]);
                        if (curr > prefixIndex) {
                            prefixIndex = curr;
                        }
                    } catch (Exception e) {
                        // Постфикс задан не по формату
                        e.printStackTrace();
                    }
                }
            }
        }
        //Инкремент постфикс-индекса
        prefixIndex++;
        return documentNumber + "_" + prefixIndex;
    }

    @Test
    @Ignore
    public void testSizeReplacer() {
        String s = "84";
        String[] params = s.split(",");
        if (params.length > 1) {
            String[] sizeMin = params[0].split("-");
            if (sizeMin.length > 1) {
                System.out.println(sizeMin[1]);
            }
        } else {
            System.out.println(s);
        }
    }

}
