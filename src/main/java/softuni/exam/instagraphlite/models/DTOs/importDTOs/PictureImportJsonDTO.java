package softuni.exam.instagraphlite.models.DTOs.importDTOs;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class PictureImportJsonDTO {

    @Expose
    private String path;
    @Expose
    private double size;

    public PictureImportJsonDTO() {
    }

    @NotNull
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @DecimalMin(value = "500")
    @DecimalMax(value = "60000")
    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
