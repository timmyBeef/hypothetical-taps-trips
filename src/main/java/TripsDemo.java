import dto.*;
import service.TapService;
import service.TripService;

import java.io.FileNotFoundException;
import java.util.*;

public class TripsDemo {

    private TapService tapService;
    private TripService tripService;

    TripsDemo() {
        this.tripService = new TripService();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new TripsDemo().readTabsThenGenerateTips("taps_csv/taps.csv", "trips_csv/trips.csv");
        new TripsDemo().readTabsThenGenerateTips("taps_csv/taps_2_unordered_time.csv", "trips_csv/trips_2.csv");
        new TripsDemo().readTabsThenGenerateTips("taps_csv/taps_3_all_incomplete.csv", "trips_csv/trips_3.csv");
        new TripsDemo().readTabsThenGenerateTips("taps_csv/taps_4_all_cases.csv", "trips_csv/trips_4.csv");
        new TripsDemo().readTabsThenGenerateTips("taps_csv/taps_5_next_day.csv", "trips_csv/trips_5.csv");
    }

    /*
        1, 22-01-2018 11:00:00, ON, Stop2, Company1, Bus37, 5500005555555559
        2, 22-01-2018 12:00:00, OFF, Stop3, Company1, Bus37, 5500005555555559
        3, 22-01-2018 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559
        4, 22-01-2018 13:03:00, OFF, Stop1, Company1, Bus37, 5500005555555559

        Map<22-01-2018_Company1_Bus37_5500005555555559, List<these 4 taps data>>
     */
    public void readTabsThenGenerateTips(String tapsFileName, String tripsFileName) throws FileNotFoundException {

        List<TapData> taps = tapService.readTaps(tapsFileName);

        // put taps data into map by key:
        // dateTimeUTC's date + "_" + companyId + "_" + busID + "_" + pan
        Map<String, List<TapData>> tapsKeyMap = new HashMap<>();
        taps.forEach(tap -> {
            String tapsKey = tap.getTapsKey();
            tapsKeyMap.putIfAbsent(tapsKey, new ArrayList<>());
            tapsKeyMap.get(tapsKey).add(tap);
        });

        // generate trips
        tripService.generateTrips(tapsKeyMap, tripsFileName);
    }




    // the total number of taps.csv record is N, m is number of map key, n is each map key's list size,
    // so m*n = N, and we do a sort in each list, so the time complexity is O(m*nlogn),
    // m depends on the number of different passengers within a day, n depends on one passenger tab how many times

    // sort the trip list: O( (n/2)log(n/2) ) ~ O(NlogN)
}
