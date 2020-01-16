package com.example.gallery;

/**
 * Created by hasee-pc on 2017/07/07.
 */

import java.util.List;
import com.example.gallery.AsyncImageLoader.ImageCallback;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hasee_pc.myapplication.R;

public class ImageAndTextListAdapter extends ArrayAdapter<GrilViewActivity.ImageAndText> {

    private GridView gridView;
    private AsyncImageLoader asyncImageLoader;
    public ImageAndTextListAdapter(Activity activity, List<GrilViewActivity.ImageAndText> imageAndTexts, GridView gridView1) {
        super(activity, 0, imageAndTexts);
        this.gridView = gridView1;
        asyncImageLoader = new AsyncImageLoader();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity) getContext();

        // Inflate the views from XML
        View rowView = convertView;
        ViewCache viewCache;
        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.griditem, null);
            viewCache = new ViewCache(rowView);
            rowView.setTag(viewCache);
        } else {
            viewCache = (ViewCache) rowView.getTag();
        }
        GrilViewActivity.ImageAndText imageAndText = getItem(position);

        // Load the image and set it on the ImageView
        String imageUrl = imageAndText.getImageUrl();
        ImageView imageView = viewCache.getImageView();
        imageView.setTag(imageUrl);
        Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl, new ImageCallback() {
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                ImageView imageViewByTag = (ImageView) gridView.findViewWithTag(imageUrl);
                if (imageViewByTag != null) {
                    imageViewByTag.setImageDrawable(imageDrawable);
                }
            }
        });
        if (cachedImage == null) {
            imageView.setImageResource(R.mipmap.ic_launcher);//原来是R.drawable.icon
        }else{
            imageView.setImageDrawable(cachedImage);
        }
        // Set the text on the TextView
        TextView textView = viewCache.getTextView();
        textView.setText(imageAndText.getText());
        return rowView;
    }
    public class ViewCache {

        private View baseView;
        private TextView textView;
        private ImageView imageView;

        public ViewCache(View baseView) {
            this.baseView = baseView;
        }

        public TextView getTextView() {
            if (textView == null) {
                textView = (TextView) baseView.findViewById(R.id.text);
            }
            return textView;
        }

        public ImageView getImageView() {
            if (imageView == null) {
                imageView = (ImageView) baseView.findViewById(R.id.image);
            }
            return imageView;
        }

    }

}