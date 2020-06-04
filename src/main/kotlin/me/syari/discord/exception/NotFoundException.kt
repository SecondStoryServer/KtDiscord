package me.syari.discord.exception

class NotFoundException(remote: Boolean = false): Exception("the entity no longer exists (remote: $remote)")