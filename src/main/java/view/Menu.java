package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Menu {
    private int option;

    public Menu() {
        super();
    }

    public int mainMenu() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        do {

            System.out.println(" \nMENU PRINCIPAL \n");

            System.out.println("1. Crear TODAS las tablas.");
            System.out.println("2. Rellenar los datos de las tablas con los CSV.");
            System.out.println("3. Listar toda la tabla characters.");
            System.out.println("4. Listar toda la tabla weapons.");
            System.out.println("5. Listar toda la tabla character_type.");
            System.out.println("6. Listar la tabla characters que sean Plant.");
            System.out.println("7. Listar la tabla characters que sean Zombie.");
            System.out.println("8. Listar el nombre de los characters y ordenar por nombre");
            System.out.println("9. Listar la tabla weapons por el nombre del weapon.");
            System.out.println("10. Modificar un character");
            System.out.println("11. Modificar un weapon");
            System.out.println("12. Borrar TODAS las tablas.");

            System.out.println("0. Sortir. ");

            System.out.println("Esculli opció: ");
            try {
                option = Integer.parseInt(br.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("valor no vàlid");
                e.printStackTrace();
            }
        } while (option != 1  && option != 2 && option != 3 && option != 4 && option != 5 && option != 6
                && option != 7 && option != 8 && option != 9 && option != 10 && option != 11 && option != 12
                && option != 0);

        return option;
    }






}