package com.example.cryptovish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Switch;

import com.example.cryptovish.databinding.ActivitySelectedCryptoBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SelectedCrypto extends AppCompatActivity {

    ActivitySelectedCryptoBinding binding;
    ArrayList<String> changeList = new ArrayList<>();
    ArrayList<Entry> arrEntry = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectedCryptoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //spinner
        changeList.add("24 Hours");
        changeList.add("1 Hour");
        changeList.add("7 Days");
        changeList.add("30 Days");
        changeList.add("60 Days");
        changeList.add("90 Days");

        ArrayAdapter<String> changesAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, changeList);
        binding.changeSpinner.setAdapter(changesAdapter);

        binding.negChange.setColorFilter(Color.RED);
        binding.posChange.setColorFilter(Color.parseColor("#00FF2A"));

        binding.backArrow.setOnClickListener(view -> {
            this.finish();
        });

        Intent intent = getIntent();

        CryptoModal selectedCrypto = (CryptoModal) intent.getSerializableExtra("selectedCrypto");

        binding.nameSymbol.setText(selectedCrypto.nameSymbol);
        binding.name.setText(selectedCrypto.cyptoName);
        binding.price.setText("$" + String.format("%.2f", selectedCrypto.price));

        if (selectedCrypto.change24h < 0) {
            binding.negChange.setVisibility(View.VISIBLE);
            binding.changePercent.setTextColor(Color.RED);
        } else {
            binding.posChange.setVisibility(View.VISIBLE);
            binding.changePercent.setTextColor(Color.parseColor("#00FF2A"));
        }

        binding.changePercent.setText(String.format("%.2f", selectedCrypto.change24h) + "%");

        binding.changeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i) {
                    case 0:
                        handleCryptoChange(selectedCrypto.change24h);
                        //handleChartChange(selectedCrypto, selectedCrypto.change24h);
                        return;
                    case 2:
                        handleCryptoChange(selectedCrypto.change7d);
                        //handleChartChange(selectedCrypto, selectedCrypto.change7d);
                        return;
                    case 3:
                        handleCryptoChange(selectedCrypto.change30d);
                        //handleChartChange(selectedCrypto, selectedCrypto.change30d);
                        return;
                    case 4:
                        handleCryptoChange(selectedCrypto.change60d);
                        //handleChartChange(selectedCrypto, selectedCrypto.change60d);
                        return;
                    case 5:
                        handleCryptoChange(selectedCrypto.change90d);
                        //handleChartChange(selectedCrypto, selectedCrypto.change90d);
                        return;
                    default:
                        handleCryptoChange(selectedCrypto.change1h);
                        //handleChartChange(selectedCrypto, selectedCrypto.change1h);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");
        String formattedDate = dateFormat.format(currentTime);

        //Line Chart 90 days to Current
        float currentPrice = Float.parseFloat(String.valueOf(selectedCrypto.price));

        float changePercent1h = Float.parseFloat(String.valueOf(selectedCrypto.change1h));
        float changePercent24h = Float.parseFloat(String.valueOf(selectedCrypto.change24h));
        float changePercent7d = Float.parseFloat(String.valueOf(selectedCrypto.change7d));
        float changePercent30d = Float.parseFloat(String.valueOf(selectedCrypto.change30d));
        float changePercent60d = Float.parseFloat(String.valueOf(selectedCrypto.change60d));
        float changePercent90d = Float.parseFloat(String.valueOf(selectedCrypto.change90d));

        float oldPrice1h = currentPrice / (1 + (changePercent1h/100));
        float oldPrice24h = currentPrice / (1 + (changePercent24h/100));
        float oldPrice7d = currentPrice / (1 + (changePercent7d/100));
        float oldPrice30d = currentPrice / (1 + (changePercent30d/100));
        float oldPrice60d = currentPrice / (1 + (changePercent60d/100));
        float oldPrice90d = currentPrice / (1 + (changePercent90d/100));

        String formattedOld1h = String.format("%.2f", oldPrice1h);
        String formattedOld24h = String.format("%.2f", oldPrice24h);
        String formattedOld7d = String.format("%.2f", oldPrice7d);
        String formattedOld30d = String.format("%.2f", oldPrice30d);
        String formattedOld60d = String.format("%.2f", oldPrice60d);
        String formattedOld90d = String.format("%.2f", oldPrice90d);
        String formattedCurrent = String.format("%.2f", currentPrice);

//        System.out.println("Current Price: " + currentPrice);
//        System.out.println("Old Price: " + oldPrice);

        arrEntry.add(new Entry(1, Float.parseFloat(formattedOld90d)));
        arrEntry.add(new Entry(2, Float.parseFloat(formattedOld60d)));
        arrEntry.add(new Entry(3, Float.parseFloat(formattedOld30d)));
        arrEntry.add(new Entry(4, Float.parseFloat(formattedOld7d)));
        arrEntry.add(new Entry(5, Float.parseFloat(formattedOld24h)));
        arrEntry.add(new Entry(6, Float.parseFloat(formattedOld1h)));
        arrEntry.add(new Entry(7, Float.parseFloat(formattedCurrent)));

        LineDataSet lineDataSet = new LineDataSet(arrEntry, selectedCrypto.cyptoName + " 90 Days Status as of " + formattedDate);
        lineDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(15f);

        LineData lineData = new LineData(lineDataSet);
        binding.barChart.setData(lineData);
        binding.barChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
        binding.barChart.getAxisRight().setEnabled(false);
        binding.barChart.setDrawBorders(true);
        binding.barChart.getDescription().setText(selectedCrypto.nameSymbol);
        binding.barChart.animateY(1000);
        binding.barChart.invalidate();

        binding.buyBtn.setText("Buy " + selectedCrypto.nameSymbol);
        binding.sellBtn.setText("Sell " + selectedCrypto.nameSymbol);

    }

    private void handleCryptoChange(double changeValue) {
        if (changeValue < 0) {
            binding.changePercent.setTextColor(Color.RED);
            binding.negChange.setVisibility(View.VISIBLE);
            binding.posChange.setVisibility(View.GONE);
        } else {
            binding.changePercent.setTextColor(Color.parseColor("#00FF2A"));
            binding.negChange.setVisibility(View.GONE);
            binding.posChange.setVisibility(View.VISIBLE);
        }
        binding.changePercent.setText(String.format("%.2f", changeValue)+"%");
    }

//    private void handleChartChange(CryptoModal selectedCrypto, double changeValue) {
//        float currentPrice = Float.parseFloat(String.valueOf(selectedCrypto.price));
//        float changePercent = Float.parseFloat(String.valueOf(changeValue));
//        float oldPrice = currentPrice + (((changePercent*-1)/100)*currentPrice);
//
//        String formattedOldString = String.format("%.2f", oldPrice);
//        String formattedCurrentString = String.format("%.2f", currentPrice);
//
//        arrEntry.clear();
//        arrEntry.add(new Entry(1, Float.parseFloat(formattedOldString)));
//        arrEntry.add(new Entry(2, Float.parseFloat(formattedOldString)));
//        arrEntry.add(new Entry(3, Float.parseFloat(formattedCurrentString)));
//        arrEntry.add(new Entry(4, Float.parseFloat(formattedCurrentString)));
//
//        LineDataSet lineDataSet = new LineDataSet(arrEntry, selectedCrypto.cyptoName + " 7 Days Status");
//        lineDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//        lineDataSet.setValueTextColor(Color.BLACK);
//        lineDataSet.setValueTextSize(15f);
//
//        LineData lineData = new LineData(lineDataSet);
//        binding.barChart.setData(lineData);
//        binding.barChart.getDescription().setText(selectedCrypto.nameSymbol);
//        binding.barChart.invalidate(); //refreshing the chart
//    }

}