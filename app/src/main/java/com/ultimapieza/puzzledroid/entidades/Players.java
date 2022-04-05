package com.ultimapieza.puzzledroid.entidades;

import java.util.Comparator;

public class Players {

    private String name;
    private int score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Comparador para ordenar los datos de jugadores por mayor puntuación
        public static Comparator<Players> playersComparator = new Comparator<Players>() {
        @Override
        public int compare(Players t1, Players t2) {
            return t2.getScore() - t1.getScore();
        }
    };

}
