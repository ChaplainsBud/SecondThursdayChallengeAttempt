package com.example.exercisefourohone;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid
                                          @ModelAttribute("user") User user, BindingResult result,
                                          Model model) {
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "registration";
        } else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Created");
        }
        // return "index"; I'll try a new page
        return "home";
    }

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/secure")
    public String secure(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        // display some user data with message/img
        model.addAttribute("messages", messageRepository.findAll());
        model.addAttribute("images", imageRepository.findAll());
     //   return "secure";
        return "list";
    }
// I don't want the registration data, I want them to go ...
// to a page that lists MESSAGES

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/add")
    public String messageForm(Model model){
        model.addAttribute("message", new Message());
        model.addAttribute("image", new Image());
        return "messageform";
    }

    @PostMapping("/add")
    public String processImage(@ModelAttribute Image image,
                               @RequestParam("file") MultipartFile file){
        if(file.isEmpty()){
            return "redirect:/add";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));
            image.setHeadshot(uploadResult.get("url").toString());
            imageRepository.save(image);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/add";
        }
        // changed from local host to 'list'
        return "redirect:/secure" ;}

    @PostMapping("/process")
    public String processForm(@Valid Message message,
                              BindingResult result){
        if (result.hasErrors()){
            return "messageform";
        }
        messageRepository.save(message);
        // changed from local host to 'list'
        return "redirect:/secure";
    }

    @RequestMapping("/detail/{id}")
    public String showMessage(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("message", messageRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id,
                                Model model){
        model.addAttribute("message", messageRepository.findById(id).get());
        return "messageform";
    }

    @RequestMapping("/delete/{id}")
    public String delMessage(@PathVariable("id") long id){
        messageRepository.deleteById(id);
        return "redirect:/secure";
    }


}
