package st.cs.uni.saarland.de.stat;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import soot.PackManager;
import soot.Scene;
import st.cs.uni.saarland.de.helpClasses.Helper;
import st.cs.uni.saarland.de.testApps.TestApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by avdiienko on 09/01/16.
 */
public class LOC {
    public static void main(String[] args){
        LOCSettings settings = new LOCSettings();
        JCommander jc = new JCommander(settings);

        try {
            jc.parse(args);
        }
        catch (ParameterException e){
            System.err.println(e.getMessage());
            jc.usage();
            System.exit(1);
        }
        TestApp.initializeSootForUiAnalysis(settings.apkPath, settings.androidJar, false, false);
        PackManager.v().runPacks();

        StringBuilder stringBuilder = new StringBuilder();
        Scene.v().getApplicationClasses().stream().filter(x->!x.isPhantom() && !Helper.isClassInSystemPackage(x.getName())).forEach(cl->{
            cl.getMethods().stream().filter(x->x.hasActiveBody()).forEach(method->{
                String line = String.format("%s;%s\n", method.getSignature(), method.getActiveBody().getUnits().size());
                stringBuilder.append(line);
            });
        });
        File apkPathFile = new File(settings.apkPath);
        String apkName = apkPathFile.getName();
        try {
            PrintWriter out = new PrintWriter(String.format("%s/%s_loc.txt", settings.outputFolder, apkName));
            out.println(stringBuilder.toString());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
