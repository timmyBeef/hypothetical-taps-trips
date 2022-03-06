package mapper;

import dto.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

// in real project, a Mapper can be done by Mapstruct
public class TapDataMapper {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public TapData fromRawData(TapRawData rawData) {
        TapData data = new TapData();
        data.setId(Long.valueOf(rawData.getID()));

        data.setDateTimeUTC(LocalDateTime.parse(rawData.getDateTimeUTC(), formatter));

        TapType type = "ON".equals(rawData.getTapType()) ? TapType.ON : TapType.OFF;
        data.setTapType(type);

        data.setStopId(StopId.valueOfLabel(rawData.getStopId()));
        data.setCompanyId(rawData.getCompanyId());
        data.setBusID(rawData.getBusID());
        data.setPan(rawData.getPAN());
        return data;
    }

    public TripData toTripData(TapData current, TapData next, BigDecimal amount, String status) {
        TripData data = new TripData();
        DecimalFormat df = new DecimalFormat("$#,##0.00");

        LocalDateTime fromTime = current.getDateTimeUTC();
        data.setStarted(fromTime.format(formatter));
        data.setFromStopId(current.getStopId().value);

        if (Status.INCOMPLETE.value.equals(status)) {
            data.setFinished("N/A");
            data.setDurationSecs("N/A");
            data.setToStopId("N/A");
        } else {
            LocalDateTime toTime = next.getDateTimeUTC();
            data.setFinished(toTime.format(formatter));
            data.setDurationSecs(String.valueOf(LocalDateTime.from(fromTime).until(toTime, ChronoUnit.SECONDS)));
            data.setToStopId(next.getStopId().value);
        }

        data.setChargeAmount(df.format(amount));
        data.setCompanyId(current.getCompanyId());
        data.setBusID(current.getBusID());
        data.setPan(current.getPan());
        data.setStatus(status);
        return data;
    }

}
