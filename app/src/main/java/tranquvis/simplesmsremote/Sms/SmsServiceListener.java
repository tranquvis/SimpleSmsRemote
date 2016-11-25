package tranquvis.simplesmsremote.Sms;

/**
 * Created by Andreas Kaltenleitner on 24.08.2016.
 */
public interface SmsServiceListener {
    void OnSmsSent(MyMessage sms, int resultCode);

    void OnSmsDelivered(MyMessage sms, int resultCode);
}
