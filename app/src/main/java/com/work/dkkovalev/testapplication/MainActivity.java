package com.work.dkkovalev.testapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, RecyclerAdapter.OnRecyclerItemClick {

    private final static int INTERVAL = 600000;


    private RestAdapter restAdapter;
    public ArrayList<Point> pointsArrayList;

    protected GoogleApiClient googleApiClient;
    protected LocationRequest locationRequest;
    protected Location location;

    private double lat;
    private double lng;

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    private AlertDialog.Builder alertDialog;
    private ApiMethods apiMethods;

    private int pos;

    private String pointId = "155c52b2-c4ae-4e50-881d-ea3ebcaee1c7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("key", pointsArrayList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        pointsArrayList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.rv_points);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
        recyclerView.setLayoutManager(linearLayoutManager);

        buildGoogleApiClient();

        restAdapter = createRestAdapter();

        getData();
    }

    private RestAdapter createRestAdapter() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(getString(R.string.endpoint))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        return restAdapter;
    }

    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (location == null) {
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
            }
            startLocationUpdates();
        }

        alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.alert_dialog_title))
                .setMessage(getString(R.string.alert_dialog_description))
                .setPositiveButton(getString(R.string.alert_dialog_button_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiMethods apiMethods = restAdapter.create(ApiMethods.class);
                        apiMethods.deletePoint(pointsArrayList.get(pos).getId(), new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {

                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                    }
                })
                .setNegativeButton(getString(R.string.alert_dialog_button_update), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Point point = new Point("blah", 0.0, 0.0);
                        ApiMethods apiMethods = restAdapter.create(ApiMethods.class);
                        apiMethods.updatePoint(pointsArrayList.get(pos).getId(), point, new Callback<List<Point>>() {
                            @Override
                            public void success(List<Point> points, Response response) {
                                Toast.makeText(getApplication(), getString(R.string.toast_success), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                //Toast.makeText(getApplication(), getString(R.string.toast_error) + error.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(INTERVAL / 2);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    private void getData() {
        ApiMethods apiMethods = restAdapter.create(ApiMethods.class);
        apiMethods.getPoints(new Callback<List<Point>>() {
            @Override
            public void success(List<Point> points, Response response) {

                pointsArrayList.addAll(points);

                recyclerAdapter = new RecyclerAdapter(points, getApplication());
                recyclerAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplication(), getString(R.string.alert_dialog_description_download) + error.getCause().getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void itemClicked(View view, int pos) {
        this.pos = pos;

        alertDialog.show();
    }

    @Override
    protected void onResume() {
        if (recyclerView != null)
            getData();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:

                Intent intent = new Intent(getApplication(), AddNewPointActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                startActivity(intent);

                break;
            case R.id.action_show_one_item:

                ApiMethods apiMethods = restAdapter.create(ApiMethods.class);
                apiMethods.getPointById(pointId, new Callback<Point>() {
                    @Override
                    public void success(Point point, Response response) {
                        Toast.makeText(getApplication(), pointId, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
