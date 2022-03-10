package dto;

public enum Status {
    COMPLETED("Completed"),
    INCOMPLETE("Incomplete"),
    CANCELLED("Cancelled");

    public final String value;

    Status(String value) {
        this.value = value;
    }
}
