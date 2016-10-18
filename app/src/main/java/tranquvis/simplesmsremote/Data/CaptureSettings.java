package tranquvis.simplesmsremote.Data;

import android.graphics.Bitmap;
import android.util.Size;

import java.io.Serializable;

import tranquvis.simplesmsremote.Utils.CameraUtils;

/**
 * Created by Andreas Kaltenleitner on 18.10.2016.
 */

public class CaptureSettings implements Serializable
{
    private Size resolution;
    private Bitmap.CompressFormat compressFormat;
    private String outputPath;
    private boolean autofocus = false;
    private FlashlightMode flashlight = FlashlightMode.AUTO;

    public CaptureSettings(Size resolution, Bitmap.CompressFormat compressFormat,
                             String outputPath)
    {
        this.resolution = resolution;
        this.compressFormat = compressFormat;
        this.outputPath = outputPath;
    }

    public Size getResolution() {
        return resolution;
    }

    public void setResolution(Size resolution) {
        this.resolution = resolution;
    }

    public Bitmap.CompressFormat getCompressFormat() {
        return compressFormat;
    }

    public void setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
    }

    public String getOutputPath() {
        return outputPath;
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
        AUTO, OFF, ON
    }
}
