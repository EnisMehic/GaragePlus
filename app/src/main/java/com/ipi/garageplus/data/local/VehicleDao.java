package com.ipi.garageplus.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.ipi.garageplus.model.Vehicle;
import java.util.List;

@Dao
public interface VehicleDao {

    @Insert
    void insert(Vehicle vehicle);

    @Update
    void update(Vehicle vehicle);

    @Delete
    void delete(Vehicle vehicle);

    @Query("SELECT * FROM vehicles WHERE userId = :userId ORDER BY id DESC")
    LiveData<List<Vehicle>> getVehiclesByUser(String userId);

    @Query("SELECT * FROM vehicles WHERE id = :vehicleId")
    LiveData<Vehicle> getVehicleById(int vehicleId);
}