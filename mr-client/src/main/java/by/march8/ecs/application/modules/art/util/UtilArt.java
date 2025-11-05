package by.march8.ecs.application.modules.art.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lidashka.
 */
public class UtilArt {
    public static final Date TODAY = new Date(System.currentTimeMillis());
    protected final SimpleDateFormat parseFormatter = new SimpleDateFormat("dd.MM.yyyy");
}
