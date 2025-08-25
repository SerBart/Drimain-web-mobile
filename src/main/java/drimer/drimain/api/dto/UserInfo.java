package drimer.drimain.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    private String username;
    private List<String> roles;
    
    public UserInfo(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }
}