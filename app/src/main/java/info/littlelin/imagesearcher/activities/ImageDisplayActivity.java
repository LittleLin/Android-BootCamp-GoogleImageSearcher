package info.littlelin.imagesearcher.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import info.littlelin.imagesearcher.R;
import info.littlelin.imagesearcher.models.ImageResult;

public class ImageDisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        // Remove the actionbar
        //this.getActionBar().hide();

        // Pull out the url from the intent
        ImageResult result = (ImageResult) this.getIntent().getSerializableExtra("result");

        // Find the image view
        ImageView ivImageResult = (ImageView) this.findViewById(R.id.ivImageResult);

        // Load the image into image view by using Picasso
        Picasso.with(this).load(result.fullUrl).placeholder(R.drawable.placeholder).into(ivImageResult);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void closeFullImage(View view) {
        this.finish();
    }
}
