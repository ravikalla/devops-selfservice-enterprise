package com.synechron.onlineacc.service.UserServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synechron.onlineacc.dao.RoleDao;
import com.synechron.onlineacc.dao.UserDao;
import com.synechron.onlineacc.domain.User;
import com.synechron.onlineacc.domain.security.UserRole;
import com.synechron.onlineacc.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	public void save(User user) {
        userDao.save(user);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }
    
    
    public User createUser(User user, Set<UserRole> userRoles) {
        User localUser = userDao.findByUsername(user.getUsername());

        if (localUser != null) {
            LOG.info("User with username {} already exist. Nothing will be done. ", user.getUsername());
        } else {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            Set<UserRole> setUserRoles = user.getUserRoles();
            if (null == setUserRoles)
            		setUserRoles = new HashSet<UserRole>();
            	setUserRoles.addAll(userRoles);
            	user.setUserRoles(setUserRoles);

            for (UserRole ur : userRoles) {
                roleDao.save(ur.getRole());
            }

            localUser = userDao.save(user);
        }

        return localUser;
    }
    
    public boolean checkUserExists(String username, String email){
        return (checkUsernameExists(username) || checkEmailExists(username));
    }

    public boolean checkUsernameExists(String username) {
        return (null != findByUsername(username));
    }
    
    public boolean checkEmailExists(String email) {
        return (null != findByEmail(email));
    }

    public User saveUser (User user) {
        return userDao.save(user);
    }
    
    public List<User> findUserList() {
        return userDao.findAll();
    }

    public void enableUser (String username) {
        User user = findByUsername(username);
        user.setEnabled(true);
        userDao.save(user);
    }

    public void disableUser (String username) {
        User user = findByUsername(username);
        user.setEnabled(false);
        System.out.println(user.isEnabled());
        userDao.save(user);
        System.out.println(username + " is disabled.");
    }
}
