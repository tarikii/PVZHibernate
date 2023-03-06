package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Access(AccessType.FIELD)
@Table(name = "characters")
public class Character implements Serializable {
  @Id
  @Column(name = "id_character")
  int characterId;
  @Column(name = "id_character_type")
  int characterTypeId;
  @Column(name = "id_weapon")
  int weaponId;
  @Column(name = "name")
  String name;
  @Column(name = "image")
  String image;
  @Column(name = "health")
  String health;
  @Column(name = "variant")
  String variant;
  @Column(name = "abilities")
  String abilities;
  @Column(name = "FPSClass")
  String FPSClass;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_weapon", referencedColumnName = "id_weapon")
  private List<Weapon> weapons = new ArrayList<Weapon>();


  public Character(int articleId, int characterTypeId, int weaponId, String name, String image, String health, String variant, String abilities, String FPSClass) {
    super();
    this.characterId = articleId;
    this.characterTypeId = characterTypeId;
    this.weaponId = weaponId;
    this.name = name;
    this.image = image;
    this.health = health;
    this.variant = variant;
    this.abilities = abilities;
    this.FPSClass = FPSClass;
  }

  public Character() {
    super();

  }

  public int getCharacterId() {
    return characterId;
  }

  public void setCharacterId(int characterId) {
    this.characterId = characterId;
  }

  public int getCharacterTypeId() {
    return characterTypeId;
  }

  public void setCharacterTypeId(int characterTypeId) {
    this.characterTypeId = characterTypeId;
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

  public void addWeapon(Weapon weapon){
    weapons.add(weapon);
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getHealth() {
    return health;
  }

  public void setHealth(String health) {
    this.health = health;
  }

  public String getVariant() {
    return variant;
  }

  public void setVariant(String variant) {
    this.variant = variant;
  }

  public String getAbilities() {
    return abilities;
  }

  public void setAbilities(String abilities) {
    this.abilities = abilities;
  }

  public String getFPSClass() {
    return FPSClass;
  }

  public void setFPSClass(String FPSClass) {
    this.FPSClass = FPSClass;
  }

  public List<Weapon> getWeapons() {
    return weapons;
  }

  public void setWeapons(List<Weapon> weapons) {
    this.weapons = weapons;
  }

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
            ", weapons=" + weapons.toString() +
            '}';
  }
}
