package com.example.vizualizacion;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageGrid extends BaseAdapter{


    private Context mContext;
    private int[] mImageIds; // Aquí puedes usar un array de recursos de imágenes o una lista de URI de imágenes seleccionadas

    public ImageGrid(Context context, int[] imageIds) {
        mContext = context;
        mImageIds = imageIds;
    }

    @Override
    public int getCount() {
        return mImageIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // Si no se reutiliza una vista existente, se crea una nueva
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250)); // Tamaño de las imágenes en el GridView
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mImageIds[position]); // Establece la imagen en el ImageView

        return imageView;
    }



}
