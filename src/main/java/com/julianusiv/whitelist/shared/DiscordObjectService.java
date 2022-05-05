package com.julianusiv.whitelist.shared;

import java.util.List;

import com.julianusiv.whitelist.shared.results.BoolCallResult;

import org.bukkit.configuration.file.FileConfiguration;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.Guild;

public class DiscordObjectService {
    private DiscordObjectService() {
    }

    public static BoolCallResult hasModRole(List<Role> memberRoles, Guild guild, FileConfiguration config){
        List<String> roleIds = config.getStringList("allowedModRoleIds");
        for(String roleId : roleIds){
            Role role = guild.getRoleById(roleId);
            if (memberRoles.contains(role)){
                BoolCallResult result = new BoolCallResult();
                result.setSuccess(true);
                result.setValue(true);
                return result;
            }
        }
        BoolCallResult result = new BoolCallResult();
        result.setSuccess(true);
        result.setValue(false);
        return result;
    }

    public static BoolCallResult hasAllowedRole(List<Role> memberRoles, Guild guild, FileConfiguration config){
        List<String> roleIds = config.getStringList("allowedRoleIds");
        for(String roleId : roleIds){
            Role role = guild.getRoleById(roleId);
            if (memberRoles.contains(role)){
                BoolCallResult result = new BoolCallResult();
                result.setSuccess(true);
                result.setValue(true);
                return result;
            }
        }
        BoolCallResult result = new BoolCallResult();
        result.setSuccess(false);
        result.setValue(false);
        return result;
    }
}
