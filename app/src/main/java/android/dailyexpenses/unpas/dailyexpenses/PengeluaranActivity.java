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


public class PengeluaranActivity extends ActionBarActivity implements OnClickListener{
    SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
    TableLayout tabelPengeluaran;
    Button buttonTambahPengeluaran;
    TextView total;
    int totalPengeluaran =0;
    ArrayList<Button> buttonEdit=new ArrayList<>();
    ArrayList<Button> buttonDelete=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran);
        tabelPengeluaran = (TableLayout) findViewById(R.id.tabelPengeluaran);
        buttonTambahPengeluaran = (Button) findViewById(R.id.btnTambahPengeluaran);
        total = (TextView) findViewById(R.id.angkaTotal);
        buttonTambahPengeluaran.setOnClickListener(this);

        TableRow barisTabel = new TableRow(this);

//        tes tambah
//        sqLiteHelper.tambahPemasukan("jajan",2000, "beli baso");
//        sqLiteHelper.tambahPemasukan("kuliah",4000, "bayar praktikum");

        //tampil database
        ArrayList<HashMap<String,String>> arrayListPengeluaran = sqLiteHelper.tampilSemuaPengeluaran();
        if (arrayListPengeluaran.size() > 0){
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

            tabelPengeluaran.addView(barisTabel, new TableLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            for (int i =0; i < arrayListPengeluaran.size();i++){

                HashMap<String,String> hashMapPengeluaran = arrayListPengeluaran.get(i);

                String id_pengeluaran = hashMapPengeluaran.get("id_pengeluaran");
                String nama_pengeluaran = hashMapPengeluaran.get("nama_pengeluaran");
                String jumlah_pengeluaran = hashMapPengeluaran.get("jumlah_pengeluaran");
                int jumlahInt = Integer.parseInt(hashMapPengeluaran.get("jumlah_pengeluaran"));
                String deskripsi_pengeluaran = hashMapPengeluaran.get("deskripsi_pengeluaran");

                barisTabel = new TableRow(this);
                if (i % 2==0){
                    barisTabel.setBackgroundColor(Color.LTGRAY);
                }

                TextView viewId = new TextView(this);
                viewId.setText(id_pengeluaran);
                viewId.setPadding(5, 1, 5, 1);
                barisTabel.addView(viewId);

                TextView viewNama = new TextView(this);
                viewNama.setText(nama_pengeluaran);
                viewNama.setPadding(5, 1, 5, 1);
                barisTabel.addView(viewNama);

                TextView viewJumlah = new TextView(this);
                viewJumlah.setText("Rp."+jumlah_pengeluaran);
                viewJumlah.setPadding(5, 1, 5, 1);
                barisTabel.addView(viewJumlah);

                TextView viewDesk = new TextView(this);
                viewDesk.setText(deskripsi_pengeluaran);
                viewDesk.setPadding(5, 1, 5, 1);
                barisTabel.addView(viewDesk);

                buttonEdit.add(i, new Button(this));
                buttonEdit.get(i).setId(Integer.parseInt(id_pengeluaran));
                buttonEdit.get(i).setTag("Ubah");
                buttonEdit.get(i).setText("Ubah");
                buttonEdit.get(i).setOnClickListener(this);
                barisTabel.addView(buttonEdit.get(i));

                buttonDelete.add(i, new Button(this));
                buttonDelete.get(i).setId(Integer.parseInt(id_pengeluaran));
                buttonDelete.get(i).setTag("Hapus");
                buttonDelete.get(i).setText("Hapus");
                buttonDelete.get(i).setOnClickListener(this);
                barisTabel.addView(buttonDelete.get(i));

                totalPengeluaran = totalPengeluaran + jumlahInt;
                tabelPengeluaran.addView(barisTabel, new TableLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            }
        }
        String totalString = String.valueOf(totalPengeluaran);
        total.setText("Rp."+totalString);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnTambahPengeluaran){
            tambahPengeluaran();
        }else{
            for (int i = 0; i < buttonEdit.size(); i++){
                if (v.getId() == buttonEdit.get(i).getId() && v.getTag().toString().trim().equals("Ubah")){
                    int id = buttonEdit.get(i).getId();
                    getDataById(id);

                }else if (v.getId() == buttonDelete.get(i).getId()
                        && v.getTag().toString().trim().equals("Hapus")){
                    int id = buttonDelete.get(i).getId();
                    deletePengeluaran(id);
                }
            }
        }

    }

    public void deletePengeluaran(final int id){
        AlertDialog.Builder builderDelete = new AlertDialog.Builder(this);
        builderDelete.setTitle("Hapus Pengeluaran ?");
        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);
        builderDelete.setView(layoutInput);
        builderDelete.setPositiveButton("Hapus",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqLiteHelper.hapusPengeluaran(id);
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

        HashMap<String,String> hashMapPengeluaran = sqLiteHelper.tampilPengeluaranBerdasarkanId(id);

        for (int i = 0; i < hashMapPengeluaran.size();i++){
            namaEdit = hashMapPengeluaran.get("nama_pengeluaran");
            jumlahEdit = hashMapPengeluaran.get("jumlah_pengeluaran");
            deskEdit = hashMapPengeluaran.get("deskripsi_pengeluaran");

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

        AlertDialog.Builder builderPengeluaran = new AlertDialog.Builder(this);
        builderPengeluaran.setTitle("Update Pengeluaran");
        builderPengeluaran.setView(layoutInput);
        builderPengeluaran.setPositiveButton("Update",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = Integer.parseInt(viewId.getText().toString());
                        String nama = editNama.getText().toString();
                        int jumlah = Integer.parseInt(editJumlah.getText().toString());
                        String desk = editDesk.getText().toString();
                        sqLiteHelper.updatePengeluaran(id, nama, jumlah, desk);

						/* restart acrtivity */
                        finish();
                        startActivity(getIntent());
                    }

                });

        builderPengeluaran.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builderPengeluaran.show();

    }

    public void tambahPengeluaran(){
        Intent intent= new Intent(getApplication(),TambahPengeluaran.class);
        startActivity(intent);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pengeluaran, menu);
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
