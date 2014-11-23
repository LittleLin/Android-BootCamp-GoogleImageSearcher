package info.littlelin.imagesearcher.views.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import info.littlelin.imagesearcher.R;
import info.littlelin.imagesearcher.models.ImageResult;

public class ImageResultAdapter extends ArrayAdapter<ImageResult> {
    public ImageResultAdapter(Context context, List<ImageResult> images) {
        super(context, android.R.layout.simple_list_item_1, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ImageResult imageInfo = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }
        // Lookup view for data population
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        // Clear out image from last time
        ivImage.setImageResource(0);

        // Populate the data into the template view using the data object
        Picasso.with(convertView.getContext()).load(imageInfo.thumbUrl).placeholder(R.drawable.placeholder).into(ivImage);
        tvTitle.setText(Html.fromHtml(imageInfo.title));

        // Return the completed view to render on screen
        return convertView;
    }
}
