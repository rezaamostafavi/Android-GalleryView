package com.example.mostafavi.galleryview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sharifin.galleryview.GalleryView;

/**
 * Created by mostafavi on 2/13/2017.
 */

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        GalleryView galleryView = (GalleryView) findViewById(R.id.galleryView);
        galleryView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.lst_item_gallery_view, parent, false);
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
                Toast.makeText(TestActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }
        });
        galleryView.setRotationY(0.3f);
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView tvPassUnit;

        public Holder(View itemView) {
            super(itemView);
            tvPassUnit = (TextView) itemView.findViewById(R.id.tvPassSession);

        }
    }
}
