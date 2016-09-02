package tranquvis.simplesmsremote.Services.Sms;

/**
 * Created by Andreas Kaltenleitner on 24.08.2016.
 */
public interface SmsServiceListener
{
    void OnSmsSent(MySms sms, int resultCode);
    void OnSmsDelivered(MySms sms, int resultCode);
}
