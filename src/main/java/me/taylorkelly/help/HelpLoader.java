package me.taylorkelly.help;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.reader.UnicodeReader;

public class HelpLoader {

    public static void load(File dataFolder, HelpList list) {
        File helpFolder = new File(dataFolder, "ExtraHelp");
        if (!helpFolder.exists()) {
            helpFolder.mkdirs();
        }
        int count = 0;
        File files[] = helpFolder.listFiles(new YmlFilter());
        if (files == null) {
            return;
        }
        String filesLoaded = "";
        for (File insideFile : files) {
            String fileName = insideFile.getName().replaceFirst(".yml$", "");
            final Yaml yaml = new Yaml(new SafeConstructor());
            Map<String, Object> root;
            FileInputStream input = null;
            try {
                input = new FileInputStream(insideFile);
                root = (Map<String, Object>) yaml.load(new UnicodeReader(input));
                if (root == null || root.isEmpty()) {
                    System.out.println("The file " + insideFile + " is empty");
                    continue;
                }
                int num = 0;
                for (String helpKey : root.keySet()) {
                    Map<String, Object> helpNode = (Map<String, Object>) root.get(helpKey);

                    if (!helpNode.containsKey("command")) {
                        HelpLogger.warning("Help entry node \"" + helpKey + "\" is missing a command name in " + insideFile);
                        continue;
                    }
                    String command = helpNode.get("command").toString();
                    if (!helpNode.containsKey("description")) {
                        HelpLogger.warning(command + "'s Help entry is missing a description");
                        continue;
                    }

                    boolean main = false;
                    String description = helpNode.get("description").toString();
                    boolean visible = true;
                    ArrayList<String> permissions = new ArrayList<String>();

                    if (helpNode.containsKey("main")) {
                        if (helpNode.get("main") instanceof Boolean) {
                            main = (Boolean) helpNode.get("main");
                        } else {
                            HelpLogger.warning(command + "'s Help entry has 'main' as a non-boolean. Defaulting to false");
                        }
                    }

                    if (helpNode.containsKey("visible")) {
                        if (helpNode.get("visible") instanceof Boolean) {
                            visible = (Boolean) helpNode.get("visible");
                        } else {
                            HelpLogger.warning(command + "'s Help entry has 'visible' as a non-boolean. Defaulting to true");
                        }
                    }

                    if (helpNode.containsKey("permissions")) {
                        if (helpNode.get("permissions") instanceof List) {
                            for (Object permission : (List) helpNode.get("permissions")) {
                                permissions.add(permission.toString());
                            }
                        } else {
                            permissions.add(helpNode.get("permissions").toString());
                        }
                    }
                    list.customRegisterCommand(command, description, fileName, main, permissions.toArray(new String[0]), visible);
                    ++num;
                    ++count;
                }
                filesLoaded += fileName + String.format("(%d), ", num);
            } catch (Exception ex) {
                HelpLogger.severe("Error!", ex);
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException ex) {
                }
            }
        }
        //HelpLogger.info(count + " extra help entries loaded" + (filesLoaded.length()>2 ? " from files: " + filesLoaded.replaceFirst(", $", "") : ""));
        HelpLogger.info(count + " extra help entries loaded" + (filesLoaded.length() > 2 ? " from files: " + filesLoaded.substring(0, filesLoaded.length() - 2) : ""));
    }
}
