package xsd.image;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * @author Andy 20.12.2018 - 10:15.
 */
public class ImageInformation {

    @Setter
    @Getter
    private File fileName;

    @Setter
    @Getter
    private int width;

    @Setter
    @Getter
    private int height;

    @Setter
    @Getter
    private String source;
    private String file;

    public File getFile() {
        return fileName;
    }

    public void setFile(String file) {
        fileName = new File(file);
        this.file = file;
    }
}

