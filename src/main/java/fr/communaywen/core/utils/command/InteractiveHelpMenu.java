package fr.communaywen.core.utils.command;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.CommandHandlerVisitor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.help.CommandHelp;
import revxrsal.commands.help.CommandHelpWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * A utility for creating interactive, paginated help menus.
 * <p>
 * Note: This requires adventure APIs to work.
 * <p>
 * <h2>Usage</h2>
 * <ol>
 *     <li>Create a singleton instance of {@link InteractiveHelpMenu} either
 *     using {@link InteractiveHelpMenu#builder()} or {@link InteractiveHelpMenu#create()}
 *     <pre>{@code
 *     public static final InteractiveHelpMenu HELP_MENU = InteractiveHelpMenu.create();
 *     }</pre>
 *     </li>
 *     <li>Register your singleton instance to the {@link CommandHandler}
 *     using {@link CommandHandler#accept(CommandHandlerVisitor)}
 *     <pre>{@code
 *     commandHandler.accept(HELP_MENU);
 *     }</pre>
 *     </li>
 *     <li>Create the commands that you would like to display the help menu in. Send the help
 *     menu using {@link InteractiveHelpMenu#sendInteractiveMenu(Audience, CommandHelp, int, ExecutableCommand)}.
 *     All the parameters should be parameters in the help method, and Lamp will resolve them automatically
 *     to the appropriate values.
 *         <pre>{@code
 * @Command("myplugin")
 * public class PluginCommands {
 *
 *     // Add this method if you want /myplugin to display the help menu
 *     @DefaultFor("~")
 *     public void sendHelp(BukkitCommandActor actor, ExecutableCommand command) {
 *          String helpCommandPath = command.getPath().toRealString() + " help";
 *          actor.getCommandHandler().dispatch(actor, helpCommandPath);
 *     }
 *
 *     @Subcommand("help")
 *     // add other annotations here such as @Usage and @Description...
 *     public void sendHelp(
 *             Audience sender,
 *             CommandHelp<Component> help,
 *             ExecutableCommand thisHelpCommand,
 *             @Default("1") @Range(min = 1) int page
 *     ) {
 *         HELP_MENU.sendInteractiveMenu(sender, help, page, thisHelpCommand);
 *     }
 * }
 * }</pre>
 *     </li>
 * </ol>
 * <p>
 * Note that the help menu lists all the subcommands (excluding the help command) in the
 * same category as the help command. For example:
 * <ul>
 *     <li>/foo bar help: Lists all commands in /foo bar</li>
 *     <li>/foo buzz bar help: Lists all commands in /foo buzz bar</li>
 * </ul>
 * This is why {@link #sendInteractiveMenu(Audience, CommandHelp, int, ExecutableCommand)} requires
 * an {@link ExecutableCommand}, as it uses it to resolve sibling commands that should be
 * listed in the help menu.
 */
public final class InteractiveHelpMenu implements CommandHelpWriter<Component>, CommandHandlerVisitor {

    /**
     * The default number of entries in a single help page
     */
    private static final int DEFAULT_PAGE_SIZE = 8;

    /**
     * The slash command color
     */
    private final String slashCommandColor;

    /**
     * The command name color
     */
    private final String commandNameColor;

    /**
     * The parameters/command usage color
     */
    private final String parametersColor;

    /**
     * The hover tooltip label color
     */
    private final String tooltipLabelsColor;

    /**
     * The page size
     */
    private final int pageSize;

    private InteractiveHelpMenu(String slashCommandColor, String commandNameColor, String parametersColor, String tooltipLabelsColor, int pageSize) {
        this.slashCommandColor = slashCommandColor;
        this.commandNameColor = commandNameColor;
        this.parametersColor = parametersColor;
        this.tooltipLabelsColor = tooltipLabelsColor;
        this.pageSize = pageSize;
    }

    /**
     * Creates a new {@link Builder}
     *
     * @return A new builder
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new {@link InteractiveHelpMenu} with default settings
     *
     * @return A new {@link InteractiveHelpMenu}
     */
    public static @NotNull InteractiveHelpMenu create() {
        return new Builder().build();
    }

    @Override
    public @Nullable Component generate(@NotNull ExecutableCommand command, @NotNull CommandActor actor) {
        if (command.isSecret() || !command.hasPermission(actor))
            return null;
        boolean hasParameters = !command.getValueParameters().isEmpty();
        List<String> tooltip = createTooltip(command);

        StringJoiner desc = new StringJoiner(" ");
        desc.add(slashCommandColor + "/" + commandNameColor + command.getPath().getParent());
        if (command.getPath().size() > 1)
            desc.add(String.join(" ", command.getPath().getSubcommandPath()));
        if (!command.getValueParameters().isEmpty())
            desc.add(parametersColor + command.getUsage());
        return lg(desc.toString() + " §f» §e" +
                (command.getDescription() == null || command.getDescription().isEmpty() ? "§cAucune description" : command.getDescription())).style(Style.style()
                .hoverEvent(HoverEvent.showText(lg(String.join("\n", tooltip))))
                .clickEvent(ClickEvent.suggestCommand('/' + command.getPath().toRealString() + (hasParameters ? " " : ""))));
    }

    private List<String> createTooltip(ExecutableCommand command) {
        List<String> tooltip = new ArrayList<>();
        if (command.getDescription() != null)
            tooltip.add(tooltipLabelsColor + "&lDescription&f: " + command.getDescription());
        if (!command.getValueParameters().isEmpty()) /* check if command has any parameters */
            tooltip.add(tooltipLabelsColor + "&lParamètres&f: " + command.getUsage());
        if (!tooltip.isEmpty())
            tooltip.add("");
        tooltip.add("Cliquez pour le mettre dans votre chat !");
        return tooltip;
    }

    public void sendInteractiveMenu(
            @NotNull Audience target,
            @NotNull CommandHelp<Component> allEntries,
            int page,
            @NotNull ExecutableCommand helpCommand, @NotNull String prefix
    ) {
        int pageCount = allEntries.getPageSize(pageSize);
        int index = coerce(page, 1, pageCount);

        Component totalComponent = Component.empty().append(sendTopBar(target, helpCommand, prefix)).appendNewline();
        Component commandComponent = Component.empty();
        CommandHelp<Component> helpPage = allEntries.paginate(index, pageSize);
        for (Component component : helpPage) {
            commandComponent = commandComponent.append(component).append(Component.newline());
        }
        totalComponent = totalComponent.append(commandComponent)
                .append(sendBottomBar(target, index, pageCount, page, helpCommand));
        target.sendMessage(totalComponent);
    }

    private static Component sendTopBar(Audience audience, ExecutableCommand command, String prefix) {
        return Component.text(prefix + " §8» §fListe des commandes")
                .appendNewline();
    }

    private static Component sendBottomBar(Audience audience, int index, int pageSize, int currentPage, ExecutableCommand command) {
        String previous = "/" + command.getPath().toRealString() + " " + (currentPage - 1);
        String next = "/" + command.getPath().toRealString() + " " + (currentPage + 1);
        return Component.text()
                .appendNewline()
                .append(currentPage <= 1 ? lg("§8[§7§m« Page précédente §8]")
                        .hoverEvent(HoverEvent.showText(lg("&cCeci est la première page")))
                        : lg("§8[§e« Page précédente §8]").clickEvent(ClickEvent.runCommand(previous))
                        .hoverEvent(HoverEvent.showText(lg("&aPage précédente"))))
                .append(lg("&r &8■ &r"))
                .append(
                        currentPage >= pageSize ? lg("§8[§7§mPage suivante »§8]")
                                .hoverEvent(HoverEvent.showText(lg("&cCeci est la dernière page")))
                                : lg("§8[§ePage suivante »§8]").clickEvent(ClickEvent.runCommand(next))
                                .hoverEvent(HoverEvent.showText(lg("&aPage suivante")))
                )
                .build();
    }

    @Override
    public String toString() {
        return "InteractiveHelpMenu(" +
                "slashCommandColor='" + slashCommandColor + '\'' +
                ", commandNameColor='" + commandNameColor + '\'' +
                ", parametersColor='" + parametersColor + '\'' +
                ", tooltipLabelsColor='" + tooltipLabelsColor + '\'' +
                ", pageSize=" + pageSize +
                ')';
    }
    /* Utility methods */

    private static int coerce(int value, int min, int max) {
        return value < min ? min : Math.min(value, max);
    }

    private static Component lg(String m) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(m);
    }

    @Override
    public void visit(@NotNull CommandHandler handler) {
        handler.setHelpWriter(this);
    }

    public static class Builder {
        private String slashCommandColor = "&7";
        private String commandNameColor = "&e";
        private String parametersColor = "&6";
        private String tooltipLabelsColor = "&e";
        private int pageSize = DEFAULT_PAGE_SIZE;

        public Builder slashCommandColor(String slashCommandColor) {
            this.slashCommandColor = slashCommandColor;
            return this;
        }

        public Builder commandNameColor(String commandNameColor) {
            this.commandNameColor = commandNameColor;
            return this;
        }

        public Builder parametersColor(String parametersColor) {
            this.parametersColor = parametersColor;
            return this;
        }

        public Builder tooltipLabelsColor(String tooltipLabelsColor) {
            this.tooltipLabelsColor = tooltipLabelsColor;
            return this;
        }

        public Builder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public InteractiveHelpMenu build() {
            return new InteractiveHelpMenu(this.slashCommandColor, this.commandNameColor, this.parametersColor, this.tooltipLabelsColor, this.pageSize);
        }

        public String toString() {
            return "InteractiveHelpMenu.Builder(slashCommandColor=" + this.slashCommandColor + ", commandNameColor=" + this.commandNameColor + ", parametersColor=" + this.parametersColor + ", tooltipLabelsColor=" + this.tooltipLabelsColor + ", pageSize=" + this.pageSize + ")";
        }
    }
}