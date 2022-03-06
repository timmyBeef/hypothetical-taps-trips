# Hypothetical System - Taps & Trips Coding Challenge
## Tech Stack:
* Java 11
* Gradle
* JUnit

# How to run it
This is a gradle project, so simply import this project into IntelliJ IDEA.

I write a JUnit TripsDemoTest.java, so you can test here, or you can run TripsDemo.java main() method. Thanks.
![](https://i.imgur.com/BeVzEMh.png)


# Thought process & Test Cases
Based on requirement, taps.csv [input file], You are welcome to assume that the input file is well formed and is not missing data

There are some more assumptons.

## Assumption - the input data should be meaningful, tap on time should be before tap off time

ex: in same stop(cancel case), OFF is before ON ⇒ not make sense
```
1, 22-01-2018 13:00:00, OFF, Stop1, Company1, Bus37, 5500005555555559

2, 22-01-2018 13:03:00, ON, Stop1, Company1, Bus37, 5500005555555559
```
ex: in different stop(completed case), OFF is before ON ⇒ not make sense
```
1, 22-01-2018 13:00:00, OFF, Stop2, Company1, Bus37, 5500005555555559

2, 22-01-2018 13:03:00, ON, Stop1, Company1, Bus37, 5500005555555559
```

## How to group taps?

based on reqirement, we can group taps data by

```
dateTimeUTC + "_" + companyId + "_" + busID + "_" + pan
```
By grouping this key, we can get a passenger's taps within one day.

# Test Cases Explaination
you can find input and ouptput csv folders here (taps_csv, trips_csv folders):
![](https://i.imgur.com/fOOo3BT.png)


## test case: different days
If taps.csv has many days' data, here we process taps within one day, and generate trips for this day, others will be other trips, and that's why I group by:
```
dateTimeUTC + "_" + companyId + "_" + busID + "_" + pan
```
#### ex: taps_5_next_day.csv has two days' data
```
ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN
1, 22-01-2018 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559
2, 22-01-2018 13:05:00, OFF, Stop2, Company1, Bus37, 5500005555555559
3, 23-01-2018 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559
4, 23-01-2018 13:05:00, OFF, Stop2, Company1, Bus37, 5500005555555559
```
#### trips.csv will be ouput like this:
```
Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status
22-01-2018 13:00:00, 22-01-2018 13:05:00, 300, Stop1, Stop2, $3.25, Company1, Bus37, 5500005555555559, Completed
23-01-2018 13:00:00, 23-01-2018 13:05:00, 300, Stop1, Stop2, $3.25, Company1, Bus37, 5500005555555559, Completed
```

## test case: unordered data

#### taps_2_unordered_time.csv
Taps data could be unordered, so in the program, I'll sort them by DateTimeUTC.

```
ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN
1, 22-01-2018 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559
2, 22-01-2018 13:05:00, ON, Stop3, Company1, Bus39, 122000000000003
3, 22-01-2018 13:03:00, OFF, Stop1, Company1, Bus37, 5500005555555559
4, 22-01-2018 13:06:00, OFF, Stop1, Company1, Bus39, 122000000000003
5, 22-01-2018 11:00:00, ON, Stop2, Company1, Bus37, 5500005555555559
6, 22-01-2018 12:00:00, OFF, Stop3, Company1, Bus37, 5500005555555559
```
#### trips.csv will be ouput like this:
```
Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status
22-01-2018 11:00:00, 22-01-2018 12:00:00, 3600, Stop2, Stop3, $5.50, Company1, Bus37, 5500005555555559, Completed
22-01-2018 13:00:00, 22-01-2018 13:03:00, 180, Stop1, Stop1, $0.00, Company1, Bus37, 5500005555555559, Cancelled
22-01-2018 13:05:00, 22-01-2018 13:06:00, 60, Stop3, Stop1, $7.30, Company1, Bus39, 122000000000003, Completed
```

## test case: all cases - completed, incomplete, canceled

#### taps_4_all_cases.csv
```
ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN
1, 22-01-2018 11:00:00, ON, Stop2, Company1, Bus37, 5500005555555559
2, 22-01-2018 12:00:00, OFF, Stop3, Company1, Bus37, 5500005555555559
3, 22-01-2018 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559
4, 22-01-2018 13:03:00, OFF, Stop1, Company1, Bus37, 5500005555555559
5, 22-01-2018 13:05:00, ON, Stop3, Company1, Bus39, 122000000000003
6, 22-01-2018 13:06:00, OFF, Stop1, Company1, Bus39, 122000000000003
7, 22-01-2018 18:00:00, ON, Stop1, Company1, Bus37, 5500005555555559
8, 22-01-2018 19:05:00, ON, Stop3, Company1, Bus39, 122000000000003
9, 22-01-2018 20:00:00, ON, Stop2, Company1, Bus37, 5500005555555559
```

#### output
If trip is a incomplete trips: the column of Finished, DurationSecs, ToStopId is "N/A"
```
Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status
22-01-2018 11:00:00, 22-01-2018 12:00:00, 3600, Stop2, Stop3, $5.50, Company1, Bus37, 5500005555555559, Completed
22-01-2018 13:00:00, 22-01-2018 13:03:00, 180, Stop1, Stop1, $0.00, Company1, Bus37, 5500005555555559, Cancelled
22-01-2018 13:05:00, 22-01-2018 13:06:00, 60, Stop3, Stop1, $7.30, Company1, Bus39, 122000000000003, Completed
22-01-2018 18:00:00, N/A, N/A, Stop1, N/A, $7.30, Company1, Bus37, 5500005555555559, Incomplete
22-01-2018 19:05:00, N/A, N/A, Stop3, N/A, $7.30, Company1, Bus39, 122000000000003, Incomplete
22-01-2018 20:00:00, N/A, N/A, Stop2, N/A, $5.50, Company1, Bus37, 5500005555555559, Incomplete

```

# Main Logic Explanation

TripsDemo.java is the main program

* In the begining, I made the RATES map
```java=
    // immutable rates map
    private final static Map<FromTo, BigDecimal> RATES = Map.of(
            new FromTo(StopId.STOP1, StopId.STOP2), new BigDecimal(3.25),
            new FromTo(StopId.STOP2, StopId.STOP1), new BigDecimal(3.25),
            new FromTo(StopId.STOP2, StopId.STOP3), new BigDecimal(5.50),
            new FromTo(StopId.STOP3, StopId.STOP2), new BigDecimal(5.50),
            new FromTo(StopId.STOP1, StopId.STOP3), new BigDecimal(7.30),
            new FromTo(StopId.STOP3, StopId.STOP1), new BigDecimal(7.30),
            new FromTo(StopId.STOP1, StopId.ANY), new BigDecimal(7.30),
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
```

* these 3 are incompled cases' rates
```java=
new FromTo(StopId.STOP1, StopId.ANY), new BigDecimal(7.30),
new FromTo(StopId.STOP2, StopId.ANY), new BigDecimal(5.50),
new FromTo(StopId.STOP3, StopId.ANY), new BigDecimal(7.30)
```

* showTrips()
1. Read taps data from taps CSV file
2. Group taps data into HashMap<String key, List of taps>, string key is
```
dateTimeUTC + "_" + companyId + "_" + busID + "_" + pan
```
3. findTrips()
   Iterate HashMap, then get each string key, get the taps list, classify taps data to completed, incomplete, cancel trips, add to results
4. at last,  write trips to CSV file

```java=
    public void showTrips(String tapsFileName, String tripsFileName) throws FileNotFoundException {
        // read taps data from taps csv
        List<TapData> taps = csvUtils.read(tapsFileName);

        // put taps data into map by key:
        // dateTimeUTC.format(formatter) + "_" + companyId + "_" + busID + "_" + pan
        Map<String, List<TapData>> tapsKeyMap = new HashMap<>();
        taps.forEach(tap -> {
            String tapsKey = tap.getTapsKey();
            tapsKeyMap.putIfAbsent(tapsKey, new ArrayList<>());
            tapsKeyMap.get(tapsKey).add(tap);
        });

        // find trips
        List<TripData> trips = findTrips(tapsKeyMap);

        // at last,  write trips to csv file
        csvUtils.write(trips, tripsFileName);
    }
```



```java=
    // get taps data list from map
    // then classify taps data to completed, incomplete, cancel trips
    private List<TripData> findTrips(Map<String, List<TapData>> map) {
        List<TripData> trips = new ArrayList<>();
        for (String tapsKey : map.keySet()) {
            List<TapData> taps = map.get(tapsKey);
            taps.sort(Comparator.comparing(TapData::getDateTimeUTC));

            int i = 0;
            while (i < taps.size()) {
                TapData current = taps.get(i);
                TapData next = i + 1 < taps.size() ? taps.get(i + 1) : null; // for incomplete trip, next will be null

                BigDecimal amount;
                // complete or cancel taps, will be pair data
                if (next != null && current.getTapType() == TapType.ON && next.getTapType() == TapType.OFF) {

                    if (!current.getStopId().equals(next.getStopId())) {
                        amount = RATES.get(new FromTo(current.getStopId(), next.getStopId()));
                        trips.add(mapper.toTripData(current, next, amount, Status.COMPLETED.value));
                    } else {
                        trips.add(mapper.toTripData(current, next, BigDecimal.ZERO, Status.CANCELLED.value));
                    }
                    i++; // finished two taps, while loop index step one more
                } else {
                    // imcomplete trip is single one tap
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
```

# That's all! hope you'll like it!
