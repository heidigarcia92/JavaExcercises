package diet;

import java.util.Optional;

/**
 * Represent a recipe of the diet.
 * 
 * A recipe consists of a a set of ingredients that are given amounts of raw materials.
 * The overall nutritional values of a recipe can be computed
 * on the basis of the ingredients' values and are expressed per 100g
 * 
 *
 */
public class Recipe extends Element {
	
	private Food food;

	/**
	 * Recipe constructor.
	 * The reference {@code food} of type {@link Food} must be used to
	 * retrieve the information about ingredients. 
	 * 
	 * @param nome unique name of the recipe
	 * @param food object containing the information about ingredients
	 */
	public Recipe(String name, Food food){
		super(name, 0.0, 0.0, 0.0, 0.0);
		this.food = food;
		food.addRecipe(this);
	}
	
	/**
	 * Adds a given quantity of an ingredient to the recipe.
	 * The ingredient is a raw material defined with the {@code food}
	 * argument of the constructor.
	 * 
	 * @param material the name of the raw material to be used as ingredient
	 * @param quantity the amount in grams of the raw material to be used
	 */
	public void addIngredient(String material, double quantity) {

		Optional<NutritionalElement> element = Optional.of(food.getRawMaterial(material));

		if (!element.isPresent()) {
			throw new IllegalArgumentException(String.format("Unable to retrieve '%s' from '%s'.", material, food.getClass()));
		}

		calories += element.get().getCalories() * quantity;
		proteins += element.get().getProteins() * quantity;
		carbs += element.get().getCarbs() * quantity;
		fat += element.get().getFat() * quantity;
	}

	/**
	 * Indicates whether the nutritional values returned by the other methods
	 * refer to a conventional 100g quantity of nutritional element,
	 * or to a unit of element.
	 * 
	 * For the {@link Recipe} class it must always return {@code true}:
	 * a recipe expressed nutritional values per 100g
	 * 
	 * @return boolean indicator
	 */
	@Override
	public boolean per100g() {
	    // a recipe expressed nutritional values per 100g
		return true;
	}

}
