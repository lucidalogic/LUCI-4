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
public class DecryptionException extends CDOCException {

    public DecryptionException(String message, Exception e) {
        super(message, e);
    }

    public DecryptionException(String message) {
        super(message);
    }
    
}
