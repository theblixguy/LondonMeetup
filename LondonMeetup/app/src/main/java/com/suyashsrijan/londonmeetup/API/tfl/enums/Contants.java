package com.suyashsrijan.londonmeetup.API.tfl.enums;

public enum Contants {

    APP_ID {
        public String toString() {
            return "be8cb40d";
        }
    },

    APP_KEY {
        public String toString() {
            return "74720a5ce2ccde1dfe9be0c492d24255";
        }
    },

    BASE_URL_STOPPOINT {
        public String toString() {
            return "https://api.tfl.gov.uk/Stoppoint";
        }
    },

    BASE_URL_BIKES {
        public String toString() {
            return "https://api.tfl.gov.uk/BikePoint";
        }
    }
}
