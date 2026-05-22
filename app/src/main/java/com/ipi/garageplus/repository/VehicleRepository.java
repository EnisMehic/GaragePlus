package com.ipi.garageplus.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.ipi.garageplus.data.local.AppDatabase;
import com.ipi.garageplus.data.local.ServiceRecordDao;
import com.ipi.garageplus.data.local.VehicleDao;
import com.ipi.garageplus.model.ServiceRecord;
import com.ipi.garageplus.model.Vehicle;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VehicleRepository {

    private VehicleDao vehicleDao;
    private ServiceRecordDao serviceRecordDao;
    private ExecutorService executor;

    public VehicleRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        vehicleDao = db.vehicleDao();
        serviceRecordDao = db.serviceRecordDao();
        executor = Executors.newSingleThreadExecutor();
    }

    // Vozila
    public void insertVehicle(Vehicle vehicle) {
        executor.execute(() -> vehicleDao.insert(vehicle));
    }

    public void updateVehicle(Vehicle vehicle) {
        executor.execute(() -> vehicleDao.update(vehicle));
    }

    public void deleteVehicle(Vehicle vehicle) {
        executor.execute(() -> vehicleDao.delete(vehicle));
    }

    public LiveData<List<Vehicle>> getVehiclesByUser(String userId) {
        return vehicleDao.getVehiclesByUser(userId);
    }

    public LiveData<Vehicle> getVehicleById(int vehicleId) {
        return vehicleDao.getVehicleById(vehicleId);
    }

    // Servisni zapisi
    public void insertServiceRecord(ServiceRecord record) {
        executor.execute(() -> serviceRecordDao.insert(record));
    }

    public void updateServiceRecord(ServiceRecord record) {
        executor.execute(() -> serviceRecordDao.update(record));
    }

    public void deleteServiceRecord(ServiceRecord record) {
        executor.execute(() -> serviceRecordDao.delete(record));
    }

    public LiveData<List<ServiceRecord>> getServicesByVehicle(int vehicleId) {
        return serviceRecordDao.getServicesByVehicle(vehicleId);
    }

    public LiveData<List<ServiceRecord>> searchServices(int vehicleId, String query) {
        return serviceRecordDao.searchServices(vehicleId, query);
    }

    public LiveData<Double> getTotalCostByVehicle(int vehicleId) {
        return serviceRecordDao.getTotalCostByVehicle(vehicleId);
    }
}