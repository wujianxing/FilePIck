package com.example.mylibrary.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylibrary.R;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

/**
 * Created by wujianxing on 16/12/26.
 * wujianxing
 * 490187140@qq.com
 * 酱紫好么？
 */
public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.DirectoryViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view,int position);
    }
    public class DirectoryViewHolder extends RecyclerView.ViewHolder{
        private ImageView mFileImage;
        private TextView mFileTitle;
        private TextView mFileSubtitle;

        public DirectoryViewHolder(View itemView,final OnItemClickListener clickListener){
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v,getAdapterPosition());
                }
            });

            mFileImage=(ImageView)itemView.findViewById(R.id.item_file_image);
            mFileTitle = (TextView) itemView.findViewById(R.id.item_file_title);
            mFileSubtitle = (TextView) itemView.findViewById(R.id.item_file_subtitle);
        }

        public DirectoryViewHolder(View itemView) {
            super(itemView);
        }
    }
    private List<File> mFiles;
    private Context mContext;
    private OnItemClickListener mOnITemClickListener;

    public DirectoryAdapter(Context context,List<File>files){
        mContext =context;
        mFiles = files;
    }
    public void setmOnITemClickListener(OnItemClickListener listener){
        mOnITemClickListener =listener;
    }
    @Override
    public DirectoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file,parent,false);

        return new DirectoryViewHolder(view,mOnITemClickListener);
    }

    @Override
    public void onBindViewHolder(DirectoryViewHolder holder, int position) {
        File currentFile = mFiles.get(position);

        FileTypeUtils.FileType fileType = FileTypeUtils.getFileType(currentFile);
        holder.mFileImage.setImageResource(fileType.getIcon());
        holder.mFileSubtitle.setText(fileType.getDescription());
        holder.mFileTitle.setText(currentFile.getName());

    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }
    public File getModel(int index) {
        return mFiles.get(index);
    }

}
