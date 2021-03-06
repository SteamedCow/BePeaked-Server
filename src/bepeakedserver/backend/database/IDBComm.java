package bepeakedserver.backend.database;

import bepeakedserver.model.DietPlanProfile;
import bepeakedserver.model.Exercise;
import bepeakedserver.model.Result;
import bepeakedserver.model.UserProfile;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * IDBComm
 * @author Lasse
 * @version 11-01-2017
 */
public interface IDBComm 
{
    public void createUser(String firstName, String lastName, String nickName, String passHash, String salt, String email) throws ConnectException;
    
    public void updatePassword(String nickName, String newHashedPassword) throws ConnectException;
    
    public int getUserType(int userID) throws ConnectException;
    
    public UserProfile getUserProfile(int userID) throws ConnectException;
    
    public DietPlanProfile getDietPlanProfile(int userID) throws ConnectException;
    
    public String getSalt(String nickName) throws ConnectException;
    
    public int authenticateUser(String nickName, String passHash) throws ConnectException;
    
    public int authenticateActivationKey(int userID, String activationKey) throws ConnectException;
    
//    public ArrayList<Result> getResults(int userID, int exerciseID) throws ConnectException;
    
    public HashMap<Integer, String> getWorkoutlist(int userID) throws ConnectException;
    
    public ArrayList<Exercise> getExerciseByWorkoutID(int userID, int workoutID) throws ConnectException;
    
    public Exercise getExercise(int exerciseID) throws ConnectException;
}