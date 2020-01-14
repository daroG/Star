package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


//        showStars();

        menu();
    }

    public static void addNewStar(){
        DatabaseManager databaseManager = new DatabaseManager();

        Star star = StarService.getNewStar();
        if (databaseManager.insertStar(star) == 1){
            System.out.println("Dodano gwiazdę");
        }

        databaseManager.close();

        menu();
    }

    public static void showConstellations(){
        DatabaseManager databaseManager = new DatabaseManager();

        List<String> list = databaseManager.getConstellations();
        for (String s : list){
            System.out.println(s);
        }

        databaseManager.close();
    }

    public static void showStars(){

        DatabaseManager databaseManager = new DatabaseManager();

        List<Star> list = databaseManager.getStarsInHemisphere(Star.Hemisphere.PN);
        System.out.println(list.size());
        for (Star s : list){
            System.out.println(s.present());
        }

        databaseManager.close();

    }

    public static void showMenu(){
        System.out.println("Wybierz co chcesz zrobić:");
        System.out.println("1 - Pokaż gwiazdy");
        System.out.println("2 - Dodaj gwiazdę");
        System.out.println("3 - Usuń gwiazdę");
        System.out.println("4 - Wyjdź");
    }

    public static void menu(){

        Scanner scanner = new Scanner(System.in);
        showMenu();
        int i = scanner.nextInt();

        switch (i){
            case 1:
                submenu();
                break;
            case 2:
                addNewStar();
                break;
            case 3:
                deleteStar();
                break;
            case 4:
                System.exit(0);
                break;
        }


    }

    private static void deleteStar() {
        Scanner scanner = new Scanner(System.in);
        DatabaseManager databaseManager = new DatabaseManager();
        List<Star> stars = databaseManager.getAllStars();
        System.out.println("Wybierz gwiazdę do usunięcia: ");
        for (int i = 0; i < stars.size(); i++){
            System.out.println("" + stars.get(i).getId() + " - " + stars.get(i).getName() + " " + stars.get(i).getCatalogName());
        }

        if(databaseManager.deleteStarWithId(scanner.nextInt()) == 1){
            System.out.println("Usunuęto");
        }else{
            System.out.println("Nie usunięto");
        }

        menu();

    }

    public static void showSubMenu(){
        System.out.println("Wybierz opcję wyświetlania gwiazd:");
        System.out.println("1 - Wszystkie gwiazdy");
        System.out.println("2 - Gwiazdy z danego gwiazdozbioru");
        System.out.println("3 - Gwiazdy na  półkuli");
        System.out.println("4 - Gwiazdy w podanej odległości");
        System.out.println("5 - Gwiazdy, które mają temperaturę w podanym zakresie");
        System.out.println("6 - Gwiazdy, które mają obserwowaną wielkość gwiazdową w podanym zakresie");
        System.out.println("7 - Gwiazdy, które mogą być supernowymi");
    }

    public static void submenu(){
        showSubMenu();
        Scanner scanner = new Scanner(System.in);
        DatabaseManager databaseManager = new DatabaseManager();
        List<Star> list = new ArrayList<>();

        double min, max;


        switch (scanner.nextInt()){
            case 1:
                list = databaseManager.getAllStars();
                break;
            case 2:
                System.out.println("Wybierz konstelajcę:");
                List<String> constellations = databaseManager.getConstellations();
                for (int i = 1; i <= constellations.size(); i++){
                    System.out.println("" + i + " - " + constellations.get(i-1));
                }
                list = databaseManager.getStarsFromConstellation(constellations.get(scanner.nextInt() - 1));
                break;
            case 3:
                System.out.println("1 - Półkula północna");
                System.out.println("2 - Półkula południowa");
                switch (scanner.nextInt()){
                    case 1:
                        list = databaseManager.getStarsInHemisphere(Star.Hemisphere.PN);
                        break;
                    case 2:
                        list = databaseManager.getStarsInHemisphere(Star.Hemisphere.PD);
                        break;
                }
                break;
            case 4:
                System.out.print("Podaj odległość w parsekach: ");
                list = databaseManager.getStarsInDistance(scanner.nextDouble());
                break;
            case 5:
                System.out.print("Podaj dolną granicę temperatury: ");
                min = scanner.nextDouble();
                System.out.print("\nPodaj górną granicę temperatury: ");
                 max = scanner.nextDouble();
                list = databaseManager.getStarsWithTempBetween(min, max);
                break;
            case 6:
                System.out.print("Podaj dolną granicę obserwowanej wielkości gwiazdowej: ");
                min = scanner.nextDouble();
                System.out.print("\nPodaj górną granicę obserwowanej wielkości gwiazdowej: ");
                max = scanner.nextDouble();
                list = databaseManager.getStarsWithMagnitudeBetween(min, max);
                break;
            case 7:
                list = databaseManager.getSupernovas();
                break;
        }

        System.out.println("Liczba gwiazd: " + list.size());
        System.out.println();
        System.out.println("nazwa\tnazwa katalogowa\tdeklinacja\trektascencja\tob.wielkość gwiazdowa\tabs. wielkość gwiazdowa\todległość\tgwiazdozbiór\tpółkula\ttemperatura\tmasa");
        for (Star s : list){
            System.out.println(s.present());
        }

        System.out.println("\n");
        menu();
    }


}
