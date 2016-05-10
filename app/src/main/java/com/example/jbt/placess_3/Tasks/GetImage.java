package com.example.jbt.placess_3.Tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Toshiba on 2/23/2016.
 */
public class GetImage extends AsyncTask<String,Void,byte[]> {
    @Override
    protected byte[] doInBackground(String... params) {
        //get the address from the params:
        String address = params[0];
        HttpURLConnection connection =null;
        InputStream stream = null;
        ByteArrayOutputStream outputStream = null;

        //the bitmap will go here:
        Bitmap b = null;
        byte[] imageBytes=null;


        try {
            // build the URL:
            URL url = new URL(address);
            // open a connection:
            connection = (HttpURLConnection) url.openConnection();

            // check the connection response code:
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                // not good..
                return null;
            }

            // the input stream:
            stream = connection.getInputStream();

            // get the length:
            int length = connection.getContentLength();
            // tell the progress dialog the length:
            // this CAN (!!) be modified outside the UI thread !!!


            // a stream to hold the read bytes.
            // (like the StringBuilder we used before)
            outputStream = new ByteArrayOutputStream();

            // a byte buffer for reading the stream in 1024 bytes chunks:
            byte[] buffer = new byte[1024];

            int totalBytesRead = 0;
            int bytesRead = 0;

            //read the bytes from the stream
            while ((bytesRead = stream.read(buffer, 0, buffer.length)) != -1) {
                totalBytesRead += bytesRead;
                outputStream.write(buffer, 0, bytesRead);

                //notify the UI thread on the progress so far:

            }

            // flush the output stream - write all the pending bytes in its
            // internal buffer.
            outputStream.flush();

            // get a byte array out of the outputStream
            // theses are the bitmap bytes
             imageBytes = outputStream.toByteArray();

            // use the BitmapFactory to convert it to a bitmap
            b = BitmapFactory.decodeByteArray(imageBytes, 0, length);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                // close connection:
                connection.disconnect();
            }
            if (outputStream != null) {
                try {
                    // close output stream:
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return imageBytes;



    }
}
