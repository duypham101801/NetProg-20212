package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PatientChatServlet
 */
@WebServlet("/chat-doc")
public class DoctorChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DoctorChatServlet() {
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DSConnection.main();
		request.getRequestDispatcher("/chat-doc.jsp").forward(request, response);
		while (true) {
			if (DSConnection.update == 1) {
				DSConnection.update = 0;
				request.getRequestDispatcher("/chat-doc.jsp").forward(request, response);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getParameter("SignOut") != null) {
			System.out.println("SignOut and Reset Connection");
			CSConnection.Reset();
			response.sendRedirect("/netprog/login");
		}
	}

}
