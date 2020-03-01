package com.synechron.onlineacc.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.synechron.onlineacc.dao.RoleDao;
import com.synechron.onlineacc.domain.SelfServiceProjectEntity;
import com.synechron.onlineacc.domain.User;
import com.synechron.onlineacc.domain.security.UserRole;
import com.synechron.onlineacc.service.CreateProjectService;
import com.synechron.onlineacc.service.UserService;
import com.synechron.onlineacc.util.AppConstants;

@Controller
public class HomeController {
	private static final Logger L = LogManager.getLogger(HomeController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
    private RoleDao roleDao;

	@Autowired
	CreateProjectService createProjectService;
	
	@RequestMapping(AppConstants.HOME_REDIRECT_INDEX)
	public String home() {
		L.debug("35 : HomeController.home(...)");
		return "redirect:/index";
	}

	@RequestMapping(AppConstants.HOME_INDEX)
    public String index() {
		L.debug("41 : HomeController.index(...)");
        return "index";
    }
	
	@RequestMapping(value = AppConstants.HOME_SIGNUP, method = RequestMethod.GET)
    public String signup(Model model) {
		L.debug("47 : Start : HomeController.signup(...)");
        User user = new User();

        model.addAttribute("user", user);
        L.debug("51 : End : HomeController.signup(...)");
        return "signup";
    }
	
	@RequestMapping(value = AppConstants.HOME_SIGNUP, method = RequestMethod.POST)
    public String signupPost(@ModelAttribute("user") User user,  Model model) {
		L.debug("57 : Start : HomeController.signupPost(...)");
        if(userService.checkUserExists(user.getUsername(), user.getEmail()))  {

            if (userService.checkEmailExists(user.getEmail())) {
                model.addAttribute("emailExists", true);
            }

            if (userService.checkUsernameExists(user.getUsername())) {
                model.addAttribute("usernameExists", true);
            }
            L.debug("67 : End : HomeController.signupPost(...)");
            return "signup";
        } else {
        	 Set<UserRole> userRoles = new HashSet<>();
             userRoles.add(new UserRole(user, roleDao.findByName("ROLE_USER")));

            userService.createUser(user, userRoles);
            L.debug("74 : End : HomeController.signupPost(...)");
            return "redirect:/";
        }
    }

	@RequestMapping(AppConstants.HOME_USER_FRONT)
	public String userFront(Principal principal, Model model) {
		L.debug("81 : Start : HomeController.userFront(...)");
		List<SelfServiceProjectEntity> lstProjects = null;

		try {
			lstProjects = createProjectService.get(principal);
		} catch (Exception e) {
			L.error("HomeController.userFront(...) : Exception e = {}", e);
		}

        model.addAttribute("projects", lstProjects);
		L.debug("88 : End : HomeController.userFront(...) : lstProjects.size() = {}", ((null != lstProjects)?lstProjects.size():0));
		return "userFront";
	}
}
