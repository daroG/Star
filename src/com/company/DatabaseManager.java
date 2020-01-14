package com.company;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DatabaseManager {
    private Connection connection;

    /*
    konstrutor w którym następuje połączenie z bazą danych
     */
    public DatabaseManager()
    {
        try {
            String path = Paths.get(".").toAbsolutePath().normalize().toString().replace('\\', '/');
            String url = "jdbc:sqlite:" + path +"/gwiazdy.db";

            connection = DriverManager.getConnection(url);

            System.out.println("Succesfull");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    /*
    metoda dodająca gwiazdę do bazy danych

    @param Star star
    @return int - 0 nie udało się dodać gwiazdę, 1 - udało się dodać gwiazdę
     */
    public int insertStar(Star star){
        String sql = "INSERT INTO gwiazdy(nazwa, gwiazdozbior, deklinacja, rektascencja, obserwowanaWielkoscGwiazdowa, odleglosc, temperatura, masa) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        int ret = 0;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, star.getName());
            statement.setString(2, star.getConstellation());
            statement.setDouble(3, star.getDeclination());
            statement.setInt(4, star.getRightAscension());
            statement.setDouble(5, star.getObservedMagnitude());
            statement.setDouble(6, star.getDistance());
            statement.setDouble(7, star.getTemperature());
            statement.setDouble(8, star.getMass());

            ret = statement.executeUpdate();
        }catch (SQLException e){
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }
        return ret;
    }


    /*
    zwraca gwiady poprawnie zapisane w bazie

    @return List<Star>
     */
    private List<Star> getStars(String where){

        String[] greeks = new String[]{
                "alfa",
                "beta",
                "gamma",
                "delta",
                "epsilon",
                "dzeta",
                "eta",
                "theta",
                "jota",
                "kappa",
                "lambda",
                "my",
                "ny",
                "ksi",
                "omikron",
                "pi",
                "rho",
                "sigma",
                "tau",
                "ipsylon",
                "phi",
                "chi",
                "psi",
                "omega"
        };

        List<Star> starList = new ArrayList<>();

        String sql = "SELECT id, nazwa, gwiazdozbior, deklinacja, rektascencja, obserwowanaWielkoscGwiazdowa, odleglosc, temperatura, masa FROM gwiazdy " + where + " ORDER BY gwiazdozbior, obserwowanaWielkoscGwiazdowa ASC";

        //Pobranie z bazy wszystkich gwiazd
        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){


            String name, letter, constellation, lastConstellation = "";
            double declination, distance, magnitude, temperature, mass;
            int rightAscension, id, i = 0;
            //czytanie rekordów
            while (resultSet.next()){
                //tworzenie nowej gwiazdy na podstawie rekordów z bazy
                id = resultSet.getInt("id");
                name = resultSet.getString("nazwa");
                constellation = resultSet.getString("gwiazdozbior");
                declination = resultSet.getDouble("deklinacja");
                rightAscension = resultSet.getInt("rektascencja");
                distance = resultSet.getDouble("odleglosc");
                magnitude = resultSet.getDouble("obserwowanaWielkoscGwiazdowa");
                temperature = resultSet.getDouble("temperatura");
                mass = resultSet.getDouble("masa");

                if(lastConstellation.equals(constellation)){
                    letter = greeks[i%greeks.length];
                    i++;
                }else {
                    letter = greeks[0];
                    lastConstellation = constellation;
                    i = 1;
                }

                try {
                    Star star = new Star(id, name, constellation, declination, rightAscension, distance, magnitude, temperature, mass);
                    star.setCatalogName(letter);
                    starList.add(star);
                }catch (Exception e){
                    e.printStackTrace();
                    Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
                }
            }


        }catch (SQLException e){
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }

        starList.sort(new Comparator<Star>() {
            @Override
            public int compare(Star o1, Star o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return starList;
    }

    /*
    zwraca listę wszystkich gwiazd z bazy

    @return List<Star>
     */
    public List<Star> getAllStars(){
        return getStars("");
    }

    /*
    zwraca listę gwiazd w podanym gwiazdozbiorze

    @return List<Star>
     */
    public List<Star> getStarsFromConstellation(String constellation){
        return getStars("WHERE gwiazdozbior = '"+ constellation +"'");
    }


    /*
    zwraca listę gwiazd, króre mają odległość nie większą niż zadana

    @param double distance
    @return List<Star>
     */
    public List<Star> getStarsInDistance(double distance){
        // 1 parsek to 3.26 roku
        distance*=3.26;
        return getStars("WHERE odleglosc <= " + distance);
    }

    /*
    zwraca listę gwiazd, których temperatura mieści się w podanym przedziale

    @param double min
    @param double max
    @return List<Star>
     */
    public List<Star> getStarsWithTempBetween(double min, double max){
        return getStars("WHERE temperatura BETWEEN " + min + " AND " + max);
    }

    /*
    zwraca listę gwiazd, których obserwowana wielkość gwiazdowa mieści się z zadanym przedziale

    @param double min
    @param double max
    @return List<Star>
     */
    public List<Star> getStarsWithMagnitudeBetween(double min, double max){
        return getStars("WHERE obserwowanaWielkoscGwiazdowa BETWEEN " + min + " AND " + max);
    }

    /*
    zwraca listę gwizd, kóre znajdują się na zadanej półkuli

    @param Star.Hemisphere hemisphere;
    @return List<Star>
     */
    public List<Star> getStarsInHemisphere(Star.Hemisphere hemisphere){
        return getStars("").stream().filter(star -> star.getHemisphere() == hemisphere).collect(Collectors.toList());
    }

    /*
    zwraca listę gwiazd, które potencjalnie mogą być supernowymi

    @return List<Star>
     */
    public List<Star> getSupernovas(){
        return getStars("WHERE masa > 1.44");
    }

    /*
    zamyka połączenie z bazą
     */
    public void close(){

        try {
            connection.close();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }
    }

    /*
    zwraca listę gwiazdozbiorów

    @return List<String>
     */
    public List<String> getConstellations(){
        List<String> constellations = new ArrayList<>();

        String sql = "SELECT DISTINCT gwiazdozbior FROM gwiazdy";

        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
        ){
            while (resultSet.next()){
                constellations.add(resultSet.getString("gwiazdozbior"));
            }
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }

        return constellations;
    }

    /*

     */
    public int deleteStarWithId(int id){

        int deleted = 0;
        String sql = "DELETE FROM gwiazdy WHERE id = " + id;
        try {
            Statement statement = connection.createStatement();
            deleted = statement.executeUpdate(sql);
        }catch (SQLException e){
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }

        return deleted;
    }

}
