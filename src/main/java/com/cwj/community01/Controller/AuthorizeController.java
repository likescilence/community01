package com.cwj.community01.Controller;

import com.cwj.community01.dto.AccessTokenDTO;
import com.cwj.community01.dto.GithubUser;
import com.cwj.community01.mapper.UserMapper;
import com.cwj.community01.model.User;
import com.cwj.community01.provider.GithubProvider;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;
    @Value("${github.Client_id}")
    private String Client_id;
    @Value("${github.Client_secret}")
    private String Client_secret;
    @Value("${github.Redirect_uri}")
    private String Redirect_uri;
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code, @RequestParam(name="state")String state, HttpServletRequest request, HttpServletResponse response){
        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setClient_id(Client_id);
        accessTokenDTO.setClient_secret(Client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(Redirect_uri);
        accessTokenDTO.setState(state);
        String token=githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubuser = githubProvider.getUser(token);
        if(githubuser!=null){
            User user=new User();
            user.setAccountId(String.valueOf(githubuser.getId()));
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubuser.getName());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",user.getToken()));
            //request.getSession().setAttribute("githubuser",githubuser);
            return "redirect:/";
        }
        else{
            return "redirect:/";
        }
    }
}
