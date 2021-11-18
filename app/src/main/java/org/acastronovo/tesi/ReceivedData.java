package org.acastronovo.tesi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.internal.Token;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ReceivedData extends AppCompatActivity {

    //Toolbar
    Toolbar toolbar;

    //UI
    TextView temperature;
    TextView heartbeat;
    TextView humidity;
    TextView position;
    TextView altitude;
    TextView pressure;
    TextView pedometer;
    TextView calories;

    MqttAsyncClient client;
    String TAG = "MqttService";
    private final String serverUri = "tcp://192.168.1.3:1883";
    private final String user = "alberto";
    private final String pwd = "1708";
    private MemoryPersistence persistance;


    String topic = "";
    int qos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_data);

        //Toolbar
        toolbar = findViewById(R.id.toolbar_received_data);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //UI
        temperature = findViewById(R.id.temperature);
        heartbeat = findViewById(R.id.heartbeat);
        humidity = findViewById(R.id.humidity);
        position = findViewById(R.id.position);
        altitude = findViewById(R.id.altitude);
        pressure = findViewById(R.id.pressure);
        pedometer = findViewById(R.id.pedometer);
        calories = findViewById(R.id.calories);



        String clientId = MqttClient.generateClientId();

        try {
            client = new MqttAsyncClient(serverUri, clientId, persistance);
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            mqttConnectOptions.setCleanSession(true);
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setUserName(user);
            mqttConnectOptions.setPassword(pwd.toCharArray());
            IMqttToken token = client.connect(mqttConnectOptions);

            try{
                Thread.sleep(5000);
            }catch (Exception e){
                e.printStackTrace();
            }

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.e(TAG, "Connection Lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.e(TAG, "Message arrived");
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.e(TAG, "Delivery Complete");
                }
            });

            token = client.connect(mqttConnectOptions);

        }catch (MqttException ex){
            ex.printStackTrace();
        }


        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //The message was published
                    Log.d(TAG, "The message was published");
                    }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    //The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                        Log.d(TAG, "The subscription could not be performed");
                    }
                });

            } catch (MqttException e) {
                e.printStackTrace();
            }


        temperature.setText("Temperature" + );
        heartbeat.setText("Heartbeat" + );
        humidity.setText("Humidity" + );
        position.setText("Position" + );
        altitude.setText("Altitude" + );
        pressure.setText("Pressure" + );
        pedometer.setText("Pedometer" + );
        calories.setText("Calories" + );
    }
}
