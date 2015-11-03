package com.spinn3r.artemis.util.crypto;

import com.google.common.base.Charsets;
import com.spinn3r.artemis.util.misc.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Simple encrypter so that we can encrypt a string... and decrypt it...
 */
public class Encrypter {

    //private static final Logger log = Logger.getLogger();

    private static final String INIT_VECTOR = "paamsvpoijepfoia";

    private static final String KEY = "noppuzjenvouzmem";

    private static final String AES = "AES";
    private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5PADDING";

    // http://stackoverflow.com/questions/15554296/simple-java-aes-encrypt-decrypt-example

    public static String encrypt( String value ) {

        try {

            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes( Charsets.UTF_8 ));

            SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes( Charsets.UTF_8 ), AES );

            Cipher cipher = Cipher.getInstance( AES_CBC_PKCS5_PADDING );
            cipher.init( Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.encode( encrypted );

        } catch (Exception ex) {
            throw new RuntimeException( ex );
        }

    }

    public static String decrypt(String encrypted) {

        if ( encrypted == null )
            throw new NullPointerException( "encrypted" );

        try {

            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes( Charsets.UTF_8 ));

            SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes( Charsets.UTF_8 ), AES );

            Cipher cipher = Cipher.getInstance( AES_CBC_PKCS5_PADDING );
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode( encrypted ));

            return new String(original);

        } catch (Exception ex) {
            //log.error( "Unable to decrypt: " + encrypted );
            throw new RuntimeException( ex );
        }

    }

}
