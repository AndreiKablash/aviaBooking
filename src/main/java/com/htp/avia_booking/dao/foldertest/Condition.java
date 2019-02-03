package com.htp.avia_booking.dao.foldertest;

public class Condition {
    private ICondition condition;
    private String value;

    public Condition() {
    }

    public Condition(ICondition condition, String value) {
        this.condition = condition;
        this.value = value;
    }

    public ICondition getCondition() {
        return condition;
    }

    public String getValue() {
        return value;
    }

    public void setCondition(ICondition condition) {
        this.condition = condition;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
