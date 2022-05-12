package de.fh_erfurt.bibliofy.view.fragments.library;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.fh_erfurt.bibliofy.R;
import de.fh_erfurt.bibliofy.view.fragments.core.BaseFragment;

/**
 * library fragment class that displays books that were sorted into one of
 * the six existing bookshelves
 */

public class LibraryFragment extends BaseFragment implements View.OnClickListener {

    private LibraryViewModel libraryViewModel;
    private LibraryAdapter libraryAdapter;
    private Button button_shelf_a;
    private Button button_shelf_b;
    private Button button_shelf_c;
    private Button button_shelf_d;
    private Button button_shelf_e;
    private Button button_shelf_f;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_library, container, false);

        libraryViewModel = this.getViewModel(LibraryViewModel.class);
        RecyclerView libraryListView = root.findViewById(R.id.list_view_library);

        // Create Adapter
        libraryAdapter = new LibraryAdapter(this.requireActivity(),
                bookId ->
                {
                    Bundle args = new Bundle();
                    args.putLong("bookId", bookId);
                    NavController nc = NavHostFragment.findNavController(this);
                    nc.navigate(R.id.action_ic_library_to_navigation_book_detail, args);
                });

        // Configure RecyclerView with Adapter and LayoutManager
        libraryListView.setAdapter(libraryAdapter);
        libraryListView.setLayoutManager(new GridLayoutManager(this.requireActivity(), 2));

        button_shelf_a = root.findViewById(R.id.button_shelf_a);
        button_shelf_b = root.findViewById(R.id.button_shelf_b);
        button_shelf_c = root.findViewById(R.id.button_shelf_c);
        button_shelf_d = root.findViewById(R.id.button_shelf_d);
        button_shelf_e = root.findViewById(R.id.button_shelf_e);
        button_shelf_f = root.findViewById(R.id.button_shelf_f);

        button_shelf_a.setOnClickListener(this);
        button_shelf_b.setOnClickListener(this);
        button_shelf_c.setOnClickListener(this);
        button_shelf_d.setOnClickListener(this);
        button_shelf_e.setOnClickListener(this);
        button_shelf_f.setOnClickListener(this);

        button_shelf_a.setTextColor(Color.parseColor("#EF6C00"));
        button_shelf_b.setTextColor(Color.parseColor("#5C5C5C"));
        button_shelf_c.setTextColor(Color.parseColor("#5C5C5C"));
        button_shelf_d.setTextColor(Color.parseColor("#5C5C5C"));
        button_shelf_e.setTextColor(Color.parseColor("#5C5C5C"));
        button_shelf_f.setTextColor(Color.parseColor("#5C5C5C"));

        return root;
    }

    /**
     * to display certain books in certain shelves and
     * set teh color to orange when you click on it
     * @param v
     */

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_shelf_a) {
            button_shelf_a.setTextColor(Color.parseColor("#EF6C00"));
            button_shelf_b.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_c.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_d.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_e.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_f.setTextColor(Color.parseColor("#5C5C5C"));

            libraryViewModel.getAllBooksWithBookshelfName("Regal A").observe(this.requireActivity(), libraryAdapter::setBookWithAuthors);


        } else if (v.getId() == R.id.button_shelf_b) {
            button_shelf_b.setTextColor(Color.parseColor("#EF6C00"));
            button_shelf_a.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_c.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_d.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_e.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_f.setTextColor(Color.parseColor("#5C5C5C"));
            libraryViewModel.getAllBooksWithBookshelfName("Regal B").observe(this.requireActivity(), libraryAdapter::setBookWithAuthors);

        } else if (v.getId() == R.id.button_shelf_c) {
            button_shelf_c.setTextColor(Color.parseColor("#EF6C00"));
            button_shelf_a.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_b.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_d.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_e.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_f.setTextColor(Color.parseColor("#5C5C5C"));
            libraryViewModel.getAllBooksWithBookshelfName("Regal C").observe(this.requireActivity(), libraryAdapter::setBookWithAuthors);

        } else if (v.getId() == R.id.button_shelf_d) {
            button_shelf_d.setTextColor(Color.parseColor("#EF6C00"));
            button_shelf_a.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_b.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_c.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_e.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_f.setTextColor(Color.parseColor("#5C5C5C"));
            libraryViewModel.getAllBooksWithBookshelfName("Regal D").observe(this.requireActivity(), libraryAdapter::setBookWithAuthors);

        } else if (v.getId() == R.id.button_shelf_e) {
            button_shelf_e.setTextColor(Color.parseColor("#EF6C00"));
            button_shelf_a.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_b.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_c.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_d.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_f.setTextColor(Color.parseColor("#5C5C5C"));
            libraryViewModel.getAllBooksWithBookshelfName("Regal E").observe(this.requireActivity(), libraryAdapter::setBookWithAuthors);


        } else {
            button_shelf_f.setTextColor(Color.parseColor("#EF6C00"));
            button_shelf_a.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_b.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_c.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_d.setTextColor(Color.parseColor("#5C5C5C"));
            button_shelf_e.setTextColor(Color.parseColor("#5C5C5C"));
            libraryViewModel.getAllBooksWithBookshelfName("Regal F").observe(this.requireActivity(), libraryAdapter::setBookWithAuthors);
        }
    }

}