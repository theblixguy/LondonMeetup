package com.suyashsrijan.londonmeetup.API.tfl.enums;

public enum Stoptypes {
    PUBLIC_BUS_COACH_TRAM {
        public String toString() {
            return "NaptanPublicBusCoachTram";
        }
    },

    METRO_STATION {
        public String toString() {
            return "NaptanMetroStation";
        }
    },

    BIKE_STOP {
        public String toString() {
            return "BikeStop";
        }
    }
}
