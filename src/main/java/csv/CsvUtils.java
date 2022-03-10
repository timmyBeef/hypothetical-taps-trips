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
    private TapDataMapper mapper = new TapDataMapper();


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

    public void write(List<TripData> trips, String fileName) throws FileNotFoundException {
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
        StringBuilder sb = new StringBuilder();
        return sb.append(data.getStarted()).append(", ")
                .append(data.getFinished()).append(", ")
                .append(data.getDurationSecs()).append(", ")
                .append(data.getFromStopId()).append(", ")
                .append(data.getToStopId()).append(", ")
                .append(data.getChargeAmount()).append(", ")
                .append(data.getCompanyId()).append(", ")
                .append(data.getBusID()).append(", ")
                .append(data.getPan()).append(", ")
                .append(data.getStatus())
                .toString();
    }
}
