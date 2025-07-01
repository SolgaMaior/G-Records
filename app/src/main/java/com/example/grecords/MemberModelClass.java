package com.example.grecords;

public class MemberModelClass {

    private String member_name;
    private String start_date;
    private String end_date;

    // Constructor
    public MemberModelClass(String member_name, String start_date, String end_date) {
        this.member_name = member_name;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    // Getter and Setter
    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
