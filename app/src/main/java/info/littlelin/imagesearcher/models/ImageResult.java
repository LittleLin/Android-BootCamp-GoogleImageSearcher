package info.littlelin.imagesearcher.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageResult implements Serializable {
    private static final long serialVersionUID = 0;

    @SerializedName("url")
    public String fullUrl;

    @SerializedName("tbUrl")
    public String thumbUrl;

    public String title;
}
