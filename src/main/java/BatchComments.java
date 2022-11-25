import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.BatchComment;
import reddit.StorageMethods;

import java.io.IOException;
import java.io.Serial;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Servlet implementation class AddLikes
 */
public class BatchComments extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BatchComments() {
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
            Type listType = new TypeToken<List<BatchComment>>() {}.getType();
            List<BatchComment> list = new Gson().fromJson(data, listType);
            try {
                for(BatchComment b:list){
                    JsonObject singleData=new Gson().toJsonTree(b,BatchComment.class).getAsJsonObject();
                    String commentid=singleData.get("commentid").getAsString();
                    String postid=singleData.get("postid").getAsString();
                    String comment=singleData.get("comment").getAsString();
                    String parentcomment;
                    if(singleData.get("parentcomment")==null || singleData.get("parentcomment").isJsonNull()){
                        parentcomment=null;
                    }else{
                        parentcomment=singleData.get("parentcomment").getAsString();
                    }
                    String created_at=singleData.get("created_at").getAsString();
                    String updated_at=singleData.get("updated_at").getAsString();
                    if(type.equalsIgnoreCase("new")) {
                            StorageMethods.postBatchComments(commentid,username, comment, postid, parentcomment, created_at, updated_at);
                    }else if(type.equalsIgnoreCase("edit")){
                        StorageMethods.editBatchComments(comment,commentid,postid);
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