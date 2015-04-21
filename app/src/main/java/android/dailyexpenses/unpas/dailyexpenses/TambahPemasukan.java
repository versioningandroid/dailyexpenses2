package android.dailyexpenses.unpas.dailyexpenses;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;


public class TambahPemasukan extends ActionBarActivity {
    SQLiteHelper db = new SQLiteHelper(this);
    EditText namaEdit,jumlahEdit,deksEdit;
    // Spinner element
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pemasukan);
        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
//        db.tambahSpinnerPemasukan("Kuliah");
//        db.tambahSpinnerPemasukan("Jajan");
        // Loading spinner data from database
        loadSpinnerData();

    }

    private void loadSpinnerData() {
        // database handler
        SQLiteHelper db = new SQLiteHelper(getApplicationContext());

        // Spinner Drop down elements
        List<String> pemasukan_spinner = db.getAllSpinnerPemasukan();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, pemasukan_spinner);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    public void edit(String id,String nama,String jumlah,String desk){
        namaEdit.setText(nama);
        jumlahEdit.setText(jumlah);
        deksEdit.setText(desk);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText){
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX()+w.getLeft()-scrcoords[0];
            float y = event.getRawY()+w.getTop()-scrcoords[1];

            if (event.getAction()==MotionEvent.ACTION_UP && (x<w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())){
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(),0);

            }
        }
        return ret;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tambah_pemasukan, menu);
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

    public void addNewPemasukan(View view){

//        namaEdit = (EditText) findViewById(R.id.namaInput);
//        String nama = namaEdit.getText().toString();

        spinner = (Spinner) findViewById(R.id.spinner);
        String nama = spinner.getSelectedItem().toString();

        jumlahEdit = (EditText) findViewById(R.id.jumlahInput);
        int jumlah = Integer.parseInt(jumlahEdit.getText().toString());

        deksEdit = (EditText) findViewById(R.id.deskInput);
        String desk = deksEdit.getText().toString();

        db.tambahPemasukan(nama,jumlah,desk);
        this.callHomePage(view);
    }

    public void closeNewPemasukan(View view){
        this.callHomePage(view);
    }

    public void callHomePage(View view){
        Intent intent = new Intent(getApplicationContext(),PemasukanActivity.class);
        startActivity(intent);
    }

}
