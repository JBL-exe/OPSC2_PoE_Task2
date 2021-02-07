package com.example.opsc2_poe_task2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
//initialize map
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//Location component
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
//Add Marker
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
//Calculate route
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import android.util.Log;
//Navigation UI
import android.view.View;
import android.widget.Button;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
//Nav Drawer Imports
import androidx.appcompat.widget.Toolbar;
//import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.Fragment;
import android.widget.AdapterView;
import android.widget.ListView;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

import android.os.Bundle;
    public class MainScreen extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {
        private MapView mapView;
        private MapboxMap mapboxMap;
        // variables for adding location layer
        private PermissionsManager permissionsManager;
        private LocationComponent locationComponent;
        // variables for calculating and drawing a route
        private DirectionsRoute currentRoute;
        private static final String TAG = "DirectionsActivity";
        private NavigationMapRoute navigationMapRoute;
        private static final String SOURCE_ID = "SOURCE_ID";
        private static final String ICON_ID = "ICON_ID";
        private static final String LAYER_ID = "LAYER_ID";
        // variables needed to initialize navigation
        private Button button, button1;
        //NavDrawer obj
        private PagerAdapter mPagerAdapter;
        private ViewPager mViewPager;
        private String[] mNavigationDrawerItemTitles;
        private DrawerLayout mDrawerLayout;
        private ListView mDrawerList;
        Toolbar toolbar;
        private CharSequence mDrawerTitle;
        private CharSequence mTitle;
        ActionBarDrawerToggle mDrawerToggle;
        FirebaseAuth fAuth;
        FirebaseFirestore fs;
        Button startMap;
        private String MapMeasurement;
        String userID;
        private String nameselect;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Mapbox.getInstance(this, getString(R.string.access_token));
            setContentView(R.layout.activity_main_screen);

            mapView = findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
            mapView.setVisibility(View.VISIBLE);
            startMap = findViewById(R.id.startButton);
            startMap.setVisibility(View.VISIBLE);
            button1 = findViewById(R.id.select_location_button);
            button1.setVisibility(View.VISIBLE);
            fAuth = FirebaseAuth.getInstance();
            fs = FirebaseFirestore.getInstance();


            mTitle = mDrawerTitle = getTitle();
            mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
            mDrawerLayout = findViewById(R.id.drawer_layout);
            mDrawerList = findViewById(R.id.left_drawer);

            setupToolbar();

            DataModel[] drawerItem = new DataModel[4];

            drawerItem[0] = new DataModel(R.drawable.ic_baseline_home_24, "Home");
            drawerItem[1] = new DataModel(R.drawable.ic_settings, "Settings");
            drawerItem[2] = new DataModel(R.drawable.ic_landmarks, "Landmarks");
            drawerItem[3] = new DataModel(R.drawable.ic_logout, "Logout");

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);

            DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view, drawerItem);
            mDrawerList.setAdapter(adapter);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            mDrawerLayout = findViewById(R.id.drawer_layout);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            setupDrawerToggle();
            userID = fAuth.getCurrentUser().getUid();


            DocumentReference documentReference = fs.collection("users").document(userID);
            documentReference.addSnapshotListener((value, e) -> {
                String UOM;

                assert value != null;
                UOM = value.getString("UOM");
                setMeasurement(UOM);
            });


        }

//        private void NameFavorite(String name) {
//            nameselect = name;
//        }

        private class DrawerItemClickListener implements ListView.OnItemClickListener {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }

        }

        private void selectItem(int position) {

            Fragment fragment = null;

            switch (position) {
                case 0:
                    Intent homeIntent = new Intent(getApplicationContext(), MainScreen.class);
                    startActivity(homeIntent);
                    button1.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    fragment = new SettingsFragment();
                    mapView.setVisibility(View.INVISIBLE);
                    startMap.setVisibility(View.INVISIBLE);
                    button1.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    fragment = new LMFragment();
                    mapView.setVisibility(View.INVISIBLE);
                    startMap.setVisibility(View.INVISIBLE);
                    button1.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    FirebaseAuth.getInstance().signOut();
                    Intent logoutIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(logoutIntent);
                    break;


                default:
                    break;
            }

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                setTitle(mNavigationDrawerItemTitles[position]);
                mDrawerLayout.closeDrawer(mDrawerList);

            } else {
                Timber.e("Error in creating fragment");
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public void setTitle(CharSequence title) {
            mTitle = title;
            getSupportActionBar().setTitle(mTitle);
        }

        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);
            mDrawerToggle.syncState();
        }

        void setupToolbar() {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        void setupDrawerToggle() {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
            mDrawerToggle.syncState();
        }


        @Override
        public void onMapReady(@NonNull final MapboxMap mapboxMap) {
            this.mapboxMap = mapboxMap;
            List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
            symbolLayerIconFeatureList.add(Feature.fromGeometry(
                    Point.fromLngLat(25.618637, -33.969492)));
            symbolLayerIconFeatureList.add(Feature.fromGeometry(
                    Point.fromLngLat(25.657956, -33.981392)));
            symbolLayerIconFeatureList.add(Feature.fromGeometry(
                    Point.fromLngLat(25.622146, -33.962019)));
            symbolLayerIconFeatureList.add(Feature.fromGeometry(
                    Point.fromLngLat(25.619459, -33.963063)));

            mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                    .withImage(ICON_ID, BitmapFactory.decodeResource(
                            MainScreen.this.getResources(), R.drawable.mapbox_marker_icon_default))
                    .withSource(new GeoJsonSource(SOURCE_ID,
                            FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))
                    .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                            .withProperties(
                                    iconImage(ICON_ID),
                                    iconAllowOverlap(true),
                                    iconIgnorePlacement(true)
                            )
                    ), style -> {
                enableLocationComponent(style);
                addDestinationIconSymbolLayer(style);
                mapboxMap.addOnMapClickListener(MainScreen.this);
                button = findViewById(R.id.startButton);
                button.setOnClickListener(v -> {
                    boolean simulateRoute = true;
                    NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                            .directionsRoute(currentRoute)
                            .shouldSimulateRoute(simulateRoute)
                            .lightThemeResId(R.style.NavigationViewDark)
                            .build();
                    // Call this method with Context from within an Activity
                    NavigationLauncher.startNavigation(MainScreen.this, options);
                });
            });


        }

        private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
            loadedMapStyle.addImage("destination-icon-id",
                    BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
            GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
            loadedMapStyle.addSource(geoJsonSource);
            SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
            destinationSymbolLayer.withProperties(
                    iconImage("destination-icon-id"),
                    iconAllowOverlap(true),
                    iconIgnorePlacement(true)
            );
            loadedMapStyle.addLayer(destinationSymbolLayer);
        }

        @SuppressWarnings({"MissingPermission"})
        @Override
        public boolean onMapClick(@NonNull final LatLng point) {
            Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
            Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                    locationComponent.getLastKnownLocation().getLatitude());

            GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
            if (source != null) {
                source.setGeoJson(Feature.fromGeometry(destinationPoint));
            }

            getRoute(originPoint, destinationPoint);
            button.setEnabled(true);
            button.setBackgroundResource(R.color.mapboxBlue);

            button1.setEnabled(true);
            button1.setBackgroundResource(R.color.mapboxBlue);
            button1.setOnClickListener(view -> {
                Button FavSelect = findViewById(R.id.btnFav);
                EditText FavName = findViewById(R.id.etFav);
                LinearLayout pop = findViewById(R.id.popup);

                pop.setVisibility(View.VISIBLE);
                button1.setVisibility(View.INVISIBLE);
                FavSelect.setOnClickListener(view1 ->
                {
                    String favname = FavName.getText().toString().trim();
                   // NameFavorite(favname);
                    pop.setVisibility(View.INVISIBLE);
                    button1.setVisibility(View.VISIBLE);
                    reverseGeocode(Point.fromLngLat(point.getLongitude(), point.getLatitude()), favname);
                });

            });


            return true;
        }

        private void getRoute(Point origin, Point destination) {

            if (MapMeasurement.equals("Imperial")) {
                NavigationRoute.builder(this)
                        .accessToken(Mapbox.getAccessToken())
                        .origin(origin)
                        .destination(destination)
                        .voiceUnits(DirectionsCriteria.IMPERIAL) //add this to change the format
                        .build()
                        .getRoute(new Callback<DirectionsResponse>() {
                            @Override
                            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                                // You can get the generic HTTP info about the response
                                Timber.d("Response code: %s", response.code());
                                if (response.body() == null) {
                                    Timber.e("No routes found, make sure you set the right user and access token.");
                                    return;
                                } else if (response.body().routes().size() < 1) {
                                    Timber.e("No routes found");
                                    return;
                                }

                                currentRoute = response.body().routes().get(0);

                                // Draw the route on the map
                                if (navigationMapRoute != null) {
                                    navigationMapRoute.removeRoute();
                                } else {
                                    navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.Theme_AppCompat_DayNight);
                                }
                                navigationMapRoute.addRoute(currentRoute);
                            }

                            @Override
                            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                                Timber.e("Error: %s", throwable.getMessage());
                            }
                        });
            } else //if (MapMeasurement == "Metric")
            {
                NavigationRoute.builder(this)
                        .accessToken(Mapbox.getAccessToken())
                        .origin(origin)
                        .destination(destination)
                        .voiceUnits(DirectionsCriteria.METRIC) //add this to change the format
                        .build()
                        .getRoute(new Callback<DirectionsResponse>() {
                            @Override
                            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                                // You can get the generic HTTP info about the response
                                Timber.d("Response code: %s", response.code());
                                if (response.body() == null) {
                                    Timber.e("No routes found, make sure you set the right user and access token.");
                                    return;
                                } else if (response.body().routes().size() < 1) {
                                    Timber.e("No routes found");
                                    return;
                                }

                                currentRoute = response.body().routes().get(0);

                                // Draw the route on the map
                                if (navigationMapRoute != null) {
                                    navigationMapRoute.removeRoute();
                                } else {
                                    navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.Theme_AppCompat_DayNight);
                                }
                                navigationMapRoute.addRoute(currentRoute);
                            }

                            @Override
                            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                                Timber.e("Error: %s", throwable.getMessage());
                            }
                        });
            }

        }

        @SuppressWarnings({"MissingPermission"})
        private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
            if (PermissionsManager.areLocationPermissionsGranted(this)) {
// Activate the MapboxMap LocationComponent to show user location
// Adding in LocationComponentOptions is also an optional parameter
                locationComponent = mapboxMap.getLocationComponent();
                locationComponent.activateLocationComponent(this, loadedMapStyle);
                locationComponent.setLocationComponentEnabled(true);
// Set the component's camera mode
                locationComponent.setCameraMode(CameraMode.TRACKING);
            } else {
                permissionsManager = new PermissionsManager(this);
                permissionsManager.requestLocationPermissions(this);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        @Override
        public void onExplanationNeeded(List<String> permissionsToExplain) {
            Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPermissionResult(boolean granted) {
            if (granted) {
                enableLocationComponent(mapboxMap.getStyle());
            } else {
                Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
                finish();
            }
        }

        @Override
        protected void onStart() {
            super.onStart();
            mapView.onStart();
        }

        @Override
        protected void onResume() {
            super.onResume();
            mapView.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();
            mapView.onPause();
        }

        @Override
        protected void onStop() {
            super.onStop();
            mapView.onStop();
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            mapView.onSaveInstanceState(outState);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            mapView.onDestroy();
        }

        @Override
        public void onLowMemory() {
            super.onLowMemory();
            mapView.onLowMemory();
        }

        public void setMeasurement(String dbType) {
            MapMeasurement = dbType;
            // Toast.makeText(getApplicationContext(),MapMeasurement, Toast.LENGTH_LONG ).show();
        }

        private void reverseGeocode(final Point point, String nameselect) {

                try {
                    MapboxGeocoding client = MapboxGeocoding.builder().accessToken(getString(R.string.access_token))
                            .query(Point.fromLngLat(point.longitude(), point.latitude())).geocodingTypes(GeocodingCriteria.TYPE_ADDRESS).build();
                    client.enqueueCall(new Callback<GeocodingResponse>() {
                        @Override
                        public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                            if (response.body() != null) {
                                List<CarmenFeature> results = response.body().features();
                                if (results.size() > 0) {
                                    final CarmenFeature feature = results.get(0);
                                    mapboxMap.getStyle(style -> {
                                        String longitude = String.valueOf(point.longitude());
                                        String latitude = String.valueOf(point.latitude());
                                        String adress = feature.placeName();
                                        Toast.makeText(MainScreen.this, adress, Toast.LENGTH_LONG).show();

                                        String lmID;
                                        lmID = fAuth.getCurrentUser().getUid();
                                        Map<String, Object> Landmark = new HashMap<>();
                                        Landmark.put("adress", adress);
                                        Landmark.put("Longitude", longitude);
                                        Landmark.put("Latitude", latitude);
                                        fs.collection("Landmark").document(lmID).collection("Favorites").document(nameselect).set(Landmark, SetOptions.merge());
                                    });
                                } else {
                                    Toast.makeText(MainScreen.this, "Error", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                            Timber.e("Geocoding Failure: %s", throwable.getMessage());
                        }
                    });
                } catch (ServicesException servicesException) {
                    Timber.e("Error geocoding: %s", servicesException.toString());
                    servicesException.printStackTrace();
                }
        }
    }