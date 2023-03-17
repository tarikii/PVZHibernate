package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representara todos los weapons que vayamos metiendo en la tabla weapons de nuestra base de datos
 *
 @author tarikii
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "weapons")
public class Weapon implements Serializable {

  /**
   * El identificador del weapon
   *
   */
  @Id
  @Column(name = "id_weapon")
  int weaponId;

  /**
   * El nombre del weapon
   *
   */
  @Column(name = "name", length = 3000)
  String name;

  /**
   * El daño del weapon
   *
   */
  @Column(name = "damage")
  int damage;

  /**

   Construye un nuevo weapon con sus atributos especificos.
   @param weaponId El identificador del weapon
   @param name Su nombre
   @param damage El daño que hace
   */
  public Weapon(int weaponId, String name, int damage) {
    super();
    this.damage = damage;
    this.name = name;
    this.weaponId = weaponId;
  }

  /**
   * Un constructor vacio de weapon
   *
   */
  public Weapon() {

  }

  /**
   * Obtenemos el identificador del weapon
   *
   * @return Devuelve el identificador del weapon
   */
  public int getWeaponId() {
    return weaponId;
  }

  /**
   * Editamos el identificador del weapon
   *
   * @param weaponId devolvemos la ID del weapon
   */
  public void setWeaponId(int weaponId) {
    this.weaponId = weaponId;
  }

  /**
   * Devuelve el nombre del weapon
   *
   * @return el nombre del weapon
   */
  public String getName() {
    return name;
  }

  /**
   * Editamos el nombre del weapon
   *
   * @param name Le pasamos el nuevo nombre del weapon
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Nos devuelve el daño del weapon
   *
   * @return devuelve el daño
   */
  public int getDamage() {
    return damage;
  }

  /**
   * Editamos el damage del weapon
   *
   * @param damage Le pasamos el nuevo daño del weapon
   */
  public void setDamage(int damage) {
    this.damage = damage;
  }

  /**
   * Devuelve una representacion del weapon en forma de String
   *
   * @return devuelve el string del weapon con sus atributos
   */
  @Override
  public String toString() {
    return "Weapon{" +
            "weaponId=" + weaponId +
            ", name='" + name + '\'' +
            ", damage=" + damage +
            '}';
  }
}