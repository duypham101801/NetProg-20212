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
@WebServlet("/index")
public class ProjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ProjectServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (CSConnection.keySympReq == true) {
			CSConnection.RequestSymp();
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} else {
			response.sendRedirect("/netprog/login");
		}
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("SignOut") != null) {
			System.out.println("SignOut and Reset Connection");
			CSConnection.Reset();
			response.sendRedirect("/netprog/login");
		} else if (request.getParameter("submit1") != null) {
			String [] checkedValues1 = request.getParameterValues("btncheck");
			CSConnection.SubmitSymp(checkedValues1);
			CSConnection.RequestAnswers();
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} else if (request.getParameter("submit2") != null) {
			String [] checkedValues2 = request.getParameterValues("btnYes");
			CSConnection.SubmitAns(checkedValues2);
			CSConnection.RequestDiag();
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
	}

}
