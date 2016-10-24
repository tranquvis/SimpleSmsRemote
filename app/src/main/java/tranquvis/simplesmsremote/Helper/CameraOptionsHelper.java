package tranquvis.simplesmsremote.Helper;

import org.intellij.lang.annotations.Language;

/**
 * Created by Andreas Kaltenleitner on 24.10.2016.
 */

public class CameraOptionsHelper
{
    public enum TakePhotoOptions
    {
        FRONT_CAMERA("front camera", "(cam(era)?\\s+front)|(front\\s+cam(era)?)"),
        BACK_CAMERA("back camera", "(cam(era)?\\s+back)|(back\\s+cam(era)?)"),
        CAMERA_INDEX("camera index", "cam(era)?\\s+\\d{1,2}"),
        FLASH_ON,
        FLASH_OFF,
        FLASH_AUTO;

        private String name;
        private String matcher;

        /**
         * Create option for taking a photo.
         * @param name option name
         * @param matcher regular expression, which defines which strings match this option
         */
        TakePhotoOptions(String name, @Language("RegExp") String matcher)
        {
            String s = "";
            this.name = name;
            this.matcher = matcher;
        }

        public String getName()
        {
            return name;
        }

        public static TakePhotoOptions FromOption(String option)
        {
            for (TakePhotoOptions o : values())
            {
                if(option.matches(o.matcher))
                    return o;
            }
            return null;
        }
    }
}
