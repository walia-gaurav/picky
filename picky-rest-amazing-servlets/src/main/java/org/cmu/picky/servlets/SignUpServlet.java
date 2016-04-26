package org.cmu.picky.servlets;

import com.google.gson.Gson;
import org.cmu.picky.model.Error;
import org.cmu.picky.model.User;
import org.cmu.picky.services.UserService;
import org.cmu.picky.util.ServletUtils;

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

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		ServletUtils.addJSONSettings(response);
		Gson gson = new Gson();

		if (username != null && !username.equals("") && password != null && !password.equals("")) {
			if (userService.usernameInUse(username)) {
				ServletUtils.addError(response, gson, "Username in use");
			} else {
				userService.signUp(username, password);
				User signUpUser = userService.login(username, password);
				response.getOutputStream().print(gson.toJson(signUpUser));
			}
		}
		response.getOutputStream().flush();
	}



}