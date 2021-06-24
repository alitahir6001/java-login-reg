package com.pakfro.loginRegDemo.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pakfro.loginRegDemo.models.User;
import com.pakfro.loginRegDemo.services.UserService;
import com.pakfro.loginRegDemo.validation.UserValidator;

@Controller

public class HomeController {

	private final UserService userServ; // Bring in the Services file here
	private final UserValidator userVal;

	public HomeController(UserService userServ, UserValidator userVal) {
		this.userServ = userServ;
		this.userVal = userVal;
	}

	@GetMapping("/")
	public String home(@ModelAttribute("user") User user) {
		return "index.jsp";
	}

	@PostMapping("/registration")
	public String register(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
		userVal.validate(user, result);
		if (result.hasErrors()) { // If there are errors on the registration page
			return "index.jsp"; // Go back to the registration page (in this case, index.jsp).
		}
		// TODO: Later, after log/reg works, prevent duplicate emails

		// Create a user with this information
		this.userServ.registerUser(user);

		// get the id of the user that just got created and put it in session
		User newUser = userServ.registerUser(user);
		session.setAttribute("userID", newUser.getId());

		return "redirect:/dashboard";
	}

	// Once you're a registered user, or logged in, go to the dashboard
	@GetMapping("/dashboard")
	public String dashboard(Model model, HttpSession session) {
		// Retrieve the user object from the db whose ID matches the ID stored in
		// session

		Long id = (Long) session.getAttribute("userID");
		User loggedInUser = this.userServ.findUserById(id);

		model.addAttribute("loggedinuser", loggedInUser);
		return "dashboard.jsp";
	}

	

	// Logout Route

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	// Login Route

	@PostMapping("/login")
	public String login(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Boolean isLegit = this.userServ.authenticateUser(email, password);
		System.out.println("********** IN LOGIN ROUTE *******");
		if (isLegit) {
			System.out.println("****** IN LEGIT *****");
			// if the email/password combo is correct, log them in with session and redirect
			// to dashboard.
			
			// Get the user with that email
			User user = this.userServ.findByEmail(email);
			
			// put that ID into session, using the same session key (variable name) as
			// whatever name you used when you registered
			session.setAttribute("userID", user.getId());
			return "redirect:/dashboard";
		} else {
			redirectAttributes.addFlashAttribute("error", "Invalid login credentials.");
			return "redirect:/";
		}
}
	}
