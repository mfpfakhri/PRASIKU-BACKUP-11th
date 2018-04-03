package clouwiko.dev.prasiku.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.Interface.LoadMore;
import clouwiko.dev.prasiku.activity.model.Cat;

/**
 * Created by muham on 03/04/2018.
 */

class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBarLoad);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView catImage;
    public TextView catName, catReason;

    public ItemViewHolder(View ItemView) {
        super(itemView);
        catImage = (ImageView) itemView.findViewById(R.id.userhome_catphotos);
        catName = (TextView) itemView.findViewById(R.id.userhome_catname);
        catReason = (TextView) itemView.findViewById(R.id.userhome_catreason);
    }
}

public class UserHomeCatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    private Context context;
    private LoadMore loadMore;
    private boolean isLoading;
    private Activity activity;
    private List<Cat> cats;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;

    public UserHomeCatListAdapter(RecyclerView recyclerView, Activity activity, List<Cat> cats) {
        this.activity = activity;
        this.cats = cats;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null)
                        loadMore.onLoadMore();
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return cats.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(LoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.cat_list_layout, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder){
            Cat cat = cats.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            Picasso.get().load(cat.getCatProfilePhoto()).into(viewHolder.catImage);
            viewHolder.catName.setText(cats.get(position).getCatName());
            viewHolder.catReason.setText(cats.get(position).getCatReason());
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder)holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return cats.size();
    }

    public void setLoaded() {
        isLoading = false;
    }
}
