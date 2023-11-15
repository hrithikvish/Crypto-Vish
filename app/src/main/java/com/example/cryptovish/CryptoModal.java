package com.example.cryptovish;

import java.io.Serializable;

public class CryptoModal implements Serializable {
    String cyptoName, nameSymbol;
    Double price, change1h, change24h, change7d, change30d, change60d, change90d;

    public CryptoModal(String cryptoName, String nameSymbol, Double price, Double change1h, Double change24h, Double change7d, Double change30d, Double change60d, Double change90d) {
        this.cyptoName = cryptoName;
        this.nameSymbol = nameSymbol;
        this.price = price;
        this.change1h = change1h;
        this.change24h = change24h;
        this.change7d = change7d;
        this.change30d = change30d;
        this.change60d = change60d;
        this.change90d = change90d;
    }

}