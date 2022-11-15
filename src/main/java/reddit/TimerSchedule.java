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
        System.out.println("the Scheduler is running"+ new Date());
        if(Storage.newCommentQueue.size()!=0 || Storage.newPostQueue.size()!=0 || Storage.editPostQueue.size()!=0 || Storage.editCommentQueue.size()!=0 || Storage.newLikeQueue.size()!=0 || Storage.editLikeQueue.size()!=0 || Storage.MessagesQueue.size()!=0){
            try {
                if(Storage.newPostQueue.size()!=0) {
                    StorageMethods.updateDBPosts();
                }if(Storage.newCommentQueue.size()!=0) {
                    StorageMethods.updateDBComments();
                }
                if(Storage.editCommentQueue.size()!=0){
                    StorageMethods.updateEditDBComments();
                }
                if(Storage.editPostQueue.size()!=0){
                    StorageMethods.UpdateEditDBPosts();
                }
                if(Storage.newLikeQueue.size()!=0){
                    StorageMethods.createNewLike();
                }
                if(Storage.editLikeQueue.size()!=0){
                    StorageMethods.UpdateLikeDB();
                }
                if(Storage.MessagesQueue.size()!=0){
                    StorageMethods.updateMessagesDB();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            System.out.println("No Data To Update");
        }
    }
}