package controller;

import model.Character;
import model.CharacterType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

public class CharacterTypeController {

  private Connection connection;
  private EntityManagerFactory entityManagerFactory;

  public CharacterTypeController(Connection connection) {
    this.connection = connection;
  }

  public CharacterTypeController(Connection connection, EntityManagerFactory entityManagerFactory) {
    this.connection = connection;
    this.entityManagerFactory = entityManagerFactory;
  }

  /**
   * @param filename Aquest String correspon amb l'arxiu on s'emmagatzemen les
   *                 dades de les instancies de CharacterType
   * @throws IOException <dt><b>Preconditions:</b>
   *                     <dd>
   *                     filename<>nil </br> llistaCharacterType== nil
   *                     <dt><b>Postconditions:</b>
   *                     <dd>
   *                     llistaCharacterType<>nil
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

  public void printCharacterType(ArrayList<CharacterType> characterTypeList) {
    for (int i = 0; i < characterTypeList.size(); i++) {
      System.out.println(characterTypeList.get(i).toString());
    }
  }

  /* Method to CREATE a CharacterType  in the database */
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

  /* Method to READ all CharacterType */
  public void listCharacterType() {
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

  /* Method to UPDATE activity for a CharacterType */
  public void updateCharacterType(Integer characterTypeId) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    CharacterType characterType = (CharacterType) em.find(CharacterType.class, characterTypeId);
    em.merge(characterType);
    em.getTransaction().commit();
    em.close();
  }

  /* Method to DELETE a CharacterType from the records */
  public void deleteCharacterType(Integer characterTypeId) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    CharacterType characterType = (CharacterType) em.find(CharacterType.class, characterTypeId);
    em.remove(characterType);
    em.getTransaction().commit();
    em.close();
  }
}
