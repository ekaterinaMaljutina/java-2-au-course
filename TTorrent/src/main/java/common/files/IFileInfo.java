package common.files;

import java.io.Serializable;

public interface IFileInfo extends Serializable {

    String getName();

    long getSize();

}
