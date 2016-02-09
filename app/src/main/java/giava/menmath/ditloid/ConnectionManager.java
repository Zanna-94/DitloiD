package giava.menmath.ditloid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

/**
 * Created by emanuele on 08/02/16.
 */
public class ConnectionManager extends AsyncTask<String, Void, Void> {

    private TextView tvEnigma;
    private TextView tvCategoria;
    private Button btnCheck;
    private EditText etSoluction;

    private OutputStream ostream;
    private InputStream istream;

    private Ditloid ditloid;

    private Dialog waitingDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        try {
            List<String> ditloids = DatabaseAccess.getInstance(MyApplication.getAppContext())
                    .getDitloids();

            int size = ditloids.size();

            long seconds = System.currentTimeMillis() / 1000l;
            Random rand = new Random(seconds);
            int id = rand.nextInt(size - 1);

            ostream.write(id);

            ditloid = DatabaseAccess.getInstance(MyApplication.getAppContext()).getById(id);

            tvEnigma.setText(ditloid.getEnigma());
            tvCategoria.setText(ditloid.getCategory());


        } catch (IOException e) {
            e.printStackTrace();

        }


    }

    @Override
    protected Void doInBackground(String... param) {

        if(param[0]=="Client")
            playLikeClient();
        else if(param[0]=="Server")
            playLikeServer();

        return null;
    }


    public void playLikeClient(){

    }

    public void playLikeServer(){

    }

    public void setOutputStream(OutputStream ostream) {
        this.ostream = ostream;
    }

    public void setInputStream(InputStream istream) {
        this.istream = istream;
    }

    public void setTvEnigma(TextView tvEnigma) {
        this.tvEnigma = tvEnigma;
    }

    public void setTvCategoria(TextView tvCategoria) {
        this.tvCategoria = tvCategoria;
    }

    public void setBtnCheck(Button btnCheck) {
        this.btnCheck = btnCheck;
    }

    public void setEtSoluction(EditText etSoluction) {
        this.etSoluction = etSoluction;
    }

}
