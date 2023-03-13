package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Esta clase nos ayuda a representar el tipo de los characteres y sus atributos
 *
 * @author tarikii
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "character_type" +
        "")
public class CharacterType implements Serializable {

  /**
   * El identificador del tipo del character
   *
   */
  @Id
  @Column(name = "id_character_type")
  private int characterTypeId;

  /**
   * El nombre del tipo, que tendra que ser Plant o Zombie
   *
   */
  @Column(name = "name_type", length = 3000)
  private String nameType;

  /**
   * Construye un nuevo tipo de character con su ID y su nombre
   *
   @param characterTypeId El identificador del tipo
   @param nameType Su nombre
   */
  public CharacterType(int characterTypeId, String nameType) {
    super();
    this.nameType = nameType;
    this.characterTypeId = characterTypeId;
  }

  /**
   * Un constructor vacio sin atributos ni parametros
   */
  public CharacterType(){}


  /**
   * Devuelve el identificador del tipo de character
   *
   @return La ID del tipo
   */
  public int getCharacterTypeId() {
    return characterTypeId;
  }

  /**
   * Editamos el identificador del tipo de character
   *
   * @param characterTypeId la nueva ID
   */
  public void setCharacterTypeId(int characterTypeId) {
    this.characterTypeId = characterTypeId;
  }

  /**
   * Nos devuelve el nombre del tipo de character
   *
   * @return el tipo del nombre
   */
  public String getNameType() {
    return nameType;
  }

  /**
   * Editamos el nombre del tipo del character
   *
   * @param nameType su nuevo nombre
   */
  public void setNameType(String nameType) {
    this.nameType = nameType;
  }

  /**
   * Devuelve una representacion del tipo character en forma de String
   *
   @return Todos sus atributos en forma de String
   */
  @Override
  public String toString() {
    return "CharacterType{" +
            "characterTypeId=" + characterTypeId +
            ", nameType='" + nameType + '\'' +
            '}';
  }
}