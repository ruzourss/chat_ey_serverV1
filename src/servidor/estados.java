package servidor;

/**
 * clase que contiene los diferentes estados
 * que puede tener nuestro servidor.
 * @author Tautvydas Bagocius
 */
public class estados {
    public static final String SEND_NICK = "0x0001";
    public static final String GET_NICK = "0x0002";
    public static final String NICK_ERROR = "0x0003";
    public static final String GET_MESSAGES = "0x0004";
    public static final String EXIT = "0x0005";
}
