import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

          characterTypeController.createTableCharacterTypes();
          weaponController.createTableWeapons();
          characterController.createTableCharacters();
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

        case 4:

          try{
            characterController.dropTableCharacters();
            weaponController.dropTableWeapons();
            characterTypeController.dropTableCharacterTypes();
          }catch (Exception e){
            System.out.println("Hay una tabla que quieres borrar que no existe en la base de datos");
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
