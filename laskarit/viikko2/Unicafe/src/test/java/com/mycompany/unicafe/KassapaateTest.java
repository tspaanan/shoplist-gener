package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class KassapaateTest {
    
    Kassapaate paate;
    Maksukortti kortti;

    @Before
    public void setUp() {
        paate = new Kassapaate();
        kortti = new Maksukortti(1000);
    }

    @Test
    public void kassassaRahaaAlussaOikein() {
        assertEquals(100000, paate.kassassaRahaa());
    }

    @Test
    public void kassassaAlussaEiMyytyjaLounaita() {
        int lounaitaMyyty = paate.edullisiaLounaitaMyyty() + paate.maukkaitaLounaitaMyyty();
        assertEquals(0, lounaitaMyyty);
    }

    @Test
    public void syoEdullisestiKateisellaLaskeeVaihtorahanOikein() {
        int palautus = paate.syoEdullisesti(500);
        assertEquals(260, palautus);
    }

    @Test
    public void syoEdullisestiKateisellaLisaaKassasaldoaOikein() {
        paate.syoEdullisesti(500);
        assertEquals(100240, paate.kassassaRahaa());
    }

    @Test
    public void syoEdullisestiLaskeeEdullisenLounaanLaskuriin() {
        paate.syoEdullisesti(500);
        assertEquals(1, paate.edullisiaLounaitaMyyty());
    }

    @Test
    public void syoEdullisestiKateisellaPalauttaaLiianVahaisenRahan() {
        int palautus = paate.syoEdullisesti(200);
        assertEquals(200, palautus);
    }
    
    @Test
    public void syoEdullisestiKateisellaLiianVahanRahaaEiLounastaLaskuriin() {
        paate.syoEdullisesti(200);
        assertEquals(0, paate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void syoMaukkaastiKateisellaLaskeeVaihtorahanOikein() {
        int palautus = paate.syoMaukkaasti(500);
        assertEquals(100, palautus);
    }

    @Test
    public void syoMaukkaastiKateisellaLisaaKassasaldoaOikein() {
        paate.syoMaukkaasti(500);
        assertEquals(100400, paate.kassassaRahaa());
    }

    @Test
    public void syoMaukkaastiLaskeeEdullisenLounaanLaskuriin() {
        paate.syoMaukkaasti(500);
        assertEquals(1, paate.maukkaitaLounaitaMyyty());
    }

    @Test
    public void syoMaukkaastiKateisellaPalauttaaLiianVahaisenRahan() {
        int palautus = paate.syoMaukkaasti(200);
        assertEquals(200, palautus);
    }
    
    @Test
    public void syoMaukkaastiKateisellaLiianVahanRahaaEiLounastaLaskuriin() {
        paate.syoMaukkaasti(200);
        assertEquals(0, paate.maukkaitaLounaitaMyyty());
    }

    @Test
    public void syoEdullisestiKortillaVeloittaaKorttiaOikein() {
        assertTrue(paate.syoEdullisesti(kortti));
        assertEquals(760, kortti.saldo());
    }

    @Test
    public void syoEdullisestiKortillaLaskeeEdullisenLounaanLaskuriin() {
        paate.syoEdullisesti(kortti);
        assertEquals(1, paate.edullisiaLounaitaMyyty());
    }

    @Test
    public void syoEdullisestiKortillaEiTeeMitaanJosKortillaEiRahaa() {
        paate.syoMaukkaasti(kortti);
        paate.syoMaukkaasti(kortti);
        // nyt kortin saldon on 200
        assertFalse(paate.syoEdullisesti(kortti));
        assertEquals(200, kortti.saldo());
        assertEquals(0, paate.edullisiaLounaitaMyyty());
    }

    @Test
    public void syoEdullisestiEiMuutaKassasaldoa() {
        paate.syoEdullisesti(kortti);
        assertEquals(100000, paate.kassassaRahaa());
    }
    
    @Test
    public void syoMaukkaastiKortillaVeloittaaKorttiaOikein() {
        assertTrue(paate.syoMaukkaasti(kortti));
        assertEquals(600, kortti.saldo());
    }

    @Test
    public void syoMaukkaastiKortillaLaskeeMaukkaanLounaanLaskuriin() {
        paate.syoMaukkaasti(kortti);
        assertEquals(1, paate.maukkaitaLounaitaMyyty());
    }

    @Test
    public void syoMaukkaastiKortillaEiTeeMitaanJosKortillaEiRahaa() {
        paate.syoMaukkaasti(kortti);
        paate.syoMaukkaasti(kortti);
        // nyt kortin saldon on 200
        assertFalse(paate.syoMaukkaasti(kortti));
        assertEquals(200, kortti.saldo());
        assertEquals(2, paate.maukkaitaLounaitaMyyty());
    }

    @Test
    public void syoMaukkaastiEiMuutaKassasaldoa() {
        paate.syoMaukkaasti(kortti);
        assertEquals(100000, paate.kassassaRahaa());
    }

    @Test
    public void lataaRahaaKortilleMuuttaaKortinSaldoa() {
        paate.lataaRahaaKortille(kortti, 1000);
        assertEquals(2000, kortti.saldo());
    }

    @Test
    public void lataaRahaaKortilleLisaaKassasaldoa() {
        paate.lataaRahaaKortille(kortti, 1000);
        assertEquals(101000, paate.kassassaRahaa());
    }

    @Test
    public void lataaRahaaKortilleEiTeeMitaanNegatiivisellaSummalla() {
        paate.lataaRahaaKortille(kortti, -1000);
        assertEquals(1000, kortti.saldo());
        assertEquals(100000, paate.kassassaRahaa());
    }
}
