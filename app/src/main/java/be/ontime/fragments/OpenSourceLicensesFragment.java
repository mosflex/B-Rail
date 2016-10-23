package be.ontime.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.webkit.WebView;

import be.ontime.R;

/**
 * Created by Jawad on 06-10-16.
 */
public class OpenSourceLicensesFragment extends DialogFragment {
    final static String LICENCES_HTML_FILE = "file:///android_asset/licenses.html";

    public OpenSourceLicensesFragment() {}

    public static OpenSourceLicensesFragment newInstance(int title) {
        OpenSourceLicensesFragment frag = new OpenSourceLicensesFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a webview, and load the HTML file
        WebView webView = new WebView(getActivity());
        webView.loadUrl(LICENCES_HTML_FILE);

        // Add the webview to the content of the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(webView)
                .setTitle(R.string.pref_licences)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();

    }
}