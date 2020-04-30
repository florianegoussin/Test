package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MenuActivity extends AppCompatActivity {

    @BindView(R.id.liste) TextView liste;
    @BindView(R.id.carte) TextView carte;
    @BindView(R.id.detail) TextView detail;
    @BindView(R.id.recherche) TextView recherche;
    @BindView(R.id.favoris) TextView favoris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        // Do NOT forget to call super.onResume()
        super.onResume();
    }


    @OnClick(R.id.liste)
    public void onClickListe(){
        Intent test = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(test);
    }

    @OnClick(R.id.carte)
    public void onClickCarte(){
        Intent test = new Intent(MenuActivity.this, MapActivity.class);
        startActivity(test);
    }

    @OnClick(R.id.detail)
    public void onClickDetail(){
        Intent test = new Intent(MenuActivity.this, ListeActivity.class);
        startActivity(test);
    }


    @OnClick(R.id.favoris)
    public  void onClickFav(){
        Intent test = new Intent(MenuActivity.this,FavoriteListActivity.class);
        startActivity(test);
    }
}

