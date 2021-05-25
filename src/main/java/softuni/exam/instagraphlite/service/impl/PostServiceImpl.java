package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.DTOs.importDTOs.PostImportDTO;
import softuni.exam.instagraphlite.models.DTOs.importDTOs.PostImportRootDTO;
import softuni.exam.instagraphlite.models.entities.Picture;
import softuni.exam.instagraphlite.models.entities.Post;
import softuni.exam.instagraphlite.models.entities.User;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.util.ValidationUtil;
import softuni.exam.instagraphlite.util.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private static final String POST_PATH_XML = "src\\main\\resources\\files\\posts.xml";
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XMLParser xmlParser;
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XMLParser xmlParser, UserRepository userRepository, PictureRepository pictureRepository) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public Boolean аreImported() {
        return this.postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(POST_PATH_XML)));
    }

    @Override
    public String importPosts() throws IOException, JAXBException {
        StringBuilder stringBuilder = new StringBuilder();
        PostImportRootDTO offerImportRootDTO = this.xmlParser.parseXML(PostImportRootDTO.class, POST_PATH_XML);
        for (PostImportDTO postImportDTO : offerImportRootDTO.getPosts()) {
            if (this.validationUtil.isValid(postImportDTO)) {
                Post post = this.modelMapper.map(postImportDTO, Post.class);
                Optional<User> user = this.userRepository.findByUsername(postImportDTO.getUser().getUsername());
                Optional<Picture> picture = this.pictureRepository.findByPath(postImportDTO.getPicture().getPath());
                if (user.isPresent() && picture.isPresent()){
                    post.setUser(user.get());
                    post.setPicture(picture.get());
                    this.postRepository.saveAndFlush(post);
                    stringBuilder.append(String.format("Successfully imported Post, made by %s", post.getUser().getUsername())).append(System.lineSeparator());
                } else {
                    stringBuilder.append("Invalid Post").append(System.lineSeparator());
                }
            } else {
                stringBuilder.append("Invalid Post").append(System.lineSeparator());
            }
        }

        return stringBuilder.toString().trim();
    }
}
