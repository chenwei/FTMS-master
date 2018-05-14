package com.rds.ftms.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

@DatabaseTable(tableName = "tb_family_member")
public class FtmsFamilyMemberBean {

    public FtmsFamilyMemberBean() {
    }
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "sex")
    private String sex;

    @DatabaseField(columnName = "nation")
    private String nation;

    @DatabaseField(columnName = "birthday")
    private String birthday;

    @DatabaseField(columnName = "address")
    private String address;

    @DatabaseField(columnName = "idcard")
    private String idcard;

    @DatabaseField(columnName = "office")
    private String office;

    @DatabaseField(columnName = "effective")
    private String effective;

    @DatabaseField(columnName = "residence")
    private String residence;

    @DatabaseField(columnName = "regtime")
    private String regtime;

    @DatabaseField(columnName = "fid")
    private int fid;

    @DatabaseField(columnName = "pid")
    private int pid;

    @DatabaseField(columnName = "delstatus")
    private String delstatus;

    @DatabaseField(columnName = "isprint")
    private String isprint;

    /**
     * 采样
     */
    @DatabaseField(columnName = "sampling")
    private String sampling;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getRegtime() {
        return regtime;
    }

    public void setRegtime(String regtime) {
        this.regtime = regtime;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getDelstatus() {
        return delstatus;
    }

    public void setDelstatus(String delstatus) {
        this.delstatus = delstatus;
    }

    public String getIsprint() {
        return isprint;
    }

    public void setIsprint(String isprint) {
        this.isprint = isprint;
    }

    public String getSampling() {
        return sampling;
    }

    public void setSampling(String sampling) {
        this.sampling = sampling;
    }

    @Override
    public String toString() {
        return id+","+sex+","+nation+","+birthday+","+address+","+idcard+","+office+","+effective+","+residence+","+regtime+","+fid+","+pid+","+sampling;
    }


}
