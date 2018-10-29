package com.example.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private List<Note> mNoteList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View noteView;
        TextView noteName;

        public ViewHolder(View view){
            super(view);
            noteView=view;
            noteName=(TextView)view.findViewById(R.id.note_name);
        }
    }

    public NoteAdapter(List<Note> noteList){
        mNoteList=noteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.noteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Note note=mNoteList.get(position);
                Intent intent=new Intent(view.getContext(), NoteActivity.class);
                intent.putExtra("fileName",note.getName());
                view.getContext().startActivity(intent);
            }
        });
        holder.noteView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage("是否删除？")
                        .setCancelable(false)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int position=holder.getAdapterPosition();
                                Note note=mNoteList.get(position);
                                File f=new File(view.getContext().getFilesDir()+"/"+note.getName());
                                f.delete();
                                mNoteList.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", null).show();
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Note note=mNoteList.get(position);
        holder.noteName.setText(note.getName());
    }

    @Override
    public int getItemCount(){
        return mNoteList.size();
    }
}
