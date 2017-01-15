package bepeakedserver.backend.database;

import bepeakedserver.model.Exercise;
import bepeakedserver.model.Result;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * DBCommImpl
 * @author Lasse
 * @version 11-01-2017
 */
public class DBCommImpl implements IDBComm
{
    private final MySQLFactory db;

    public DBCommImpl(MySQLFactory dbComm) {
        this.db = dbComm;
    }

    @Override
    public void createUser(String firstName, String lastName, String nickName, String passHash, String salt) throws ConnectException {
        Connection con = null;
        String query = "CALL `bepeakedwebserver_com_db`.`create_user`(?,?,?,?,?)";
        
        try {
            con = db.createConnection();
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, nickName);
                ps.setString(4, passHash);
                ps.setString(5, salt);
                
                ResultSet rs = ps.executeQuery();
                System.out.println(rs);
//                while (rs.next()) {
//                    Result element = createResultElement(rs);
//                    results.add(element);
//                }
//                rs.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new ConnectException(e.getMessage());
        } 
        finally {
            if(con != null) {
                try {
                    con.close();
                } 
                catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void updatePassword(String nickName, String newHashedPassword) throws ConnectException {
        Connection con = null;
        String query = "CALL `bepeakedwebserver_com_db`.`update_password`(?,?)";
        
        try {
            con = db.createConnection();
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, nickName);
                ps.setString(2, newHashedPassword);
                
                System.out.println("PS=" + ps);
                ResultSet rs = ps.executeQuery();
                
//                while (rs.next()) {
//                }
                rs.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new ConnectException(e.getMessage());
        } 
        finally {
            if(con != null) {
                try {
                    con.close();
                } 
                catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getSalt(String nickName) throws ConnectException {
        String salt = null;
        Connection con = null;
        String query = "CALL `bepeakedwebserver_com_db`.`get_salt`(?)";
        
        try {
            con = db.createConnection();
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, nickName);
                
                System.out.println("PS=" + ps);
                
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    salt = rs.getString(DBTags.USER_SALT);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {
            if(con != null) {
                try {
                    con.close();
                } 
                catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return salt;
    }

    @Override
    public boolean authenticateUser(String nickName, String passHash) throws ConnectException {
        boolean success = false;
        Connection con = null;
//        String query = "CALL `bepeakedwebserver_com_db`.`authenticate_login`(?,'41f2ec89612ed00fe6c168c7dedef7b8 ')";
//        String query = "CALL `bepeakedwebserver_com_db`.`authenticate_login`(?,'" + passHash + "')";
        String query = "CALL `bepeakedwebserver_com_db`.`authenticate_login`(?,?)";
        
        try {
            con = db.createConnection();
                

            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, nickName);
                ps.setString(2, passHash);
                
                System.out.println("PS=" + ps);
                
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    System.out.println("RS=" + rs.getInt(DBTags.USER_VALIDATION));
                    if(rs.getInt(DBTags.USER_VALIDATION) == 1)
                        success = true;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {
            if(con != null) {
                try {
                    con.close();
                } 
                catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return success;
    }
    
    @Override
    public ArrayList<Result> getResults(int userID, int exerciseID) throws ConnectException {
        ArrayList<Result> results = new ArrayList<>();
        Connection con = null;
        String query = "CALL `bepeakedwebserver_com_db`.`get_results`(?,?)";
        
        try {
            con = db.createConnection();
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, userID);
                ps.setInt(2, exerciseID);
                
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Result element = createResultElement(rs);
                    results.add(element);
                }
                rs.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {
            if(con != null) {
                try {
                    con.close();
                } 
                catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return results;
    }

    @Override
    public HashMap<Integer, String> getWorkoutlist() throws ConnectException {
        HashMap<Integer, String> result = new HashMap<>();
        Connection con = null;
        String query = "CALL `bepeakedwebserver_com_db`.`get_workoutlist`()";
        
        try {
            con = db.createConnection();
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt(DBTags.WORKOUT_ID);
                    String name = rs.getString(DBTags.WORKOUT_NAME);
                    
                    result.put(id, name);
                }
                rs.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {
            if(con != null) {
                try {
                    con.close();
                } 
                catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public ArrayList<Exercise> getExerciseByWorkoutID(int workoutID) throws ConnectException {
        ArrayList<Exercise> result = new ArrayList<>();
        Connection con = null;
        String query = "CALL `bepeakedwebserver_com_db`.`getExerciseByWorkout`(?)";
        
        try {
            con = db.createConnection();
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, workoutID);
                
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Exercise element = createExerciseElement(rs);
                    result.add(element);
                }
                rs.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {
            if(con != null) {
                try {
                    con.close();
                } 
                catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return result;
    }

    private Result createResultElement(ResultSet rs) throws SQLException {
        int exerciseID = rs.getInt(DBTags.EXERCISE_ID);
        int reps = rs.getInt(DBTags.REPS);
        double weighth = rs.getDouble(DBTags.WEIGHT);
        Date date = rs.getDate(DBTags.DATE);
        
        return new Result(exerciseID, reps, weighth, date);
    }

    private Exercise createExerciseElement(ResultSet rs) throws SQLException {
        int exerciseID = rs.getInt(DBTags.EXERCISE_ID);
        String name = rs.getString(DBTags.EXERCISE_NAME);
        String description = rs.getString(DBTags.EXERCISE_DESCRIPTION);
        int imageID = rs.getInt(DBTags.EXERCISE_IMAGEID);
        int sets = rs.getInt(DBTags.WORKOUT_SETS);
        
        return new Exercise(exerciseID, name, description, imageID, sets);
    }
}