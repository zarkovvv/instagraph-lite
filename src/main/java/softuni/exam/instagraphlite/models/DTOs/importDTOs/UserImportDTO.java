package softuni.exam.instagraphlite.models.DTOs.importDTOs;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;
import softuni.exam.instagraphlite.models.entities.Picture;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserImportDTO {

    @Expose
    private String username;
    @Expose
    private String password;
    @Expose
    private String profilePicture;

    public UserImportDTO() {
    }

    @NotNull
    @Length(min = 2, max = 19)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotNull
    @Length(min = 4)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


//    @Pattern(regexp = "src\\w+")
    @NotNull
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
