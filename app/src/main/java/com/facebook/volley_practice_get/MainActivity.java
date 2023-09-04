package com.facebook.volley_practice_get;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class MainActivity extends AppCompatActivity {
    private TextView mTextViewResult;
    SimpleDraweeView imageView;
    RequestQueue requestQueue;
    List<String> imageurls;

    int currentImageIndex = 0;
    ImageView imgGlide;
    ImageButton leftButton;
    ImageButton rightButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        imgGlide = findViewById(R.id.imgGlide);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        mTextViewResult = findViewById(R.id.text_view_result);
        Button buttonParse = findViewById(R.id.button_parse);
        imageView = findViewById(R.id.action_image);

        requestQueue = Volley.newRequestQueue(this);
        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParse();
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentImageIndex>0){
                    currentImageIndex --;
                    loadimagearray(currentImageIndex);
                }
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentImageIndex < imageurls.size()-1){
                    currentImageIndex ++;
                    loadimagearray(currentImageIndex);
                }
            }
        });
    }
    private void loadimagearray(int Index) {
        Glide.with(this)
                .load(imageurls.get(Index))
                .into(imgGlide);
    }

    private void jsonParse() {
        String url="https://dummyjson.com/products/5";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int id =response.getInt("id");
                    String description=response.getString("description");
                    String title=response.getString("title");
                    String  image=response.getString("thumbnail");
                    JSONArray jsonArray=response.getJSONArray("images");
                    imageurls=new ArrayList<>();
                 for (int i=0;i<jsonArray.length();i++){
                     imageurls.add(jsonArray.optString(i));
                 }
                    mTextViewResult.append(id+"\n");
                    mTextViewResult.append(description+"\n");
                    mTextViewResult.append(title+"\n");
                    loadimage(image);
                    loadimagearray(0);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void loadimage(String image) {

        //Using Glide..
       /* Glide.with(this)
                .load(image)
                .into(imageView);*/

        //Using Picasso..
       /* Picasso.get()
                .load(image)
                .into(imageView);*/

        //Using Coil...
      /*  ImageLoader imageLoader = new ImageLoader.Builder(this)
                .build();

        ImageRequest imageRequest = new ImageRequest.Builder(this)
                .data(image)
                .target(imageView)
                .build();

        imageLoader.enqueue(imageRequest);*/

        //Using Fresco..
// Set the image URI to load an image into the SimpleDraweeView
        Uri imageUrlUri = Uri.parse(image);

        // Create a DraweeController to load the image into the SimpleDraweeView
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(imageUrlUri)
                .setAutoPlayAnimations(true) // Optional: Auto-play animations if the image is an animated GIF
                .build();

        // Set the controller to the SimpleDraweeView to load the image
        imageView.setController(controller);
    }


}