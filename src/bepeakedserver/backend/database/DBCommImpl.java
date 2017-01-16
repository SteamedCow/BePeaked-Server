package bepeakedserver.backend.database;

import bepeakedserver.model.DietPlanProfile;
import bepeakedserver.model.Exercise;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public void createUser(String firstName, String lastName, String nickName, String passHash, String salt, String email) throws ConnectException {
        Connection con = null;
        String query = "CALL `bepeakedwebserver_com_db`.`create_user`(?,?,?,?,?,?)";
        
        try {
            con = db.createConnection();
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, nickName);
                ps.setString(4, passHash);
                ps.setString(5, salt);
                ps.setString(6, email);
                
                ResultSet rs = ps.executeQuery();
                System.out.println(rs);
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
    public int getUserType(int userID) throws ConnectException {
        int userType = -1;
        Connection con = null;
        String query = "CALL `bepeakedwebserver_com_db`.`get_user_type`(?)";
        
        try {
            con = db.createConnection();
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, userID);
                
                System.out.println("PS=" + ps);
                
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    userType = rs.getInt(DBTags.USER_TYPE);
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
        return userType;
    }

    @Override
    public int authenticateUser(String nickName, String passHash) throws ConnectException {
        int us_id = -1;
        Connection con = null;
        String query = "CALL `bepeakedwebserver_com_db`.`authenticate_login`(?,?)";
        
        try {
            con = db.createConnection();
                

            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, nickName);
                ps.setString(2, passHash);
                
                System.out.println("PS=" + ps);
                
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    System.out.println("RS=" + rs.getInt(DBTags.VALIDATION));
                    if(rs.getInt(DBTags.VALIDATION) == 1)
                        us_id = rs.getInt(DBTags.USER_ID);
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
        return us_id;
    }

    @Override
    public int authenticateActivationKey(int userID, String activationKey) throws ConnectException {
        int us_id = -1;
        Connection con = null;
        String query = "CALL `bepeakedwebserver_com_db`.`authenticate_activationkey`(?,?)";
        
        try {
            con = db.createConnection();
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, userID);
                ps.setString(2, activationKey);
                
                System.out.println("PS=" + ps);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    System.out.println("RS=" + rs.getInt(DBTags.VALIDATION));
                    if(rs.getInt(DBTags.VALIDATION) == 1)
                        us_id = rs.getInt(DBTags.USER_ID);
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
        return us_id;
    }
    
//    @Override
//    public ArrayList<Result> getResults(int userID, int exerciseID) throws ConnectException {
//        ArrayList<Result> results = new ArrayList<>();
//        Connection con = null;
//        String query = "CALL `bepeakedwebserver_com_db`.`get_results`(?,?)";
//        
//        try {
//            con = db.createConnection();
//            try (PreparedStatement ps = con.prepareStatement(query)) {
//                ps.setInt(1, userID);
//                ps.setInt(2, exerciseID);
//                
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    Result element = createResultElement(rs);
//                    results.add(element);
//                }
//                rs.close();
//            }
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        } 
//        finally {
//            if(con != null) {
//                try {
//                    con.close();
//                } 
//                catch (SQLException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }
//        return results;
//    }

    @Override
    public HashMap<Integer, String> getWorkoutlist(int userID) throws ConnectException {
        HashMap<Integer, String> result = new HashMap<>();
        Connection con = null;
        String query = "CALL `bepeakedwebserver_com_db`.`get_workoutlist`(?)";
        
        try {
            con = db.createConnection();
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, userID);
                
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
    public ArrayList<Exercise> getExerciseByWorkoutID(int userID, int workoutID) throws ConnectException {
        ArrayList<Exercise> result = new ArrayList<>();
        Connection con = null;
        String query = "CALL `bepeakedwebserver_com_db`.`getExerciseByWorkout`(?, ?)";
        
        try {
            con = db.createConnection();
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, userID);
                ps.setInt(2, workoutID);
                
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

    @Override
    public Exercise getExercise(int exerciseID) throws ConnectException {
        Exercise result = null;
        Connection con = null;
        String query = "CALL `bepeakedwebserver_com_db`.`get_exercise`(?)";
        
        try {
            con = db.createConnection();
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, exerciseID);
                
                try (ResultSet rs = ps.executeQuery()) {
                    if(rs.next())
                        result = createExerciseElement(rs);
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
        return result;
    }

    @Override
    public DietPlanProfile getDietPlanProfile(int userID) throws ConnectException {
        DietPlanProfile result = null;
        Connection con = null;
        String query = "CALL `bepeakedwebserver_com_db`.`get_dietplan_profile`(?)";
        
        try {
            con = db.createConnection();
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, userID);
                
                try (ResultSet rs = ps.executeQuery()) {
                    if(rs.next())
                        result = createDietPlanProfile(rs);
                    else
                        result = new DietPlanProfile(-1.0, -1.0, -1.0, -1.0);
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
        return result;
    }

//    private Result createResultElement(ResultSet rs) throws SQLException {
//        int exerciseID = rs.getInt(DBTags.EXERCISE_ID);
//        int reps = rs.getInt(DBTags.REPS);
//        double weighth = rs.getDouble(DBTags.WEIGHT);
//        Date date = rs.getDate(DBTags.DATE);
//        
//        return new Result(exerciseID, reps, weighth, date);
//    }

    private Exercise createExerciseElement(ResultSet rs) throws SQLException {
        int exerciseID = rs.getInt(DBTags.EXERCISE_ID);
        String name = rs.getString(DBTags.EXERCISE_NAME);
        String description = rs.getString(DBTags.EXERCISE_DESCRIPTION);
        int imageID = rs.getInt(DBTags.EXERCISE_IMAGEID);
        int sets = rs.getInt(DBTags.WORKOUT_SETS);
        String reps = rs.getString(DBTags.WORKOUT_REPS);
        
        return new Exercise(exerciseID, name, description, imageID, sets, reps);
    }

    private DietPlanProfile createDietPlanProfile(ResultSet rs) throws SQLException {
        double protein = rs.getDouble(DBTags.DIETPLAN_PROT);
        double calories = rs.getDouble(DBTags.DIETPLAN_CAL);
        double culhydrates = rs.getDouble(DBTags.DIETPLAN_CUL);
        double fat = rs.getDouble(DBTags.DIETPLAN_FAT);
        
        return new DietPlanProfile(protein, calories, culhydrates, fat);
    }
}