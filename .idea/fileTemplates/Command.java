#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import android.content.Context;
import android.support.annotation.NonNull;

import org.intellij.lang.annotations.Language;

import tranquvis.simplesmsremote.CommandManagement.Commands.Command;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

#parse("File Header.java")
public class ${NAME} extends Command{
    @Language("RegExp")
    private static final String PATTERN_ROOT = "";
    
    public ${NAME}(@NonNull Module module)
    {
        super(module);
        
        this.titleRes = R.string.command_title_;
        this.syntaxDescList =  new String[]{
                ""
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.DO_NOT_MATCH
        );
    }
    
    @Override
    public void execute(Context context, CommandInstance commandInstance,
                           CommandExecResult result) throws Exception
    {
        
    }
}