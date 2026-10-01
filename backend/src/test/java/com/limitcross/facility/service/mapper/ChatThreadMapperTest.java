package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ChatThreadAsserts.*;
import static com.limitcross.facility.domain.ChatThreadTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatThreadMapperTest {

    private ChatThreadMapper chatThreadMapper;

    @BeforeEach
    void setUp() {
        chatThreadMapper = new ChatThreadMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChatThreadSample1();
        var actual = chatThreadMapper.toEntity(chatThreadMapper.toDto(expected));
        assertChatThreadAllPropertiesEquals(expected, actual);
    }
}
