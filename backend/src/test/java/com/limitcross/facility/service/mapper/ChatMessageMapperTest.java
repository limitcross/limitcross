package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ChatMessageAsserts.*;
import static com.limitcross.facility.domain.ChatMessageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatMessageMapperTest {

    private ChatMessageMapper chatMessageMapper;

    @BeforeEach
    void setUp() {
        chatMessageMapper = new ChatMessageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChatMessageSample1();
        var actual = chatMessageMapper.toEntity(chatMessageMapper.toDto(expected));
        assertChatMessageAllPropertiesEquals(expected, actual);
    }
}
