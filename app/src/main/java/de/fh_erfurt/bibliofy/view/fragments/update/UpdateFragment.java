package de.fh_erfurt.bibliofy.view.fragments.update;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.fh_erfurt.bibliofy.R;
import de.fh_erfurt.bibliofy.model.Author;
import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.Bookshelf;
import de.fh_erfurt.bibliofy.model.Genre;
import de.fh_erfurt.bibliofy.model.relations.BookWithAuthor;
import de.fh_erfurt.bibliofy.model.relations.BookWithGenre;
import de.fh_erfurt.bibliofy.view.fragments.add.AddFragment;
import de.fh_erfurt.bibliofy.view.fragments.core.BaseFragment;

public class UpdateFragment extends BaseFragment {

    public static final String ARG_BOOK_ID = "bookId";

    private static final int REQUEST_IMAGE_CAPTURE = 1337;
    DatePickerDialog.OnDateSetListener setListener;
    private String currentPhotoPath;
    private final View.OnClickListener bookCoverClickListener = v -> {

        Context context = getActivity();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(context, "Could not create file for image", Toast.LENGTH_SHORT);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                assert context != null;
                Uri photoURI = FileProvider.getUriForFile(context,
                        "de.fh_erfurt.bibliofy.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    };
    private View root;
    private UpdateViewModel viewModel;
    private TextInputEditText authorFirstName;
    private EditText authorLastName;
    private TextInputLayout authorInputFirstnameLayout, authorInputLastnameLayout;
    private TextInputLayout bookInputTitleLayout, bookInputPublisherLayout, bookInputIsbnLayout;
    private RadioGroup authorRadioGroup;
    private RadioButton authorRadioMr;
    private RadioButton authorRadioMrs;
    private RadioButton authorRadioNOT_SPECIFIED;
    private EditText bookTitle;
    private EditText bookPublisher;
    private EditText bookNumberOfPages;
    private EditText bookIsbn;
    private EditText bookDescription;
    private TextView bookYearsOfPublication;
    private CheckBox checkBoxRed;
    private CheckBox checkBoxWish;
    private CheckBox checkBoxFavourite;
    private ImageView bookCover;
    private RadioGroup genreRadioGroup;
    private RadioButton RadioButtonHumor;
    private RadioButton RadioButtonFiction;
    private RadioButton RadioButtonHorror;
    private RadioButton RadioButtonFantasy;
    private RadioButton RadioButtonDrama;
    private RadioButton RadioButtonFable;
    private Spinner     spinner;
    private Book book;
    private Bookshelf bookshelf;
    private LiveData<Book> bookLiveData;
    private long GenreID;
    private long AuthorID;


    /**
     * in this method will prepare the data before they are updated to the database.
     * A book will be updated in the database when all the required fields are already full with data.
     * @param updateButtonClickListener
     */
    private final View.OnClickListener updateButtonClickListener = v -> {

        if (v.getId() == R.id.update_btnBookSave) {
            if (!validateAuthorFirstname() | !validateAuthorLastname() | !validateBookTitle() | !validateBookPublisher() | !validateBookIsbn()) {
                String returnValue = "Buch konnte nicht aktualisiert werden";

                hideKeyboard(this.requireContext(), v);
                Snackbar.make(v, returnValue, Snackbar.LENGTH_SHORT).show();

            } else {

                //-------------Bookshelf Details ------------------
                Bookshelf newBookshelf = new Bookshelf();
                String bookshelfName = spinner.getSelectedItem().toString();
                newBookshelf.setBookshelfName(bookshelfName);

                //------------- update Bookshelf Details ------------------
                viewModel.updateBookshelf(newBookshelf.getBookshelfName(), book.getBookshelfId());
                //-------------Genre Details ------------------

                Genre newGenre = new Genre();
                int checkedGenreRadioButtonId = this.genreRadioGroup.getCheckedRadioButtonId();
                if (checkedGenreRadioButtonId == R.id.update_genre_input_Type_RadioButton_humor) {
                    newGenre.setDescription(Genre.GenreType.Humor);
                } else if (checkedGenreRadioButtonId == R.id.update_genre_input_Type_RadioButton_fiction) {
                    newGenre.setDescription(Genre.GenreType.Fiction);
                } else if (checkedGenreRadioButtonId == R.id.update_genre_input_Type_RadioButton_fantasy) {
                    newGenre.setDescription(Genre.GenreType.Fantasy);
                } else if (checkedGenreRadioButtonId == R.id.update_genre_input_Type_RadioButton_horror) {
                    newGenre.setDescription(Genre.GenreType.Horror);
                } else if (checkedGenreRadioButtonId == R.id.update_genre_input_Type_RadioButton_drama) {
                    newGenre.setDescription(Genre.GenreType.Drama);
                } else if (checkedGenreRadioButtonId == R.id.update_genre_input_Type_RadioButton_fable) {
                    newGenre.setDescription(Genre.GenreType.Fable);
                }

                //------------- update Bookshelf Details ------------------
                viewModel.updateGenreById(newGenre.getDescription(), GenreID);

                //-------------Book Details ------------------

                Book newBook = new Book(
                        bookIsbn.getText().toString(),
                        bookTitle.getText().toString(),
                        bookPublisher.getText().toString(),
                        bookNumberOfPages.getText().toString(),
                        bookDescription.getText().toString()
                );
                newBook.setYearsOfPublication(bookYearsOfPublication.getText().toString());
                newBook.setCoverUrl(this.currentPhotoPath);
                if (newBook.getCoverUrl() == null || newBook.getCoverUrl().isEmpty()) {
                    newBook.setCoverUrl(book.getCoverUrl());
                }
                newBook.setBookshelfId(book.getBookshelfId());
                if (this.checkBoxRed.isChecked()) {
                    newBook.setHadRead(true);
                }
                if (this.checkBoxWish.isChecked()) {
                    newBook.setWish(true);
                }
                if (this.checkBoxFavourite.isChecked()) {
                    newBook.setFavourite(true);
                }

                //------------- update Book Details ------------------

                viewModel.updateBookById(newBook.getTitle(), newBook.getPublisher(), newBook.getNumberOfPages(), newBook.getIsbn(), newBook.getDescription(), newBook.getYearsOfPublication(), newBook.isHadRead(), newBook.isFavourite(), newBook.isWish(), newBook.getCoverUrl(), book.getBookId());

                //-------------Author Details -----------------

                Author newAuthor = new Author(
                        Objects.requireNonNull(authorFirstName.getText()).toString(),
                        Objects.requireNonNull(authorLastName.getText()).toString()
                );
                int checkedAuthorRadioButtonId = this.authorRadioGroup.getCheckedRadioButtonId();
                if (checkedAuthorRadioButtonId == R.id.update_author_input_Type_group_Mr) {
                    newAuthor.setSalutationType(Author.SalutationType.Herr);
                } else if (checkedAuthorRadioButtonId == R.id.update_author_input_Type_group_Mrs) {
                    newAuthor.setSalutationType(Author.SalutationType.Frau);
                } else {
                    newAuthor.setSalutationType(Author.SalutationType.Divers);
                }
                //------------- update Author Details ------------------
                viewModel.updateAuthorById(newAuthor.getFirstName(), newAuthor.getLastName(), newAuthor.getSalutationType(), AuthorID);

                String returnValue = "Buch erfolgreich aktualisiert";

                hideKeyboard(this.requireContext(), v);
//                Snackbar.make(v, returnValue, Snackbar.LENGTH_SHORT).show();
                final long bookId = getArguments().getLong(ARG_BOOK_ID);
                Bundle args = new Bundle();
                args.putLong("bookId", bookId);
                Navigation.findNavController(root).navigate(R.id.action_updateFragment_to_navigation_book_detail, args);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Create Layout/Views
        root = inflater.inflate(R.layout.fragment_update, container, false);

        // Get View Model
        viewModel = this.getViewModel(UpdateViewModel.class);

        this.hideBackButton();

        assert getArguments() != null;
        final long bookId = getArguments().getLong(ARG_BOOK_ID);

        this.book = viewModel.getOneBook(bookId);
        this.bookshelf = viewModel.getOneBookshelf(book.getBookshelfId());
        this.bookLiveData = viewModel.getBook(bookId);
        this.bookLiveData.observe(requireActivity(), this::getView);

        this.authorFirstName = root.findViewById(R.id.update_et_firstname);
        this.authorInputFirstnameLayout = root.findViewById(R.id.update_author_FirstnameLayout);
        this.authorLastName = root.findViewById(R.id.update_et_lastname);
        this.authorInputLastnameLayout = root.findViewById(R.id.update_author_LastnameLayout);
        this.authorRadioGroup = root.findViewById(R.id.update_author_input_Type_group);
        this.authorRadioMr = root.findViewById(R.id.update_author_input_Type_group_Mr);
        this.authorRadioMrs = root.findViewById(R.id.update_author_input_Type_group_Mrs);
        this.authorRadioNOT_SPECIFIED = root.findViewById(R.id.update_author_input_Type_group_NOT_SPECIFIED);

        this.bookTitle = root.findViewById(R.id.update_et_bookTitle);
        this.bookInputTitleLayout = root.findViewById(R.id.update_book_TitleLayout);
        this.bookPublisher = root.findViewById(R.id.update_et_book_publisher);
        this.bookInputPublisherLayout = root.findViewById(R.id.update_book_PublisherLayout);
        this.bookNumberOfPages = root.findViewById(R.id.update_et_book_number_of_pages);
        this.bookIsbn = root.findViewById(R.id.update_et_book_isbn);
        this.bookInputIsbnLayout = root.findViewById(R.id.update_book_isbnLayout);
        this.bookDescription = root.findViewById(R.id.update_et_book_description);
        this.bookYearsOfPublication = root.findViewById(R.id.update_et_book_yearsOfPublication);

        this.checkBoxRed = root.findViewById(R.id.update_book_input_Type_CheckBox_hasRead);
        this.checkBoxWish = root.findViewById(R.id.update_book_input_Type_CheckBox_wish);
        this.checkBoxFavourite = root.findViewById(R.id.update_book_input_Type_CheckBox_favourite);
        this.bookCover = root.findViewById(R.id.update_btnAddImage);
        this.bookCover.setOnClickListener(this.bookCoverClickListener);

        this.genreRadioGroup = root.findViewById(R.id.update_genre_input_Type_group);
        this.RadioButtonHumor = root.findViewById(R.id.update_genre_input_Type_RadioButton_humor);
        this.RadioButtonHorror = root.findViewById(R.id.update_genre_input_Type_RadioButton_horror);
        this.RadioButtonDrama = root.findViewById(R.id.update_genre_input_Type_RadioButton_drama);
        this.RadioButtonFable = root.findViewById(R.id.update_genre_input_Type_RadioButton_fable);
        this.RadioButtonFantasy = root.findViewById(R.id.update_genre_input_Type_RadioButton_fantasy);
        this.RadioButtonFiction = root.findViewById(R.id.update_genre_input_Type_RadioButton_fiction);
        this.spinner = root.findViewById(R.id.update_bookshelfSpinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(UpdateFragment.super.getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.bookshelves));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);
        String bookshelfValue = bookshelf.getBookshelfName();
        spinner.setSelection(getIndex(spinner,bookshelfValue));


        bookTitle.setText(book.getTitle());
        bookPublisher.setText(book.getPublisher());
        bookNumberOfPages.setText(book.getNumberOfPages());
        bookIsbn.setText(book.getIsbn());
        bookDescription.setText(book.getDescription());
        bookYearsOfPublication.setText(book.getYearsOfPublication());
        checkBoxRed.setChecked(book.isHadRead());
        checkBoxWish.setChecked(book.isWish());
        checkBoxFavourite.setChecked(book.isFavourite());
        Picasso p = Picasso.get();
        p.load(book.getCoverUrl())
                .placeholder(R.drawable.ic_add)
                .error(R.drawable.ic_search)
                .resize(250, 250)
                .into(bookCover);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        bookYearsOfPublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateFragment.super.getContext()
                        , android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day
                );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                bookYearsOfPublication.setText(date);
            }
        };
        Button updateBtn = root.findViewById(R.id.update_btnBookSave);
        updateBtn.setOnClickListener(this.updateButtonClickListener);

        return root;
    }

    private int getIndex(Spinner spinner, String bookshelfValue) {
        for (int i = 0; i<spinner.getCount();i++)
        {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(bookshelfValue))
            {
                return i;
            }
        }

        return 0;
    }


    private void getView(Book book) {
        boolean isAvailable = true;
        if (book == null) isAvailable = false;
        if (isAvailable) {
            Log.i("EventCallbacks", "Update Detail View with Book: " + book);

            assert getView() != null;

            // Create the observer which updates the UI.
            final Observer<List<BookWithAuthor>> bookAuthorsObserver = bookWithAuthors -> {
                if (bookWithAuthors.size() == 0) return;
                String firstname = bookWithAuthors.get(0).authors.stream().map(a -> a.getFirstName()).collect(Collectors.joining(", "));
                authorFirstName.setText(firstname);

                String lastname = bookWithAuthors.get(0).authors.stream().map(a -> a.getLastName()).collect(Collectors.joining(", "));
                authorLastName.setText(lastname);

                Author.SalutationType salutationType = bookWithAuthors.get(0).authors.get(0).getSalutationType();
                if (salutationType == Author.SalutationType.Herr) {
                    authorRadioMr.setChecked(true);
                } else if (salutationType == Author.SalutationType.Frau) {
                    authorRadioMrs.setChecked(true);
                } else if (salutationType == Author.SalutationType.Divers) {
                    authorRadioNOT_SPECIFIED.setChecked(true);
                }

                AuthorID = bookWithAuthors.get(0).authors.get(0).getAuthorId();

            };
            viewModel.getBooksWithAuthors(book.getBookId()).observe(requireActivity(), bookAuthorsObserver);

            // Create the observer which updates the UI.
            final Observer<List<BookWithGenre>> bookGenresObserver = bookWithGenres -> {
                if (bookWithGenres.size() > 0) {
                    Genre.GenreType genreType = bookWithGenres.get(0).genres.get(0).getDescription();
                    if (genreType == Genre.GenreType.Humor) {
                        RadioButtonHumor.setChecked(true);
                    } else if (genreType == Genre.GenreType.Horror) {
                        RadioButtonHorror.setChecked(true);
                    } else if (genreType == Genre.GenreType.Drama) {
                        RadioButtonDrama.setChecked(true);
                    } else if (genreType == Genre.GenreType.Fable) {
                        RadioButtonFable.setChecked(true);
                    } else if (genreType == Genre.GenreType.Fantasy) {
                        RadioButtonFantasy.setChecked(true);
                    } else if (genreType == Genre.GenreType.Fiction) {
                        RadioButtonFiction.setChecked(true);
                    }
                }

                GenreID = bookWithGenres.get(0).genres.get(0).getGenreId();

            };
            viewModel.getBooksWithGenres(book.getBookId()).observe(requireActivity(), bookGenresObserver);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            this.bookCover.setImageURI(Uri.parse(currentPhotoPath));
        }
    }

    /**
     * creates a file for the captured photo
     */
    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PME_CA_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = "file://" + image.getAbsolutePath();

        Log.i("TakingPictures", currentPhotoPath);

        return image;
    }

    /**
     * in this method it is first checked if the input field of the author first name is empty.
     * if the input field is empty,false will be returned, otherwise true.
     */
    private boolean validateAuthorFirstname() {
        String val = Objects.requireNonNull(authorFirstName.getText()).toString();
        if (val.isEmpty()) {
            authorInputFirstnameLayout.setError("Pflichtfeld");
            return false;
        } else {
            authorInputFirstnameLayout.setError(null);
            return true;
        }
    }

    /**
     * in this method it is first checked if the input field of the author last name is empty.
     * if the input field is empty,false will be returned, otherwise true.
     */
    private boolean validateAuthorLastname() {
        String val = Objects.requireNonNull(authorLastName.getText()).toString();
        if (val.isEmpty()) {
            authorInputLastnameLayout.setError("Pflichtfeld");
            return false;
        } else {
            authorInputLastnameLayout.setError(null);
            return true;
        }
    }

    /**
     * in this method it is first checked if the input field of the book title is empty.
     * if the input field is empty,false will be returned, otherwise true.
     */
    private boolean validateBookTitle() {
        String val = Objects.requireNonNull(bookTitle.getText()).toString();
        if (val.isEmpty()) {
            bookInputTitleLayout.setError("Pflichtfeld");
            return false;
        } else {
            bookInputTitleLayout.setError(null);
            return true;
        }
    }

    /**
     * in this method it is first checked if the input field of the book Publisher is empty.
     * if the input field is empty,false will be returned, otherwise true.
     */
    private boolean validateBookPublisher() {
        String val = Objects.requireNonNull(bookPublisher.getText()).toString();
        if (val.isEmpty()) {
            bookInputPublisherLayout.setError("Pflichtfeld");
            return false;
        } else {
            bookInputPublisherLayout.setError(null);
            return true;
        }
    }

    /**
     * in this method it is first checked if the input field of the book ISBN is empty.
     * if the input field is empty,false will be returned, otherwise true.
     */
    private boolean validateBookIsbn() {
        String val = Objects.requireNonNull(bookIsbn.getText()).toString();
        if (val.isEmpty()) {
            bookInputIsbnLayout.setError("Pflichtfeld");
            return false;
        } else {
            bookInputIsbnLayout.setError(null);
            return true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("test", "test321");
        this.bookLiveData.removeObservers(requireActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.bookLiveData.removeObservers(requireActivity());
    }


}