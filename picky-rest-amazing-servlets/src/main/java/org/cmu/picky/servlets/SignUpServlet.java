package org.cmu.picky.servlets;

import com.google.gson.Gson;
import org.cmu.picky.model.Error;
import org.cmu.picky.model.User;
import org.cmu.picky.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignUpServlet extends HttpServlet {

	private static UserService userService;

	public static void init(UserService _userService) {
		userService = _userService;
	}

	public static final int BAD_STATUS = 400;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		response.setContentType("application/json");
		response.setHeader("Content-Type", "text/plain; charset=UTF-8");

		Error error = new Error();
		User signUpUser = null;
		Gson gson = new Gson();

		if (username != null && !username.equals("") && password != null && !password.equals("")) {
			if (userService.usernameInUser(username)) {
				error.setMessage("Username in use");
				response.getOutputStream().print(gson.toJson(error));
				response.setStatus(BAD_STATUS);
			} else {
				userService.signUp(username, password);
				signUpUser = userService.login(username, password);
				response.getOutputStream().print(gson.toJson(signUpUser));
			}
		}
		response.getOutputStream().flush();
	}
}