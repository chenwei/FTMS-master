package com.rds.ftms.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

@DatabaseTable(tableName = "tb_family")
public class FtmsFamilyBean {

    public FtmsFamilyBean() {
    }
    @DatabaseField(generatedId = true)
    private int fid;

    @DatabaseField(columnName = "fname")
    private String fname;

    @DatabaseField(columnName = "ffirstname")
    private String ffirstname;

    @DatabaseField(columnName = "fsubdivision")
    private String fsubdivision;

    @DatabaseField(columnName = "finper")
    private String finper;

    @DatabaseField(columnName = "findate")
    private String findate;

    @DatabaseField(columnName = "fremark")
    private String fremark;

    @DatabaseField(columnName = "status")
    private String status;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFfirstname() {
        return ffirstname;
    }

    public void setFfirstname(String ffirstname) {
        this.ffirstname = ffirstname;
    }

    public String getFsubdivision() {
        return fsubdivision;
    }

    public void setFsubdivision(String fsubdivision) {
        this.fsubdivision = fsubdivision;
    }

    public String getFinper() {
        return finper;
    }

    public void setFinper(String finper) {
        this.finper = finper;
    }

    public String getFindate() {
        return findate;
    }

    public void setFindate(String findate) {
        this.findate = findate;
    }

    public String getFremark() {
        return fremark;
    }

    public void setFremark(String fremark) {
        this.fremark = fremark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return fid + "," + fname + ","+ ffirstname+"," + fsubdivision+ ","+findate+","+fremark;
    }


}
