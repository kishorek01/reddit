package reddit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Storage {
    protected static ConcurrentHashMap<String, User> users=new ConcurrentHashMap<>();
    protected static CopyOnWriteArrayList<String> registeredEmails=new CopyOnWriteArrayList<>();
    protected static ConcurrentHashMap<String,Posts> posts=new ConcurrentHashMap<>();
    protected static ConcurrentHashMap<String,Comments> comments= new ConcurrentHashMap<>();
}
