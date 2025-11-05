package by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Andy 19.12.2018 - 7:24.
 */
public class ImageFileFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        String name = file.getName().toLowerCase();
        return name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png");
    }
}
