package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Access(AccessType.FIELD)
@Table(name = "character_type" +
        "")
public class CharacterType implements Serializable {
  @Id
  @Column(name = "id_character_type")
  private int characterTypeId;
  @Column(name = "name_type", length = 3000)
  private String nameType;

  public CharacterType(int characterTypeId, String nameType) {
    super();
    this.nameType = nameType;
    this.characterTypeId = characterTypeId;
  }

  public CharacterType() {
    super();
  }

  public int getCharacterTypeId() {
    return characterTypeId;
  }

  public void setCharacterTypeId(int characterTypeId) {
    this.characterTypeId = characterTypeId;
  }

  public String getNameType() {
    return nameType;
  }

  public void setNameType(String nameType) {
    this.nameType = nameType;
  }

  @Override
  public String toString() {
    return "CharacterType{" +
            "characterTypeId=" + characterTypeId +
            ", nameType='" + nameType + '\'' +
            '}';
  }

}
