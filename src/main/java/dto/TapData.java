package dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TapData {
    private Long id;
    private LocalDateTime dateTimeUTC;
    private Enum tapType;
    private StopId stopId;
    private String companyId;
    private String busID;
    private String pan;

    public String getTapsKey() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return this.dateTimeUTC.format(formatter) + "_" + this.companyId + "_" + this.busID + "_" + this.pan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTimeUTC() {
        return dateTimeUTC;
    }

    public void setDateTimeUTC(LocalDateTime dateTimeUTC) {
        this.dateTimeUTC = dateTimeUTC;
    }

    public Enum getTapType() {
        return tapType;
    }

    public void setTapType(Enum tapType) {
        this.tapType = tapType;
    }

    public StopId getStopId() {
        return stopId;
    }

    public void setStopId(StopId stopId) {
        this.stopId = stopId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getBusID() {
        return busID;
    }

    public void setBusID(String busID) {
        this.busID = busID;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    @Override
    public String toString() {
        return "TapData{" +
                "id=" + id +
                ", dateTimeUTC=" + dateTimeUTC +
                ", tapType=" + tapType +
                ", stopId='" + stopId + '\'' +
                ", companyId='" + companyId + '\'' +
                ", busID='" + busID + '\'' +
                ", pan='" + pan + '\'' +
                '}';
    }
}
