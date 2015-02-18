package com.pratamawijaya.panggilpeta.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.pratamawijaya.panggilpeta.BaseActivity;
import com.pratamawijaya.panggilpeta.R;
import com.pratamawijaya.panggilpeta.adapter.ListRouteAdapter;
import com.pratamawijaya.panggilpeta.helper.RecyclerItemClickListener;
import com.pratamawijaya.panggilpeta.model.LatLngModel;
import com.pratamawijaya.panggilpeta.model.LocationModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends BaseActivity implements RecyclerItemClickListener.OnItemClickListener {

    public static final int FROM_LOCAL = 0;
    public static final int FROM_NET = 1;


    @InjectView(R.id.recyclerview)
    RecyclerView recyclerView;
    @InjectView(R.id.emptytext)
    TextView emptyText;
    @InjectView(R.id.fab)
    FloatingActionButton fab;

    private ListRouteAdapter adapterRoute;
    private List<LocationModel> locationModels;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        realm = Realm.getInstance(this);

        getSupportActionBar().setTitle(getResources().getString(R.string.txt_liburan));

        locationModels = new ArrayList<>();
        adapterRoute = new ListRouteAdapter(this, locationModels);

        updateView();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterRoute);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
        readDataFromDb();
    }

    private void updateView() {
        if (locationModels.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void readDataFromDb() {
        if (locationModels.size() > 0)
            locationModels.clear();
        RealmResults<LocationModel> result = realm.where(LocationModel.class).findAll();
        for (LocationModel data : result) {
            Log.d("tag", "datanya " + data.getId());
            locationModels.add(data);

            for (LatLngModel test : data.getLatLngRealmList()) {
//                Log.d("tag", "lat lng dari :" + data.getId() + " -> " + test.getLat() + "," + test.getLng());
            }
        }
        // notify adapter
        adapterRoute.notifyDataSetChanged();

        // update view
        updateView();
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        startActivity(new Intent(this, AddRouteActivity.class));
    }

    @Override
    public void onItemClick(View v, int position) {
        Toast.makeText(this, "" + locationModels.get(position).getTujuan(), Toast.LENGTH_SHORT).show();
        Bundle data = new Bundle();
        data.putInt("status", FROM_LOCAL);
        data.putString("id", locationModels.get(position).getId());
        startActivity(new Intent(this, ViewRouteActivity.class).putExtras(data));
    }

    @Override
    protected void onResume() {
        super.onResume();
        readDataFromDb();
    }
}
