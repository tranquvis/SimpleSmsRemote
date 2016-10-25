package tranquvis.simplesmsremote.Helper;

import org.apache.commons.lang3.NotImplementedException;
import org.intellij.lang.annotations.Language;

import tranquvis.simplesmsremote.Data.CaptureSettings;
import tranquvis.simplesmsremote.Utils.CameraUtils;


/**
 * Created by Andreas Kaltenleitner on 24.10.2016.
 */

public class CameraOptionsHelper
{
    public enum TakePhotoOptions
    {
        FRONT_CAMERA("front camera", "(((cam(era)?)|lens)\\s+front)|(front\\s+((cam(era)?)|lens))"),
        BACK_CAMERA("back camera", "(((cam(era)?)|lens)\\s+back)|(back\\s+((cam(era)?)|lens))"),
        CAMERA_INDEX("camera index", "((cam(era)?)|lens)\\s+\\d{1,2}"),
        FLASH_ON("flash on", "flash(light)?(\\s+(on|enabled))?"),
        FLASH_OFF("flash off", "(flash(light)?\\s+(off|disabled))|(no\\s+flash(light)?)"),
        FLASH_AUTO("flash auto", "(flash(light)?\\s+auto)|(auto\\s+flash(light)?)"),
        AUTOFOCUS_ON("autofocus on", "auto\\s*focus(\\s+(on|enabled))?"),
        AUTOFOCUS_OFF("autofocus off", "(auto\\s*focus\\s+(off|disabled))|(no\\s+auto\\s*focus)");

        private String name;

        @Language("RegExp")
        private String pattern;

        private Class valueType;

        /**
         * Create option for taking a photo.
         * @param name option name
         * @param pattern regular expression, which defines which strings match this option
         */
        TakePhotoOptions(String name, @Language("RegExp") String pattern)
        {
            this.name = name;
            this.pattern = pattern;
        }

        public String getName()
        {
            return name;
        }

        public static TakePhotoOptions FromOption(String option)
        {
            for (TakePhotoOptions o : values())
            {
                if(option.matches(o.pattern))
                    return o;
            }
            return null;
        }

        public Object getValue(String option)
        {
            switch (this)
            {
                case FRONT_CAMERA:
                    return CameraUtils.LensFacing.FRONT;
                case BACK_CAMERA:
                    return CameraUtils.LensFacing.BACK;
                case CAMERA_INDEX:
                    return Integer.parseInt(option.split("\\s+")[1]);
                case FLASH_ON:
                    return CaptureSettings.FlashlightMode.ON;
                case FLASH_OFF:
                    return CaptureSettings.FlashlightMode.OFF;
                case FLASH_AUTO:
                    return CaptureSettings.FlashlightMode.AUTO;
                case AUTOFOCUS_ON:
                    return true;
                case AUTOFOCUS_OFF:
                    return false;
                default:
                    throw new NotImplementedException("Option not implemented");
            }
        }
    }
}
