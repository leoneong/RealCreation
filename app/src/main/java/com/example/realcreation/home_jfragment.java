package com.example.realcreation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by leone on 5/2/2018.
 */

public class home_jfragment extends Fragment {
    private Product[] products = {new Product("bag", R.drawable.bag), new Product("glass", R.drawable.glass)
            , new Product("Laoptop", R.drawable.laptop)};

    private Promotion[] promotion = {new Promotion(R.drawable.bag),new Promotion(R.drawable.laptop),
    new Promotion(R.drawable.glass)};

    private List<Product> productList = new ArrayList<>();
    private List<Promotion> promotionList = new ArrayList<>();

    private ProductAdapter adapter;
    private PromotionAdapter adapter1;


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, null);
        iniProducts();
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);

        iniPromotion();
        RecyclerView recyclerView1 = (RecyclerView)view.findViewById(R.id.recyclerview2);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(layoutManager1);
        adapter1 = new PromotionAdapter(promotionList);
        recyclerView1.setAdapter(adapter1);

        return view;


    }

    private void iniProducts() {
        productList.clear();
        for (int i = 0; i < 12; i++) {
            Random rand = new Random();
            int index = rand.nextInt(products.length);
            productList.add(products[index]);
        }
    }

    private void iniPromotion() {
        promotionList.clear();
        for (int i = 0; i < 5; i++) {
            Random rand = new Random();
            int index = rand.nextInt(promotion.length);
            promotionList.add(promotion[index]);
        }
    }
}
