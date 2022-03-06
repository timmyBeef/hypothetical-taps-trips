package dto;


public class TapRawData {
    private String ID;
    private String DateTimeUTC;
    private String TapType;
    private String StopId;
    private String CompanyId;
    private String BusID;
    private String PAN;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDateTimeUTC() {
        return DateTimeUTC;
    }

    public void setDateTimeUTC(String dateTimeUTC) {
        DateTimeUTC = dateTimeUTC;
    }

    public String getTapType() {
        return TapType;
    }

    public void setTapType(String tapType) {
        TapType = tapType;
    }

    public String getStopId() {
        return StopId;
    }

    public void setStopId(String stopId) {
        StopId = stopId;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    public String getBusID() {
        return BusID;
    }

    public void setBusID(String busID) {
        BusID = busID;
    }

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    @Override
    public String toString() {
        return "TapData{" +
                "ID=" + ID +
                ", DateTimeUTC=" + DateTimeUTC +
                ", TapType=" + TapType +
                ", StopId='" + StopId + '\'' +
                ", CompanyId='" + CompanyId + '\'' +
                ", BusID='" + BusID + '\'' +
                ", PAN='" + PAN + '\'' +
                '}';
    }
}
