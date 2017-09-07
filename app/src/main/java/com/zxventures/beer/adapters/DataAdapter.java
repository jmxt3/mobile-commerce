package com.zxventures.beer.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zxventures.beer.R;
import com.zxventures.beer.models.ProductModel;
import com.zxventures.beer.utils.ImageUtils;
import com.zxventures.beer.utils.Log;
import com.zxventures.beer.utils.NetworkUtils;

import java.util.List;

/**
 * Created by joaopmmachete on 9/6/17.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private static final String TAG = DataAdapter.class.getSimpleName();
    private static final String FAKE_IMG_URL = "http://wwww.google.com/img.jpg";

    private List<ProductModel> data;
    private Context context;
    private Resources res;

    public DataAdapter(Context context, List<ProductModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        res = context.getResources();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.price.setText(data.get(i).price);
        viewHolder.title.setText(data.get(i).title);

        final int imgSize = ImageUtils.dpToPx(res, (int) res.getDimension(R.dimen.thumbnail_image));

        try {

            String url = TextUtils.isEmpty(data.get(i).imageUrl) ?
                    FAKE_IMG_URL:data.get(i).imageUrl;

            Picasso.with(context).invalidate(url);
            Picasso.with(context)
                    .load(url)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(imgSize, imgSize)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_sync)
                    .error(R.mipmap.ic_avatar)
                    .into(viewHolder.img);
        } catch (final NullPointerException e) {
            Log.e(TAG, NullPointerException.class
                    .getSimpleName(), e);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView price;
        private TextView title;
        private ImageView img;

        ViewHolder(View view) {
            super(view);

            price = view.findViewById(R.id.price);
            title = view.findViewById(R.id.title);
            img = view.findViewById(R.id.img);
        }
    }
}
