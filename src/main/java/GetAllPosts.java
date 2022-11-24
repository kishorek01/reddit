import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.StorageMethods;

import java.io.IOException;

/**
 * Servlet implementation class GetAllPosts
 */
public class GetAllPosts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAllPosts() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username=SessionManager.validateSession(request,response);
		if(username!=null) {
			try {
//			StorageMethods.getAllPosts(request,response);
				String sortType = request.getParameter("sort_type");
				if(sortType==null){
					sortType="new";
				}
				StorageMethods.sortAllPosts(sortType,request,response);
		}catch(Exception e) {
				e.printStackTrace();
			StorageMethods.throwUnknownError(request,response);
		}
		}else{
			StorageMethods.throwSessionExpired(request,response);
		}





	}

}
