package com.example.vizualizacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.util.IOUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Activity_AnadirPan extends AppCompatActivity {
    RequestQueue requestQueue;
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;

    private Button buttonSelectImage;
    private ImageView imageView;

    EditText edt1,edt2,edt3,edt4;
    Uri selectedImageUri;

    String Nombre,Descripcion,Cantidad;
double Precio;
    private static final String SERVER_URL = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/GuardarImagen.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_pan);

        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        imageView = findViewById(R.id.imageView2);
        edt1 = findViewById(R.id.edtNombre);
        edt2 = findViewById(R.id.edtDesc);
        edt3 = findViewById(R.id.edtPrecio);
        edt4 = findViewById(R.id.edtCant);


        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
            }
        });
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);

        }
    }

    private void sendImageToServer(Uri imageUri) {
        // Crear una cola de solicitudes de Volley
        RequestQueue queue = Volley.newRequestQueue(this);

        // Crear una solicitud HTTP POST multipart/form-data
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, SERVER_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        // Manejar la respuesta del servidor
                        String result = new String(response.data);
                        Toast.makeText(Activity_AnadirPan.this, "Imagen enviada correctamente", Toast.LENGTH_SHORT).show();
                        Log.d("imagen", result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el fallo de la solicitud
                        Toast.makeText(Activity_AnadirPan.this, "Error al enviar la imagen", Toast.LENGTH_SHORT).show();
                        Log.d("imagen error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    byte[] fileData = IOUtils.toByteArray(inputStream);
                    String imageName = getFileNameFromUri(selectedImageUri);
                    params.put("image", new VolleyMultipartRequest.DataPart(imageName, fileData, "image/jpeg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley
        queue.add(multipartRequest);
    }






    private void insertarPanEnServidor(final String name, final String descriptiom, final Double price, final Uri imagenUri, final String canti) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/AgregarPan.php"; // Reemplaza con la URL de tu archivo PHP

        // Crear una solicitud de cadena de solicitud POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Manejar la respuesta del servidor
                Toast.makeText(Activity_AnadirPan.this, "Pan insertado correctamente", Toast.LENGTH_SHORT).show();
                Log.d("error asasd", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el fallo de la solicitud
                Toast.makeText(Activity_AnadirPan.this, "Error al insertar el pan", Toast.LENGTH_SHORT).show();
                Log.d("error asasd", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imageName = getFileNameFromUri(selectedImageUri);
                // Parámetros de la solicitud POST
                Map<String, String> params = new HashMap<>();
                params.put("nombre", name);
                params.put("descripcion", descriptiom);
                params.put("precio", String.valueOf(price));
                params.put("cantidad", canti);
                params.put("img", "https://slate-gray-cargoes.000webhostapp.com/Panes/"+imageName);
                return params;
            }
        };

        queue.add(stringRequest);
    }
    public void AñadirPan(View v){

        Nombre = edt1.getText().toString();
        Descripcion = edt2.getText().toString();
        Precio = Double.valueOf(edt3.getText().toString());
        Cantidad = edt4.getText().toString();

        String imageName = getFileNameFromUri(selectedImageUri);
        Log.d("imagen nombre", imageName);
        sendImageToServer(selectedImageUri);
        insertarPanEnServidor(Nombre,Descripcion,Precio,selectedImageUri,Cantidad);
    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (nameIndex != -1) {
                fileName = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return fileName;
    }

}