package bepeakedserver.backend;

import bepeakedserver.session.Sessions;
import SocketServer.IMultiplexer;
import SocketServer.TextTransfer;
import bepeakedserver.backend.database.DBCommImpl;
import bepeakedserver.backend.database.MySQLFactory;
import bepeakedserver.model.Exercise;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import scSecurity.RandomGen;
import scSecurity.hashing.MD5Hashing;

/**
 * ServerMultiplexer
 * @author Lasse
 * @version 11-01-2017
 */
public class ServerMultiplexer implements IMultiplexer
{
    private static final String TAG_COMMAND = "cmd";
    private static final String TAG_CMD_TEST = "SCTest";
    private static final String TAG_CMD_STOP = "stop";
    private static final String TAG_CMD_CREATE = "create";
    private static final String TAG_CMD_UPDATE = "update";
    private static final String TAG_CMD_VALIDATE = "validate";
    private static final String TAG_CMD_GET = "get";
    private static final String TAG_CMD_SESSION_ID = "session";
    private static final String TAG_ARGS = "args";
    private static final String TAG_USER = "user";
    private static final String TAG_WORKOUTLIST = "workouts";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_SALT = "salt";
    private static final String TAG_RESULT = "result";
    private static final String TAG_EXERCISE = "exercise";
    private static final String TAG_ERROR = "error";
    private static final String TAG_ERROR_NONE = "none";
    
    @Override
    public void parse(JSONObject jsonObj, Socket socket) {
        String cmd = (String) jsonObj.get(TAG_COMMAND);
        
        switch (cmd) {
            case TAG_CMD_TEST: {
                sendText(socket, BackendData.CHARSET_ENCODING, "SCAccept");
                break;
            }
            case TAG_CMD_STOP: {
                System.out.println("Stopping server..");
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    System.out.println("Complete!");
                    System.exit(1);
                }
                break;
            }
            case TAG_CMD_VALIDATE: {
                JSONArray jsonArr = (JSONArray) jsonObj.get(TAG_ARGS);
                switch ((String) jsonArr.get(0)) {
                    case TAG_USER: {
                        String nickName = (String) jsonArr.get(1);
                        JSONArray passHashArrayJA = (JSONArray) jsonArr.get(2);
                        String[] passHashArray = new String[passHashArrayJA.size()];
                        
                        for (int i = 0; i < passHashArrayJA.size(); i++)
                            passHashArray[i] = (String) passHashArrayJA.get(i);
                        
                        String md5Hash = new MD5Hashing().decryptHash(passHashArray);
                        
                        System.out.println("MD5 Hash=" + md5Hash);
                        authenticateUser(socket, nickName, md5Hash);
                        break;
                    }
                    default:
                        throw new UnsupportedOperationException("Invalid command");
                }
                break;
            }
            case TAG_CMD_UPDATE: {
                JSONArray jsonArr = (JSONArray) jsonObj.get(TAG_ARGS);
                switch ((String) jsonArr.get(0)) {
                    case TAG_PASSWORD: {
                        String nickName = (String) jsonArr.get(1);
                        JSONArray passHashArrayJA = (JSONArray) jsonArr.get(2);
                        String[] passHashArray = new String[passHashArrayJA.size()];
                        
                        for (int i = 0; i < passHashArrayJA.size(); i++)
                            passHashArray[i] = (String) passHashArrayJA.get(i);
                        
                        String md5Hash = new MD5Hashing().decryptHash(passHashArray);
                        
                        updatePassword(socket, nickName, md5Hash);
                        break;
                    }
                    default:
                        throw new UnsupportedOperationException("Invalid command");
                }
                break;
            }
            case TAG_CMD_CREATE: {
                JSONArray jsonArr = (JSONArray) jsonObj.get(TAG_ARGS);
                switch ((String) jsonArr.get(0)) {
                    case TAG_USER: {
                        String firstName = (String) jsonArr.get(1);
                        String lastName = (String) jsonArr.get(2);
                        String nickName = (String) jsonArr.get(3);
                        String passHash = (String) jsonArr.get(4);
                        String salt = (String) jsonArr.get(5);
                        
                        createUser(socket, firstName, lastName, nickName, passHash, salt);
                        break;
                    }
                    default:
                        throw new UnsupportedOperationException("Invalid command");
                }
                break;
            }
            case TAG_CMD_GET: {
                JSONArray jsonArr = (JSONArray) jsonObj.get(TAG_ARGS);
                
                switch ((String) jsonArr.get(0)) {
                    case TAG_SALT: {
                        String userName = (String) jsonArr.get(1);
                        
                        getSalt(socket, userName);
                        break;
                    }
//                    case TAG_RESULT: {
//                        String sessionID = (String) jsonObj.get(TAG_CMD_SESSION_ID);
//                        int userID = (int) (long) jsonArr.get(1);
//                        int exerciseID = (int) (long) jsonArr.get(2);
//                        
//                        getResults(socket, userID, exerciseID);
//                        break;
//                    }
                    case TAG_WORKOUTLIST: {
                        String sessionID = (String) jsonObj.get(TAG_CMD_SESSION_ID);
                        
                        getWorkoutList(socket, sessionID);
                        break;
                    }
                    case TAG_EXERCISE: {
                        String sessionID = (String) jsonObj.get(TAG_CMD_SESSION_ID);
                        int workoutID = (int) (long) jsonArr.get(1);
                        
                        getExercises(socket, workoutID, sessionID);
                        break;
                    }
                    default:
                        throw new UnsupportedOperationException("Invalid command");
                }
                break;
            }
        }
    }

    private void authenticateUser(Socket socket, String nickName, String passHash) {
        DBCommImpl db = new DBCommImpl(new MySQLFactory(BackendData.DB_URL, BackendData.DB_SCHEMA, BackendData.DB_USERNAME, BackendData.DB_PASSWORD));
        try {
            boolean success = db.authenticateUser(nickName, passHash);
            JSONObject reply = new JSONObject();
            
            if(success) {
                String sessionID = RandomGen.getSalt(16);
                Sessions.addSession(sessionID, new Date().getTime());
                
                reply.put(TAG_ERROR, TAG_ERROR_NONE);
                reply.put(TAG_CMD_SESSION_ID, sessionID);
            }
            else
                reply.put(TAG_ERROR, "Username and password does not match");
            
            sendText(socket, BackendData.CHARSET_ENCODING, reply.toJSONString());
        } catch (ConnectException e) {
            e.printStackTrace();
            JSONObject reply = new JSONObject();
            
            reply.put(TAG_ERROR, e);
            
            sendText(socket, BackendData.CHARSET_ENCODING, reply.toJSONString());
        }
    }

    private void createUser(Socket socket, String firstName, String lastName, String nickName, String passHash, String salt) {
        DBCommImpl db = new DBCommImpl(new MySQLFactory(BackendData.DB_URL, BackendData.DB_SCHEMA, BackendData.DB_USERNAME, BackendData.DB_PASSWORD));
        try {
            db.createUser(firstName, lastName, nickName, passHash, salt);
            JSONObject reply = new JSONObject();
            
            reply.put(TAG_ERROR, TAG_ERROR_NONE);
            
            sendText(socket, BackendData.CHARSET_ENCODING, reply.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject reply = new JSONObject();
            
            reply.put(TAG_ERROR, e);
            
            sendText(socket, BackendData.CHARSET_ENCODING, reply.toJSONString());
        }
    }

    private void updatePassword(Socket socket, String nickName, String passHash) {
        DBCommImpl db = new DBCommImpl(new MySQLFactory(BackendData.DB_URL, BackendData.DB_SCHEMA, BackendData.DB_USERNAME, BackendData.DB_PASSWORD));
        try {
            db.updatePassword(nickName, passHash);
            JSONObject reply = new JSONObject();
            
            reply.put(TAG_ERROR, TAG_ERROR_NONE);
            
            sendText(socket, BackendData.CHARSET_ENCODING, reply.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject reply = new JSONObject();
            
            reply.put(TAG_ERROR, e);
            
            sendText(socket, BackendData.CHARSET_ENCODING, reply.toJSONString());
        }
    }

    private void getSalt(Socket socket, String userName) {
        DBCommImpl db = new DBCommImpl(new MySQLFactory(BackendData.DB_URL, BackendData.DB_SCHEMA, BackendData.DB_USERNAME, BackendData.DB_PASSWORD));
        try {
            String salt = db.getSalt(userName);
            JSONObject reply = new JSONObject();
            
            reply.put(TAG_USER, userName);
            reply.put(TAG_SALT, salt);
            
            sendText(socket, BackendData.CHARSET_ENCODING, reply.toJSONString());
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }
    
//    private void getResults(Socket socket, int userID, int exerciseID) {
//        DBCommImpl db = new DBCommImpl(new MySQLFactory(BackendData.DB_URL, BackendData.DB_SCHEMA, BackendData.DB_USERNAME, BackendData.DB_PASSWORD));
//        try {
//            ArrayList<Result> results = db.getResults(userID, exerciseID);
//            JSONArray reply = new JSONArray();
//            
//            JSONObject jsonResult;
//            for (Result result : results) {
//                jsonResult = new JSONObject();
//                jsonResult.put("exerciseID", result.getExerciseID());
//                jsonResult.put("reps", result.getReps());
//                jsonResult.put("weight", result.getWeight());
//                jsonResult.put("date", result.getDate());
//                reply.add(jsonResult);
//            }
//            
//            sendText(socket, BackendData.CHARSET_ENCODING, reply.toJSONString());
//        } catch (ConnectException e) {
//            e.printStackTrace();
//        }
//    }

    private void getWorkoutList(Socket socket, String sessionID) {
        DBCommImpl db = new DBCommImpl(new MySQLFactory(BackendData.DB_URL, BackendData.DB_SCHEMA, BackendData.DB_USERNAME, BackendData.DB_PASSWORD));
        try {
            if(Sessions.containsSession(sessionID)) {
                Sessions.updateSessionTimestamp(sessionID, new Date().getTime());
                
                HashMap<Integer, String> results = db.getWorkoutlist();
                
                JSONObject reply = new JSONObject();
                JSONArray workoutlist = new JSONArray();
                JSONObject jsonResult;
                Map.Entry<Integer, String> workout;
                for (Object obj : results.entrySet()) {
                    workout = (Map.Entry<Integer, String>) obj;
                    jsonResult = new JSONObject();
                    jsonResult.put("id", workout.getKey());
                    jsonResult.put("name", workout.getValue());
                    workoutlist.add(jsonResult);
                }
                reply.put(TAG_WORKOUTLIST, workoutlist);
                sendText(socket, BackendData.CHARSET_ENCODING, reply.toJSONString());
            }
            else {
                JSONObject reply = new JSONObject();
                reply.put(TAG_ERROR, "Invalid session id");
                sendText(socket, BackendData.CHARSET_ENCODING, reply.toJSONString());
            }
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }

    private void getExercises(Socket socket, int workoutID, String sessionID) {
        DBCommImpl db = new DBCommImpl(new MySQLFactory(BackendData.DB_URL, BackendData.DB_SCHEMA, BackendData.DB_USERNAME, BackendData.DB_PASSWORD));
        try {
            if(Sessions.containsSession(sessionID)) {
                Sessions.updateSessionTimestamp(sessionID, new Date().getTime());
                
                ArrayList<Exercise> results = db.getExerciseByWorkoutID(workoutID);
                
                JSONObject reply = new JSONObject();
                JSONArray exercises = new JSONArray();
                JSONObject jsonResult;
                for (Exercise exercise : results) {
                    jsonResult = new JSONObject();
                    jsonResult.put("id", exercise.getExerciseID());
                    jsonResult.put("name", exercise.getExerciseName());
                    jsonResult.put("description", exercise.getDescription());
                    jsonResult.put("imageID", exercise.getImageID());
                    jsonResult.put("sets", exercise.getSets());
                    exercises.add(jsonResult);
                }
                reply.put(TAG_EXERCISE, exercises);
                sendText(socket, BackendData.CHARSET_ENCODING, reply.toJSONString());
            }
            else {
                JSONObject reply = new JSONObject();
                reply.put(TAG_ERROR, "Invalid session id");
                sendText(socket, BackendData.CHARSET_ENCODING, reply.toJSONString());
            }
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }
    
    private void sendText(Socket socket, String charEncoding, String message) {
        try {
            System.out.println("Sending reply: " + message);
            TextTransfer.sendReply(socket, charEncoding, message);
        }
        catch (IOException e) {
            e.printStackTrace();
            try {
                if(socket != null)
                    socket.close();
            }
            catch (IOException ex) {
                System.err.println("Could not close socket connection! " + ex.getMessage());
            }
        }
    }
}