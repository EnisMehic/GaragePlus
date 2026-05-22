package com.ipi.garageplus.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.ipi.garageplus.model.ServiceRecord;
import com.ipi.garageplus.model.Vehicle;

@Database(entities = {Vehicle.class, ServiceRecord.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract VehicleDao vehicleDao();
    public abstract ServiceRecordDao serviceRecordDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "garageplus_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}