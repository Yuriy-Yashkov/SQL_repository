package by.gomel.freedev.ucframework.ucswing.uicontrols.filelist;

import java.io.File;

/**
 * @author Andy 10.01.2019 - 10:30.
 */
public interface UCFileListListener {
    void onClick(File file);

    void onDoubleClick(File file);
}
