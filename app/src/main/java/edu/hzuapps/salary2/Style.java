package edu.hzuapps.salary2;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Style {

    private Integer styleid;
    private String stylename;
    private BigDecimal price;

    public void setStyleid(Integer styleid) {
        this.styleid = styleid;
    }

    public void setStylename(String stylename) {
        this.stylename = stylename;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStyleid() {
        return styleid;
    }

    public String getStylename() {
        return stylename;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
