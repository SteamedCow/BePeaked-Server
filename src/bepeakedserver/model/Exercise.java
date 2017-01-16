package bepeakedserver.model;

import java.util.Objects;

/**
 * Exercise
 * @author Lasse
 * @version 11-01-2017
 */
public class Exercise 
{
    private String exerciseName, description, reps;
    private int exerciseID, imageID, sets;

    public Exercise(int exerciseID, String exerciseName, String description, int imageID) {
        this(exerciseID, exerciseName, description, imageID, -1, null);
    }

    public Exercise(int exerciseID, String exerciseName, String description, int imageID, int sets, String reps) {
        this.exerciseName = exerciseName;
        this.description = description;
        this.exerciseID = exerciseID;
        this.imageID = imageID;
        this.sets = sets;
        this.reps = reps;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    @Override
    public String toString() {
        return "Exercise{" + "exerciseName=" + exerciseName + ", description=" + description + ", exerciseID=" + exerciseID + ", imageID=" + imageID + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        
        final Exercise other = (Exercise) obj;
        return Objects.equals(this.exerciseName, other.exerciseName) &&
                Objects.equals(this.description, other.description) &&
                this.exerciseID == other.exerciseID &&
                this.imageID == other.imageID;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.exerciseName);
        hash = 23 * hash + Objects.hashCode(this.description);
        hash = 23 * hash + this.exerciseID;
        hash = 23 * hash + this.imageID;
        return hash;
    }
}