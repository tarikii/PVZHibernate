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


public class Main {
  private static final Scanner scanner = new Scanner(System.in);

  static SessionFactory sessionFactoryObj;
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

  public static void main(String[] args) {
    boolean salirMenu = false;

    ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
    Connection c = connectionFactory.connect();

//    SessionFactory sessionFactory = buildSessionFactory();
    EntityManagerFactory entityManagerFactory = createEntityManagerFactory();
    //sessionObj = buildSessionFactory().openSession();


    WeaponController weaponController = new WeaponController(c, entityManagerFactory);
    CharacterController characterController = new CharacterController(c, entityManagerFactory);
    CharacterTypeController characterTypeController = new CharacterTypeController(c, entityManagerFactory);

    Menu menu = new Menu();
    int opcio;


    while(!salirMenu){
      opcio = menu.mainMenu();

      switch (opcio) {

        case 1:
          System.out.println("1!!");

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
          if(idWeapon >= 1 && idWeapon < 31){
            System.out.print("Escribe el daño nuevo para el weapon que quieres modificar: ");
            int updateDamage = scanner.nextInt();

            weaponController.updateWeapon(idWeapon,updateDamage);
          }
          else{
            System.out.println("La ID que estas intentando buscar no existe, recuerda que tiene que ser del 1 al 30!");
          }
          break;

        case 12:

          try{
            characterController.dropTableCharacters();
            weaponController.dropTableWeapons();
            characterTypeController.dropTableCharacterTypes();
          }catch (Exception e){
            System.out.println("Hay una o varias tablas que quieres borrar que no existen en la base de datos");
          }


          break;

        default:
          System.out.println("Adeu!!");
          System.exit(1);

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
