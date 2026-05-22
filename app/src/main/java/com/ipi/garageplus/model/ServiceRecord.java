package com.ipi.garageplus.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "service_records",
        foreignKeys = @ForeignKey(
                entity = Vehicle.class,
                parentColumns = "id",
                childColumns = "vehicleId",
                onDelete = ForeignKey.CASCADE
        )
)
public class ServiceRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int vehicleId;
    private String tipServisa;
    private String opis;
    private double cijena;
    private String datum;
    private int kilometrazaServisa;

    public ServiceRecord(int vehicleId, String tipServisa, String opis,
                         double cijena, String datum, int kilometrazaServisa) {
        this.vehicleId = vehicleId;
        this.tipServisa = tipServisa;
        this.opis = opis;
        this.cijena = cijena;
        this.datum = datum;
        this.kilometrazaServisa = kilometrazaServisa;
    }

    // Getteri i setteri
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public String getTipServisa() { return tipServisa; }
    public void setTipServisa(String tipServisa) { this.tipServisa = tipServisa; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }

    public double getCijena() { return cijena; }
    public void setCijena(double cijena) { this.cijena = cijena; }

    public String getDatum() { return datum; }
    public void setDatum(String datum) { this.datum = datum; }

    public int getKilometrazaServisa() { return kilometrazaServisa; }
    public void setKilometrazaServisa(int km) { this.kilometrazaServisa = km; }
}