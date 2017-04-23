package com.sharifin.galleryview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mostafavi on 2/8/2017.
 */

public class GalleryView extends RelativeLayout {


    private Context mContext;
    private RecyclerView.Adapter adapter;
    private GalleryFragment fragment;


    public interface OnCenterViewClickListener {
        void onClick(View view, int position);
    }

    public interface OnSelectedItemChangedListener {
        void onSelectedChange(View item, int position);
    }

    public GalleryView(Context context) throws Exception {
        super(context);
        init(context, null);
    }

    public GalleryView(Context context, AttributeSet attrs) throws Exception {
        super(context, attrs);
        init(context, attrs);
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyleAttr) throws Exception {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        fragment = (GalleryFragment) ((FragmentActivity) mContext).getSupportFragmentManager().findFragmentById(R.id.mostafavi_galleryView_frameMain);
        super.onConfigurationChanged(newConfig);
    }

    private void init(Context context, AttributeSet attrs) throws Exception {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_main, this, true);
        float rotationY = 0, scale = 0;

        int overScrollMode = -1;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GalleryView);
            for (int i = 0; i < array.getIndexCount(); i++) {
                int attr = array.getIndex(i);
                if (attr == R.styleable.GalleryView_scale) {
                    scale = array.getFloat(attr, 0);
                } else if (attr == R.styleable.GalleryView_rotationY)
                    rotationY = array.getFloat(attr, 0);
            }
            array.recycle();
            int[] androidAttrs = new int[]{android.R.attr.overScrollMode};
            TypedArray a = context.obtainStyledAttributes(attrs, androidAttrs);
            overScrollMode = a.getInt(0, View.OVER_SCROLL_ALWAYS);
        }

        if (mContext instanceof FragmentActivity) {

        } else {
            throw new Exception("You most use FragmentActivity except Activity");
        }

        FragmentManager supportFragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        fragment = new GalleryFragment();
        this.fragment.setRotationY(rotationY);
        this.fragment.setScale(scale);
        this.fragment.setOverScrollMode(overScrollMode);
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mostafavi_galleryView_frameMain, fragment);
        fragmentTransaction.commit();
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        fragment.setAdapter(adapter);
    }

    public OnCenterViewClickListener getOnCenterViewClickListener() {
        if (fragment != null)
            return fragment.onCenterViewClickListener;
        return null;
    }

    public void setOnCenterViewClickListener(OnCenterViewClickListener onCenterViewClickListener) {
        if (fragment != null)
            fragment.setOnCenterViewClickListener(onCenterViewClickListener);
    }

    public void setScale(float scale) {
        if (fragment != null) {
            fragment.setScale(scale);
        }
    }

    public void setRotationY(float rotationY) {
        if (fragment != null) {
            fragment.setRotationY(rotationY);
        }
    }

    public void notifyDataSetChanged() {
        if (fragment != null)
            fragment.notifyDataSetChanged();
    }

    public void setCurrentIndex(int index) {
        if (fragment != null) {
            fragment.setSelectedIndex(index);
        }
    }

    public void smoothToPosition(int index) {
        if (fragment != null) {
            fragment.smoothToPosition(index);
        }
    }

    public int getCurrentIndex() {
        if (fragment != null)
            return fragment.getSelectedIndex();
        return 0;
    }

    public OnSelectedItemChangedListener getSelectedItemChangedListener() {
        if (fragment != null)
            return fragment.getOnSelectedItemChangedListener();
        return null;
    }

    public void setSelectedItemChangedListener(OnSelectedItemChangedListener selectedItemChangedListener) {
        if (fragment != null)
            fragment.setOnSelectedItemChangedListener(selectedItemChangedListener);
    }

    public void setOverScrollMode(int overScrollMode) {
        if (fragment != null)
            fragment.setOverScrollMode(overScrollMode);
    }

    public RecyclerView getRecyclerView() {
        if (fragment != null)
            fragment.getRecyclerView();
        return null;
    }

    public static class GalleryFragment extends Fragment {

        private Context mContext;
        private RecyclerView recyclerView;
        private RecyclerView.Adapter adapter;
        private int pageWidth;
        private static int viewWidth;
        private int currentX = 0, overScrollMode;
        private int width;
        private int selectedIndex = 0;
        private int lastIndex = -1;
        private OnCenterViewClickListener onCenterViewClickListener;
        Map<Integer, View> galleryViews = new HashMap<>();
        private float scale = 0;
        private float rotationY = 0;
        private OnSelectedItemChangedListener onSelectedItemChangedListener;
        private RecyclerView.OnScrollListener scrollListener;
        boolean canSmooth = false;
        private int scrollIndex = 1;

        @Override
        public void onAttach(Context context) {
            mContext = context;
            super.onAttach(context);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.layout_gallery, container, false);
            currentX = 0;
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerGallery);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            if (adapter != null) {
                setAdapter(adapter);
            }
            if (overScrollMode != -1)
                recyclerView.setOverScrollMode(overScrollMode);
            return view;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt("SelectedIndex", selectedIndex);
            outState.putInt("viewWidth", viewWidth);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if (savedInstanceState != null && savedInstanceState.containsKey("viewWidth"))
                viewWidth = savedInstanceState.getInt("viewWidth");
            if (savedInstanceState != null && savedInstanceState.containsKey("SelectedIndex")) {
                selectedIndex = savedInstanceState.getInt("SelectedIndex");
                scrollToIndex(selectedIndex);
            }

        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (recyclerView != null && recyclerView.getAdapter() != null) {
                        recyclerView.getAdapter().notifyDataSetChanged();
                        recyclerView.scrollToPosition(0);
                        currentX = 0;
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                smoothRecycler(selectedIndex);
                            }
                        }, 100);
                    }
                }
            }, 100);
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                scrollToIndex(selectedIndex);
            }
        }

        public void setAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
            if (recyclerView != null) {
                recyclerView.setAdapter(getThisAdapter());
                if (scrollListener == null) {
                    scrollListener = getScrollListener();
                    recyclerView.addOnScrollListener(scrollListener);
                }
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollToPosition(0);
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollToIndex(selectedIndex);
                            }
                        }, 100);
                    }
                });
            }

        }

        @NonNull
        private RecyclerView.OnScrollListener getScrollListener() {
            return new RecyclerView.OnScrollListener() {


                float localX = 0;

                @Override
                public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == 0 && canSmooth) {
                        Log.d("@Index1", scrollIndex + "," + selectedIndex);
                        calculateCenter();
//                        if (scrollIndex != selectedIndex && scrollIndex != selectedIndex + 1)
//                            notifyDataSetChanged();
                        canSmooth = false;
                    }
                    if (newState == 1) {
                        canSmooth = true;
                        localX = 0;
                    }

                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    currentX += dx;
                    localX += dx;
                    if (rotationY == 0 && scale == 0)
                        return;
                    if (currentX == 0)
                        return;
                    float percent = ((float) currentX - (viewWidth * (scrollIndex - 1))) / (viewWidth);
                    if (percent >= 1) {
                        scrollIndex++;
                        percent = 0;
                    }
                    if (percent < 0) {
                        scrollIndex--;
                        percent = 1;
                    }


                    View nextView = recyclerView.findViewWithTag(scrollIndex + 1);
                    View previousView = recyclerView.findViewWithTag(scrollIndex - 1);
                    View currentView = recyclerView.findViewWithTag(scrollIndex);

//                    if (nextView != null && (nextView.getScaleX() > 1 - scale || (nextView.getScaleX() == 1 - scale && dx > 0))) {
                    if (nextView != null) {
                        nextView.setScaleX(((1 - scale) + (percent * scale)));
                        nextView.setScaleY(((1 - scale) + (percent * scale)));
                        nextView.setRotationY(rotationY + ((-1 * rotationY) * percent));
                    }
//                    if (previousView != null && (previousView.getScaleX() > 1 - scale || (previousView.getScaleX() == 1 - scale && dx < 0))) {
//                        previousView.setScaleX(((1 - scale) + (percent * scale)));
//                        previousView.setScaleY(((1 - scale) + (percent * scale)));
//                        previousView.setRotationY(rotationY + ((-1 * rotationY) * percent));
//                    }
                    if (currentView != null) {
                        Log.d("@Index", scrollIndex + "," + percent + "," + currentX);
                        currentView.setScaleY((1 - (percent * scale)));
                        currentView.setScaleX((1 - (percent * scale)));
                        currentView.setRotationY((rotationY * percent));
                    }


//                    float firstPosition = (index - 1) * viewWidth;
//                    float percent = ((float) currentX - firstPosition) / ((index * viewWidth) - firstPosition);
//                    if (percent >= 1) {
//                        index++;
//                        percent = 0;
//                    }
//                    if (percent < 0) {
//                        index--;
//                        percent = 1;
//                    }
//                    Log.d("@Index", index + "");
////                    View nextView = galleryViews.get(index + 1);
////                    View previousView = galleryViews.get(index - 1);
////                    View currentView = galleryViews.get(index);
//                    View nextView = recyclerView.findViewWithTag(index + 1);
//                    View previousView = recyclerView.findViewWithTag(index - 1);
//                    View currentView = recyclerView.findViewWithTag(index);
//                    if (nextView != null) {
//                        nextView.setScaleX(((1 - scale) + (percent * scale)));
//                        nextView.setScaleY(((1 - scale) + (percent * scale)));
//                        nextView.setRotationY(rotationY + ((-1 * rotationY) * percent));
//                    }
//                    if (currentView != null) {
//                        currentView.setScaleY((1 - (percent * scale)));
//                        currentView.setScaleX((1 - (percent * scale)));
//                        currentView.setRotationY((rotationY * percent));
//                    }
//                    if (previousView != null) {
//                        previousView.setScaleX(((1 - scale) + (percent * scale)));
//                        previousView.setScaleY(((1 - scale) + (percent * scale)));
//                        previousView.setRotationY(rotationY + ((-1 * rotationY) * percent));
//                    }
                }
            };
        }

        private void calculateCenter() {
            long index = Math.round((double) getCurrentX() / (double) viewWidth);
            if (index < 0)
                index = 0;
            if (index > adapter.getItemCount())
                index = adapter.getItemCount();
            selectedIndex = (int) index;
            smoothRecycler(selectedIndex);
        }

        private void smoothRecycler(final int position) {
            int toPosition = ((position) * viewWidth);
            recyclerView.smoothScrollBy(toPosition - getCurrentX(), 0);
            if (onSelectedItemChangedListener != null && lastIndex != selectedIndex)
                onSelectedItemChangedListener.onSelectedChange(galleryViews.get(selectedIndex), selectedIndex);
            lastIndex = selectedIndex;

//            notifyDataSetChanged();
        }

        private RecyclerView.Adapter getThisAdapter() {
            return new RecyclerView.Adapter() {

                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    if (viewType == -100) {
                        RelativeLayout itemView = new RelativeLayout(mContext);
                        itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        MyHolder myHolder = new MyHolder(itemView);
                        itemView.addView(myHolder.view);
                        return myHolder;
                    }
                    RecyclerView.ViewHolder viewHolder = adapter.onCreateViewHolder(parent, viewType);
                    viewHolder.itemView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                    viewHolder.itemView.setScaleX(1 - scale);
                    viewHolder.itemView.setScaleY(1 - scale);
                    viewWidth = viewHolder.itemView.getMeasuredWidth();
                    return viewHolder;
                }

                @Override
                public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
                    galleryViews.put(position, holder.itemView);
                    holder.itemView.setTag(position);
                    if (holder.getItemViewType() == -100) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                int size = (getWidth() - viewWidth) / 2;
                                ((MyHolder) holder).view.setLayoutParams(new RelativeLayout.LayoutParams(size, ViewGroup.LayoutParams.WRAP_CONTENT));
                            }
                        });

                    } else {
                        adapter.onBindViewHolder(holder, position - 1);
                        if (rotationY != 0 || scale != 0) {
                            if (selectedIndex != position - 1) {
//                                ObjectAnimator.ofFloat(holder.itemView, "scaleX", holder.itemView.getScaleX(), 1 - scale).setDuration(400).start();
//                                ObjectAnimator.ofFloat(holder.itemView, "scaleY", holder.itemView.getScaleY(), 1 - scale).setDuration(400).start();
                                holder.itemView.setScaleY(1 - scale);
                                holder.itemView.setScaleX(1 - scale);
                                holder.itemView.setRotationY(rotationY);
                            } else {
//                                ObjectAnimator.ofFloat(holder.itemView, "scaleX", holder.itemView.getScaleX(), 1f).setDuration(400).start();
//                                ObjectAnimator.ofFloat(holder.itemView, "scaleY", holder.itemView.getScaleY(), 1f).setDuration(400).start();
                                holder.itemView.setScaleY(1f);
                                holder.itemView.setScaleX(1f);
                                holder.itemView.setRotationY(0f);
                            }
                            Log.d("@Position", position + "," + selectedIndex);
                        }
                        holder.itemView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int index = (int) (Math.round((double) getCurrentX() / (double) viewWidth)) + 1;
                                if (index < 0)
                                    index = 0;
                                if (index > adapter.getItemCount())
                                    index = adapter.getItemCount();
                                if (index == position) {
                                    if (onCenterViewClickListener != null)
                                        onCenterViewClickListener.onClick(holder.itemView, position - 1);
                                } else {
                                    selectedIndex = position - 1;
                                    smoothRecycler(selectedIndex);
                                }
                            }
                        });
                    }
                }

                @Override
                public int getItemCount() {
                    return adapter.getItemCount() + 2;
                }

                @Override
                public int getItemViewType(int position) {
                    if (position == 0 || position == getItemCount() - 1)
                        return -100;
                    return adapter.getItemViewType(position - 1);
                }
            };
        }

        public int getWidth() {
            return recyclerView.getWidth();
        }

        public void setWidth(int width) {
            this.width = width;
        }

        class MyHolder extends RecyclerView.ViewHolder {
            View view;

            public MyHolder(View itemView) {
                super(itemView);
                itemView.setScaleX(1 - scale);
                itemView.setScaleY(1 - scale);
                view = new View(mContext);
                view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        public int getCurrentX() {
//        return recyclerView.computeHorizontalScrollOffset();
            return currentX;
        }

        public OnCenterViewClickListener getOnCenterViewClickListener() {
            return onCenterViewClickListener;
        }

        public void setOnCenterViewClickListener(OnCenterViewClickListener onCenterViewClickListener) {
            this.onCenterViewClickListener = onCenterViewClickListener;
        }

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
            notifyDataSetChanged();
        }

        public float getRotationY() {
            return rotationY;
        }

        public void setRotationY(float rotationY) {
            this.rotationY = rotationY * -100;
            notifyDataSetChanged();
        }

        public int getSelectedIndex() {
            return selectedIndex;
        }

        public void setSelectedIndex(final int selectedIndex) {
            if (selectedIndex < 0 || selectedIndex >= adapter.getItemCount())
                throw new NullPointerException("Index is more than gallery size");
            this.selectedIndex = selectedIndex;
            scrollIndex = selectedIndex + 1;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollToIndex(selectedIndex);
                    notifyDataSetChanged();
                }
            }, 10);
        }

        private void scrollToIndex(int selectedIndex) {
            if (recyclerView != null && viewWidth != 0) {
                int newPosition = selectedIndex * viewWidth;
                recyclerView.scrollBy(newPosition - currentX, 0);
                if (onSelectedItemChangedListener != null && lastIndex != selectedIndex) {
                    onSelectedItemChangedListener.onSelectedChange(galleryViews.get(selectedIndex), selectedIndex);
                }
                lastIndex = selectedIndex;
            }
        }

        public void smoothToPosition(int selectedIndex) {
            if (selectedIndex < 0 || selectedIndex >= adapter.getItemCount())
                throw new ArrayIndexOutOfBoundsException("Index is more than gallery size");
            this.selectedIndex = selectedIndex;
            scrollIndex = selectedIndex + 1;
            if (recyclerView != null)
                smoothRecycler(selectedIndex);
        }

        public void notifyDataSetChanged() {
            if (recyclerView != null && recyclerView.getAdapter() != null)
                recyclerView.getAdapter().notifyDataSetChanged();
        }

        public OnSelectedItemChangedListener getOnSelectedItemChangedListener() {
            return onSelectedItemChangedListener;
        }

        public void setOnSelectedItemChangedListener(OnSelectedItemChangedListener onSelectedItemChangedListener) {
            this.onSelectedItemChangedListener = onSelectedItemChangedListener;
        }

        public void setOverScrollMode(int overScrollMode) {
            this.overScrollMode = overScrollMode;
            if (recyclerView != null && overScrollMode != -1)
                recyclerView.setOverScrollMode(overScrollMode);
        }

        public RecyclerView getRecyclerView() {
            return recyclerView;
        }
    }

}
