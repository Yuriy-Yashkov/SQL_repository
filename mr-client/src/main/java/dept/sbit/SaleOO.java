package dept.sbit;

import by.march8.ecs.application.modules.marketing.model.ModelImageSize;
import by.march8.ecs.framework.common.LogCrutch;
import by.march8.ecs.services.images.ImageService;
import by.march8.ecs.services.images.ModelImageService;
import com.sun.star.awt.Size;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameContainer;
import com.sun.star.container.XNamed;
import com.sun.star.drawing.FillStyle;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.drawing.XDrawPageSupplier;
import com.sun.star.drawing.XShape;
import com.sun.star.frame.XModel;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.sheet.XCellAddressable;
import com.sun.star.sheet.XSheetAnnotation;
import com.sun.star.sheet.XSheetAnnotationAnchor;
import com.sun.star.sheet.XSheetAnnotationShapeSupplier;
import com.sun.star.sheet.XSheetAnnotations;
import com.sun.star.sheet.XSheetAnnotationsSupplier;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.CellAddress;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.UnoRuntime;
import common.UtilFunctions;
import dept.MyReportsModule;
import dept.tools.imgmanager.ImageTools;
import workOO.OO_new;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//import org.apache.log4j.Logger;

@SuppressWarnings("all")
/** Класс для работы с документами OpenOffice в группе [СБЫТ] */
public class SaleOO extends OO_new {

    //private static final Logger log = new Log().getLoger(SaleOO.class);
    private static final LogCrutch log = new LogCrutch();
    com.sun.star.beans.PropertyValue[] lParams = new com.sun.star.beans.PropertyValue[0];
    String savePath = MyReportsModule.confPath + "/Enakl";
    private ArrayList<Object[]> dataArray;

    public SaleOO(ArrayList<Object[]> data) {
        super();
        this.dataArray = data;
    }

    @SuppressWarnings("deprecation")
    public void createReport(String nameTamplates, String pathSave) {
        try {
            if (pathSave.equals("".toString()) || pathSave == null) {
                savePath = MyReportsModule.confPath + "/Enakl";
            } else savePath = pathSave;
            connect();
            XComponent currentDocument = openDocumentOld("Templates/"
                    + nameTamplates);
            if (nameTamplates.equals("ПриложениеЭлНакл.ots")) {
                createAppTN(currentDocument);
            }

        } catch (java.lang.Exception e) {
            System.err
                    .println("Ошибка метода createReport(String nameTamplates)"
                            + e);
            log.error("Ошибка метода createReport(String nameTamplates)", e);
        }
    }

    /**
     * Метод загружает выбранное по fName изображение и возвращает его размер
     * как Size()
     */

    private Size getImageSize(String fName) {
        BufferedImage img = null;
        // Обработка исключений при открытии файла спрайта
        try {
            img = ImageIO.read(new File(fName));
        } catch (IOException e) {
            // Действия в случае возникшего исключения
            System.err.println("Ошибка метода getImageSize(String fName)" + e);
            log.error("Ошибка метода getImageSize(String fName)", e);
            img = null;
            return new Size(200, 250);
        }
        // Возвращаем результат
        return new Size(img.getWidth(), img.getHeight());
    }

    private Size scaleSize(Size size, int percent) {
        if (size.Height > 300) {
            return new Size(size.Width / percent, size.Height / percent);
        } else {
            return size;
        }
    }

    /**
     * Метод добавляет аннотацию (примечание) к ячейке и в качестве фона
     * помещает фотографию соответствующую индексу модели передаваемой
     * параметром
     */
    public void insertAnnotationToDoc(XSpreadsheetDocument document,
                                      XSpreadsheet sheet, XCell cell, String idImage) {
        // *********************************************************************************
        // Блок добавления примечания к ячейке
        // **********************************************************************************
        try {
            // Получаем ссылку на ячейку
            XCellAddressable xCellAddr = (XCellAddressable) UnoRuntime
                    .queryInterface(XCellAddressable.class, cell);
            CellAddress aAddress = xCellAddr.getCellAddress();
            // Добавляем в ячейку примечание
            XSheetAnnotationsSupplier xAnnotationsSupp = (XSheetAnnotationsSupplier) UnoRuntime
                    .queryInterface(XSheetAnnotationsSupplier.class, sheet);
            XSheetAnnotations xAnnotations = xAnnotationsSupp.getAnnotations();
            xAnnotations.insertNew(aAddress, "Модель : " + idImage);

            XSheetAnnotationAnchor xAnnotAnchor = (XSheetAnnotationAnchor) UnoRuntime
                    .queryInterface(XSheetAnnotationAnchor.class, cell);
            XSheetAnnotation xAnnot = xAnnotAnchor.getAnnotation();
            XSheetAnnotationShapeSupplier xSheetAnnotationShapeSupplier = (XSheetAnnotationShapeSupplier) UnoRuntime
                    .queryInterface(XSheetAnnotationShapeSupplier.class, xAnnot);
            XShape xShape = xSheetAnnotationShapeSupplier.getAnnotationShape();

            String homePath = getHomePath();
            String sPath = homePath + "/catalog/" + idImage + ".jpg";
            String index;
            Size size;
            String pref = "";
            if (!UtilFunctions.isWindows()) {
                pref = "/";
            }
            // ПРоверка на наличие файла перед открытием
            if ((new File(sPath)).exists()) {
                sPath = "file:" + pref + homePath + "/catalog/" + idImage
                        + ".jpg";
                index = idImage;
                size = getImageSize(homePath + "/catalog/" + idImage
                        + ".jpg");
                System.out.println("Фото " + sPath + " загружено");
            } else {
                System.out.println("Фото " + sPath + " не найдено");
                sPath = "file:" + pref + homePath + "/catalog/noimage.jpg";
                index = "noimage";
                size = new Size(200, 250);
            }

            //sPath = "file:///C:/noimage.jpg";
            // index = "noimage";

            xShape.setSize(new Size(size.Width * 27, size.Height * 27));
            XModel xSpreadsheetModel = (XModel) UnoRuntime.queryInterface(
                    XModel.class, document);
            XMultiServiceFactory xmsf = (XMultiServiceFactory) UnoRuntime
                    .queryInterface(XMultiServiceFactory.class,
                            xSpreadsheetModel);
            XNameContainer xBitmapContainer = (XNameContainer) UnoRuntime
                    .queryInterface(XNameContainer.class, xmsf
                            .createInstance("com.sun.star.drawing.BitmapTable"));

            if (!xBitmapContainer.hasByName(index))
                xBitmapContainer.insertByName(index, sPath);
            String internalURL = AnyConverter.toString(xBitmapContainer
                    .getByName(index));
            Object imgTemp = xmsf
                    .createInstance("com.sun.star.drawing.GraphicObjectShape");
            XShape xImage = (XShape) UnoRuntime.queryInterface(XShape.class,
                    imgTemp);
            XPropertySet xpi = (XPropertySet) UnoRuntime.queryInterface(
                    XPropertySet.class, xImage);
            xpi.setPropertyValue("GraphicURL", internalURL);
            Size imgSize = size;
            xImage.setSize(imgSize);

            XPropertySet xpa = (XPropertySet) UnoRuntime.queryInterface(
                    XPropertySet.class, xShape);
            xpa.setPropertyValue("FillStyle", FillStyle.BITMAP);
            xpa.setPropertyValue("FillBitmapURL", internalURL);

        } catch (Exception exInternal) {
            log.error("Ошибка в методе insertAnnotationToDoc() ", exInternal);
            System.err.println("Ошибка в методе insertAnnotationToDoc(): "
                    + exInternal.getMessage());
        }
    }

    private void setCustomHeight(XCellRange range, int width) {
        com.sun.star.table.XColumnRowRange xColRowRange = (com.sun.star.table.XColumnRowRange)
                UnoRuntime.queryInterface(com.sun.star.table.XColumnRowRange.class, range);
        com.sun.star.table.XTableRows xRows = xColRowRange.getRows();
        try {
            Object aRowObj = xRows.getByIndex(0);
            XPropertySet xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(
                    com.sun.star.beans.XPropertySet.class, aRowObj);
            xPropSet.setPropertyValue("Height", new Integer(width));
        } catch (Exception e) {
            System.out.println("Ошибка");
        }
    }

    public int insertImageToDocOld(XSpreadsheetDocument document,
                                   XSpreadsheet sheet, XCell cell, String idImage, int align) {

        // *********************************************************************************
        // Блок добавления примечания к ячейке
        // **********************************************************************************
        try {

            String homePath = getHomePath();
            String sPath = homePath + "/catalog/" + idImage + ".jpg";
            String index;
            Size size;

            String pref = "";
            if (!UtilFunctions.isWindows()) {
                pref = "/";
            }
            // ПРоверка на наличие файла перед открытием
            if ((new File(sPath)).exists()) {
                ImageTools imgTools = new ImageTools();
                BufferedImage buffImage = imgTools.imageOpen(sPath);
                BufferedImage imgWater = imgTools.placeTextOnImage(buffImage, "www.8marta.com");
                imgTools.imageSaveAs(imgWater, System.getProperty("user.home"), "/temp.jpg");

               /* sPath = "file:" + pref + homePath + "/catalog/" + idImage
                        + ".jpg";
                        */
                if (UtilFunctions.isWindows()) {
                    sPath = "file:///" + pref + System.getProperty("user.home") + "/temp.jpg";
                } else {
                    sPath = "file:/" + pref + System.getProperty("user.home") + "/temp.jpg";
                }

                System.out.println(sPath);
                index = idImage;
                size = getImageSize(System.getProperty("user.home") + "/temp.jpg");
                //System.out.println("Фото " + sPath + " загружено");

            } else {
                //System.out.println("Фото " + sPath + " не найдено");
                sPath = "file:" + pref + homePath + "/catalog/noimage.jpg";
                index = "noimage";
                size = new Size(100, 150);
            }

            XModel xSpreadsheetModel = (XModel) UnoRuntime.queryInterface(
                    XModel.class, document);
            XMultiServiceFactory xmsf = (XMultiServiceFactory) UnoRuntime
                    .queryInterface(XMultiServiceFactory.class,
                            xSpreadsheetModel);

            XNameContainer xBitmapContainer = (XNameContainer) UnoRuntime
                    .queryInterface(XNameContainer.class, xmsf
                            .createInstance("com.sun.star.drawing.BitmapTable"));

            if (!xBitmapContainer.hasByName(index))
                xBitmapContainer.insertByName(index, sPath);
            String internalURL = AnyConverter.toString(xBitmapContainer
                    .getByName(index));
           /* Object imgTemp = xmsf
                    .createInstance("com.sun.star.drawing.GraphicObjectShape");

            XShape xImage = (XShape) UnoRuntime.queryInterface(XShape.class,
                    imgTemp);
            */

            Object oGraphic = xmsf.createInstance("com.sun.star.drawing.GraphicObjectShape");

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.awt.Point p;

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, cell);
            p = (com.sun.star.awt.Point) xPropSet.getPropertyValue("Position");

            XPropertySet xGraphicProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, oGraphic);
            xGraphicProps.setPropertyValue("GraphicURL", internalURL);

            XShape xGraphicShape = (XShape) UnoRuntime.queryInterface(XShape.class, oGraphic);

            XDrawPageSupplier xDrawPageSupplier = (XDrawPageSupplier) UnoRuntime.queryInterface(XDrawPageSupplier.class, sheet);
            XDrawPage xDrawPage = xDrawPageSupplier.getDrawPage();

            try {
                xDrawPage.add(xGraphicShape);
                xGraphicShape.setSize(new Size(size.Width * 19, size.Height * 19));
                xGraphicShape.setPosition(p);
            } catch (Exception e) {
                System.out.println("Ошибка");
            }


            cell = sheet.getCellByPosition(0, align);
            //cell.setFormula(hm[4].toString());

            // Получаем ссылку на ячейку
            XCellAddressable xCellAddr = (XCellAddressable) UnoRuntime
                    .queryInterface(XCellAddressable.class, cell);
            CellAddress aAddress = xCellAddr.getCellAddress();


            // Добавляем в ячейку примечание
            XSheetAnnotationsSupplier xAnnotationsSupp = (XSheetAnnotationsSupplier) UnoRuntime
                    .queryInterface(XSheetAnnotationsSupplier.class, sheet);
            XSheetAnnotations xAnnotations = xAnnotationsSupp.getAnnotations();
            xAnnotations.insertNew(aAddress, "Модель : " + idImage);

            XSheetAnnotationAnchor xAnnotAnchor = (XSheetAnnotationAnchor) UnoRuntime
                    .queryInterface(XSheetAnnotationAnchor.class, cell);
            XSheetAnnotation xAnnot = xAnnotAnchor.getAnnotation();
            XSheetAnnotationShapeSupplier xSheetAnnotationShapeSupplier = (XSheetAnnotationShapeSupplier) UnoRuntime
                    .queryInterface(XSheetAnnotationShapeSupplier.class, xAnnot);
            XShape xShape = xSheetAnnotationShapeSupplier.getAnnotationShape();


            xShape.setSize(new Size(size.Width * 27, size.Height * 27));
            xSpreadsheetModel = (XModel) UnoRuntime.queryInterface(
                    XModel.class, document);
            xmsf = (XMultiServiceFactory) UnoRuntime
                    .queryInterface(XMultiServiceFactory.class,
                            xSpreadsheetModel);
            xBitmapContainer = (XNameContainer) UnoRuntime
                    .queryInterface(XNameContainer.class, xmsf
                            .createInstance("com.sun.star.drawing.BitmapTable"));

            if (!xBitmapContainer.hasByName(index))
                xBitmapContainer.insertByName(index, sPath);
            internalURL = AnyConverter.toString(xBitmapContainer
                    .getByName(index));
            Object imgTemp = xmsf
                    .createInstance("com.sun.star.drawing.GraphicObjectShape");
            XShape xImage = (XShape) UnoRuntime.queryInterface(XShape.class,
                    imgTemp);
            XPropertySet xpi = (XPropertySet) UnoRuntime.queryInterface(
                    XPropertySet.class, xImage);
            xpi.setPropertyValue("GraphicURL", internalURL);
            Size imgSize = size;
            xImage.setSize(imgSize);

            XPropertySet xpa = (XPropertySet) UnoRuntime.queryInterface(
                    XPropertySet.class, xShape);
            xpa.setPropertyValue("FillStyle", FillStyle.BITMAP);
            xpa.setPropertyValue("FillBitmapURL", internalURL);

            return size.Height * 19;
        } catch (Exception exInternal) {
            log.error("Ошибка в методе insertImageToDoc() ", exInternal);
            System.err.println("Ошибка в методе insertImageToDoc(): "
                    + exInternal.getMessage());
            return 200;
        }
    }

    public int insertImageToDoc(XSpreadsheetDocument document,
                                XSpreadsheet sheet, XCell cell, String idImage, int align) {

        // *********************************************************************************
        // Блок добавления примечания к ячейке
        // **********************************************************************************
        try {

            String homePath = getHomePath();
            String sPath = homePath + "/catalog/" + idImage + ".jpg";
            String index;
            Size size;
            String noImageFile;

            String pref = "";
            if (!UtilFunctions.isWindows()) {
                pref = "/";
            }

            ImageService service = ModelImageService.getInstance();
            sPath = service.getDefaultImageFileByModelNumber(idImage, ModelImageSize.BIG);
            if (sPath == null) {
                sPath = "";
            }
            // ПРоверка на наличие файла перед открытием
            if ((new File(sPath)).exists()) {
                ImageTools imgTools = new ImageTools();
                //BufferedImage buffImage = imgTools.imageOpen(sPath);
                //BufferedImage imgWater = imgTools.placeTextOnImage(buffImage,"www.8marta.com") ;
                //imgTools.imageSaveAs(imgWater,System.getProperty("user.home"),"/temp.jpg");
                //imgTools.imageSaveAs(imgWater,System.getProperty("user.home"),"/temp.jpg");
               /* sPath = "file:" + pref + homePath + "/catalog/" + idImage
                        + ".jpg";
                        */
                index = idImage;
                size = getImageSize(sPath);
                size = scaleSize(size, 5);

                if (UtilFunctions.isWindows()) {
                    sPath = "file:" + sPath;
                } else {
                    sPath = "file:/" + sPath;
                }


            } else {
                //System.out.println("Фото " + sPath + " не найдено");
                sPath = "file:" + service.getNoImageFile();
                sPath = sPath.replace("\\", "/");
                index = "noimage";
                size = new Size(200, 250);
            }

            System.err.println("Фото " + sPath + " загружено");

            XModel xSpreadsheetModel = (XModel) UnoRuntime.queryInterface(
                    XModel.class, document);
            XMultiServiceFactory xmsf = (XMultiServiceFactory) UnoRuntime
                    .queryInterface(XMultiServiceFactory.class,
                            xSpreadsheetModel);

            XNameContainer xBitmapContainer = (XNameContainer) UnoRuntime
                    .queryInterface(XNameContainer.class, xmsf
                            .createInstance("com.sun.star.drawing.BitmapTable"));

            if (!xBitmapContainer.hasByName(index))
                xBitmapContainer.insertByName(index, sPath);
            String internalURL = AnyConverter.toString(xBitmapContainer
                    .getByName(index));

           /* Object imgTemp = xmsf
                    .createInstance("com.sun.star.drawing.GraphicObjectShape");
            XShape xImage = (XShape) UnoRuntime.queryInterface(XShape.class,
                    imgTemp);
            */

            Object oGraphic = xmsf.createInstance("com.sun.star.drawing.GraphicObjectShape");

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.awt.Point p;

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, cell);
            p = (com.sun.star.awt.Point) xPropSet.getPropertyValue("Position");

            XPropertySet xGraphicProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, oGraphic);
            xGraphicProps.setPropertyValue("GraphicURL", internalURL);

            XShape xGraphicShape = (XShape) UnoRuntime.queryInterface(XShape.class, oGraphic);

            XDrawPageSupplier xDrawPageSupplier = (XDrawPageSupplier) UnoRuntime.queryInterface(XDrawPageSupplier.class, sheet);
            XDrawPage xDrawPage = xDrawPageSupplier.getDrawPage();

            try {
                xDrawPage.add(xGraphicShape);
                //xGraphicShape.setSize(new Size(size.Width * 19, size.Height * 19));
                xGraphicShape.setSize(new Size(size.Width * 19, size.Height * 19));
                xGraphicShape.setPosition(p);
            } catch (Exception e) {
                System.out.println("Ошибка");
            }

            cell = sheet.getCellByPosition(0, align);
            //cell.setFormula(hm[4].toString());

            // Получаем ссылку на ячейку
            XCellAddressable xCellAddr = (XCellAddressable) UnoRuntime
                    .queryInterface(XCellAddressable.class, cell);
            CellAddress aAddress = xCellAddr.getCellAddress();

            // Добавляем в ячейку примечание
            XSheetAnnotationsSupplier xAnnotationsSupp = (XSheetAnnotationsSupplier) UnoRuntime
                    .queryInterface(XSheetAnnotationsSupplier.class, sheet);
            XSheetAnnotations xAnnotations = xAnnotationsSupp.getAnnotations();
            xAnnotations.insertNew(aAddress, "Модель : " + idImage);

            XSheetAnnotationAnchor xAnnotAnchor = (XSheetAnnotationAnchor) UnoRuntime
                    .queryInterface(XSheetAnnotationAnchor.class, cell);
            XSheetAnnotation xAnnot = xAnnotAnchor.getAnnotation();
            XSheetAnnotationShapeSupplier xSheetAnnotationShapeSupplier = (XSheetAnnotationShapeSupplier) UnoRuntime
                    .queryInterface(XSheetAnnotationShapeSupplier.class, xAnnot);
            XShape xShape = xSheetAnnotationShapeSupplier.getAnnotationShape();


            xShape.setSize(new Size(size.Width * 27, size.Height * 27));
            xSpreadsheetModel = (XModel) UnoRuntime.queryInterface(
                    XModel.class, document);
            xmsf = (XMultiServiceFactory) UnoRuntime
                    .queryInterface(XMultiServiceFactory.class,
                            xSpreadsheetModel);
            xBitmapContainer = (XNameContainer) UnoRuntime
                    .queryInterface(XNameContainer.class, xmsf
                            .createInstance("com.sun.star.drawing.BitmapTable"));

            if (!xBitmapContainer.hasByName(index))
                xBitmapContainer.insertByName(index, sPath);
            internalURL = AnyConverter.toString(xBitmapContainer
                    .getByName(index));
            Object imgTemp = xmsf
                    .createInstance("com.sun.star.drawing.GraphicObjectShape");
            XShape xImage = (XShape) UnoRuntime.queryInterface(XShape.class,
                    imgTemp);
            XPropertySet xpi = (XPropertySet) UnoRuntime.queryInterface(
                    XPropertySet.class, xImage);
            xpi.setPropertyValue("GraphicURL", internalURL);
            Size imgSize = size;
            xImage.setSize(imgSize);

            XPropertySet xpa = (XPropertySet) UnoRuntime.queryInterface(
                    XPropertySet.class, xShape);
            xpa.setPropertyValue("FillStyle", FillStyle.BITMAP);
            xpa.setPropertyValue("FillBitmapURL", internalURL);

            return size.Height * 19;
        } catch (Exception exInternal) {
            log.error("Ошибка в методе insertImageToDoc() ", exInternal);
            System.err.println("Ошибка в методе insertImageToDoc(): "
                    + exInternal.getMessage());
            exInternal.printStackTrace();
            return 200;
        }
    }

    private String getHomePath() {
        if (UtilFunctions.isWindows()) {
            return "//file-server/Programs/MyReports";
        } else {
            return "/nfs/Programs/MyReports";
        }
    }

    /**
     * Метод формирует приложение к электронной накладной по шаблону
     * [ПриложениеЭлНакл.ots] Такая реализация временная, и алгоритм будет
     * переписываться
     */
    private void createAppTN(XComponent currentDocument) {
        XCell xCell;
        float charSize = (float) 6.5;
        Object[] hm;

        try {
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, currentDocument);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();

            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);
            xNamed.setName("Приложение к электронной накладной №"
                    + dataArray.get(1)[1]);

            com.sun.star.beans.XPropertySet xPropSet;
            com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
            aLine.Color = 0x000000;
            aLine.Color = 0x000000;
            aLine.InnerLineWidth = 0;
            aLine.LineDistance = 0;
            aLine.OuterLineWidth = 10;
            com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
            aBorder.BottomLine = aLine;
            aBorder.IsBottomLineValid = true;
            // ----------------Заполнение шапки документа
            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setFormula(" №" + dataArray.get(1)[1]);
            // ----------------Заполнение тела документа
            int row = 6;
            int lineStart = row - 1;
            String sar = dataArray.get(0)[2].toString();

            for (int i = 0; i < dataArray.size(); i++) {
                hm = (Object[]) dataArray.get(i);

                if (!sar.equals(hm[2].toString())) {// Если новый артикул
                    hm = (Object[]) dataArray.get(i - 1);
                    // sar=hm[2].toString();
                    XCellRange xCellRange = xSpreadsheet
                            .getCellRangeByPosition(0, lineStart + 1, 0,
                                    row - 1);

                    com.sun.star.util.XMergeable xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                            .queryInterface(com.sun.star.util.XMergeable.class,
                                    xCellRange);
                    xMerge.merge(true);

                    xCellRange = xSpreadsheet
                            .getCellRangeByPosition(7, lineStart + 1, 7,
                                    row - 1);

                    xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                            .queryInterface(com.sun.star.util.XMergeable.class,
                                    xCellRange);
                    xMerge.merge(true);


                    xCell = xSpreadsheet.getCellByPosition(0, lineStart + 1);
                    xCell.setFormula(hm[4].toString());

                    /*
                    insertAnnotationToDoc(xSpreadsheetDocument, xSpreadsheet,
                            xCell, hm[2].toString());
                    */

                    xCell = xSpreadsheet.getCellByPosition(7, lineStart + 1);
                    int height = insertImageToDoc(xSpreadsheetDocument, xSpreadsheet,
                            xCell, hm[2].toString(), lineStart + 1);

                    setCustomHeight(xCellRange, height);

                    xCell = xSpreadsheet.getCellByPosition(1, lineStart);
                    xCell.setFormula(hm[3].toString() + " модель "
                            + hm[2].toString());

                    xCellRange = xSpreadsheet.getCellRangeByPosition(1,
                            lineStart, 6, lineStart);

                    xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                            .queryInterface(com.sun.star.util.XMergeable.class,
                                    xCellRange);
                    xMerge.merge(true);


                    xCell = xSpreadsheet.getCellByPosition(5, row);
                    xCell.setFormula("=SUM(F" + (lineStart + 2) + ":F" + (row)
                            + ")");
/*
                    for (int p = lineStart; p < row + 1; p++) {
                        for (int j = 0; j < 8; j++) {
                            xCell = xSpreadsheet.getCellByPosition(j, p);

                            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                                    .queryInterface(
                                            com.sun.star.beans.XPropertySet.class,
                                            xCell);
                            xPropSet.setPropertyValue("CharHeight", charSize);
                        }
                    }
                    */

                    rowFormat(xSpreadsheet, row - 1, 7);

                    row += 2;
                    // row++ ;
                    lineStart = row - 1;

                    hm = (Object[]) dataArray.get(i);
                    sar = hm[2].toString();
                }

                // Цвет
                xCell = xSpreadsheet.getCellByPosition(1, row);
                xCell.setFormula(hm[7].toString());
                // Сорт
                xCell = xSpreadsheet.getCellByPosition(2, row);
                xCell.setFormula(hm[6].toString());
                // Размер
                xCell = xSpreadsheet.getCellByPosition(3, row);
                xCell.setFormula(hm[5].toString());
                // Размер
                xCell = xSpreadsheet.getCellByPosition(4, row);
                xCell.setFormula("шт.");
                // КОличество
                xCell = xSpreadsheet.getCellByPosition(5, row);
                xCell.setFormula(hm[8].toString());
                // ТНВЭД
                xCell = xSpreadsheet.getCellByPosition(6, row);
                xCell.setFormula(hm[18].toString());

                rowFormat(xSpreadsheet, row - 1, 7);

                row++;

            }

            hm = (Object[]) dataArray.get(dataArray.size() - 1);
            XCellRange xCellRange = xSpreadsheet.getCellRangeByPosition(0,
                    lineStart + 1, 0, row - 1);

            com.sun.star.util.XMergeable xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);

            xCell = xSpreadsheet.getCellByPosition(0, lineStart + 1);
            xCell.setFormula(hm[4].toString());

 /*           insertAnnotationToDoc(xSpreadsheetDocument, xSpreadsheet, xCell,
                    hm[2].toString());
*/
            xCell = xSpreadsheet.getCellByPosition(7, lineStart + 1);
            int height = insertImageToDoc(xSpreadsheetDocument, xSpreadsheet,
                    xCell, hm[2].toString(), lineStart + 1);
            setCustomHeight(xCellRange, height);

            xCell = xSpreadsheet.getCellByPosition(1, lineStart);
            xCell.setFormula(hm[3].toString() + " модель " + hm[2].toString());


            xCellRange = xSpreadsheet.getCellRangeByPosition(1,
                    lineStart, 6, lineStart);

            xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            xCellRange);
            xMerge.merge(true);


            xCell = xSpreadsheet.getCellByPosition(5, row);
            xCell.setFormula("=SUM(F" + (lineStart + 2) + ":F" + (row)
                    + ")");
/*
            for (int p = lineStart; p < row + 1; p++) {
                for (int j = 0; j < 8; j++) {
                    xCell = xSpreadsheet.getCellByPosition(j, p);

                    xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                            .queryInterface(
                                    com.sun.star.beans.XPropertySet.class,
                                    xCell);
                    xPropSet.setPropertyValue("CharHeight", charSize);
                }
            }*/

            rowFormat(xSpreadsheet, row - 1, 7);

            saveAsDocument(currentDocument, savePath + "/"
                    + dataArray.get(1)[1] + ".ods", lParams);

        } catch (Exception e) {
            log.error(
                    "Ошибка в методе createAppTN(XComponent currentDocument) для накладной "
                            + dataArray.get(1)[1], e);
            System.err
                    .println("Ошибка в методе createAppTN(XComponent currentDocument) для накладной "
                            + dataArray.get(1)[1] + e.getMessage());
        }

    }

    protected void rowFormat(XSpreadsheet sheet, int row, int size) {
        float charSize = (float) 6.5;
        com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
        aLine.Color = 0x000000;
        aLine.InnerLineWidth = (short) 0;
        aLine.LineDistance = (short) 0;
        aLine.OuterLineWidth = (short) 5;

        com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
        aBorder.BottomLine = aLine;
        aBorder.LeftLine = aLine;
        aBorder.RightLine = aLine;
        aBorder.IsBottomLineValid = true;
        aBorder.IsLeftLineValid = true;
        aBorder.IsRightLineValid = true;
        try {
            for (int z = 0; z < size; z++) {
                XCell xCell = sheet.getCellByPosition(z, row);
                XPropertySet xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                // xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.CENTER);
                xPropSet.setPropertyValue("VertJustify", com.sun.star.table.CellVertJustify.TOP);
            }
        } catch (Exception e) {
            System.out.println("Ошибка форматирования строки");
        }
    }

}
