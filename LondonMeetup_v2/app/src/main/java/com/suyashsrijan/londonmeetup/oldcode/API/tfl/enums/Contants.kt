package com.suyashsrijan.londonmeetup.API.tfl.enums

enum class Contants {

    APP_ID {
        override fun toString(): String {
            return "be8cb40d"
        }
    },

    APP_KEY {
        override fun toString(): String {
            return "74720a5ce2ccde1dfe9be0c492d24255"
        }
    },

    BASE_URL_STOPPOINT {
        override fun toString(): String {
            return "https://api.tfl.gov.uk/Stoppoint"
        }
    },

    BASE_URL {
        override fun toString(): String {
            return "https://api.tfl.gov.uk"
        }
    },

    BASE_URL_BIKES {
        override fun toString(): String {
            return "https://api.tfl.gov.uk/BikePoint"
        }
    }
}
