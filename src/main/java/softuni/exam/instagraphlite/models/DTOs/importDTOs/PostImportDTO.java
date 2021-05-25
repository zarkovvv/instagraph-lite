package softuni.exam.instagraphlite.models.DTOs.importDTOs;

import org.hibernate.validator.constraints.Length;
import softuni.exam.instagraphlite.models.entities.User;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "post")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostImportDTO {

    @XmlElement(name = "caption")
    private String caption;
    @XmlElement(name = "user")
    private UserImportXmlDTO user;
    @XmlElement(name = "picture")
    private PictureImportXmlDTO picture;

    public PostImportDTO() {
    }

    @NotNull
    @Length(min = 21)
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @NotNull
    public UserImportXmlDTO getUser() {
        return user;
    }

    public void setUser(UserImportXmlDTO user) {
        this.user = user;
    }

    @NotNull
    public PictureImportXmlDTO getPicture() {
        return picture;
    }

    public void setPicture(PictureImportXmlDTO picture) {
        this.picture = picture;
    }
}
