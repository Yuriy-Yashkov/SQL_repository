package by.march8.ecs.application.modules.cut.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ReportGenerator {

    /**
     * Создаёт новый ODT-файл на основе шаблона, заменяя content.xml на contentXmlReplaced.
     * Совместимо с Java 1.8 (нет transferTo()).
     *
     * @param templateFile        исходный .odt шаблон
     * @param outputName          путь и имя выходного .odt
     * @param contentXmlReplaced  строка content.xml, уже с подставленными значениями
     * @return созданный файл
     * @throws IOException при ошибках IO
     */
    public static File generateReport(File templateFile, String outputName,
                                      String contentXmlReplaced) throws IOException {

        File outFile = new File(outputName);

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(templateFile));
             ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outFile))) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();

                if ("content.xml".equals(entryName)) {
                    // создаём новую запись content.xml и пишем туда заменённый текст
                    ZipEntry newEntry = new ZipEntry("content.xml");
                    copyZipEntryAttributes(entry, newEntry);
                    zos.putNextEntry(newEntry);

                    byte[] bytes = contentXmlReplaced.getBytes(StandardCharsets.UTF_8);
                    zos.write(bytes);
                    zos.closeEntry();
                } else {
                    // копируем все остальные записи как есть
                    ZipEntry newEntry = new ZipEntry(entryName);
                    copyZipEntryAttributes(entry, newEntry);
                    zos.putNextEntry(newEntry);
                    copyStream(zis, zos); // заменяет transferTo
                    zos.closeEntry();
                }

                zis.closeEntry();
            }
        }

        return outFile;
    }

    // Копируем некоторые атрибуты ZipEntry (опционально, но полезно)
    private static void copyZipEntryAttributes(ZipEntry src, ZipEntry dest) {
        dest.setTime(src.getTime());
        // можно добавить комментарии, атрибуты и т.д. при необходимости
        // dest.setComment(src.getComment());
        // dest.setMethod(src.getMethod());
    }

    // Копирование InputStream -> OutputStream для Java 8
    private static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
    }
}
