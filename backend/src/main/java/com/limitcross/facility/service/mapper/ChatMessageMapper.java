package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.ChatMessage;
import com.limitcross.facility.domain.ChatThread;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.ChatMessageDTO;
import com.limitcross.facility.service.dto.ChatThreadDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatMessage} and its DTO {@link ChatMessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatMessageMapper extends EntityMapper<ChatMessageDTO, ChatMessage> {
    @Mapping(target = "sender", source = "sender", qualifiedByName = "userLogin")
    @Mapping(target = "thread", source = "thread", qualifiedByName = "chatThreadId")
    ChatMessageDTO toDto(ChatMessage s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("chatThreadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChatThreadDTO toDtoChatThreadId(ChatThread chatThread);
}
