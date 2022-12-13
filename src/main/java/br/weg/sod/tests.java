package br.weg.sod;

import java.sql.Time;
import java.util.Date;

public class tests {

    public static void main(String[] args) {
        Time tempo = new Time(new Date().getTime());
        System.out.println(tempo.getTime());
    }
}
