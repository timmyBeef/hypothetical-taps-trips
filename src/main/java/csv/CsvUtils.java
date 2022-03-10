package csv;

import dto.TapData;
import dto.TapRawData;
import dto.TripData;
import exception.CsvReadException;
import exception.CsvWriteException;
import mapper.TapDataMapper;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {
    private static final String DELIMITER = ", ";
    private static final String TRIP_HEADER =
            "Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status";
    private String[] headers;
    private final TapDataMapper mapper = new TapDataMapper();


    public List<TapData> read(String fileName) {
        List<TapData> result = new ArrayList<>();
        String line;
        File file = new File(fileName);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            headers = br.readLine().split(DELIMITER);

            while ((line = br.readLine()) != null) {
                TapRawData rawData = new TapRawData();
                String[] rowData = line.split(DELIMITER);
                for (int i = 0; i < rowData.length; i++) {
                    setValueToTapData(rawData, i, rowData[i]);
                }
                result.add(mapper.fromRawData(rawData));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CsvReadException(e.getMessage());
        }

        // result.forEach(v -> System.out.println(v.toString()));
        return result;
    }

    public void write(List<TripData> trips, String fileName) {
        File csvOutputFile = new File(fileName);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println(TRIP_HEADER);
            trips.stream()
                    .map(this::getTripString)
                    .forEach(pw::println);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CsvWriteException(e.getMessage());
        }
    }

    // use reflection set value to dto
    private void setValueToTapData(TapRawData tap, int i, String data) throws NoSuchFieldException, IllegalAccessException {
        String colName = headers[i];
        Field nameField = tap.getClass().getDeclaredField(colName);
        nameField.setAccessible(true);
        nameField.set(tap, data);
    }

    private String getTripString(TripData data) {
        return data.getStarted() + ", " +
                data.getFinished() + ", " +
                data.getDurationSecs() + ", " +
                data.getFromStopId() + ", " +
                data.getToStopId() + ", " +
                data.getChargeAmount() + ", " +
                data.getCompanyId() + ", " +
                data.getBusID() + ", " +
                data.getPan() + ", " +
                data.getStatus();
    }
}
