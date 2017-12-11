package common.commands.response;

import common.commands.handlers.server.ListCommand;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class ListResponse implements Serializable {
    private final List<ItemFile> listFiles;

    public ListResponse(@NotNull List<ItemFile> listFiles) {
        this.listFiles = listFiles;
    }

    public List<ItemFile> getListFiles() {
        return listFiles;
    }

    public static class ItemFile {
        private int id;
        private String name;
        private long size;

        public ItemFile(int id, String name, long size) {
            this.id = id;
            this.name = name;
            this.size = size;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public long getSize() {
            return size;
        }

        @Override
        public String toString() {
            return "{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", size=" + size +
                    "};";
        }
    }


    @Override
    public String toString() {
        return "ListResponse{" +
                "listFiles=" + listFiles +
                '}';
    }
}
