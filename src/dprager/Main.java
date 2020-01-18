package dprager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * Main class to start program
 *
 * @author Erhard List
 * @version infi-1920-01
 */
public class Main {

    /**
     * Starting point for app execution
     *
     * @param args commandline arguments
     */
    public static void main(String[] args) {
        String teacherid = "";
        String filepath = "";

        // get the name of the package this class sits in
        String p = (new Main()).getClass().getPackage().getName();

        // there have to be two commandline arguments
        if (args.length != 2) {
            // give some calling advice
            System.err.println("Syntax:      java " + p + ".Main XML-File TeacherID");
            System.err.println("for example: java " + p + ".Main lehrer.xml lise0");
            System.exit(1);
        } else {
            // teacherids must be 4 or 5 characters long
            if ((args[1].length() < 4) || (args[1].length() > 5)) {
                System.err.println("TeacherID invalid");
                System.exit(2);
            }
            // let's see if the given file is present
            File fp = new File(args[0]);
            if (fp == null || !fp.exists()) {
                System.err.println("Given XML-File doesn't exist");
                System.exit(3);
            }
            // ok, looks good. Any other errors with the data has to be checked by YOU!
            teacherid = args[1].toLowerCase();
            filepath = fp.getAbsolutePath();
        }

        // build the class name we want to call a method from
        String callee = p + ".XMLReader" + p.toUpperCase().charAt(0) + p.toLowerCase().substring(1);

        try {
            // call the needed method - the hard way ;-)
            Class<?> cls = Class.forName(callee);
            Method m = cls.getDeclaredMethod("parseXML", String.class, String.class);
            String s = (String) m.invoke(null, filepath, teacherid);
            System.out.println(s);
            // lots of things can go wrong here, so be catchy
        } catch (ClassNotFoundException e) {
            System.err.println("Die geforderte Klasse " + callee + " wurde nicht gefunden");
        } catch (NoSuchMethodException | SecurityException e) {
            System.err.println("Die geforderte Klasse " + callee + " enthält keine Methode parseXML(String, String).");
        } catch (NullPointerException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            System.err.println("Die geforderte Methode parseXML(String) lässt sich nicht aufrufen. Ist sie static?");
        }

    }

}
