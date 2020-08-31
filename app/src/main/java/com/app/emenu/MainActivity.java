package com.app.emenu;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.app.emenu.api.ApiClient;
import com.app.emenu.api.ApiInterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private final int MY_PERMISSIONS_REQUEST = 100;

    private GoogleMap googleMap;
    private LocationManager locationManager;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private TextView tv_name, tv_address, tv_city, tv_zipcode, tv_tel, tv_email, tv_takeaway, tv_wifi, tv_publicita;
    private ImageView iv_image;

    private double current_latitude;
    private double current_longitude;

    private void initService() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) ||ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage("Locations permissions are necessary");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST);
                    }
                });
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            initLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void initLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
                return;
            }
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, 0).show();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initService();
        //To check google play service available
        if(!isGooglePlayServicesAvailable()){
            Toast.makeText(this,"Google Play Services not available.",Toast.LENGTH_LONG).show();
            finish();
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        RequestBody requestLon = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(current_longitude));
        RequestBody requestLat = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(current_latitude));
        RequestBody requestDistance = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf("1000"));

        Call<JsonObject> call = apiInterface.pubblicita(requestLat, requestLon, requestDistance);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String response_body = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(response_body);
                    JSONObject resultObject = jsonObject.getJSONObject("response");
                    String result = resultObject.getString("result");
                    if (result.equals("OK")) {
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        String name = dataObject.getString("nome_esercizio");
                        String address = dataObject.getString("indirizzo_esercizio");
                        String city = dataObject.getString("citta_esercizio");
                        String zipcode = dataObject.getString("cap_esercizio");
                        String tel = dataObject.getString("telefono_esercizio");
                        String email = dataObject.getString("email_esercizio");
                        String takeaway = dataObject.getString("asporto");
                        String wifi = dataObject.getString("wifi");
                        String publicita = dataObject.getString("pubblicita");
                        String image = dataObject.getString("fotografia_esercizio").substring(1);

                        tv_name.setText(name);
                        tv_address.setText(address);
                        tv_city.setText(city);
                        tv_zipcode.setText(zipcode);
                        tv_tel.setText(tel);
                        tv_email.setText(email);
                        tv_takeaway.setText(takeaway);
                        tv_wifi.setText(wifi);
                        tv_publicita.setText(publicita);

                        Picasso.with(MainActivity.this).load("https://www.e-menu.tech" + image).into(iv_image);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_app_bar_open_drawer_description, R.string.nav_app_bar_navigate_up_description);
        drawer.addDrawerListener(toggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();
                if (id == R.id.nav_login) {
                    Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_recovery) {
                    Intent intent = new Intent(MainActivity.this, Recovery1Activity.class);
                    startActivity(intent);
                    finish();
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void initComponent() {
        tv_name = findViewById(R.id.tv_name);
        iv_image = findViewById(R.id.iv_image);
        tv_address = findViewById(R.id.tv_address);
        tv_city = findViewById(R.id.tv_city);
        tv_zipcode = findViewById(R.id.tv_zipcode);
        tv_tel = findViewById(R.id.tv_tel);
        tv_email = findViewById(R.id.tv_email);
        tv_takeaway = findViewById(R.id.tv_takeaway);
        tv_wifi = findViewById(R.id.tv_wifi);
        tv_publicita = findViewById(R.id.tv_publicita);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
        } else {
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, location.getLatitude() + " : " + location.getLongitude(), Toast.LENGTH_LONG).show();
        current_latitude = location.getLatitude();
        current_longitude = location.getLongitude();
    }
}