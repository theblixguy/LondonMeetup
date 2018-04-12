package com.suyashsrijan.londonmeetup.API.tfl.enums

enum class Stoptypes {
    PUBLIC_BUS_COACH_TRAM {
        override fun toString(): String {
            return "NaptanPublicBusCoachTram"
        }
    },

    METRO_STATION {
        override fun toString(): String {
            return "NaptanMetroStation"
        }
    },

    BIKE_STOP {
        override fun toString(): String {
            return "BikeStop"
        }
    }
}
