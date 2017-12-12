package com.example.android.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;
    int price = 0;
    String text = "";
    String quote1Line = "";
    String quote2Line = "";
    ImageView drinkPicture;
    String subject = "";
    String drink = "";
    String hasWhippedCreamAnswer = "";
    String hasChocolateToppingAnswer = "";

    /**
     * Identify which drink was chosen.
     */
    private RadioGroup radioGroupDrink;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher_round);



        drinkPicture = (ImageView) findViewById(R.id.drink);
        radioGroupDrink = (RadioGroup) findViewById(R.id.radio_group_drink);
        radioGroupDrink.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected and what kind of topping is pick up

                if (checkedId == R.id.coffee) {

                    text = "\n" + getString(R.string.your_choice_coffee);
                    subject = getString(R.string.coffee_order);
                    quote1Line = getString(R.string.coffee_quote);
                    quote2Line = "Connor Franta";
                    drinkPicture.setImageResource(R.drawable.coffee1);
                    drink = getString(R.string.coffee);

                } else if (checkedId == R.id.tea) {
                    text = "\n" + getString(R.string.your_choice_tea);
                    subject = getString(R.string.tea_order);
                    quote1Line = getString(R.string.tea_quote);
                    quote2Line = "Letitia Baldrige";
                    drinkPicture.setImageResource(R.drawable.tea1);
                    drink = "tea";

                } else {

                    text = "\n" + getString(R.string.your_choice_choco);
                    subject = getString(R.string.choco_order);
                    quote1Line = getString(R.string.choco_quote);
                    quote2Line = "Talulah Riley";
                    drinkPicture.setImageResource(R.drawable.chcolate);
                    drink = getString(R.string.choco);
                }
                int selectedId = radioGroupDrink.getCheckedRadioButtonId();
            }
        });
    }

    /**
     * This method is called when the order button is clicked.
     */

    public void submitOrder(View view) {
        EditText nameField = (EditText) findViewById(R.id.name);
        String name = nameField.getText().toString();

        //figure out if Whipped cream is checked
        CheckBox whippedCreamStatus = (CheckBox) findViewById(R.id.whipped_cream);
        boolean hasWhippedCream = whippedCreamStatus.isChecked();
        hasWhippedCreamAnswer = hasWhippedCream ? getString(R.string.yes) : getString(R.string.no);


        //figure out if chocolade is checked
        CheckBox chocolateToppingStatus = (CheckBox) findViewById(R.id.chocolade_topping);
        boolean hasChocolateTopping = chocolateToppingStatus.isChecked();
        hasChocolateToppingAnswer = hasChocolateTopping ? getString(R.string.yes) : getString(R.string.no);

        price = calculatePrice(hasWhippedCream, hasChocolateTopping);


        String priceMessage = createOrderSummary(name, price, hasWhippedCream, hasChocolateTopping);

        Intent email = new Intent(Intent.ACTION_SEND);
        email.setData(Uri.parse("mailto:"));
        email.setType("text/plain");
        email.putExtra(Intent.EXTRA_EMAIL, name);
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, priceMessage);

        if (email.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(email, "Send Email"));

        }
    }

    /**
     * Order Summary.
     *
     * @ int price is a price of ordered drinks
     * @ addWhippedCram drink with or without
     * @ addChocoladeTopping with or without
     */

    public String createOrderSummary(String name, int price, boolean hasWhippedCream, boolean hasChocolateTopping) {

        String thanks = "\n" + getString(R.string.thank_you);
        String addCream = "\n" + getString(R.string.add_whipped_cream) + " " + hasWhippedCreamAnswer;
        String addTopping = "\n" + getString(R.string.add_chocolade) + " " + hasChocolateToppingAnswer;
        String priceMessage = getString(R.string.name) + name;
        priceMessage += text;

        priceMessage += addCream;
        priceMessage += addTopping;
        priceMessage += "\n" + getString(R.string.quantity_java) + " " + quantity;
        priceMessage += "\n" + getString(R.string.Total) + price + " " + "€";
        priceMessage += "\n" + "\n";
        priceMessage += getString(R.string.thank_you);

        priceMessage += "\n" + "\n";
        priceMessage += quote1Line;
        priceMessage += "\n";
        priceMessage += quote2Line;

        return priceMessage;

    }


    /**
     * This method calculate price.
     */
    //param adWhipedCream add 1$ to total price
    //param addChocoladeTopping add 2$ to tota price
    public int calculatePrice(boolean addWhippedCream, boolean addChocolateTopping) {
        int basePrice = 5;
        if (addWhippedCream) {
            basePrice = basePrice + 1;
        }

        if (addChocolateTopping) {
            basePrice = basePrice + 2;
        }
        return quantity * basePrice;

    }

    /**
     * This method is called when we increase numbers of cups. Max No of cups is 100
     */
    public void increment(View view) {
        quantity = quantity + 1;

        if (quantity > 100) {
            Toast.makeText(this, getString(R.string.more_100), Toast.LENGTH_SHORT).show();

            quantity = 100;
        }
        displayQuantity(quantity);
    }

    /**
     * This method is called when we decrease numbers of cups. Min. No of cups is 1
     */


    public void decrement(View view) {

        quantity = quantity - 1;
        if (quantity < 1) {

            Toast.makeText(this, getString(R.string.less_100), Toast.LENGTH_SHORT).show();
            quantity = 1;
        }
        displayQuantity(quantity);

    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

}