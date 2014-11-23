package info.littlelin.imagesearcher.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import info.littlelin.imagesearcher.R;

public class SettingActivity extends Activity {

    private Spinner spImageSizes;
    private Spinner spColorFilter;
    private Spinner spImageTypes;
    private EditText etSite;

    private List<String> imageSizeItems;
    private List<String> colorFilterItems;
    private List<String> imageTypeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        this.setupViews();
    }

    private void setupViews() {
        this.spImageSizes = (Spinner) this.findViewById(R.id.spImageSizes);
        this.spColorFilter = (Spinner) this.findViewById(R.id.spColorFilter);
        this.spImageTypes = (Spinner) this.findViewById(R.id.spImageTypes);
        this.etSite = (EditText) this.findViewById(R.id.etSite);

        // Setup the values of spinners
        this.imageSizeItems = new ArrayList<String>();
        this.imageSizeItems.add("small");
        this.imageSizeItems.add("medium");
        this.imageSizeItems.add("large");
        this.imageSizeItems.add("extra-large");

        ArrayAdapter adapterImageSize = new ArrayAdapter(this, R.layout.item_setting, this.imageSizeItems);
        this.spImageSizes.setAdapter(adapterImageSize);

        this.colorFilterItems = new ArrayList<String>();
        this.colorFilterItems.add("black");
        this.colorFilterItems.add("blue");
        this.colorFilterItems.add("brown");
        this.colorFilterItems.add("gray");
        this.colorFilterItems.add("green");

        ArrayAdapter adapterColorFilter = new ArrayAdapter(this, R.layout.item_setting, this.colorFilterItems);
        this.spColorFilter.setAdapter(adapterColorFilter);

        this.imageTypeItems = new ArrayList<String>();
        this.imageTypeItems.add("faces");
        this.imageTypeItems.add("photo");
        this.imageTypeItems.add("clip art");
        this.imageTypeItems.add("line art");

        ArrayAdapter adapterImageType = new ArrayAdapter(this, R.layout.item_setting, this.imageTypeItems);
        spImageTypes.setAdapter(adapterImageType);

        // Restore the preference to the views
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        String prefImageSize = pref.getString("imageSize", this.imageSizeItems.get(0));
        this.spImageSizes.setSelection(this.imageSizeItems.indexOf(prefImageSize));

        String prefColorFilter = pref.getString("colorFilter", this.colorFilterItems.get(0));
        this.spColorFilter.setSelection(this.colorFilterItems.indexOf(prefColorFilter));

        String prefImageType = pref.getString("imageType", this.imageTypeItems.get(0));
        this.spImageTypes.setSelection(this.imageTypeItems.indexOf(prefImageType));

        this.etSite.setText(pref.getString("site", ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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


}
