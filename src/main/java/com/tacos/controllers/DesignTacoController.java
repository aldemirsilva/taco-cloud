// java
package com.tacos.controllers;

import com.tacos.models.Ingredient;
import com.tacos.models.Ingredient.Type;
import com.tacos.models.Taco;
import com.tacos.models.TacoOrder;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;

/*
 * Controller responsible for handling requests under "/design".
 * @Slf4j: Lombok annotation to provide a logger (not used in methods here).
 * @Controller: marks this class as a Spring MVC controller.
 * @RequestMapping("/design"): base path for handler methods in this controller.
 * @SessionAttributes("tacoOrder"): keeps the "tacoOrder" model attribute in HTTP session
 *   across multiple requests (so items can be accumulated in an order).
 */
@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {

  /*
   * Adds ingredient lists (grouped by type) to the model for every request handled
   * by this controller. Because it has no name, attributes are added directly to the model map.
   * This runs before controller handler methods so the view has the data it needs.
   */
  @ModelAttribute
  public void addIngredientsToModel(Model model) {

    // Hard-coded list of available ingredients (id, name, type)
    List<Ingredient> ingredients = Arrays.asList(
        new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),
        new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),
        new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),
        new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN),
        new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES),
        new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),
        new Ingredient("CHED", "Cheddar Cheese", Ingredient.Type.CHEESE),
        new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE),
        new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE),
        new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE)
    );

    // Iterate over each Ingredient.Type and add a model attribute with the lower-case type name
    // Example: model attribute "wrap" contains all wrap ingredients
    Type[] types = Ingredient.Type.values();
    for (Type type : types) {
      model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
    }

  }

  /*
   * Creates and exposes a TacoOrder object in the model named "tacoOrder".
   * Because "tacoOrder" is listed in @SessionAttributes, this object will be kept in session
   * and shared across requests to build an order.
   */
  @ModelAttribute(name = "tacoOrder")
  public TacoOrder order() {
    return new TacoOrder();
  }

  /*
   * Creates and exposes a Taco object in the model named "taco".
   * Useful for form binding when the design page posts a new Taco.
   */
  @ModelAttribute(name = "taco")
  public Taco taco() {
    return new Taco();
  }

  /*
   * Handles GET requests to "/design" (because of class-level @RequestMapping).
   * Returns the logical view name "design" so Spring will render the corresponding template.
   */
  @GetMapping
  public String showDesignForm() {
    return "design";
  }

  /*
   * Helper method that filters the provided ingredient list by the given type
   * and returns the matching ingredients as a List (Iterable).
   */
  private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
    return ingredients
        .stream()
        .filter(x -> x.getType().equals(type))
        .collect(Collectors.toList());
  }

}
