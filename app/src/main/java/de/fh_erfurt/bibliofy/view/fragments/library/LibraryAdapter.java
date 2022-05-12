package de.fh_erfurt.bibliofy.view.fragments.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

import de.fh_erfurt.bibliofy.R;
import de.fh_erfurt.bibliofy.model.Author;
import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.relations.BookWithAuthor;

/**
 * Library Adapter
 * for Recycler View
 */

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.BookViewHolder> {

    private final LayoutInflater inflater;
    private final LibraryAdapter.BookClickListener bookClickListener;
    private List<BookWithAuthor> bookList;

    public LibraryAdapter(Context context, LibraryAdapter.BookClickListener bookClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.bookClickListener = bookClickListener;
    }

    @NonNull
    @Override
    public LibraryAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.inflater.inflate(R.layout.list_item_library, parent, false);

        return new LibraryAdapter.BookViewHolder(itemView, this.bookClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryAdapter.BookViewHolder holder, int position) {
        if (this.bookList != null && !this.bookList.isEmpty()) {
            BookWithAuthor current = this.bookList.get(position);

            Book book = current.book;
            List<Author> authors = current.authors;

            holder.currentBookId = current.book.getBookId();
            holder.bookName.setText(book.getTitle());

            String names = authors.stream().map(a -> a.getFirstName() + " " + a.getLastName()).collect(Collectors.joining(", "));
            holder.authorFullName.setText(names);

            Picasso p = Picasso.get();

            p.load(book.getCoverUrl())
                    .placeholder(R.drawable.ic_add)
                    .error(R.drawable.ic_search)
                    .into(holder.bookImage);
        } else {
            // Covers the case of data not being ready yet.
            holder.bookName.setText(R.string.text_empty);
        }
    }

    public void setBookWithAuthors(List<BookWithAuthor> bookList) {
        this.bookList = bookList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.bookList != null && !this.bookList.isEmpty())
            return this.bookList.size();
        else
            return 0;
    }

    public interface BookClickListener {
        void onClick(long bookId);
    }

    // View Holder definition
    static class BookViewHolder extends RecyclerView.ViewHolder {
        private final ImageView bookImage;
        private final TextView bookName;
        private final TextView authorFullName;

        private long currentBookId = -1;

        private BookViewHolder(View itemView, LibraryAdapter.BookClickListener bookClickListener) {
            super(itemView);
            this.bookImage = itemView.findViewById(R.id.list_item_library_image);
            this.bookName = itemView.findViewById(R.id.list_item_library_name);
            this.authorFullName = itemView.findViewById(R.id.list_item_library_author_name);

            itemView.setOnClickListener(v -> {
                bookClickListener.onClick(this.currentBookId);
            });

        }
    }


}