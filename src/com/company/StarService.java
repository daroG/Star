package com.company;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StarService {

    /*
    metoda wczytująca dane od użytkownika

    @return Star
     */
    public static Star getNewStar(){

        Star star = new Star();

        Scanner scanner = new Scanner(System.in);

        boolean ok = true;

        //wczytywanie potrzebnej informacji do skutku
        //nazwa gwiazdy
        do {
            System.out.println("Podaj nazwę gwiazdy: ");
            try {
                star.setName(scanner.nextLine());
                ok = true;
            } catch (Exception e) {
                ok = false;
            }
        } while (!ok);

        //gwiazdozbiór
        do {
            System.out.println("Podaj nazwę gwiazdozbioru: ");
            try {
                star.setConstellation(scanner.nextLine());
                ok = true;
            } catch (Exception e) {
                ok = false;
            }
        } while (!ok);

        //deklinacja
        do {
            System.out.println("Podaj deklinację (hh mm ss.ss): ");
            try {
                star.setDeclination(scanner.nextLine());
                ok = true;
            } catch (Exception e) {
                Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
                ok = false;
            }
        } while (!ok);

        //rektascencja
        do {
            System.out.println("Podaj rektascencję (hh mm ss): ");
            try {
                star.setRightAscension(scanner.nextLine());
                ok = true;
            } catch (Exception e) {
                ok = false;
            }
        } while (!ok);

        //odleglość
        do {
            System.out.println("Podaj odleglość (lata świetlne): ");
            try {
                star.setDistance(scanner.nextDouble());
                ok = true;
            } catch (Exception e) {
                ok = false;
            }
        } while (!ok);

        //obserwowana wielkosc gwiazdowa
        do {
            System.out.println("Podaj obserwowaną wielkość gwiazdową (magnitudo): ");
            try {
                star.setObservedMagnitude(scanner.nextDouble());
                ok = true;
            } catch (Exception e) {
                ok = false;
            }
        } while (!ok);

        //temperatura
        do {
            System.out.println("Podaj temperaturę: ");
            try {
                star.setTemperature(scanner.nextDouble());
                ok = true;
            } catch (Exception e) {
                ok = false;
            }
        } while (!ok);


        //masa
        do {
            System.out.println("Podaj masę gwiazdy (w odniesieniu do Słońca): ");
            try {
                star.setMass(scanner.nextDouble());
                ok = true;
            } catch (Exception e) {
                ok = false;
            }
        } while (!ok);




        return star;
    }

}
