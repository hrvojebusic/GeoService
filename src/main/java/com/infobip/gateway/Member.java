package com.infobip.gateway;

/**
 * Created by hbusic on 10.8.2016.
 */
public class Member {

    private String number;

    public Member(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return number != null ? number.equals(member.number) : member.number == null;

    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }
}
