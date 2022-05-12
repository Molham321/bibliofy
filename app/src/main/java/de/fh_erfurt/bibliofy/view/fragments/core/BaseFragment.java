package de.fh_erfurt.bibliofy.view.fragments.core;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

/**
 * Common super class for all our Fragments. Offers some regularly needed
 * helper methods.
 */
public class BaseFragment extends Fragment {

    /**
     * Getting a ViewModel as well as a specific AndroidViewModel that has
     * access to the context requires some effort. This method hides all the
     * complex stuff.
     *
     * @param tClass
     * @param <T>
     * @return
     */
    protected <T extends ViewModel> T getViewModel(Class<T> tClass) {
        return new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(
                        requireActivity().getApplication()
                )).get(tClass);
    }

    /**
     * Helper method to hide the keyboard, for example when submitting a form.
     *
     * @param context
     * @param view
     */
    public void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Helper Method to remove Back Button from Navigation/Action Bar at the top of the screen
     */
    protected void hideBackButton() {
        // Hide Back Button
        Objects.requireNonNull(
                ((AppCompatActivity) requireActivity()).getSupportActionBar()
        ).setDisplayHomeAsUpEnabled(false);
    }
}

