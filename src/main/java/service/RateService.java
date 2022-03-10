package service;

import dto.FromTo;
import dto.StopId;

import java.math.BigDecimal;
import java.util.Map;

public class RateService {
    // immutable transit rates map
    public static final Map<FromTo, BigDecimal> RATES = Map.of(
            new FromTo(StopId.STOP1, StopId.STOP2), new BigDecimal("3.25"),
            new FromTo(StopId.STOP2, StopId.STOP1), new BigDecimal("3.25"),
            new FromTo(StopId.STOP2, StopId.STOP3), new BigDecimal("5.50"),
            new FromTo(StopId.STOP3, StopId.STOP2), new BigDecimal("5.50"),
            new FromTo(StopId.STOP1, StopId.STOP3), new BigDecimal("7.30"),
            new FromTo(StopId.STOP3, StopId.STOP1), new BigDecimal("7.30"),
            new FromTo(StopId.STOP1, StopId.ANY), new BigDecimal("7.30"), // incomplete trip
            new FromTo(StopId.STOP2, StopId.ANY), new BigDecimal("5.50"),
            new FromTo(StopId.STOP3, StopId.ANY), new BigDecimal("7.30")
    );
}
