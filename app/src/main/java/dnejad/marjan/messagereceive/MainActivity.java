package dnejad.marjan.messagereceive;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Marjan.dnejad
 * on 09/12/2017.
 */

public class MainActivity extends AppCompatActivity implements SmsListener{

    private static final int PERMISSION_REQUEST_CODE = 1;

    TextView mCodeView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCodeView=findViewById(R.id.code);

        //initialize variable of SmsListener interface in SmsReceiver broadcast
        SmsReceiver.bindListener(this);

        //check for show runtime permission or not
        checkForRuntimePermission();

    }

    //send fake sms to emulator
    private void sendFakeSms(){
        Random rand = new Random();
        int  n = rand.nextInt(50) + 1;
        SmsManager sms= SmsManager.getDefault();

        sms.sendTextMessage("5554",null,
                String.valueOf(n),null,null);

        Log.e("MESSAGE BODY",String.valueOf(n));
    }

    /*
    * if shown runtime permission after user selected one of Allow or Deny options for permission
    * then run this override method and do sth for each kind
    */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_LONG).show();
                sendFakeSms();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }


    /*
    * check if android version is < Marshmallow
    * and then check that user had generate permission or not
    * and then show runtime permission or call sendFakeSms() method
    */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkForRuntimePermission(){
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_REQUEST_CODE);
        }else {
            sendFakeSms();
        }
    }


    /*
    * this method call from SmsReceiver when receive a sms
    */
    @Override
    public void onMessageReceived(String messageText) {
        Log.e("Text",messageText);
        mCodeView.setText(messageText);
    }
}
