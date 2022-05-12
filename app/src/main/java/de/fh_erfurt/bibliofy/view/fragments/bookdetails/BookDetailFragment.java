package de.fh_erfurt.bibliofy.view.fragments.bookdetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

import de.fh_erfurt.bibliofy.R;
import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.relations.BookWithAuthor;
import de.fh_erfurt.bibliofy.model.relations.BookWithGenre;
import de.fh_erfurt.bibliofy.view.fragments.core.BaseFragment;

/**
 * book detail fragment class that displays all relevant information of a single book
 * after the user clicks on one book entry in the home fragment list;
 * also has options to edit/delete the current book
 */

public class BookDetailFragment extends BaseFragment implements View.OnClickListener {

    public static final String ARG_BOOK_ID = "bookId";
    ImageButton add_to_read_books;
    ImageButton add_to_wishlist;
    ImageButton add_to_favorite_books;
    Button button_delete_book;
    Button button_edit_book;
    private View root;
    private BookDetailViewModel viewModel;
    private LiveData<Book> bookLiveData;
    private Book book;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Create Layout/Views
        root = inflater.inflate(R.layout.fragment_book_detail, container, false);

        // Get View Model
        viewModel = this.getViewModel(BookDetailViewModel.class);

        this.hideBackButton();

        assert getArguments() != null;
        final long bookId = getArguments().getLong(ARG_BOOK_ID);
        this.bookLiveData = viewModel.getBook(bookId);
        this.book = viewModel.getOneBook((bookId));
        this.bookLiveData.observe(requireActivity(), this::updateView);

        /**
         * click listener to redirect the user to the UpdateFragment
         * to edit the current book's data
         */

        // BookClickListener to Update Fragment
        button_edit_book = root.findViewById(R.id.button_edit_book);
        button_edit_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putLong("bookId", bookId);
                Toast.makeText(getActivity(), "Buch bearbeiten?", Toast.LENGTH_LONG).show();
                Navigation.findNavController(root).navigate(R.id.action_navigation_book_detail_to_updateFragment, args);
            }
        });

        /**
         * click listener to delete the current book from the database
         * and afterwards redirect the user to the Home View
         */

        button_delete_book = root.findViewById(R.id.button_delete_book);
        button_delete_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Buch löschen?", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Buch löschen?");
                builder.setMessage("Möchtest du das Buch wirklich aus deiner Liste entfernen?");
                builder.setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.deleteBook(book);
                        Toast.makeText(getActivity(), "Buch gelöscht", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(root).navigate(R.id.action_navigation_book_detail_to_ic_home);
                    }
                });
                builder.setNegativeButton("Zurück", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        add_to_read_books = root.findViewById(R.id.add_to_read_books);
        add_to_wishlist = root.findViewById(R.id.add_to_wishlist);
        add_to_favorite_books = root.findViewById(R.id.add_to_favorite_books);

        add_to_read_books.setOnClickListener(this);
        add_to_wishlist.setOnClickListener(this);
        add_to_favorite_books.setOnClickListener(this);

        return root;
    }

    /**
     * to get the book's information, display it on screen
     * and create an observer that updates the UI
     *
     * also displays an icon in an orange color, if the book belongs
     * to one of the categories (read, wished, favourite)
     * @param book
     */

    private void updateView(Book book) {

        boolean isAvailable = true;
        if (book == null) isAvailable = false;
        if (isAvailable) {
            Log.i("EventCallbacks", "Update Detail View with Book: " + book);

            assert getView() != null;
            // book info
            TextView bookTitleView = getView().findViewById(R.id.fragment_book_details_title);
            ImageView bookImageView = getView().findViewById(R.id.fragment_book_details_image);
            TextView bookYearsOfPublicationView = getView().findViewById(R.id.fragment_book_details_bookYearsOfPublication);
            TextView bookPublisherView = getView().findViewById(R.id.fragment_book_details_publisher);
            TextView bookNumberOfPagesView = getView().findViewById(R.id.fragment_book_details_numberOfPages);
            TextView bookDescriptionView = getView().findViewById(R.id.fragment_book_description);
            TextView bookIsbnView = getView().findViewById(R.id.fragment_book_details_isbn);

            // Author info
            TextView authorNameView = getView().findViewById(R.id.fragment_book_details_authorName);
            TextView authorSalutationTypeView = getView().findViewById(R.id.fragment_book_details_authorSalutationType);

            // Genre info
            TextView genreView = getView().findViewById(R.id.fragment_book_details_genre);

            // set book Information
            bookTitleView.setText(book.getTitle());
            bookNumberOfPagesView.setText(book.getNumberOfPages());
            bookPublisherView.setText(book.getPublisher());
            bookYearsOfPublicationView.setText(book.getYearsOfPublication());
            bookDescriptionView.setText(book.getDescription());
            bookIsbnView.setText(book.getIsbn());
            Picasso p = Picasso.get();
            p.load(book.getCoverUrl())
                    .placeholder(R.drawable.ic_add)
                    .error(R.drawable.ic_search)
                    .resize(250, 250)
                    .into(bookImageView);

            // Create the observer which updates the UI.
            final Observer<List<BookWithAuthor>> bookAuthorsObserver = bookWithAuthors -> {
                if (bookWithAuthors.size() == 0) return;
                String names = bookWithAuthors.get(0).authors.stream().map(a -> a.getFirstName() + " " + a.getLastName()).collect(Collectors.joining(", "));
                authorNameView.setText(names);
                String salutationType = bookWithAuthors.get(0).authors.stream().map(a -> a.getSalutationType().toString()).collect(Collectors.joining(", "));
                authorSalutationTypeView.setText(salutationType);
            };
            viewModel.getBooksWithAuthors(book.getBookId()).observe(requireActivity(), bookAuthorsObserver);

            // Create the observer which updates the UI.
            final Observer<List<BookWithGenre>> bookGenresObserver = bookWithGenres -> {
                if (bookWithGenres.size() == 0) return;
                String genreType = bookWithGenres.get(0).genres.stream().map(a -> a.getDescription().toString()).collect(Collectors.joining(", "));
                genreView.setText(genreType);
            };
            viewModel.getBooksWithGenres(book.getBookId()).observe(requireActivity(), bookGenresObserver);

            if (book.isHadRead()) {
                add_to_read_books.setColorFilter(Color.rgb(239, 108, 0));
            } else {
                add_to_read_books.setColorFilter(Color.rgb(196, 196, 196));
            }
            if (book.isWish()) {
                add_to_wishlist.setColorFilter(Color.rgb(239, 108, 0));
            } else {
                add_to_wishlist.setColorFilter(Color.rgb(196, 196, 196));
            }
            if (book.isFavourite()) {
                add_to_favorite_books.setColorFilter(Color.rgb(239, 108, 0));
            } else {
                add_to_favorite_books.setColorFilter(Color.rgb(196, 196, 196));
            }
        }
    }

    /**
     * to remove observer when user leaves fragment
     */

    @Override
    public void onPause() {
        super.onPause();
        Log.i("test", "test321");
        this.bookLiveData.removeObservers(requireActivity());
    }

    /**
     * to remove observer when user closes app
     */

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.bookLiveData.removeObservers(requireActivity());
    }

    /**
     * click listener to add the current book to one of the categories (read, wished, favourite),
     * whenever the user clicks on the respective icon-button and remove it again, if the
     * user clicks on it once more
     * @param v
     */

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_to_read_books) {
            if (book.isHadRead()) {
                book.setHadRead(false);
                Toast.makeText(getActivity(), "von gelesenen Büchern entfernt", Toast.LENGTH_SHORT).show();
                add_to_read_books.setColorFilter(Color.rgb(196, 196, 196));
            } else {
                book.setHadRead(true);
                Toast.makeText(getActivity(), "zu gelesenen Büchern hinzugefügt", Toast.LENGTH_SHORT).show();
                add_to_read_books.setColorFilter(Color.rgb(239, 108, 0));
            }
            viewModel.updateBook(book);
        } else if (v.getId() == R.id.add_to_wishlist) {
            if (book.isWish()) {
                book.setWish(false);
                Toast.makeText(getActivity(), "von der Wunschliste entfernt", Toast.LENGTH_SHORT).show();
                add_to_wishlist.setColorFilter(Color.rgb(196, 196, 196));
            } else {
                book.setWish(true);
                Toast.makeText(getActivity(), "zur Wunschliste hinzugefügt", Toast.LENGTH_SHORT).show();
                add_to_wishlist.setColorFilter(Color.rgb(239, 108, 0));
            }
            viewModel.updateBook(book);
        } else if (v.getId() == R.id.add_to_favorite_books) {
            if (book.isFavourite()) {
                book.setFavourite(false);
                Toast.makeText(getActivity(), "von Favoriten entfernt", Toast.LENGTH_SHORT).show();
                add_to_favorite_books.setColorFilter(Color.rgb(196, 196, 196));
            } else {
                book.setFavourite(true);
                Toast.makeText(getActivity(), "zu Favoriten hinzugefügt", Toast.LENGTH_SHORT).show();
                add_to_favorite_books.setColorFilter(Color.rgb(239, 108, 0));
            }
            viewModel.updateBook(book);
        }
    }
}