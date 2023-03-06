package model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Access(AccessType.FIELD)
@Table(name = "weapons")
public class Weapon implements Serializable {
  @Id
  @Column(name = "id_weapon")
  int weaponId;
  @Column(name = "name", length = 3000)
  String name;
  @Column(name = "damage")
  int damage;

  public Weapon(int weaponId, String name, int damage) {
    super();
    this.damage = damage;
    this.name = name;
    this.weaponId = weaponId;
  }

  public Weapon() {

  }

  public int getWeaponId() {
    return weaponId;
  }

  public void setWeaponId(int weaponId) {
    this.weaponId = weaponId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getDamage() {
    return damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }


  @Override
  public String toString() {
    return "Weapon{" +
            "weaponId=" + weaponId +
            ", name='" + name + '\'' +
            ", damage=" + damage +
            '}';
  }
}
