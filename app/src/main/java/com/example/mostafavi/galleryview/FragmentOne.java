package com.example.mostafavi.galleryview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sharifin.galleryview.GalleryView;

/**
 * Created by mostafavi on 2/13/2017.
 */

public class FragmentOne extends Fragment {

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        GalleryView galleryView = (GalleryView) view.findViewById(R.id.galleryView);
        galleryView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = inflater.inflate(R.layout.lst_item_gallery_view, parent, false);
                return new Holder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                Holder myHolder = (Holder) holder;
                myHolder.tvPassUnit.setText((position + 1) + "/8");
            }

            @Override
            public int getItemCount() {
                return 28;
            }
        });

        galleryView.setOnCenterViewClickListener(new GalleryView.OnCenterViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();
            }
        });

        galleryView.setSelectedItemChangedListener(new GalleryView.OnSelectedItemChangedListener() {
            @Override
            public void onSelectedChange(View item, int position) {
                Log.d("New Position", position + "");
            }
        });

        galleryView.notifyDataSetChanged();
        galleryView.smoothToPosition(1);
        galleryView.notifyDataSetChanged();
        galleryView.smoothToPosition(1);
        return view;
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView tvPassUnit;

        public Holder(View itemView) {
            super(itemView);
            tvPassUnit = (TextView) itemView.findViewById(R.id.tvPassSession);

        }
    }
}
