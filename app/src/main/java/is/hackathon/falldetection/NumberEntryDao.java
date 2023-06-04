package is.hackathon.falldetection;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NumberEntryDao {
    @Insert
    void insertNumberEntry(NumberEntry numberEntry);

    @Query("SELECT * FROM numberEntry")
    List<NumberEntry> getAllNumbers();

    @Query("SELECT * FROM numberEntry WHERE uid=:numberId")
    NumberEntry getNumber(String numberId);

    @Update
    void update(NumberEntry numberEntry);

    @Delete
    int delete(NumberEntry numberEntry);
}
