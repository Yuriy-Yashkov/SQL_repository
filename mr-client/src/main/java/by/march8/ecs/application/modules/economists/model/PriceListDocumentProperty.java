package by.march8.ecs.application.modules.economists.model;

import by.march8.api.utils.DateUtils;

import java.util.Date;

/**
 * @author Andy 15.03.2017.
 */
public class PriceListDocumentProperty {

    private String type;
    private String number;
    private Date date;
    private double tradeAllowanceValue;

    private String file;

    public static PriceListDocumentProperty parse(String fileName) {
        boolean parsingError = false;
        // Имя файла не задано
        if (fileName == null) {
            System.out.println("Имя файла прейскуранта не задано");
            return null;
        }

        int pointIndex = fileName.indexOf(".");

        if (pointIndex < 0) {
            System.out.println("Ошибка в имени файла прейскуранта");
            return null;
        }

        String[] obj = fileName.substring(0, pointIndex).split("_");

        // У имени файла не верная сигнатура
        if (obj.length != 7) {
            System.out.println("Парсер: Файл имеет неизвестную сигнатуру");
            parsingError = true;
        }

        if (!obj[0].toLowerCase().equals("прейскурант")) {
            System.out.println("Парсер: В сигнатуре файла не задан тип документа (прейскурант)");
            parsingError = true;
        }

        if (!obj[1].toLowerCase().equals("уценка")) {
            System.out.println("Парсер: В сигнатуре файла не задана категория документа (уценка)");
            parsingError = true;
        }

        if (obj[2].length() < 2) {
            System.out.println("Парсер: В сигнатуре файла не задан номер документа (№ХХ)");
            parsingError = true;
        }

        if (obj[3].length() < 1) {
            System.out.println("Парсер: В сигнатуре файла не задан ставка надбавки документа (Х)");
            parsingError = true;
        }

        if (obj[4].length() != 2) {
            System.out.println("Парсер: В сигнатуре файла указанный номер дня не соответствует шаблону (XX)");
            parsingError = true;
        }

        if (obj[5].length() != 2) {
            System.out.println("Парсер: В сигнатуре файла указанный номер месяца не соответствует шаблону (XX)");
            parsingError = true;
        }

        if (obj[6].length() != 2) {
            System.out.println("Парсер: В сигнатуре файла указанный номер года не соответствует шаблону (XX)");
            parsingError = true;
        }

        if (parsingError) {
            System.out.println("Парсер: В сигнатуре файла [" + fileName + "] обнаружены ошибки");
            return null;
        }

        PriceListDocumentProperty result = new PriceListDocumentProperty();
        result.setType("Прейскурант уцененных изделий");
        result.setNumber(obj[2].substring(1, obj[2].length()));

        double percentValue;
        try {
            percentValue = Double.valueOf(obj[3]);
        } catch (Exception e) {
            percentValue = 0.0;
        }

        result.setTradeAllowanceValue(percentValue);
        result.setDate(DateUtils.getDateByStringValueSimple(obj[4] + "." + obj[5] + "." + obj[6]));
        return result;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public double getTradeAllowanceValue() {
        return tradeAllowanceValue;
    }

    public void setTradeAllowanceValue(final double tradeAllowanceValue) {
        this.tradeAllowanceValue = tradeAllowanceValue;
    }

    public String getFile() {
        return file;
    }

    public void setFile(final String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return type +
                " №" + number +
                " от " + DateUtils.getNormalDateFormat(date) +
                " c процентом ТН: " + tradeAllowanceValue;
    }
}
