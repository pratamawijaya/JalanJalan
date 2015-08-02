package com.pratamawijaya.wisatajogja.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pratamawijaya.wisatajogja.R;
import com.pratamawijaya.wisatajogja.models.LocationModel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Created by pratama on 2/18/15.
 */
public class ListRouteAdapter extends RecyclerView.Adapter<ListRouteAdapter.ViewHolderRoute> {
    private Context context;
    private List<LocationModel> locationModels;

    public ListRouteAdapter(Context context, List<LocationModel> locationModels) {
        this.context = context;
        this.locationModels = locationModels;
    }

    @Override
    public ViewHolderRoute onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_route, parent, false);
        return new ViewHolderRoute(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderRoute holder, int position) {
        holder.txtDari.setText(locationModels.get(position).getAsal());
        holder.txtTujuan.setText(locationModels.get(position).getTujuan());
        holder.txtDistance.setText(locationModels.get(position).getDistance());
        holder.txtEta.setText("ETA :" + locationModels.get(position).getDuration());
    }

    @Override
    public int getItemCount() {
        return locationModels.size();
    }

    public static class ViewHolderRoute extends RecyclerView.ViewHolder {
        @Bind(R.id.txttujuan)
        TextView txtTujuan;
        @Bind(R.id.txtdari)
        TextView txtDari;
        @Bind(R.id.txtdistance)
        TextView txtDistance;
        @Bind(R.id.txteta)
        TextView txtEta;

        public ViewHolderRoute(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
