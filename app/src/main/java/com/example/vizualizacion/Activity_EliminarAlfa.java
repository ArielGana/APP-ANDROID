package com.example.vizualizacion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_EliminarAlfa extends AppCompatActivity {

    RequestQueue requestQueue;
    private static final String URL1 = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/ListarAlfajores.php";



    TextView badge;

    String Rut;

    int ID,cantidadPendientes ;
    List<View> orderViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_alfa);



        requestQueue = Volley.newRequestQueue(this);

        ScrollView scrollView = findViewById(R.id.scrollView2); // Obtener la referencia del ScrollView
        LinearLayout linearContainer = new LinearLayout(Activity_EliminarAlfa.this);

        linearContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        linearContainer.setOrientation(LinearLayout.VERTICAL); // Asegurar orientación vertical
        Typeface typeface = ResourcesCompat.getFont(Activity_EliminarAlfa.this, R.font.description);
        TextView precioFinalTextView = new TextView(Activity_EliminarAlfa.this);

        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                URL1,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            // Crea un contenedor adicional para los orderLayout
                            LinearLayout orderContainer = new LinearLayout(Activity_EliminarAlfa.this);
                            orderContainer.setOrientation(LinearLayout.VERTICAL);


                            for (int i = 0; i < response.length(); i++) {
                                JSONObject orderObject = response.getJSONObject(i);

                                final String Pan_id = orderObject.getString("id");
                                String nombre = orderObject.getString("nombre");
                                String precio = orderObject.getString("Precio");
                                String imagen = orderObject.getString("imagen");
                                String Cantidad = orderObject.getString("Cantidad");

                                LinearLayout textButtonLayout = new LinearLayout(Activity_EliminarAlfa.this);
                                textButtonLayout.setOrientation(LinearLayout.VERTICAL);
                                textButtonLayout.setGravity(Gravity.CENTER_HORIZONTAL);


                                LinearLayout orderLayout = new LinearLayout(Activity_EliminarAlfa.this);
                                orderLayout.setOrientation(LinearLayout.HORIZONTAL);
                                orderLayout.setPadding(0, 0, 0, 16); // Espacio inferior entre cada orden

                                // Crea una vista ImageView para mostrar la foto
                                ImageView imageView = new ImageView(Activity_EliminarAlfa.this);
                                // Aquí puedes establecer la imagen según el ID de la orden
                                TraerImg(imagen,imageView);

                                // Ajusta el tamaño de la imagen a 120x120 píxeles
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(320, 320);
                                layoutParams.setMargins(0, 0, 0, 0); // Margen izquierdo de 50 píxeles
                                imageView.setLayoutParams(layoutParams);

                                // Crea un espacio entre la imagen y el TextView
                                Space space1 = new Space(Activity_EliminarAlfa.this);
                                LinearLayout.LayoutParams spaceParams1 = new LinearLayout.LayoutParams(
                                        20, // Ancho de 50 píxeles
                                        LinearLayout.LayoutParams.WRAP_CONTENT // Altura ajustable según el contenido
                                );
                                space1.setLayoutParams(spaceParams1);

                                // Crea  boton Añadir
                                Button VerButton = new Button(Activity_EliminarAlfa.this);
                                VerButton.setText("Eliminar");
                                VerButton.setBackgroundColor(Color.parseColor("#1C3562"));
                                VerButton.setTextColor(Color.WHITE);
                                VerButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_EliminarAlfa.this);
                                        builder.setTitle("Confirmar eliminación");
                                        builder.setMessage("¿Estás seguro de que quieres eliminar este Alfajor?");
                                        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {


                                                orderLayout.setVisibility(View.GONE);

                                                orderContainer.requestLayout();
                                                eliminarPan(Pan_id);

                                                dialog.dismiss();
                                            }
                                        });
                                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.create().show();
                                    }
                                });


                                VerButton.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                ));


                                Space space3 = new Space(Activity_EliminarAlfa.this);
                                LinearLayout.LayoutParams spaceParams3 = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, // Ancho ajustado al contenedor
                                        16 // Altura de 16 píxeles, ajusta según sea necesario
                                );
                                space3.setLayoutParams(spaceParams3);

                                // Crea un espacio entre el TextView y el botón Eliminar
                                Space space2 = new Space(Activity_EliminarAlfa.this);
                                LinearLayout.LayoutParams spaceParams2 = new LinearLayout.LayoutParams(
                                        80, // Ancho de 80 píxeles
                                        LinearLayout.LayoutParams.WRAP_CONTENT // Altura ajustable según el contenido
                                );
                                space2.setLayoutParams(spaceParams2);


                                // Crea una vista TextView para mostrar los datos de la orden
                                Typeface typeface = ResourcesCompat.getFont(Activity_EliminarAlfa.this, R.font.description);
                                TextView textView = new TextView(Activity_EliminarAlfa.this);
                                textView.setText(nombre+"\n"+"Cantidad: "+Cantidad+"\n"+"Precio: "+precio);
                                textView.setTextColor(Color.BLACK);
                                textView.setTypeface(typeface);
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


                                // Agrega los elementos al LinearLayout de la orden
                                orderLayout.addView(imageView);
                                orderLayout.addView(space1);
                                orderLayout.addView(textView);
                                orderLayout.addView(space2);
                                orderLayout.addView(VerButton);
                                orderLayout.addView(space3);


                                // Agrega el LinearLayout de la orden al contenedor adicional
                                orderViews.add(orderLayout);
                                orderContainer.addView(orderLayout);
                            }

                            // Agrega el contenedor adicional al linearContainer
                            linearContainer.addView(orderContainer);

                            // Agrega el TextView para mostrar el precio final

                            priceParams.setMargins(0, 16, 0, 0); // Margen superior de 16 píxeles, ajusta según sea necesario
                            priceParams.gravity = Gravity.CENTER_HORIZONTAL; // Centra el TextView horizontalmente
                            precioFinalTextView.setLayoutParams(priceParams);
                            linearContainer.addView(precioFinalTextView);



                            // Cambia el color de fondo del LinearLayout

                            scrollView.addView(linearContainer); // Agrega el contenedor al ScrollView

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        requestQueue.add(request);

    }

    public void TraerImg(String direccion ,ImageView imageView){

        String url=direccion;

        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Aquí puedes hacer algo con la imagen, como mostrarla en un ImageView

                        imageView.setImageBitmap(response);

                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores
                    }
                });
        requestQueue.add(imageRequest);
    }

    public void eliminarPan(String id) {
        String url = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/EliminarAlfajor.php";

        // Crear una solicitud POST utilizando Volley
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta del servidor
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar los errores de la solicitud
                        Toast.makeText(getApplicationContext(), "Error al eliminar el pan", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Parámetros POST que se enviarán al archivo PHP
                Map<String, String> params = new HashMap<>();
                params.put("id", id); // Convertir el ID a String
                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(request);
    }


    public void AñadirAlfajor(View a){

        Intent i = new Intent(Activity_EliminarAlfa.this, Activity_AnadirAlfa.class);
        startActivity(i);

    }

}