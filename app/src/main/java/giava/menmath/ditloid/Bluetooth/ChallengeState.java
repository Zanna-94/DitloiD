package giava.menmath.ditloid.Bluetooth;

/***
 * String used in {@link BluetoothChallenge} to exchange message.
 *
 * @author MenMath.GiaVa
 */
public interface ChallengeState {

    public static final String INIT = "Init";
    public static final String ID_SENT = "id_sent";
    public static final String GAMING = "gaming";
    public static final String WAIT = "wait";
    public static final String TIMEOUT = "Timeout";
    public static final String BALANCE = "balance";
    public static final String LOST = "Lost";
    public static final String WIN = "Win";

}
