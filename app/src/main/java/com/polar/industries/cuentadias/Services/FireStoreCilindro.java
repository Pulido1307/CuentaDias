package com.polar.industries.cuentadias.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.polar.industries.cuentadias.Interfaces.ListaCilindros;
import com.polar.industries.cuentadias.MainActivity;
import com.polar.industries.cuentadias.Modelos.Cilindro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreCilindro {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference CilindroCollection = db.collection("cilindro");
    private Cilindro cilindro = null;

    public void registrarCilindro(final ProgressDialog progressDialog, final String fecha_inicio, final String precio, final String size_cilindro, final Context context){
        final Map<String, Object> data = new HashMap<>();
        data.put("fecha_inicio", fecha_inicio);
        data.put("precio", precio);
        data.put("size_cilindro", size_cilindro);
        data.put("dias_duracion", "");
        data.put("fecha_fin", "");

       CilindroCollection.document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();

                if(task.isSuccessful()){
                    new AlertDialogPersonalized().alertDialogInformacion("Se registro con éxito", context);
                }else {
                    new AlertDialogPersonalized().alertDialogInformacion("No se pudo registrar la información", context);
                }
            }
        });
    }


    public void readCilindros(final ListaCilindros listaCilindros, ProgressDialog dialog){
        final List<Cilindro> list_cilindros = new ArrayList<>();
        CilindroCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        final Map<String,Object> asesorado_add =  document.getData();
                        dialog.dismiss();
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("dd/MM/yyyy").parse(asesorado_add.get("fecha_inicio").toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        cilindro = new Cilindro(document.getId().toString(), document.get("dias_duracion").toString(), document.get("fecha_fin").toString(),
                               date, document.get("precio").toString(), document.get("size_cilindro").toString());
                        list_cilindros.add(cilindro);
                    }
                    listaCilindros.getCilindros(list_cilindros);
                }
            }
        });
    }

    public void updateData(final ProgressDialog progressDialog, final Context context, final String id, final String dias_duracion, final String fecha_fin){
        final Map<String, Object> data = new HashMap<>();
        data.put("dias_duracion", dias_duracion);
        data.put("fecha_fin", fecha_fin);

        CilindroCollection.document(id).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                //new AlertDialogPersonalized().alertDialogInformacion("Se actualizaron los datos con éxito", context);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new AlertDialogPersonalized().alertDialogInformacion("Error al actualizar los datos", context);
            }
        });
    }
}
