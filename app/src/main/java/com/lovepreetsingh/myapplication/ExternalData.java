package com.lovepreetsingh.myapplication;

import android.app.Activity;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Lovepreet Singh on 08-04-2015.
 */
public class ExternalData extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private TextView canWrite, canRead;
    private String state;
    boolean canW, canR;
    Spinner spinner;
    String[] paths = { "Music", "Pictures", "Download"};
    File path = null;
    File file =null;
    EditText saveFile;
    Button confirm,save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.externaldata);
        canWrite = (TextView) findViewById(R.id.tvCanWrite);
        canRead =(TextView)findViewById(R.id.tvCanRead);
        confirm = (Button) findViewById(R.id.bConfirmSaveAs);
        save =(Button) findViewById(R.id.bSaveFile);
        saveFile = (EditText) findViewById(R.id.etSaveAs);
        confirm.setOnClickListener(this);
        save.setOnClickListener(this);

        checkState();

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(ExternalData.this, android.R.layout.simple_spinner_item, paths);
        spinner =(Spinner)findViewById(R.id.spinner1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    private void checkState() {
        state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            //read and write
            canWrite.setText("true");
            canRead.setText("true");
            canW = canR =true;
        } else if(state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            //read but can't write
            canWrite.setText("false");
            canRead.setText("true");
            canW=false;
            canR = true;
        }else {
            canWrite.setText("false");
            canRead.setText("false");
            canW = false;
            canR = false;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?>arg0, View arg1, int arg2, long arg3) {
        int position = spinner.getSelectedItemPosition();
        switch(position){
            case 0:
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                break;
            case 1:
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                break;
            case 2:
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View arg0) {
        switch(arg0.getId()){
            case R.id.bSaveFile:
                String f= saveFile.getText().toString();
                file = new File(path,f + ".png");
                checkState();
                if(canW == canR ==true){
                    path.mkdirs();

                    try {
                        InputStream is = getResources().openRawResource(R.drawable.greenball);
                        OutputStream os =new FileOutputStream(file);
                        byte[] data = new byte[is.available()];
                        is.read(data);
                        os.write(data);
                        is.close();
                        os.close();

                        Toast t = Toast.makeText(ExternalData.this, "File has been saved", Toast.LENGTH_LONG);
                        t.show();
                        //Update files for the user to use
                        MediaScannerConnection.scanFile(ExternalData.this,
                                new String[]{file.toString()},
                                null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Toast t = Toast.makeText(ExternalData.this,"sacn complete", Toast.LENGTH_SHORT);
                                t.show();
                            }
                        });

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.bConfirmSaveAs:
                save.setVisibility(View.VISIBLE);
                break;

        }

    }
}
