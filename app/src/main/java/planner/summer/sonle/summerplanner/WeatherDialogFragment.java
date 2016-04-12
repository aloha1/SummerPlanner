package planner.summer.sonle.summerplanner;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sonle on 4/10/16.
 */
public class WeatherDialogFragment extends DialogFragment {
    private String mLayoutType;
    private String mTitle;
    static WeatherDialogFragment newInstance() {
        return new WeatherDialogFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_layout, container, false);
        Bundle args = getArguments();
        mTitle = args.getString("title");
        WeatherFragment wf = new WeatherFragment();
        wf.setArguments(args); // forward area list to WeatherFragment to handle
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.embedded, wf);// get child fragment (WeatherFragment)
        ft.addToBackStack(null);
        ft.commit();
        return v;
    }
    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Pick an area");
        return dialog;
    }
}
