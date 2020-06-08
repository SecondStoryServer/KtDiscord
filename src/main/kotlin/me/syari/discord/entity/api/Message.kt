package me.syari.discord.entity.api

import me.syari.discord.entity.Mentionable.Companion.replaceAll

class Message(
    val channel: TextChannel,
    val member: Member,
    val content: String,
    val mentionMembers: List<Member>,
    val mentionRoles: List<Role>,
    val mentionChannels: List<TextChannel>
) {
    val contentDisplay = content.replaceAll(mentionMembers + mentionRoles + mentionChannels)

    override fun toString(): String {
        return "Message(#${channel.name} ${member.displayName}: ${contentDisplay})"
    }
}