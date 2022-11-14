package reddit;

import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Storage {
    protected static ConcurrentHashMap<String, User> users=new ConcurrentHashMap<>();
    protected static CopyOnWriteArrayList<String> registeredEmails=new CopyOnWriteArrayList<>();
    protected static ConcurrentHashMap<String,Posts> posts=new ConcurrentHashMap<>();
    protected static ConcurrentHashMap<String,Comments> comments= new ConcurrentHashMap<>();
    protected static ConcurrentLinkedQueue<String> commentQueue=new ConcurrentLinkedQueue<>();
    static {
//        registerQueue.put("Register",new ConcurrentLinkedQueue<>());
//        registerQueue.put("Posts",new ConcurrentLinkedQueue<>());
//        registerQueue.put("Comments",new ConcurrentLinkedQueue<>());
//        registerQueue.put("Messages",new ConcurrentLinkedQueue<>());
        TimerSchedule t1=new TimerSchedule("Task");
        Timer t=new Timer();
        t.scheduleAtFixedRate(t1, 0,10000);
    }
}


