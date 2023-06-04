package is.hackathon.falldetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.UUID;

public class SettingsActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{
    AppDatabase appDatabase;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    EditText number_text;
    Button save;
    Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        appDatabase = Room.databaseBuilder(this, AppDatabase.class,"database").allowMainThreadQueries().build();

        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_settigs_layout);

        number_text = findViewById(R.id.number_text);
        if(!appDatabase.numberDao().getAllNumbers().isEmpty()) {
            number_text.setText(appDatabase.numberDao().getAllNumbers().get(0).phoneNumber);
        }
        save = findViewById(R.id.btn_save);
        menu = findViewById(R.id.btn_menu);

        navigationView.setNavigationItemSelectedListener(this);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appDatabase.numberDao().getAllNumbers().isEmpty()){
                    final String entry_id = UUID.randomUUID().toString();
                    NumberEntry numberEntry = new NumberEntry(entry_id, number_text.getText().toString());
                    appDatabase.numberDao().insertNumberEntry(numberEntry);
                    Toast.makeText(getApplicationContext(),"številka shranjena", Toast.LENGTH_SHORT).show();
                }else{
                    final String entry_id = appDatabase.numberDao().getAllNumbers().get(0).uid;
                    NumberEntry numberEntry = new NumberEntry(entry_id, number_text.getText().toString());
                    appDatabase.numberDao().update(numberEntry);
                    Toast.makeText(getApplicationContext(),"številka shranjena", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.home:
                // Start the HomeActivity
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.settings:
                // Start the SettingsActivity
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return false;
    }
}