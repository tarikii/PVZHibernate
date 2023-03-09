package controller;

import model.Character;
import model.Weapon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Esta clase es el controlador de weapon, que nos ayuda a interactuar con la tabla weapons.
 *
 * @author tarikii
 */
public class WeaponController {

  private Connection connection;
  private EntityManagerFactory entityManagerFactory;

  /**
   * Un constructor del controlador que nos ayudara a conectarnos en la base de datos
   *
   * @param connection Le pasamos la conexion de la base de datos
   */
  public WeaponController(Connection connection) {
    this.connection = connection;
  }

  /**
   * Creamos una nueva instancia del controlador de weapon usando la conexion de la base de datos
   *
   * @param connection Le pasamos la conexion de la base de datos
   * @param entityManagerFactory Le pasamos tambien el Hibernate que hemos creado
   */
  public WeaponController(Connection connection, EntityManagerFactory entityManagerFactory) {
    this.connection = connection;
    this.entityManagerFactory = entityManagerFactory;
  }

  /**
   * Esta clase se encarga de leer el archivo CSV, y con este archivo rellenarnos toda la tabla de nuestra
   * base de datos con la informacion que saca del archivo.
   *
   * @param filename la ruta del archivo weapons que queremos leer
   * @return Una lista de weapons, que luego se meteran con ayuda de otros metodos
   * @throws IOException Devuelve este error si hay algun problema al leer los archivos
   */
  public List<Weapon> readWeaponsFile(String filename) throws IOException {
    int id;
    String name;
    int damage;
    List<Weapon> weaponsList = new ArrayList<Weapon>();

    BufferedReader br = new BufferedReader(new FileReader(filename));
    String linea = "";
    while ((linea = br.readLine()) != null) {
      StringTokenizer str = new StringTokenizer(linea, ",");
      id = Integer.parseInt(str.nextToken());
      name = str.nextToken();
      damage = Integer.parseInt(str.nextToken());
      // System.out.println(id + name + damage);
      weaponsList.add(new Weapon(id,name,damage));

    }
    br.close();

    return weaponsList;
  }


  /**
   * Añade un weapon (que procesamos con el csv) y lo mete en la base de datos
   *
   * @param weapon El weapon que queremos añadir
   */
  public void addWeapon(Weapon weapon) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    Weapon weaponExists = (Weapon) em.find(Weapon.class, weapon.getWeaponId());
    if (weaponExists == null ){
      System.out.println("inserting weapon...");
      em.persist(weapon);
    }
    em.getTransaction().commit();
    em.close();
  }


  /**
   * Lista todos los weapons
   */
  public void listAllWeapons() {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    List<Weapon> result = em.createQuery("from Weapon", Weapon.class)
        .getResultList();

    for (Weapon weapon : result) {
      System.out.println(weapon.toString());
    }
    em.getTransaction().commit();
    em.close();
  }

  /**
   * Ordena los weapons por su nombre y los lista
   *
   * @param weaponName El nombre del weapon que el usuario quiere buscar
   */
  public void listAllWeaponsByName(String weaponName) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    List<Weapon> result = em.createQuery("SELECT w FROM Weapon w WHERE LOWER(w.name) = LOWER(:name)", Weapon.class)
            .setParameter("name", weaponName.toLowerCase())
            .getResultList();

    for (Weapon weapon : result) {
      System.out.println(weapon.toString());
    }
    em.getTransaction().commit();
    em.close();
  }

  /**
   * Actualiza el daño del weapon que buscaras con su ID
   *
   * @param weaponId El ID del weapon que quieres actualizar
   * @param damage El daño nuevo que posee el weapon
   */
  public void updateWeapon(Integer weaponId, int damage) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    Weapon weapon = (Weapon) em.find(Weapon.class, weaponId);
    weapon.setDamage(damage);
    em.merge(weapon);
    em.getTransaction().commit();

    em.getTransaction().begin();
    weapon = em.find(Weapon.class, weaponId);
    System.out.println("Informacion del weapon despues de tu Update:");
    System.out.println(weapon.toString());
    em.getTransaction().commit();
    em.close();
  }


  /**
   * Crea la tabla weapons con ayuda del schema SQL
   *
   */
  public void createTableWeapons() {
    // crea un EntityManagerFactory utilizando la configuración definida en persistence.xml
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JPAMagazines");

    // obtiene un EntityManager a partir del EntityManagerFactory
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    // comienza una transacción
    entityManager.getTransaction().begin();

    // crea la tabla Weapons
    entityManager.createNativeQuery(
            "CREATE TABLE weapons ( " +
                    "  id_weapon serial NOT NULL, " +
                    "  name character varying(3000) NOT NULL, " +
                    "  damage integer NOT NULL, " +
                    "  CONSTRAINT pk_weapons PRIMARY KEY (id_weapon) " +
                    ")"
    ).executeUpdate();

    // finaliza la transacción
    entityManager.getTransaction().commit();

    // cierra el EntityManager y el EntityManagerFactory
    entityManager.close();
    entityManagerFactory.close();
  }

  /**
   * Inserta un nuevo weapon en la tabla weapons de la base de datos en base a lo que nos de el usuario
   * como informacion
   *
   * @param name El nombre que querra el usuario del weapon
   * @param damage El daño que posee este weapon
   @throws javax.persistence.PersistenceException Devuelve este error si no se ha podido añadir el weapon
   */
  public void createWeaponManually(String name, int damage) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    Weapon weapon = new Weapon();
    weapon.setName(name);
    weapon.setDamage(damage);
    em.persist(weapon);
    em.getTransaction().commit();
    em.close();
  }

  /**
   * Borra el weapon o los weapons que poseen el mismo nombre que pone nuestro usuario por pantalla
   *
   @param name El nombre del weapon a borrar
   @throws javax.persistence.PersistenceException Devuelve este error si ha habido un problema borrando
   */
  public void deleteWeaponByName(String name){
    String sql = "FROM Weapon WHERE name = :name";

    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    List<Weapon> result = em.createQuery(sql, Weapon.class)
            .setParameter("name", name)
            .getResultList();
    for (Weapon weapon : result) {
      em.remove(weapon);
    }
    em.getTransaction().commit();
    em.close();
  }

  /**
   * Dropea la tabla entera de weapons
   *
   @throws javax.persistence.PersistenceException Devuelve este error si hay un problema dropeando la tabla
   */
  public void dropTableWeapons() {
    // crea un EntityManagerFactory utilizando la configuración definida en persistence.xml
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JPAMagazines");

    // obtiene un EntityManager a partir del EntityManagerFactory
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    // comienza una transacción
    entityManager.getTransaction().begin();

    // dropea la tabla weapons
    entityManager.createNativeQuery("DROP TABLE weapons").executeUpdate();

    // finaliza la transacción
    entityManager.getTransaction().commit();

    // cierra el EntityManager y el EntityManagerFactory
    entityManager.close();
    entityManagerFactory.close();
  }

}