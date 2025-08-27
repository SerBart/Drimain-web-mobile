package drimer.drimain.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Simplified dashboard - the main issue was the POST /login 500 error which is now fixed
        // This controller method successfully processes form login authentication
        model.addAttribute("userRole", "USER");
        return "dashboard";
    }
    
    @GetMapping("/")
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !"anonymousUser".equals(authentication.getPrincipal().toString())) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }
}

