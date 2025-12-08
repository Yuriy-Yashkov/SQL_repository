package by.march8.ecs.application.modules.cut.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class OdtTemplateEngine {

    /**
     * Читает content.xml из ODT-шаблона и заменяет текстовые плейсхолдеры.
     * Совместимо с Java 1.8 (нет readAllBytes()).
     *
     * @param templateFile файл .odt шаблона (ZIP)
     * @param values       карта <KEY, value> без фигурных скобок
     * @return содержимое content.xml после замены
     * @throws IOException при ошибках чтения ZIP или отсутствии content.xml
     */
    public static String loadAndFillTemplate(File templateFile, Map<String, String> values)
            throws IOException {

        String contentXml = null;

        // 1) читаем content.xml из ZIP
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(templateFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if ("content.xml".equals(entry.getName())) {
                    // читаем поток в байты через ByteArrayOutputStream (Java 8)
                    byte[] bytes = toByteArray(zis);
                    contentXml = new String(bytes, StandardCharsets.UTF_8);
                    // не забываем закрыть текущую запись
                    zis.closeEntry();
                    break;
                }
                zis.closeEntry();
            }
        }

        if (contentXml == null) {
            throw new IOException("content.xml not found in ODT template");
        }

        // 2) заменяем {{KEY}} → значение
        for (Map.Entry<String, String> e : values.entrySet()) {
            // простой replace; см. предупреждение про XML-символы ниже
            contentXml = contentXml.replace("{{" + e.getKey() + "}}", e.getValue());
        }

        return contentXml;
    }

    // Вспомогательный метод: читает весь InputStream в byte[] (без readAllBytes)
    private static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int len;
        while ((len = in.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        return bos.toByteArray();
    }

    // OdtTemplateEngine.java
    public static String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

}
