package com.rr.student.data.info;

public class StudentData {

    private String name;
    private String roll;
    private String reg;
    private String dep;


    public StudentData(String name, String roll, String reg, String dep) {
        this.name = name;
        this.roll = roll;
        this.reg = reg;
        this.dep = dep;
    }

    public String getName() {
        return name;
    }

    public String getRoll() {
        return roll;
    }

    public String getReg() {
        return reg;
    }

    public String getDep() {
        return dep;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }
}
