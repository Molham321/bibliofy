package de.fh_erfurt.bibliofy.view.fragments.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.fh_erfurt.bibliofy.R;
import de.fh_erfurt.bibliofy.model.Author;
import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.relations.BookWithAuthor;

/**
 * Home Adapter
 * for Recycler View
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.BookViewHolder> implements Filterable {

    private final LayoutInflater inflater;
    private final BookClickListener bookClickListener;
    private List<BookWithAuthor> bookList;
    private List<BookWithAuthor> bookListFull;
    private Filter bookListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<BookWithAuthor> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(bookListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (BookWithAuthor item : bookListFull) {
                    if (item.book.getTitle().toLowerCase().contains(filterPattern)
                            || item.authors.get(0).getFirstName().toLowerCase().contains(filterPattern)
                            || item.authors.get(0).getLastName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            bookList.clear();
            bookList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public HomeAdapter(Context context, BookClickListener bookClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.bookClickListener = bookClickListener;
        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.inflater.inflate(R.layout.list_item_book, parent, false);

        return new BookViewHolder(itemView, this.bookClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
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
            holder.bookName.setText(R.string.text_empty);
        }
    }

    public void setBookWithAuthors(List<BookWithAuthor> bookList) {
        this.bookList = bookList;
        bookListFull = new ArrayList<>(bookList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.bookList != null && !this.bookList.isEmpty())
            return this.bookList.size();
        else
            return 0;
    }

    @Override
    public Filter getFilter() {
        return bookListFilter;
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

        private BookViewHolder(View itemView, BookClickListener bookClickListener) {
            super(itemView);
            this.bookImage = itemView.findViewById(R.id.list_item_book_image);
            this.bookName = itemView.findViewById(R.id.list_item_book_name);
            this.authorFullName = itemView.findViewById(R.id.list_item_author_name);

            itemView.setOnClickListener(v -> {
                bookClickListener.onClick(this.currentBookId);
            });
        }
    }
}

