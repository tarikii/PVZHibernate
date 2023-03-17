/**
 * Paquete que contiene todas las clases con las que vamos a trabajar
 */
package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Esta clase representara todos los characters que vayamos metiendo en la tabla characters de nuestra
 * base de datos
 *
 @author tarikii
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "characters")
public class Character implements Serializable {

  /**
   * El identificador del character
   *
   */
  @Id
  @Column(name = "id_character")
  int characterId;

  /**
   * El identificador del tipo de character
   *
   */
  @Column(name = "id_character_type")
  int characterTypeId;

  /**
   * El identificador del weapon
   *
   */
  @Column(name = "id_weapon")
  int weaponId;

  /**
   * El nombre del character
   *
   */
  @Column(name = "name")
  String name;

  /**
   * La imagen del character
   *
   */
  @Column(name = "image")
  String image;

  /**
   * La vida del character
   *
   */
  @Column(name = "health")
  String health;

  /**
   * La variant del character
   *
   */
  @Column(name = "variant")
  String variant;

  /**
   * Las habilidades del character
   *
   */
  @Column(name = "abilities")
  String abilities;

  /**
   * La clase del character en el juego
   *
   */
  @Column(name = "FPSClass")
  String FPSClass;

  /**
   * La lista de weapons asociada con un character
   *
   */
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_weapon", referencedColumnName = "id_weapon")
  private List<Weapon> weapons = new ArrayList<Weapon>();


  /**
   * Construye un character nuevo con una serie de atributos
   *
   @param characterId El identificador del character
   @param characterTypeId El identificador del tipo del character, Plant o Zombie
   @param weaponId El identificador del weapon que posee el character
   @param name El nombre del character
   @param image El archivo que contiene una imagen del character
   @param health La vida del character
   @param variant La variante del character
   @param abilities La habilidad/es del character.
   @param FPSClass La clase que rolea el character en el juego
   */
  public Character(int characterId, int characterTypeId, int weaponId, String name, String image, String health, String variant, String abilities, String FPSClass) {
    super();
    this.characterId = characterId;
    this.characterTypeId = characterTypeId;
    this.weaponId = weaponId;
    this.name = name;
    this.image = image;
    this.health = health;
    this.variant = variant;
    this.abilities = abilities;
    this.FPSClass = FPSClass;
  }

  /**
   * Nos da un constructor vacio del character, que contiene atributos por defecto y le pasamos la clase Object
   *
   */
  public Character() {
    super();
  }

  /**
   * Devuelve la ID de un character
   *
   @return Su identificador
   */
  public int getCharacterId() {
    return characterId;
  }

  /**
   * Editamos el identificador del character
   *
   * @param characterId Le pasamos la nueva ID
   */
  public void setCharacterId(int characterId) {
    this.characterId = characterId;
  }

  /**
   * Nos devuelve el identificador del tipo
   *
   * @return ID del tipo
   */
  public int getCharacterTypeId() {
    return characterTypeId;
  }

  /**
   * Edita el identificador del tipo de character
   *
   @param characterTypeId Su nuevo tipo
   */
  public void setCharacterTypeId(int characterTypeId) {
    this.characterTypeId = characterTypeId;
  }

  /**
   * Nos devuelve el identificador del weapon
   *
   * @return El ID del weapon
   */
  public int getWeaponId() {
    return weaponId;
  }

  /**
   * Edita el identificador del weapon de character
   *
   @param weaponId Su nuevo weapon
   */
  public void setWeaponId(int weaponId) {
    this.weaponId = weaponId;
  }

  /**
   * Nos da el nombre del character
   *
   * @return devuelve el nombre del character
   */
  public String getName() {
    return name;
  }

  /**
   * Edita el nombre del character
   *
   @param name Su nuevo nombre
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Nos devuelve la imagen del character
   *
   * @return la imagen
   */
  public String getImage() {
    return image;
  }

  /**
   * Edita la imagen del character
   *
   @param image Su nueva imagen
   */
  public void setImage(String image) {
    this.image = image;
  }

  /**
   * Nos devuelve los puntos de vida del character
   *
   * @return la vida
   */
  public String getHealth() {
    return health;
  }

  /**
   * Edita los puntos de vida del character
   *
   @param health Su nueva vida
   */
  public void setHealth(String health) {
    this.health = health;
  }

  /**
   * Nos devuelve el variant del character
   *
   * @return la variant
   */
  public String getVariant() {
    return variant;
  }

  /**
   * Edita el variant del character
   *
   @param variant Su nueva variant
   */
  public void setVariant(String variant) {
    this.variant = variant;
  }

  /**
   * Nos da las habilidades del character
   *
   * @return devuelve las habilidades
   */
  public String getAbilities() {
    return abilities;
  }

  /**
   * Edita la habilidad/es del character
   *
   @param abilities Su nueva/s habilidad/es
   */
  public void setAbilities(String abilities) {
    this.abilities = abilities;
  }

  /**
   * Nos devuelve la clase del character
   *
   * @return nos devuelve la clase
   */
  public String getFPSClass() {
    return FPSClass;
  }

  /**
   * Nos devuelve la lista de weapons de un character
   *
   * @return Nos devuelve la lista de wepaons
   */
  public List<Weapon> getWeapons() {
    return weapons;
  }

  /**
   * Editamos la lista de weapons
   *
   * @param weapons la nueva lista que contiene el character
   */
  public void setWeapons(List<Weapon> weapons) {
    this.weapons = weapons;
  }

  /**
   * Edita la clase del character en el juego
   *
   @param FPSClass Su nueva clase en el juego
   */



  public void setFPSClass(String FPSClass) {
    this.FPSClass = FPSClass;
  }

  /**
   * Devuelve una representacion del character en forma de String
   *
   * @return devuelve el string del character con sus atributos
   */
  @Override
  public String toString() {
    return "Character{" +
            "characterId=" + characterId +
            ", characterTypeId=" + characterTypeId +
            ", weaponId=" + weaponId +
            ", name='" + name + '\'' +
            ", image='" + image + '\'' +
            ", health='" + health + '\'' +
            ", variant='" + variant + '\'' +
            ", abilities='" + abilities + '\'' +
            ", FPSClass='" + FPSClass + '\'' +
            '}';
  }
}