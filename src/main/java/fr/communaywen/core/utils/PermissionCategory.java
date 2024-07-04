package fr.communaywen.core.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Permission categories
 */
public enum PermissionCategory {

    /**
     * Administrative permissions (management commands, etc.)
     */
    ADMIN("admin"),

    /**
     * Regular commands
     */
    COMMAND("command"),
    ;

    /**
     * Permission prefix.
     * <br>
     * Permissions in the plugin <b>SHOULD</b> be prefixed with this prefix. E.g. <code>ayw.command.prout</code>
     */
    public static final @NotNull String PERMISSION_PREFIX = "ayw";

    /**
     * Permission postfix.
     */
    private final @NotNull String postfix;

    /**
     * Create a new permission category.
     *
     * @param postfix the permission postfix
     */
    PermissionCategory(final @NotNull String postfix) {
        this.postfix = postfix;
    }

    /**
     * Get the permission postfix.
     *
     * @return the permission postfix
     */
    public @NotNull String getPostfix() {
        return postfix;
    }

    /**
     * Format a permission with the permission prefix.
     *
     * @param suffix the permission suffix
     * @return the formatted permission
     */
    public @NotNull String formatPermission(final @NotNull String suffix) {
        return PERMISSION_PREFIX + "." + postfix + "." + suffix;
    }

}
