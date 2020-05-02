package com.example.test;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Database.DataSource.FavoriteRepository;
import com.example.test.Database.Local.EDMTRoomDatabase;
import com.example.test.Database.Local.FavoriteDataSource;
import com.example.test.Database.ModelDB.Favorite;
import com.example.test.ui.FavoriteAdapter;
import com.example.test.utils.Common;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavoriteListActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView_fav)
    RecyclerView recyclerView_fav;

    // private FavoriteAdapter mFavAdpt;

    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        ButterKnife.bind(this);

        compositeDisposable = new CompositeDisposable();

        //recyclerView_fav = (RecyclerView)findViewById(R.id.recyclerView_fav);
        //mFavAdpt = new FavoriteAdapter(this, new ArrayList<>());
        //recyclerView_fav.setAdapter(mFavAdpt);
        recyclerView_fav.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_fav.setHasFixedSize(true);

        //initDB
        Common.edmtRoomDatabase = EDMTRoomDatabase.getInstance(this);
        //Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.edmtRoomDatabase.CartDAO()));
        Common.favoriteRepository = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(Common.edmtRoomDatabase.favoriteDAO()));



        loadFavoritesItem();
    }

    private void loadFavoritesItem() {
        compositeDisposable.add(Common.favoriteRepository.getFavItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Favorite>>() {
                    @Override
                    public void accept(List<Favorite> favorites) throws Exception {
                        displayFavoriteItem(favorites);
                    }
                }));
    }

    private void displayFavoriteItem(List<Favorite> favorites) {
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(this,favorites);
        recyclerView_fav.setAdapter(favoriteAdapter);
    }

    @OnClick(R.id.icon_menu)
    public void clickedOnSwitchToMenu(){
        Intent switchToMenu = new Intent (this, MenuActivity.class);
        startActivity(switchToMenu);
    }

}
