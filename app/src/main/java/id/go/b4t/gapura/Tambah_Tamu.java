package id.go.b4t.gapura;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Tambah_Tamu extends AppCompatActivity {

    //Deklarasi atribut manifest
    public  static final int REQUEST_CODE=100;
    public  static final int PERMISSION_REQUEST=200;

    //deklarasi atribut properti
    Button scanBtn;
    FloatingActionButton Back,Send;
    EditText Card;
    RadioGroup genderRadioGroup;
    RadioGroup nationalityRadioGroup;
    EditText nama;
    EditText instansi;
    EditText nomor;
    EditText kartu;
    Bundle hasil;
    RadioButton genderRadio;
    RadioButton nationalityRadio;
    JSONArray jsonResponse;
    JSONObject obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah__tamu);

        scanBtn=(Button)findViewById(R.id.scanBtn);
        Card=(EditText) findViewById(R.id.Card);
        Back=(FloatingActionButton) findViewById(R.id.Back);
        //Send=(FloatingActionButton) findViewById(R.id.ki);

        Send = (FloatingActionButton) findViewById(R.id.kirim);
        hasil= new Bundle();
        //deklarasi varibel dan properti tampilan
        findAllViewsId();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},PERMISSION_REQUEST);
        }
        scanBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent openCam = new Intent(getApplicationContext(),ScanActivity.class);
                startActivityForResult(openCam,REQUEST_CODE);
            }
        });
    }

    private void findAllViewsId() {
        //button = (Button) findViewById(R.id.kirim);
        nama = (EditText) findViewById(R.id.Nama);
        instansi = (EditText) findViewById(R.id.Instansi);
        kartu = (EditText) findViewById(R.id.Card);
        nomor= (EditText) findViewById(R.id.Nomor_Hp);
        genderRadioGroup= (RadioGroup) findViewById(R.id.jenisKelamin);
        nationalityRadioGroup= (RadioGroup) findViewById(R.id.nationality);
    }

    public void klik_kirim (View v){

        hasil.putString("nama", nama.getText().toString());
        hasil.putString("instansi", instansi.getText().toString());
        hasil.putString("kartu", kartu.getText().toString());
        hasil.putString("nomor", nomor.getText().toString());
        hasil.putString("id", "4");
        //int id;
        //id=genderRadioGroup.getCheckedRadioButtonId();
        genderRadio = (RadioButton) findViewById(genderRadioGroup.getCheckedRadioButtonId());
        //id=nationalityRadioGroup.getCheckedRadioButtonId();
        nationalityRadio = (RadioButton) findViewById(nationalityRadioGroup.getCheckedRadioButtonId());

        hasil.putString("JenisKelamin", genderRadio.getText().toString());
        hasil.putString("Wargaan", nationalityRadio.getText().toString());

        Log.d("instance 1", String.valueOf(hasil));
        // Intent Kotak = new Intent(this,Hasil.class);
        //Kotak.putExtras(hasil);
        //startActivity(Kotak);
        if (nama.getText().toString().length()==0&&nomor.getText().toString().length()==0&&instansi.getText().toString().length()==0&&kartu.getText().toString().length()==0){
            nama.setError("pastikan tidak ada yang salah");
            nomor.setError("pastikan password benar");
            instansi.setError("pastikan password benar");
            kartu.setError("pastikan password benar");
            Toast.makeText(this, "Data Masih Ada Kosong",Toast.LENGTH_LONG).show();
        }else {
            showDialog();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ){
        if (requestCode==REQUEST_CODE && resultCode==RESULT_OK){
            if (data!=null){
                final Barcode barcode= data.getParcelableExtra("barcode");
                Card.post(new Runnable() {
                    @Override
                    public void run() {
                        Card.setText(barcode.displayValue);
                    }
                });
            }
        }
    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Apakah Data Anda sudah Benar?");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Tekan Ya jika sudah yakin!")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        tambah();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }


    private void tambah() {
        String url1 = "http://gapura.b4t.go.id/index.php/api/tambah?nama="+nama.getText().toString()+"&id=4&label="+kartu.getText().toString()+"&noHp="+nomor.getText().toString()+"&gender="+genderRadio.getText().toString()+"&wn="+nationalityRadio.getText().toString()+"&instansi="+instansi.getText().toString();
        String url=url1.replace(" ","+");
        Log.d("hasil url",url);
        //String request adalah milik dari Volley library bertujuan untuk meyimpan perintah request
        //jadi String ini adalah string yang berisi syntax
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, response -> {

            try {
                obj = new JSONObject(response);
                //for (int i = 0; i < jsonResponse.length(); i++) {
                //obj = jsonResponse.getJSONObject(i);

                //  String status = obj.getString("status");
                // String message = obj.getString("message");


                // String img = obj.getString("url");
                Log.d("tambah", "tambah: " + obj.getString("status") + " pesan : " + obj.getString("message"));
                //  Toast.makeText(this,status+""+message,Toast.LENGTH_LONG).show();
                //asuk kedalam tagList
                //tagList.add(new DataTamuModel(id, nama));
                //}
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.d("DEBUG", "error: " + error)) {
            //Jika gagal maka coba pindah ke dalam mode MAP / pemetaan pdengan string parameter
            protected Map<String, String> getParams() {
                //String username = "001";
                //String password = "001";
                Map<String, String> map = new HashMap<String, String>();
                //masukkan nilai ke dalam varibel id
                map.put("name", hasil.getString("nama"));
                map.put("id", hasil.getString("nama"));
                map.put("label", hasil.getString("kartu"));
                map.put("noHp", hasil.getString("nomor"));
                map.put("gender", hasil.getString("JenisKelamin"));
                map.put("instansi", hasil.getString("wargaan"));
                Log.d("hasil", String.valueOf(map));
                //return null;
                return null;
            }
        };

        //String jsonObj perintah dikirim
        NetworkRequest.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }

    public void klikButtonBalik(View v) {
        Intent dashboard = new Intent(this, TabelTamu.class);
        startActivity(dashboard);
        this.finish();
    }
}
