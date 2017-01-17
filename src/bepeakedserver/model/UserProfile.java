package bepeakedserver.model;

/**
 * UserProfile
 * @author SteamedCow
 * @version 17-01-2017
 */
public class UserProfile 
{
    private final String firstName, lastName;
    private final double height, weight, prot, cal, col, fat;
    private final int age, dietplanID;

    public UserProfile(String firstName, String lastName, int age, double height, double weight, double prot, double cal, double col, double fat, int dietplanID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.height = height;
        this.weight = weight;
        this.prot = prot;
        this.cal = cal;
        this.col = col;
        this.fat = fat;
        this.age = age;
        this.dietplanID = dietplanID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public double getProt() {
        return prot;
    }

    public double getCal() {
        return cal;
    }

    public double getCol() {
        return col;
    }

    public double getFat() {
        return fat;
    }

    public int getAge() {
        return age;
    }

    public int getDietplanID() {
        return dietplanID;
    }

    @Override
    public String toString() {
        return "UserProfile{" + "firstName=" + firstName + ", lastName=" + lastName + ", height=" + height + ", weight=" + weight + ", prot=" + prot + ", cal=" + cal + ", col=" + col + ", fat=" + fat + ", age=" + age + '}';
    }
}