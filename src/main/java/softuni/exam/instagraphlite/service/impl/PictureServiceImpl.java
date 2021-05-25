package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.DTOs.importDTOs.PictureImportJsonDTO;
import softuni.exam.instagraphlite.models.entities.Picture;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

@Service
public class PictureServiceImpl implements PictureService {

    private static final String PICTURES_PATH_JSON = "src\\main\\resources\\files\\pictures.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final PictureRepository pictureRepository;
    private final ValidationUtil validationUtil;

    @Autowired
    public PictureServiceImpl(ModelMapper modelMapper, Gson gson, PictureRepository pictureRepository, ValidationUtil validationUtil) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.pictureRepository = pictureRepository;
        this.validationUtil = validationUtil;
    }

    @Override
    public Boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(PICTURES_PATH_JSON)));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        PictureImportJsonDTO[] pictureImportJsonDTOS = this.gson.fromJson(this.readFromFileContent(), PictureImportJsonDTO[].class);

        for (PictureImportJsonDTO pictureImportJsonDTO : pictureImportJsonDTOS) {
            Optional<Picture> byPath = this.pictureRepository.findByPath(pictureImportJsonDTO.getPath());
            if (this.validationUtil.isValid(pictureImportJsonDTO) && byPath.isEmpty()){
                this.pictureRepository.saveAndFlush(this.modelMapper.map(pictureImportJsonDTO, Picture.class));
                stringBuilder.append(String.format("Successfully imported picture with size %.2f", pictureImportJsonDTO.getSize())).append(System.lineSeparator());
            } else {
                stringBuilder.append("Invalid Picture").append(System.lineSeparator());
            }
        }

        return stringBuilder.toString().trim();
    }

    @Override
    public String exportPictures() {
        StringBuilder stringBuilder = new StringBuilder();

        Set<Picture> allBySizeIsGreaterThanOrderBySizeAsc = this.pictureRepository.findAllBySizeIsGreaterThanOrderBySizeAsc(30000.00);

        for (Picture picture : allBySizeIsGreaterThanOrderBySizeAsc) {
            stringBuilder.append(String.format("%.2f - %s", picture.getSize(), picture.getPath())).append(System.lineSeparator());
        }
        return stringBuilder.toString().trim();
    }
}
