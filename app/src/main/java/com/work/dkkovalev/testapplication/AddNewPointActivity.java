package com.work.dkkovalev.testapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddNewPointActivity extends AppCompatActivity {

    private EditText titleET;
    private EditText descriptionET;
    private Button confirmBTN;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_point);
        lat = getIntent().getDoubleExtra("lat", 0.1);
        lng = getIntent().getDoubleExtra("lng", 0.1);
        setupUI();
    }

    private void setupUI() {
        titleET = (EditText) findViewById(R.id.et_title);
        descriptionET = (EditText) findViewById(R.id.et_description);
        confirmBTN = (Button) findViewById(R.id.btn_submit_adding);
        confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Point point = new Point(titleET.getText().toString(), descriptionET.getText().toString(), lat, lng);
                sendToServer(point);
            }
        });
    }

    private void sendToServer(Point point) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(getString(R.string.endpoint))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        ApiMethods apiMethods = restAdapter.create(ApiMethods.class);
        apiMethods.postPoint(point, new Callback<List<Point>>() {
            @Override
            public void success(List<Point> points, Response response) {
                Toast.makeText(getApplication(), getString(R.string.toast_success),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                //Toast.makeText(getApplication(), getString(R.string.toast_error) + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
