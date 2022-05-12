package de.fh_erfurt.bibliofy.view.fragments.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.fh_erfurt.bibliofy.R;
import de.fh_erfurt.bibliofy.view.fragments.core.BaseFragment;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private HomeAdapter adapter;
    private HomeViewModel homeViewModel;

    private Button button_all_books;
    private Button button_read_books;
    private Button button_wished_books;
    private Button button_favorite_books;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Create Layout/Views
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Get View Model
        homeViewModel = this.getViewModel(HomeViewModel.class);

        // Get RecyclerView Reference
        RecyclerView bookListView = root.findViewById(R.id.list_view_books);

        // Create Adapter
        adapter = new HomeAdapter(this.requireActivity(),
                bookId ->
                {
                    Bundle args = new Bundle();
                    args.putLong("bookId", bookId);
                    NavController nc = NavHostFragment.findNavController(this);
                    nc.navigate(R.id.action_ic_home_to_navigation_book_detail, args);
                });

        // Configure RecyclerView with Adapter and LayoutManager
        bookListView.setAdapter(adapter);
        bookListView.setLayoutManager(new GridLayoutManager(this.requireActivity(), 2));

        button_all_books = root.findViewById(R.id.button_all_books);
        button_read_books = root.findViewById(R.id.button_read_books);
        button_wished_books = root.findViewById(R.id.button_wished_books);
        button_favorite_books = root.findViewById(R.id.button_favorite_books);

        button_all_books.setOnClickListener(this);
        button_read_books.setOnClickListener(this);
        button_wished_books.setOnClickListener(this);
        button_favorite_books.setOnClickListener(this);

        button_all_books.setTextColor(Color.parseColor("#EF6C00"));
        button_favorite_books.setTextColor(Color.parseColor("#5C5C5C"));
        button_wished_books.setTextColor(Color.parseColor("#5C5C5C"));
        button_read_books.setTextColor(Color.parseColor("#5C5C5C"));

        homeViewModel.getBooksWithAuthors().observe(this.requireActivity(), adapter::setBookWithAuthors);

        return root;
    }

    /**
     * to display the search bar
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * to display certain books from certain sections and
     * set teh color to orange when you click on it
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_read_books) {
            button_read_books.setTextColor(Color.parseColor("#EF6C00"));
            button_wished_books.setTextColor(Color.parseColor("#5C5C5C"));
            button_favorite_books.setTextColor(Color.parseColor("#5C5C5C"));
            button_all_books.setTextColor(Color.parseColor("#5C5C5C"));

            homeViewModel.getBooksSortedByHadRead().observe(this.requireActivity(), adapter::setBookWithAuthors);
        } else if (v.getId() == R.id.button_wished_books) {
            button_wished_books.setTextColor(Color.parseColor("#EF6C00"));
            button_read_books.setTextColor(Color.parseColor("#5C5C5C"));
            button_favorite_books.setTextColor(Color.parseColor("#5C5C5C"));
            button_all_books.setTextColor(Color.parseColor("#5C5C5C"));

            homeViewModel.getBooksSortedByWish().observe(this.requireActivity(), adapter::setBookWithAuthors);
        } else if (v.getId() == R.id.button_favorite_books) {
            button_favorite_books.setTextColor(Color.parseColor("#EF6C00"));
            button_wished_books.setTextColor(Color.parseColor("#5C5C5C"));
            button_read_books.setTextColor(Color.parseColor("#5C5C5C"));
            button_all_books.setTextColor(Color.parseColor("#5C5C5C"));

            homeViewModel.getBooksSortedByFavourite().observe(this.requireActivity(), adapter::setBookWithAuthors);
        } else {
            button_all_books.setTextColor(Color.parseColor("#EF6C00"));
            button_favorite_books.setTextColor(Color.parseColor("#5C5C5C"));
            button_wished_books.setTextColor(Color.parseColor("#5C5C5C"));
            button_read_books.setTextColor(Color.parseColor("#5C5C5C"));

            homeViewModel.getBooksWithAuthors().observe(this.requireActivity(), adapter::setBookWithAuthors);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        homeViewModel.getBooksWithAuthors().removeObservers(requireActivity());
        homeViewModel.getBooksSortedByFavourite().removeObservers(requireActivity());
        homeViewModel.getBooksSortedByWish().removeObservers(requireActivity());
        homeViewModel.getBooksSortedByHadRead().removeObservers(requireActivity());
    }


}