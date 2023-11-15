package com.example.cryptovish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cryptovish.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<CryptoModal> arrCrpyto = new ArrayList<>();
    RequestQueue queue;

    String apiUrl = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    String apiKey = "ca8a6249-20b7-4494-9874-8abba66f80f9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cryptorv.setLayoutManager(new LinearLayoutManager(this));

        CryptoAdapter cryptoAdapter = new CryptoAdapter(this, arrCrpyto);

        binding.cryptorv.setAdapter(cryptoAdapter);

        binding.searchIcon.setOnClickListener(view -> {
            binding.searchbar.setVisibility(View.VISIBLE);
            binding.searchIcon.setVisibility(View.INVISIBLE);
            binding.cancelIcon.setVisibility(View.VISIBLE);
        });

        binding.cancelIcon.setOnClickListener(view -> {
            binding.searchbar.setVisibility(View.INVISIBLE);
            binding.searchbarET.setText("");
            binding.searchIcon.setVisibility(View.VISIBLE);
            binding.cancelIcon.setVisibility(View.INVISIBLE);
        });


        //queue = Volley.newRequestQueue(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();

        //volley queue
        @SuppressLint({"NotifyDataSetChanged", "DefaultLocale"})
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null, response -> {
            binding.progressCryptorv.setVisibility(View.INVISIBLE);
            try {
                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject cryptoObject  = jsonArray.getJSONObject(i);
                    String name = cryptoObject.getString("name");
                    String symbol = cryptoObject.getString("symbol");
                    JSONObject quote = cryptoObject.getJSONObject("quote");
                    JSONObject usd = quote.getJSONObject("USD");
                    Double price = usd.getDouble("price");
                    Double change1h = usd.getDouble("percent_change_1h");
                    Double change24h = usd.getDouble("percent_change_24h");
                    Double change7d = usd.getDouble("percent_change_7d");
                    Double change30d = usd.getDouble("percent_change_30d");
                    Double change60d = usd.getDouble("percent_change_60d");
                    Double change90d = usd.getDouble("percent_change_90d");

                    arrCrpyto.add(new CryptoModal(name, symbol, price, change1h, change24h, change7d, change30d, change60d, change90d));
                }
                cryptoAdapter.notifyDataSetChanged();
                //binding.countcryptbtn.setText("Count: " + arrCrpyto.size());

            } catch (Exception e) {
                System.out.println("Exception Occurred: " + e.getMessage());
            }
        }, Throwable::printStackTrace)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //return super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("X-CMC_PRO_API_KEY", apiKey);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);

        Comparator<CryptoModal> comAsce = new Comparator<CryptoModal>() {
            @Override
            public int compare(CryptoModal crypto1, CryptoModal crypto2) {
                if(crypto1.price > crypto2.price) return 1;
                else return -1;
            }
        };
        Comparator<CryptoModal> comDesc = new Comparator<CryptoModal>() {
            @Override
            public int compare(CryptoModal crypto1, CryptoModal crypto2) {
                if(crypto1.price > crypto2.price) return -1;
                else return 1;
            }
        };

        /*//sorting
        binding.sortAsce.setOnClickListener(view -> {
            arrCrpyto.sort(comAsce);
            cryptoAdapter.notifyDataSetChanged();
        });

        binding.sortDesc.setOnClickListener(view -> {
            arrCrpyto.sort(comDesc);
            cryptoAdapter.notifyDataSetChanged();
        });*/

        binding.searchbarET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().isEmpty()) {
                    cryptoAdapter.filterList(arrCrpyto);
                }
                ArrayList<CryptoModal> filteredList = new ArrayList<>();
                for(CryptoModal crypto : arrCrpyto) {
                    if(crypto.cyptoName.toLowerCase().contains(editable.toString().toLowerCase())) {
                        filteredList.add(crypto);
                    }
                }
                cryptoAdapter.filterList(filteredList);
                binding.searchbarET.clearFocus();
                //binding.countcryptbtn.setText("Count: " + filteredList.size());
            }
        });

    }
}