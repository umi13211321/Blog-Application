package com.umesh.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.umesh.dao.PostDao;
import com.umesh.entity.Comment;
import com.umesh.entity.Post;
import com.umesh.entity.Tag;
import com.umesh.service.PostService;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping("/")
    public String redirect() {
        return "redirect:/dashboard";
    }

    @RequestMapping("/dashboard")
    public String listPosts(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo, Model model) {
        System.out.println("Inside Dashboard");
        int pageLimit = 5;
        int start = 0;
        int end = 0;
        List<Post> listOfPosts = postService.getPostData();
        List<Post> posts = new ArrayList<>();
        model.addAttribute("previous", pageNo-1);
        if (pageNo == 0) {
            end = start + pageLimit;
        } else {
            end = (pageNo+1)*pageLimit;
            start = end - 5;
        }

        if(listOfPosts.size()>end){
            posts = listOfPosts.subList(start,end);
            model.addAttribute("next", pageNo + 1);
        }
        else{
            posts = listOfPosts.subList(start, listOfPosts.size()-1);
            model.addAttribute("next", 0);
        }
        model.addAttribute("posts", posts);
        List<Tag> listOfTags = postService.getTags();
        model.addAttribute("tags", listOfTags);

        List<String> listOfAuthors = postService.getAuthors();
        model.addAttribute("authors", listOfAuthors);
        return "dashboard";
    }

    @RequestMapping("/showPageForWriting")
    public String showPageForWriting(Model model) {
        Post thePost = new Post();
        model.addAttribute("story", thePost);
        return "write-story";
    }

    @RequestMapping("/saveStory")
    public String saveStory(@RequestParam("storyId") String id, @RequestParam("tagDetail") String theTag, @ModelAttribute("story") Post thePost) {
        System.out.println("inside save story controller");
        postService.saveStory(Integer.parseInt(id), thePost);
        postService.saveTag(thePost.getId(), theTag);
        return "redirect:/dashboard";
    }

    @RequestMapping("/viewCompleteStory")
    public String viewCompleteStory(@RequestParam("storyId") String id, Model model) {
        System.out.println(id);
        Post thePost = postService.getPostData(Integer.parseInt(id));
        Comment theComment = new Comment();
        model.addAttribute("story", thePost);
        model.addAttribute("comment", theComment);
        return "view-story";
    }

    @RequestMapping("/updatePost")
    public String updatePost(@RequestParam("storyId") String id,
                             Model theModel) {
        System.out.println("inside update post controller");
        List<Tag> listOfTags = postService.getTags();
        Post post = postService.getPost(Integer.parseInt(id));
        theModel.addAttribute("story", post);
        theModel.addAttribute("tags", listOfTags);
        return "write-story";
    }

    @RequestMapping("/deleteStory")
    public String deleteStory(@RequestParam("storyId") String id) {
        postService.deleteStory(Integer.parseInt(id));
        return "redirect:/dashboard";
    }


    @RequestMapping("/saveComment")
    public String saveComment(@RequestParam("storyId") String id, @ModelAttribute("comment") Comment theComment, Model model) {
        postService.saveComment(Integer.parseInt(id), theComment);
        model.addAttribute("storyId", id);
        return "redirect:/viewCompleteStory";
    }

    @RequestMapping("/updateComment")
    public String updateComment(@RequestParam("commentId") String commentId,
                                @RequestParam("storyId") String storyId, Model model) {
        System.out.println("inside update comment controller");
        System.out.println("Comment id :" + commentId);
        System.out.println("Stroy id :" + storyId);
        Post post = postService.getPost(Integer.parseInt(storyId));
        Comment comment = postService.getComment(Integer.parseInt(commentId));

        model.addAttribute("thePost", post);
        model.addAttribute("theComment", comment);
        return "update-comment";
    }

    @RequestMapping("/update")
    public String update(@RequestParam("storyId") String storyId, @RequestParam("commentId") String commentId, @ModelAttribute("comment") Comment theComment, Model model) {
        postService.saveUpdatedComment(Integer.parseInt(storyId), Integer.parseInt(commentId), theComment);
        model.addAttribute("storyId", storyId);
        return "redirect:/viewCompleteStory";
    }

    @RequestMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentId") String commentId,
                                @RequestParam("storyId") String storyId, Model model) {

        postService.deleteComment(Integer.parseInt(storyId), Integer.parseInt(commentId));
        model.addAttribute("storyId", storyId);
        return "redirect:/viewCompleteStory";
    }

    @RequestMapping("/search")
    public String searchCustomers(@RequestParam(value = "pageNo", defaultValue = "1", required=false) int pageNo,
                                  @RequestParam(value="theSearchName", required=false) String searchName,
                                  Model model) {
        System.out.println("checkpoint inside search: "+searchName);
        System.out.println("Page No: "+pageNo);
        int end = 5*pageNo;
        int start = end - 5;

        int pageLimit = 5;
        List<Post> listOfPosts = postService.searchPosts(searchName);
        List<Post> posts = new ArrayList<>();

        int size = listOfPosts.size();
        int totalPage = 0;
        if(size%5==0){
            totalPage = size/5;
        }
        else{
            totalPage = size/5 + 1;
        }

        List<Integer> list = new ArrayList<>();
        for(int i=1; i<=totalPage;i++){
            list.add(i);
        }

        if(listOfPosts.size()>end){
            posts = listOfPosts.subList(start,end);
        }
        else {
            posts = listOfPosts.subList(start, listOfPosts.size() - 1);
        }

        List<Tag> listOfTags = postService.getTags();
        List<String> listOfAuthors = postService.getAuthors();
        model.addAttribute("authors", listOfAuthors);
        model.addAttribute("list", list);
        model.addAttribute("posts", posts);
        model.addAttribute("tags", listOfTags);
        model.addAttribute("theSearchName", searchName);

        return "dashboardSearchResult";
    }

    @RequestMapping("/sorting")
    public String sorting(@RequestParam(value="sort", defaultValue = "1") String value,
                          @RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model model) {

        System.out.println("checkpoint inside search: "+value);
        System.out.println("Page No: "+pageNo);
        int end = 5*pageNo;
        int start = end - 5;

        int pageLimit = 5;
        List<Post> listOfPosts = postService.getListOfPosts(Integer.parseInt(value));
        List<Post> posts = new ArrayList<>();

        int size = listOfPosts.size();
        int totalPage = 0;
        if(size%5==0){
            totalPage = size/5;
        }
        else{
            totalPage = size/5 + 1;
        }

        List<Integer> list = new ArrayList<>();
        for(int i=1; i<=totalPage;i++){
            list.add(i);
        }

        if(listOfPosts.size()>end){
            posts = listOfPosts.subList(start,end);
        }
        else {
            posts = listOfPosts.subList(start, listOfPosts.size() - 1);
        }

        List<Tag> listOfTags = postService.getTags();
        List<String> listOfAuthors = postService.getAuthors();
        model.addAttribute("authors", listOfAuthors);
        model.addAttribute("list", list);
        model.addAttribute("posts", posts);
        model.addAttribute("tags", listOfTags);
        model.addAttribute("sort", value);

        return "dashboardSortingResult";
    }

    @RequestMapping("/filter")
    public String filter(@RequestParam(value = "pageNo", defaultValue = "1", required=false) int pageNo,
                         @RequestParam(value = "filter", required = false) String tag,
                         @RequestParam(value = "filterAuthor", required = false) String author, Model model) {
        List<Post> listOfPosts = new ArrayList<>();

        if (tag == null) {
            tag = "xyzxyzxyz";
            listOfPosts = postService.getFilteredPostData(tag, author);
        }
        if (author == null) {
            author = "xyzxyzxyz";
            listOfPosts = postService.getFilteredPostData(tag, author);
        } else {
            listOfPosts = postService.getFilteredPostData(tag, author);
        }

        System.out.println("Page No: "+pageNo);
        int end = 5*pageNo;
        int start = end - 5;
        int pageLimit = 5;

        List<Post> posts = new ArrayList<>();

        int size = listOfPosts.size();
        int totalPage = 0;
        if(size%5==0){
            totalPage = size/5;
        }
        else{
            totalPage = size/5 + 1;
        }

        List<Integer> list = new ArrayList<>();
        for(int i=1; i<=totalPage;i++){
            list.add(i);
        }

        if(listOfPosts.size()>end){
            posts = listOfPosts.subList(start,end);
        }
        else {
            posts = listOfPosts.subList(start, listOfPosts.size() - 1);
        }

        List<Tag> listOfTags = postService.getTags();
        List<String> listOfAuthors = postService.getAuthors();
        model.addAttribute("authors", listOfAuthors);
        model.addAttribute("list", list);
        model.addAttribute("posts", posts);
        model.addAttribute("tags", listOfTags);
        model.addAttribute("filterAuthor",author);
        model.addAttribute("filter",tag);

        return "dashboardFilterResult";

    }
}
