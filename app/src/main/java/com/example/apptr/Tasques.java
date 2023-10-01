package com.example.apptr;

public class Tasques {


    String titoltasca;
    String desctasca;
    String datatasca;
    String keytasca;


    public Tasques(){

    }

    public Tasques(String titoltasca, String desctasca, String datatasca, String keytasca) {
        this.titoltasca = titoltasca;
        this.desctasca = desctasca;
        this.datatasca = datatasca;
        this.keytasca = keytasca;
    }

    public String getKeytasca() {
        return keytasca;
    }

    public void setKeytasca(String keytasca) {
        this.keytasca = keytasca;
    }

    public String getTitoltasca() {
        return titoltasca;
    }

    public void setTitoltasca(String titoltasca) {
        this.titoltasca = titoltasca;
    }

    public String getDesctasca() {
        return desctasca;
    }

    public void setDesctasca(String desctasca) {
        this.desctasca = desctasca;
    }

    public String getDatatasca() {
        return datatasca;
    }

    public void setDatatasca(String datatasca) {
        this.datatasca = datatasca;
    }


}