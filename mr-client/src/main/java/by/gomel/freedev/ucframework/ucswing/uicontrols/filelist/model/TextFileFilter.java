package by.gomel.freedev.ucframework.ucswing.uicontrols.filelist.model;


import java.io.File;
import java.io.FileFilter;

/**
 * @author Andy 18.12.2018 - 12:07.
 */
public class TextFileFilter implements FileFilter {

    @Override
    public boolean accept(File file) {
        String name = file.getName().toLowerCase();
        return name.length() < 20;
    }
}
