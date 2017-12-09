package common.files;

import java.util.Objects;

public class FileInfoImpl implements IFileInfo {

    private String nameFile;
    private long sizeFile;

    public FileInfoImpl(String nameFile, long sizeFile) {
        this.nameFile = nameFile;
        this.sizeFile = sizeFile;
    }

    @Override
    public String getName() {
        return nameFile;
    }

    @Override
    public long getSize() {
        return sizeFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileInfoImpl)) return false;

        FileInfoImpl fileInfo = (FileInfoImpl) o;

        return sizeFile == fileInfo.sizeFile && nameFile.equals(fileInfo.nameFile);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sizeFile);
    }
}
