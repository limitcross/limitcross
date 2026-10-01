package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.ChatMessageTestSamples.*;
import static com.limitcross.facility.domain.ChatThreadTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChatMessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatMessage.class);
        ChatMessage chatMessage1 = getChatMessageSample1();
        ChatMessage chatMessage2 = new ChatMessage();
        assertThat(chatMessage1).isNotEqualTo(chatMessage2);

        chatMessage2.setId(chatMessage1.getId());
        assertThat(chatMessage1).isEqualTo(chatMessage2);

        chatMessage2 = getChatMessageSample2();
        assertThat(chatMessage1).isNotEqualTo(chatMessage2);
    }

    @Test
    void threadTest() {
        ChatMessage chatMessage = getChatMessageRandomSampleGenerator();
        ChatThread chatThreadBack = getChatThreadRandomSampleGenerator();

        chatMessage.setThread(chatThreadBack);
        assertThat(chatMessage.getThread()).isEqualTo(chatThreadBack);

        chatMessage.thread(null);
        assertThat(chatMessage.getThread()).isNull();
    }
}
