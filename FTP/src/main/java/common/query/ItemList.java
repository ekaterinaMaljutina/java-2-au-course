package common.query;

import java.io.Serializable;

public class ItemList implements Serializable {
    private String name;
    private boolean isDir;

    public ItemList(String name, boolean isDir) {
        this.name = name;
        this.isDir = isDir;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemList)) {
            return false;
        }
        ItemList itemList = (ItemList) o;
        return isDir == itemList.isDir && name.equals(itemList.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (isDir ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "(name: " + name + " is_dir: " + isDir + ")";
    }
}
