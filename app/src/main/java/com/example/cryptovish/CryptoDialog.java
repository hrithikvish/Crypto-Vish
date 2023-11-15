package com.example.cryptovish;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CryptoDialog extends AppCompatDialogFragment {

    Boolean isPriceClicked = false;
    Boolean isChangeClicked = false;
    String fullPriceString;
    String change1h, change24h, change7d, change30d, change60d, change90d;
    public CryptoDialog(String price) {
        this.fullPriceString = price;
        isPriceClicked = true;
    }
    public CryptoDialog(String change1h,String change24h,String change7d,String change30d,String change60d,String change90d) {
        this.change1h = change1h;
        this.change24h = change24h;
        this.change7d = change7d;
        this.change30d = change30d;
        this.change60d = change60d;
        this.change90d = change90d;
        isChangeClicked = true;
    }
    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(isPriceClicked) {
            builder.setMessage(fullPriceString);
            isPriceClicked = false;
        }
        if(isChangeClicked) {
            builder.setMessage("Change in 01h: " + change1h + "%\n" +
                    "Change in 24h: " + change24h + "%\n"+
                    "Change in 07d: " + change7d + "%\n"+
                    "Change in 30d: " + change30d + "%\n"+
                    "Change in 60d: " + change60d + "%\n"+
                    "Change in 90d: " + change90d + "%");
            isChangeClicked = false;
        }
        return builder.create();
    }
}
