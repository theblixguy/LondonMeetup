package com.suyashsrijan.londonmeetup.models;

public class Friend {

    private String friendName;
    private String friendMobileNumber;

    public Friend(String friendName, String friendMobileNumber) {
        this.friendName = friendName;
        this.friendMobileNumber = friendMobileNumber;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getFriendMobileNumber() {
        return friendMobileNumber;
    }
}
