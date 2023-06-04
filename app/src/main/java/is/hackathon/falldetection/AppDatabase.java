package is.hackathon.falldetection;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NumberEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NumberEntryDao numberDao();
}
