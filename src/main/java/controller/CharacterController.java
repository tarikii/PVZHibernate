package controller;

import model.Character;
import model.Weapon;
import model.CharacterType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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

/**
 * Esta clase es el controlador del character, que nos ayuda a interactuar con la tabla characters.
 *
 * @author tarikii
 */
public class CharacterController {

  private Connection connection;
  private EntityManagerFactory entityManagerFactory;

  private CharacterTypeController characterTypeController = new CharacterTypeController(connection);
  private WeaponController weaponController = new WeaponController(connection);

  /**
   * Creamos una nueva instancia del controlador del character usando la conexion de la base de datos
   *
   * @param connection Le pasamos la conexion de la base de datos
   * @param entityManagerFactory Le pasamos tambien el Hibernate que hemos creado
   */
  public CharacterController(Connection connection, EntityManagerFactory entityManagerFactory) {
    this.connection = connection;
    this.entityManagerFactory = entityManagerFactory;
  }


  /**
   * Esta clase se encarga de leer el archivo CSV, y con este archivo rellenarnos toda la tabla de nuestra
   * base de datos con la informacion que saca del archivo.
   *
   * @param charactersFile la ruta del archivo characters que queremos leer
   * @param weaponsFile la ruta del archivo weapons que queremos leer
   * @return Una lista de characters, que luego se meteran con ayuda de otros metodos
   * @throws IOException Devuelve este error si hay algun problema al leer los archivos
   */
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

  /**
   * Añade un character (que procesamos con el csv) y lo mete en la base de datos
   *
   * @param character El character que queremos añadir
   */
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

  /**
   * Lista todos los characters de la base de datos
   */
  public void listAllCharacters() {
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

  /**
   * Lista todos los characters que sean Plant
   */
  public void listAllPlantCharacters() {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    List<String> result = em.createQuery("SELECT name FROM Character c WHERE c.characterTypeId = :type", String.class)
            .setParameter("type", 1)
            .getResultList();

    for (String names : result) {
      System.out.println("Plant Character Name: " + names);
    }
    em.getTransaction().commit();
    em.close();
  }

  /**
   * Lista todos los characters que sean Zombie
   */
  public void listAllZombieCharacters() {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    List<String> result = em.createQuery("SELECT name FROM Character c WHERE c.characterTypeId = :type", String.class)
            .setParameter("type", 2)
            .getResultList();

    for (String names : result) {
      System.out.println("Zombie Character Name: " + names);
    }
    em.getTransaction().commit();
    em.close();
  }

  /**
   * Ordena los characters por su nombre y los lista
   */
  public void orderCharactersByName() {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    List<String> result = em.createQuery("SELECT c.name FROM Character c ORDER BY c.name", String.class)
            .getResultList();

    for (String name : result) {
      System.out.println(name);
    }
    em.getTransaction().commit();
    em.close();
  }

  /**
   * Actualiza el nombre del character que buscaras con su ID
   *
   * @param characterId El ID del character que quieres actualizar
   * @param updateName El nombre nuevo que le quieres poner a tu character
   */
  public void updateCharacter(int characterId, String updateName) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    Character character = (Character) em.find(Character.class, characterId);
    character.setName(updateName);
    em.merge(character);
    em.getTransaction().commit();

    em.getTransaction().begin();
    character = em.find(Character.class, characterId);
    System.out.println("Informacion del character despues de tu Update:");
    System.out.println(character.toString());
    em.getTransaction().commit();

    em.close();
  }


  /**
   * Crea la tabla characters con ayuda del schema SQL
   *
   */
  public void createTableCharacters() {
    // crea un EntityManagerFactory utilizando la configuración definida en persistence.xml
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JPAMagazines");

    // obtiene un EntityManager a partir del EntityManagerFactory
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    // comienza una transacción
    entityManager.getTransaction().begin();

    // crea la tabla Characters
    entityManager.createNativeQuery(
            "CREATE TABLE characters ( " +
                    "  id_character serial NOT NULL, " +
                    "  id_character_type integer, " +
                    "  id_weapon integer NOT NULL, " +
                    "  name character varying(3000) NOT NULL, " +
                    "  image character varying(3000) NOT NULL, " +
                    "  health character varying(3000) NOT NULL, " +
                    "  variant character varying(3000) NOT NULL, " +
                    "  abilities character varying(3000) NOT NULL, " +
                    "  FPSClass character varying(3000) NOT NULL, " +
                    "  CONSTRAINT pk_characters PRIMARY KEY (id_character), " +
                    "  CONSTRAINT fk_characters_types FOREIGN KEY (id_character_type) " +
                    "      REFERENCES character_type (id_character_type) MATCH SIMPLE " +
                    "      ON UPDATE NO ACTION ON DELETE NO ACTION, " +
                    "  CONSTRAINT fk_characters_weapons FOREIGN KEY (id_weapon) " +
                    "      REFERENCES weapons (id_weapon) MATCH SIMPLE " +
                    "      ON UPDATE NO ACTION ON DELETE CASCADE " +
                    ")"
    ).executeUpdate();

    // finaliza la transacción
    entityManager.getTransaction().commit();

    // cierra el EntityManager y el EntityManagerFactory
    entityManager.close();
    entityManagerFactory.close();
  }


  /**
   * Inserta un nuevo character en la tabla characters de la base de datos en base a lo que nos de el usuario
   * como informacion
   *
   @param characterTypeId La ID del tipo
   @param weaponId La ID del weapon que posee el character
   @param name Su nombre
   @param image El archivo de la imagen
   @param health Su vida
   @param variant Su variante
   @param abilities Su habilidad/es
   @param fpsClass Su clase FPS
   @throws javax.persistence.PersistenceException Devuelve este error si no se ha podido añadir el character
   */
  public void createCharacterManually(int characterTypeId, int weaponId, String name, String image, String health, String variant, String abilities, String fpsClass) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    Character character = new Character();
    character.setCharacterTypeId(characterTypeId);
    character.setWeaponId(weaponId);
    character.setName(name);
    character.setImage(image);
    character.setHealth(health);
    character.setVariant(variant);
    character.setAbilities(abilities);
    character.setFPSClass(fpsClass);
    em.persist(character);
    em.getTransaction().commit();
    em.close();
  }

  /**
   * Borra el character o los characters que poseen el mismo nombre que pone nuestro usuario por pantalla
   *
   @param name El nombre del character a borrar
   @throws javax.persistence.PersistenceException Devuelve este error si ha habido un problema borrando
   */
  public void deleteCharacterByName(String name) {
    String sql = "FROM Character WHERE name = :name";
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    List<Character> result = em.createQuery(sql, Character.class)
            .setParameter("name", name)
            .getResultList();
    for (Character character : result) {
      List<Weapon> weapons = character.getWeapons();
      if (!weapons.isEmpty()) {
        Weapon weapon = weapons.get(0);
        character.setWeapons(Collections.emptyList());
        em.merge(character);
        em.remove(weapon);
      }
      em.remove(character);
    }
    try{
      em.getTransaction().commit();
    }catch (Exception e){
      System.out.println();
      System.out.println();
      System.out.println("------------------------------------------------------------------------------------------------");
      System.out.println("-------------------------------------WARNING----------------------------------------------------");
      System.out.println("PRIMERO DEBES BORRAR UN WEAPON, YA QUE DEPENDEN DE ESTE CAMPEON QUE ESTAS INTENTANDO BORRAR!!!!!");
      System.out.println("------------------------------------------------------------------------------------------------");
      System.out.println("------------------------------------------------------------------------------------------------");

    }
    em.close();
  }

  /**
   * Dropea la tabla entera de characters
   *
   @throws javax.persistence.PersistenceException Devuelve este error si hay un problema dropeando la tabla
   */
  public void dropTableCharacters() {
    // crea un EntityManagerFactory utilizando la configuración definida en persistence.xml
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JPAMagazines");

    // obtiene un EntityManager a partir del EntityManagerFactory
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    // comienza una transacción
    entityManager.getTransaction().begin();

    // dropea la tabla characters
    entityManager.createNativeQuery("DROP TABLE characters").executeUpdate();

    // finaliza la transacción
    entityManager.getTransaction().commit();

    // cierra el EntityManager y el EntityManagerFactory
    entityManager.close();
    entityManagerFactory.close();
  }
}