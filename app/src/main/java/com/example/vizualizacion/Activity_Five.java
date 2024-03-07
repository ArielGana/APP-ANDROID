package com.example.vizualizacion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Five extends AppCompatActivity {




    RequestQueue requestQueue;
    private static final String URL1 = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/TraerCompletados.php";


    String id, Rut;
    String idorder;


    double totalPreciof = 0.0; // Variable para almacenar el total de precios
    double totalPrecio = 0.0;
    List<View> orderViews = new ArrayList<>();
    List<Double> preciosTotales = new ArrayList<>();
    private boolean isExpanded = false;
    private FloatingActionButton fabMain;
    private FloatingActionButton fabButton1;
    private FloatingActionButton fabButton2;
    private FloatingActionButton fabButton3;
    private FloatingActionButton fabButton4;
    private TextView txv1,txv2,txv3,txv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);


        fabMain = findViewById(R.id.fab_main);
        fabButton1 = findViewById(R.id.fab_button1);
        fabButton2 = findViewById(R.id.fab_button2);
        fabButton3 = findViewById(R.id.fab_button3);
        fabButton4 = findViewById(R.id.fab_button4);
        txv1 = findViewById(R.id.text1);
        txv2 = findViewById(R.id.txvCant);
        txv3 = findViewById(R.id.txvDin);
        txv4 = findViewById(R.id.txvFecha);


        fabButton1.setVisibility(View.INVISIBLE);
        fabButton2.setVisibility(View.INVISIBLE);
        fabButton3.setVisibility(View.INVISIBLE);
        fabButton4.setVisibility(View.INVISIBLE);
        txv1.setVisibility(View.INVISIBLE);
        txv2.setVisibility(View.INVISIBLE);
        txv3.setVisibility(View.INVISIBLE);
        txv4.setVisibility(View.INVISIBLE);


        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    collapseFabButtons();
                } else {
                    expandFabButtons();
                }
                isExpanded = !isExpanded;
            }
        });
        fabButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón 1

                Intent i = new Intent(Activity_Five.this, Activity_Second.class);
                startActivity(i);


            }
        });

        fabButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón 2
                Intent i = new Intent(Activity_Five.this, Activity_Third.class);
                startActivity(i);

            }
        });
        fabButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón 2

                Intent i = new Intent(Activity_Five.this, Activity_Fourth.class);
                startActivity(i);


            }
        });
        fabButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón 2

                Intent i = new Intent(Activity_Five.this, Activity_Five.class);
                startActivity(i);


            }
        });


        HorizontalScrollView horizontalScrollView = findViewById(R.id.horizontalScrollView);
        requestQueue = Volley.newRequestQueue(this);

        LinearLayout dataContainer = findViewById(R.id.dataContainer); // Cambia "dataContainer" por el ID real de tu contenedor en el layout XML

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                URL1,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject orderObject = response.getJSONObject(i);

                                // Extrae los datos de cada objeto de la respuesta
                                final String idorder = orderObject.getString("id");
                                String precio = orderObject.getString("Precio");
                                String fecha = orderObject.getString("Fecha");
                                String cantidad = orderObject.getString("Cantidad");
                                String precio_final = orderObject.getString("PrecioFinal");
                                String nombreuser = orderObject.getString("nombre");
                                String apellidouser = orderObject.getString("apellido");
                                String nombrePan = orderObject.getString("nombrepan");
                                final String token = orderObject.getString("token");


                                // Crea una vista de fila
                                LinearLayout rowLayout = new LinearLayout(Activity_Five.this);
                                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                                LinearLayout.LayoutParams rowLayoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                rowLayoutParams.setMargins(0, 0, 0, 0); // Espacio inferior entre filas
                                rowLayout.setLayoutParams(rowLayoutParams);


                                // Crea los TextViews para cada campo de datos
                                TextView PrecioTextView = createTextView("Precio " + "\n" + precio, 0, 0, 20, 0);

                                TextView fechaTextView = createTextView("Fecha " + "\n" + fecha, 0, 0, 20, 0);

                                TextView cantidadTextView = createTextView("Cantidad " + "\n" + cantidad, 0, 0, 20, 0);

                                TextView precio_finalTextView = createTextView("PrecioFinal " + "\n" + precio_final, 0, 0, 20, 0);

                                TextView nombreuserTextView = createTextView("nombre " + "\n" + nombreuser, 0, 0, 20, 0);

                                TextView apellidouserTextView = createTextView("apellido " + "\n" + apellidouser, 0, 0, 20, 0);

                                TextView nombrePanTextView = createTextView("nombrepan " + "\n" + nombrePan, 0, 0, 20, 70);

                                // ... Repite el proceso para los demás TextViews ...


                                // Crea el botón
                                Button eliminarButton = new Button(Activity_Five.this);
                                eliminarButton.setText("Listo");
                                eliminarButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Crear un cuadro de diálogo
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Five.this);
                                        builder.setTitle("Completar");
                                        builder.setMessage("Estás seguro que deseas dar la orden por completada?");

                                        // Agregar un botón al cuadro de diálogo
                                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dataContainer.removeView(rowLayout);
                                                dataContainer.requestLayout();
                                                String idOrdenEliminar = idorder;
                                                ListoFuncion(idOrdenEliminar);
                                                Enviar(nombreuser,apellidouser,fecha,token,nombrePan,cantidad);
                                                Log.d("TAG id order", idOrdenEliminar);
                                            }
                                        });

                                        // Opcionalmente, agregar un botón de "Cancelar"
                                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Acciones a realizar cuando se hace clic en el botón "Cancelar"
                                            }
                                        });

                                        // Mostrar el cuadro de diálogo
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }

                                });

                                // Agrega los TextViews y el botón a la vista de fila
                                rowLayout.addView(PrecioTextView);
                                rowLayout.addView(fechaTextView);
                                rowLayout.addView(cantidadTextView);
                                rowLayout.addView(precio_finalTextView);
                                rowLayout.addView(nombreuserTextView);
                                rowLayout.addView(apellidouserTextView);
                                rowLayout.addView(nombrePanTextView);
                                rowLayout.addView(eliminarButton);

                                // Agrega la vista de fila al contenedor
                                dataContainer.addView(rowLayout);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar error de la solicitud
                    }
                });

        requestQueue.add(request);
    }
    private void expandFabButtons() {
        // Muestra los botones flotantes adicionales
        fabButton1.show();
        fabButton2.show();
        fabButton3.show();
        fabButton4.show();
        txv1.setVisibility(View.VISIBLE);
        txv2.setVisibility(View.VISIBLE);
        txv3.setVisibility(View.VISIBLE);
        txv4.setVisibility(View.VISIBLE);
        // Muestra más botones flotantes según sea necesario
    }

    private void collapseFabButtons() {
        // Oculta los botones flotantes adicionales
        fabButton1.hide();
        fabButton2.hide();
        fabButton3.hide();
        fabButton4.hide();
        txv1.setVisibility(View.INVISIBLE);
        txv2.setVisibility(View.INVISIBLE);
        txv3.setVisibility(View.INVISIBLE);
        txv4.setVisibility(View.INVISIBLE);


        // Oculta más botones flotantes según sea necesario
    }

    private TextView createTextView(String text, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        TextView textView = new TextView(Activity_Five.this);
        textView.setText(text);
        textView.setTextSize(20);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        textView.setLayoutParams(layoutParams);
        return textView;
    }


    public void ListoFuncion(String idorder){
        String urlEliminar = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/Completarorden.php";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                urlEliminar,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String >params= new HashMap<>();
                params.put("id",idorder);

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void Enviar(String Nombre , String Apellido ,String Fecha ,String tokenid,String Pan,String Cantidad){


        if(!tokenid.isEmpty()){
            String title="Gana-Baker";
            String message="Hola "+Nombre+" "+Apellido+" Tu "+ Pan +" (Cantidad " +Cantidad+ ") Encargado El "+ Fecha+" se Encuentra Listo";

            FCMsend.pushNotification(
                    Activity_Five.this,tokenid,title,message);

        }else {
            System.out.println("no tiene token");
        }


    }
}