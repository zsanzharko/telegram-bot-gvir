package kz.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Presentation card for get information.
 */
@Getter
@Setter
@AllArgsConstructor
public class GameCard {
  private String title;
  private String description;
  private Integer power;
}
