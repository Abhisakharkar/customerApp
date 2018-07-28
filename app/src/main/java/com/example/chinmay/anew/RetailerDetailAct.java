package com.example.chinmay.anew;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Handler;
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
import com.example.chinmay.anew.localDatabase.ProductDatabase;
import com.example.chinmay.anew.model.ProductDetail;
import com.example.chinmay.anew.model.RetailerDetail;
import com.example.chinmay.anew.model.RetailerProduct;
import com.example.chinmay.anew.utils.DbExecutor;
import com.example.chinmay.anew.viewModel.ProductDBViewModel;
import com.example.chinmay.anew.viewModel.ProductDetailViewModel;
import com.example.chinmay.anew.viewModel.ProductModelFactory;
import com.example.chinmay.anew.viewModel.RetailerViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RetailerDetailAct extends AppCompatActivity {

    private TextView name;
    private TextView subLocality;
    private TextView location;
    private TextView number;
    private TextView timing;
    private ImageView imageView;
    private RecyclerView recyclerView;


    private RetailerDetail retailerDetail;
    private ArrayList<ProductDetail> productDetails = new ArrayList<>();
    private HashMap<String,Integer> priceAndId = new HashMap<>();

    private String retailerId;
    private ArrayList<String> csv = new ArrayList<>();

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

                    ArrayList<RetailerProduct> productDetails = retailerDetail.getRetailerProducts();

                    for (int i=0;i<productDetails.size() ; i++){
                        String id = productDetails.get(i).getProductId();
                        priceAndId.put(id,productDetails.get(i).getPrice());
                        try{
                            loadFromDb(id);
                        }catch (Exception e){
                            csv.add(id);
                        }
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getProductFromApi();
                        }
                    },2000);

                }else{
                    Toast.makeText(RetailerDetailAct.this,"Fetching Problem",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getProductFromApi(){
        String id = "";
        for(int i=0;i<csv.size();i++){
            id += csv.get(i);
            if(csv.size()-1 != i){
                id += ",";
            }
        }
        ProductDetailViewModel productDetailViewModel = ViewModelProviders.of(this).get(ProductDetailViewModel.class);
        productDetailViewModel.getProductDetail(this,id).observe(this, new Observer<ArrayList<ProductDetail>>() {
            @Override
            public void onChanged(@Nullable ArrayList<ProductDetail> productDetailsRes) {
                productDetails.addAll(productDetailsRes);
                for(int i=0;i<productDetailsRes.size();i++){
                    if(productDetailsRes.get(i) != null){
                        addToDb(productDetailsRes.get(i));
                    }
                }
                setView();
            }
        });
    }

    public void loadFromDb(final String id){
        ProductDatabase productDatabase = ProductDatabase.getShowDatabase(this);
        ProductModelFactory productModelFactory = new ProductModelFactory(productDatabase,id);
        final ProductDBViewModel productDBViewModel = ViewModelProviders.of(this,productModelFactory).get(ProductDBViewModel.class);
        productDBViewModel.getProductDetailLiveData().observe(this, new Observer<ProductDetail>() {
            @Override
            public void onChanged(@Nullable ProductDetail productDetail) {
                if(productDetail == null){
                    csv.add(id);
                }else{
                    productDetails.add(productDetail);
                    setView();
                }
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

            Glide.with(this).load("http://ec2-13-59-88-132.us-east-2.compute.amazonaws.com/rt"+imageStr).into(imageView);
            name.setText(nameStr);
            subLocality.setText(subLocalityStr);
            location.setText(addressStr);
            number.setText(numberStr);
            timing.setText(timingStr);

           if(productDetails != null){
               recyclerView.setLayoutManager(new GridLayoutManager(this,1));
               ProductAdapter productAdapter = new ProductAdapter(productDetails,this,priceAndId);
               recyclerView.setAdapter(productAdapter);
           }
        }
    }

    private void addToDb(final ProductDetail productDetail){
        final Date date = new Date();
        final ProductDatabase productDatabase = ProductDatabase.getShowDatabase(this);

        DbExecutor.getDbExecutor().getBackgroundIo().execute(new Runnable() {
            @Override
            public void run() {
                if(productDetail != null){
                    productDatabase.productDao().addProductDetailToDB(new ProductDetail(productDetail.getId(),String.valueOf(priceAndId.get(productDetail.getId())),productDetail.getImage(),productDetail.getName(),date));
                }
            }
        });
    }
}
