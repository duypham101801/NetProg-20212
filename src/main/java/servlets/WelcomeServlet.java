package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WelcomeServlet
 */
@WebServlet("/login")
public class WelcomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public WelcomeServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		if (request.getAttribute("result") == null) {
//			CSConnection.main();
//		}
		if (CSConnection.s == null) {
			CSConnection.main();
		}
        request.getRequestDispatcher("/login.jsp").forward(request, response);
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int logInResult = CSConnection.LogIn(request.getParameter("Username"),request.getParameter("Password"));
		if (logInResult == 1) {
//			request.getRequestDispatcher("/index.jsp").forward(request, response);
			CSConnection.setActiveUsername(request.getParameter("Username"));
			response.sendRedirect("/netprog/index");
		} else if (logInResult == 2){
			CSConnection.setActiveUsername(request.getParameter("Username"));
			response.sendRedirect("/netprog/doc");
		} else {
			request.setAttribute("result", logInResult);
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}

}
