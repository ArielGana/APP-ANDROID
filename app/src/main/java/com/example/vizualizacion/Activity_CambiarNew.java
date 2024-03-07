package com.example.vizualizacion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineExceptionHandler;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Activity_CambiarNew extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 1;
    private static final int REQUEST_SELECT_IMAGES = 2;
    private List<String> mSelectedImageNames;

    private GridView mGridView;
    private ImageAdapter mImageAdapter;
    private List<String> mSelectedImages;
    private static final String SERVER_URL = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/AgregarNews.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_new);

        mGridView = findViewById(R.id.gridView);
        mSelectedImages = new ArrayList<>();

        mImageAdapter = new ImageAdapter();
        mGridView.setAdapter(mImageAdapter);

        // Verificar y solicitar permiso de acceso a la galería si no está concedido
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }

        mSelectedImageNames = new ArrayList<>();


    }


    public void selectImages(View view) {
        // Abrir la galería para seleccionar imágenes
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, REQUEST_SELECT_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_IMAGES && resultCode == RESULT_OK) {
            if (data != null) {
                // Obtener las imágenes seleccionadas por el usuario
                if (data.getClipData() != null) {
                    // Si se seleccionaron múltiples imágenes
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        String imagePath = getImagePath(imageUri);
                        if (imagePath != null) {
                            mSelectedImages.add(imagePath);
                            mSelectedImageNames.add(getImageName(imageUri));
                        }
                    }
                } else if (data.getData() != null) {
                    // Si se seleccionó una única imagen
                    Uri imageUri = data.getData();
                    String imagePath = getImagePath(imageUri);
                    if (imagePath != null) {
                        mSelectedImages.add(imagePath);
                        mSelectedImageNames.add(getImageName(imageUri));
                    }
                }

                mImageAdapter.notifyDataSetChanged();
                Log.d("Image Names", mSelectedImageNames.toString()); // Mostrar los nombres de las imágenes en el log

            }
        }
    }
    private String getImageName(Uri imageUri) {
        String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(imageUri, projection, null, null, null);
        if (cursor != null) {
            int columnIdx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            cursor.moveToFirst();
            String imageName = cursor.getString(columnIdx);
            cursor.close();
            return imageName;
        }
        return null;
    }

    private String getImagePath(Uri imageUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(imageUri, projection, null, null, null);
        if (cursor != null) {
            int columnIdx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imagePath = cursor.getString(columnIdx);
            cursor.close();
            return imagePath;
        }
        return null;
    }

    private class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mSelectedImages.size();
        }

        @Override
        public Object getItem(int position) {
            return mSelectedImages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(Activity_CambiarNew.this);
                imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            String imagePath = mSelectedImages.get(position);
            // Carga la imagen en el ImageView usando una biblioteca como Picasso o Glide
            // Aquí se muestra un ejemplo básico usando BitmapFactory
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));

            return imageView;
        }
    }

    // Maneja la respuesta de la solicitud de permiso de acceso a la galería
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes realizar la acción deseada
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendImagesToServer(List<String> imagePaths) {
        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        // Agrega los archivos de imagen al cuerpo de la solicitud multipart/form-data
        for (int i = 0; i < imagePaths.size(); i++) {
            String imagePath = imagePaths.get(i);
            File file = new File(imagePath);

            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), file);
            requestBodyBuilder.addFormDataPart("img[]", file.getName(), imageBody);
        }

        RequestBody requestBody = requestBodyBuilder.build();

        Request request = new Request.Builder()
                .url(SERVER_URL)
                .post(requestBody)
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try (Response response = client.newCall(request).execute()) {
                    // Maneja la respuesta del servidor
                    final String result = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Activity_CambiarNew.this, "Imágenes enviadas correctamente", Toast.LENGTH_SHORT).show();
                            Log.d("imagen", result);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Activity_CambiarNew.this, "Error al enviar las imágenes", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Cierra el ExecutorService después de su uso
        executor.shutdown();
    }





    private String getFileNameFromPath(String path) {
        File file = new File(path);
        return file.getName();
    }

    public void Enviar(View a){
        if (mSelectedImages.isEmpty()) {
            Toast.makeText(this, "No se han seleccionado imágenes", Toast.LENGTH_SHORT).show();
        } else {
            sendImagesToServer(mSelectedImages);
        }
    }


    }


