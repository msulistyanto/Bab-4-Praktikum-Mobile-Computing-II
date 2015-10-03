package com.example.bab4prakmc2;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
	private float lastX,lastY,lastZ;
	private SensorManager sensorManager;
	private Sensor accelerometer;
	
	private float deltaXMax =0.0f, deltaYMax =0.0f, deltaZMax =0.0f;
	private float deltaX=0.0f, deltaY=0.0f, deltaZ=0.0f;
	private float vibrateThreshold =0.0f;
	
	private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;
	public Vibrator v;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initalizeView();
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
			accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
			vibrateThreshold = accelerometer.getMaximumRange()/2;
		} else { }
		v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
	}
	private void initalizeView() {
		currentX = (TextView) findViewById(R.id.currentX);
		currentY = (TextView) findViewById(R.id.currentY);
		currentZ = (TextView) findViewById(R.id.currentZ);
		
		maxX =(TextView) findViewById(R.id.maxX);
		maxY =(TextView) findViewById(R.id.maxY);
		maxZ =(TextView) findViewById(R.id.maxZ);
	}
	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		displayCleanValues();
		displayCurrentValues();
		displayMaxValues();

		deltaX = Math.abs(lastX - event.values[0]);
		deltaY = Math.abs(lastY - event.values[1]);
		deltaZ = Math.abs(lastZ - event.values[2]);

		if (deltaX < 2)
			deltaX = 0;
		if (deltaY < 2)
			deltaY = 0;
		if ((deltaX > vibrateThreshold) || (deltaY > vibrateThreshold) || (deltaZ > vibrateThreshold)) {
			v.vibrate(50);
		}
		
	}
	private void displayMaxValues() {
		if (deltaX > deltaXMax) {
			deltaXMax = deltaX;
			maxX.setText(Float.toString(deltaXMax));
		}
		if (deltaY > deltaYMax) {
			deltaYMax = deltaY;
			maxY.setText(Float.toString(deltaYMax));
		}
		if (deltaZ > deltaZMax) {
			deltaZMax = deltaZ;
			maxZ.setText(Float.toString(deltaZMax));
		}
	}
	private void displayCurrentValues() {
		currentX.setText(Float.toString(deltaX));
		currentY.setText(Float.toString(deltaY));
		currentZ.setText(Float.toString(deltaZ));
	}
	private void displayCleanValues() {
		currentX.setText("0.0");
		currentY.setText("0.0");
		currentZ.setText("0.0");
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
