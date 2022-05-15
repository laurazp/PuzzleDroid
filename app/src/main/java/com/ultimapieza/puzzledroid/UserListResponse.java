package com.ultimapieza.puzzledroid;

import com.ultimapieza.puzzledroid.entidades.Players;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class UserListResponse {

    // POJO class to get the data from web api
    private String name;
    private int score;

    private String com_code;
    private String status;
    private String forgot;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();



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



    public String getCom_code() {
        return com_code;
    }

    public void setCom_code(String com_code) {
        this.com_code = com_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getForgot() {
        return forgot;
    }

    public void setForgot(String forgot) {
        this.forgot = forgot;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
