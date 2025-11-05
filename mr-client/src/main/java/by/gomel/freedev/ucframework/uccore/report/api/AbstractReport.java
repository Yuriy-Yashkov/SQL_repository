package by.gomel.freedev.ucframework.uccore.report.api;

import by.gomel.freedev.ucframework.uccore.report.enums.FontStyle;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ProgressBar;
import by.march8.ecs.application.modules.marketing.model.ModelImageSize;
import by.march8.ecs.services.images.ImageService;
import by.march8.ecs.services.images.ModelImageService;
import com.sun.star.awt.FontWeight;
import com.sun.star.awt.Size;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameContainer;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.drawing.XDrawPageSupplier;
import com.sun.star.drawing.XShape;
import com.sun.star.frame.XModel;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.sheet.XPrintAreas;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.BorderLine;
import com.sun.star.table.CellHoriJustify;
import com.sun.star.table.CellRangeAddress;
import com.sun.star.table.CellVertJustify;
import com.sun.star.table.TableBorder;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.table.XColumnRowRange;
import com.sun.star.table.XTableRows;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.UnoRuntime;
import common.UtilFunctions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * @author Andy 11.04.2018 - 9:14.
 */
@SuppressWarnings("all")
public abstract class AbstractReport {

    protected ImageService imageService = null;
    protected ProgressBar pb;

    protected void setCharHeightForCell(XCell cell, float charSize) {
        setCharHeightForCell(cell, charSize, FontStyle.Normal);
    }

    protected void setCharHeightForCell(XCell cell, float charSize, FontStyle fontStyle) {
        XPropertySet xPropSet = UnoRuntime
                .queryInterface(com.sun.star.beans.XPropertySet.class,
                        cell);
        float charWeight;
        try {
            switch (fontStyle) {
                case Bold:
                    charWeight = FontWeight.BOLD;
                    break;
                default:
                    charWeight = FontWeight.NORMAL;
                    break;
            }
            xPropSet.setPropertyValue("CharHeight", charSize);
            xPropSet.setPropertyValue("CharWeight", charWeight);
        } catch (Exception e) {
            System.err.println("Ошибка установки параметра [CharHeight] для ячейки: " + cell);
            e.printStackTrace();
        }
    }

    protected void enableImageService() {
        imageService = ModelImageService.getInstance();
    }

    protected void setCharHeightForRangeCell(XSpreadsheet xSpreadsheet, int column, int row, int size, float charSize) {
        for (int p = column; p < size; p++) {
            try {
                XCell xCell = xSpreadsheet.getCellByPosition(p, row);
                XPropertySet xPropSet = UnoRuntime
                        .queryInterface(
                                XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("CharHeight", charSize);
            } catch (Exception e) {
                System.err.println("Ошибка установки параметра [TableBorder] для диапазона");
                e.printStackTrace();
            }
        }
    }

    protected void setRowHeight(XCellRange range, int width) {
        XColumnRowRange xColRowRange = (XColumnRowRange)
                UnoRuntime.queryInterface(XColumnRowRange.class, range);
        XTableRows xRows = xColRowRange.getRows();
        try {
            Object aRowObj = xRows.getByIndex(0);
            XPropertySet xPropSet = (XPropertySet) UnoRuntime.queryInterface(
                    com.sun.star.beans.XPropertySet.class, aRowObj);
            xPropSet.setPropertyValue("Height", width);
        } catch (Exception e) {
            System.out.println("Ошибка");
        }
    }

    protected void setRowHeight(XSpreadsheet xSpreadsheet, int row, int width) {

        try {

            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(0, row, 99, row);

            XColumnRowRange xColRowRange = (XColumnRowRange)
                    UnoRuntime.queryInterface(XColumnRowRange.class, xCellRange);
            XTableRows xRows = xColRowRange.getRows();

            Object aRowObj = xRows.getByIndex(0);
            XPropertySet xPropSet = (XPropertySet) UnoRuntime.queryInterface(
                    com.sun.star.beans.XPropertySet.class, aRowObj);
            xPropSet.setPropertyValue("Height", width);
        } catch (Exception e) {
            System.out.println("Ошибка");
        }
    }

    protected void mergeCellRange(XSpreadsheet xSpreadsheet, int columnStart, int rowStart, int columnEnd, int rowEnd) {
        XCellRange xCellRange = null;
        try {
            xCellRange = xSpreadsheet
                    .getCellRangeByPosition(columnStart, rowStart, columnEnd, rowEnd);
            mergeCellRange(xCellRange);
        } catch (com.sun.star.lang.IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    protected void mergeCellRange(XCellRange cellRange) {
        try {
            com.sun.star.util.XMergeable xMerge = (com.sun.star.util.XMergeable) UnoRuntime
                    .queryInterface(com.sun.star.util.XMergeable.class,
                            cellRange);
            xMerge.merge(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setBorderForRangeCell(XSpreadsheet xSpreadsheet, int column, int row, int size) {
        TableBorder aBorder = new TableBorder();
        BorderLine aLine = new BorderLine();

        aLine.Color = 0x000000;
        aLine.InnerLineWidth = 0;
        aLine.LineDistance = 0;
        aLine.OuterLineWidth = 25;

        aBorder.BottomLine = aLine;
        aBorder.IsBottomLineValid = true;
        XPropertySet xPropSet;
        for (int p = column; p < size; p++) {
            try {
                XCell xCell = xSpreadsheet.getCellByPosition(p, row - 1);
                xPropSet = UnoRuntime
                        .queryInterface(
                                XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            } catch (Exception e) {
                System.err.println("Ошибка установки параметра [TableBorder] для диапазона");
                e.printStackTrace();
            }
        }
    }


    protected void rowFontSizeForRow(XSpreadsheet sheet, int startRow, int stopRow, int size, float charSize) {
        try {
            XCellRange xCellRange = sheet.getCellRangeByPosition(0, startRow, size,
                    stopRow);

            XPropertySet xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCellRange);
            xPropSet.setPropertyValue("CharHeight", charSize);
        } catch (Exception e) {
            System.out.println("Ошибка форматирования строки");
        }
    }


    protected void rowFormat(XSpreadsheet sheet, int row, int length, float charSize) {
        //float charSize = (float) 6.5;
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
            for (int z = 0; z < length; z++) {
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

    protected void rowFormatDouble(XSpreadsheet sheet, int row, int length, float charSize) {
        //float charSize = (float) 6.5;
        com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
        aLine.Color = 0x000000;
        aLine.InnerLineWidth = (short) 0;
        aLine.LineDistance = (short) 0;
        aLine.OuterLineWidth = (short) 50;

        com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
        aBorder.Distance = (short) 1;
        aBorder.BottomLine = aLine;
        aBorder.LeftLine = aLine;
        aBorder.RightLine = aLine;
        aBorder.IsBottomLineValid = true;
        aBorder.IsLeftLineValid = true;
        aBorder.IsRightLineValid = true;
        try {
            for (int z = 0; z < length; z++) {
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

    protected void rowFormatCenterLeft(XSpreadsheet sheet, int row, int length, float charSize) {
        //float charSize = (float) 6.5;
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
            for (int z = 0; z < length; z++) {
                XCell xCell = sheet.getCellByPosition(z, row);
                XPropertySet xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                        .queryInterface(com.sun.star.beans.XPropertySet.class,
                                xCell);
                //xPropSet.setPropertyValue("VertJustify", CellVertJustify.STANDARD);
                xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);
                xPropSet.setPropertyValue("CharHeight", charSize);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }
        } catch (Exception e) {
            System.out.println("Ошибка форматирования строки");
        }
    }

    private XPropertySet appendProperty(XPropertySet property, String key, Object value) {
        try {
            property.setPropertyValue(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return property;
    }

/*    protected void rowFormat(XSpreadsheet sheet, int row, int size, int fontSize) {
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
                xPropSet.setPropertyValue("CharHeight", fontSize);
                xPropSet.setPropertyValue("TableBorder", aBorder);
                // xPropSet.setPropertyValue("HoriJustify", com.sun.star.table.CellHoriJustify.CENTER);
                xPropSet.setPropertyValue("VertJustify", com.sun.star.table.CellVertJustify.TOP);
            }
        } catch (Exception e) {
            System.out.println("Ошибка форматирования строки");
        }
    }*/


    public int insertImageToDoc(XSpreadsheetDocument document,
                                XSpreadsheet sheet, XCell cell, String idImage, int align) {
        try {

            String sPath;
            String index;
            Size size;

            if (imageService == null) {
                imageService = ModelImageService.getInstance();
            }

            sPath = imageService.getDefaultImageFileByModelNumber(idImage, ModelImageSize.SMALL);
            if (sPath == null) {
                sPath = "";
            }
            // ПРоверка на наличие файла перед открытием
            if ((new File(sPath)).exists()) {
                index = idImage;

                size = getImageSize(sPath);
                //size = scaleSize(size, 5);

                if (UtilFunctions.isWindows()) {
                    sPath = "file:" + sPath;
                } else {
                    sPath = "file:/" + sPath;
                }

            } else {
                //System.out.println("Фото " + sPath + " не найдено");
                sPath = "file:" + imageService.getNoImageFile();
                sPath = sPath.replace("\\", "/");
                index = "noimage";
                size = new Size(200, 250);
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

            Object oGraphic = xmsf.createInstance("com.sun.star.drawing.GraphicObjectShape");

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.awt.Point p;

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, cell);
            p = (com.sun.star.awt.Point) xPropSet.getPropertyValue("Position");
            p.Y = p.Y + 300;
            p.X = p.X + 100;


            XShape xGraphicShape = (XShape) UnoRuntime.queryInterface(XShape.class, oGraphic);

            XDrawPageSupplier xDrawPageSupplier = (XDrawPageSupplier) UnoRuntime.queryInterface(XDrawPageSupplier.class, sheet);
            XDrawPage xDrawPage = xDrawPageSupplier.getDrawPage();

            try {
                xDrawPage.add(xGraphicShape);
                //xGraphicShape.setSize(new Size(size.Width * 19, size.Height * 19));
                xGraphicShape.setSize(new Size(size.Width * 19, size.Height * 19));
                xGraphicShape.setPosition(p);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return size.Height * 19 + 500;
        } catch (Exception exInternal) {
            System.err.println("Ошибка в методе insertImageToDoc(): "
                    + exInternal.getMessage());
            exInternal.printStackTrace();
            return 700;
        }
    }

    public void insertImageToDocAndResize(XSpreadsheetDocument document,
                                          XSpreadsheet sheet, XCell cell, String idImage, XCellRange xCellRange, int align) {
        try {
            String sPath;
            String index;
            Size size;

            if (imageService == null) {
                imageService = ModelImageService.getInstance();
            }

            sPath = imageService.getDefaultImageFileByModelNumber(idImage, ModelImageSize.SMALL);
            if (sPath == null) {
                sPath = "";
            }

            // ПРоверка на наличие файла перед открытием
            if ((new File(sPath)).exists()) {
                index = idImage;

                size = getImageSize(sPath);
                //size = scaleSize(size, 5);

                if (UtilFunctions.isWindows()) {
                    sPath = "file:" + sPath;
                } else {
                    sPath = "file:/" + sPath;
                }

            } else {
                //System.out.println("Фото " + sPath + " не найдено");
                sPath = "file:" + imageService.getNoImageFile();
                sPath = sPath.replace("\\", "/");
                index = "noimage";
                size = new Size(200, 250);
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

            Object oGraphic = xmsf.createInstance("com.sun.star.drawing.GraphicObjectShape");

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.awt.Point p;

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, cell);
            p = (com.sun.star.awt.Point) xPropSet.getPropertyValue("Position");
            p.Y = p.Y + 300;
            p.X = p.X + 100;


            XPropertySet xGraphicProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, oGraphic);
            XShape xGraphicShape = (XShape) UnoRuntime.queryInterface(XShape.class, oGraphic);
            XPropertySet xImgPropSet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xGraphicShape);


            //XPropertySetInfo xIPSInfo = xImgPropSet.getPropertySetInfo();
            //xGraphicProps.setPropertyValue("AnchorType",TextContentAnchorType.AS_CHARACTER);

            xGraphicProps.setPropertyValue("GraphicURL", internalURL);


            //xGraphicProps.setPropertyValue("HoriOrientPosition", p.X-300);
            //xGraphicProps.setPropertyValue("VertOrientPosition", p.Y-100);

            XDrawPageSupplier xDrawPageSupplier = (XDrawPageSupplier) UnoRuntime.queryInterface(XDrawPageSupplier.class, sheet);
            XDrawPage xDrawPage = xDrawPageSupplier.getDrawPage();


            try {

                xGraphicShape.setSize(new Size(size.Width * 19, size.Height * 19));
                xDrawPage.add(xGraphicShape);
                //xGraphicShape.setSize(new Size(size.Width * 19, size.Height * 19));

                //xGraphicShape.setPosition(p);

                int height = size.Height * 19 + 500;


                if (height < align) {
                    height = align;
                }

                setRowHeight(xCellRange, height);

                xImgPropSet.setPropertyValue("Anchor", cell);
                xImgPropSet.setPropertyValue("HoriOrientPosition", 25);
                xImgPropSet.setPropertyValue("VertOrientPosition", 25);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception exInternal) {
            System.err.println("Ошибка в методе insertImageToDoc(): "
                    + exInternal.getMessage());
            exInternal.printStackTrace();
        }
    }


    private Size getImageSize(String fName) {
        BufferedImage img = null;
        // Обработка исключений при открытии файла спрайта
        try {
            img = ImageIO.read(new File(fName));
        } catch (IOException e) {
            // Действия в случае возникшего исключения
            System.err.println("Ошибка метода getImageSize(String fName)" + e);
            img = null;
            return new Size(200, 250);
        }
        // Возвращаем результат
        return new Size(img.getWidth(), img.getHeight());
    }

    protected void setFontBoldForCellRange(XSpreadsheet xSpreadsheet, int fColumnIndex, int lColumnIndex, int rowIndex) {
        try {
            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(fColumnIndex, rowIndex, lColumnIndex,
                            rowIndex);

            XPropertySet xPropSet = UnoRuntime
                    .queryInterface(
                            XPropertySet.class,
                            xCellRange);
            // xPropSet.setPropertyValue("CharHeight", charSize);
            xPropSet.setPropertyValue("CharWeight", FontWeight.BOLD);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setBorderForCellRange(XSpreadsheet xSpreadsheet, int fColumnIndex, int lColumnIndex, int rowIndex) {
        TableBorder aBorder = new TableBorder();
        BorderLine aLine = new BorderLine();
        aLine.Color = 0x000000;
        aLine.InnerLineWidth = 0;
        aLine.LineDistance = 0;
        aLine.OuterLineWidth = 25;

        aBorder.BottomLine = aLine;
        aBorder.IsBottomLineValid = true;
        try {
            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(fColumnIndex, rowIndex, lColumnIndex,
                            rowIndex);

            XPropertySet xPropSet = UnoRuntime
                    .queryInterface(
                            XPropertySet.class,
                            xCellRange);
            xPropSet.setPropertyValue("TableBorder", aBorder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setFullBorderForCellRange(XSpreadsheet xSpreadsheet, int fColumnIndex, int lColumnIndex, int fRowIndex, int lRowIndex) {
        TableBorder aBorder = new TableBorder();
        BorderLine aLine = new BorderLine();
        aLine.Color = 0x000000;
        aLine.InnerLineWidth = 10;
        aLine.LineDistance = 0;
        aLine.OuterLineWidth = 0;

        aBorder.BottomLine = aLine;
        aBorder.LeftLine = aLine;
        aBorder.RightLine = aLine;
        aBorder.TopLine = aLine;
        aBorder.HorizontalLine = aLine;
        aBorder.VerticalLine = aLine;

        aBorder.IsTopLineValid = true;
        aBorder.IsBottomLineValid = true;
        aBorder.IsLeftLineValid = true;
        aBorder.IsRightLineValid = true;
        aBorder.IsDistanceValid = true;
        aBorder.IsHorizontalLineValid = true;
        aBorder.IsVerticalLineValid = true;


        try {
            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(fColumnIndex, fRowIndex, lColumnIndex,
                            lRowIndex);

            XPropertySet xPropSet = UnoRuntime
                    .queryInterface(
                            XPropertySet.class,
                            xCellRange);
            xPropSet.setPropertyValue("TableBorder", aBorder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setFullBorderForCell(XSpreadsheet xSpreadsheet, int fColumnIndex, int lColumnIndex, int rowIndex) {
        TableBorder aBorder = new TableBorder();
        BorderLine aLine = new BorderLine();
        aLine.Color = 0x000000;
        aLine.InnerLineWidth = 0;
        aLine.LineDistance = 0;
        aLine.OuterLineWidth = 25;

        aBorder.BottomLine = aLine;
        aBorder.LeftLine = aLine;
        aBorder.RightLine = aLine;
        aBorder.TopLine = aLine;

        aBorder.IsTopLineValid = true;
        aBorder.IsBottomLineValid = true;
        aBorder.IsLeftLineValid = true;
        aBorder.IsRightLineValid = true;

        try {


            for (int z = fColumnIndex; z <= lColumnIndex; z++) {
                XCell xCell = xSpreadsheet.getCellByPosition(z, rowIndex);
                XPropertySet xPropSet = UnoRuntime
                        .queryInterface(
                                XPropertySet.class,
                                xCell);
                xPropSet.setPropertyValue("TableBorder", aBorder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

/*        TableBorder aBorder = new TableBorder();
        BorderLine aLine = new BorderLine();
        aLine.Color = 0x000000;
        aLine.InnerLineWidth = 25;
        aLine.LineDistance = 0;
        aLine.OuterLineWidth = 25;

        aBorder.BottomLine = aLine;
        aBorder.LeftLine = aLine;
        aBorder.RightLine = aLine ;
        aBorder.TopLine = aLine ;

        aBorder.IsTopLineValid = true;
        aBorder.IsBottomLineValid = true;
        aBorder.IsLeftLineValid = true;
        aBorder.IsRightLineValid = true;

        try {
            XCellRange xCellRange = xSpreadsheet
                    .getCellRangeByPosition(fColumnIndex, rowIndex, lColumnIndex,
                            rowIndex);

            XPropertySet xPropSet = UnoRuntime
                    .queryInterface(
                            XPropertySet.class,
                            xCellRange);
            xPropSet.setPropertyValue("TableBorder", aBorder);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    protected void rowFormatCenterCenter(XCell xCell) {
        try {
            XPropertySet xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime
                    .queryInterface(com.sun.star.beans.XPropertySet.class,
                            xCell);
            xPropSet.setPropertyValue("VertJustify", CellVertJustify.CENTER);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.CENTER);
        } catch (Exception e) {
            System.out.println("Ошибка форматирования строки");
        }
    }

    public void insertImageToDocAndResize(XSpreadsheetDocument document,
                                          XSpreadsheet sheet, XCell cell, String model, String idImage, XCellRange xCellRange, int height_) {
        try {
            String sPath;
            String index;
            Size size;

            if (imageService == null) {
                imageService = ModelImageService.getInstance();
            }

            sPath = idImage.replace("//", "/");
            if (sPath == null) {
                sPath = "";
            }

            if ((new File(sPath)).exists()) {
                index = model;

                size = getImageSize(sPath);
                //size = scaleSize(size, 5);

                if (UtilFunctions.isWindows()) {
                    sPath = "file:" + sPath;
                } else {
                    sPath = "file:/" + sPath;
                }

            } else {
                //System.out.println("Фото " + sPath + " не найдено");
                sPath = "file:" + imageService.getNoImageFile();
                sPath = sPath.replace("\\", "/");
                index = "noimage";
                size = new Size(200, 250);
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

            Object oGraphic = xmsf.createInstance("com.sun.star.drawing.GraphicObjectShape");

            com.sun.star.beans.XPropertySet xPropSet = null;
            com.sun.star.awt.Point p;

            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, cell);
            p = (com.sun.star.awt.Point) xPropSet.getPropertyValue("Position");
            p.Y = p.Y + 300;
            p.X = p.X + 100;
            xPropSet.setPropertyValue("Position", p);

            XPropertySet xGraphicProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, oGraphic);
            XShape xGraphicShape = (XShape) UnoRuntime.queryInterface(XShape.class, oGraphic);
            XPropertySet xImgPropSet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xGraphicShape);
            xGraphicProps.setPropertyValue("GraphicURL", internalURL);
            XDrawPageSupplier xDrawPageSupplier = (XDrawPageSupplier) UnoRuntime.queryInterface(XDrawPageSupplier.class, sheet);
            XDrawPage xDrawPage = xDrawPageSupplier.getDrawPage();

            try {

                xGraphicShape.setSize(new Size(size.Width * 19, size.Height * 19));
                xGraphicShape.setPosition(p);
                xDrawPage.add(xGraphicShape);
                int height = size.Height * 19 + 500;


                if (height < height_) {
                    height = height_;
                }

                setRowHeight(xCellRange, height);

                xImgPropSet.setPropertyValue("Anchor", cell);
                xImgPropSet.setPropertyValue("HoriOrientPosition", 100);
                xImgPropSet.setPropertyValue("VertOrientPosition", 100);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception exInternal) {
            System.err.println("Ошибка в методе insertImageToDoc(): "
                    + exInternal.getMessage());
            exInternal.printStackTrace();
        }
    }

    public String getXCellName(XCell xCell) {
        com.sun.star.sheet.XCellAddressable xCellAddr = UnoRuntime.queryInterface(com.sun.star.sheet.XCellAddressable.class, xCell);
        com.sun.star.table.CellAddress aAddress = xCellAddr.getCellAddress();
        XPropertySet xPropSet = UnoRuntime
                .queryInterface(
                        XPropertySet.class,
                        xCell);
        try {
            return (String) xPropSet.getPropertyValue("AbsoluteName");
            //System.out.println(o);
        } catch (UnknownPropertyException e) {
            e.printStackTrace();
        } catch (WrappedTargetException e) {
            e.printStackTrace();
        }
/*
        String aText = "Address of this cell:  Column=" + xCellAddr.toString();
        aText += ";  Row=" + aAddress.Row;
        aText += ";  Sheet=" + aAddress.Sheet;
        System.out.println( aText );*/
        return null;
    }

    public void insertBreakPage(XSpreadsheet sheet, CellRangeAddress[] ranges) {
        XPrintAreas printAreas = (XPrintAreas) UnoRuntime.queryInterface(XPrintAreas.class, sheet);
        printAreas.setPrintAreas(ranges);
    }

    public CellRangeAddress getCellRanges(int rowStart, int rowStop, int colStart, int colStop) {
        com.sun.star.table.CellRangeAddress aAddress = new com.sun.star.table.CellRangeAddress();
        aAddress.Sheet = (short) 0;
        aAddress.StartColumn = colStart;
        aAddress.StartRow = rowStart;
        aAddress.EndColumn = colStop;
        aAddress.EndRow = rowStop;

        return aAddress;
    }
}
