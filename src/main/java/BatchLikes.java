import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.BatchLike;
import reddit.StorageMethods;

import java.io.IOException;
import java.io.Serial;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Servlet implementation class AddLikes
 */
public class BatchLikes extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BatchLikes() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=SessionManager.validateSession(request,response);
        if(username!=null) {
            String data = request.getParameter("data");
            String type = request.getParameter("type");
            Type listType = new TypeToken<List<BatchLike>>() {}.getType();
            List<BatchLike> list = new Gson().fromJson(data, listType);
            try {
                for(BatchLike b:list){
                    JsonObject singleData=new Gson().toJsonTree(b,BatchLike.class).getAsJsonObject();
                    String likeid=singleData.get("likeid").getAsString();
                    String postid=singleData.get("postid").getAsString();
                    String commentid;
                    if(singleData.get("commentid")==null || singleData.get("commentid").isJsonNull()){
                        commentid=null;
                    }else{
                        commentid=singleData.get("commentid").getAsString();
                    }
                    String created_at=singleData.get("created_at").getAsString();
                    String updated_at=singleData.get("updated_at").getAsString();
                    boolean status = singleData.get("status").getAsBoolean();
                    String likeType;
                    if(commentid==null || commentid.equalsIgnoreCase("null")){
                        likeType="post";
                    }else{
                        likeType="comment";
                    }
                    if(type.equalsIgnoreCase("new")) {
                        if(likeType.equalsIgnoreCase("post")){
                            StorageMethods.addLikesToPostsBatch(likeid,postid,commentid,status,username,created_at,updated_at);
                        }else {
                            StorageMethods.addLikesToCommentsBatch(likeid, postid, commentid, status, username, created_at, updated_at);
                        }
                    }else if(type.equalsIgnoreCase("edit")){
                        StorageMethods.editLikesBatch(likeid,status,postid,commentid);
                    }
                }
                StorageMethods.throwSuccess(request,response);

            } catch (Exception e) {
                e.printStackTrace();
                StorageMethods.throwUnknownError(request, response);
            }
        }else{
            StorageMethods.throwSessionExpired(request,response);
        }

    }


}