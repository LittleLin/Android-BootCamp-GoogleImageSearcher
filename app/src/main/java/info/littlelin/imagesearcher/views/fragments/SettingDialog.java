package info.littlelin.imagesearcher.views.fragments;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import info.littlelin.imagesearcher.R;

public class SettingDialog extends DialogFragment {
    private Spinner spImageSizes;
    private Spinner spColorFilter;
    private Spinner spImageTypes;
    private EditText etSite;
    private Button btnSearch;

    private List<String> imageSizeItems;
    private List<String> colorFilterItems;
    private List<String> imageTypeItems;

    public SettingDialog() {
        // Empty constructor required for DialogFragment
    }

    public static SettingDialog newInstance(String title) {
        SettingDialog frag = new SettingDialog();
        Bundle args = new Bundle();
        args.putString("title", title);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, true);
        String title = getArguments().getString("title", "Comments");
        getDialog().setTitle(title);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        this.setupViews();
        super.onActivityCreated(savedInstanceState);
    }

    private void setupViews() {
        this.spImageSizes = (Spinner) this.getView().findViewById(R.id.spImageSizes);
        this.spColorFilter = (Spinner) this.getView().findViewById(R.id.spColorFilter);
        this.spImageTypes = (Spinner) this.getView().findViewById(R.id.spImageTypes);
        this.etSite = (EditText) this.getView().findViewById(R.id.etSite);
        this.btnSearch = (Button) this.getView().findViewById(R.id.btnSave);

        // Setup the values of spinners
        this.imageSizeItems = new ArrayList<String>();
        this.imageSizeItems.add("small");
        this.imageSizeItems.add("medium");
        this.imageSizeItems.add("large");
        this.imageSizeItems.add("extra-large");

        ArrayAdapter adapterImageSize = new ArrayAdapter(this.getActivity(), R.layout.item_setting, this.imageSizeItems);
        this.spImageSizes.setAdapter(adapterImageSize);

        this.colorFilterItems = new ArrayList<String>();
        this.colorFilterItems.add("black");
        this.colorFilterItems.add("blue");
        this.colorFilterItems.add("brown");
        this.colorFilterItems.add("gray");
        this.colorFilterItems.add("green");

        ArrayAdapter adapterColorFilter = new ArrayAdapter(this.getActivity(), R.layout.item_setting, this.colorFilterItems);
        this.spColorFilter.setAdapter(adapterColorFilter);

        this.imageTypeItems = new ArrayList<String>();
        this.imageTypeItems.add("faces");
        this.imageTypeItems.add("photo");
        this.imageTypeItems.add("clip art");
        this.imageTypeItems.add("line art");

        ArrayAdapter adapterImageType = new ArrayAdapter(this.getActivity(), R.layout.item_setting, this.imageTypeItems);
        spImageTypes.setAdapter(adapterImageType);

        // Restore the preference to the views
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        String prefImageSize = pref.getString("imageSize", this.imageSizeItems.get(0));
        this.spImageSizes.setSelection(this.imageSizeItems.indexOf(prefImageSize));

        String prefColorFilter = pref.getString("colorFilter", this.colorFilterItems.get(0));
        this.spColorFilter.setSelection(this.colorFilterItems.indexOf(prefColorFilter));

        String prefImageType = pref.getString("imageType", this.imageTypeItems.get(0));
        this.spImageTypes.setSelection(this.imageTypeItems.indexOf(prefImageType));

        this.etSite.setText(pref.getString("site", ""));

        this.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveSetting(v);
            }
        });
    }

    public void onSaveSetting(View view) {
        // Save the settings
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("imageSize", this.spImageSizes.getSelectedItem().toString());
        edit.putString("colorFilter", this.spColorFilter.getSelectedItem().toString());
        edit.putString("imageType", this.spImageTypes.getSelectedItem().toString());
        edit.putString("site", this.etSite.getText().toString());
        edit.commit();

        // Finish current dialog
        this.dismiss();
    }
}
