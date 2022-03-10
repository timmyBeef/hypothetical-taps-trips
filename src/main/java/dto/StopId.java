package dto;

public enum StopId {
    STOP1("Stop1"),
    STOP2("Stop2"),
    STOP3("Stop3"),
    ANY("Any");

    public final String value;

    StopId(String value) {
        this.value = value;
    }

    public static StopId valueOfLabel(String label) {
        for (StopId e : values()) {
            if (e.value.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
