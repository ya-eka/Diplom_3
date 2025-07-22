package utils;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class User {
    private String name;
    private String email;
    private String password;
}
