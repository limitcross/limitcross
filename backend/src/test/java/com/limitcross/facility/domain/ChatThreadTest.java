package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.ChatMessageTestSamples.*;
import static com.limitcross.facility.domain.ChatThreadTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ChatThreadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatThread.class);
        ChatThread chatThread1 = getChatThreadSample1();
        ChatThread chatThread2 = new ChatThread();
        assertThat(chatThread1).isNotEqualTo(chatThread2);

        chatThread2.setId(chatThread1.getId());
        assertThat(chatThread1).isEqualTo(chatThread2);

        chatThread2 = getChatThreadSample2();
        assertThat(chatThread1).isNotEqualTo(chatThread2);
    }

    @Test
    void bookingTest() {
        ChatThread chatThread = getChatThreadRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        chatThread.setBooking(bookingBack);
        assertThat(chatThread.getBooking()).isEqualTo(bookingBack);

        chatThread.booking(null);
        assertThat(chatThread.getBooking()).isNull();
    }

    @Test
    void messageTest() {
        ChatThread chatThread = getChatThreadRandomSampleGenerator();
        ChatMessage chatMessageBack = getChatMessageRandomSampleGenerator();

        chatThread.addMessage(chatMessageBack);
        assertThat(chatThread.getMessages()).containsOnly(chatMessageBack);
        assertThat(chatMessageBack.getThread()).isEqualTo(chatThread);

        chatThread.removeMessage(chatMessageBack);
        assertThat(chatThread.getMessages()).doesNotContain(chatMessageBack);
        assertThat(chatMessageBack.getThread()).isNull();

        chatThread.messages(new HashSet<>(Set.of(chatMessageBack)));
        assertThat(chatThread.getMessages()).containsOnly(chatMessageBack);
        assertThat(chatMessageBack.getThread()).isEqualTo(chatThread);

        chatThread.setMessages(new HashSet<>());
        assertThat(chatThread.getMessages()).doesNotContain(chatMessageBack);
        assertThat(chatMessageBack.getThread()).isNull();
    }

    @Test
    void professionalTest() {
        ChatThread chatThread = getChatThreadRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        chatThread.setProfessional(professionalBack);
        assertThat(chatThread.getProfessional()).isEqualTo(professionalBack);

        chatThread.professional(null);
        assertThat(chatThread.getProfessional()).isNull();
    }
}
