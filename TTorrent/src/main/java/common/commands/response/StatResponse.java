package common.commands.response;

import java.util.Set;

public class StatResponse {
    private final Set<Integer> idFiles;

    public StatResponse(Set<Integer> idFiles) {
        this.idFiles = idFiles;
    }

    public Set<Integer> getIdFiles() {
        return idFiles;
    }
}
