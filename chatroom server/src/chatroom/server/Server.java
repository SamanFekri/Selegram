/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroom.server;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author hessa
 */
public class Server implements Runnable {

    private Semaphore mutex;
    private MongoDatabase db;

    public Server() {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDatabase("chatroom");
        mutex = new Semaphore(1);
    }

    @Override
    public void run() {
        try {
            ServerSocket welcomeSocket = new ServerSocket(2027);
            System.out.println("regServer TCP Port " + welcomeSocket.getLocalPort() + " IP Address: " + InetAddress.getLocalHost().getHostAddress());
            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

//                String clientSentence = inFromClient.readLine();
                //System.out.println(clientSentence);
                new Thread(new RequestHandler(connectionSocket)).start();
            }
        } catch (IOException ex) {
            System.out.println("error in connection");
        }
    }

    private String register(String options) {
        JSONObject obj = new JSONObject(options);
        String username = obj.getString("username");
        String password = obj.getString("password");
        FindIterable<Document> iterable = db.getCollection("users").find(eq("username", username));
        Document document = iterable.first();
        JSONObject result = new JSONObject();
        result.put("status", "err").put("payload", new JSONObject());
        if (document == null) {
            result = new JSONObject();
            result.put("status", "ok").put("payload", new JSONObject().put("registered", false));
            // add new user to users collection
            db.getCollection("users").insertOne(new Document()
                    .append("username", username)
                    .append("password", password)
                    .append("registered", false)
                    .append("first_name", "")
                    .append("last_name", "")
                    .append("phone", "")
                    .append("email", "")
                    .append("birth_day", "")
                    .append("bio", "")
                    .append("img", "")
                    .append("friends", asList())
                    .append("groups", asList())
                    .append("channels", asList())
                    .append("reporters", asList())
                    .append("reported_date", ""));
        }
        return result.toString();
    }

    private String login(String options) {
        JSONObject obj = new JSONObject(options);
        String username = obj.getString("username");
        String password = obj.getString("password");
        FindIterable<Document> iterable = db.getCollection("users").find(eq("username", username));
        Document document = iterable.first();
        JSONObject result = new JSONObject();
        result.put("status", "err").put("payload", new JSONObject());
        if (document != null) {
            if (document.getString("password").equals(password)) {
                result = new JSONObject();
                result.put("status", "ok").put("payload", new JSONObject().put("reported_date",
                        document.getString("reported_date")
                ).put("registered", document.getBoolean("registered")));
            }
        }
        return result.toString();
    }

    private String profile_edit(String options) {
        JSONObject obj = new JSONObject(options);
//        error found: password??
//        String password = obj.getString("password");
        JSONObject result = new JSONObject();
        result.put("status", "err").put("payload", new JSONObject());
        FindIterable<Document> iterable = db.getCollection("users").find(eq("username", obj.getString("user_name")));
        Document document = iterable.first();
        if (document != null) {
            String password = document.getString("password");
            result = new JSONObject();
            result.put("status", "ok").put("payload", new JSONObject());
            if (!obj.getString("password").equals("")) {
                password = obj.getString("password");
            }
            ArrayList<String> friends = (ArrayList<String>) document.get("friends");
            ArrayList<String> groups = (ArrayList<String>) document.get("groups");
            ArrayList<String> channels = (ArrayList<String>) document.get("channels");
            ArrayList<String> reporters = (ArrayList<String>) document.get("reporters");
            String reported_date = document.getString("reported_date");
            db.getCollection("users").replaceOne(new Document("username", obj.getString("user_name")),
                    new Document()
                    .append("username", obj.getString("user_name"))
                    .append("password", password)
                    .append("registered", true)
                    .append("first_name", obj.getString("first_name"))
                    .append("last_name", obj.getString("last_name"))
                    .append("phone", obj.getString("phone"))
                    .append("email", obj.getString("email"))
                    .append("birth_day", obj.getString("birth_day"))
                    .append("bio", obj.getString("bio"))
                    .append("img", obj.getString("img"))
                    .append("friends", friends)
                    .append("groups", groups)
                    .append("channels", channels)
                    .append("reporters", reporters)
                    .append("reported_date", reported_date)
            );
        }
        return result.toString();
    }

    ///////////////////////////////////////
    private String get_tree(String options) {
        JSONObject obj = new JSONObject(options);
        String username = obj.getString("user_name");
        JSONObject result = new JSONObject();
        result.put("status", "err").put("payload", new JSONObject());
        FindIterable<Document> iterable = db.getCollection("users").find(eq("username", username));
        Document document = iterable.first();
        if (document != null) {
            JSONArray channels = new JSONArray();
            JSONArray groups = new JSONArray();
            JSONArray directs = new JSONArray();
            JSONArray privates = new JSONArray();
            JSONArray unknowns = new JSONArray();
            ////////////////////////////////////////////////// channels
            iterable = db.getCollection("chats").find(new Document("participants", username).append("channel", new BasicDBObject("$exists", true)));
            for (Document d : iterable) {
                channels.put(new JSONObject()
                        .put("chat_id", d.getObjectId("_id").toString())
                        .put("name", ((Document) d.get("channel")).getString("name"))
                        .put("admin", ((Document) d.get("channel")).getString("admin"))
                );
            }
            ////////////////////////////////////////////////// groups
            iterable = db.getCollection("chats").find(new Document("participants", username).append("group", new BasicDBObject("$exists", true)));
            for (Document d : iterable) {
                System.out.println("found some groups");
                boolean mentioned = false;
                ArrayList<Document> o = (ArrayList<Document>) d.get("messages");
                System.out.println("HIHIHI " + o.toString());
//                System.out.println(d.get("messages").toString());
//                JSONArray o = new JSONArray(d.get("messages").toString());
                for (int i = 0; i < o.size(); i++) {
//                    JSONObject jo = new JSONObject(o.get(i).toJson());
//                    System.out.println(o.get(i).toString());
//                    JSONObject msg = new JSONObject(o.get(i).toString());
                    System.out.println("YOYOYO " + o.get(i).get("mentions"));
                    ArrayList<String> mentions = (ArrayList<String>) o.get(i).get("mentions");
//                    JSONArray mentions = msg.getJSONArray("mentions");
                    System.out.println(mentions.size());
                    for (int j = 0; j < mentions.size(); j++) {
                        if (mentions.get(j).equals(username)) {
                            System.out.println(mentions.get(j));
                            mentioned = true;
                            break;
                        }
                    }
                    if (mentioned) {
                        break;
                    }
                }
                groups.put(new JSONObject()
                        .put("chat_id", d.getObjectId("_id").toString())
                        .put("name", d.getString("group"))
                        .put("participants", new JSONArray(d.get("participants").toString()))
                        .put("mentioned", mentioned)
                );
            }
            ////////////////////////////////////////////////// privates
            iterable = db.getCollection("chats").find(new Document("participants", username).append("private", new BasicDBObject("$exists", true)));
            for (Document d : iterable) {
                JSONArray arr = new JSONArray(d.get("participants").toString());
                String us = "";
                for (int i = 0; i < arr.length(); i++) {
                    if (!arr.getString(i).equals(username)) {
                        us = arr.getString(i);
                    }
                }
                privates.put(new JSONObject()
                        .put("chat_id", d.getObjectId("_id").toString())
                        .put("username", us)
                );
            }
            /////////////////////////////////////////////////// directs and unknowns
            iterable = db.getCollection("chats").find(new Document("participants", username).append("direct", new BasicDBObject("$exists", true)));
            for (Document d : iterable) {
                JSONArray arr = new JSONArray(d.get("participants").toString());
                String us = "";
                for (int i = 0; i < arr.length(); i++) {
                    if (!arr.getString(i).equals(username)) {
                        us = arr.getString(i);
                    }
                }
                FindIterable<Document> userIt = db.getCollection("users").find(eq("username", username));
                Document user = userIt.first();
                System.out.println(user.toString());
                JSONArray user_friends = new JSONArray(user.get("friends").toString());
                boolean flag = false;
                for (int i = 0; i < user_friends.length(); i++) {
                    if (user_friends.getString(i).equals(us)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    directs.put(new JSONObject()
                            .put("chat_id", d.getObjectId("_id").toString())
                            .put("username", us)
                    );
                } else {
                    unknowns.put(new JSONObject()
                            .put("chat_id", d.getObjectId("_id").toString())
                            .put("username", us)
                    );
                }
            }
            result = new JSONObject();
            result.put("status", "ok").put("payload", new JSONObject()
                    .put("channels", channels)
                    .put("groups", groups)
                    .put("directs", directs)
                    .put("privates", privates)
                    .put("unknowns", unknowns)
            );
        }
        return result.toString();
    }

    private String add_friend(String options) {
        JSONObject obj = new JSONObject(options);
        String username = obj.getString("username");
        String fname = obj.getString("friend_username");
        JSONObject result = new JSONObject();
        result.put("status", "err").put("payload", new JSONObject());
        FindIterable<Document> iterable = db.getCollection("users").find(eq("username", username));
        Document document = iterable.first();
        if (document != null) {
            db.getCollection("users").updateOne(new Document("username", username),
                    new Document("$push", new Document("friends", fname))
            );
            boolean exists = false;
            FindIterable<Document> iterable2 = db.getCollection("chats").find(new Document("participants", username).append("direct", new BasicDBObject("$exists", true)));
            for (Document d : iterable2) {
                JSONArray arr = new JSONArray(d.get("participants").toString());
                for (int i = 0; i < arr.length(); i++) {
                    if (arr.getString(i).equals(fname)) {
                        exists = true;
                    }
                }
            }
            if (!exists) {
                db.getCollection("chats").insertOne(new Document()
                        .append("participants", asList(username, fname))
                        .append("messages", asList())
                        .append("direct", "")
                );
            }
            result = new JSONObject();
            result.put("status", "ok").put("payload", new JSONObject());
        }
        return result.toString();
    }

    private String unfriend(String options) {
        JSONObject obj = new JSONObject(options);
        String username = obj.getString("username");
        String fname = obj.getString("friend_username");
        JSONObject result = new JSONObject();
        result.put("status", "err").put("payload", new JSONObject());
        FindIterable<Document> iterable = db.getCollection("users").find(eq("username", username));
        Document document = iterable.first();
        if (document != null) {
            db.getCollection("users").updateOne(new Document("username", username),
                    new Document("$pull", new Document("friends", fname))
            );
            result = new JSONObject();
            result.put("status", "ok").put("payload", new JSONObject());
        }
        return result.toString();
    }

    ///////////////////////////////////////
    private String get_messages(String options) {
        JSONObject obj = new JSONObject(options);
        String username = obj.getString("username");
        String chat_id = obj.getString("chat_id");
        int count = obj.getInt("count");
        JSONObject result = new JSONObject();
        result.put("status", "err").put("payload", new JSONObject());
        FindIterable<Document> iterable = db.getCollection("chats").find(eq("_id", new ObjectId(chat_id)));
        Document document = iterable.first();
        if (document != null) {
            ArrayList<Document> o = (ArrayList<Document>) document.get("messages");
            JSONArray messages = new JSONArray();
            JSONArray sorted = new JSONArray();
            for (int i = 0; i < o.size(); i++) {
                JSONObject jo = new JSONObject(o.get(i).toJson());
                messages.put(jo);
            }
            ArrayList<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < messages.length(); i++) {
                jsonValues.add(messages.getJSONObject(i));
            }
            Collections.sort(jsonValues, new Comparator<JSONObject>() {
                private static final String KEY_NAME = "send_date";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    Long valA = null;
                    Long valB = null;
                    try {
//                        valA = a.getLong(KEY_NAME);
//                        valB = b.getLong(KEY_NAME);
                        valA = a.getJSONObject(KEY_NAME).getLong("$numberLong");
                        valB = b.getJSONObject(KEY_NAME).getLong("$numberLong");
                        System.out.println("val a " + valA);
                        System.out.println("val b " + valB);
                    } catch (JSONException e) {
                        System.out.println("ridam" + e.getMessage() + a.getJSONObject(KEY_NAME).get("$numberLong"));
                    }
                    return valA.compareTo(valB);
                }
            });
            if (jsonValues.size() <= count) {
                count = jsonValues.size();
            }
            for (int i = 0; i < count; i++) {
                sorted.put(jsonValues.get(i));
            }
            result = new JSONObject();
            result.put("status", "ok").put("payload", new JSONObject().put("messages", sorted));
        }
        return result.toString();
    }

    private String get_profile(String options) {
        JSONObject obj = new JSONObject(options);
        String username = obj.getString("username");
        JSONObject result = new JSONObject();
        result.put("status", "err").put("payload", new JSONObject());
        FindIterable<Document> iterable = db.getCollection("users").find(eq("username", username));
        Document document = iterable.first();
        if (document != null) {
            result = new JSONObject();
            result.put("status", "ok").put("payload", new JSONObject()
                    .put("img", document.getString("img"))
                    .put("first_name", document.getString("first_name"))
                    .put("last_name", document.getString("last_name"))
                    .put("username", document.getString("username"))
                    .put("birth_day", document.getString("birth_day"))
                    .put("email", document.getString("email"))
                    .put("bio", document.getString("bio"))
                    .put("password", document.getString("password"))
                    .put("phone", document.getString("phone"))
            );
        }
        return result.toString();
    }

    private String report(String options) {
        JSONObject obj = new JSONObject(options);
        String username = obj.getString("username");
        String rname = obj.getString("report_username");
        JSONObject result = new JSONObject();
        result.put("status", "err").put("payload", new JSONObject());
        FindIterable<Document> iterable = db.getCollection("users").find(eq("username", rname));
        Document document = iterable.first();
        if (document != null) {
            db.getCollection("users").updateOne(new Document("username", rname),
                    new Document("$push", new Document("reporters", username))
            );
            result = new JSONObject();
            result.put("status", "ok").put("payload", new JSONObject());
            JSONArray arr = new JSONArray(document.get("reporters").toString());
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                if (!list.contains(arr.getString(i))) {
                    list.add(arr.getString(i));
                }
            }
            if (list.size() > 9) {
                db.getCollection("users").updateOne(new Document("username", rname),
                        new Document("$set", new Document("reported_date", new Date().toString()))
                );
            }
        }
        return result.toString();
    }

    ///////////////////////////////////////
    private String make_private(String options) {
        JSONObject obj = new JSONObject(options);
        String username = obj.getString("username");
        String fname = obj.getString("friend_username");
        JSONObject result = new JSONObject();
        result.put("status", "err").put("payload", new JSONObject());
        FindIterable<Document> iterable = db.getCollection("users").find(eq("username", fname));
        Document document = iterable.first();
        if (document != null) {
            boolean exists = false;
            FindIterable<Document> iterable2 = db.getCollection("chats").find(new Document("participants", username).append("private", new BasicDBObject("$exists", true)));
            for (Document d : iterable2) {
                JSONArray arr = new JSONArray(d.get("participants").toString());
                for (int i = 0; i < arr.length(); i++) {
                    if (arr.getString(i).equals(fname)) {
                        exists = true;
                        break;
                    }
                }
                if (exists) {
                    result = new JSONObject();
                    result.put("status", "ok").put("payload", new JSONObject().put("chat_id", d.getObjectId("_id").toString()));
                    break;
                }
            }
            if (!exists) {
                ObjectId id = new ObjectId();
                db.getCollection("chats").insertOne(new Document()
                        .append("_id", id)
                        .append("private", 60000L)
                        .append("participants", asList(username, fname))
                        .append("messages", asList())
                );
                result = new JSONObject();
                result.put("status", "ok").put("payload", new JSONObject().put("chat_id", id.toString()));
            }
        }
        return result.toString();
    }

    ///////////////////////////////////////
    private String send_message(String options) {
        JSONObject obj = new JSONObject(options);
        String chat_id = obj.getString("chat_id");
        String username = obj.getString("username");
        String recipient = obj.getString("recipient");
        String body = obj.getString("body");
        JSONObject result = new JSONObject();
        result.put("status", "err").put("payload", new JSONObject());
        List<String> mentions = new ArrayList<>();
        List<String> hashtags = new ArrayList<>();
        Pattern MY_PATTERN = Pattern.compile("#(\\S+)");
        Matcher mat = MY_PATTERN.matcher(body);
        while (mat.find()) {
            hashtags.add(mat.group(1));
        }
        MY_PATTERN = Pattern.compile("@(\\S+)");
        mat = MY_PATTERN.matcher(body);
        while (mat.find()) {
            mentions.add(mat.group(1));
        }
        if (chat_id.equals("")) {
            boolean exists = false;
            FindIterable<Document> iterable2 = db.getCollection("chats").find(new Document("participants", username).append("direct", new BasicDBObject("$exists", true)));
            for (Document d : iterable2) {
                JSONArray arr = new JSONArray(d.get("participants").toString());
                for (int i = 0; i < arr.length(); i++) {
                    if (arr.getString(i).equals(recipient)) {
                        chat_id = d.getObjectId("_id").toString();
                        exists = true;
                    }
                }
            }
            if (!exists) {
                ObjectId newId = new ObjectId();
                db.getCollection("chats").insertOne(new Document()
                        .append("_id", newId)
                        .append("participants", asList(username, recipient))
                        .append("messages", asList(new Document()
                                        .append("message_id", new ObjectId())
                                        .append("sender", username)
                                        .append("body", body)
                                        .append("send_date", System.currentTimeMillis())
                                        .append("mentions", mentions)
                                        .append("hashtags", hashtags)
                                ))
                        .append("direct", "")
                );
                result = new JSONObject();
                result.put("status", "ok").put("payload", new JSONObject());
            } else {
                result = new JSONObject();
                result.put("status", "ok").put("payload", new JSONObject());
                db.getCollection("chats").updateOne(new Document("_id", new ObjectId(chat_id)),
                        new Document("$push", new Document("messages", new Document()
                                        .append("message_id", new ObjectId())
                                        .append("sender", username)
                                        .append("body", body)
                                        .append("send_date", System.currentTimeMillis())
                                        .append("mentions", mentions)
                                        .append("hashtags", hashtags)
                                ))
                );
            }
        } else {
            result = new JSONObject();
            result.put("status", "ok").put("payload", new JSONObject());
            db.getCollection("chats").updateOne(new Document("_id", new ObjectId(chat_id)),
                    new Document("$push", new Document("messages", new Document()
                                    .append("message_id", new ObjectId())
                                    .append("sender", username)
                                    .append("body", body)
                                    .append("send_date", System.currentTimeMillis())
                                    .append("mentions", mentions)
                                    .append("hashtags", hashtags)
                            ))
            );
        }

        return result.toString();
    }

    ///////////////////////////////////////
    private String set_dest(String options) {
        JSONObject obj = new JSONObject(options);
        String chat_id = obj.getString("chat_id");
        int secs = obj.getInt("time");
        ObjectId id = new ObjectId(chat_id);
        JSONObject result = new JSONObject();
        result.put("status", "err").put("payload", new JSONObject());
        FindIterable<Document> iterable = db.getCollection("chats").find(eq("_id", id));
        Document document = iterable.first();
        if (document != null) {
            result = new JSONObject();
            result.put("status", "ok").put("payload", new JSONObject());
            db.getCollection("chats").updateOne(new Document("_id", id),
                    new Document("$set", new Document("private", (long) (secs * 1000)))
            );
        }
        return result.toString();
    }

    ///////////////////////////////////////
    private String search_name(String options) {
        JSONObject obj = new JSONObject(options);
        String name = obj.getString("name");
        FindIterable<Document> iterable = db.getCollection("users").find();
        List<String> search_results = new ArrayList<>();
        for (Document doc : iterable) {
            if (doc.getString("username").toLowerCase().contains(name)) {
                search_results.add(doc.getString("username"));
            }
        }
        JSONObject result = new JSONObject();
        result.put("status", "ok").put("payload", new JSONObject().put("username", search_results));
        return result.toString();
    }

    ///////////////////////////////////////
    private String search_hashtag(String options) {
        JSONObject obj = new JSONObject(options);
        String username = obj.getString("username");
        String tag = obj.getString("hashtag");
        FindIterable<Document> iterable = db.getCollection("chats").find(new Document("participants", username));
        JSONArray result_massages = new JSONArray();
        for (Document doc : iterable) {
            ArrayList<Document> messages = (ArrayList<Document>) doc.get("messages");
            for (int i = 0; i < messages.size(); i++) {
                JSONObject jo = new JSONObject(messages.get(i).toJson());
                for (int j = 0; j < jo.getJSONArray("hashtags").length(); j++) {
                    if (jo.getJSONArray("hashtags").getString(j).equals(tag)) {
                        result_massages.put(jo);
                        break;
                    }
                }

            }
        }
        JSONObject result = new JSONObject();
        result.put("status", "ok").put("payload", new JSONObject().put("messages", result_massages));
        return result.toString();
    }

    ///////////////////////////////////////
    private String make_group(String options) {
        JSONObject obj = new JSONObject(options);
        String username = obj.getString("username");
        String name = obj.getString("name");
        JSONArray participants = obj.getJSONArray("participants");
        JSONObject result = new JSONObject();
        result.put("status", "err").put("payload", new JSONObject());
        FindIterable<Document> iterable = db.getCollection("users").find(eq("group", name));
        Document document = iterable.first();
        if (document == null) {
            List<String> parts = new ArrayList<String>();
            parts.add(username);
            for (int i = 0; i < participants.length(); i++) {
                parts.add(participants.getString(i));
            }
            result = new JSONObject();
            result.put("status", "ok").put("payload", new JSONObject());
            db.getCollection("chats").insertOne(new Document()
                    .append("group", name)
                    .append("participants", parts)
                    .append("messages", asList())
            );

        }
        return result.toString();
    }

    ///////////////////////////////////////
    private String make_channel(String options) {
        JSONObject obj = new JSONObject(options);
        String username = obj.getString("username");
        String name = obj.getString("name");
        JSONArray participants = obj.getJSONArray("participants");
        JSONObject result = new JSONObject();
        result.put("status", "err").put("payload", new JSONObject());
        FindIterable<Document> iterable = db.getCollection("users").find(new Document("channel.name", name));
        Document document = iterable.first();
        if (document == null) {
            List<String> parts = new ArrayList<String>();
            parts.add(username);
            for (int i = 0; i < participants.length(); i++) {
                parts.add(participants.getString(i));
            }
            result = new JSONObject();
            result.put("status", "ok").put("payload", new JSONObject());
            db.getCollection("chats").insertOne(new Document()
                    .append("channel", new Document().append("name", name).append("admin", username))
                    .append("participants", parts)
                    .append("messages", asList())
            );
        }
        return result.toString();
    }

    private boolean autoDest() {
        Long time = System.currentTimeMillis();
        FindIterable<Document> iterable = db.getCollection("chats").find(new Document("private", new BasicDBObject("$exists", true)));
        for (Document document : iterable) {
            Long documentSelfDestTime = document.getLong("private");
            ArrayList<Document> messages = (ArrayList<Document>) document.get("messages");
            for (Document message : messages) {
                Long thisMessageDate = message.getLong("send_date");
                if (time - thisMessageDate > documentSelfDestTime) {
                    Document selectedMessage = message;
                    db.getCollection("chats").updateOne(new Document("_id", document.getObjectId("_id")),
                            new Document("$pull", new Document("messages", message))
                    );
                }
            }
        }
        return true;
    }

    private boolean unban() {
        FindIterable<Document> iterable = db.getCollection("users").find();
        for (Document document : iterable) {
            if (!"".equals(document.getString("reported_date"))) {
                Date currentTime = new Date();
                Date reportedTime = new Date(document.getString("reported_date"));
                if (currentTime.getTime() - reportedTime.getTime() > 1000000) {
                    ObjectId id = document.getObjectId("_id");
                    db.getCollection("users").updateOne(new Document("_id", id),
                            new Document("$set", new Document("reported_date", "")));
                    db.getCollection("users").updateOne(new Document("_id", id),
                            new Document("$set", new Document("reporters", asList())));
                }
            }
        }
        return true;
    }

    private class RequestHandler implements Runnable {

        private final Socket connectionSocket;

        public RequestHandler(Socket connectionSocket) {
            this.connectionSocket = connectionSocket;
        }

        private String getJSONCommand(String jsonString) {
            JSONObject obj = new JSONObject(jsonString);
            return obj.getString("command");
        }

        private String getJSONOptions(String jsonString) {
            JSONObject obj = new JSONObject(jsonString);
            return obj.getJSONObject("options").toString();
        }

        @Override
        public void run() {
            boolean whileFlag = true;
            DataOutputStream outToClient = null;
            try {
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                while (whileFlag) {
                    try {
                        String clientSentence = inFromClient.readLine();
                        unban();
                        autoDest();
                        System.out.println("CLIENT: " + clientSentence);
                        if (clientSentence == null) {
                            System.out.println("closing connection...");
                            break;
                        }
                        String command = getJSONCommand(clientSentence);
                        String options = getJSONOptions(clientSentence);
                        String response = "";
                        switch (command) {
                            case "register":
                                response = register(options);
                                break;
                            case "login":
                                response = login(options);
                                break;
                            case "profile_edit":
                                response = profile_edit(options);
                                break;
                            case "get_tree":
                                response = get_tree(options);
                                break;
                            case "add_friend":
                                response = add_friend(options);
                                break;
                            case "unfriend":
                                response = unfriend(options);
                                break;
                            case "get_messages":
                                response = get_messages(options);
                                break;
                            case "get_profile":
                                response = get_profile(options);
                                break;
                            case "report":
                                response = report(options);
                                break;
                            case "make_private":
                                response = make_private(options);
                                break;
                            case "send_message":
                                response = send_message(options);
                                break;
                            case "set_dest":
                                response = set_dest(options);
                                break;
                            case "search_name":
                                response = search_name(options);
                                break;
                            case "search_hashtag":
                                response = search_hashtag(options);
                                break;
                            case "make_group":
                                response = make_group(options);
                                break;
                            case "make_channel":
                                response = make_channel(options);
                                break;
                        }
                        System.out.println("SERVER: " + response);
                        outToClient.writeBytes(response);
                    } catch (IOException ex) {

                    }
                }
            } catch (IOException ex) {

            } finally {
                try {
                    outToClient.close();
                } catch (IOException ex) {

                }
            }
        }
    }
}
