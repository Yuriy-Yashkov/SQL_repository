package image;

import by.gomel.freedev.ucframework.common.DirectoryScanner;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.framework.common.Settings;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Andy 20.12.2017.
 */
public class TestImageProcessing {

    private String scanDir = "\\\\file-server3\\datamatrix\\resize";
    private String treeDirFile = "tree.list";

    @Test
    @Ignore
    public void testThreadDirectoryScanner() {

        String scanDir = "\\\\file-server3\\datamatrix\\resize";
        DirectoryScanner scanner;
        ArrayList<String> list = new ArrayList<>();

        try {
            scanner = new DirectoryScanner(scanDir, list);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        Thread t = new Thread(scanner);
        t.start();
    }

    @Test
    @Ignore
    public void testDirectoryScanner() {

        DirectoryScanner scanner;
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> listTemp = new ArrayList<>();

        try {
            scanner = new DirectoryScanner(scanDir, list);
            scanner.start();
            //scanner.saveToFile(scanDir + "\\"+treeDirFile);
            FileUtils.writeLines(new File(scanDir + "\\" + treeDirFile), list);

            for (String s : list) {
                listTemp.add(s.replace("\\\\file-server3\\datamatrix\\resize\\", Settings.WINDOWS_CATALOG_PATH));
            }

            FileUtils.writeLines(new File(scanDir + "\\" + treeDirFile + ".windows"), listTemp);

            listTemp.clear();
            for (String s : list) {
                listTemp.add(s.replace("\\\\file-server3\\datamatrix\\resize\\", Settings.UNIX_CATALOG_PATH).replace("\\", "/"));
            }

            FileUtils.writeLines(new File(scanDir + "\\" + treeDirFile + ".linux"), listTemp);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


/*        for (String s : list) {
            File f = new File(s);
            if (f.isDirectory()) {
                System.out.println(s);
            }
        }*/
    }

    @Test
    @Ignore
    public void testLoadTreeDir() {
        List<String> list = null;
        try {
            list = FileUtils.readLines(new File(scanDir + "\\" + treeDirFile), "utf-8");
            int dirCount = 0;
            for (String s : list) {

                File f = new File(s);
                if (f.isDirectory()) {
                    //System.out.println(s);
                    dirCount++;
                }

            }
            System.out.println("Найдено " + dirCount + " каталогов");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void testRegex() {
        List<String> list = null;
        try {
            list = FileUtils.readLines(new File(scanDir + "\\" + treeDirFile), "utf-8");
            int dirCount = 0;
            for (String s : list) {

                if (s.contains("7231")) {
                    System.out.println(s);
                }
/*
                Pattern pattern = Pattern.compile("'5605'(.*?)");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find())
                {
                    System.out.println(matcher.group(1));
                }

                */

            }
            System.out.println("Найдено " + dirCount + " каталогов");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean uniqueImageIsExist(List<String> list, String number) {
        for (String s : list) {
            if (s.toLowerCase().endsWith(number + ".jpg")) {
                return true;
            }
        }
        return false;
    }

    private boolean imageIsExist(List<String> list, String number) {
        for (String s : list) {
            if (s.contains(File.separator + number)) {
                if (s.toLowerCase().endsWith(".jpg")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Test
    @Ignore
    public void testDifference() {
        String sourceListFile = "model.txt";
        String imageListFile = "tree.list.windows";
        String path = "image/";

        List<String> imageList = new ArrayList<>();

        List<String> outList = new ArrayList<>();
        List<String> source = null;

        try {

            source = FileUtils.readLines(new File(path + sourceListFile), "utf-8");
            imageList = FileUtils.readLines(new File(path + imageListFile), "utf-8");

            int count = 0;
            outList.add("****************************************************************************************");
            outList.add("Актуальность базы изображений [" + imageList.get(0) + "]");
            outList.add("Всего загружено [" + source.size() + "] моделей, ищем изображения по маске [НОМЕР_МОДЕЛИ.jpg]");
            outList.add("****************************************************************************************");
            for (String s : source) {

                File f = new File(s);
                if (!uniqueImageIsExist(imageList, s)) {
                    count++;
                    outList.add(s);
                }
            }

            outList.add("Найдено [" + count + "] моделей из [" + source.size() + "] не имеющих изображений по маске [НОМЕР_МОДЕЛИ.jpg]");
            count = 0;
            outList.add("\n\n\n\n\n\n\n\n\n");
            outList.add("****************************************************************************************");
            outList.add("Просмотр базы на наличие изображений вообще...");
            outList.add("****************************************************************************************");
            for (String s : source) {
                if (!imageIsExist(imageList, s)) {
                    count++;
                    outList.add(s);
                }
            }
            outList.add("Найдено [" + count + "] моделей из [" + source.size() + "] не имеющих никаких изображений ");
            FileUtils.writeLines(new File(path + "image_report_" + DateUtils.getNormalDateFormatDelimiter(new Date()) + ".txt"), outList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testFOrm() {


    }
}
