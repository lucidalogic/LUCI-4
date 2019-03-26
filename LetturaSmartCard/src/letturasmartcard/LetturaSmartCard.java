/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package letturasmartcard;

import java.util.List;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;


public class LetturaSmartCard {
    
    public static void main(String[] args) throws CardException {

        // stampo lista lettori 
        TerminalFactory factory = TerminalFactory.getDefault();
        //System.out.println("entre");
        List<CardTerminal> terminals = factory.terminals().list();
        
        System.out.println("Smart card reader list: " + terminals);

        // prendo il primo lettore
        CardTerminal terminal = terminals.get(0);

        System.out.println("Using reader: " + terminal);
        //stabilisco connessione con la carta

        Card card = terminal.connect("*");
        // System.out.println(card.getProtocol());
        System.out.println("\n\nSmart Card use: " + card);
        CardChannel channel = card.getBasicChannel();


        ATR atr = card.getATR();
        byte[] ATR = atr.getBytes();
        byte[] TuttaCarta;
        System.out.println("ATR of the card: " + LetturaSmartCard.bytesToHex(ATR));


        //------------------------------------lettura---------------------------

        //byte[] selectVpay = {(byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00, (byte) 0x07, (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x20, (byte) 0x20, (byte) 0x00};
        byte[] selectMastercard = {(byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00, (byte) 0x07, (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x10, (byte) 0x10, (byte) 0x00};
        //byte[] selectVisaElectron = {(byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00, (byte) 0x07, (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x20, (byte) 0x10, (byte) 0x00};
        //byte[] bo = {(byte) 0x00, (byte) 0xB2, (byte) 0x00, (byte) 0x03, (byte) 0x60};
        //byte[] selectMaestro = {(byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00, (byte) 0x07, (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x30, (byte) 0x60, (byte) 0x00};
        byte[] getProcessingOptions = {(byte) 0x80, (byte) 0xA8, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x83, (byte) 0x00, (byte) 0x00};
        byte[] readRecord = {(byte) 0x00, (byte) 0xB2, (byte) 0x01, (byte) 0x0C, (byte) 0x00};
        //byte[] lettura = {(byte) 0x00, (byte) 0xB0, (byte) 0x60, (byte) 0x10, (byte) 0x00};
      //  byte[] digitalSignature
        ResponseAPDU r = null;


        CommandAPDU capdu = new CommandAPDU(readRecord);
        r = card.getBasicChannel().transmit(capdu);
        TuttaCarta = r.getBytes();
        System.out.println(LetturaSmartCard.bytesToHex(TuttaCarta));

        capdu = new CommandAPDU(getProcessingOptions);
        r = card.getBasicChannel().transmit(capdu);
        TuttaCarta = r.getBytes();
        System.out.println(LetturaSmartCard.bytesToHex(TuttaCarta));


        capdu = new CommandAPDU(readRecord);
        r = card.getBasicChannel().transmit(capdu);
        TuttaCarta = r.getBytes();
        System.out.println(LetturaSmartCard.bytesToHex(TuttaCarta));


       
        //----------------------------------------------------------------------


        // disconnect
        card.disconnect(false);


    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%02x", bytes[i]));
        }
        //System.out.println("sb: "+ sb);
        return sb.toString();
    }

}