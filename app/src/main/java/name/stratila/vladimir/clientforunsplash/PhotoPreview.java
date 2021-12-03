package name.stratila.vladimir.clientforunsplash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class PhotoPreview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.purple_700));

        getSupportActionBar().setTitle("Photo Preview");

        ImageView imageView = findViewById(R.id.myZoomageView);
        Glide.with(this).load(getIntent().getStringExtra("image")).into(imageView);
    }
}