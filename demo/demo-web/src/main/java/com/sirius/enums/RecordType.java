package com.sirius.enums;

public enum RecordType {

    Initialization(0),
    Input(1),
    Output(2);


    int code;

    RecordType(int i) {
        this.code = i;
    }
}
