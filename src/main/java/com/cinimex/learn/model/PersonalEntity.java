package com.cinimex.learn.model;

import javax.persistence.*;

/**
 * Created by nXs on 08.04.2015.
 */
@Entity
@Table(name = "personal", schema = "", catalog = "personaldb")
public class PersonalEntity {
    private int id;
    private String name;
    private String appointment;
    private int salary;

    @Id
    @Column(name = "Id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "appointment")
    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    @Basic
    @Column(name = "Salary")
    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonalEntity that = (PersonalEntity) o;

        if (id != that.id) return false;
        if (salary != that.salary) return false;
        if (appointment != null ? !appointment.equals(that.appointment) : that.appointment != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (appointment != null ? appointment.hashCode() : 0);
        result = 31 * result + salary;
        return result;
    }
}
