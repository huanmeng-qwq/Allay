package org.allaymc.api.command;

import org.allaymc.api.entity.interfaces.EntityPlayer;
import org.allaymc.api.i18n.MayContainTrKey;
import org.cloudburstmc.protocol.bedrock.data.command.CommandData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamData;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.Set;

/**
 * @author daoge_cmd
 */
public interface Command {

    /**
     * Prefix for command permissions.
     */
    String COMMAND_PERM_PREFIX = "command.";

    /**
     * Executes this command with the given sender and arguments.
     *
     * @param sender The sender of the command.
     * @param args   The arguments provided to the command.
     *
     * @return The result of the command execution.
     */
    CommandResult execute(CommandSender sender, String[] args);

    /**
     * Builds a network data representation of this command for the given player.
     *
     * @param player The player to build the data for.
     *
     * @return The network data representation of this command.
     */
    CommandData buildNetworkDataFor(EntityPlayer player);

    /**
     * Gets the name of this command.
     *
     * @return The name of this command.
     */
    String getName();

    /**
     * Gets the aliases of this command.
     *
     * @return The aliases of this command.
     */
    @UnmodifiableView
    List<String> getAliases();

    /**
     * Gets the description of this command.
     *
     * @return The description of this command.
     */
    @MayContainTrKey
    String getDescription();

    /**
     * Gets the command overloads of this command.
     *
     * @return The command overloads of this command.
     */
    @UnmodifiableView
    List<CommandParamData[]> getCommandOverloads();

    /**
     * Gets the flags of this command.
     *
     * @return The flags of this command.
     */
    @UnmodifiableView
    Set<CommandData.Flag> getFlags();

    /**
     * Gets the permissions required to execute this command.
     *
     * @return The permissions required to execute this command.
     */
    List<String> getPermissions();
}