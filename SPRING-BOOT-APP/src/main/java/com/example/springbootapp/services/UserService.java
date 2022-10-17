package com.example.springbootapp.services;

import com.example.springbootapp.entities.User;
import com.example.springbootapp.exceptions.ResourceNotFoundException;
import com.example.springbootapp.repos.CommentRepository;
import com.example.springbootapp.repos.LikeRepository;
import com.example.springbootapp.repos.PostRepository;
import com.example.springbootapp.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private LikeRepository likeRepository;

    public UserService(UserRepository userRepository,PostRepository postRepository,CommentRepository commentRepository,LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.postRepository=postRepository;
        this.commentRepository=commentRepository;
        this.likeRepository=likeRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveOneUser(User newUser) {
        if(newUser.getUserName()==""){
            return null;
        }
        return userRepository.save(newUser);
    }

    public User getOneUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Not found user with id = " + userId));
    }

    public User updateOneUser(Long userId, User newUser) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Not found user with id = " + userId));
            if(newUser.getUserName()==null & newUser.getPassword()==null){
                user.setAvatar(newUser.getAvatar());
                userRepository.save(user);
            }
            if(newUser.getUserName()!=null & newUser.getPassword()!=null){
                user.setUserName(newUser.getUserName());
                user.setPassword(newUser.getPassword());
                user.setAvatar(newUser.getAvatar());
                userRepository.save(user);
            }
            return user;
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User getOneUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public List<Object> getUserActivity(Long userId) {
        List<Long> postIds=postRepository.findTopByUserId(userId);
        if(postIds.isEmpty())
            return null;
        List<Object> comments= commentRepository.findUserCommentsByPostId(postIds);
        List<Object> likes=likeRepository.findUserLikesByPostId(postIds);
        List<Object> result=new ArrayList<>();
        result.addAll(comments);
        result.addAll(likes);
        return result;

    }
}
