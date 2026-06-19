package ru.itis.skins_marketplace.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEditProfile {

    private String name;

    private String email;

    private MultipartFile avatar;
}
