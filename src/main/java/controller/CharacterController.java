package controller;

import model.Character;
import model.Weapon;
import model.CharacterType;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CharacterController {

  private Connection connection;
  private EntityManagerFactory entityManagerFactory;

  private CharacterTypeController characterTypeController = new CharacterTypeController(connection);
  private WeaponController weaponController = new WeaponController(connection);

  public CharacterController(Connection connection) {
    this.connection = connection;
  }

  public CharacterController(Connection connection, EntityManagerFactory entityManagerFactory) {
    this.connection = connection;
    this.entityManagerFactory = entityManagerFactory;
  }


  public List<Character> readCharactersFile(String charactersFile, String weaponsFile) throws IOException {
    // Lee el archivo de personajes
    List<String> characterLines = Files.readAllLines(Paths.get(charactersFile), StandardCharsets.UTF_8);
    // Lee el archivo de armas
    List<String> weaponLines = Files.readAllLines(Paths.get(weaponsFile), StandardCharsets.UTF_8);

    List<Character> characters = new ArrayList<Character>();

    // Crea un mapa para guardar las armas por ID
    Map<Integer, Weapon> weaponMap = new HashMap<Integer, Weapon>();
    for (String weaponLine : weaponLines) {
      // Separa la línea en campos
      String[] fields = weaponLine.split(",");
      // Crea un objeto de arma con los campos correspondientes
      Weapon weapon = new Weapon(Integer.parseInt(fields[0]), fields[1], Integer.parseInt(fields[2]));
      // Agrega el objeto de arma al mapa
      weaponMap.put(weapon.getWeaponId(), weapon);
    }

    // Crea los objetos de personaje con las armas correspondientes
    for (String characterLine : characterLines) {
      // Separa la línea en campos
      String[] fields = characterLine.split(",");
      // Crea un objeto de personaje con los campos correspondientes
      Character character = new Character(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), Integer.parseInt(fields[2]), fields[3], fields[4], fields[5], fields[6], fields[7], fields[8]);
      // Agrega el objeto de personaje a la lista
      characters.add(character);
    }

    return characters;
  }

  /* Method to CREATE a Character in the database */
  public void addCharacter(Character character) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    Character characterExists = (Character) em.find(Character.class, character.getCharacterId());
    if (characterExists == null ){
      System.out.println("inserting character...");
      em.persist(character);
    }
    em.merge(character);
    em.getTransaction().commit();
    em.close();
  }

  /* Method to READ all Characters */
  public void listCharacters() {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    List<Character> result = em.createQuery("from Character", Character.class)
        .getResultList();
    for (Character character : result) {
      System.out.println(character.toString());
    }
    em.getTransaction().commit();
    em.close();
  }

  /* Method to UPDATE activity for a Character */
  public void updateCharacter(Integer characterId) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    Character character = (Character) em.find(Character.class, characterId);
    em.merge(character);
    em.getTransaction().commit();
    em.close();
  }

  /* Method to DELETE a Character from the records */
  public void deleteCharacter(Integer characterId) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    Character character = (Character) em.find(Character.class, characterId);
    em.remove(character);
    em.getTransaction().commit();
    em.close();
  }



  public void createTableCharacters(Connection connection) throws SQLException {


    StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
    registryBuilder.configure();


    StandardServiceRegistry registry = registryBuilder.build();


    MetadataSources metadataSources = new MetadataSources(registry);


    metadataSources.addAnnotatedClass(Character.class);

    SchemaExport schemaExport = new SchemaExport();
    schemaExport.setDelimiter(";");
    schemaExport.setOutputFile("schema.sql");
    schemaExport.createOnly(EnumSet.of(TargetType.SCRIPT), metadataSources.buildMetadata());
  }

}