package bepeakedserver.backend.database;

import bepeakedserver.model.Exercise;
import bepeakedserver.model.Result;
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
    public void createUser(String firstName, String lastName, String nickName, String passHash, String salt) throws ConnectException;
    
    public void updatePassword(String nickName, String newHashedPassword) throws ConnectException;
    
    public String getSalt(String nickName) throws ConnectException;
    
    public boolean authenticateUser(String nickName, String passHash) throws ConnectException;
    
    public ArrayList<Result> getResults(int userID, int exerciseID) throws ConnectException;
    
    public HashMap<Integer, String> getWorkoutlist() throws ConnectException;
    
    public ArrayList<Exercise> getExerciseByWorkoutID(int workoutID) throws ConnectException;
}