package gui;
import log.Logger;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;

public class MyLoader extends ClassLoader {
    private File file;

    private boolean flag = true;
    public MyLoader(File f) {
        file = f;
    }

    protected Class loadClass(String name,
                                           boolean resolve)
            throws ClassNotFoundException {
        Class result;
        if (!flag){
            result = Class.forName(name);
        }
        else
            result = findClass(file);
        if (resolve)
            resolveClass(result);
        Logger.debug("name  " + name);
        return result;
    }

    protected Class findClass(File file)
            throws ClassNotFoundException {
        var name = file.getName().substring(0,file.getName().length() - 6);
        Class result = null;
        try {
            byte[] classBytes = loadFileAsBytes(file);
            if (flag) {
                flag = false;
                Logger.debug("flag");
                result = defineClass(name, classBytes, 0,
                        classBytes.length);

            }
        } catch (IOException e) {
            throw new ClassNotFoundException(
                    "Cannot load class " + name + ": " + e);
        } catch (ClassFormatError e) {
            throw new ClassNotFoundException(
                    "Format of class file incorrect for class "
                            + name + " : " + e);
        }
        return result;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    protected java.net.URL findResource(String name) {
        File f = file;
        if (f == null)
            return null;
        try {
            return f.toURL();
        } catch (java.net.MalformedURLException e) {
            return null;
        }
    }

    public static byte[] loadFileAsBytes(File file)
            throws IOException {
        byte[] result = new byte[(int) file.length()];
        FileInputStream f = new FileInputStream(file);
        try {
            f.read(result, 0, result.length);
        } finally {
            try {
                f.close();
            } catch (Exception e) {
            }

        }
        return result;
    }
}
