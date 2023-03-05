package com.umesh.restController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.ObjLongConsumer;

import com.umesh.entity.Comment;
import com.umesh.service.UserService;
import com.umesh.users.RestComment;
import com.umesh.users.RestPosts;
import com.umesh.entity.Post;
import com.umesh.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
public class PostRestController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;
    
    @GetMapping("/post")
    public List<RestPosts> posts() {
        List<Post> listOfPosts = postService.getPostData();
        List<RestPosts> listOfRestPosts = new ArrayList<>();
        for (Post post : listOfPosts) {
            RestPosts restPosts = new RestPosts();
            restPosts.setTitle(post.getTitle());
            restPosts.setContent(post.getContent());
            restPosts.setAuthor(post.getAuthor());
            listOfRestPosts.add(restPosts);
        }
        return listOfRestPosts;
    }

    @GetMapping("/post/{postId}")
    public RestPosts getPost(@PathVariable int postId) {
        RestPosts restPosts = new RestPosts();
        Post post = postService.getPost(postId);
        restPosts.setTitle(post.getTitle());
        restPosts.setContent(post.getContent());
        restPosts.setAuthor(post.getAuthor());
        return restPosts;
    }

    @PostMapping("/post")
    public String savePost(@RequestBody RestPosts theRestPost) {
        Authentication authorize = SecurityContextHolder.getContext().getAuthentication();
        if(authorize.isAuthenticated()) {
            Post post = new Post();
            post.setAuthor(theRestPost.getAuthor());
            post.setTitle(theRestPost.getTitle());
            post.setContent(theRestPost.getContent());
            postService.saveStory(0, post);
            postService.saveTag(post.getId(), theRestPost.getTags());
            return "Post is Saved Successfully";
        }
        else{
            return "Post can't be saved";
        }
    }

    @PutMapping("/post/{postId}")
    public String updatePost(@RequestBody RestPosts restPosts, @PathVariable int postId) {
        Authentication authorize = SecurityContextHolder.getContext().getAuthentication();
        if((authorize.isAuthenticated()) && (authorize.getName().equals(postService.getPost(postId).getUser().getUsername()))) {
            Post post = new Post();
            post.setAuthor(restPosts.getAuthor());
            post.setTitle(restPosts.getTitle());
            post.setContent(restPosts.getContent());
            postService.saveStory(postId, post);
            postService.saveTag(postId, restPosts.getTags());
            return "Story is updated";
        }
        else {
            return "Story can't be updated";
        }
    }

    @DeleteMapping("/post/{postId}")
    public String deletePost(@PathVariable int postId) {
        Authentication authorize = SecurityContextHolder.getContext().getAuthentication();
        if((authorize.isAuthenticated()) && (authorize.getName().equals(postService.getPost(postId).getUser().getUsername()))) {
            postService.deleteStory(postId);
            return "Story is deleted";
        }
        else{
            return "Story can't be deleted";
        }
    }

    @GetMapping("/comment/{commentId}")
    public RestComment getComment(@PathVariable int commentId){
        Comment comment = postService.getComment(commentId);
        RestComment restComment = new RestComment();
        restComment.setName(comment.getName());
        restComment.setComment(comment.getComment());
        restComment.setEmail(comment.getEmail());
        return restComment;
    }

    @PostMapping("/comment/{postId}")
    public void saveComment(@RequestBody RestComment restComment, @PathVariable int postId) {
        Comment comment = new Comment();
        comment.setName(restComment.getName());
        comment.setComment(restComment.getComment());
        comment.setEmail(restComment.getEmail());

        postService.saveComment(postId, comment);
    }

    @PutMapping("/comment/{storyId}/{commentId}")
    public String updateComment(@RequestBody RestComment restComment,
                              @PathVariable("postId") int postId,
                              @PathVariable("commentId") int commentId){
        Authentication authorize = SecurityContextHolder.getContext().getAuthentication();
        if((authorize.isAuthenticated()) && (authorize.getName().equals(postService.getPost(postId).getUser().getUsername()))) {
            String name = restComment.getName();
            String comment = restComment.getComment();
            String email = restComment.getEmail();
            postService.updateCommentREST(name, comment, email, commentId);
            return "Comment is updated";
        }
        else{
            return "Comment can't be updated";
        }
    }

    @DeleteMapping("/comment/{storyId}/{commentId}")
    public String deleteComment(@PathVariable("postId") int postId, @PathVariable("commentId") int commentId){
        Authentication authorize = SecurityContextHolder.getContext().getAuthentication();
        if((authorize.isAuthenticated()) && (authorize.getName().equals(postService.getPost(postId).getUser().getUsername()))) {
            postService.deleteComment(postId, commentId);
            return "Comment is Deleted";
        }
        else{
            return "Comment can't be deleted";
        }
    }
}
