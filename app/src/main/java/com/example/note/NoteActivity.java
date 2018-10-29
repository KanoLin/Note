package com.example.note;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.DialogInterface.*;

public class NoteActivity extends AppCompatActivity {
    private String fileName,original_text;
    private EditText note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_show);
        Intent intent=getIntent();
        fileName=intent.getStringExtra("fileName");
        note=(EditText)findViewById(R.id.note_text);
        String fileInputText=load();
        if (!TextUtils.isEmpty(fileInputText)){
            note.setText(fileInputText);
            note.setSelection(fileInputText.length());
        }
        original_text=note.getText().toString();
    }
    public String load(){
        FileInputStream in=null;
        BufferedReader reader=null;
        StringBuilder content=new StringBuilder();
        try{
            in=openFileInput(fileName);
            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            while ((line=reader.readLine())!=null){
                content.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    public void save(String newName,String inputText,boolean mode)
    {
        if (mode){
            File f=new File(getFilesDir()+"/"+fileName);
            f.renameTo(new File(getFilesDir()+"/"+newName));
            fileName=newName;
        }
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try{
            out=openFileOutput(newName, Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
            original_text=inputText;
            fileName=newName;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if (writer!=null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void alert_save(final View view){
        final EditText et = new EditText(this);
        et.setText(fileName);
        new AlertDialog.Builder(this).setTitle("请输入笔记名")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(et)
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean mode=false;
                        String newName=et.getText().toString();
                        if (newName.equals("")){
                            new AlertDialog.Builder(view.getContext())
                                    .setMessage("文件名不许为空！")
                                    .setCancelable(false)
                                    .setPositiveButton("我知道了", null)
                                    .show();
                            return;
                        }
                        if (newName.equals(""))newName="_NO_SAVE_";
                        if (!newName.equals(fileName)) mode=true;
                        save(newName,note.getText().toString(),mode);
                        Toast.makeText(view.getContext(),"笔记已保存！",Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("取消",null).show();
    }

    @Override
    public void onBackPressed(){
        if (!note.getText().toString().equals(original_text)){
            new AlertDialog.Builder(this).setTitle("尚未保存")
                    .setMessage("确认退出吗？")
                    .setCancelable(false)
                    .setPositiveButton("退出", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NoteActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("取消", null).show();
        }
        else super.onBackPressed();
    }

}
