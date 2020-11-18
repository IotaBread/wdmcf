import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.*;
import java.util.Scanner;
import java.io.File;

public class Generate {
    public static void main(String[] args) {
        if (args.length < 1 || !Pattern.compile("^([a-z]+-)*[a-z]+$").matcher(args[0]).matches()) {
            System.out.println("Usage: java Generate <mod-name> [<packagename>] [<ClassName>] [<Mod Name>]");
            return;
        }

        String MODID = args[0];
        String MODNAME = toSplittedCamelCase(MODID);
        String PKGNAME = args.length > 1 ? args[1] : MODID.replaceAll("-", "");
        String CLASSNAME = args.length > 2 ? args[2] : toCamelCase(MODID);

        System.out.println(String.format("New mod ID = \"%s\"", MODID));
        System.out.println(String.format("New mod name = \"%s\"", MODNAME));
        System.out.println(String.format("New package name = \"%s\"", PKGNAME));
        System.out.println(String.format("New class name = \"%s\"", CLASSNAME));

        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you sure? [Y/N]");

        String response = scanner.nextLine();
        if (!Pattern.compile("^[yY][eE][sS]|[yY]$").matcher(response).matches()) {
            System.out.println("Aborting.");
            return;
        }

        // Checks if the file already exists and deletes it if so
        try {
            File mainIml = new File(String.format("./.idea/modules/%s.main.iml", MODID));
            if (mainIml.exists()) mainIml.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        move("./.idea/modules/mod-skeleton.main.iml", ".idea/modules/" + MODID + ".main.iml"); // Changes the name of mod-skeleton.main.iml to %MODID%.main.iml

        // Checks if the file already exists and deletes it if so
        try {
            File testIml = new File(String.format("./.idea/modules/%s.test.iml", MODID));
            if (testIml.exists()) testIml.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        move("./.idea/modules/mod-skeleton.test.iml", ".idea/modules/" + MODID + ".test.iml"); // Changes the name of mod-skeleton.test.iml to %MODID%.test.iml

        replace("./.idea/runConfigurations/Minecraft_Client.xml", "mod-skeleton", MODID); // Changes any occurrence of mod-skeleton to %MODID%
        replace("./.idea/runConfigurations/Minecraft_Server.xml", "mod-skeleton", MODID); // Changes any occurrence of mod-skeleton to %MODID%

        replace("./gradle.properties", "mod-skeleton", MODID); // Updates the archives base name on the gradle.properties
        replace("./gradle.properties", "io.github.bymartrixx", "io.github.bymartrixx." + PKGNAME); // Updates the maven group on the gradle.properties
        replace("./settings.gradle", "mod-skeleton", MODID); // Doesn't make any changes right now

        replace("./src/main/java/io/github/bymartrixx/skeleton/ModSkeleton.java", ".skeleton", "." + PKGNAME); // Updates the package name on the main class
        replace("./src/main/java/io/github/bymartrixx/skeleton/ModSkeleton.java", "ModSkeleton", CLASSNAME); // Changes the main class name
        replace("./src/main/java/io/github/bymartrixx/skeleton/ModSkeleton.java", "mod-skeleton", MODID); // Changes the MOD_ID string on the main class
        replace("./src/main/java/io/github/bymartrixx/skeleton/ModSkeleton.java", "Mod Skeleton", MODNAME); // Changes the MOD_NAME string on the main class

        // Changes the main class file name
        move("./src/main/java/io/github/bymartrixx/skeleton/ModSkeleton.java", "./src/main/java/io/github/bymartrixx/skeleton/" + CLASSNAME + ".java");
        move("./src/main/java/io/github/bymartrixx/skeleton", "./src/main/java/io/github/bymartrixx/" + PKGNAME); // Moves the main package

        replace("./src/main/resources/fabric.mod.json", "mod-skeleton", MODID); // Replaces the mod id on the mod.json file
        replace("./src/main/resources/fabric.mod.json", ".skeleton", "." + PKGNAME); // Updates the package name on the mod.json file
        replace("./src/main/resources/fabric.mod.json", "ModSkeleton", CLASSNAME); // Updates the main class name on the mod.json file
        replace("./src/main/resources/fabric.mod.json", "Mod Skeleton", MODNAME); // Updates the mod name on the mod.json file

        move("./src/main/resources/assets/mod-skeleton", "./src/main/resources/assets/" + MODID); // Moves the assets

        try {
            Runtime.getRuntime().exec("cmd /c ping localhost -n 6 > nul && del generate.bat");
            Runtime.getRuntime().exec("cmd /c ping localhost -n 6 > nul && del generate.sh");
            Runtime.getRuntime().exec("cmd /c ping localhost -n 6 > nul && del Generate.class");
            Runtime.getRuntime().exec("cmd /c ping localhost -n 6 > nul && del Generate.java");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static String toCamelCase(String string) {
        String[] strings = string.split("-");
        StringBuilder camelCaseStr = new StringBuilder();
        for (String string1 : strings) {
            camelCaseStr.append(string1.substring(0, 1).toUpperCase()).append(string1.substring(1).toLowerCase());
        }
        return camelCaseStr.toString();
    }

    private static String toSplittedCamelCase(String string) {
        String[] strings = string.split("-");
        StringBuilder camelCaseStr = new StringBuilder();
        for (String string1 : strings) {
            camelCaseStr.append(string1.substring(0, 1).toUpperCase()).append(string1.substring(1).toLowerCase()).append(" ");
        }
        return camelCaseStr.toString().trim();
    }

    private static void replace(String file, String from, String to) {
        try {
            Path path = Paths.get(file);
            Charset charset = StandardCharsets.UTF_8;

            String content = new String(Files.readAllBytes(path), charset);
            content = content.replaceAll(from, to);
            Files.write(path, content.getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void move(String from, String to) {
        try {
            Files.move(Paths.get(from), Paths.get(to));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}