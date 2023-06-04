package is.hackathon.falldetection;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NumberEntry {
    @PrimaryKey
    @NonNull
    public String uid;

    @ColumnInfo(name = "number")
    public String phoneNumber;


    public NumberEntry(@NonNull String uid, String phoneNumber){
        this.uid = uid;
        this.phoneNumber = phoneNumber;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
