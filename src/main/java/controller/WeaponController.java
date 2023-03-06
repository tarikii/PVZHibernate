package controller;

import model.Weapon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class WeaponController {

  private Connection connection;
  private EntityManagerFactory entityManagerFactory;

  public WeaponController(Connection connection) {
    this.connection = connection;
  }

  public WeaponController(Connection connection, EntityManagerFactory entityManagerFactory) {
    this.connection = connection;
    this.entityManagerFactory = entityManagerFactory;
  }

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

  public void printWeapons(ArrayList<Weapon> weaponsList) {
    for (int i = 0; i < weaponsList.size(); i++) {
      System.out.println(weaponsList.get(i).toString());
    }
  }


  /* Method to CREATE a Weapon in the database */
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


  /* Method to READ all Weapons */
  public void listWeapons() {
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

  /* Method to UPDATE activity for a weapon */
  public void updateWeapon(Integer weaponId, int damage) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    Weapon weapon = (Weapon) em.find(Weapon.class, weaponId);
    weapon.setDamage(damage);
    em.merge(weapon);
    em.getTransaction().commit();
    em.close();
  }

  /* Method to DELETE a Weapon from the records */
  public void deleteWeapon(Integer weaponId) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    Weapon weapon = (Weapon) em.find(Weapon.class, weaponId);
    em.remove(weapon);
    em.getTransaction().commit();
    em.close();
  }

}