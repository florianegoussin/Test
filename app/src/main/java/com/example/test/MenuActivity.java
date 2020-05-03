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
    @BindView(R.id.rechercheMes) TextView rechercheMes;
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
        Intent liste = new Intent(this, ZoneActivity.class);
        startActivity(liste);
    }

    @OnClick(R.id.carte)
    public void onClickCarte(){
        Intent carte = new Intent(this, MapActivity.class);
        startActivity(carte);
    }

    @OnClick(R.id.detail)
    public void onClickDetail(){
        Intent detail = new Intent(this, ListeActivity.class);
        startActivity(detail);
    }

    /*@OnClick(R.id.recherche)
    public  void onClickRecherche(){
        Intent recherche = new Intent(this,RechercheActivity.class);
        startActivity(recherche);
    }*/

    @OnClick(R.id.rechercheMes)
    public  void onClickRechercheMes(){
        Intent rechercheMes = new Intent(this,RechercheActivityMesure.class);
        startActivity(rechercheMes);
    }


    @OnClick(R.id.favoris)
    public  void onClickFav(){
        Intent favoris = new Intent(this, FavorisActivity.class);
        startActivity(favoris);
    }
}

