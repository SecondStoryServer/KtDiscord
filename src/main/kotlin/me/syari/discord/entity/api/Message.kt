package me.syari.discord.entity.api

import me.syari.discord.entity.Mentionable.Companion.replaceAll

class Message(
    val guild: Guild,
    val channel: TextChannel,
    val member: Member,
    val content: String,
    val mentionMembers: List<Member>,
    val mentionRoles: List<Role>
) {
    val contentDisplay = content.replaceAll(mentionMembers + mentionRoles)

    override fun toString(): String {
        return "Message(#${channel.name} ${member.displayName}: ${contentDisplay})"
    }
}