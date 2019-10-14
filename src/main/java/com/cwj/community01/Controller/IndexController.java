package com.cwj.community01.Controller;

import com.cwj.community01.mapper.UserMapper;
import com.cwj.community01.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie:cookies){
                if(cookie.getName().equals("token")){
                    User user=userMapper.findUserByToken(cookie.getValue());
                    System.out.println(user);
                    request.getSession().setAttribute("user",user);
                }
            }
        }
        return "index";
    }
}
