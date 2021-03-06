package diet;

public abstract class Element implements NutritionalElement, Comparable<Element> {
    
    private String name;
    protected double calories;
    protected double proteins;
    protected double carbs;
    protected double fat;

    public Element(String name, double calories, double proteins, double carbs, double fat) {
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.carbs = carbs;
        this.fat = fat;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getCalories() {
        return calories;
    }

    @Override
    public double getCarbs() {
        return carbs;
    }

    @Override
    public double getFat() {
        return fat;
    }

    @Override
    public double getProteins() {
        return proteins;
    }

    @Override
    public int compareTo(Element o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object obj) {
        
        if (!(obj instanceof Element)) {
            return false;
        }

        return (compareTo((Element)obj) == 0);
    }

    @Override
    public int hashCode() {
        return (name + fat).hashCode();
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();

        buff.append("Type: ").append(getClass().getSimpleName())
            .append(". Name: ").append(name)
            .append(". Calories: ").append(calories)
            .append(". Proteins").append(proteins)
            .append(". Carbs: ").append(carbs)
            .append(". Fat: ").append(fat)
            .append(". Per100g? ").append(per100g() ? "YES" : "NO");

        return buff.toString();
    }
}