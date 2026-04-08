    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.gov.nrscisro.bndauth;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import java.security.Key;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author user
 */
public class JWTManage {
    
    public static void main(String[] args)   {

    System.out.println("STARTED JWT AUTH");
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    String keysecret="RADHAKRISHNA+SECRETKEYasdasdasdassdasdassdasdasdasd";
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(keysecret);
    int i=0;
    int j=100;
    Key key=Keys.hmacShaKeyFor(apiKeySecretBytes);
    //Key key = new SecretKeySpec(apiKeySecretBytes,signatureAlgorithm.getJcaName());
    //Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    //String secretkey="RADHAKRISHNA,ASDASDASDASDDASSDASD";
    //Key key1 = Keys.hmacShaKeyFor(key.);
    //SecretKey key2 = new SecretKeySpec(data, 0, data.length, "DES");
    String jws = Jwts.builder().setSubject("BHOONIDHI").claim("hello", "world").claim("hello1", "world").claim("hello2", "world").signWith(key).compact();
    System.out.println("JWT IS : \n"+jws);
    System.out.println("Key in bytes is : \n"+key.getEncoded());
    //System.out.println("Key1 in bytes is : \n"+apiKeySecretBytes);
    assert Jwts.parser().setSigningKey(key).parseClaimsJws(jws).getBody().getSubject().equals("Joe");
    try {

    Object d=Jwts.parser().setSigningKey(key).parseClaimsJws(jws).getBody();

    System.out.println(d);

    } catch (JwtException e) {

        //don't trust the JWT!
    }
    }
}
