package onepunman.remembermethis;

import java.io.File;
import java.io.Serializable;

public class FileWrapper implements Serializable{
    private File _file;

    public FileWrapper(File file) {
        _file = file;
    }

    public File getFile() {
        return  _file;
    }
}
