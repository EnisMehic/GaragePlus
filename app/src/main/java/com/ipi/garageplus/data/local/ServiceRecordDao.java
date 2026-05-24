package com.ipi.garageplus.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ipi.garageplus.model.ServiceRecord;

import java.util.List;

@Dao
public interface ServiceRecordDao {

    @Insert
    void insert(ServiceRecord serviceRecord);

    @Update
    void update(ServiceRecord serviceRecord);

    @Delete
    void delete(ServiceRecord serviceRecord);

    @Query("SELECT * FROM service_records WHERE vehicleId = :vehicleId ORDER BY datum DESC")
    LiveData<List<ServiceRecord>> getServicesByVehicle(int vehicleId);

    @Query("SELECT * FROM service_records WHERE vehicleId = :vehicleId ORDER BY tipServisa ASC, datum DESC")
    LiveData<List<ServiceRecord>> getServicesByVehicleSortedByType(int vehicleId);

    @Query("SELECT * FROM service_records WHERE vehicleId = :vehicleId ORDER BY cijena DESC, datum DESC")
    LiveData<List<ServiceRecord>> getServicesByVehicleSortedByPrice(int vehicleId);

    @Query("SELECT * FROM service_records WHERE vehicleId = :vehicleId AND (tipServisa LIKE '%' || :query || '%' OR opis LIKE '%' || :query || '%') ORDER BY datum DESC")
    LiveData<List<ServiceRecord>> searchServices(int vehicleId, String query);

    @Query("SELECT SUM(cijena) FROM service_records WHERE vehicleId = :vehicleId")
    LiveData<Double> getTotalCostByVehicle(int vehicleId);
}