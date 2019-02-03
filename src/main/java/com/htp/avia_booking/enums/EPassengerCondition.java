package com.htp.avia_booking.enums;

import com.htp.avia_booking.dao.foldertest.ICondition;

public enum EPassengerCondition implements ICondition {
    DOCUMENT_ID("like","document_id");

    private String operand;
    private String columnName;

    EPassengerCondition(String operand, String columnName) {
        this.columnName=columnName;
        this.operand=operand;
    }

    @Override
    public String getOperand() {
        return this.operand;
    }

    @Override
    public String getColumnName() {
        return this.columnName;
    }
}
