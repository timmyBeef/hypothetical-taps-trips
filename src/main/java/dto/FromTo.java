package dto;

import java.util.Objects;

public class FromTo {
    StopId from;
    StopId to;

    public FromTo(StopId from, StopId to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FromTo fromTo = (FromTo) o;
        return from == fromTo.from && to == fromTo.to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
