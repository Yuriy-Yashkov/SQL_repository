package dept.production.planning.ean;

/**
 * @author lidashka
 */
class EAN13 {
    private int prefix;
    private int index;
    private int checkDigit;

    public EAN13() {

    }

    public int getPrefix() {
        return prefix;
    }

    public void setPrefix(int prefix) {
        this.prefix = prefix;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCheckDigit() {
        return checkDigit;
    }

    public void setCheckDigit(int checkDigit) {
        this.checkDigit = checkDigit;
    }

    public String generateEancode() {
        try {
            char[] code = new char[13];

            for (int i = 0; i < code.length; i++)
                code[i] = '0';

            char[] prefixData = (String.valueOf(prefix)).toCharArray();
            System.arraycopy(prefixData, 0, code, 0, prefixData.length);

            char[] indexData = (String.valueOf(index)).toCharArray();
            System.arraycopy(indexData, 0, code, 7, indexData.length < 6 ? indexData.length : 5);

            int checkSum = generateCheckSum(code);
            code[12] = String.valueOf(checkSum).charAt(0);

            return String.valueOf(code);

        } catch (Exception e) {
            return null;
        }
    }

    public String generateEancodeByEanсode(String eancodeWithColor) {
        try {
            char[] code = new char[13];

            for (int i = 0; i < code.length; i++)
                code[i] = '0';

            char[] prefixData = (String.valueOf(prefix)).toCharArray();
            System.arraycopy(prefixData, 0, code, 0, prefixData.length);

            char[] eancodeWithColorData = eancodeWithColor.toCharArray();
            System.arraycopy(eancodeWithColorData, 3, code, 3, eancodeWithColorData.length - 3);

            int checkSum = generateCheckSum(code);
            code[12] = String.valueOf(checkSum).charAt(0);

            return String.valueOf(code);

        } catch (Exception e) {
            return null;
        }
    }

    protected int generateCheckSum(char[] data) {
        int result = 0;
        for (int i = 0; i < 12; i++) {
            int num = Character.getNumericValue(data[i]);
            num = (i % 2 == 0) ? num : num * 3;
            result += num;
        }

        result = (result % 10 == 0) ? 0 : ((result / 10) + 1) * 10 - result;
        return result;
    }
}
