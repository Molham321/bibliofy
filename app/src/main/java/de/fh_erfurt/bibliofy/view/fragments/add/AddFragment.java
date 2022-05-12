package de.fh_erfurt.bibliofy.view.fragments.add;

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

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import de.fh_erfurt.bibliofy.R;
import de.fh_erfurt.bibliofy.model.Author;
import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.Bookshelf;
import de.fh_erfurt.bibliofy.model.Genre;
import de.fh_erfurt.bibliofy.model.relations.BookAuthorCrossRef;
import de.fh_erfurt.bibliofy.model.relations.BookGenreCrossRef;
import de.fh_erfurt.bibliofy.view.fragments.core.BaseFragment;

public class AddFragment extends BaseFragment {

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
    private AddViewModel addViewModel;
    private View         root;
    private TextInputEditText authorFirstName;
    private EditText authorLastName;
    private TextInputLayout authorInputFirstnameLayout, authorInputLastnameLayout;
    private TextInputLayout bookInputTitleLayout, bookInputPublisherLayout, bookInputIsbnLayout;
    private RadioGroup  authorRadioGroup;
    private EditText    bookTitle;
    private EditText    bookPublisher;
    private EditText    bookNumberOfPages;
    private EditText    bookIsbn;
    private EditText    bookDescription;
    private TextView    bookYearsOfPublication;
    private CheckBox    checkBoxRed;
    private CheckBox    checkBoxWish;
    private CheckBox    checkBoxFavourite;
    private ImageView   bookCover;
    private RadioGroup  genreRadioGroup;
    private Spinner     spinner;


    /**
     * in this method will prepare the data before they are added to the database.
     * A book will be saved in the database when all the required fields are already full with data.
     * @param saveButtonClickListener
     */
    private final View.OnClickListener saveButtonClickListener = v -> {

        if (v.getId() == R.id.btnAddBookSave) {
            if (!validateAuthorFirstname() | !validateAuthorLastname() | !validateBookTitle() | !validateBookPublisher() | !validateBookIsbn()) {
                String returnValue = "Buch konnte nicht gespeichert werden";

                hideKeyboard(this.requireContext(), v);
                Snackbar.make(v, returnValue, Snackbar.LENGTH_SHORT).show();

            } else {

                //-------------Bookshelf Details ------------------
                Bookshelf newBookshelf = new Bookshelf();
                String bookshelfName = spinner.getSelectedItem().toString();
                newBookshelf.setBookshelfName(bookshelfName);

                //------------- save Bookshelf Details ------------------
                long newBookshelfId = addViewModel.saveBookshelf(newBookshelf);


                //-------------Genre Details ------------------

                Genre newGenre = new Genre();
                int checkedGenreRadioButtonId = this.genreRadioGroup.getCheckedRadioButtonId();
                if (checkedGenreRadioButtonId == R.id.genre_input_Type_RadioButton_humor ) {
                    newGenre.setDescription(Genre.GenreType.Humor);
                }
                else if (checkedGenreRadioButtonId == R.id.genre_input_Type_RadioButton_fiction) {
                    newGenre.setDescription(Genre.GenreType.Fiction);
                }
                else if (checkedGenreRadioButtonId == R.id.genre_input_Type_RadioButton_fantasy) {
                    newGenre.setDescription(Genre.GenreType.Fantasy);
                }
                else if (checkedGenreRadioButtonId == R.id.genre_input_Type_RadioButton_horror) {
                    newGenre.setDescription(Genre.GenreType.Horror);
                }
                else if (checkedGenreRadioButtonId == R.id.genre_input_Type_RadioButton_drama) {
                    newGenre.setDescription(Genre.GenreType.Drama);
                }
                else if (checkedGenreRadioButtonId == R.id.genre_input_Type_RadioButton_fable) {
                    newGenre.setDescription(Genre.GenreType.Fable);
                }else{
                    newGenre.setDescription(Genre.GenreType.Fable);
                }

                //------------- save Bookshelf Details ------------------
                long newGenreId = addViewModel.saveGenre(newGenre);

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
                newBook.setBookshelfId(newBookshelfId);
                if (this.checkBoxRed.isChecked()) {
                    newBook.setHadRead(true);
                }
                if (this.checkBoxWish.isChecked()) {
                    newBook.setWish(true);
                }
                if (this.checkBoxFavourite.isChecked()) {
                    newBook.setFavourite(true);
                }

                //------------- save Book Details ------------------

                long newBookId = addViewModel.saveBook(newBook);

                //-------------Author Details -----------------

                Author newAuthor = new Author(
                        Objects.requireNonNull(authorFirstName.getText()).toString(),
                        Objects.requireNonNull(authorLastName.getText()).toString()
                );
                int checkedAuthorRadioButtonId = this.authorRadioGroup.getCheckedRadioButtonId();
                if (checkedAuthorRadioButtonId == R.id.author_input_Type_group_Mr) {
                    newAuthor.setSalutationType(Author.SalutationType.Herr);
                } else if (checkedAuthorRadioButtonId == R.id.author_input_Type_group_Mrs) {
                    newAuthor.setSalutationType(Author.SalutationType.Frau);
                } else {
                    newAuthor.setSalutationType(Author.SalutationType.Divers);
                }
                //------------- save Author Details ------------------

                long newAuthorId = addViewModel.saveAuthor(newAuthor);

                //-------------BookAuthorCrossRef Details ------------------

                BookAuthorCrossRef bookAuthorCrossRef = new BookAuthorCrossRef();
                bookAuthorCrossRef.setBookId(newBookId);
                bookAuthorCrossRef.setAuthorId(newAuthorId);

                //------------- save BookAuthorCrossRef Details ------------------
                addViewModel.saveBookAuthorCrossRef(bookAuthorCrossRef);

                //-------------BookGenreCrossRef Details ------------------

                BookGenreCrossRef bookGenreCrossRef = new BookGenreCrossRef();
                bookGenreCrossRef.setBookId(newBookId);
                bookGenreCrossRef.setGenreId(newGenreId);

                //------------- save BookAuthorCrossRef Details ------------------
                addViewModel.saveBookGenreCrossRef(bookGenreCrossRef);

                String returnValue = "Buch erfolgreich gespeichert";

                hideKeyboard(this.requireContext(), v);
                Snackbar.make(v, returnValue, Snackbar.LENGTH_SHORT).show();
                Navigation.findNavController(root).navigate(R.id.action_ic_add_to_ic_home);
            }
        }


    };

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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        addViewModel = this.getViewModel(AddViewModel.class);

        this.root = inflater.inflate(R.layout.fragment_add, container, false);
        this.authorFirstName = root.findViewById(R.id.et_firstname);
        this.authorInputFirstnameLayout = root.findViewById(R.id.author_FirstnameLayout);
        this.authorLastName = root.findViewById(R.id.et_lastname);
        this.authorInputLastnameLayout = root.findViewById(R.id.author_LastnameLayout);
        this.authorRadioGroup = root.findViewById(R.id.author_input_Type_group);
        this.bookTitle = root.findViewById(R.id.et_bookTitle);
        this.bookInputTitleLayout = root.findViewById(R.id.book_TitleLayout);
        this.bookPublisher = root.findViewById(R.id.et_book_publisher);
        this.bookInputPublisherLayout = root.findViewById(R.id.book_PublisherLayout);
        this.bookNumberOfPages = root.findViewById(R.id.et_book_number_of_pages);
        this.bookIsbn = root.findViewById(R.id.et_book_isbn);
        this.bookInputIsbnLayout = root.findViewById(R.id.book_isbnLayout);
        this.bookDescription = root.findViewById(R.id.et_book_description);
        this.bookYearsOfPublication = root.findViewById(R.id.et_book_yearsOfPublication);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        bookYearsOfPublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddFragment.super.getContext()
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
        this.checkBoxRed = root.findViewById(R.id.book_input_Type_CheckBox_hasRead);
        this.checkBoxWish = root.findViewById(R.id.book_input_Type_CheckBox_wish);
        this.checkBoxFavourite = root.findViewById(R.id.book_input_Type_CheckBox_favourite);
        this.bookCover = root.findViewById(R.id.btnAddImage);
        this.bookCover.setOnClickListener(this.bookCoverClickListener);
        this.genreRadioGroup = root.findViewById(R.id.genre_input_Type_group);
        this.spinner = root.findViewById(R.id.bookshelfSpinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(AddFragment.super.getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.bookshelves));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);



        Button saveBtn = root.findViewById(R.id.btnAddBookSave);
        saveBtn.setOnClickListener(this.saveButtonClickListener);

        return root;
    }


}