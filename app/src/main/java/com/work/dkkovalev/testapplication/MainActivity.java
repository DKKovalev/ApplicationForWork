package com.work.dkkovalev.testapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private Point point;
    private RestAdapter restAdapter;
    public ArrayList<Point> pointsArrayList;
    private MathHandler mathHandler;
    private ListView itemsLV;
    private Button mapBTN;
    private Button deleteBTN;
    private Button postBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        point = new Point("Point 1", "Cool point", 0.0, 0.0);

        mapBTN = (Button) findViewById(R.id.btn_map);
        mapBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("point", pointsArrayList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        deleteBTN = (Button)findViewById(R.id.btn_delete);
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiMethods apiMethods = restAdapter.create(ApiMethods.class);
                apiMethods.deletePoint("13744169-7675-41b4-b52b-c0020e585253", new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        });

        postBTN = (Button)findViewById(R.id.btn_post);
        postBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiMethods apiMethods = restAdapter.create(ApiMethods.class);
                apiMethods.postPoint(point, new Callback<List<Point>>() {
                    @Override
                    public void success(List<Point> points, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        });

        restAdapter = createRestAdapter();

        pointsArrayList = new ArrayList<>();
        mathHandler = new MathHandler();

        ApiMethods apiMethods = restAdapter.create(ApiMethods.class);
        apiMethods.getPoints(new Callback<List<Point>>() {

            @Override
            public void success(List<Point> points, Response response) {
                ArrayList<Double> distances = new ArrayList<>();
                pointsArrayList.addAll(points);
                for (Point point : points) {
                    distances.add(mathHandler.getDistance(0.0, 0.0, point.getLat(), point.getLng()));
                }

                Collections.sort(distances);
                for (Double distance : distances) {
                    //Toast.makeText(getApplication(), String.valueOf(distance), Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(getApplication(), String.valueOf(distances.get(3)), Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplication(), getString(R.string.alert_dialog_description_download) + error.getCause().getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private RestAdapter createRestAdapter(){
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(getString(R.string.endpoint))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        return restAdapter;
    }


    private class DataWrapper implements Serializable {
        private ArrayList<Point> points;

        public DataWrapper(ArrayList<Point> points) {
            this.points = points;
        }

        public ArrayList<Point> getPoints() {
            return this.points;
        }
    }
}
