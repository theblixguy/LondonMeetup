package com.suyashsrijan.londonmeetup.API.tfl.enums;

public enum Modes {
    BUS {
        public String toString() {
            return "bus";
        }
    },

    TUBE {
        public String toString() {
            return "tube";
        }
    },

    CYCLE {
        public String toString() {
            return "cycle";
        }
    }
}
