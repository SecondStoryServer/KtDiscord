package me.syari.discord.entity.api

import me.syari.discord.entity.Mentionable.Companion.replaceAll

class Message(
    val channel: TextChannel,
    val member: Member,
    val content: String,
    val mentionMembers: List<Member>,
    val mentionRoles: List<Role>,
    val mentionChannels: List<TextChannel>,
    val mentionEmojis: List<Emoji>
) {
    val contentDisplay = content.replaceAll(mentionMembers + mentionRoles + mentionChannels + mentionEmojis)

    override fun toString(): String {
        return "Message(#${channel.name} ${member.displayName}: ${contentDisplay})"
    }
}