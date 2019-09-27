package com.yalkansoft.bean;

import java.io.Serializable;

public class HallResultBean implements Serializable {
                            /**大厅*/
    public static final int TYPE_HALL = 1;
                            /**院子*/
    public static final int TYPE_YARD = 2;
                            /**包厢*/
    public static final int TYPE_BALCONY = 3;


    public static final int STATUS_NOTHING = 1;
    public static final int STATUS_ENOUGH = 2;
    public static final int STATUS_NOT_ENOUGH = 3;
    private int type;

    private int status;

    private int personNumber;

    public int getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(int personNumber) {
        this.personNumber = personNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
