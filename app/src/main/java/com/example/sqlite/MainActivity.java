package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnThem;
    EditText editTen;
    Database database;
    ListView lvCongViec;
    ArrayList<CongViec> arrayCongViec;
    CongViecAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnThem=(Button) findViewById(R.id.buttonThem);
        editTen=(EditText) findViewById(R.id.editTextTenCV);
        lvCongViec=(ListView) findViewById(R.id.listviewCongViec);
        arrayCongViec=new ArrayList<>();
        adapter=new CongViecAdapter(this,R.layout.dong_cong_viec,arrayCongViec);
        lvCongViec.setAdapter(adapter);

        //tạo database ghichu
        database=new Database(this,"ghichu.sqlite",null,1);

        //tạo bản CongViec
        database.QueryData("CREATE TABLE IF NOT EXISTS CongViec(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenCV VARCHAR(200))");

        //insert data
    //    database.QueryData("INSERT INTO CongViec VALUES(null,'Viết Ứng dụng ghi chú')");
        //select data
        GetDataCongViec();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tencv=editTen.getText().toString();
                if(tencv=="")
                    Toast.makeText(MainActivity.this, "Vui Lòng Nhập Tên Công Việc", Toast.LENGTH_SHORT).show();
                else
                {
                    database.QueryData("INSERT INTO CongViec VALUES(null,'"+ tencv+"')");
                    Toast.makeText(MainActivity.this, "Đã Thêm", Toast.LENGTH_SHORT).show();
                    GetDataCongViec();
                }
            }
        });
    }
    public void DialogXoaCV(final String tencv,final int id)
    {
        AlertDialog.Builder dialogXoa=new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xóa công việc "+ tencv+" không ?");
        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.QueryData("DELETE FROM CongViec WHERE Id= '"+id+"'");
                Toast.makeText(MainActivity.this, "Đã Xóa "+tencv, Toast.LENGTH_SHORT).show();
                GetDataCongViec();
            }
        });
        dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogXoa.show();
    }

    public void DialogSuaCongViec(String ten, final int id){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialod_sua);
        final EditText edtTenCV=(EditText) dialog.findViewById(R.id.editTextTenCVEdit) ;
        Button btnXacNhan=(Button) dialog.findViewById(R.id.buttonXacNhan);
        Button btnHuy=(Button) dialog.findViewById(R.id.buttonHuyEdit);
        edtTenCV.setText(ten);
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenMoi=edtTenCV.getText().toString().trim();
                database.QueryData("UPDATE CongViec SET TenCV='"+tenMoi+"' WHERE ID= '"+id+"'");
                Toast.makeText(MainActivity.this, "Đã Cập Nhật", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                GetDataCongViec();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private  void GetDataCongViec(){
        Cursor dataCongViec= database.GetData("SELECT * FROM CongViec");
        arrayCongViec.clear();
        while (dataCongViec.moveToNext()){
            String ten=dataCongViec.getString(1);
            int id=dataCongViec.getInt(0);
            arrayCongViec.add(new CongViec(id,ten));
        }
        adapter.notifyDataSetChanged();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.add_congviec,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
}