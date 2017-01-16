package bepeakedserver.model;

/**
 * DietPlanProfile
 * @author Lasse
 * @version 16-01-2017
 */
public class DietPlanProfile 
{
    private final double protein, calories, culhydrates, fat;

    public DietPlanProfile(double protein, double calories, double culhydrates, double fat) {
        this.protein = protein;
        this.calories = calories;
        this.culhydrates = culhydrates;
        this.fat = fat;
    }

    public double getProtein() {
        return protein;
    }

    public double getCalories() {
        return calories;
    }

    public double getCulhydrates() {
        return culhydrates;
    }

    public double getFat() {
        return fat;
    }
}