package service;

import csv.CsvUtils;
import dto.TapData;

import java.util.List;

public class TapService {
    private final CsvUtils csvUtils;

    public TapService() {
        this.csvUtils = new CsvUtils();
    }

    public List<TapData> readTaps(String tapsFileName) {
        // read taps data from taps csv
        return csvUtils.read(tapsFileName);
    }
}
