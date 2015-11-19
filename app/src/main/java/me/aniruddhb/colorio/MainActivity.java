package me.aniruddhb.colorio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // holds current swatch of colors and bitmap stuff
    private ArrayList<String> swatch = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toast to user once added to swatch
        final Toast t;

        // Random class
        final Random util = new Random();

        // backdrop for RelativeLayout
        final RelativeLayout backdrop = (RelativeLayout) findViewById(R.id.back_coloring);

        // textview
        final TextView current_col = (TextView) findViewById(R.id.current_color);



        Button color_gen = (Button) findViewById(R.id.color_generator);
        color_gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // generate new Color
                int r, g, b;
                r = util.nextInt(256);
                g = util.nextInt(256);
                b = util.nextInt(256);

                // hex string from the r, g, b
                String hex = String.format("#%02x%02x%02x", r, g, b);

                // sets background color of backdrop to hex string parsed
                backdrop.setBackgroundColor(Color.parseColor(hex));

                // sets the current_col textview text to the current color
                current_col.setText(hex);
            }
        });

        Button save_swatch = (Button) findViewById(R.id.swatch_button);
        save_swatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // current string
                String this_col = current_col.getText().toString();

                // check whether already exists!
                boolean exists = false;
                for (int i = 0; i < swatch.size(); i++) {
                    if (swatch.get(i).equals(this_col)) {
                        exists = true;
                    }
                }

                // if exists is false (needs to be added to swatch)
                if (!exists) {
                    // save current color to swatch
                    swatch.add(this_col);

                    // add user msg to toast
                    this_col += " has been added to the swatch!";

                    // toast to user
                    Toast toast = Toast.makeText(getApplicationContext(), this_col, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 25);
                    toast.show();
                } else {
                    // already in swatch :(
                    this_col += " already exists in the swatch!";
                    Toast toast = Toast.makeText(getApplicationContext(), this_col, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 25);
                    toast.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.email) {

            // build swatch string
            String palette = "Color palette:" + '\n';

            for (int i = 0; i < swatch.size(); i++) {
                palette += swatch.get(i);
                palette += '\n';
            }

            Intent emailer = new Intent(Intent.ACTION_SEND);
            emailer.setType("message/rfc822");
            emailer.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
            emailer.putExtra(Intent.EXTRA_SUBJECT, "Color Palette - made by ColorIO");
            emailer.putExtra(Intent.EXTRA_TEXT, palette);
            try {
                startActivity(Intent.createChooser(emailer, "Send mail!"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }


        return super.onOptionsItemSelected(item);
    }
}
