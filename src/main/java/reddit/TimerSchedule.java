package reddit;

import java.util.Date;
import java.util.TimerTask;

public class TimerSchedule extends TimerTask {

    private String name;

    public TimerSchedule(String n) {
        this.name = n;
    }

    @Override
    public void run() {

            try {
                if(Storage.newPostQueue.size()!=0) {
                    StorageMethods.updateDBPosts();
                    System.out.println("All New Posts Created in DB at "+ new Date());
                }if(Storage.newCommentQueue.size()!=0) {
                    StorageMethods.updateDBComments();
                    System.out.println("All New Comments Created in DB at "+ new Date());
                }
                if(Storage.editCommentQueue.size()!=0){
                    StorageMethods.updateEditDBComments();
                    System.out.println("All Comments Edited in DB at "+ new Date());
                }
                if(Storage.editPostQueue.size()!=0){
                    StorageMethods.UpdateEditDBPosts();
                    System.out.println("All Posts Edited in DB at "+ new Date());
                }
                if(Storage.newLikeQueue.size()!=0){
                    StorageMethods.createNewLike();
                    System.out.println("All Likes Created in DB at "+ new Date());
                }
                if(Storage.editLikeQueue.size()!=0){
                    StorageMethods.UpdateLikeDB();
                    System.out.println("All Likes Edited in DB at "+ new Date());
                }
                if(Storage.MessagesQueue.size()!=0){
                    StorageMethods.updateMessagesDB();
                    System.out.println("All Messages Created in DB at "+ new Date());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

    }
}