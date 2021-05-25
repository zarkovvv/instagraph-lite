package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.DTOs.exportDTOs.ExportUserDTO;
import softuni.exam.instagraphlite.models.DTOs.importDTOs.UserImportDTO;
import softuni.exam.instagraphlite.models.entities.Picture;
import softuni.exam.instagraphlite.models.entities.Post;
import softuni.exam.instagraphlite.models.entities.User;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final String USERS_PATH_JSON = "src\\main\\resources\\files\\users.json";

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final PictureRepository pictureRepository;
    private final PostRepository postRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, PictureRepository pictureRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.pictureRepository = pictureRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Boolean аreImported() {
        return this.userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(USERS_PATH_JSON)));
    }

    @Override
    public String importUsers() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        UserImportDTO[] userImportDTOS = this.gson.fromJson(this.readFromFileContent(), UserImportDTO[].class);

        for (UserImportDTO userImportDTO : userImportDTOS) {
            Optional<User> byPath = this.userRepository.findByUsername(userImportDTO.getUsername());
            if (this.validationUtil.isValid(userImportDTO) && byPath.isEmpty()){
                User user = this.modelMapper.map(userImportDTO, User.class);
                Optional<Picture> picture = this.pictureRepository.findByPath(userImportDTO.getProfilePicture());
                if (picture.isPresent()){
                    user.setProfilePicture(picture.get());
                    this.userRepository.saveAndFlush(user);
                    stringBuilder.append(String.format("Successfully imported User: %s", userImportDTO.getUsername())).append(System.lineSeparator());
                } else {
                    stringBuilder.append("Invalid User").append(System.lineSeparator());
                }
            } else {
                stringBuilder.append("Invalid User").append(System.lineSeparator());
            }
        }

        return stringBuilder.toString().trim();
    }

    @Override
    public String exportUsersWithTheirPosts() {
        StringBuilder stringBuilder = new StringBuilder();
        List<User> all = this.userRepository.findAll();
        List<ExportUserDTO> toExport = new ArrayList<>();
        for (User user : all) {
            ExportUserDTO exportUserDTO = this.modelMapper.map(user, ExportUserDTO.class);
            Set<Post> allByUser_id = this.postRepository.findAllByUser_Id(user.getId());
            Set<Post> collect = new LinkedHashSet<>();
            allByUser_id.stream().sorted(Comparator.comparingDouble(f -> f.getPicture().getSize())).forEach(collect::add);
            exportUserDTO.setPosts(collect);
            toExport.add(exportUserDTO);
        }

        toExport.stream().sorted((f1, f2) -> Integer.compare(f2.getPosts().size(), f1.getPosts().size())).forEach(exportUserDTO -> {
            stringBuilder.append(String.format("User: %s", exportUserDTO.getUsername())).append(System.lineSeparator());
            stringBuilder.append(String.format("Posts count: %d", exportUserDTO.getPosts().size())).append(System.lineSeparator());
            for (Post post : exportUserDTO.getPosts()) {
                stringBuilder.append("==Post Details:").append(System.lineSeparator());
                stringBuilder.append(String.format("----Caption: %s", post.getCaption())).append(System.lineSeparator());
                stringBuilder.append(String.format("----Picture size: %.2f", post.getPicture().getSize())).append(System.lineSeparator());
            }
        });

        return stringBuilder.toString().trim();
    }
}
