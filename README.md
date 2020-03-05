1. The home page shall display a list of all messages

//

@RequestMapping()
private String index(Model model){
model.addAttribute("users", userRepository.findAll());
return "index";
}

2. Make the users login for any page they access (easier for you all)

//

Security Template:

i. After registering, there is no record of user. 
ii. I register, and then go to the login page.
iii. The login page says invalid user.


 
3. Users shall sign up using a registration page

//

4. Users shall be logged in to create a post or edit a post

//

5. Every page (or template) should have a link (or button) to add a new message. This link shall be visible only when the user is logged in. (check on security thymeleaf extras)

//

6. There shall be a link to Logout which, when clicked, logs out the user and redirects them to the login page. This link shall only be visible when the user is logged in.

//

7. Implement Bootstrap to make the site look good

//

8. Use a  database to store your messages 

//

9. Pre-loading data is recommended but not required. You probably want to modify the data loader to execute only once.

//

10. Display the logged in username on every page (or in the navigation menu) where they are logged in.

//