package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }

    @Test
    public void kortinSaldoOnAlussaOikein() {
        assertEquals(10, kortti.saldo());
        // testataan sama myös toString() metodia käyttäen
        assertEquals("saldo: 0.10", kortti.toString());
    }

    @Test
    public void rahanLataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(10);
        assertEquals(20, kortti.saldo());
    }

    @Test
    public void rahanOttaminenPienentääSaldoaOikein() {
        // otaRahaa() palauttaa True, jos onnistuu: saldo muuttuu
        assertTrue(kortti.otaRahaa(2));
        assertEquals(8, kortti.saldo());
    }

    @Test
    public void rahanOttamisenEpaonnistuminenEiPienennaSaldoa() {
        // otaRahaa() palauttaa False, jos ei onnistu: saldo ei muutu
        assertFalse(kortti.otaRahaa(12));
        assertEquals(10, kortti.saldo());
    }
}
