package com.polar.industries.cuentadias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.polar.industries.cuentadias.Interfaces.ListaCilindros;
import com.polar.industries.cuentadias.Modelos.Cilindro;
import com.polar.industries.cuentadias.Services.FireStoreCilindro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListaCilindros {
    private ListView listView_informacion;
    private FloatingActionButton floatingActionButton_add;
    private TextView textView_sin_data;
    private static final String[] tam_cilindros = {"20kg", "30kg"};
    private FireStoreCilindro fireStoreCilindro = new FireStoreCilindro();
    private String ERROR_GLOBAL = "Campo requerido";
    private ArrayList<String> lista_informacion = new ArrayList<>();
    private ArrayList<Cilindro> arrayList_cilindros = new ArrayList<>();
    private ArrayAdapter arrayAdapterListView;
    private SimpleDateFormat dateFormat;
    private CoordinatorLayout coordinatorLayout_main;

    @Override
    protected void onStart() {
        super.onStart();
        ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "", "Cargando...", true);
        fireStoreCilindro.readCilindros(MainActivity.this, progressDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView_informacion = findViewById(R.id.listView_informacion);
        floatingActionButton_add = findViewById(R.id.floatingActionButton_add);
        textView_sin_data = findViewById(R.id.textView_sin_data);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        coordinatorLayout_main = findViewById(R.id.coordinatorLayout_main);
        floatingActionButton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd();
            }
        });

        arrayAdapterListView = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_informacion);
        listView_informacion.setAdapter(arrayAdapterListView);

        listView_informacion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                showDialogRead(arrayList_cilindros.get(position));
            }
        });

        manejoTemaOscuro();

    }

    private void showDialogRead(Cilindro cilindro){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_read, null);
        builder.setView(view);

        final AlertDialog dialogRead = builder.create();
        dialogRead.setCancelable(false);
        dialogRead.show();

        final TextInputEditText textInputLayout_fecha_inicio_read = dialogRead.findViewById(R.id.textInputLayout_fecha_inicio_read);
        final TextInputLayout textInputLayout_precio_read = dialogRead.findViewById(R.id.textInputLayout_precio_read);
        final TextInputLayout textInputLayout_size_read = dialogRead.findViewById(R.id.textInputLayout_size_read);
        final TextInputEditText textInputLayout_fecha_fin_read = dialogRead.findViewById(R.id.textInputLayout_fecha_fin_read);
        final TextInputLayout textInputLayout_dias_duracion_read = dialogRead.findViewById(R.id.textInputLayout_dias_duracion_read);
        final MaterialButton materialButton_registrar_read = dialogRead.findViewById(R.id.materialButton_registrar_read);
        final MaterialButton materialButton_salir_read = dialogRead.findViewById(R.id.materialButton_salir_read);



        textInputLayout_fecha_inicio_read.setText(dateFormat.format(cilindro.getFecha_inicio()));
        textInputLayout_precio_read.getEditText().setText("$"+cilindro.getPrecio());
        textInputLayout_size_read.getEditText().setText(cilindro.getSize_cilindro());
        textInputLayout_dias_duracion_read.getEditText().setText(""+cilindro.getDias_duracion());
        textInputLayout_fecha_fin_read.setText(""+cilindro.getFecha_fin());

        materialButton_salir_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "", "Cargando...", true);
                fireStoreCilindro.readCilindros(MainActivity.this, progressDialog);
                dialogRead.dismiss();
            }
        });


        textInputLayout_fecha_fin_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog pickerDialogFecha = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yearD, int monthD, int dayD) {
                        int mesActual = monthD + 1;
                        String diaFormateado = (dayD < 10)? "0" + String.valueOf(dayD):String.valueOf(dayD);
                        String mesFormateado = (mesActual < 10)? "0" + String.valueOf(mesActual):String.valueOf(mesActual);
                        textInputLayout_fecha_fin_read.setText(diaFormateado + "/" + mesFormateado + "/" + yearD);
                    }
                }, year, month, day);
                pickerDialogFecha.show();
            }
        });


        materialButton_registrar_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!textInputLayout_fecha_fin_read.getText().toString().isEmpty()){
                    String inicio = textInputLayout_fecha_inicio_read.getText().toString();
                    String fin = textInputLayout_fecha_fin_read.getText().toString();

                    String array_inicio[] = inicio.split("/");
                    String array_fin[] = fin.split("/");

                    Calendar inicio_c = Calendar.getInstance();
                    inicio_c.set(Integer.valueOf(array_inicio[2]), Integer.valueOf(array_inicio[1]) - 1, Integer.valueOf(array_inicio[0]));
                    inicio_c.set(Calendar.HOUR, 0);
                    inicio_c.set(Calendar.HOUR_OF_DAY, 0);
                    inicio_c.set(Calendar.MINUTE, 0);
                    inicio_c.set(Calendar.SECOND, 0);

                    Calendar fin_c = Calendar.getInstance();
                    fin_c.set(Integer.valueOf(array_fin[2]), Integer.valueOf(array_fin[1]) - 1, Integer.valueOf(array_fin[0]));
                    fin_c.set(Calendar.HOUR, 0);
                    fin_c.set(Calendar.HOUR_OF_DAY, 0);
                    fin_c.set(Calendar.MINUTE, 0);
                    fin_c.set(Calendar.SECOND, 0);

                    long finMS = fin_c.getTimeInMillis();
                    long inicioMS = inicio_c.getTimeInMillis();

                    int dias = (int) ((Math.abs(finMS - inicioMS)) / (1000 * 60 * 60 * 24));

                    ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "", "Actualizando...", true);
                    fireStoreCilindro.updateData(progressDialog, MainActivity.this, cilindro.getId(),dias+"", fin);
                    textInputLayout_dias_duracion_read.getEditText().setText(dias+"");

                }else {
                    textInputLayout_fecha_fin_read.setError(ERROR_GLOBAL);
                }
            }
        });

    }

    private void showDialogAdd(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view =inflater.inflate(R.layout.dialog_add_fecha, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        final TextInputEditText textInputLayoutFechaCompra = dialog.findViewById(R.id.textInputLayoutFechaCompra);
        final TextInputLayout textInputLayout_precio = dialog.findViewById(R.id.textInputLayout_precio);
        final TextInputLayout spinner_size = dialog.findViewById(R.id.spinner_size);
        final MaterialButton materialButton_registrar = dialog.findViewById(R.id.materialButton_registrar);
        final MaterialButton materialButton_salir_add = dialog.findViewById(R.id.materialButton_salir_add);

        ArrayAdapter<String> arrayAdapterSize = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tam_cilindros);
        ((AutoCompleteTextView)spinner_size.getEditText()).setAdapter(arrayAdapterSize);

        materialButton_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fecha_inicio = textInputLayoutFechaCompra.getText().toString().trim();
                String precio = textInputLayout_precio.getEditText().getText().toString();
                String size_cilindro = spinner_size.getEditText().getText().toString();

                boolean flag_fecha = false, flag_precio = false, flag_size = false;

                if(!fecha_inicio.isEmpty()){
                    flag_fecha = true;
                }else{
                    flag_fecha = false;
                    textInputLayoutFechaCompra.setError(ERROR_GLOBAL);
                }

                if(!precio.isEmpty()){
                    if(isNumeric(precio)){
                        flag_precio = true;
                    }else{
                        textInputLayout_precio.setError("Necesita ser una monto númerico");
                        flag_precio = false;
                    }
                }else{
                    textInputLayout_precio.setError(ERROR_GLOBAL);
                    flag_precio = false;
                }

                if (!size_cilindro.isEmpty()){
                    flag_size = true;
                }else {
                    flag_size = false;
                    spinner_size.setError(ERROR_GLOBAL);
                }

                if(flag_fecha && flag_precio && flag_size) {
                    ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "", "Agregando...", true);
                    fireStoreCilindro.registrarCilindro(progressDialog, fecha_inicio, precio, size_cilindro, MainActivity.this);
                    ProgressDialog dialogP = ProgressDialog.show(MainActivity.this, "", "Cargando...", true);
                    fireStoreCilindro.readCilindros(MainActivity.this, dialogP);
                    dialog.dismiss();
                }
            }
        });


        textInputLayoutFechaCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog pickerDialogFecha = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yearD, int monthD, int dayD) {
                        int mesActual = monthD + 1;
                        String diaFormateado = (dayD < 10)? "0" + String.valueOf(dayD):String.valueOf(dayD);
                        String mesFormateado = (mesActual < 10)? "0" + String.valueOf(mesActual):String.valueOf(mesActual);
                        textInputLayoutFechaCompra.setText(diaFormateado + "/" + mesFormateado + "/" + yearD);
                    }
                }, year, month, day);
                pickerDialogFecha.show();
            }
        });

        materialButton_salir_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    private boolean isNumeric(String number){
        try {
            Double.valueOf(number);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void getCilindros(List<Cilindro> cilindroList) {
        if(cilindroList.size() != 0){
            arrayList_cilindros = (ArrayList<Cilindro>) cilindroList;
            Collections.sort(this.arrayList_cilindros, new Comparator<Cilindro>() {
                @Override
                public int compare(Cilindro c1, Cilindro c2) {
                    return Long.valueOf(c1.getFecha_inicio().getTime()).compareTo(c2.getFecha_inicio().getTime());
                }
            });

            obtenerLista();
            arrayAdapterListView.notifyDataSetChanged();
        }
    }

    private void obtenerLista() {

        lista_informacion.clear();
        int i = 0;

        if(arrayList_cilindros.size()!=0) {
            for (Cilindro cilindro : arrayList_cilindros) {
                Log.e("I: ", (i++) + "");
                lista_informacion.add("Fecha de inicio:"+dateFormat.format(cilindro.getFecha_inicio()).toString()+"\nPrecio:$"+cilindro.getPrecio()+"\nDias de duración:"+cilindro.getDias_duracion());
            }
            Collections.reverse(arrayList_cilindros);
            Collections.reverse(lista_informacion);
            listView_informacion.setVisibility(View.VISIBLE);
            textView_sin_data.setVisibility(View.GONE);
        }else {
            listView_informacion.setVisibility(View.GONE);
            textView_sin_data.setVisibility(View.VISIBLE);
        }

       //Collections.reverse(lista_informacion);
    }

    private void manejoTemaOscuro(){

        int nightModeFlags = getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {

            case Configuration.UI_MODE_NIGHT_YES:
                coordinatorLayout_main.setBackgroundColor(Color.rgb(48, 48, 48));
                floatingActionButton_add.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(66,66,66)));
                break;

        }
    }

}