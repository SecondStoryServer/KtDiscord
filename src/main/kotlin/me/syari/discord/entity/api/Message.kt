package me.syari.discord.entity.api

class Message(
    val guild: Guild,
    val channel: TextChannel,
    val member: Member,
    val content: String,
    val mentionMembers: List<Member>,
    val mentionRoles: List<Long>
) {
    override fun toString(): String {
        return "Message(#${channel.name} ${member.displayName}: ${content})"
    }
}