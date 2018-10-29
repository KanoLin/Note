package com.example.note;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Note> noteList=new ArrayList<>();
    private NoteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNotes();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new NoteAdapter(noteList);
        recyclerView.setAdapter(adapter);
    }

    private void initNotes(){
        File files=new File(MainActivity.this.getFilesDir().toString());
        String[] note_list=files.list();
        noteList.clear();
        for(int i=0;i<note_list.length;++i){
            noteList.add(new Note(note_list[i]));
        }
    }

    public void newnote(View view){
        Intent intent=new Intent(MainActivity.this,NoteActivity.class);
        intent.putExtra("fileName","");
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        initNotes();
        adapter.notifyDataSetChanged();
        super.onResume();
    }
}
