package me.syari.discord.entity.api

class Message(
    val guild: Guild,
    val channel: TextChannel,
    val member: Member,
    val content: String,
    val mentionMembers: List<Member>,
    val mentionRoles: List<Role>
) {
    val contentDisplay: String
        get() {
            var display = content
            (mentionMembers + mentionRoles).forEach {
                display = display.replace(it.asMentionRegex, it.asMentionDisplay)
            }
            return display
        }

    override fun toString(): String {
        return "Message(#${channel.name} ${member.displayName}: ${contentDisplay})"
    }
}