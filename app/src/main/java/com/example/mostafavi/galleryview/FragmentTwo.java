package com.example.mostafavi.galleryview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sharifin.galleryview.GalleryView;

/**
 * Created by mostafavi on 2/13/2017.
 */

public class FragmentTwo extends Fragment {

    private Context mContext;
    private GalleryView galleryView;



    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        galleryView = (GalleryView) view.findViewById(R.id.galleryView);
        galleryView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = inflater.inflate(R.layout.lst_item_gallery_view, parent, false);
                return new Holder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                Holder myHolder = (Holder) holder;
                myHolder.tvPassUnit.setText("P2: " + (position + 1) + "/8");
            }

            @Override
            public int getItemCount() {
                return 12;
            }
        });

        galleryView.setOnCenterViewClickListener(new GalleryView.OnCenterViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();
            }
        });

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
