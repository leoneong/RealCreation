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
 * Created by leone on 9/2/2018.
 */

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.ViewHolder>  {
    private Context mContext;

    private List<Promotion> mPromotionlist;

    public PromotionAdapter(List<Promotion> promotionList) {
        mPromotionlist = promotionList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView promotionimage;

        public ViewHolder(View view){
            super(view);
            cardView =(CardView) view;
            promotionimage =(ImageView)view.findViewById(R.id.promo_image);

        }
    }

    @Override
    public PromotionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.promotion,parent,false);
        return new PromotionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Promotion promotion = mPromotionlist.get(position);
        holder.promotionimage.setImageResource(promotion.getImageID());


    }


    @Override
    public int getItemCount() {
        return mPromotionlist.size();
    }
}
