import exception.CsvReadException;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TripsDemoTest {
    private TripsDemo demo = new TripsDemo();

    @Test
    public void test_trips_csv_result_valid() throws FileNotFoundException {
        String sourceFileName = "taps_csv/taps.csv";
        String targetFileName = "trips_csv/trips.csv";
        demo.readTabsThenGenerateTips(sourceFileName, targetFileName);

        List<String> expected = Arrays.asList(
                "22-01-2018 13:00:00, 22-01-2018 13:05:00, 300, Stop1, Stop2, $3.25, Company1, Bus37, 5500005555555559, Completed"
        );
        assertEquals(readTripsCSV(targetFileName), expected);


        sourceFileName = "taps_csv/taps_2_unordered_time.csv";
        targetFileName = "trips_csv/trips_2.csv";
        demo.readTabsThenGenerateTips(sourceFileName, targetFileName);
        expected = Arrays.asList(
                "22-01-2018 11:00:00, 22-01-2018 12:00:00, 3600, Stop2, Stop3, $5.50, Company1, Bus37, 5500005555555559, Completed",
                "22-01-2018 13:00:00, 22-01-2018 13:03:00, 180, Stop1, Stop1, $0.00, Company1, Bus37, 5500005555555559, Cancelled",
                "22-01-2018 13:05:00, 22-01-2018 13:06:00, 60, Stop3, Stop1, $7.30, Company1, Bus39, 122000000000003, Completed"
        );
        assertEquals(readTripsCSV(targetFileName), expected);

        sourceFileName = "taps_csv/taps_3_all_incomplete.csv";
        targetFileName = "trips_csv/trips_3.csv";
        demo.readTabsThenGenerateTips(sourceFileName, targetFileName);
        expected = Arrays.asList(
                "22-01-2018 11:00:00, N/A, N/A, Stop2, N/A, $5.50, Company1, Bus37, 5500005555555559, Incomplete",
                "22-01-2018 13:00:00, N/A, N/A, Stop1, N/A, $7.30, Company1, Bus37, 5500005555555559, Incomplete",
                "22-01-2018 13:05:00, N/A, N/A, Stop3, N/A, $7.30, Company1, Bus39, 122000000000003, Incomplete"
        );
        assertEquals(readTripsCSV(targetFileName), expected);

        sourceFileName = "taps_csv/taps_4_all_cases.csv";
        targetFileName = "trips_csv/trips_4.csv";
        demo.readTabsThenGenerateTips(sourceFileName, targetFileName);
        expected = Arrays.asList(
                "22-01-2018 11:00:00, 22-01-2018 12:00:00, 3600, Stop2, Stop3, $5.50, Company1, Bus37, 5500005555555559, Completed",
                "22-01-2018 13:00:00, 22-01-2018 13:03:00, 180, Stop1, Stop1, $0.00, Company1, Bus37, 5500005555555559, Cancelled",
                "22-01-2018 13:05:00, 22-01-2018 13:06:00, 60, Stop3, Stop1, $7.30, Company1, Bus39, 122000000000003, Completed",
                "22-01-2018 18:00:00, N/A, N/A, Stop1, N/A, $7.30, Company1, Bus37, 5500005555555559, Incomplete",
                "22-01-2018 19:05:00, N/A, N/A, Stop3, N/A, $7.30, Company1, Bus39, 122000000000003, Incomplete",
                "22-01-2018 20:00:00, N/A, N/A, Stop2, N/A, $5.50, Company1, Bus37, 5500005555555559, Incomplete"
        );
        assertEquals(readTripsCSV(targetFileName), expected);

        sourceFileName = "taps_csv/taps_5_next_day.csv";
        targetFileName = "trips_csv/trips_5.csv";
        demo.readTabsThenGenerateTips(sourceFileName, targetFileName);
        expected = Arrays.asList(
                "22-01-2018 13:00:00, 22-01-2018 13:05:00, 300, Stop1, Stop2, $3.25, Company1, Bus37, 5500005555555559, Completed",
                "23-01-2018 13:00:00, 23-01-2018 13:05:00, 300, Stop1, Stop2, $3.25, Company1, Bus37, 5500005555555559, Completed"
        );
        assertEquals(readTripsCSV(targetFileName), expected);
    }


    public List<String> readTripsCSV(String fileName) {
        List<String> result = new ArrayList<>();
        String line;
        File file = new File(fileName);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CsvReadException(e.getMessage());
        }
        return result;
    }
}