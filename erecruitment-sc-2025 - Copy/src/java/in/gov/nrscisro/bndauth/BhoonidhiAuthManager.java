/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.gov.nrscisro.bndauth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import java.lang.reflect.Field;
import java.security.Key;
import java.util.Base64;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

import ehiring.db.DBConnectionManager;

import javax.crypto.spec.SecretKeySpec;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author user
 */
public class BhoonidhiAuthManager {

    private final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    private final String ORACLE_DATE_FORMATTER = "yyyy-MM-dd HH24:mi:ss";
    private final long HOURS_LOGIN_VALID_FOR = 120;
    private final int REFRESH_FOR_EVERY_MINUTES = 7200;
    private final String BHOONIDHI_SECRET_KEY = "PWP&EDA_SOFTWARE_GROUP_DPPA&WAA_NRSC_QWEERADSXVCADSFSDASFAS";
    public String USERNAME = "";
    public String IPADDRESS = "";
    public int SESSIONID = 0;

    
   
	// public ResultSet rs = null;
	DBConnectionManager conMgr = null;
	String lPoolName = "recruit";
	
    private java.sql.Connection conn;

    private boolean createdConn = false;
    private final String TABLE_NAME = "BHOONIDHISESSIONS";

    public BhoonidhiAuthManager() {
    	conMgr = DBConnectionManager.getInstance();
    }

 
    public String startSession(String UserId, String IPAddress) throws BhoonidhiAuthException, SQLException {

        //BhoonidhiAuthManager bh=new BhoonidhiAuthManager();
        int sessionId = 0;
        conn = conMgr.getConnection(lPoolName);
        if (conn == null) {
            throw new BhoonidhiAuthException("Database connection failed");
        }

        PreparedStatement getSessionId;

        getSessionId = conn.prepareStatement("select max(sessionid) as id from " + TABLE_NAME + " where userid=?");
        getSessionId.setString(1, UserId);
        ResultSet rst = getSessionId.executeQuery();
        while (rst.next()) {
            sessionId = rst.getInt("id");
        }
        int finalsession = sessionId + 1;
        //java.util.Date date= new java.util.Date();
        //long time = date.getTime();

        LocalDateTime issuedAtTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        String issuedAtTimeStr = issuedAtTime.format(formatter);

        Duration duration = Duration.ofHours(HOURS_LOGIN_VALID_FOR);
        LocalDateTime expiresAtTime = issuedAtTime.plus(duration);
        String expiresAtTimeStr = expiresAtTime.format(formatter);
        //java.sql.Timestamp logInTime = new java.sql.Timestamp(time);

        PreparedStatement createNewSession;
        String query = "INSERT INTO " + TABLE_NAME + " (USERID,LOGGEDINAT,LASTACTIVITYAT,IPADDRESS,SESSIONID,TOKENEXPIRESAT)  VALUES ("
                + "'" + UserId + "'" + ","
                + "to_date('" + issuedAtTimeStr + "','" + ORACLE_DATE_FORMATTER + "')" + ","
                + "to_date('" + issuedAtTimeStr + "','" + ORACLE_DATE_FORMATTER + "')" + ","
                + "'" + IPAddress + "'" + ","
                + finalsession + ","
                + "to_date('" + expiresAtTimeStr + "','" + ORACLE_DATE_FORMATTER + "')"
                + ")";
        //System.out.println(query);
        createNewSession = conn.prepareStatement(query);
 
        int insertCount = createNewSession.executeUpdate();
        //System.out.println("Number of rows inserted into Database:" + insertCount);
   //     conn.commit();
        getSessionId.close();
        createNewSession.close();
        rst.close();
        conMgr.freeConnection(lPoolName,conn);
        
        String jwt = this.generateBhoonidhiJWT(UserId, IPAddress, finalsession, expiresAtTime);
        return jwt;
    }

    public int updateSession(String UserId, String IPAddress, int Sessionid, String issuedat, String expiresat) throws BhoonidhiAuthException, SQLException {

        //BhoonidhiAuthManager bh=new BhoonidhiAuthManager();
        int sessionId = 0;
        conn = conMgr.getConnection(lPoolName);
        if (conn == null) {
            throw new BhoonidhiAuthException("Database connection failed");
        }
        conn.setAutoCommit(false);
        LocalDateTime issuedAtTime = LocalDateTime.now();
        //Timestamp logInTime = Timestamp.valueOf(issuedAtTime);
        String updateQuery = "update " + TABLE_NAME + " set"
                + " LASTACTIVITYAT = to_date('" + issuedat + "','" + ORACLE_DATE_FORMATTER + "')" + ","
                + " TOKENEXPIRESAT = to_date('" + expiresat + "','" + ORACLE_DATE_FORMATTER + "')"
                + " where USERID='" + UserId + "'"
                + " and SESSIONID=" + Sessionid;
        PreparedStatement createNewSession;
        createNewSession = conn.prepareStatement(updateQuery);

        int updateCount = createNewSession.executeUpdate();
        //System.out.println("Number of rows inserted into Database:"+insertCount);
       conn.commit();
        //getSessionId.close();
        createNewSession.close();
        //rst.close();
        conMgr.freeConnection(lPoolName,conn);
        //String jwt=this.generateBhoonidhiJWT(UserId, IPAddress, sessionId);
        return updateCount;
    }

    public String generateBhoonidhiJWT(String UserId, String IPAddress, int SessionID, LocalDateTime expiry) throws BhoonidhiAuthException {

        try {

            System.out.println("Generating Bhoonidhi JWT......");
//            LocalDateTime issuedAtTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
            String expiresAtTimeStr = expiry.format(formatter);
//            String issuedAtTimeStr = issuedAtTime.format(formatter);
//            Duration duration = Duration.ofHours(HOURS_LOGIN_VALID_FOR);
//            LocalDateTime expiresAtTime = issuedAtTime.plus(duration);
            //String expiresAtTimeStr = expiry.toLocalDateTime().format(formatter);
            //LocalDateTime parsetoday=LocalDateTime((String)today);
            //System.out.println(" Bhoonidhi JWT Token Issued at : " + issuedAtTimeStr + "\n and it expires at " + expiresAtTimeStr);
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            String keysecret = BHOONIDHI_SECRET_KEY;
            //System.out.println("Secret Key Combi is \n" + keysecret);
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(keysecret);
            Key key = Keys.hmacShaKeyFor(apiKeySecretBytes);
            String jws = Jwts.builder().setSubject("Auth").
                    claim("UserID", UserId).
                    claim("IPAddress", IPAddress).
                    claim("SessionID", SessionID).
                    claim("expiresAtTime", expiresAtTimeStr).
                    signWith(key).compact();
            //System.out.println("JWT IS : \n" + jws);
            //System.out.println("Key in bytes is : \n"+key.getEncoded());
            return jws;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" Exception occured in Generating JWT ");
            throw new BhoonidhiAuthException("Couldnot Generate JWT");
        }
    }

    public String validateBhoonidhiJWT(String BhoonidhiJWT) throws BhoonidhiAuthException {
        try {
            if (BhoonidhiJWT == null || BhoonidhiJWT.isEmpty() || BhoonidhiJWT.equals(null) || BhoonidhiJWT.equalsIgnoreCase("null")) {
                System.out.println("EMPTY JWT!");
                return "NOT_VALID";
            }
            //String keysecret
            String keysecret = BHOONIDHI_SECRET_KEY;
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(keysecret);
            Key key = Keys.hmacShaKeyFor(apiKeySecretBytes);
            try {
                Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(BhoonidhiJWT);//claims
                Claims c = claims.getBody();
                //System.out.println("Claim : " + c);
                String UserID_from_jwt = c.get("UserID").toString();
                String SessionID_from_jwt = c.get("SessionID").toString();
                String IPAddress_from_jwt = c.get("IPAddress").toString();
                String expiresAtTimeStr_from_jwt = c.get("expiresAtTime").toString();
                int sessionid = Integer.parseInt(SessionID_from_jwt);
                this.USERNAME = UserID_from_jwt;
                this.SESSIONID = sessionid;
                this.IPADDRESS = IPAddress_from_jwt;

                LocalDateTime validationTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
                LocalDateTime expiresAtTime = LocalDateTime.parse(expiresAtTimeStr_from_jwt, formatter);

                if (validationTime.isAfter(expiresAtTime)) {
                    System.out.println("EXPIRED JWT!");
                    return "NOT_VALID";
                }
                int valdb = validateDBSession(UserID_from_jwt, IPAddress_from_jwt, sessionid);
                if (valdb == 0) {
                    System.out.println("BACKEND LOGGED OUT!");
                    return "NOT_VALID";
                }

                String newjwt = refreshBhoonidhiJWT(UserID_from_jwt, IPAddress_from_jwt, sessionid, expiresAtTime, BhoonidhiJWT);
                return newjwt;
                //System.out.println(d);

            } catch (JwtException e) {

                //don't trust the JWT!
                System.out.println("DONT TRUST JWT!");
                //e.printStackTrace();
                return "NOT_VALID";
            }
            // return 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" Exception occured in Validate JWT " + e);
            throw new BhoonidhiAuthException("Couldnot Validate JWT" + e);
        }

    }

    public String refreshBhoonidhiJWT(String UserID, String IPAddress, int SessionID, LocalDateTime expirestime, String jwt) throws BhoonidhiAuthException, SQLException {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime lastUpdate = expirestime.minusHours(HOURS_LOGIN_VALID_FOR);
        long diff = Duration.between(lastUpdate, currentTime).toMinutes();

        LocalDateTime issuedAtTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        String issuedAtTimeStr = issuedAtTime.format(formatter);

        Duration duration = Duration.ofHours(HOURS_LOGIN_VALID_FOR);
        LocalDateTime expiresAtTime = issuedAtTime.plus(duration);
        String expiresAtTimeStr = expiresAtTime.format(formatter);

        String njwt = jwt;
        System.out.println("DIFFERENCE TIME IN MINUTES IS " + diff);
        if (diff >= REFRESH_FOR_EVERY_MINUTES) {
            //refresh
            System.out.println("TOKEN IS REFERESHED");
            njwt = this.generateBhoonidhiJWT(UserID, IPAddress, SessionID, expiresAtTime);
            this.updateSession(UserID, IPAddress, SessionID, issuedAtTimeStr, expiresAtTimeStr);
        } else {
            System.out.println("TOKEN IS not REFERESHED");
        }
        return njwt;
    }

    public int validateDBSession(String UserID, String IPAddress, int SessionID) throws BhoonidhiAuthException, SQLException {

        //validate session flag in database and return either 1 or 0.
        //1 if session is still valid , 0 if session is logged out
    	conn = conMgr.getConnection(lPoolName);
        if (conn == null) {
            throw new BhoonidhiAuthException("Database connection failed");
        }
        LocalDateTime issuedAtTime = LocalDateTime.now();
        Timestamp logInTime = Timestamp.valueOf(issuedAtTime);
        String updateQuery = "select LOGGEDOUTAT as logout from " + TABLE_NAME + " where USERID='" + UserID + "' and SESSIONID=" + SessionID;
        System.out.println(updateQuery);
        PreparedStatement createNewSession;
        createNewSession = conn.prepareStatement(updateQuery);

        //int updateCount=createNewSession.executeUpdate();
        ResultSet rst = createNewSession.executeQuery();
        Timestamp comparetime;
        int d = 0;
        while (rst.next()) {
            //System.out.println(rst.getTimestamp("logout"));
            comparetime = rst.getTimestamp("logout");
            if (comparetime == null) {
                d = 1;
            }

        }

        createNewSession.close();
        conMgr.freeConnection(lPoolName,conn);
        rst.close();

        //String jwt=this.generateBhoonidhiJWT(UserId, IPAddress, sessionId);
        return d;
    }

    public int destroyBhoonidhiSession(String BhoonidhiJWT) throws BhoonidhiAuthException, SQLException {
        //invalidate the flag in the database here.
        int sessionId = 0;
        conn = conMgr.getConnection(lPoolName);
        if (conn == null) {
            throw new BhoonidhiAuthException("Database connection failed");
        }
        try {
            //String keysecret
            String keysecret = BHOONIDHI_SECRET_KEY;
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(keysecret);
            Key key = Keys.hmacShaKeyFor(apiKeySecretBytes);
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(BhoonidhiJWT);//claims
            Claims c = claims.getBody();
            //System.out.println("Claim : " + c);
            String UserId = c.get("UserID").toString();
            String SessionID = c.get("SessionID").toString();
            String IPAddress = c.get("IPAddress").toString();
            //String expiresAtTimeStr_from_jwt = c.get("expiresAtTime").toString();
            //int sessionid=Integer.parseInt(SessionID_from_jwt);
            LocalDateTime logoutTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
            String logoutTimeStr = logoutTime.format(formatter);
            String updateQuery = "update " + TABLE_NAME + " set LOGGEDOUTAT =to_date('" + logoutTimeStr + "','" + ORACLE_DATE_FORMATTER + "') where USERID= '" + UserId + "' and SESSIONID=" + SessionID;
            PreparedStatement createNewSession;
            createNewSession = conn.prepareStatement(updateQuery);
            System.out.println("LOGGED OUT");
            int updateCount = createNewSession.executeUpdate();

            createNewSession.close();

            conMgr.freeConnection(lPoolName,conn);

            return updateCount;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" Exception occured in Validate JWT " + e);
            throw new BhoonidhiAuthException("Couldnot Validate JWT" + e);
        }

    }

    
    String validateSession(String userId, String ipAddress) {
		try {
			String jws =startSession(userId, ipAddress);
			return jws;
		} catch (Exception e) {
			e.printStackTrace();
			return "EXCEPTION";
		}
	}
    
    
    public static void main(String[] args) throws SQLException {

        BhoonidhiAuthManager bam = new BhoonidhiAuthManager();
     //   String UserId = "RADHA KRISHNA";
        String IPADDRESS = "10.1.8.53";
        //String SessionID = "3";
      //  String RandStr = "ASDASDASDASDASDASDASDASDASDASDA";
        //LocalDateTime issuedAtTime = LocalDateTime.now();
        System.out.println("start time :" + LocalDateTime.now());
    //    try {
        //    System.out.println("login start time :" + LocalDateTime.now());
        //    String jws = bam.startSession(UserId, IPADDRESS);
            //String jws="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBdXRoIiwiVXNlcklEIjoiUkFESEEgS1JJU0hOQSIsIklQQWRkcmVzcyI6IjEwLjEuOC41MyIsIlNlc3Npb25JRCI6NCwiZXhwaXJlc0F0VGltZSI6IjIwMTktMDgtMjYgMTE6NDQ6NTkifQ.Sd6NEqMwVHZ5SSy8glvcbSa4dikSOpqAshdEFk7fjiE";
            //String jws = bam.generateBhoonidhiJWT(UserId, IPADDRESS, SessionID);
        //    System.out.println("JWT TOKEN IS :\n" + jws);
          //  String d = bam.validateBhoonidhiJWT(jws);
         //   if (d == "NOT_VALID") {
          //      System.out.println("JWT NOT VALID");
          //  } else {
           //     System.out.println("JWT IS VALID!!!");
           //     System.out.println("NEW JWT IS :\n" + d);
           //     System.out.println(bam.USERNAME);
                //
           //     bam.destroyBhoonidhiSession(d);
          //  }
           // System.out.println("middle time :" + LocalDateTime.now());
      //  } catch (BhoonidhiAuthException ex) {
      //      Logger.getLogger(BhoonidhiAuthManager.class.getName()).log(Level.SEVERE, null, ex);
      //  }

    }

}
