package com.ielts.lakshacademy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ielts.lakshacademy.ApiHelper.WebUrl;
import com.ielts.lakshacademy.Model.BookData;
import com.ielts.lakshacademy.R;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    Context context;
    ArrayList<BookData> arrayList;

    public BookAdapter(Context context, ArrayList<BookData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_list,parent,false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        holder.bookName.setText(arrayList.get(position).getBookName());
        holder.bookTime.setText(arrayList.get(position).getCreatedDate());

        String pdf_url = WebUrl.BOOK_PATH + arrayList.get(position).getBookUrl();

        holder.openBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(pdf_url));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        TextView bookName,bookTime,openBook;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            bookName = itemView.findViewById(R.id.bookName);
            bookTime = itemView.findViewById(R.id.bookTime);
            openBook = itemView.findViewById(R.id.openBook);

        }
    }
}
