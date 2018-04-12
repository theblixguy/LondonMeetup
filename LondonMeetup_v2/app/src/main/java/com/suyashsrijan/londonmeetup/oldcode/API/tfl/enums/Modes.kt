package com.suyashsrijan.londonmeetup.API.tfl.enums

enum class Modes {
    BUS {
        override fun toString(): String {
            return "bus"
        }
    },

    TUBE {
        override fun toString(): String {
            return "tube"
        }
    },

    CYCLE {
        override fun toString(): String {
            return "cycle"
        }
    }
}
