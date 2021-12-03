package name.stratila.vladimir.clientforunsplash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import name.stratila.vladimir.clientforunsplash.Api.ApiHelper;
import name.stratila.vladimir.clientforunsplash.Models.Photo;
import name.stratila.vladimir.clientforunsplash.Models.SearchResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridLayoutManager manager;
    private PhotoAdapter adapter;
    private ProgressDialog dialog;

    private ArrayList<Photo> list;
    private String query = "";
    private int page = 1;
    private final int pageSize = 30;
    private boolean isLoading;
    private boolean isLastPage;
    private boolean isSearching;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.purple_700));

        getSupportActionBar().setTitle("Latest Photos");

        recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        adapter = new PhotoAdapter(this, list);
        manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItem = manager.getChildCount();
                int totalItem = manager.getItemCount();
                int firstVisibleItemPos = manager.findFirstVisibleItemPosition();
                if (!isLoading && !isLastPage){
                    if ((visibleItem+firstVisibleItemPos >= totalItem)
                            && firstVisibleItemPos >= 0 && totalItem >= pageSize){
                        if (isSearching) {
                            page++;
                            searchData(query);
                        } else {
                            page++;
                            getData();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        getData();
    }

    private void getData() {
        isLoading = true;
        ApiHelper.getApiInterface().getPhotos(page, pageSize)
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                        if (response.body() != null) {
                            list.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }
                        isLoading = false;
                        dialog.dismiss();

                        if (list.size() > 0) {
                            isLastPage = list.size() < pageSize;
                        } else isLastPage = true;

                    }

                    @Override
                    public void onFailure(Call<List<Photo>> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) { return true; }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                page = 1;
                isSearching = false;
                dialog.show();
                list.clear();
                getData();
                return true;
            }
        });

        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                page = 1;
                query = q;
                isSearching = true;
                dialog.show();
                list.clear();
                searchData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void searchData(String query) {
        isLoading = true;
        ApiHelper.getApiInterface().searchPhotos(query, page, pageSize)
                .enqueue(new Callback<SearchResult>() {
                    @Override
                    public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                        if (response.body() != null) {
                            list.addAll(response.body().getResults());
                            adapter.notifyDataSetChanged();
                        }
                        isLoading = false;
                        dialog.dismiss();

                        if (list.size() > 0) {
                            isLastPage = list.size() < pageSize;
                        } else isLastPage = true;
                    }

                    @Override
                    public void onFailure(Call<SearchResult> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}