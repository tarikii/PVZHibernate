import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.CharacterController;
import controller.WeaponController;
import controller.CharacterTypeController;
import database.ConnectionFactory;
import model.*;
import model.Character;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import view.Menu;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * Esta clase basicamente nos da lo necesario para que el usuario pueda interactuar con nuestra base de datos
 * a partir de un menu que nosotros proveemos, el usuario escoge una opcion con un int, y nuestro programa
 * hara lo que la opcion del menu nos ha indicado
 *
 * @author tarikii
 */
public class Main {
  private static final Scanner scanner = new Scanner(System.in);

  static SessionFactory sessionFactoryObj;

  /**
   * Creamos un constructor vacio de Main (porque simplemente nos da un error en JavaDoc si no lo hacemos)
   *
   */
  public Main(){}
/*
  private static SessionFactory buildSessionFactory() {
    // Creating Configuration Instance & Passing Hibernate Configuration File
    Configuration configObj = new Configuration();
    configObj.configure("hibernate.cfg.xml");

    // Since Hibernate Version 4.x, ServiceRegistry Is Being Used
    ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build();

    // Creating Hibernate SessionFactory Instance
    sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
    return sessionFactoryObj;
  } */

  /**
   * Construye un Object Hibernate para que podamos empezar a interactuar con la base de datos.
   *
   * @return Nos devuelve el Hibernate que hemos construido con una clase SessionFactory.
   */
  private static SessionFactory buildSessionFactory() {
    try {
      StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
          .configure("hibernate.cfg.xml").build();
      Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
      return metadata.getSessionFactoryBuilder().build();

    } catch (HibernateException he) {
      System.out.println("Session Factory creation failure");
      throw he;
    }
  }

  /**
   * Crea un EntityManagerFactory para interactuar con el framework.
   *
   * @return Devuelve el EntityManagerFactory.
   */
  public static EntityManagerFactory createEntityManagerFactory(){
    EntityManagerFactory emf;
    try {
      emf = Persistence.createEntityManagerFactory("JPAMagazines");
    } catch (Throwable ex) {
      System.err.println("Failed to create EntityManagerFactory object."+ ex);
      throw new ExceptionInInitializerError(ex);
    }
    return emf;
  }

  /**
   * Aqui basicamente muestra el menu interactuable con el usuario, donde podremos toquetear la base de datos.
   *
   * @param args Los argumentos que le pasamos por consola (no se usa)
   */
  public static void main(String[] args) {
    boolean salirMenu = false;

    ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
    Connection c = connectionFactory.connect();

//    SessionFactory sessionFactory = buildSessionFactory();
    EntityManagerFactory entityManagerFactory = createEntityManagerFactory();
    //sessionObj = buildSessionFactory().openSession();

    //Creamos los 3 controladores que hemos creado para poder usar las tablas de la base de datos
    WeaponController weaponController = new WeaponController(c, entityManagerFactory);
    CharacterController characterController = new CharacterController(c, entityManagerFactory);
    CharacterTypeController characterTypeController = new CharacterTypeController(c, entityManagerFactory);

    Menu menu = new Menu();
    int opcio;


    //Nos saldra el menu infinitas veces, hasta que se presione la tecla 0 que nos cierra el programa.
    while(!salirMenu){
      opcio = menu.mainMenu();

      //Aqui se muestran todas las opciones del menu, cada opcion se encarga de lo que indicamos al usuario
      //por escrito en el menu
      switch (opcio) {

        case 1:

          try{
            characterTypeController.createTableCharacterTypes();
            weaponController.createTableWeapons();
            characterController.createTableCharacters();
          }catch (Exception e){
            System.out.println("Hay una o varias tablas que quieres crear que ya existen en la base de datos");
          }

          break;

        case 2:

          System.out.println("2!!");
          try {
            List<Weapon> weapons = weaponController.readWeaponsFile("src/main/resources/weapons.csv");
            for (Weapon w : weapons) {
              try {
                weaponController.addWeapon(w);
              } catch (Exception e) {
              }
            }


            List<CharacterType> characterTypes = characterTypeController.readCharacterTypeFile("src/main/resources/characterType.csv");
            for (CharacterType ct : characterTypes) {
              try {
                characterTypeController.addCharacterType(ct);
              } catch (Exception e) {
              }
            }

            List<Character> characters = characterController.readCharactersFile("src/main/resources/characters.csv", "src/main/resources/weapons.csv");
            for (Character ch : characters) {
              try {
                characterController.addCharacter(ch);
              } catch (Exception e) {
              }
            }


          } catch (NumberFormatException | IOException e) {

            e.printStackTrace();
          }

          break;

        case 3:
          characterController.listAllCharacters();
          break;

        case 4:
          weaponController.listAllWeapons();
          break;

        case 5:
          characterTypeController.listAllCharacterType();
          break;

        case 6:
          characterController.listAllPlantCharacters();
          break;

        case 7:
          characterController.listAllZombieCharacters();
          break;

        case 8:
          characterController.orderCharactersByName();
          break;

        case 9:
          System.out.println("Que weapon quieres buscar?");
          String weaponName = scanner.nextLine();

          try{
            weaponController.listAllWeaponsByName(weaponName);
          }catch (Exception e){
            System.out.println("No se ha encontrado ningun Weapon con el nombre que has proporcionado, intentalo de nuevo");
          }

          break;

        case 10:
          System.out.println("Que ID tiene el character que quieres cambiar? Del 1 al 30");
          int idCharacter = scanner.nextInt();
          scanner.nextLine();
          if(idCharacter >= 1 && idCharacter < 31){
            System.out.print("Escribe el nombre nuevo para el character que quieres modificar: ");
            String updateName = scanner.nextLine();

            characterController.updateCharacter(idCharacter,updateName);
          }
          else{
            System.out.println("La ID que estas intentando buscar no existe, recuerda que tiene que ser del 1 al 30!");
          }
          break;

        case 11:
          System.out.println("Que ID tiene el weapon que quieres cambiar? Del 1 al 30");
          int idWeapon = scanner.nextInt();
          scanner.nextLine();
          if(idWeapon >= 1 && idWeapon < 31){
            System.out.print("Escribe el daño nuevo para el weapon que quieres modificar: ");
            int updateDamage = scanner.nextInt();
            scanner.nextLine();

            weaponController.updateWeapon(idWeapon,updateDamage);
          }
          else{
            System.out.println("La ID que estas intentando buscar no existe, recuerda que tiene que ser del 1 al 30!");
          }
          break;

        case 12:
          System.out.println("Para crear un nuevo character, rellena este formulario");
          System.out.println();

          System.out.println("Que tipo de character es, 1 - Plant, 2 - Zombie");
          int newCharacterTypeId = scanner.nextInt();
          scanner.nextLine();
          if(newCharacterTypeId != 1 && newCharacterTypeId != 2){
            System.out.println("No puede ser otro numero, tiene que ser el 1 o el 2!");
          }
          else{
            System.out.println("Que arma quieres tener en este character, del 1 al 30");
            int newWeaponId = scanner.nextInt();
            scanner.nextLine();
            if(newWeaponId >= 1 && newWeaponId <= 30){
              System.out.println("Que nombre tiene este character?");
              String newCharacterName = scanner.nextLine();

              System.out.println("Inserta la imagen del character, por ejemplo: hola.jpg o hola.png");
              String newCharacterImage = scanner.nextLine();

              System.out.println("Cuanta vida tiene este character?");
              String newCharacterHealth = scanner.nextLine();

              System.out.println("Que tipo de variante es este character?");
              String newCharacterVariant = scanner.nextLine();

              System.out.println("Que tipo de habilidad/es tiene este character?");
              String newCharacterAbility = scanner.nextLine();

              System.out.println("Que tipo de clase es este character?");
              String newCharacterFPSClass = scanner.nextLine();

              characterController.createCharacterManually(newCharacterTypeId,newWeaponId,newCharacterName,
                      newCharacterImage,newCharacterHealth,newCharacterVariant,newCharacterAbility,
                      newCharacterFPSClass);
            }
            else{
              System.out.println("Esta arma no existe, debe de ser un arma del 1 al 30");
            }
          }

          break;

        case 13:
          System.out.println("Para crear un nuevo weapon, rellena este formulario");
          System.out.println();

          System.out.println("Que nombre quieres que tenga esta arma?");
          String newWeaponName = scanner.nextLine();

          System.out.println("Que daño quieres que haga esta nueva arma?");
          int newWeaponDamage = scanner.nextInt();
          scanner.nextLine();

          weaponController.createWeaponManually(newWeaponName,newWeaponDamage);

          break;


        case 14:
          System.out.print("Inserta la ID del weapon que quieres borrar: ");
          int deleteIdWeapon = scanner.nextInt();
          scanner.nextLine();

          weaponController.deleteWeaponByName(deleteIdWeapon);
          break;

        case 15:
          System.out.print("Inserta el nombre del character que quieres borrar: ");
          String deleteNameCharacter = scanner.nextLine();

          characterController.deleteCharacterByName(deleteNameCharacter);
          break;

        case 16:

          try{
            characterController.dropTableCharacters();
            weaponController.dropTableWeapons();
            characterTypeController.dropTableCharacterTypes();
          }catch (Exception e){
            System.out.println("Hay una o varias tablas que quieres borrar que no existen en la base de datos");
          }


          break;

        default:
          System.out.println("Acha luegor!!");
          salirMenu = true;
          //System.exit(1);
      }
    }
  }
}


/*


    static User userObj;
    static Session sessionObj;
    static SessionFactory sessionFactoryObj;

    private static SessionFactory buildSessionFactory() {
        // Creating Configuration Instance & Passing Hibernate Configuration File
        Configuration configObj = new Configuration();
        configObj.configure("hibernate.cfg.xml");

        // Since Hibernate Version 4.x, ServiceRegistry Is Being Used
        ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build();

        // Creating Hibernate SessionFactory Instance
        sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
        return sessionFactoryObj;
    }

    public static void main(String[] args) {
        System.out.println(".......Hibernate Maven Example.......\n");
        try {
            sessionObj = buildSessionFactory().openSession();
            sessionObj.beginTransaction();

            for(int i = 101; i <= 105; i++) {
                userObj = new User();
                userObj.setUserid(i);
                userObj.setUsername("Editor " + i);
                userObj.setCreatedBy("Administrator");
                userObj.setCreatedDate(new Date());

                sessionObj.save(userObj);
            }
            System.out.println("\n.......Records Saved Successfully To The Database.......\n");

            // Committing The Transactions To The Database
            sessionObj.getTransaction().commit();
        } catch(Exception sqlException) {
            if(null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
            sqlException.printStackTrace();
        } finally {
            if(sessionObj != null) {
                sessionObj.close();
            }
        }
    }

*/