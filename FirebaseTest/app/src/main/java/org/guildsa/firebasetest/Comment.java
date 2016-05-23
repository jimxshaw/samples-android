package org.guildsa.firebasetest;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Comment
{

    public String uid;
    public String author;
    public String text;

    public Comment()
    {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text)
    {
        this.uid = uid;
        this.author = author;
        this.text = text;
    }

}

//@IgnoreExtraProperties
//public class Comment {
//
//    public String uid;
//    public String message;
//
//
//    public Comment() {
//        // Default constructor required for calls to DataSnapshot.getValue(User.class)
//    }
//
//    public Comment(String uid, String message) {
//        this.uid = uid;
//        this.message = message;
//    }
//
//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("uid", uid);
//        result.put("message", message);
//
//        return result;
//    }
//}
