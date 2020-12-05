package de.utilities;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class findRole {

    public static Role methode(Member member, String roleID) {

        try {

            List<Role> roles = member.getJDA().getGuildById("763425801391308901").getMemberById(member.getId()).getRoles();

            return roles.stream()
                    .filter(role -> role.getId().equals(roleID)) // filter by role name
                    .findFirst() // take first result
                    .orElse(null); // else return null

        } catch (NullPointerException e) {

        }
        return null;
    }


}
