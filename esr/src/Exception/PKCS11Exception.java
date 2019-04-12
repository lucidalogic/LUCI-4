/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exception;

/**
 *
 * @author Lucida
 */
public class PKCS11Exception extends CDOCException {
    
    public PKCS11Exception(String message, Exception e) {
        super(message, e);
    }

    public PKCS11Exception(String message) {
        super(message);
    }
}
