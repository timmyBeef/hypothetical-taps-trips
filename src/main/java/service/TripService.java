package service;

import csv.CsvUtils;
import dto.*;
import mapper.TapDataMapper;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TripService {
    private TapDataMapper mapper;
    private Map<FromTo, BigDecimal> rates;
    private CsvUtils csvUtils;

    public TripService() {
        this.mapper = new TapDataMapper();
        this.rates = RateService.RATES;
        this.csvUtils = new CsvUtils();
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
    public List<TripData> generateTrips(Map<String, List<TapData>> map, String fileName) throws FileNotFoundException {
        List<TripData> trips = new ArrayList<>();
        for (String tapsKey : map.keySet()) {
            setOneCustomerTrips(map, trips, tapsKey);
        }
        this.csvUtils.write(trips, fileName);

        // for final trips result, sort by start time
        trips.sort(Comparator.comparing(TripData::getStarted));

        return trips;
    }

    private void setOneCustomerTrips(Map<String, List<TapData>> map, List<TripData> trips, String tapsKey) {
        List<TapData> taps = map.get(tapsKey);
        taps.sort(Comparator.comparing(TapData::getDateTimeUTC)); // sort asc by time

        int i = 0;
        while (i < taps.size()) {
            TapData current = taps.get(i);
            TapData next = i + 1 < taps.size() ? taps.get(i + 1) : null; // for incomplete trip, next will be null
            BigDecimal amount;

            // complete or cancel taps, will be pair data
            boolean isPairedTaps = next != null && current.getTapType() == TapType.ON && next.getTapType() == TapType.OFF;

            if (isPairedTaps) {
                addTripsWithCompletedOrCancelTrip(trips, current, next);
                i++; // finished two taps, while loop index step one more
            } else {
                addTripsWithIncompleteTrip(trips, current);
            }
            i++;
        }
    }

    private void addTripsWithIncompleteTrip(List<TripData> trips, TapData current) {
        BigDecimal amount;
        // incomplete trip is single one tap
        amount = rates.get(new FromTo(current.getStopId(), StopId.ANY));
        trips.add(mapper.toTripData(current, null, amount, Status.INCOMPLETE.value));
    }

    private void addTripsWithCompletedOrCancelTrip(List<TripData> trips, TapData current, TapData next) {
        BigDecimal amount;
        // COMPLETED trip
        if (!current.getStopId().equals(next.getStopId())) {
            amount = rates.get(new FromTo(current.getStopId(), next.getStopId()));
            trips.add(mapper.toTripData(current, next, amount, Status.COMPLETED.value));
        } else { // CANCELLED trip
            trips.add(mapper.toTripData(current, next, BigDecimal.ZERO, Status.CANCELLED.value));
        }
    }


}
