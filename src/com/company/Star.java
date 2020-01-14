package com.company;

import java.io.Serializable;

public class Star implements Serializable{


    private int id;
    private String name;
    private String catalogName;

    private double declination;

    private int rightAscension;

    private double observedMagnitude;

    private double distance;

    private String constellation;

    private double absoluteMagnitude;

    private Hemisphere hemisphere;

    private double temperature;

    private double mass;

    /*
    @return int
     */
    public int getId() {
        return id;
    }

    /*
    @return String
     */
    public String getName() {
        return name;
    }

    /*
    @param String name
     */
    public void setName(String name) throws Exception{

        boolean poprawne = true;

        //Nazwa musi się składać z 7 znaków
        if(name.length() != 7){
            poprawne = false;
        }else {

            //Pierwsze 3 to wielkie litery
            for (int i = 0; i<3; i++){
                if(!Character.isAlphabetic(name.charAt(i)) || !Character.isUpperCase(name.charAt(i)))
                    poprawne = false;
            }
            //Kolejne 4 to cyfry
            for (int i = 3; i<7; i++){
                if(!Character.isDigit(name.charAt(i)))
                    poprawne = false;
            }
        }

        //Jeśli nazwa spełnia warunki to ją ustawiamy, jeśli nie, to rzucamy wyjątek
        if(!poprawne){
            throw new Exception("Nieodpowiedni format nazwy");
        }

        this.name = name;


    }

    /*
    @return String
     */
    public String getCatalogName() {
        return catalogName;
    }

    /*
    @param String catalogName
     */
    public void setCatalogName(String letter) {

        this.catalogName = letter + " " + constellation;
    }

    /*
    @return double
     */
    public double getDeclination() {
        return declination;
    }

    /*
    @param double declination
     */
    public void setDeclination(double declination){

        double xx = declination/3600;

        if(xx >= -90 && xx < 0)
            hemisphere = Hemisphere.PD;
        else if(xx > 0 && xx <= 90)
            hemisphere = Hemisphere.PN;


        this.declination = declination;
    }

    /*
    @param int xx
    @param int yy
    @param int zz
     */
    public void setDeclination(int xx, int yy, double zz) {
        double declination = (xx<0) ? 3600*xx - 60*yy - zz : 3600 + 60*yy + zz;
        setDeclination(declination);
    }

    /*
    @param String declination
     */
    public void setDeclination(String declination) throws Exception{
        String[] splittedString = declination.split(" ");

        if(splittedString.length != 3){
            throw new Exception("Niepoprawny format deklinacji");
        }

        int xx = Integer.parseInt(splittedString[0]),
                yy = Integer.parseInt(splittedString[1]);
        double zz = Double.parseDouble(splittedString[2]);
        setDeclination(xx, yy, zz);
    }

    /*
    @return int
     */
    public int getRightAscension() {
        return rightAscension;
    }

    /*
    @param int rightAscension
     */
    public void setRightAscension(int rightAscension) throws Exception{
        //sprawdzenie poprawności zakresu rektascencji
        if(rightAscension < 0 || rightAscension > 3600*23 + 59*60 + 59)
            throw new Exception("Niepoprawny format rektascencji");

        this.rightAscension = rightAscension;
    }

    /*
    @param int xx
    @param int yy
    @param int zz
     */
    public void setRightAscension(int xx, int yy, int zz) throws Exception{

        int rektascencja = 3600*xx + 60*yy + zz;
        //Sprawdzamy czy podane wartości mieszczą się w dopuszczalnych wartościach dla czasu
        if( xx < 0 || xx > 24  || (xx == 24 && yy != 0 && zz != 0) || yy < 0 || yy >= 60 || zz < 0 || zz >= 60)
            throw new Exception("Niepoprawny format rektascencji");

        this.rightAscension = rektascencja;
    }


    /*
    Setter dla formatu "xx yy zz.zz"

    @param String rightAscension
     */
    public void setRightAscension(String rightAscension) throws Exception{

        String[] splittedString = rightAscension.split(" ");

        if(splittedString.length != 3){
            throw new Exception("Niepoprawny format rektascencji");
        }

        int xx = Integer.parseInt(splittedString[0]),
            yy = Integer.parseInt(splittedString[1]),
            zz = Integer.parseInt(splittedString[2]);
        setRightAscension(xx, yy, zz);



    }

    /*
    @return double
     */
    public double getObservedMagnitude() {
        return observedMagnitude;
    }

    /*
    @param double observedMagnitude
     */
    public void setObservedMagnitude(double observedMagnitude) throws Exception{

        if(observedMagnitude < -26.74 || observedMagnitude > 15){
            throw new Exception("Niepoprawna obserwowana wielkość gwiazdowa");
        }

        this.observedMagnitude = observedMagnitude;

        //M = m − 5· log10 r + 5
        this.absoluteMagnitude = observedMagnitude - 5*Math.log10(distance /3.26) + 5;
    }

    /*
    @return double
     */
    public double getDistance() {
        return distance;
    }

    /*
    @param double distance
     */
    public void setDistance(double distance) {
        this.distance = distance;

        this.absoluteMagnitude = observedMagnitude - 5*Math.log10(distance /3.26) + 5;
    }

    /*
    @return String
     */
    public String getConstellation() {
        return constellation;
    }

    /*
    @param String constellation
     */
    public void setConstellation(String constellation) {
        this.constellation = constellation;

        //TODO: Zmiana gwiazdozbioru = zmiana litery


    }

    /*
    @return double
     */
    public double getAbsoluteMagnitude() {
        return absoluteMagnitude;
    }

    /*
    @param double absoluteMagnitude
     */
    public void setAbsoluteMagnitude(double absoluteMagnitude) {
        this.absoluteMagnitude = absoluteMagnitude;

        observedMagnitude = absoluteMagnitude + 5*Math.log10(distance) - 5;
    }

    /*
    @return Hemisphere
     */
    public Hemisphere getHemisphere() {
        return hemisphere;
    }

    /*
    @return double
     */
    public double getTemperature() {
        return temperature;
    }

    /*
    @param double temperature
     */
    public void setTemperature(double temperature) throws Exception{

        if (temperature < 2000)
            throw new Exception("Niepoprawna wartość temperatury");

        this.temperature = temperature;
    }

    /*
    @return double
     */
    public double getMass() {
        return mass;
    }

    /*
    @param double mass
     */
    public void setMass(double mass) throws Exception{

        if(mass < 0.1 || mass > 50)
            throw new Exception("Niepoprawna wartość masy w odniesieniu do Słońca");

        this.mass = mass;
    }

    /*
    @param String name
    @param String catalogName
    @param double declination
    @param int rightAscension
    @param double observedMagnitude
    @param double distance
    @param double temperature
    @param double mass
     */
//    public Star(String name, String catalogName, double declination, int rightAscension, double observedMagnitude, double distance, double temperature, double mass) throws Exception{
//        setName(name);
//        setCatalogName(catalogName);
//        setDeclination(declination);
//        setRightAscension(rightAscension);
//        setDistance(distance);
//        setObservedMagnitude(observedMagnitude);
//        setMass(mass);
//        setTemperature(temperature);
//    }

    public Star(int id, String name, String constellation, double declination, int rightAscension, double distance, double observedMagnitude, double temperature, double mass) throws Exception{
        this.id = id;
        setName(name);
        setConstellation(constellation);
        setDeclination(declination);
        setRightAscension(rightAscension);
        setDistance(distance);
        setObservedMagnitude(observedMagnitude);
        setTemperature(temperature);
        setMass(mass);
    }

    public Star(){

    }

    public enum Hemisphere {
        PN, PD
    }

    public String present(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = new StringBuilder("");
        stringBuilder.append(getName());
        stringBuilder.append('\t');
        stringBuilder.append(getCatalogName());
        stringBuilder.append('\t');
        stringBuilder.append(getDeclination());
        stringBuilder.append('\t');
        stringBuilder.append(getRightAscension());
        stringBuilder.append('\t');
        stringBuilder.append(getObservedMagnitude());
        stringBuilder.append('\t');
        stringBuilder.append(getAbsoluteMagnitude());
        stringBuilder.append('\t');
        stringBuilder.append(getDistance());
        stringBuilder.append('\t');
        stringBuilder.append(getConstellation());
        stringBuilder.append('\t');
        stringBuilder.append(getHemisphere() == Star.Hemisphere.PD ? "Półkula PD" : "Półkula PN");
        stringBuilder.append('\t');
        stringBuilder.append(getTemperature());
        stringBuilder.append('\t');
        stringBuilder.append(getMass());
        return stringBuilder.toString();
    }

}
