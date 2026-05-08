package pl.szelagi.command.container;

import com.google.common.reflect.Reflection;
import org.bukkit.command.CommandSender;
import pl.szelagi.command.CommandHelper;
import pl.szelagi.command.SubCommand;
import pl.szelagi.component.ComponentIndex;
import pl.szelagi.component.container.Container;
import pl.szelagi.manager.ContainerManager;

import java.util.List;

import static pl.szelagi.command.CommandHelper.*;

public class IndexCommand implements SubCommand {
    @Override
    public String getName() {
        return "index";
    }

    @Override
    public String getDescription() {
        return "Shows container index.";
    }

    @Override
    public String getUsage() {
        return "<container>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        var container = CommandHelper.extractContainer(sender, args);
        if (container == null) return;

        try {
            var field = Container.class.getDeclaredField("index");
            field.setAccessible(true);
            var index = (ComponentIndex) field.get(container);

            sender.sendMessage(PREFIX + index.toString());
        } catch (Exception e) {
            sender.sendMessage(PREFIX + ERROR_COLOR + "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return ContainerManager.containers().stream().map(Container::identifier).toList();
    }
}
