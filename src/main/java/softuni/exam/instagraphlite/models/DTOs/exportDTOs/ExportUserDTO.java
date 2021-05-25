package softuni.exam.instagraphlite.models.DTOs.exportDTOs;

import softuni.exam.instagraphlite.models.entities.Post;

import java.util.Set;

public class ExportUserDTO {

    private String username;
    private Set<Post> posts;

    public ExportUserDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
}
