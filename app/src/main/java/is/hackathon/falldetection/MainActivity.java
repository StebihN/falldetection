package is.hackathon.falldetection;

import static java.lang.Math.sqrt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import is.hackathon.falldetection.ml.Model;

public class MainActivity extends AppCompatActivity implements SensorEventListener, NavigationView.OnNavigationItemSelectedListener {
    private SensorManager sensorManager;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Button menuBtn;
    private int count = 0;
    private float ax,ay,az;
    private float value;
    private ArrayList<Float> result;
    private boolean isFall = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), 1666);
        result = new ArrayList<>();

        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        menuBtn = findViewById(R.id.btn_menu);

        navigationView.setNavigationItemSelectedListener(this);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
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
    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_LINEAR_ACCELERATION){
            count++;
            ax=event.values[0];
            ay=event.values[1];
            az=event.values[2];

            value = (float)sqrt(ax*ax + ay*ay + az*az);
            result.add(value);
            if(count == 120){
                detectFall(result);
                count = 0;
                result.clear();
            }
        }
    }
    public void detectFall(ArrayList<Float> result){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 120}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 120);
            byteBuffer.order(ByteOrder.nativeOrder());

            for(int i = 0; i < result.size();i++){
                byteBuffer.putFloat(result.get(i));
            }

            inputFeature0.loadBuffer(byteBuffer);

            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            Float prediction = outputFeature0.getFloatValue(0);

            if(prediction >= 0.5){
                isFall = true;
                handleFall();
            }else{
                isFall = false;
            }

            model.close();
        } catch (IOException e) {
            Log.d("exception", e.getMessage());
        }

    }
    public void handleFall(){
        if(isFall){
            Intent i = new Intent(getApplicationContext(),FallActivity.class);
            startActivity(i);
            finish();
        }
    }
}