package com.example.chinmay.anew;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chinmay.anew.adapter.ProductAdapter;
import com.example.chinmay.anew.model.ProductDetail;
import com.example.chinmay.anew.model.RetailerDetail;
import com.example.chinmay.anew.viewModel.ProductDetailViewModel;
import com.example.chinmay.anew.viewModel.RetailerViewModel;

import java.util.ArrayList;

public class RetailerDetailAct extends AppCompatActivity {

    private TextView name;
    private TextView subLocality;
    private TextView location;
    private TextView number;
    private TextView timing;
    private ImageView imageView;
    private RecyclerView recyclerView;


    private RetailerDetail retailerDetail;
    private ArrayList<ProductDetail> productDetails;

    private String retailerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_detail);

        Intent intent = getIntent();
        if(intent == null){
            finish();
        }

        retailerId = intent.getStringExtra("RETID");
        if(retailerId == null){
            finish();
        }

        name = findViewById(R.id.retailerNameAtRetDet);
        subLocality = findViewById(R.id.subLocalityAtRetDet);
        location = findViewById(R.id.addressAtRetDtl);
        number = findViewById(R.id.callAtRetDtl);
        timing = findViewById(R.id.timingAtRetDtl);
        imageView = findViewById(R.id.retailerImgAtRetDtl);
        recyclerView = findViewById(R.id.productRecyclerView);

        RetailerViewModel retailerViewModel = ViewModelProviders.of(this).get(RetailerViewModel.class);
        retailerViewModel.getRetailerDetail(this,retailerId).observe(this, new Observer<com.example.chinmay.anew.model.RetailerDetail>() {
            @Override
            public void onChanged(@Nullable com.example.chinmay.anew.model.RetailerDetail retailerDetailres) {
                retailerDetail = retailerDetailres;
                if(retailerDetail != null){
                    if(retailerDetail.getCvs() != null)
                        getProduct(retailerDetail.getCvs());
                }else{
                    Toast.makeText(RetailerDetailAct.this,"Fetching Problem",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getProduct(String id){
        ProductDetailViewModel productDetailViewModel = ViewModelProviders.of(this).get(ProductDetailViewModel.class);
        productDetailViewModel.getProductDetail(this,id).observe(this, new Observer<ArrayList<ProductDetail>>() {
            @Override
            public void onChanged(@Nullable ArrayList<ProductDetail> productDetailsRes) {
                productDetails = productDetailsRes;
                setView();
            }
        });
    }

    public void setView(){
        if(retailerDetail != null){
            String locationStr = retailerDetail.getLocality() != null? retailerDetail.getLocality():"";
            String address1Str = retailerDetail.getAddLine1() != null? retailerDetail.getAddLine1():"";

            String timing1 = retailerDetail.getShopOpenTime1() != null? retailerDetail.getShopOpenTime1():"";
            String timing2 = retailerDetail.getShopCloseTime1() != null? retailerDetail.getShopCloseTime1():"";

            String imageStr = retailerDetail.getShopPhoto() != null? retailerDetail.getShopPhoto():"";
            String nameStr = retailerDetail.getEnterpriseName() != null? retailerDetail.getEnterpriseName():"";
            String subLocalityStr = retailerDetail.getSubLocality1() != null? retailerDetail.getSubLocality1():"" ;
            String addressStr = address1Str+"\n"+subLocalityStr+"\n"+locationStr;
            String timingStr = "Open -> "+timing1+"\nClose -> "+timing2;
            String numberStr = retailerDetail.getMobileNo() != null? retailerDetail.getMobileNo():"";

            Glide.with(this).load("http://ec2-13-58-16-206.us-east-2.compute.amazonaws.com/rt/"+imageStr).into(imageView);
            name.setText(nameStr);
            subLocality.setText(subLocalityStr);
            location.setText(addressStr);
            number.setText(numberStr);
            timing.setText(timingStr);

           if(productDetails != null){
               recyclerView.setLayoutManager(new GridLayoutManager(this,1));
               ProductAdapter productAdapter = new ProductAdapter(productDetails,this);
               recyclerView.setAdapter(productAdapter);
           }
        }
    }
}
