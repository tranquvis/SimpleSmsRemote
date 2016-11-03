package tranquvis.simplesmsremote.Data;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.NotImplementedException;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Andreas Kaltenleitner on 18.10.2016.
 */

public class CaptureSettings implements Serializable, Cloneable
{
    private String cameraId = null;
    private int[] resolution; // width, height
    private ImageFormat outputImageFormat;
    private String outputPath;
    private boolean autofocus = false;
    private FlashlightMode flashlight = FlashlightMode.AUTO;

    public CaptureSettings(@Nullable String cameraId, int[] resolution, ImageFormat outputImageFormat,
                           String outputPath)
    {
        this.cameraId = cameraId;
        this.resolution = resolution;
        this.outputImageFormat = outputImageFormat;
        this.outputPath = outputPath;
    }

    public String getCameraId()
    {
        return cameraId;
    }

    public void setCameraId(String cameraId)
    {
        this.cameraId = cameraId;
    }

    public int[] getResolution() {
        return resolution;
    }

    public void setResolution(int[] resolution) {
        this.resolution = resolution;
    }

    public ImageFormat getOutputImageFormat() {
        return outputImageFormat;
    }

    public void setOutputImageFormat(ImageFormat outputImageFormat) {
        this.outputImageFormat = outputImageFormat;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getFileOutputPath()
    {
        String filename = "remotely_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        return outputPath + File.separator + filename;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public boolean isAutofocus() {
        return autofocus;
    }

    public void setAutofocus(boolean autofocus) {
        this.autofocus = autofocus;
    }

    public FlashlightMode getFlashlight()
    {
        return flashlight;
    }

    public void setFlashlight(FlashlightMode flashlight)
    {
        this.flashlight = flashlight;
    }

    public enum FlashlightMode
    {
        AUTO, ON, OFF
    }

    public enum ImageFormat
    {
        JPEG, PNG;

        public Bitmap.CompressFormat getBitmapCompressFormat()
        {
            switch (this)
            {
                case JPEG:
                    return Bitmap.CompressFormat.JPEG;
                case PNG:
                    return Bitmap.CompressFormat.PNG;
                default:
                    throw new NotImplementedException("Image format not implemented");
            }
        }
    }

    public CaptureSettings clone() throws CloneNotSupportedException
    {
        return (CaptureSettings) super.clone();
        /*
        CaptureSettings settings =
                new CaptureSettings(cameraId, resolution, outputImageFormat, outputPath);
        settings.autofocus = autofocus;
        settings.flashlight = flashlight;

        return settings;
        */
    }
}
