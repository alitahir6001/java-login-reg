package com.pakfro.loginRegDemo.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.pakfro.loginRegDemo.models.User;
import com.pakfro.loginRegDemo.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
//     register user and hash their password
    public User registerUser(User user) {
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);
        return userRepository.save(user);
    }
    
    // find user by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // find user by id
    public User findUserById(Long id) {
    	return userRepository.findById(id).orElse(null);
    }
    

    public boolean authenticateUser(String email, String password) {
        // first find the user by email
        User user = userRepository.findByEmail(email);
        // if we can't find it by email, return false
        if(user == null) {
            return false;
        } else {
            // if the passwords match, return true, else, return false
            if(BCrypt.checkpw(password, user.getPassword())) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //    // authenticate user
//    public boolean authenticateUser(String email, String password) {
//        // first find the user by email
////    	System.out.println("******* FIRST STEP OF AuthenticateUser*****");
//        User user = userRepository.findByEmail(email);
//        System.out.println(user.getEmail());
//        // if we can't find it by email, return false
//        if(user == null) {
////        	System.out.println("******* WE DIDNT FIND THE  USER*****");
//
//            return false;
//        } else {
////        	System.out.println(password);
////        	System.out.println("******* IN THE ELSE *****");
////        	System.out.println();
////        	System.out.println(BCrypt.checkpw("$2a$10$DreoaERvLKQciYdnyVdzt.BjZ2tOpl30tGKID1Wj98UafVPyO2iS6", user.getPassword() ));
//
//            // if the passwords match, return true, else, return false
//            if(BCrypt.checkpw(password, user.getPassword())) {
//            	System.out.println("******* THE PASSWORDS MATCH *****");
//
//
//                return true;
//            } else {
//            	System.out.println(user.getPassword());
//            	System.out.println("*******PASSWORDS DO NOT MATCH *****");
//                return false;
//            }
//        }
//    }
}