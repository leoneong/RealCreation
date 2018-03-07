package com.example.realcreation;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by leone on 7/2/2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context mContext;

    private List<Product> mProductlist;

    public ProductAdapter(List<Product> productList) {
        mProductlist = productList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView productimage;
        TextView productname;

        public ViewHolder(View view){
            super(view);
            cardView =(CardView) view;
            productimage =(ImageView)view.findViewById(R.id.product_image);
            productname = (TextView)view.findViewById(R.id.product_name);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.popular_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = mProductlist.get(position);
        holder.productname.setText(product.getName());
        Glide.with(mContext).load(product.getImageID()).into(holder.productimage);
    }

    @Override
    public int getItemCount() {
        return mProductlist.size();
    }


}
