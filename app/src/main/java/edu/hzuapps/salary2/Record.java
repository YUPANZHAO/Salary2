package edu.hzuapps.salary2;

import java.math.BigDecimal;
import java.util.Date;

public class Record {

    private Integer recordid;
    private Integer styleid;
    private String stylename;
    private Integer number;
    private Date time;
    private String remark;
    private Integer isdel;
    private BigDecimal money;

    public Record() {
    }

    public Record(Integer recordid, Integer styleid, Integer number, Date time, String remark, Integer isdel) {
        this.recordid = recordid;
        this.styleid = styleid;
        this.number = number;
        this.time = time;
        this.remark = remark;
        this.isdel = isdel;
    }

    public Integer getRecordid() {
        return recordid;
    }

    public Integer getStyleid() {
        return styleid;
    }

    public String getStylename() {
        return stylename;
    }

    public Integer getNumber() {
        return number;
    }

    public Date getTime() {
        return time;
    }

    public String getRemark() {
        return remark;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setRecordid(Integer recordid) {
        this.recordid = recordid;
    }

    public void setStyleid(Integer styleid) {
        this.styleid = styleid;
    }

    public void setStylename(String stylename) {
        this.stylename = stylename;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
