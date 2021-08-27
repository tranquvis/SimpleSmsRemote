package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.NonNull;

import org.apache.commons.lang3.NotImplementedException;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;

public abstract class PhoneDependentCommand extends Command {
    protected PhoneDependentCommand(@NonNull Module module) {
        super(module);
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance,
                        CommandExecResult result, DataManager dataManager) throws Exception {
        throw new NotImplementedException("Phone dependent command cannot be executed without a phone");
    }

    @Override
    abstract public void execute(Context context, CommandInstance commandInstance,
                        String phone, CommandExecResult result, DataManager dataManager) throws Exception;
}
