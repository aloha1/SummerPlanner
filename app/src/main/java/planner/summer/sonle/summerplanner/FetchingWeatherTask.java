package planner.summer.sonle.summerplanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sonle on 4/10/16.
 */
public class FetchingWeatherTask extends AsyncTask<String, Void, String> {
    private final Context context;
    private String locationName;
    public AsyncResponse delegate = null;
    private ProgressDialog mDialog;
    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(String output);
    }
    public FetchingWeatherTask(Context c, AsyncResponse delegate){
        this.context = c;
        this.delegate = delegate;
        mDialog = new ProgressDialog(context);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog.setMessage("Loading weather...");
        mDialog.show();
    }
    @Override
    protected String doInBackground(String... params) {
        try{
            String query = params[0];
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            StringBuilder stringBuilder = new StringBuilder();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                // bridge than convert(read and decode to bytes) bytes to characters using charset
                InputStreamReader streamReader = new InputStreamReader(conn.getInputStream());
                // read text from character input stream
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String response = null;
                while ((response = bufferedReader.readLine()) != null) {
                    stringBuilder.append(response + "\n");
                }
                // Closes the stream and releases any system resources associated with it.
                bufferedReader.close();
                Log.d("SummerPlanner", stringBuilder.toString());
                return stringBuilder.toString();
            } else {
                Log.e("SummerPlanner", conn.getResponseMessage());
            }
        } catch (IOException x) {
            Log.e("SummerPlanner", "Error sending param", x);
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}