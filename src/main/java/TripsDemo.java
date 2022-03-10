import csv.CsvUtils;
import dto.*;
import mapper.TapDataMapper;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;

public class TripsDemo {

    // immutable transit rates map
    private final static Map<FromTo, BigDecimal> RATES = Map.of(
            new FromTo(StopId.STOP1, StopId.STOP2), new BigDecimal(3.25),
            new FromTo(StopId.STOP2, StopId.STOP1), new BigDecimal(3.25),
            new FromTo(StopId.STOP2, StopId.STOP3), new BigDecimal(5.50),
            new FromTo(StopId.STOP3, StopId.STOP2), new BigDecimal(5.50),
            new FromTo(StopId.STOP1, StopId.STOP3), new BigDecimal(7.30),
            new FromTo(StopId.STOP3, StopId.STOP1), new BigDecimal(7.30),
            new FromTo(StopId.STOP1, StopId.ANY), new BigDecimal(7.30), // incomplete trip
            new FromTo(StopId.STOP2, StopId.ANY), new BigDecimal(5.50),
            new FromTo(StopId.STOP3, StopId.ANY), new BigDecimal(7.30)
    );

    private TapDataMapper mapper;
    private CsvUtils csvUtils;

    TripsDemo() {
        Collections.unmodifiableMap(RATES);
        mapper = new TapDataMapper();
        csvUtils = new CsvUtils();
    }

    public static void main(String[] args) throws FileNotFoundException {
        //new TripsDemo().showTrips("taps_csv/taps.csv", "trips_csv/trips.csv");
        //new TripsDemo().showTrips("taps_csv/taps_2_unordered_time.csv", "trips_csv/trips_2.csv");
        new TripsDemo().showTrips("taps_csv/taps_3_all_incomplete.csv", "trips_csv/trips_3.csv");
        //new TripsDemo().showTrips("taps_csv/taps_4_all_cases.csv", "trips_csv/trips_4.csv");
        //new TripsDemo().showTrips("taps_csv/taps_5_next_day.csv", "trips_csv/trips_5.csv");

    }

    /*
        1, 22-01-2018 11:00:00, ON, Stop2, Company1, Bus37, 5500005555555559
        2, 22-01-2018 12:00:00, OFF, Stop3, Company1, Bus37, 5500005555555559
        3, 22-01-2018 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559
        4, 22-01-2018 13:03:00, OFF, Stop1, Company1, Bus37, 5500005555555559

        Map<22-01-2018_Company1_Bus37_5500005555555559, List<these 4 taps data>>
     */
    public void showTrips(String tapsFileName, String tripsFileName) throws FileNotFoundException {
        // read taps data from taps csv
        List<TapData> taps = csvUtils.read(tapsFileName);

        // put taps data into map by key:
        // dateTimeUTC's date + "_" + companyId + "_" + busID + "_" + pan
        Map<String, List<TapData>> tapsKeyMap = new HashMap<>();
        taps.forEach(tap -> {
            String tapsKey = tap.getTapsKey();
            tapsKeyMap.putIfAbsent(tapsKey, new ArrayList<>());
            tapsKeyMap.get(tapsKey).add(tap);
        });

        // find trips
        List<TripData> trips = findTrips(tapsKeyMap);

        csvUtils.write(trips, tripsFileName);
    }

    /*
        1, 22-01-2018 11:00:00, ON, Stop2, Company1, Bus37, 5500005555555559
        2, 22-01-2018 12:00:00, OFF, Stop3, Company1, Bus37, 5500005555555559
        3, 22-01-2018 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559
        4, 22-01-2018 13:03:00, OFF, Stop1, Company1, Bus37, 5500005555555559

        Map<22-01-2018_Company1_Bus37_5500005555555559, List<these 4 taps data>>
    */
    // get taps data list from map
    // then classify taps data to completed, incomplete, cancel trips
    private List<TripData> findTrips(Map<String, List<TapData>> map) {
        List<TripData> trips = new ArrayList<>();
        for (String tapsKey : map.keySet()) {
            List<TapData> taps = map.get(tapsKey);
            taps.sort(Comparator.comparing(TapData::getDateTimeUTC)); // sort asc by time

            int i = 0;
            while (i < taps.size()) {
                TapData current = taps.get(i);
                TapData next = i + 1 < taps.size() ? taps.get(i + 1) : null; // for incomplete trip, next will be null

                BigDecimal amount;
                // complete or cancel taps, will be pair data
                if (next != null && current.getTapType() == TapType.ON && next.getTapType() == TapType.OFF) {

                    // COMPLETED trip
                    if (!current.getStopId().equals(next.getStopId())) {
                        amount = RATES.get(new FromTo(current.getStopId(), next.getStopId()));
                        trips.add(mapper.toTripData(current, next, amount, Status.COMPLETED.value));
                    } else { // CANCELLED trip
                        trips.add(mapper.toTripData(current, next, BigDecimal.ZERO, Status.CANCELLED.value));
                    }
                    i++; // finished two taps, while loop index step one more
                } else {
                    // incomplete trip is single one tap
                    amount = RATES.get(new FromTo(current.getStopId(), StopId.ANY));
                    trips.add(mapper.toTripData(current, null, amount, Status.INCOMPLETE.value));
                }
                i++;
            }
        }

        // for final trips result, sort by start time
        trips.sort(Comparator.comparing(TripData::getStarted));
        return trips;
    }


    // the total number of taps.csv record is N, m is number of map key, n is each map key's list size,
    // so m*n = N, and we do a sort in each list, so the time complexity is O(m*nlogn),
    // m depends on the number of different passengers within a day, n depends on one passenger tab how many times

    // sort the trip list: O( (n/2)log(n/2) ) ~ O(NlogN)
}
