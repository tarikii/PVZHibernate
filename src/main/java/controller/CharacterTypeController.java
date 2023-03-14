/**
 * Paquete que contiene el controlador de las clases con las que podremos cambiar
 */
package controller;


import model.CharacterType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

/**
 * Esta clase es el controlador de character_type, que nos ayuda a interactuar con la tabla character_type.
 *
 * @author tarikii
 */
public class CharacterTypeController {

  private Connection connection;
  private EntityManagerFactory entityManagerFactory;

  /**
   * Un constructor del controlador que nos ayudara a conectarnos en la base de datos
   *
   * @param connection Le pasamos la conexion de la base de datos
   */
  public CharacterTypeController(Connection connection) {
    this.connection = connection;
  }

  /**
   * Creamos una nueva instancia del controlador de character_type usando la conexion de la base de datos
   *
   * @param connection Le pasamos la conexion de la base de datos
   * @param entityManagerFactory Le pasamos tambien el Hibernate que hemos creado
   */
  public CharacterTypeController(Connection connection, EntityManagerFactory entityManagerFactory) {
    this.connection = connection;
    this.entityManagerFactory = entityManagerFactory;
  }


  /**
   * Esta clase se encarga de leer el archivo CSV, y con este archivo rellenarnos toda la tabla de nuestra
   * base de datos con la informacion que saca del archivo.
   *
   * @param filename la ruta del archivo character_type que queremos leer
   * @return Una lista de character_type, que luego se meteran con ayuda de otros metodos
   * @throws IOException Devuelve este error si hay algun problema al leer los archivos
   */
  public List<CharacterType> readCharacterTypeFile(String filename) throws IOException {
    int characterTypeId;
    String name_type;
    List<CharacterType> characterTypeList = new ArrayList();

    BufferedReader br = new BufferedReader(new FileReader(filename));
    String linea = "";
    while ((linea = br.readLine()) != null) {
      StringTokenizer str = new StringTokenizer(linea, ",");
      characterTypeId = Integer.parseInt(str.nextToken());
      name_type = str.nextToken();

      characterTypeList.add(new CharacterType(characterTypeId, name_type));

    }
    br.close();
    return characterTypeList;
  }


  /**
   * Añade un character_type (que procesamos con el csv) y lo mete en la base de datos
   *
   * @param characterType El character_type que queremos añadir
   */
  public void addCharacterType(CharacterType characterType) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    CharacterType characterTypeExists = (CharacterType) em.find(CharacterType.class, characterType.getCharacterTypeId());
    if (characterTypeExists == null ){
      System.out.println("inserting character_type...");
      em.persist(characterType);
    }
    em.merge(characterType);
    em.getTransaction().commit();
    em.close();
  }

  /**
   * Ordena los character_type por su nombre y los lista
   */
  public void listAllCharacterType() {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    List<CharacterType> result = em.createQuery("from CharacterType", CharacterType.class)
        .getResultList();

    for (CharacterType characterType : result) {
      System.out.println(characterType.toString());
    }
    em.getTransaction().commit();
    em.close();
  }

  /**
   * Crea la tabla character_type con ayuda del schema SQL
   *
   */
  public void createTableCharacterTypes(){
      // crea un EntityManagerFactory utilizando la configuración definida en persistence.xml
      EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JPAMagazines");

      // obtiene un EntityManager a partir del EntityManagerFactory
      EntityManager entityManager = entityManagerFactory.createEntityManager();

      // comienza una transacción
      entityManager.getTransaction().begin();

      // crea la tabla Character_Type
      entityManager.createNativeQuery(
              "CREATE TABLE character_type ( " +
                      "  id_character_type serial NOT NULL, " +
                      "  name_type character varying(3000) NOT NULL, " +
                      "  CONSTRAINT pk_characters_types PRIMARY KEY (id_character_type) " +
                      ")"
      ).executeUpdate();

      // finaliza la transacción
      entityManager.getTransaction().commit();

      // cierra el EntityManager y el EntityManagerFactory
      entityManager.close();
      entityManagerFactory.close();
  }

  /**
   * Dropea la tabla entera de character_type
   *
   @throws javax.persistence.PersistenceException Devuelve este error si hay un problema dropeando la tabla
   */
  public void dropTableCharacterTypes() {
    // crea un EntityManagerFactory utilizando la configuración definida en persistence.xml
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JPAMagazines");

    // obtiene un EntityManager a partir del EntityManagerFactory
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    // comienza una transacción
    entityManager.getTransaction().begin();

    // dropea la tabla characters
    entityManager.createNativeQuery("DROP TABLE character_type").executeUpdate();

    // finaliza la transacción
    entityManager.getTransaction().commit();

    // cierra el EntityManager y el EntityManagerFactory
    entityManager.close();
    entityManagerFactory.close();
  }
}
