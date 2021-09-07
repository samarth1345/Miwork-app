package com.example.android.miwok;

import android.content.Context;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class wordadapter extends ArrayAdapter<word> {

    //to get color resource id for different activities
    private int colorResourceid;

    public wordadapter(@NonNull Context context, int resource, @NonNull List<word> objects,int colorResourceid) {
        super(context, 0, objects);
        this.colorResourceid=colorResourceid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listview=convertView;
        if(listview == null)
        {
            listview = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        word currentword = getItem(position);

        TextView miworktranslation = (TextView) listview.findViewById(R.id.miwork_list_view);
        miworktranslation.setText(currentword.getMiworktranslation());

        TextView defaulttranslation = (TextView) listview.findViewById(R.id.number_list_view);
        defaulttranslation.setText(currentword.getDefaulttranslation());

        ImageView imageView = (ImageView) listview.findViewById(R.id.image);

        if(currentword.hasImage()) {
            imageView.setImageResource(currentword.getResourceid());
        }
        else
        {
            imageView.setVisibility(View.GONE);
        }

        // differnt colors for different activities
        View textContainer = listview.findViewById(R.id.text_container);
        // Find the color that the resource ID maps to
        int color = ContextCompat.getColor(getContext(), colorResourceid);
        //set the background color dynamically for different Activities
        textContainer.setBackgroundColor(color);

        return listview;
    }
}
