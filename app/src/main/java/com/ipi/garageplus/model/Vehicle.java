package com.ipi.garageplus.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vehicles")
public class Vehicle {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String userId;
    private String marka;
    private String model;
    private int godina;
    private int kilometraza;
    private String registracija;
    private String napomena;

    public Vehicle(String userId, String marka, String model,
                   int godina, int kilometraza, String registracija, String napomena) {
        this.userId = userId;
        this.marka = marka;
        this.model = model;
        this.godina = godina;
        this.kilometraza = kilometraza;
        this.registracija = registracija;
        this.napomena = napomena;
    }

    // Getteri i setteri
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getMarka() { return marka; }
    public void setMarka(String marka) { this.marka = marka; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getGodina() { return godina; }
    public void setGodina(int godina) { this.godina = godina; }

    public int getKilometraza() { return kilometraza; }
    public void setKilometraza(int kilometraza) { this.kilometraza = kilometraza; }

    public String getRegistracija() { return registracija; }
    public void setRegistracija(String registracija) { this.registracija = registracija; }

    public String getNapomena() { return napomena; }
    public void setNapomena(String napomena) { this.napomena = napomena; }
}