package android.dailyexpenses.unpas.dailyexpenses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.HashMap;


public class PemasukanActivity extends ActionBarActivity implements OnClickListener{
    SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
    TableLayout tabelPemasukan;
    Button buttonTambahPemasukan;
    TextView total;
    int totalPemasukan=0;
    ArrayList<Button> buttonEdit=new ArrayList<>();
    ArrayList<Button> buttonDelete=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemasukan2);

        tabelPemasukan = (TableLayout) findViewById(R.id.tabelPemasukan);
        buttonTambahPemasukan  = (Button) findViewById(R.id.btnTambahPemasukan);
        total = (TextView) findViewById(R.id.angkaTotal);
        buttonTambahPemasukan.setOnClickListener(this);

        TableRow barisTabel = new TableRow(this);

//        tes tambah
//        sqLiteHelper.tambahPemasukan("jajan",2000, "beli baso");
//        sqLiteHelper.tambahPemasukan("kuliah",4000, "bayar praktikum");

        //tampil database
        ArrayList<HashMap<String,String>> arrayListPemasukan = sqLiteHelper.tampilSemuaPemasukan();
        if (arrayListPemasukan.size() > 0){
            TextView viewHeaderId = new TextView(this);
            TextView viewHeaderNama = new TextView(this);
            TextView viewHeaderJumlah = new TextView(this);
            TextView viewHeaderDeskripsi = new TextView(this);
            TextView viewHeaderAction = new TextView(this);

            viewHeaderId.setText("ID");
            viewHeaderNama.setText("Nama");
            viewHeaderJumlah.setText("Jumlah");
            viewHeaderDeskripsi.setText("Deskripsi");
            viewHeaderAction.setText("Action");

            viewHeaderId.setPadding(5, 1, 5, 1);
            viewHeaderNama.setPadding(5, 1, 5, 1);
            viewHeaderJumlah.setPadding(5,1,5,1);
            viewHeaderDeskripsi.setPadding(5, 1, 5, 1);
            viewHeaderAction.setPadding(5, 1, 5, 1);

            barisTabel.addView(viewHeaderId);
            barisTabel.addView(viewHeaderNama);
            barisTabel.addView(viewHeaderJumlah);
            barisTabel.addView(viewHeaderDeskripsi);
            barisTabel.addView(viewHeaderAction);

            tabelPemasukan.addView(barisTabel, new TableLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            for (int i =0; i < arrayListPemasukan.size();i++){

                HashMap<String,String> hashMapPemasukan = arrayListPemasukan.get(i);

                String id_pemasukan = hashMapPemasukan.get("id_pemasukan");
                String nama_pemasukan = hashMapPemasukan.get("nama_pemasukan");
                String jumlah_pemasukan = hashMapPemasukan.get("jumlah_pemasukan");
                int jumlahInt = Integer.parseInt(hashMapPemasukan.get("jumlah_pemasukan"));
                String deskripsi_pemasukan = hashMapPemasukan.get("deskripsi_pemasukan");

                barisTabel = new TableRow(this);
                if (i % 2==0){
                    barisTabel.setBackgroundColor(Color.LTGRAY);
                }

                TextView viewId = new TextView(this);
                viewId.setText(id_pemasukan);
                viewId.setPadding(5, 1, 5, 1);
                barisTabel.addView(viewId);

                TextView viewNama = new TextView(this);
                viewNama.setText(nama_pemasukan);
                viewNama.setPadding(5, 1, 5, 1);
                barisTabel.addView(viewNama);

                TextView viewJumlah = new TextView(this);
                viewJumlah.setText("Rp."+jumlah_pemasukan);
                viewJumlah.setPadding(5, 1, 5, 1);
                barisTabel.addView(viewJumlah);

                TextView viewDesk = new TextView(this);
                viewDesk.setText(deskripsi_pemasukan);
                viewDesk.setPadding(5, 1, 5, 1);
                barisTabel.addView(viewDesk);

                buttonEdit.add(i, new Button(this));
                buttonEdit.get(i).setId(Integer.parseInt(id_pemasukan));
                buttonEdit.get(i).setTag("Ubah");
                buttonEdit.get(i).setText("Ubah");
                buttonEdit.get(i).setOnClickListener(this);
                barisTabel.addView(buttonEdit.get(i));

                buttonDelete.add(i, new Button(this));
                buttonDelete.get(i).setId(Integer.parseInt(id_pemasukan));
                buttonDelete.get(i).setTag("Hapus");
                buttonDelete.get(i).setText("Hapus");
                buttonDelete.get(i).setOnClickListener(this);
                barisTabel.addView(buttonDelete.get(i));

                totalPemasukan = totalPemasukan + jumlahInt;
                tabelPemasukan.addView(barisTabel, new TableLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            }
        }
        String totalString = String.valueOf(totalPemasukan);
        total.setText("Rp."+totalString);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnTambahPemasukan){
            tambahPemasukan();
        }else{
            for (int i = 0; i < buttonEdit.size(); i++){
                if (v.getId() == buttonEdit.get(i).getId() && v.getTag().toString().trim().equals("Ubah")){
                    int id = buttonEdit.get(i).getId();
                    getDataById(id);

                }else if (v.getId() == buttonDelete.get(i).getId()
                        && v.getTag().toString().trim().equals("Hapus")){
                    int id = buttonDelete.get(i).getId();
                    deletePemasukan(id);
                }
            }
        }

    }

    public void deletePemasukan(final int id){
        AlertDialog.Builder builderDelete = new AlertDialog.Builder(this);
        builderDelete.setTitle("Delete Pemasukan");
        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);
        builderDelete.setView(layoutInput);
        builderDelete.setPositiveButton("Hapus",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqLiteHelper.hapusPemasukan(id);
                        finish();
                        startActivity(getIntent());
                    }

                });

        builderDelete.setNegativeButton("Keluar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builderDelete.show();
    }

    public void getDataById(int id){
        String namaEdit = null;
        String jumlahEdit = null;
        String deskEdit = null;


        HashMap<String,String> hashMapPemasukan = sqLiteHelper.tampilPemasukanBerdasarkanId(id);

        for (int i = 0; i < hashMapPemasukan.size();i++){
            namaEdit = hashMapPemasukan.get("nama_pemasukan");
            jumlahEdit = hashMapPemasukan.get("jumlah_pemasukan");
            deskEdit = hashMapPemasukan.get("deskripsi_pemasukan");

        }



        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);

        final TextView viewId = new TextView(this);
        viewId.setText(String.valueOf(id));
        viewId.setTextColor(Color.TRANSPARENT);
        layoutInput.addView(viewId);

        final EditText editNama = new EditText(this);
        editNama.setText(namaEdit);
        layoutInput.addView(editNama);

        final EditText editJumlah = new EditText(this);
        editJumlah.setText(jumlahEdit);
        layoutInput.addView(editJumlah);

        final EditText editDesk = new EditText(this);
        editDesk.setText(deskEdit);
        layoutInput.addView(editDesk);

        AlertDialog.Builder builderPemasukan = new AlertDialog.Builder(this);
        builderPemasukan.setTitle("Update Pemasukan");
        builderPemasukan.setView(layoutInput);
        builderPemasukan.setPositiveButton("Update",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = Integer.parseInt(viewId.getText().toString());
                        String nama = editNama.getText().toString();
                        int jumlah = Integer.parseInt(editJumlah.getText().toString());
                        String desk = editDesk.getText().toString();
                        sqLiteHelper.updatePemasukan(id, nama, jumlah, desk);

						/* restart acrtivity */
                        finish();
                        startActivity(getIntent());
                    }

                });

        builderPemasukan.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builderPemasukan.show();

    }

    public void tambahPemasukan(){
        Intent intent= new Intent(getApplication(),TambahPemasukan.class);
        startActivity(intent);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pemasukan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
