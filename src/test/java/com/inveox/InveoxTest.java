package com.inveox;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InveoxTest {

    Inveox inveox = new Inveox();

    @Test
    public void test() {
        assertThat(inveox.hello()).isEqualTo("inveox");
    }
}
