package bepeakedserver.model;

import java.util.Date;
import java.util.Objects;

/**
 * Result
 * @author Lasse
 * @version 11-01-2017
 */
public class Result 
{
    private int exerciseID;
    private int reps;
    private double weight;
    private Date date;

    public Result(int exerciseID, int reps, double weight, Date date) {
        this.exerciseID = exerciseID;
        this.reps = reps;
        this.weight = weight;
        this.date = date;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Result{" + "exerciseID=" + exerciseID + ", reps=" + reps + ", weight=" + weight + ", date=" + date + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        
        final Result other = (Result) obj;
        return this.exerciseID == other.exerciseID &&
                this.reps == other.reps &&
                Double.doubleToLongBits(this.weight) == Double.doubleToLongBits(other.weight) &&
                Objects.equals(this.date, other.date);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.exerciseID;
        hash = 79 * hash + this.reps;
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.weight) ^ (Double.doubleToLongBits(this.weight) >>> 32));
        hash = 79 * hash + Objects.hashCode(this.date);
        return hash;
    }
}