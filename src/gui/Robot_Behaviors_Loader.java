package gui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Robot_Behaviors_Loader {

    Map<String, Method> behaviorDict = new HashMap<String, Method>();

    Robot_Behaviors_Loader(){
// здесь я просто извлекаю имена всех файлов в папке где лежат скомпиленные behavior
        ArrayList<String> classes = new ArrayList<String>();
        try (Stream<Path> walk = Files.walk(Paths.get("C:/Users/danii/IdeaProjects/JAVA_ROBOT_FINAL/Compiled_behaviors"))) {

            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());
            for (String a:result) {
                var temp_lst = a.split("\\\\");
                String fileName = temp_lst[temp_lst.length-1];
                fileName = fileName.split("\\.")[0];
                System.out.println(fileName);
                classes.add(fileName);
            }
            classes.remove("Robot");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            URL classUrl;
            classUrl = new URL("file:///C:/Users/danii/IdeaProjects/JAVA_ROBOT_FINAL/Compiled_behaviors/");
            URL[] classUrls = {classUrl};
            URLClassLoader ucl = new URLClassLoader(classUrls);
            Class robo = ucl.loadClass("Robot");
            for (String className:classes)
            {
                Class c = ucl.loadClass(className);
                try {
                    Method method = c.getMethod("onModelUpdateEvent", new Class[]{robo});
                    behaviorDict.put(className, method);
                }
                catch (NoSuchMethodException e){
                    continue;
                }

            }
        }
        catch (MalformedURLException|
                ClassNotFoundException e){
            e.printStackTrace();
        }

    }
}
