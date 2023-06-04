package is.hackathon.falldetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FallActivity extends AppCompatActivity {

    AppDatabase appDatabase;
    Button home;
    Button message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall);

        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "database").allowMainThreadQueries().build();

        home = findViewById(R.id.btn_off);
        message = findViewById(R.id.btn_message);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }

        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(FallActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                    sendSMS();
                }else{
                    ActivityCompat.requestPermissions(FallActivity.this, new String[]{Manifest.permission.SEND_SMS},100);
                }
            }
        });
    }

    private void sendSMS() {
        String phoneNumber = appDatabase.numberDao().getAllNumbers().get(0).phoneNumber;
        if(!phoneNumber.isEmpty()){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,null, "padec zaznan", null, null);
            Toast.makeText(FallActivity.this, "sporočilo poslano", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(FallActivity.this, "številka je prazna", Toast.LENGTH_SHORT).show();
        }
    }
}